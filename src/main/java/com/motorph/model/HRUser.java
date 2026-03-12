package com.motorph.model;

public class HRUser extends User {
    public HRUser(String username, String password, int employeeId, boolean isActive) {
        super(username, password, employeeId, "HR", isActive);
    }

    @Override
    public boolean canPerformAction(String action) {
        // HR can manage employees but not process payroll
        return action.equalsIgnoreCase("ADD_EMPLOYEE") ||
               action.equalsIgnoreCase("EDIT_EMPLOYEE") ||
               action.equalsIgnoreCase("VIEW_EMPLOYEE");
    }
}