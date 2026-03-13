package com.motorph.leave;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LeaveRequestService {

    private final List<LeaveRequest> leaveRequests;
    private final String csvFilePath;
    private final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public LeaveRequestService(String csvFilePath) {
        this.csvFilePath = csvFilePath;
        this.leaveRequests = new ArrayList<>();
        loadLeaveRequestsFromCSV();
    }

    public void addLeaveRequest(LeaveRequest leaveRequest) {
        leaveRequests.add(leaveRequest);
        saveAllLeaveRequestsToCSV();
    }

    public List<LeaveRequest> getAllLeaveRequests() {
        return new ArrayList<>(leaveRequests);
    }

    public void deleteLeaveRequest(LeaveRequest leaveRequest) {
        Iterator<LeaveRequest> iterator = leaveRequests.iterator();
        while (iterator.hasNext()) {
            LeaveRequest lr = iterator.next();
            if (isSameRequest(lr, leaveRequest)) {
                iterator.remove();
                break;
            }
        }
        saveAllLeaveRequestsToCSV();
    }

    public void approveLeaveRequest(LeaveRequest leaveRequest) {
        for (LeaveRequest lr : leaveRequests) {
            if (isSameRequest(lr, leaveRequest)) {
                lr.setStatus("APPROVED");
                break;
            }
        }
        saveAllLeaveRequestsToCSV();
    }

    public void denyLeaveRequest(LeaveRequest leaveRequest) {
        for (LeaveRequest lr : leaveRequests) {
            if (isSameRequest(lr, leaveRequest)) {
                lr.setStatus("DENIED");
                break;
            }
        }
        saveAllLeaveRequestsToCSV();
    }

    private boolean isSameRequest(LeaveRequest a, LeaveRequest b) {
        return a.getEmployeeName().equals(b.getEmployeeName()) &&
               a.getLeaveType().equals(b.getLeaveType()) &&
               a.getStartDate().equals(b.getStartDate()) &&
               a.getEndDate().equals(b.getEndDate()) &&
               a.getNotes().equals(b.getNotes());
    }

    private void saveAllLeaveRequestsToCSV() {
        try (CSVWriter writer = new CSVWriter(new FileWriter(csvFilePath))) {
            String[] header = {"Employee Name", "Leave Type", "Start Date", "End Date", "Notes", "Status"};
            writer.writeNext(header);

            for (LeaveRequest request : leaveRequests) {
                String[] data = {
                        request.getEmployeeName(),
                        request.getLeaveType(),
                        request.getStartDate() != null ? request.getStartDate().format(dateFormatter) : "",
                        request.getEndDate() != null ? request.getEndDate().format(dateFormatter) : "",
                        request.getNotes() != null ? request.getNotes() : "",
                        request.getStatus() != null ? request.getStatus() : "PENDING"
                };
                writer.writeNext(data);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to save leave requests to CSV: " + e.getMessage(), e);
        }
    }

    private void loadLeaveRequestsFromCSV() {
        File file = new File(csvFilePath);
        if (!file.exists()) return;

        try (CSVReader reader = new CSVReader(new FileReader(file))) {
            String[] line;
            boolean isHeader = true;

            while ((line = reader.readNext()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String employeeName = line[0];
                String leaveType = line[1];
                LocalDate startDate = !line[2].isEmpty() ? LocalDate.parse(line[2], dateFormatter) : null;
                LocalDate endDate = !line[3].isEmpty() ? LocalDate.parse(line[3], dateFormatter) : null;
                String notes = line[4];
                String status = line.length > 5 ? line[5] : "PENDING";

                LeaveRequest request = new LeaveRequest(
                        employeeName,
                        leaveType,
                        startDate,
                        endDate,
                        notes,
                        status
                );

                leaveRequests.add(request);
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to load leave requests from CSV: " + e.getMessage(), e);
        }
    }
}