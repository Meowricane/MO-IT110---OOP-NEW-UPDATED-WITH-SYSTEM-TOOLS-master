package com.motorph.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.motorph.controller.EmployeeController;
import com.motorph.model.Employee;
import com.motorph.model.User;
import com.motorph.util.AppConstants;
import com.motorph.util.AppUtils;

/**
 * Header panel for the MotorPH Payroll System.
 * Shows the current user information and system branding.
 */
public class HeaderPanel extends JPanel {

    private final EmployeeController employeeController;

    public HeaderPanel(EmployeeController employeeController) {
        this.employeeController = employeeController;
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(Color.WHITE);
        setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, AppConstants.BORDER_COLOR),
                BorderFactory.createEmptyBorder(15, 25, 15, 25)));

        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        leftPanel.setOpaque(false);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightPanel.setOpaque(false);

        User currentUser = AppUtils.getCurrentUser();

        String displayName = getEmployeeDisplayName(currentUser);
        String initials = generateInitials(displayName);

        JLabel userLabel = new JLabel(displayName);
        userLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        userLabel.setForeground(AppConstants.TEXT_COLOR);

        JLabel initialsLabel = new JLabel(initials) {
            @Override
            protected void paintComponent(java.awt.Graphics g) {
                java.awt.Graphics2D g2 = (java.awt.Graphics2D) g.create();
                g2.setRenderingHint(java.awt.RenderingHints.KEY_ANTIALIASING,
                        java.awt.RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(AppConstants.PRIMARY_BUTTON_COLOR);
                g2.fillOval(0, 0, getWidth(), getHeight());
                g2.dispose();
                super.paintComponent(g);
            }
        };
        initialsLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        initialsLabel.setForeground(Color.WHITE);
        initialsLabel.setOpaque(false);
        initialsLabel.setHorizontalAlignment(JLabel.CENTER);
        initialsLabel.setVerticalAlignment(JLabel.CENTER);
        initialsLabel.setPreferredSize(new java.awt.Dimension(32, 32));

        rightPanel.add(userLabel);
        rightPanel.add(javax.swing.Box.createHorizontalStrut(12));
        rightPanel.add(initialsLabel);

        add(leftPanel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);
    }

    private String getEmployeeDisplayName(User currentUser) {
        if (currentUser == null) {
            return "Guest User";
        }

        try {
            int currentEmployeeId = currentUser.getEmployeeId();
            List<Employee> employees = employeeController.getAllEmployees();

            for (Employee emp : employees) {
                if (emp.getEmployeeId() == currentEmployeeId) {
                    return emp.getFirstName() + " " + emp.getLastName();
                }
            }
        } catch (Exception e) {
            System.out.println("Unable to load employee name: " + e.getMessage());
        }

        return currentUser.getUsername();
    }

    private String generateInitials(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "GU";
        }

        String[] parts = fullName.trim().split("\\s+");

        if (parts.length == 1) {
            String first = parts[0].substring(0, 1).toUpperCase();
            return first + first;
        }

        String firstInitial = parts[0].substring(0, 1).toUpperCase();
        String secondInitial = parts[parts.length - 1].substring(0, 1).toUpperCase();

        return firstInitial + secondInitial;
    }
}