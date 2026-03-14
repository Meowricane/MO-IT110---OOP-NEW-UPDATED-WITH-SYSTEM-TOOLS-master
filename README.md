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
Project Structure
MotorPH-PayrollSystem/
│
├── src/                           # Source code directory
│   ├── Employee.java              # Employee class for storing personal information
│   ├── FileHandler.java           # Utility for reading from data files
│   ├── Main.java                  # Main application & UI implementation
│   ├── PayrollCalculator.java     # Salary and deduction calculations
│   ├── TimeKeeping.java           # Daily attendance tracking
│   └── WeeklyAttendance.java      # Weekly hours aggregation and analysis
│
├── data/                          # Data files
│   ├── employee_data.txt          # Employee records
│   ├── login_credentials.txt      # System login information
│   └── attendance_*.txt           # Attendance records for different weeks
│
└── docs/
    └── MotorPH_PayrollSystem.txt  # System documentation
Setup Instructions
There are two ways to set up and run this project. Choose the method that works best for you:

Method 1: Quick Setup - Direct File Opening
This is the simplest method for reviewing or running the code:

Download and Extract Files

Download the ZIP file containing all project files
Extract the ZIP file to a location of your choice
Open Main.java Directly in NetBeans

Launch NetBeans
Go to File > Open File
Navigate to the extracted files and open Main.java
NetBeans should automatically detect other related files
Run the File

Right-click in the Main.java editor window
Select "Run File" or press Shift+F6
The application should start with a login screen
Method 2: Project Setup in NetBeans
This method creates a proper NetBeans project structure:

Download and Extract Files

Download the ZIP file containing all project files
Extract the ZIP file to a location of your choice
Create a New Java Project in NetBeans

Launch NetBeans
Go to File > New Project
Select "Java Application" from the Java category
Click Next
Name your project (e.g., "MotorPH-PayrollSystem")
Choose a location to save it
Uncheck "Create Main Class" (since you already have a Main.java)
Click Finish
Copy the Files to Your Project

Copy all .java files to the src folder of your NetBeans project
Copy all .txt files to the root directory of your project (not in src)
Fix the Main Class Configuration

Right-click on your project in NetBeans
Select "Properties"
Go to "Run"
Set the "Main Class" field to Main (not any package name, just Main)
Click OK to save the changes
Run the Project

Right-click on the project
Select "Run" or press F6
Login Credentials
The application will start with a login screen. Use the default credentials:

Username: 1
Password: 1
Troubleshooting
If you encounter a "Main class not found" error:

Double-check that the Main Class in Properties is set to exactly Main
Ensure that Main.java does not contain any package declaration at the top
Verify that all Java files are in the src directory of your project
If you have file not found errors:

Make sure all .txt files are in the correct location relative to how they're accessed in the code
Expected File Structure
The system expects certain data files to be present in the project directory:

login_credentials.txt - Contains username and password pairs
employee_data.txt - Contains employee information
attendance_[date].txt - Contains attendance records for specific weeks
If these files are not found, the system will create sample files automatically.

Default Login Credentials
Username: 1
Password: 1
Features
Login System

Secure access with username/password
Three attempts allowed before system exit
Employee Information

View basic employee details
Employee number, name, birthday
Attendance Tracking

Weekly view of clock-in/out times
Regular and overtime hours calculation
Late and undertime tracking
Payroll Calculation

Regular and overtime pay computation
Statutory deductions (SSS, PhilHealth, Pag-IBIG, Withholding Tax)
Late penalty calculation
Final net salary calculation
Statutory Deductions
The system implements the following Philippine statutory deductions:

SSS Contributions

Based on monthly salary brackets
Prorated for weekly calculations
PhilHealth Contributions

3% of monthly salary (equal share between employer and employee)
Minimum: PHP 300 (salary ≤ PHP 10,000)
Maximum: PHP 1,800 (salary ≥ PHP 60,000)
Pag-IBIG Contributions

1% for salaries up to PHP 1,500
2% for salaries over PHP 1,500
Maximum contribution: PHP 100
Withholding Tax

Progressive tax calculation based on taxable income
Applied after all other deductions
Development Notes
The system includes a 10-minute grace period for employee punctuality
Late penalties are calculated as 10% of regular pay multiplied by the proportion of late time
Late penalties are capped at 20% of regular pay
Regular work hours are 8:00 AM to 5:00 PM with a 1-hour lunch break
Standard work week is 40 hours, with overtime calculated beyond that
Testing
Sample data is included to test all system functionality:

Multiple employees with varying hourly rates
Sample attendance records with both regular and lateness scenarios
Different salary brackets to test all statutory deduction calculations
License
This project was developed as an educational exercise and is not licensed for commercial use.
