package com.motorph.view.dialog;

import com.motorph.model.Employee;
import com.motorph.model.SystemUserRecord;
import com.motorph.repository.DataRepository;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageUserAccountsDialog extends JDialog {

    private final DataRepository dataRepository;
    private JTable userTable;
    private DefaultTableModel tableModel;

    public ManageUserAccountsDialog(Frame parent, DataRepository dataRepository) {
        super(parent, "Manage User Accounts", true);
        this.dataRepository = dataRepository;

        initializeUI();
        loadUserData();
    }

    private void initializeUI() {
        setSize(900, 500);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));

        JLabel titleLabel = new JLabel("Manage User Accounts", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        add(titleLabel, BorderLayout.NORTH);

        String[] columns = {
                "Employee ID", "Full Name", "Username", "Role", "Active", "Position", "Status"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        userTable = new JTable(tableModel);
        userTable.setRowHeight(24);

        JScrollPane scrollPane = new JScrollPane(userTable);
        add(scrollPane, BorderLayout.CENTER);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        JButton addButton = new JButton("Add User");
        JButton editRoleButton = new JButton("Edit Role");
        JButton toggleButton = new JButton("Enable/Disable");
        JButton closeButton = new JButton("Close");

        buttonPanel.add(addButton);
        buttonPanel.add(editRoleButton);
        buttonPanel.add(toggleButton);
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(e -> openAddUserDialog());
        editRoleButton.addActionListener(e -> editSelectedUserRole());
        toggleButton.addActionListener(e -> toggleSelectedUserStatus());
        closeButton.addActionListener(e -> dispose());
    }

    private void loadUserData() {
        tableModel.setRowCount(0);

        List<Employee> employees = dataRepository.getAllEmployees();
        List<SystemUserRecord> users = dataRepository.getAllSystemUsers();

        System.out.println("Employees loaded: " + employees.size());
        System.out.println("System users loaded: " + users.size());

        Map<Integer, Employee> employeeMap = new HashMap<>();
        for (Employee employee : employees) {
            employeeMap.put(employee.getEmployeeId(), employee);
        }

        for (SystemUserRecord user : users) {
            Employee employee = employeeMap.get(user.getEmployeeId());

            String fullName = employee != null ? employee.getFullName() : "Unknown";
            String position = employee != null ? employee.getPosition() : "";
            String status = employee != null ? employee.getStatus() : "";

            tableModel.addRow(new Object[]{
                    user.getEmployeeId(),
                    fullName,
                    user.getUsername(),
                    user.getRole(),
                    user.isActive() ? "Yes" : "No",
                    position,
                    status
            });
        }
    }

    private void openAddUserDialog() {
        AddUserDialog dialog = new AddUserDialog((Frame) getParent());
        dialog.setVisible(true);

        SystemUserRecord newUser = dialog.getCreatedUser();
        if (newUser != null) {
            tableModel.addRow(new Object[]{
                    newUser.getEmployeeId(),
                    "Unknown",
                    newUser.getUsername(),
                    newUser.getRole(),
                    newUser.isActive() ? "Yes" : "No",
                    "",
                    ""
            });

            JOptionPane.showMessageDialog(this, "User added to table.");
        }
    }

    private void editSelectedUserRole() {
        int selectedRow = userTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user first.");
            return;
        }

        String currentRole = tableModel.getValueAt(selectedRow, 3).toString();

        String[] roles = {"Admin", "HR", "Finance", "IT", "Employee"};
        String newRole = (String) JOptionPane.showInputDialog(
                this,
                "Select new role:",
                "Edit Role",
                JOptionPane.PLAIN_MESSAGE,
                null,
                roles,
                currentRole
        );

        if (newRole != null && !newRole.trim().isEmpty()) {
            tableModel.setValueAt(newRole, selectedRow, 3);
            JOptionPane.showMessageDialog(this, "Role updated.");
        }
    }

    private void toggleSelectedUserStatus() {
        int selectedRow = userTable.getSelectedRow();

        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Please select a user first.");
            return;
        }

        String currentStatus = tableModel.getValueAt(selectedRow, 4).toString();
        String newStatus = currentStatus.equalsIgnoreCase("Yes") ? "No" : "Yes";

        tableModel.setValueAt(newStatus, selectedRow, 4);
        JOptionPane.showMessageDialog(this, "User status updated.");
    }
}