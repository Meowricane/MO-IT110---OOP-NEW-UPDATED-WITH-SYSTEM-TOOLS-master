package com.motorph.dao;

import com.motorph.model.AttendanceRecord;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVParserBuilder;

import java.io.FileReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO class responsible for loading attendance records from the CSV file.
 */
public class AttendanceDAO {

    private static final String ATTENDANCE_FILE = "data/attendanceRecord.csv";

    /**
     * Loads all attendance records from the CSV file.
     *
     * @return List of AttendanceRecord objects
     */
    public List<AttendanceRecord> loadAttendanceRecords() {
        List<AttendanceRecord> records = new ArrayList<>();

        try (CSVReader reader = new CSVReaderBuilder(new FileReader(ATTENDANCE_FILE))
                .withCSVParser(new CSVParserBuilder().withSeparator('\t').build())
                .build()) {

            List<String[]> rows = reader.readAll();

            for (int i = 1; i < rows.size(); i++) { // Skip header
                String[] row = rows.get(i);

                if (row.length < 6) {
                    continue;
                }

                try {
                    int employeeId = Integer.parseInt(row[0].trim());
                    LocalDate date = parseFlexibleDate(row[3].trim());
                    LocalTime timeIn = parseFlexibleTime(row[4].trim());
                    LocalTime timeOut = parseFlexibleTime(row[5].trim());

                    AttendanceRecord record = new AttendanceRecord(employeeId, date, timeIn, timeOut);
                    records.add(record);

                } catch (Exception rowError) {
                    System.out.println("Skipping invalid attendance row #" + i + ": " + rowError.getMessage());
                }
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

    /**
     * Flexible date parser.
     */
    private LocalDate parseFlexibleDate(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        value = value.trim();

        DateTimeFormatter[] formats = new DateTimeFormatter[] {
                DateTimeFormatter.ofPattern("M/d/yy"),
                DateTimeFormatter.ofPattern("M/d/yyyy"),
                DateTimeFormatter.ofPattern("MM/dd/yy"),
                DateTimeFormatter.ofPattern("MM/dd/yyyy")
        };

        for (DateTimeFormatter formatter : formats) {
            try {
                return LocalDate.parse(value, formatter);
            } catch (Exception ignored) {
            }
        }

        throw new IllegalArgumentException("Invalid date: " + value);
    }

    /**
     * Flexible time parser.
     */
    private LocalTime parseFlexibleTime(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        value = value.trim();

        DateTimeFormatter[] formats = new DateTimeFormatter[] {
                DateTimeFormatter.ofPattern("H:mm"),
                DateTimeFormatter.ofPattern("HH:mm"),
                DateTimeFormatter.ofPattern("H:mm:ss"),
                DateTimeFormatter.ofPattern("HH:mm:ss")
        };

        for (DateTimeFormatter formatter : formats) {
            try {
                return LocalTime.parse(value, formatter);
            } catch (Exception ignored) {
            }
        }

        throw new IllegalArgumentException("Invalid time: " + value);
    }
}