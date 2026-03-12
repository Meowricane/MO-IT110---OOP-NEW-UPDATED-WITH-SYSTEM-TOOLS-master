package com.motorph.view;

import javax.swing.*;
import java.awt.*;

public class ToolsPanel extends JPanel {

    public ToolsPanel() {
        setLayout(new BorderLayout());

        // Title at the top
        JLabel title = new JLabel("System Tools", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));

        // Container for buttons
        JPanel toolsContainer = new JPanel();
        toolsContainer.setLayout(new GridLayout(3, 1, 15, 15));
        toolsContainer.setBorder(BorderFactory.createEmptyBorder(40, 200, 40, 200));

        // Buttons
        JButton manageUsersBtn = new JButton("Manage User Accounts");
        JButton resetPasswordBtn = new JButton("Reset Password");
        JButton systemMaintenanceBtn = new JButton("System Maintenance");

        toolsContainer.add(manageUsersBtn);
        toolsContainer.add(resetPasswordBtn);
        toolsContainer.add(systemMaintenanceBtn);

        add(title, BorderLayout.NORTH);
        add(toolsContainer, BorderLayout.CENTER);

        // ----------------------------
        // Button Actions (functional)
        // ----------------------------

        // Manage User Accounts
        manageUsersBtn.addActionListener(e -> {
            JDialog userDialog = new JDialog();
            userDialog.setTitle("Manage User Accounts");
            userDialog.setSize(600, 400);
            userDialog.setLocationRelativeTo(this);
            userDialog.setModal(true);

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(new JLabel("Here you can manage user accounts...", JLabel.CENTER), BorderLayout.CENTER);

            JButton closeBtn = new JButton("Close");
            closeBtn.addActionListener(ev -> userDialog.dispose());
            panel.add(closeBtn, BorderLayout.SOUTH);

            userDialog.add(panel);
            userDialog.setVisible(true);
        });

        // Reset Password
        resetPasswordBtn.addActionListener(e -> {
            JDialog resetDialog = new JDialog();
            resetDialog.setTitle("Reset Password");
            resetDialog.setSize(400, 250);
            resetDialog.setLocationRelativeTo(this);
            resetDialog.setModal(true);

            JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10));
            panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

            panel.add(new JLabel("Username:"));
            JTextField usernameField = new JTextField();
            panel.add(usernameField);

            panel.add(new JLabel("New Password:"));
            JPasswordField passwordField = new JPasswordField();
            panel.add(passwordField);

            JButton okBtn = new JButton("OK");
            okBtn.addActionListener(ev -> {
                // Here you can call your password reset logic
                JOptionPane.showMessageDialog(resetDialog,
                        "Password reset for user: " + usernameField.getText());
                resetDialog.dispose();
            });
            panel.add(okBtn);

            JButton cancelBtn = new JButton("Cancel");
            cancelBtn.addActionListener(ev -> resetDialog.dispose());
            panel.add(cancelBtn);

            resetDialog.add(panel);
            resetDialog.setVisible(true);
        });

        // System Maintenance
        systemMaintenanceBtn.addActionListener(e -> {
            JDialog maintenanceDialog = new JDialog();
            maintenanceDialog.setTitle("System Maintenance");
            maintenanceDialog.setSize(500, 350);
            maintenanceDialog.setLocationRelativeTo(this);
            maintenanceDialog.setModal(true);

            JPanel panel = new JPanel(new BorderLayout());
            panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            panel.add(new JLabel("System maintenance options here...", JLabel.CENTER), BorderLayout.CENTER);

            JButton closeBtn = new JButton("Close");
            closeBtn.addActionListener(ev -> maintenanceDialog.dispose());
            panel.add(closeBtn, BorderLayout.SOUTH);

            maintenanceDialog.add(panel);
            maintenanceDialog.setVisible(true);
        });
    }
}