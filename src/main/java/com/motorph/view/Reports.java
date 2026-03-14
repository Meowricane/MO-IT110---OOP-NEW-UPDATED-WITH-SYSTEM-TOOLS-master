package com.motorph.view;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.motorph.controller.ReportController;
import com.motorph.util.AppConstants;
import com.motorph.view.dialog.DateRangeDialog;
import com.motorph.view.dialog.EmployeeNumberInputDialog;
import com.motorph.view.dialog.PayslipDialog;
import com.motorph.view.dialog.WeeklySummaryReportDialog;

/**
 * Panel for report generation functions.
 */
public class Reports extends JPanel {

    private final MainFrame mainFrame;
    private final ReportController reportController;

    public Reports(MainFrame mainFrame, ReportController reportController) {
        this.mainFrame = mainFrame;
        this.reportController = reportController;
        initPanel();
    }

    private void initPanel() {
        setLayout(new BorderLayout(10, 10));
        setBackground(AppConstants.BACKGROUND_COLOR);

        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(AppConstants.BACKGROUND_COLOR);
        JLabel titleLabel = new JLabel("Reports", SwingConstants.CENTER);
        titleLabel.setFont(AppConstants.TITLE_FONT);
        titlePanel.add(titleLabel);
        add(titlePanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        buttonPanel.setBackground(AppConstants.BACKGROUND_COLOR);

        JButton payslipButton = createStyledButton("Payslip");
        JButton weeklySummaryButton = createStyledButton("Weekly Summary");
        JButton monthlySummaryButton = createStyledButton("Monthly Summary");
        JButton backButton = createStyledButton("Back to Main Menu");

        buttonPanel.add(payslipButton);
        buttonPanel.add(weeklySummaryButton);
        buttonPanel.add(monthlySummaryButton);
        buttonPanel.add(backButton);

        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(AppConstants.BACKGROUND_COLOR);
        centerPanel.add(buttonPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        payslipButton.addActionListener(e -> generatePayslipReport());
        weeklySummaryButton.addActionListener(e -> generateSummaryReport("Weekly"));
        monthlySummaryButton.addActionListener(e -> generateSummaryReport("Monthly"));
        backButton.addActionListener(e -> mainFrame.showMainMenu());
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setBackground(AppConstants.BUTTON_COLOR);
        button.setFont(AppConstants.NORMAL_FONT);
        button.setFocusPainted(false);
        return button;
    }

    private void generatePayslipReport() {
        EmployeeNumberInputDialog employeeDialog = new EmployeeNumberInputDialog(mainFrame, "Payslip Report");
        employeeDialog.setVisible(true);

        if (!employeeDialog.isConfirmed()) {
            return;
        }

        int employeeId = employeeDialog.getEmployeeNumber();

        DateRangeDialog dateDialog = new DateRangeDialog(mainFrame, "Select Report Period");
        dateDialog.setVisible(true);

        if (!dateDialog.isConfirmed()) {
            return;
        }

        LocalDate startDate = dateDialog.getStartDate();
        LocalDate endDate = dateDialog.getEndDate();

        try {
            var payslip = reportController.generatePayslipReport(employeeId, startDate, endDate);
            new PayslipDialog(mainFrame, payslip, "PAYSLIP REPORT").setVisible(true);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Error generating payslip report: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void generateSummaryReport(String reportType) {
        DateRangeDialog dateDialog = new DateRangeDialog(mainFrame, "Select " + reportType + " Report Period");
        dateDialog.setVisible(true);

        if (!dateDialog.isConfirmed()) {
            return;
        }

        LocalDate startDate = dateDialog.getStartDate();
        LocalDate endDate = dateDialog.getEndDate();

        try {
            var summaryData = reportController.generateSummaryReport(reportType, startDate, endDate);

            if (summaryData.isEmpty()) {
                JOptionPane.showMessageDialog(
                        mainFrame,
                        "No data available for the specified period.",
                        reportType + " Summary Report",
                        JOptionPane.INFORMATION_MESSAGE
                );
                return;
            }

            WeeklySummaryReportDialog dialog =
                    new WeeklySummaryReportDialog(mainFrame);

            for (Map<String, Object> row : summaryData) {
                dialog.addReportRow(
                        String.valueOf(row.get("employeeId")),
                        String.valueOf(row.get("name")),
                        String.format("%.2f", ((Number) row.get("totalHours")).doubleValue()),
                        String.format("%.2f", ((Number) row.get("grossPay")).doubleValue()),
                        String.format("%.2f", ((Number) row.get("netPay")).doubleValue())
                );
            }

            dialog.setTitle(reportType + " Summary Report");
            dialog.setVisible(true);

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        } catch (Exception e) {
            JOptionPane.showMessageDialog(
                    mainFrame,
                    "Error generating " + reportType.toLowerCase() + " summary report: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}