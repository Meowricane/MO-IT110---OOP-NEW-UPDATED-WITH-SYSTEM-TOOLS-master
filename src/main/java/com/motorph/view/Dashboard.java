package com.motorph.view;

import java.awt.*;
import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.*;

import com.motorph.controller.EmployeeController;
import com.motorph.model.Employee;
import com.motorph.util.AppConstants;
import com.motorph.util.AppUtils;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

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

        try {
            List<AttendanceRecord> employeeRecords =
                    loadAttendanceRecordsForEmployee(currentEmployee.getEmployeeId());

            if (employeeRecords.isEmpty()) {
                showInfo("Attendance", "No attendance records found for this employee.");
                return;
            }

            int presentCount = employeeRecords.size();
            int lateCount = countLateArrivals(employeeRecords);
            int undertimeCount = countUndertime(employeeRecords);

            List<AttendanceRecord> lastFiveRecords = getLastFiveRecords(employeeRecords);

            StringBuilder summary = new StringBuilder();
            summary.append("Employee Number: ").append(currentEmployee.getEmployeeId()).append("\n");
            summary.append("Name: ").append(currentEmployee.getFirstName())
                   .append(" ")
                   .append(currentEmployee.getLastName())
                   .append("\n\n");

            summary.append("SUMMARY\n");
            summary.append("-------------------------------------\n");
            summary.append("Present: ").append(presentCount).append("\n");
            summary.append("Late: ").append(lateCount).append("\n");
            summary.append("Undertime: ").append(undertimeCount).append("\n\n");

            summary.append("LAST 5 RECORDS\n");
            summary.append("--------------------------------------------------\n");
            summary.append(String.format("%-12s %-8s %-8s %-12s%n",
                    "Date", "Log In", "Log Out", "Status"));
            summary.append("--------------------------------------------------\n");

            for (AttendanceRecord record : lastFiveRecords) {
                String status = determineStatus(record);
                summary.append(String.format("%-12s %-8s %-8s %-12s%n",
                        formatDate(record.getDate()),
                        formatTime(record.getTimeIn()),
                        formatTime(record.getTimeOut()),
                        status));
            }

            showInfo("Attendance", summary.toString());

        } catch (Exception e) {
            showInfo("Attendance", "Error loading attendance data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private List<AttendanceRecord> loadAttendanceRecordsForEmployee(int employeeNumber) {
        List<AttendanceRecord> records = new ArrayList<>();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader("data/attendanceRecord.csv"))
                .withCSVParser(new CSVParserBuilder().withSeparator(',').build())
                .build()) {

            List<String[]> rows = reader.readAll();

            for (int i = 1; i < rows.size(); i++) {
                String[] row = rows.get(i);

                if (row.length < 6) {
                    continue;
                }

                try {
                    int empId = Integer.parseInt(row[0].trim());
                    if (empId != employeeNumber) {
                        continue;
                    }

                    String dateValue = row[3] != null ? row[3].trim() : "";
                    String timeInValue = row[4] != null ? row[4].trim() : "";
                    String timeOutValue = row[5] != null ? row[5].trim() : "";

                    if (dateValue.isEmpty() || timeInValue.isEmpty() || timeOutValue.isEmpty()) {
                        continue;
                    }

                    LocalDate date = parseFlexibleDate(dateValue);
                    LocalTime timeIn = parseFlexibleTime(timeInValue);
                    LocalTime timeOut = parseFlexibleTime(timeOutValue);

                    AttendanceRecord record = new AttendanceRecord(empId, date, timeIn, timeOut);
                    records.add(record);

                } catch (Exception rowError) {
                    System.out.println("Skipping invalid attendance row #" + i + ": " + rowError.getMessage());
                }
            }

            records.sort((a, b) -> b.getDate().compareTo(a.getDate()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return records;
    }

    private int countLateArrivals(List<AttendanceRecord> records) {
        LocalTime lateThreshold = LocalTime.of(9, 0);
        return (int) records.stream()
                .filter(record -> record.getTimeIn() != null && record.getTimeIn().isAfter(lateThreshold))
                .count();
    }

    private int countUndertime(List<AttendanceRecord> records) {
        LocalTime undertimeThreshold = LocalTime.of(17, 0);
        return (int) records.stream()
                .filter(record -> record.getTimeOut() == null || record.getTimeOut().isBefore(undertimeThreshold))
                .count();
    }

    private List<AttendanceRecord> getLastFiveRecords(List<AttendanceRecord> records) {
        return records.stream()
                .limit(5)
                .collect(Collectors.toList());
    }

    private String determineStatus(AttendanceRecord record) {
        LocalTime lateThreshold = LocalTime.of(9, 0);
        LocalTime undertimeThreshold = LocalTime.of(17, 0);

        boolean isLate = record.getTimeIn() != null && record.getTimeIn().isAfter(lateThreshold);
        boolean isUndertime = record.getTimeOut() == null || record.getTimeOut().isBefore(undertimeThreshold);

        if (isLate && isUndertime) {
            return "Late & UT";
        } else if (isLate) {
            return "Late";
        } else if (isUndertime) {
            return "Undertime";
        }
        return "On Time";
    }

    private String formatDate(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
    }

    private String formatTime(LocalTime time) {
        if (time == null) return "--";
        return time.format(DateTimeFormatter.ofPattern("HH:mm"));
    }

    private LocalDate parseFlexibleDate(String value) {
        DateTimeFormatter[] formats = new DateTimeFormatter[] {
                DateTimeFormatter.ofPattern("M/d/yyyy"),
                DateTimeFormatter.ofPattern("MM/dd/yyyy"),
                DateTimeFormatter.ofPattern("M/d/yy"),
                DateTimeFormatter.ofPattern("MM/dd/yy")
        };

        for (DateTimeFormatter formatter : formats) {
            try {
                return LocalDate.parse(value, formatter);
            } catch (Exception ignored) {
            }
        }

        throw new IllegalArgumentException("Invalid date: " + value);
    }

    private LocalTime parseFlexibleTime(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        value = value.trim();

        DateTimeFormatter[] formats = new DateTimeFormatter[] {
            DateTimeFormatter.ofPattern("H:mm"),
            DateTimeFormatter.ofPattern("HH:mm"),
            DateTimeFormatter.ofPattern("H:mm:ss"),
            DateTimeFormatter.ofPattern("HH:mm:ss")
        };

        for (DateTimeFormatter formatter : formats) {
            try {
                return LocalTime.parse(value, formatter);
            } catch (Exception ignored) {
            }
        }

        throw new IllegalArgumentException("Invalid time: " + value);
    }

    private static class AttendanceRecord {
        private int employeeId;
        private LocalDate date;
        private LocalTime timeIn;
        private LocalTime timeOut;

        public AttendanceRecord(int employeeId, LocalDate date, LocalTime timeIn, LocalTime timeOut) {
            this.employeeId = employeeId;
            this.date = date;
            this.timeIn = timeIn;
            this.timeOut = timeOut;
        }

        public int getEmployeeId() { return employeeId; }
        public LocalDate getDate() { return date; }
        public LocalTime getTimeIn() { return timeIn; }
        public LocalTime getTimeOut() { return timeOut; }
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
                "\nEmergency Leave: 0";

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