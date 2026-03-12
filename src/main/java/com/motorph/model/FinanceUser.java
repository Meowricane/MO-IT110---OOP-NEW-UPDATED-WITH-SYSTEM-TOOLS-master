package com.motorph.model;

public class FinanceUser extends User {
    public FinanceUser(String username, String password, int employeeId, boolean isActive) {
        super(username, password, employeeId, "FINANCE", isActive);
    }

    @Override
    public boolean canPerformAction(String action) {
        // Finance can generate payroll and view payslips
        return action.equalsIgnoreCase("GENERATE_PAYSLIP") ||
               action.equalsIgnoreCase("VIEW_PAYROLL");
    }
}