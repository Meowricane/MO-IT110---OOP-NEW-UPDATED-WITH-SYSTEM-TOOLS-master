package com.motorph.service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.motorph.model.AdminUser;
import com.motorph.model.EmployeeUser;
import com.motorph.model.FinanceUser;
import com.motorph.model.HRUser;
import com.motorph.model.ITUser; // NEW: IT user class
import com.motorph.model.User;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

public class AuthenticationService {
    private static final Logger logger = Logger.getLogger(AuthenticationService.class.getName());
    private static final String CREDENTIALS_FILE_PATH = "data/userCredentials.csv";

    private final List<User> users;

    public AuthenticationService() {
        users = new ArrayList<>();
        loadCredentials();
    }

    private void loadCredentials() {
        try (CSVReader reader = new CSVReader(new FileReader(CREDENTIALS_FILE_PATH))) {
            List<String[]> records = reader.readAll();

            for (int i = 1; i < records.size(); i++) { // skip header
                String[] record = records.get(i);
                if (record.length >= 5) {
                    try {
                        String username = record[0].trim();
                        String password = record[1].trim();
                        int employeeId = Integer.parseInt(record[2].trim());
                        String role = record[3].trim();
                        boolean isActive = Boolean.parseBoolean(record[4].trim());

                        User user = createUserByRole(username, password, employeeId, role, isActive);
                        if (user != null) {
                            users.add(user);
                            logger.log(Level.INFO, "Loaded user: {0}", username);
                        } else {
                            logger.log(Level.WARNING, "Skipping unknown role for user: {0}", username);
                        }

                    } catch (NumberFormatException e) {
                        logger.log(Level.WARNING, "Invalid employee ID in credentials file at line {0}", i + 1);
                    }
                } else {
                    logger.log(Level.WARNING, "Invalid record format in credentials file at line {0}", i + 1);
                }
            }

            logger.log(Level.INFO, "Successfully loaded {0} user credentials", users.size());

        } catch (IOException | CsvException e) {
            logger.log(Level.SEVERE, "Failed to load user credentials from file: " + CREDENTIALS_FILE_PATH, e);
            createDefaultAdminUser();
        }
    }

    /** 
     * Factory method to return proper subclass based on role
     */
    private User createUserByRole(String username, String password, int employeeId, String role, boolean isActive) {
        switch (role.toUpperCase()) {
            case "ADMIN":
                return new AdminUser(username, password, employeeId, isActive);
            case "EMPLOYEE":
                return new EmployeeUser(username, password, employeeId, isActive);
            case "HR":
                return new HRUser(username, password, employeeId, isActive);
            case "FINANCE":
                return new FinanceUser(username, password, employeeId, isActive);
            case "IT": // NEW: IT user
                return new ITUser(username, password, employeeId, isActive);
            default:
                return null; // unknown role
        }
    }

    private void createDefaultAdminUser() {
        logger.log(Level.INFO, "Creating default admin user");
        User defaultAdmin = new AdminUser("admin", "admin123", 1, true);
        users.add(defaultAdmin);
    }

    public User authenticateUser(String username, String password) {
        if (username == null || password == null || username.trim().isEmpty() || password.trim().isEmpty()) {
            logger.warning("Authentication attempt with empty username or password");
            return null;
        }

        String trimmedUsername = username.trim();
        String trimmedPassword = password.trim();

        for (User user : users) {
            if (user.getUsername().equals(trimmedUsername) &&
                user.getPassword().equals(trimmedPassword) &&
                user.isActive()) {
                logger.log(Level.INFO, "Successful authentication for user: {0}", trimmedUsername);
                return user;
            }
        }

        logger.log(Level.WARNING, "Failed authentication attempt for username: {0}", trimmedUsername);
        return null;
    }

    public boolean validateLogin(String username, String password) {
        return authenticateUser(username, password) != null;
    }

    public List<User> getActiveUsers() {
        List<User> activeUsers = new ArrayList<>();
        for (User user : users) {
            if (user.isActive()) activeUsers.add(user);
        }
        return activeUsers;
    }

    public int getUserCount() {
        return users.size();
    }

    public boolean isCredentialsFileAvailable() {
        try {
            java.io.File file = new java.io.File(CREDENTIALS_FILE_PATH);
            return file.exists() && file.canRead();
        } catch (Exception e) {
            return false;
        }
    }
}