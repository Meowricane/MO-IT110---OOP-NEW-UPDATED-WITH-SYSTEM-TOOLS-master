# MotorPH Payroll System (Phase 1)

A Java Swing desktop payroll management system developed for MotorPH. Phase 1 implements core HR and payroll operations including employee management, attendance-based payroll computation, payslip generation, leave requests, and role-based access control. CSV files are used as the primary data source.

---

## Project Overview

The system currently includes:

- User authentication with role-based access (Admin, HR, Finance, IT, Employee)
- Presentation and management of employee details
- Attendance viewing and attendance-based payroll computation
- Payslip generation with allowances and statutory deductions
- Weekly and monthly summary reports
- Leave request filing and basic leave management
- Role-specific modules and permissions

The project follows an MVC-like organization with packages for controller, dao, model, service, util, and view.

---

## System Requirements

- Java Development Kit (JDK) 8 or higher  
- NetBeans IDE (recommended: 8.2 or higher)  
- Minimum 512 MB RAM  
- At least 100 MB free disk space

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
```

---

## Setup Instructions

There are two ways to set up and run this project.

### Method 1 — Open Existing NetBeans Project (recommended if you have the full project)

1. Download the ZIP file containing the whole project and extract it to your preferred location.  
2. Launch NetBeans.  
3. Go to File > Open Project and browse to the extracted MotorPH project folder.  
4. Open the project.  
5. Right-click the project and select Run (or press F6). The application should start at the login screen.

### Method 2 — Create a New Java Project in NetBeans (if you only have source/data files)

1. Launch NetBeans.  
2. Go to File > New Project > Java Application. Click Next.  
3. Name the project (e.g., MotorPH-PayrollSystem), choose the save location, uncheck Create Main Class, and click Finish.  
4. Copy the source folders (com/motorph/...) into the project's `src` folder, preserving package structure exactly as shown in the Project Structure above.  
5. Create a `data` folder at the project root and copy the CSV files into it:
   - `employeeDetails.csv`  
   - `userCredentials.csv`  
   - `attendanceRecord.csv`  
   - `leave_requests.csv`  
6. Right-click the project > Properties > Run and set the Main Class to the entry point that launches the Login or MainFrame.  
7. Right-click the project and select Run (or press F6).

---

## Data Files

The application expects the following CSV files inside the project root `data/` folder (paths are relative to the project root):

- `data/userCredentials.csv` — login credentials and roles  
- `data/employeeDetails.csv` — employee master information  
- `data/attendanceRecord.csv` — employee attendance logs  
- `data/leave_requests.csv` — leave requests  

Make sure the code's file paths match this location or update the paths in the config/constants (`AppConstants.java`) accordingly.

---

## Example Login Credentials

Example account for testing:

- Username: `admin`  
- Password: `admin123`

Other accounts and roles depend on the contents of `userCredentials.csv`. Ensure the `isActive` field is set to TRUE for users you want to log in with.

---

## Features

- Authentication and role-based access control (uses `userCredentials.csv` and `RolePermission.java`)  
- Employee management: view, search, add, edit, delete employees  
- Attendance tracking: loads `attendanceRecord.csv`, flexible date/time parsing, attendance summary (present, late, undertime)  
- Payroll calculation: gross/net pay, SSS, PhilHealth, Pag-IBIG, withholding tax (handled by `PayrollProcessor.java` and `PaySlip.java`)  
- Payslip generation: detailed payslip dialog with employee info, deductions, allowances, and net pay  
- Reports: weekly and monthly summary reports (hours, gross, net)  
- Leave management: file and store leave requests in `leave_requests.csv`  
- UI: Java Swing-based screens and dialogs; utility helpers in `AppUtils.java` and constants in `AppConstants.java`

---

## Troubleshooting

- Login failure:
  - Confirm `data/userCredentials.csv` exists and contains the correct username/password.
  - Ensure the `isActive` field is `TRUE`.
- Missing employee data:
  - Verify `data/employeeDetails.csv` exists.
  - Ensure employee IDs in `userCredentials.csv` match those in `employeeDetails.csv`.
- Attendance not showing:
  - Verify `data/attendanceRecord.csv` exists and has rows with Employee #, Date, Log In, Log Out.
  - Ensure employee IDs in attendance match employee records.
- Payslip values are zero:
  - Verify `PaySlip.generate(...)` is invoked and attendance records exist for the selected period.
  - Confirm the date range matches actual attendance dates.
- UI/dialog issues:
  - Save files, run Clean and Build in NetBeans, then run the application again.

---

## Testing

Recommended tests:

- Login using different roles (Admin, HR, Finance, IT, Employee)  
- Open dashboard and quick links (Payslip, Attendance, Leave Balance, Leave Request)  
- View attendance records for users with existing logs  
- Generate payslip for a selected period  
- Generate weekly and monthly summary reports  
- Add, edit, and delete employee records  
- File a leave request and verify it is saved to `leave_requests.csv`

---

## Development Notes

- UI implemented with Java Swing.  
- CSV files used in place of a database for simplicity (see `dao` classes).  
- Attendance parsing supports multiple date/time formats.  
- Utility class `AppUtils` handles session, formatting, validation, and UI helpers.  
- Header panel can show an employee’s full name instead of username.  
- Logout returns the user to the login screen.

---

## License

This project was developed as an educational system for MotorPH and is not intended for commercial distribution.
