package com.motorph.view.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

import com.motorph.model.PaySlip;
import com.motorph.util.AppConstants;
import com.motorph.util.AppUtils;

public class PayslipDialog extends JDialog {

    public PayslipDialog(JFrame parent, PaySlip paySlip, String title) {
        super(parent, title, true);
        initComponents(paySlip);
    }

    private void initComponents(PaySlip paySlip) {
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 24, 20, 24));

        mainPanel.add(createHeaderPanel());
        mainPanel.add(Box.createVerticalStrut(18));
        mainPanel.add(createEmployeeInfoPanel(paySlip));
        mainPanel.add(Box.createVerticalStrut(18));
        mainPanel.add(createEntitlementsSection(paySlip));
        mainPanel.add(Box.createVerticalStrut(16));
        mainPanel.add(createDeductionsSection(paySlip));
        mainPanel.add(Box.createVerticalStrut(16));
        mainPanel.add(createNetPaySection(paySlip));

        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(14);
        scrollPane.setPreferredSize(new Dimension(900, 650));

        add(scrollPane, BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);

        pack();
        setLocationRelativeTo(getParent());
    }

    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel titleLabel = new JLabel("Payslip");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel businessLabel = new JLabel("MotorPH Payroll System");
        businessLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        businessLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel orgLabel = new JLabel("Phone: (028) 911-5071 / (028) 911-5072 / (028) 911-5073 ");
        orgLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        orgLabel.setAlignmentX(CENTER_ALIGNMENT);

        JLabel addressLabel = new JLabel("Office Address: 7 Jupiter Avenue cor. F. Sandoval Jr., Bagong Nayon, Quezon City");
        addressLabel.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        addressLabel.setAlignmentX(CENTER_ALIGNMENT);

        panel.add(titleLabel);
        panel.add(Box.createVerticalStrut(6));
        panel.add(businessLabel);
        panel.add(Box.createVerticalStrut(2));
        panel.add(orgLabel);
        panel.add(Box.createVerticalStrut(2));
        panel.add(addressLabel);

        return panel;
    }

    private JPanel createEmployeeInfoPanel(PaySlip paySlip) {
        JPanel wrapper = new JPanel(new GridLayout(1, 2, 24, 0));
        wrapper.setBackground(Color.WHITE);
        wrapper.setAlignmentX(LEFT_ALIGNMENT);

        JPanel leftPanel = new JPanel(new GridLayout(6, 2, 8, 6));
        leftPanel.setBackground(Color.WHITE);

        leftPanel.add(createLabelBold("Employee name:"));
        leftPanel.add(createLabelValue(paySlip.getEmployee().getFullName()));

        leftPanel.add(createLabelBold("Employment status:"));
        leftPanel.add(createLabelValue(safe(paySlip.getEmployee().getStatus())));

        leftPanel.add(createLabelBold("Award/agreement:"));
        leftPanel.add(createLabelValue("N/A"));

        leftPanel.add(createLabelBold("Classification:"));
        leftPanel.add(createLabelValue(safe(paySlip.getEmployee().getPosition())));

        leftPanel.add(createLabelBold("Hourly rate:"));
        leftPanel.add(createLabelValue(formatMoney(paySlip.getEmployee().getHourlyRate())));

        leftPanel.add(createLabelBold("Annual salary:"));
        leftPanel.add(createLabelValue(formatMoney(paySlip.getEmployee().getBasicSalary() * 12)));

        JPanel rightPanel = new JPanel(new GridLayout(6, 2, 8, 6));
        rightPanel.setBackground(Color.WHITE);

        rightPanel.add(createLabelBold("Pay period:"));
        rightPanel.add(createLabelValue(AppUtils.formatDateRange(paySlip.getStartDate(), paySlip.getEndDate())));

        rightPanel.add(createLabelBold("Pay date:"));
        rightPanel.add(createLabelValue(AppUtils.formatDate(paySlip.getEndDate())));

        rightPanel.add(createLabelBold("Annual leave balance:"));
        rightPanel.add(createLabelValue("5 days"));

        rightPanel.add(createLabelBold("Sick / carer's leave balance:"));
        rightPanel.add(createLabelValue("3 days"));

        rightPanel.add(createLabelBold("Regular hours:"));
        rightPanel.add(createLabelValue(String.format("%.2f", paySlip.getRegularHours())));

        rightPanel.add(createLabelBold("Overtime hours:"));
        rightPanel.add(createLabelValue(String.format("%.2f", paySlip.getOvertimeHours())));

        wrapper.add(leftPanel);
        wrapper.add(rightPanel);

        return wrapper;
    }

    private JPanel createEntitlementsSection(PaySlip paySlip) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel title = new JLabel("Entitlements");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));

        String[] columns = {"Description", "Rate", "Total"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        double hourlyRate = paySlip.getEmployee().getHourlyRate();
        double regularHours = paySlip.getRegularHours();
        double overtimeHours = paySlip.getOvertimeHours();
        double regularPay = regularHours * hourlyRate;
        double overtimeRate = hourlyRate * 1.25;
        double overtimePay = overtimeHours * overtimeRate;

        Map<String, Double> allowances = paySlip.getAllowances();

        double rice = getMapValue(allowances, "rice");
        double phone = getMapValue(allowances, "phone");
        double clothing = getMapValue(allowances, "clothing");

        model.addRow(new Object[]{"Ordinary hours", formatMoney(hourlyRate), formatMoney(regularPay)});
        model.addRow(new Object[]{"Overtime", formatMoney(overtimeRate), formatMoney(overtimePay)});
        model.addRow(new Object[]{"Rice Subsidy", formatMoney(rice), formatMoney(rice)});
        model.addRow(new Object[]{"Phone Allowance", formatMoney(phone), formatMoney(phone)});
        model.addRow(new Object[]{"Clothing Allowance", formatMoney(clothing), formatMoney(clothing)});
        model.addRow(new Object[]{"Total", "", formatMoney(paySlip.getGrossPay())});

        JTable table = createStyledTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(820, 150));

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createDeductionsSection(PaySlip paySlip) {
        JPanel panel = new JPanel(new BorderLayout(0, 8));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel title = new JLabel("Deductions");
        title.setFont(new Font("Segoe UI", Font.BOLD, 14));

        String[] columns = {"Description", "Rate", "Total"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        Map<String, Double> deductions = paySlip.getDeductions();

        double sss = getMapValue(deductions, "sss");
        double philhealth = getMapValue(deductions, "philhealth");
        double pagibig = getMapValue(deductions, "pagibig");
        double withholdingTax = getMapValue(deductions, "withholdingTax");

        double totalDeductions = sss + philhealth + pagibig + withholdingTax;

        model.addRow(new Object[]{"SSS", formatMoney(sss), formatMoney(sss)});
        model.addRow(new Object[]{"PhilHealth", formatMoney(philhealth), formatMoney(philhealth)});
        model.addRow(new Object[]{"Pag-IBIG", formatMoney(pagibig), formatMoney(pagibig)});
        model.addRow(new Object[]{"Income tax", formatMoney(withholdingTax), formatMoney(withholdingTax)});
        model.addRow(new Object[]{"Total", "", formatMoney(totalDeductions)});

        JTable table = createStyledTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(820, 130));

        panel.add(title, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);

        return panel;
    }

    private JPanel createNetPaySection(PaySlip paySlip) {
        JPanel panel = new JPanel(new GridLayout(3, 2, 8, 8));
        panel.setBackground(Color.WHITE);
        panel.setAlignmentX(LEFT_ALIGNMENT);
        panel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        panel.add(createLabelBold("Bank details:"));
        panel.add(createLabelValue("MotorPH Payroll Bank"));

        panel.add(createLabelBold("Account number:"));
        panel.add(createLabelValue("**** **** **** 1234"));

        JLabel totalLabel = createLabelBold("Total net pay:");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 15));

        JLabel netPayValue = createLabelValue(formatMoney(paySlip.getNetPay()));
        netPayValue.setFont(new Font("Segoe UI", Font.BOLD, 15));

        panel.add(totalLabel);
        panel.add(netPayValue);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 12, 0));

        JButton closeButton = new JButton("Close");
        closeButton.setFont(AppConstants.NORMAL_FONT);
        closeButton.setBackground(AppConstants.BUTTON_COLOR);
        closeButton.setForeground(Color.BLACK);
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(e -> dispose());

        buttonPanel.add(closeButton);
        return buttonPanel;
    }

    private JTable createStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setRowHeight(24);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setGridColor(new Color(230, 230, 230));
        table.setShowGrid(true);
        table.setFillsViewportHeight(true);

        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 12));
        header.setBackground(new Color(240, 240, 240));
        header.setForeground(Color.BLACK);

        return table;
    }

    private JLabel createLabelBold(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        return label;
    }

    private JLabel createLabelValue(String text) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        return label;
    }

    private String safe(String value) {
        return value == null || value.trim().isEmpty() ? "N/A" : value;
    }

    private double getMapValue(Map<String, Double> map, String key) {
        if (map == null) {
            return 0.0;
        }
        Double value = map.get(key);
        return value == null ? 0.0 : value;
    }

    private String formatMoney(double value) {
        return String.format("₱%,.2f", value);
    }
}