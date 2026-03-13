package com.motorph.view;

import com.motorph.leave.LeaveRequest;
import com.motorph.leave.LeaveRequestService;
import com.motorph.security.RolePermission;
import com.motorph.util.AppUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.List;

/**
 * Panel for submitting and managing leave requests.
 */
public class LeavePanel extends JPanel {

    private JTextField employeeNameField;
    private JComboBox<String> leaveTypeCombo;
    private JTextField leaveDaysField;

    private JButton submitButton;
    private JButton deleteButton;
    private JButton approveButton;
    private JButton denyButton;

    private JTable leaveTable;
    private DefaultTableModel tableModel;

    private final LeaveRequestService leaveService;
    private List<LeaveRequest> leaveRequests;
    private final RolePermission rolePermission;

    public LeavePanel(LeaveRequestService leaveService) {
        this.leaveService = leaveService;
        this.leaveRequests = leaveService.getAllLeaveRequests();
        this.rolePermission = new RolePermission(AppUtils.getCurrentUser().getRole());

        initializeComponents();
        applyRolePermissions();
    }

    private void initializeComponents() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(10, 10, 10, 10);

        JLabel titleLabel = new JLabel("Leave Request");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);

        gbc.gridwidth = 1;
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Employee Name:"), gbc);

        employeeNameField = new JTextField(20);
        if (AppUtils.getCurrentUser() != null) {
            employeeNameField.setText(AppUtils.getCurrentUser().getUsername());
        }
        gbc.gridx = 1;
        add(employeeNameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Leave Type:"), gbc);

        leaveTypeCombo = new JComboBox<>(new String[]{
                "Sick Leave",
                "Vacation Leave",
                "Holiday Leave",
                "Personal Time Off"
        });
        gbc.gridx = 1;
        add(leaveTypeCombo, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        add(new JLabel("Number of Days:"), gbc);

        leaveDaysField = new JTextField(5);
        gbc.gridx = 1;
        add(leaveDaysField, gbc);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));

        submitButton = new JButton("Submit Leave Request");
        deleteButton = new JButton("Delete Selected");
        approveButton = new JButton("Approve");
        denyButton = new JButton("Deny");

        buttonPanel.add(submitButton);
        buttonPanel.add(deleteButton);
        buttonPanel.add(approveButton);
        buttonPanel.add(denyButton);

        GridBagConstraints gbcButtons = new GridBagConstraints();
        gbcButtons.gridx = 0;
        gbcButtons.gridy = 4;
        gbcButtons.gridwidth = 2;
        gbcButtons.anchor = GridBagConstraints.CENTER;
        gbcButtons.insets = new Insets(10, 0, 10, 0);
        add(buttonPanel, gbcButtons);

        String[] columns = {"Employee Name", "Leave Type", "Start Date", "End Date", "Notes", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        leaveTable = new JTable(tableModel);
        leaveTable.setFillsViewportHeight(true);
        leaveTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

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

        refreshTable();

        submitButton.addActionListener(new SubmitListener());
        deleteButton.addActionListener(e -> deleteSelectedLeave());
        approveButton.addActionListener(e -> approveSelectedLeave());
        denyButton.addActionListener(e -> denySelectedLeave());
    }

    private void applyRolePermissions() {
        boolean canReview = rolePermission.canAccessEmployee(); // ADMIN or HR in your setup

        approveButton.setVisible(canReview);
        denyButton.setVisible(canReview);

        if (canReview) {
            employeeNameField.setEditable(false);
        }
    }

    private void refreshTable() {
        leaveRequests = leaveService.getAllLeaveRequests();
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

    private class SubmitListener implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            String employeeName = employeeNameField.getText().trim();
            String leaveType = leaveTypeCombo.getSelectedItem().toString();
            String daysText = leaveDaysField.getText().trim();

            if (employeeName.isEmpty() || daysText.isEmpty()) {
                JOptionPane.showMessageDialog(
                        LeavePanel.this,
                        "Please fill all fields.",
                        "Input Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            int days;
            try {
                days = Integer.parseInt(daysText);
                if (days <= 0) throw new NumberFormatException();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        LeavePanel.this,
                        "Number of days must be a positive integer.",
                        "Input Error",
                        JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            LocalDate startDate = LocalDate.now();
            LocalDate endDate = startDate.plusDays(days - 1);
            String notes = "";
            String status = "PENDING";

            LeaveRequest leaveRequest = new LeaveRequest(
                    employeeName,
                    leaveType,
                    startDate,
                    endDate,
                    notes,
                    status
            );

            leaveService.addLeaveRequest(leaveRequest);
            refreshTable();

            JOptionPane.showMessageDialog(
                    LeavePanel.this,
                    "Leave request submitted for " + employeeName,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );

            if (!rolePermission.canAccessEmployee()) {
                employeeNameField.setText(AppUtils.getCurrentUser() != null
                        ? AppUtils.getCurrentUser().getUsername()
                        : "");
            } else {
                employeeNameField.setText("");
            }

            leaveDaysField.setText("");
            leaveTypeCombo.setSelectedIndex(0);
        }
    }

    private void deleteSelectedLeave() {
        int selectedRow = leaveTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a leave request to delete.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        LeaveRequest toDelete = leaveRequests.get(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete the selected leave request?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            leaveService.deleteLeaveRequest(toDelete);
            refreshTable();

            JOptionPane.showMessageDialog(
                    this,
                    "Leave request deleted successfully.",
                    "Deleted",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private void approveSelectedLeave() {
        if (!rolePermission.canAccessEmployee()) {
            JOptionPane.showMessageDialog(this, "Only Admin/HR can approve leave requests.");
            return;
        }

        int selectedRow = leaveTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a leave request to approve.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        LeaveRequest selectedRequest = leaveRequests.get(selectedRow);
        leaveService.approveLeaveRequest(selectedRequest);
        refreshTable();

        JOptionPane.showMessageDialog(
                this,
                "Leave request approved.",
                "Approved",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void denySelectedLeave() {
        if (!rolePermission.canAccessEmployee()) {
            JOptionPane.showMessageDialog(this, "Only Admin/HR can deny leave requests.");
            return;
        }

        int selectedRow = leaveTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(
                    this,
                    "Please select a leave request to deny.",
                    "No Selection",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        LeaveRequest selectedRequest = leaveRequests.get(selectedRow);
        leaveService.denyLeaveRequest(selectedRequest);
        refreshTable();

        JOptionPane.showMessageDialog(
                this,
                "Leave request denied.",
                "Denied",
                JOptionPane.INFORMATION_MESSAGE
        );
    }
}