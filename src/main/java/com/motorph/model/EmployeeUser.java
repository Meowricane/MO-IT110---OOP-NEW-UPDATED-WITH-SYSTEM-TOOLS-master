package com.motorph.model;

public class EmployeeUser extends User {
    public EmployeeUser(String username, String password, int employeeId, boolean isActive) {
        super(username, password, employeeId, "EMPLOYEE", isActive);
    }

    @Override
    public boolean canPerformAction(String action) {
        // Employee has limited access
        return action.equalsIgnoreCase("VIEW_PAYSLIP") ||
               action.equalsIgnoreCase("SUBMIT_LEAVE");
    }
}