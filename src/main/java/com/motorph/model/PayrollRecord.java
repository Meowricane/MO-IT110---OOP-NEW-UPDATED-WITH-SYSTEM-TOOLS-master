package com.motorph.model;

import java.time.LocalDate;
import java.util.List;

import com.motorph.service.PayrollService;
import com.motorph.service.PayrollProcessor;

/**
 * Represents a payroll record manager that handles payroll runs
 * and communicates with PayrollService to generate payslips.
 */
public class PayrollRecord {

    private final PayrollService payrollService;
    private final PayrollProcessor payrollProcessor;

    public PayrollRecord(PayrollService payrollService, PayrollProcessor payrollProcessor) {
        this.payrollService = payrollService;
        this.payrollProcessor = payrollProcessor;
    }

    /**
     * Generate a payroll run for all employees for a given date range
     */
    public PayrollRun createPayrollRun(LocalDate startDate, LocalDate endDate, String processedBy) {
        PayrollRun run = new PayrollRun(startDate, endDate, processedBy);

        // Generate payslips for all employees
        List<PaySlip> payslips = payrollService.generatePayroll(startDate, endDate);
        run.setPaySlips(payslips); // Totals will be automatically calculated in PayrollRun

        // No need to manually set totalGrossPay, totalNetPay, totalDeductions
        // PayrollRun.calculateTotals() already handles this

        return run;
    }

    /**
     * Approve a payroll run
     */
    public void approvePayrollRun(PayrollRun run, String approvedBy) {
        run.setApprovedBy(approvedBy);
        run.setApprovedAt(LocalDate.now().atStartOfDay());
        run.setStatus(PayrollStatus.APPROVED);
    }
}