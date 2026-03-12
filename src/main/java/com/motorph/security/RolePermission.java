package com.motorph.security;

public class RolePermission {

    private String role;

    public RolePermission(String role) {
        this.role = role;
    }

    public boolean isAdmin() {
        return role.equalsIgnoreCase("ADMIN");
    }

    public boolean isHR() {
        return role.equalsIgnoreCase("HR");
    }

    public boolean canAccessEmployee() {
        return role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("HR");
    }

    public boolean canAccessPayroll() {
        return role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("FINANCE");
    }

    public boolean canAccessReports() {
        return role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("FINANCE");
    }

    public boolean canAccessLeave() {
        return true; // all roles can request leave
    }

    public boolean canAccessDashboard() {
        return true;
    }

    // NEW: IT and Admin can access system tools
    public boolean canAccessTools() {
        return role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("IT");
    }
}