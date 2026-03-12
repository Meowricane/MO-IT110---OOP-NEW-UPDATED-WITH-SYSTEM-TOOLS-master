package com.motorph.dao;

import com.motorph.model.AttendanceRecord;
import com.opencsv.CSVReader;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class responsible for loading attendance records from the CSV file.
 */
public class AttendanceDAO {

    private static final String ATTENDANCE_FILE = "data/attendance.csv";

    /**
     * Loads all attendance records from the CSV file.
     *
     * @return List of AttendanceRecord objects
     */
    public List<AttendanceRecord> loadAttendanceRecords() {

        List<AttendanceRecord> records = new ArrayList<>();

        try (CSVReader reader = new CSVReader(new FileReader(ATTENDANCE_FILE))) {

            List<String[]> rows = reader.readAll();

            for (int i = 1; i < rows.size(); i++) { // Skip header

                String[] row = rows.get(i);

                int employeeId = Integer.parseInt(row[0].trim());
                LocalDate date = LocalDate.parse(row[1].trim());
                LocalTime timeIn = LocalTime.parse(row[2].trim());
                LocalTime timeOut = LocalTime.parse(row[3].trim());

                AttendanceRecord record =
                        new AttendanceRecord(employeeId, date, timeIn, timeOut);

                records.add(record);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return records;
    }

    /**
     * Retrieves attendance records for a specific employee.
     *
     * @param employeeId employee ID
     * @return list of attendance records
     */
    public List<AttendanceRecord> getAttendanceByEmployee(int employeeId) {

        List<AttendanceRecord> employeeRecords = new ArrayList<>();

        List<AttendanceRecord> allRecords = loadAttendanceRecords();

        for (AttendanceRecord record : allRecords) {
            if (record.getEmployeeId() == employeeId) {
                employeeRecords.add(record);
            }
        }

        return employeeRecords;
    }
}