package com.motorph.model;

/**
 * IT user class
 */
public class ITUser extends User {

    public ITUser(String username, String password, int employeeId, boolean isActive) {
        super(username, password, employeeId, "IT", isActive); // role is IT
    }

    @Override
    public boolean canPerformAction(String action) {
        // IT users can only access system tools actions
        return action.equalsIgnoreCase("SYSTEM_TOOLS");
    }

    @Override
    public String getRole() {
        return "IT";
    }
}