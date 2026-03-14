package com.motorph.view.dialog;

import com.motorph.model.SystemUserRecord;

import javax.swing.*;
import java.awt.*;

public class AddUserDialog extends JDialog {

    private JTextField employeeIdField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JCheckBox activeCheckBox;

    private SystemUserRecord createdUser;

    public AddUserDialog(Frame parent) {
        super(parent, "Add User", true);
        initializeUI();
    }

    private void initializeUI() {
        setSize(420, 300);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        employeeIdField = new JTextField();
        usernameField = new JTextField();
        passwordField = new JPasswordField();
        roleComboBox = new JComboBox<>(new String[]{"Admin", "HR", "Finance", "IT", "Employee"});
        activeCheckBox = new JCheckBox("Active", true);

        formPanel.add(new JLabel("Employee ID:"));
        formPanel.add(employeeIdField);

        formPanel.add(new JLabel("Username:"));
        formPanel.add(usernameField);

        formPanel.add(new JLabel("Password:"));
        formPanel.add(passwordField);

        formPanel.add(new JLabel("Role:"));
        formPanel.add(roleComboBox);

        formPanel.add(new JLabel("Status:"));
        formPanel.add(activeCheckBox);

        add(formPanel, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        add(buttonPanel, BorderLayout.SOUTH);

        saveButton.addActionListener(e -> saveUser());
        cancelButton.addActionListener(e -> dispose());
    }

    private void saveUser() {
        try {
            int employeeId = Integer.parseInt(employeeIdField.getText().trim());
            String username = usernameField.getText().trim();
            String password = new String(passwordField.getPassword()).trim();
            String role = (String) roleComboBox.getSelectedItem();
            boolean active = activeCheckBox.isSelected();

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Username and password are required.");
                return;
            }

            createdUser = new SystemUserRecord();
            createdUser.setEmployeeId(employeeId);
            createdUser.setUsername(username);
            createdUser.setPassword(password);
            createdUser.setRole(role);
            createdUser.setActive(active);

            dispose();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Employee ID must be a number.");
        }
    }

    public SystemUserRecord getCreatedUser() {
        return createdUser;
    }
}