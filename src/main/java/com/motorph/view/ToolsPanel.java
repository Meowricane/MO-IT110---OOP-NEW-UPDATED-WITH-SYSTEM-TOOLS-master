package com.motorph.view;

import javax.swing.*;
import java.awt.*;

import com.motorph.repository.DataRepository;
import com.motorph.util.AppConstants;
import com.motorph.view.dialog.ManageUserAccountsDialog;
import com.motorph.view.dialog.SystemMaintenanceDialog;

public class ToolsPanel extends JPanel {

    public ToolsPanel() {
        setLayout(new BorderLayout());

        JLabel title = new JLabel("System Tools", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));

        JPanel toolsContainer = new JPanel();
        toolsContainer.setLayout(new GridLayout(3, 1, 15, 15));
        toolsContainer.setBorder(BorderFactory.createEmptyBorder(40, 200, 40, 200));

        JButton manageUsersBtn = new JButton("Manage User Accounts");
        JButton resetPasswordBtn = new JButton("Reset Password");
        JButton systemMaintenanceBtn = new JButton("System Maintenance");

        toolsContainer.add(manageUsersBtn);
        toolsContainer.add(resetPasswordBtn);
        toolsContainer.add(systemMaintenanceBtn);

        add(title, BorderLayout.NORTH);
        add(toolsContainer, BorderLayout.CENTER);

        manageUsersBtn.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);

            String employeesFilePath = AppConstants.getEmployeeFilePath();
            String attendanceFilePath = AppConstants.getAttendanceFilePath();
            String usersFilePath = "data/userCredentials.csv";

            System.out.println("Employee file: " + employeesFilePath);
            System.out.println("Attendance file: " + attendanceFilePath);
            System.out.println("Users file: " + usersFilePath);

            DataRepository dataRepository = new DataRepository(
                    employeesFilePath,
                    attendanceFilePath,
                    usersFilePath
            );

            ManageUserAccountsDialog dialog = new ManageUserAccountsDialog(parentFrame, dataRepository);
            dialog.setVisible(true);
        });

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
                JOptionPane.showMessageDialog(
                        resetDialog,
                        "Password reset for user: " + usernameField.getText()
                );
                resetDialog.dispose();
            });
            panel.add(okBtn);

            JButton cancelBtn = new JButton("Cancel");
            cancelBtn.addActionListener(ev -> resetDialog.dispose());
            panel.add(cancelBtn);

            resetDialog.add(panel);
            resetDialog.setVisible(true);
        });

        systemMaintenanceBtn.addActionListener(e -> {
            JFrame parentFrame = (JFrame) SwingUtilities.getWindowAncestor(this);
            new SystemMaintenanceDialog(parentFrame).setVisible(true);
        });
    }
}