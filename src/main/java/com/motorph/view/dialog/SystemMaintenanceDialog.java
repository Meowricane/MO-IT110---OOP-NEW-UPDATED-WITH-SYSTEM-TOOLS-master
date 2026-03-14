package com.motorph.view.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

public class SystemMaintenanceDialog extends JDialog {

    private JTextArea outputArea;

    public SystemMaintenanceDialog(JFrame parent) {
        super(parent, "System Maintenance", true);
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("System Maintenance", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.setBackground(Color.WHITE);

        JButton validateFilesBtn = new JButton("Validate CSV Files");
        JButton fileStatusBtn = new JButton("Show File Status");
        JButton resetLeaveBtn = new JButton("Reset Leave Requests");
        JButton closeBtn = new JButton("Close");

        styleButton(validateFilesBtn, new Color(121, 9, 37));
        styleButton(fileStatusBtn, new Color(121, 9, 37));
        styleButton(resetLeaveBtn, new Color(200, 60, 60));
        styleButton(closeBtn, new Color(100, 100, 100));

        buttonPanel.add(validateFilesBtn);
        buttonPanel.add(fileStatusBtn);
        buttonPanel.add(resetLeaveBtn);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        outputArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        outputArea.setLineWrap(true);
        outputArea.setWrapStyleWord(true);
        outputArea.setBackground(new Color(250, 250, 250));
        outputArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(210, 210, 210)),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        outputArea.setText("Select a maintenance action.");

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(buttonPanel);
        centerPanel.add(Box.createVerticalStrut(10));
        centerPanel.add(outputArea);

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(closeBtn);

        validateFilesBtn.addActionListener(e -> validateCsvFiles());
        fileStatusBtn.addActionListener(e -> showFileStatus());
        resetLeaveBtn.addActionListener(e -> resetLeaveRequests());
        closeBtn.addActionListener(e -> dispose());

        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(620, 420));
        pack();
        setLocationRelativeTo(getParent());
    }

    private void styleButton(JButton button, Color bgColor) {
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setBackground(bgColor);
        button.setForeground(Color.BLACK);
        button.setFocusPainted(false);
    }

    private void validateCsvFiles() {
        StringBuilder result = new StringBuilder();
        result.append("CSV FILE VALIDATION\n");
        result.append("-----------------------------------\n");

        String[] files = {
                "data/employeeDetails.csv",
                "data/attendanceRecord.csv",
                "data/leave_requests.csv",
                "data/userCredentials.csv"
        };

        for (String path : files) {
            File file = new File(path);
            if (file.exists()) {
                result.append("[OK] ").append(path).append("\n");
            } else {
                result.append("[MISSING] ").append(path).append("\n");
            }
        }

        outputArea.setText(result.toString());
    }

    private void showFileStatus() {
        StringBuilder result = new StringBuilder();
        result.append("SYSTEM FILE STATUS\n");
        result.append("-----------------------------------\n");

        String[] files = {
                "data/employeeDetails.csv",
                "data/attendanceRecord.csv",
                "data/leave_requests.csv",
                "data/userCredentials.csv"
        };

        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");

        for (String path : files) {
            File file = new File(path);
            result.append(path).append("\n");
            if (file.exists()) {
                result.append("  Exists: YES\n");
                result.append("  Size: ").append(file.length()).append(" bytes\n");
                result.append("  Last Modified: ")
                      .append(sdf.format(new Date(file.lastModified())))
                      .append("\n");
            } else {
                result.append("  Exists: NO\n");
            }
            result.append("\n");
        }

        outputArea.setText(result.toString());
    }

    private void resetLeaveRequests() {
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to reset all leave requests?\nThis will clear the leave_requests.csv file contents.",
                "Confirm Reset",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            java.io.FileWriter writer = new java.io.FileWriter("data/leave_requests.csv");
            writer.write("Employee Name,Leave Type,Start Date,End Date,Notes,Status\n");
            writer.close();

            outputArea.setText("Leave requests have been reset successfully.");
        } catch (Exception e) {
            outputArea.setText("Error resetting leave requests: " + e.getMessage());
        }
    }
}