package com.motorph.model;

public class AdminUser extends User {
    public AdminUser(String username, String password, int employeeId, boolean isActive) {
        super(username, password, employeeId, "ADMIN", isActive);
    }

    @Override
    public boolean canPerformAction(String action) {
        // Admin can perform all actions
        return true;
    }
}