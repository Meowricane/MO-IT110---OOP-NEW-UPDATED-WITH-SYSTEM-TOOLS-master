package com.motorph.util;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import com.motorph.model.User;

/**
 * Consolidated utility class containing UI helpers, validation, date utilities,
 * error handling, and session management.
 */
public final class AppUtils {

    private static final Logger logger = Logger.getLogger(AppUtils.class.getName());
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy");

    // ========== SESSION MANAGEMENT ==========

    private static User currentUser;

    /**
     * Set the current logged-in user
     */
    public static void setCurrentUser(User user) {
        currentUser = user;
        logger.log(Level.INFO, "User session started: {0}", user != null ? user.getUsername() : "null");
    }

    /**
     * Get the current logged-in user
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /**
     * Check if a user is currently logged in
     */
    public static boolean isUserLoggedIn() {
        return currentUser != null;
    }

    /**
     * Clear the current user session
     */
    public static void clearSession() {
        logger.log(Level.INFO, "User session ended: {0}", currentUser != null ? currentUser.getUsername() : "unknown");
        currentUser = null;
    }

    // ========== UI COMPONENT CREATION ==========

    /**
     * Create a primary button
     */
    public static JButton createPrimaryButton(String text) {
        return createModernButton(text, AppConstants.PRIMARY_BUTTON_COLOR,
                AppConstants.PRIMARY_BUTTON_HOVER, AppConstants.BUTTON_TEXT_COLOR);
    }

    /**
     * Create a secondary button
     */
    public static JButton createSecondaryButton(String text) {
        return createModernButton(text, AppConstants.SECONDARY_BUTTON_COLOR,
                AppConstants.SECONDARY_BUTTON_HOVER, AppConstants.BUTTON_TEXT_COLOR);
    }

    /**
     * Create a danger button
     */
    public static JButton createDangerButton(String text) {
        return createModernButton(text, AppConstants.DELETE_BUTTON_COLOR,
                AppConstants.DELETE_BUTTON_HOVER, AppConstants.BUTTON_TEXT_COLOR);
    }

    /**
     * Create a small action button
     */
    public static JButton createActionButton(String text, boolean isDanger) {
        Color bgColor, hoverColor;
        if (isDanger) {
            bgColor = AppConstants.ACTION_DELETE_COLOR;
            hoverColor = AppConstants.DELETE_BUTTON_HOVER;
        } else if (text.toLowerCase().contains("edit")) {
            bgColor = AppConstants.ACTION_EDIT_COLOR;
            hoverColor = AppConstants.SUCCESS_COLOR;
        } else {
            bgColor = AppConstants.ACTION_VIEW_COLOR;
            hoverColor = AppConstants.PRIMARY_BUTTON_HOVER;
        }

        JButton button = createModernButton(text, bgColor, hoverColor, AppConstants.BUTTON_TEXT_COLOR);
        button.setFont(AppConstants.SMALL_FONT);
        button.setPreferredSize(new Dimension(65, 28));
        return button;
    }

    /**
     * Create a modern card-style panel
     */
    public static JPanel createCardPanel() {
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0, 0, 0, 10));
                g2.fillRoundRect(2, 2, getWidth() - 2, getHeight() - 2, 8, 8);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth() - 2, getHeight() - 2, 8, 8);

                g2.dispose();
            }
        };

        panel.setBackground(AppConstants.CARD_BACKGROUND);
        panel.setBorder(new EmptyBorder(16, 16, 16, 16));
        panel.setOpaque(false);
        return panel;
    }

    /**
     * Create a modern styled text field
     */
    public static JTextField createStyledTextField(String placeholder) {
        JTextField field = new JTextField();
        field.setFont(AppConstants.NORMAL_FONT);
        field.setBorder(new EmptyBorder(8, 12, 8, 12));

        if (placeholder != null && !placeholder.isEmpty()) {
            field.setText(placeholder);
            field.setForeground(AppConstants.TEXT_MUTED);

            field.addFocusListener(new java.awt.event.FocusAdapter() {
                @Override
                public void focusGained(java.awt.event.FocusEvent e) {
                    if (field.getText().equals(placeholder)) {
                        field.setText("");
                        field.setForeground(AppConstants.TEXT_COLOR);
                    }
                }

                @Override
                public void focusLost(java.awt.event.FocusEvent e) {
                    if (field.getText().isEmpty()) {
                        field.setText(placeholder);
                        field.setForeground(AppConstants.TEXT_MUTED);
                    }
                }
            });
        }

        return field;
    }

    /**
     * Create a modern button with hover effects
     */
    private static JButton createModernButton(String text, Color bgColor, Color hoverColor, Color textColor) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), AppConstants.BUTTON_BORDER_RADIUS,
                        AppConstants.BUTTON_BORDER_RADIUS);

                g2.dispose();
                super.paintComponent(g);
            }
        };

        button.setFont(AppConstants.BUTTON_FONT);
        button.setBackground(bgColor);
        button.setForeground(textColor);
        button.setBorder(new EmptyBorder(8, 16, 8, 16));
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setOpaque(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                if (button.isEnabled()) {
                    button.setBackground(hoverColor);
                }
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });

        return button;
    }

    /**
     * Create a navigation button
     */
    public static JButton createNavigationButton(String text) {
        JButton button = createModernButton(text,
                new Color(121, 9, 37),
                new Color(168, 82, 104),
                AppConstants.BUTTON_TEXT_COLOR);

        button.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        button.setBorder(new EmptyBorder(12, 20, 12, 20));

        return button;
    }

    // ========== VALIDATION UTILITIES ==========

    /**
     * Validates employee number input
     */
    public static int validateEmployeeNumber(String employeeNumberStr) throws IllegalArgumentException {
        if (employeeNumberStr == null || employeeNumberStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Employee number cannot be empty");
        }

        try {
            int employeeNumber = Integer.parseInt(employeeNumberStr);
            if (employeeNumber < AppConstants.MIN_EMPLOYEE_ID || employeeNumber > AppConstants.MAX_EMPLOYEE_ID) {
                throw new IllegalArgumentException("Employee number must be between " +
                        AppConstants.MIN_EMPLOYEE_ID + " and " + AppConstants.MAX_EMPLOYEE_ID);
            }
            return employeeNumber;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Employee number must be a valid number");
        }
    }

    /**
     * Validates and parses a date string in MM/dd/yyyy format
     */
    public static LocalDate validateDate(String dateStr) throws IllegalArgumentException {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            throw new IllegalArgumentException("Date cannot be empty");
        }

        try {
            return LocalDate.parse(dateStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Date must be in MM/dd/yyyy format");
        }
    }

    /**
     * Validates that the start date is before or equal to the end date
     */
    public static void validateDateRange(LocalDate startDate, LocalDate endDate) throws IllegalArgumentException {
        if (startDate == null || endDate == null) {
            throw new IllegalArgumentException("Start date and end date cannot be null");
        }

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException("Start date must be before or equal to end date");
        }
    }

    /**
     * Validates employee name
     */
    public static String validateName(String name, String fieldName) throws IllegalArgumentException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }

        String trimmedName = name.trim();
        if (trimmedName.length() > AppConstants.MAX_NAME_LENGTH) {
            throw new IllegalArgumentException(
                    fieldName + " cannot exceed " + AppConstants.MAX_NAME_LENGTH + " characters");
        }

        return trimmedName;
    }

    /**
     * Validates required field
     */
    public static String validateRequiredField(String value, String fieldName) throws IllegalArgumentException {
        if (value == null || value.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        return value.trim();
    }

    /**
     * Validates salary amount
     */
    public static double validateSalary(String salaryStr, String fieldName) throws IllegalArgumentException {
        if (salaryStr == null || salaryStr.trim().isEmpty()) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }

        try {
            String cleanSalary = salaryStr.replaceAll("[,₱$]", "").trim();
            double salary = Double.parseDouble(cleanSalary);

            if (salary < AppConstants.MIN_SALARY || salary > AppConstants.MAX_SALARY) {
                throw new IllegalArgumentException(fieldName + " must be between " +
                        AppConstants.MIN_SALARY + " and " + AppConstants.MAX_SALARY);
            }

            return salary;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(fieldName + " must be a valid number");
        }
    }

    /**
     * Validates phone number format
     */
    public static String validatePhoneNumber(String phone) throws IllegalArgumentException {
        if (phone == null || phone.trim().isEmpty()) {
            return "";
        }

        String trimmedPhone = phone.trim();
        if (!trimmedPhone.matches(AppConstants.PHONE_PATTERN)) {
            throw new IllegalArgumentException("Phone number must be in format: 123-456-7890");
        }

        return trimmedPhone;
    }

    /**
     * Validates SSS number
     */
    public static String validateSSS(String sss) throws IllegalArgumentException {
        if (sss == null || sss.trim().isEmpty()) {
            throw new IllegalArgumentException("SSS Number cannot be empty");
        }

        String value = sss.trim();
        if (!value.matches("^\\d{2}-\\d{7}-\\d$")) {
            throw new IllegalArgumentException("SSS Number must be in format: 12-1234567-1");
        }

        return value;
    }

    /**
     * Validates PhilHealth number
     */
    public static String validatePhilhealth(String philhealth) throws IllegalArgumentException {
        if (philhealth == null || philhealth.trim().isEmpty()) {
            throw new IllegalArgumentException("PhilHealth Number cannot be empty");
        }

        String value = philhealth.trim();
        if (!value.matches("^\\d{12}$")) {
            throw new IllegalArgumentException("PhilHealth Number must be exactly 12 digits");
        }

        return value;
    }

    /**
     * Validates TIN number
     */
    public static String validateTIN(String tin) throws IllegalArgumentException {
        if (tin == null || tin.trim().isEmpty()) {
            throw new IllegalArgumentException("TIN Number cannot be empty");
        }

        String value = tin.trim();
        if (!value.matches("^\\d{3}-\\d{3}-\\d{3}-\\d{3}$")) {
            throw new IllegalArgumentException("TIN Number must be in format: 123-456-789-000");
        }

        return value;
    }

    /**
     * Validates Pag-IBIG number
     */
    public static String validatePagibig(String pagibig) throws IllegalArgumentException {
        if (pagibig == null || pagibig.trim().isEmpty()) {
            throw new IllegalArgumentException("Pag-IBIG Number cannot be empty");
        }

        String value = pagibig.trim();
        if (!value.matches("^\\d{12}$")) {
            throw new IllegalArgumentException("Pag-IBIG Number must be exactly 12 digits");
        }

        return value;
    }

    // ========== DATE UTILITIES ==========

    /**
     * Format a LocalDate to a string using the default format (MM/dd/yyyy)
     */
    public static String formatDate(LocalDate date) {
        if (date == null) {
            return "";
        }
        return date.format(DATE_FORMATTER);
    }

    /**
     * Create a human-readable date range string
     */
    public static String formatDateRange(LocalDate startDate, LocalDate endDate) {
        if (startDate == null || endDate == null) {
            return "";
        }
        return formatDate(startDate) + " to " + formatDate(endDate);
    }

    // ========== ERROR HANDLING ==========

    /**
     * Show error dialog with consistent styling
     */
    public static void showError(String message) {
        showError(null, message);
    }

    /**
     * Show error dialog with parent component
     */
    public static void showError(java.awt.Component parent, String message) {
        logger.log(Level.WARNING, "Error dialog shown: {0}", message);
        JOptionPane.showMessageDialog(parent, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show success dialog with consistent styling
     */
    public static void showSuccess(String message) {
        showSuccess(null, message);
    }

    /**
     * Show success dialog with parent component
     */
    public static void showSuccess(java.awt.Component parent, String message) {
        logger.log(Level.INFO, "Success dialog shown: {0}", message);
        JOptionPane.showMessageDialog(parent, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Show confirmation dialog
     */
    public static boolean showConfirm(String message) {
        return showConfirm(null, message);
    }

    /**
     * Show confirmation dialog with parent component
     */
    public static boolean showConfirm(java.awt.Component parent, String message) {
        int result = JOptionPane.showConfirmDialog(parent, message, "Confirm",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        return result == JOptionPane.YES_OPTION;
    }

    /**
     * Log and handle exceptions consistently
     */
    public static void handleException(String operation, Exception e) {
        String message = "Error during " + operation + ": " + e.getMessage();
        logger.log(Level.SEVERE, message, e);
        showError(message);
    }

    /**
     * Log and handle exceptions with custom user message
     */
    public static void handleException(String operation, Exception e, String userMessage) {
        logger.log(Level.SEVERE, "Error during " + operation + ": " + e.getMessage(), e);
        showError(userMessage);
    }

    /**
     * Create a status badge label with appropriate styling
     */
    public static JLabel createStatusBadge(String status, boolean isActive) {
        JLabel badge = new JLabel(status);
        badge.setOpaque(true);
        badge.setBorder(new EmptyBorder(4, 8, 4, 8));
        badge.setFont(AppConstants.SMALL_FONT);
        badge.setForeground(Color.WHITE);

        if (isActive) {
            badge.setBackground(AppConstants.SUCCESS_COLOR);
        } else {
            badge.setBackground(AppConstants.DELETE_BUTTON_COLOR);
        }

        return badge;
    }

    // Private constructor to prevent instantiation
    private AppUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}