package com.motorph.view;

import com.motorph.leave.LeaveRequest;
import com.motorph.leave.LeaveRequestService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

/**
 * Panel for submitting leave requests (SL/VL/HL) and managing them.
 */
public class LeavePanel extends JPanel {

    private JTextField employeeNameField;
    private JComboBox<String> leaveTypeCombo;
    private JTextField leaveDaysField;
    private JButton submitButton;
    private JButton deleteButton;

    private JTable leaveTable;
    private DefaultTableModel tableModel;
    private final LeaveRequestService leaveService;
    private List<LeaveRequest> leaveRequests;

    public LeavePanel(LeaveRequestService leaveService) {
        this.leaveService = leaveService;
        this.leaveRequests = leaveService.getAllLeaveRequests();

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        // --- TITLE ---
        JLabel titleLabel = new JLabel("Leave Request");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        // Employee Name
        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Employee Name:"), gbc);
        employeeNameField = new JTextField(20);
        gbc.gridx = 1;
        add(employeeNameField, gbc);

        // Leave Type
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Leave Type:"), gbc);
        leaveTypeCombo = new JComboBox<>(new String[]{"Sick Leave", "Holiday Leave", "Personal Time Off"});
        gbc.gridx = 1;
        add(leaveTypeCombo, gbc);

        // Number of Days
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Number of Days:"), gbc);
        leaveDaysField = new JTextField(5);
        gbc.gridx = 1;
        add(leaveDaysField, gbc);

        // --- Buttons Panel (Submit + Delete) ---
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        submitButton = new JButton("Submit Leave Request");
        deleteButton = new JButton("Delete Selected Leave");
        buttonPanel.add(submitButton);
        buttonPanel.add(deleteButton);

        GridBagConstraints gbcButtons = new GridBagConstraints();
        gbcButtons.gridx = 0;
        gbcButtons.gridy = 4;
        gbcButtons.gridwidth = 2;
        gbcButtons.anchor = GridBagConstraints.CENTER;
        gbcButtons.insets = new Insets(10, 0, 10, 0);
        add(buttonPanel, gbcButtons);

        // --- Table ---
        String[] columns = {"Employee Name", "Leave Type", "Start Date", "End Date", "Notes", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        leaveTable = new JTable(tableModel);
        leaveTable.setFillsViewportHeight(true);

        JScrollPane tableScroll = new JScrollPane(leaveTable);
        GridBagConstraints tableGbc = new GridBagConstraints();
        tableGbc.gridx = 0;
        tableGbc.gridy = 5;
        tableGbc.gridwidth = 2;
        tableGbc.fill = GridBagConstraints.BOTH;
        tableGbc.weightx = 1;
        tableGbc.weighty = 1;
        tableGbc.insets = new Insets(20, 10, 10, 10);
        add(tableScroll, tableGbc);

        // Populate table with existing leave requests
        refreshTable();

        // Listeners
        submitButton.addActionListener(new SubmitListener());
        deleteButton.addActionListener(e -> deleteSelectedLeave());
    }

    private void refreshTable() {
        tableModel.setRowCount(0);
        for (LeaveRequest lr : leaveRequests) {
            tableModel.addRow(new Object[]{
                    lr.getEmployeeName(),
                    lr.getLeaveType(),
                    lr.getStartDate(),
                    lr.getEndDate(),
                    lr.getNotes(),
                    lr.getStatus()
            });
        }
    }

    // --- Submit leave ---
    private class SubmitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String employeeName = employeeNameField.getText().trim();
            String leaveType = leaveTypeCombo.getSelectedItem().toString();
            String daysText = leaveDaysField.getText().trim();

            if (employeeName.isEmpty() || daysText.isEmpty()) {
                JOptionPane.showMessageDialog(LeavePanel.this,
                        "Please fill all fields.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            int days;
            try {
                days = Integer.parseInt(daysText);
                if (days <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(LeavePanel.this,
                        "Number of days must be a positive integer.", "Input Error", JOptionPane.WARNING_MESSAGE);
                return;
            }

            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusDays(days - 1);
            String notes = "";
            String status = "Pending";

            LeaveRequest leaveRequest = new LeaveRequest(employeeName, leaveType, startDate, endDate, notes, status);

            leaveService.addLeaveRequest(leaveRequest);
            leaveRequests.add(leaveRequest);
            refreshTable();

            JOptionPane.showMessageDialog(LeavePanel.this,
                    "Leave request submitted for " + employeeName,
                    "Success", JOptionPane.INFORMATION_MESSAGE);

            // Clear form
            employeeNameField.setText("");
            leaveDaysField.setText("");
            leaveTypeCombo.setSelectedIndex(0);
        }
    }

    // --- Delete selected leave ---
    private void deleteSelectedLeave() {
        int selectedRow = leaveTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this,
                    "Please select a leave request to delete.",
                    "No Selection", JOptionPane.WARNING_MESSAGE);
            return;
        }

        LeaveRequest toDelete = leaveRequests.get(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(this,
                "Are you sure you want to delete the selected leave request?",
                "Confirm Delete", JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            leaveService.deleteLeaveRequest(toDelete); // make sure this method exists in LeaveRequestService
            leaveRequests.remove(selectedRow);
            refreshTable();
            JOptionPane.showMessageDialog(this,
                    "Leave request deleted successfully.",
                    "Deleted", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}