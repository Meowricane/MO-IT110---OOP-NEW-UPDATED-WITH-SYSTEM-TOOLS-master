## **MotorPH Payroll System (Phase 1)**
A Java-based payroll management system developed for MotorPH, designed to handle employee information, attendance tracking, and salary calculations including statutory deductions.

**Project Overview**
The MotorPH Payroll System fulfills Phase 1 requirements of MotorPH's plan to develop an end-to-end inventory and payroll system. This initial phase covers:

Presentation of employee details
Calculation of hours worked based on time records
Calculation of gross weekly salary
Calculation of net weekly salary after applying statutory deductions
System Requirements
Java Development Kit (JDK) 8 or higher
NetBeans IDE (recommended: version 8.2 or higher)
Minimum 512MB RAM
100MB free disk space
## **Project Structure**
# MotorPH Payroll System

A Java-based payroll management system developed for MotorPH, designed to handle employee information, authentication, attendance tracking, leave requests, payslip generation, payroll calculations, and summary reporting.

---

## Project Overview

The MotorPH Payroll System is a desktop Java Swing application that supports core HR and payroll operations for MotorPH. Based on the current implementation, the system includes:

- User authentication with role-based access
- Presentation and management of employee details
- Attendance viewing and attendance-based payroll computation
- Payslip generation with allowances and statutory deductions
- Weekly and monthly summary reports
- Leave request filing
- Role-specific modules for Admin, HR, Finance, IT, and Employee users

The system uses CSV files as its main data source and is organized using MVC-style packages such as controller, service, dao, model, util, and view.

---

## System Requirements

- Java Development Kit (JDK) 8 or higher
- NetBeans IDE (recommended: 8.2 or higher)
- Minimum 512MB RAM
- At least 100MB free disk space

---

## Project Structure
```text
MotorPH-PayrollSystem/
│
├── src/
│   └── com/
│       └── motorph/
│           ├── controller/                  # Controllers for app logic
│           │   ├── AuthenticationController.java
│           │   ├── EmployeeController.java
│           │   ├── PayrollController.java
│           │   └── ReportController.java
│           │
│           ├── dao/                         # CSV data access
│           │   └── AttendanceDAO.java
│           │
│           ├── leave/                       # Leave request models and services
│           │   ├── LeaveRequest.java
│           │   └── LeaveRequestService.java
│           │
│           ├── model/                       # Core domain models
│           │   ├── AttendanceRecord.java
│           │   ├── Employee.java
│           │   ├── PaySlip.java
│           │   ├── User.java
│           │   ├── AdminUser.java
│           │   ├── EmployeeUser.java
│           │   ├── HRUser.java
│           │   ├── FinanceUser.java
│           │   └── ITUser.java
│           │
│           ├── security/                    # Role permissions
│           │   └── RolePermission.java
│           │
│           ├── service/                     # Business logic services
│           │   ├── AuthenticationService.java
│           │   ├── EmployeeService.java
│           │   └── PayrollProcessor.java
│           │
│           ├── util/                        # Utilities and constants
│           │   ├── AppConstants.java
│           │   └── AppUtils.java
│           │
│           └── view/                        # Swing UI screens and panels
│               ├── Dashboard.java
│               ├── EmployeePanel.java
│               ├── HeaderPanel.java
│               ├── LeavePanel.java
│               ├── Login.java
│               ├── LoginPanel.java
│               ├── MainFrame.java
│               ├── Payroll.java
│               ├── Reports.java
│               ├── ToolsPanel.java
│               │
│               └── dialog/                  # Popup dialogs
│                   ├── AttendanceViewerDialog.java
│                   ├── DateRangeDialog.java
│                   ├── EmployeeDetailsDialog.java
│                   ├── EmployeeDialog.java
│                   ├── EmployeeNumberInputDialog.java
│                   ├── PayslipDialog.java
│                   ├── SearchResultDialog.java
│                   └── WeeklySummaryReportDialog.java
│
├── data/                                    # CSV data files
│   ├── attendanceRecord.csv
│   ├── employeeDetails.csv
│   ├── leave_requests.csv
│   └── userCredentials.csv
│
└── README.md
##**Setup Instructions**
There are two ways to run this project.
