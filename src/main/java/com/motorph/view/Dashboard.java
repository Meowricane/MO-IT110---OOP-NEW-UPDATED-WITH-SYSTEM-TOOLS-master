package com.motorph.view;

import java.awt.*;
import java.util.List;
import javax.swing.*;

import com.motorph.controller.EmployeeController;
import com.motorph.model.Employee;
import com.motorph.util.AppConstants;
import com.motorph.util.AppUtils;

public class Dashboard extends JPanel {

    private final MainFrame mainFrame;
    private final EmployeeController employeeController;
    private Employee currentEmployee;

    public Dashboard(MainFrame mainFrame, EmployeeController employeeController) {
        this.mainFrame = mainFrame;
        this.employeeController = employeeController;
        this.currentEmployee = findCurrentEmployee();
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        setBackground(AppConstants.BACKGROUND_COLOR);

        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(AppConstants.BACKGROUND_COLOR);
        container.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30));

        JPanel headerPanel = createHeaderPanel();
        headerPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel statsPanel = createStatisticsPanel();
        statsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel quickLinksPanel = createQuickLinksPanel();
        quickLinksPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel notificationsPanel = createNotificationsPanel();
        notificationsPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        container.add(headerPanel);
        container.add(Box.createVerticalStrut(20));
        container.add(statsPanel);
        container.add(Box.createVerticalStrut(25));
        container.add(quickLinksPanel);
        container.add(Box.createVerticalStrut(25));
        container.add(notificationsPanel);

        JScrollPane scrollPane = new JScrollPane(container);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);

        add(scrollPane, BorderLayout.CENTER);
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(AppConstants.BACKGROUND_COLOR);
        header.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

        JLabel titleLabel = new JLabel("Dashboard");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(AppConstants.TEXT_COLOR);

        header.add(titleLabel, BorderLayout.WEST);
        return header;
    }

    private JPanel createStatisticsPanel() {
        JPanel statsContainer = new JPanel(new GridLayout(1, 2, 20, 0));
        statsContainer.setBackground(AppConstants.BACKGROUND_COLOR);
        statsContainer.setMaximumSize(new Dimension(Integer.MAX_VALUE, 140));

        String latestPay = currentEmployee != null
                ? formatMoney(currentEmployee.getGrossSemiMonthlyRate())
                : "N/A";

        String basicSalary = currentEmployee != null
                ? formatMoney(currentEmployee.getBasicSalary())
                : "N/A";

        statsContainer.add(createStatCard("Latest Pay (Semi-monthly)", latestPay));
        statsContainer.add(createStatCard("Basic Monthly Salary", basicSalary));

        return statsContainer;
    }

    private JPanel createQuickLinksPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AppConstants.BACKGROUND_COLOR);
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, 260));

        JLabel headerLabel = new JLabel("Quick Links");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(AppConstants.TEXT_COLOR);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 20, 0));

        JPanel linksPanel = new JPanel(new GridLayout(1, 4, 16, 0));
        linksPanel.setBackground(AppConstants.BACKGROUND_COLOR);

        linksPanel.add(createQuickLinkCard(
                "My Payslips",
                "View your salary details and latest pay",
                this::openPayslipDialog
        ));

        linksPanel.add(createQuickLinkCard(
                "Attendance",
                "View attendance summary and logs",
                this::openAttendanceDialog
        ));

        linksPanel.add(createQuickLinkCard(
                "Leave Balance",
                "View your available leave credits",
                this::showLeaveBalanceInfo
        ));

        linksPanel.add(createQuickLinkCard(
                "Leave Request",
                "Submit and monitor your leave application",
                () -> mainFrame.showPanel("LeavePanel")
        ));

        container.add(headerLabel, BorderLayout.NORTH);
        container.add(linksPanel, BorderLayout.CENTER);

        return container;
    }

    private JPanel createNotificationsPanel() {
        JPanel container = new JPanel(new BorderLayout());
        container.setBackground(AppConstants.BACKGROUND_COLOR);
        container.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));

        JLabel headerLabel = new JLabel("Notifications");
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        headerLabel.setForeground(AppConstants.TEXT_COLOR);
        headerLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));

        String latestPay = currentEmployee != null
                ? formatMoney(currentEmployee.getGrossSemiMonthlyRate())
                : "N/A";

        JTextArea notificationsArea = new JTextArea(
                "• Your latest semi-monthly pay is " + latestPay + ".\n\n" +
                "• Click My Payslips to review your payroll details.\n\n" +
                "• Use Leave Request to file your leave application."
        );
        notificationsArea.setEditable(false);
        notificationsArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        notificationsArea.setForeground(AppConstants.TEXT_COLOR);
        notificationsArea.setBackground(Color.WHITE);
        notificationsArea.setLineWrap(true);
        notificationsArea.setWrapStyleWord(true);
        notificationsArea.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppConstants.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(16, 16, 16, 16)
        ));

        container.add(headerLabel, BorderLayout.NORTH);
        container.add(notificationsArea, BorderLayout.CENTER);

        return container;
    }

    private JPanel createQuickLinkCard(String title, String content, Runnable action) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppConstants.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(18, 18, 18, 18)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        titleLabel.setForeground(AppConstants.TEXT_COLOR);
        titleLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JTextArea contentArea = new JTextArea(content);
        contentArea.setEditable(false);
        contentArea.setFocusable(false);
        contentArea.setOpaque(false);
        contentArea.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        contentArea.setForeground(AppConstants.TEXT_SECONDARY);
        contentArea.setLineWrap(true);
        contentArea.setWrapStyleWord(true);
        contentArea.setBorder(null);
        contentArea.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        java.awt.event.MouseAdapter clickListener = new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (action != null) {
                    action.run();
                }
            }
        };

        card.addMouseListener(clickListener);
        titleLabel.addMouseListener(clickListener);
        contentArea.addMouseListener(clickListener);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(contentArea, BorderLayout.CENTER);

        return card;
    }

    private JPanel createStatCard(String label, String value) {
        JPanel card = new JPanel(new BorderLayout());
        card.setBackground(Color.WHITE);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AppConstants.BORDER_COLOR, 1),
                BorderFactory.createEmptyBorder(24, 20, 24, 20)
        ));

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        valueLabel.setForeground(AppConstants.TEXT_COLOR);

        JLabel labelLabel = new JLabel(label);
        labelLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        labelLabel.setForeground(AppConstants.TEXT_SECONDARY);

        card.add(valueLabel, BorderLayout.NORTH);
        card.add(Box.createVerticalStrut(8), BorderLayout.CENTER);
        card.add(labelLabel, BorderLayout.SOUTH);

        return card;
    }

    private Employee findCurrentEmployee() {
    try {
        if (AppUtils.getCurrentUser() == null) {
            return null;
        }

        String loginKey = String.valueOf(AppUtils.getCurrentUser().getEmployeeId());
        List<Employee> employees = employeeController.getAllEmployees();

        for (Employee emp : employees) {
            String empNo = String.valueOf(emp.getEmployeeId());
            if (empNo.equals(loginKey)) {
                return emp;
            }
        }
    } catch (Exception e) {
        System.out.println("Unable to resolve current employee: " + e.getMessage());
    }

    return null;
}

    public void openPayslipDialog() {
        if (currentEmployee == null) {
            showInfo("My Payslip", "Employee data not found.");
            return;
        }

        String payslip =
                "Employee Number: " + currentEmployee.getEmployeeId() +
                "\nName: " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName() +
                "\nPosition: " + safeText(currentEmployee.getPosition()) +
                "\nStatus: " + safeText(currentEmployee.getStatus()) +
                "\nSupervisor: " + safeText(currentEmployee.getSupervisor()) +
                "\n\nBasic Salary: " + formatMoney(currentEmployee.getBasicSalary()) +
                "\nRice Subsidy: " + formatMoney(currentEmployee.getRiceSubsidy()) +
                "\nPhone Allowance: " + formatMoney(currentEmployee.getPhoneAllowance()) +
                "\nClothing Allowance: " + formatMoney(currentEmployee.getClothingAllowance()) +
                "\nGross Semi-monthly Rate: " + formatMoney(currentEmployee.getGrossSemiMonthlyRate()) +
                "\nHourly Rate: " + formatMoney(currentEmployee.getHourlyRate());

        showInfo("My Payslip", payslip);
    }

    public void openAttendanceDialog() {
        if (currentEmployee == null) {
            showInfo("Attendance", "Employee data not found.");
            return;
        }

        String attendanceInfo =
                "Employee Number: " + currentEmployee.getEmployeeId() +
                "\nName: " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName() +
                "\n\nPresent: 10" +
                "\nLate: 1" +
                "\nAbsent: 0" +
                "\nUndertime: 0" +
                "\n\nNote: Replace these sample values with your attendanceRecord.csv data.";

        showInfo("Attendance", attendanceInfo);
    }

    public void showLeaveBalanceInfo() {
        if (currentEmployee == null) {
            showInfo("Leave Balance", "Employee data not found.");
            return;
        }

        String leaveInfo =
                "Employee Number: " + currentEmployee.getEmployeeId() +
                "\nName: " + currentEmployee.getFirstName() + " " + currentEmployee.getLastName() +
                "\n\nVacation Leave: 5" +
                "\nSick Leave: 3" +
                "\nEmergency Leave: 0" +
                "\n\nNote: Replace these sample values with your leave records.";

        showInfo("Leave Balance", leaveInfo);
    }

    private void showInfo(String title, String message) {
        JTextArea area = new JTextArea(message);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        area.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        area.setBackground(Color.WHITE);
        area.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JScrollPane scrollPane = new JScrollPane(area);
        scrollPane.setPreferredSize(new Dimension(420, 260));

        JOptionPane.showMessageDialog(this, scrollPane, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private String safeText(Object value) {
        return value == null ? "N/A" : String.valueOf(value);
    }

    private String formatMoney(Object value) {
        if (value == null) return "N/A";

        try {
            String clean = String.valueOf(value).replace(",", "").trim();
            double amount = Double.parseDouble(clean);
            return String.format("₱%,.2f", amount);
        } catch (Exception e) {
            return String.valueOf(value);
        }
    }
}