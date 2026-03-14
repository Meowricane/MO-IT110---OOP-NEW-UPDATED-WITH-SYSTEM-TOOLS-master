package com.motorph.view.dialog;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;

public class WeeklySummaryReportDialog extends JDialog {

    private JTable reportTable;
    private DefaultTableModel tableModel;

    public WeeklySummaryReportDialog(JFrame parent) {
        super(parent, "Weekly Summary Report", true);
        initializeComponents();
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        String[] columns = {"Emp#", "Name", "Total Hours", "Gross Pay", "Net Pay"};

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reportTable = new JTable(tableModel);
        reportTable.setRowHeight(28);
        reportTable.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        reportTable.setGridColor(new Color(220, 220, 220));
        reportTable.setShowGrid(true);
        reportTable.setFillsViewportHeight(true);
        reportTable.setSelectionBackground(new Color(242, 214, 220));
        reportTable.setSelectionForeground(Color.BLACK);

        JTableHeader header = reportTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setForeground(Color.WHITE);
        header.setBackground(new Color(121, 9, 37));
        header.setOpaque(true);
        header.setPreferredSize(new Dimension(0, 32));
        header.setReorderingAllowed(false);

        UIManager.put("TableHeader.background", new Color(121, 9, 37));
        UIManager.put("TableHeader.foreground", Color.WHITE);
        UIManager.put("TableHeader.font", new Font("Segoe UI", Font.BOLD, 13));

        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(121, 9, 37));
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 13));
        headerRenderer.setHorizontalAlignment(DefaultTableCellRenderer.LEFT);
        headerRenderer.setBorder(BorderFactory.createLineBorder(new Color(150, 150, 150)));
        headerRenderer.setOpaque(true);

        for (int i = 0; i < reportTable.getColumnModel().getColumnCount(); i++) {
            reportTable.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        DefaultTableCellRenderer renderer = new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(
                    JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {

                Component c = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    c.setBackground(new Color(242, 214, 220));
                    c.setForeground(Color.BLACK);
                } else {
                    if (row % 2 == 0) {
                        c.setBackground(Color.WHITE);
                    } else {
                        c.setBackground(new Color(248, 248, 248));
                    }
                    c.setForeground(Color.BLACK);
                }

                setBorder(BorderFactory.createEmptyBorder(4, 8, 4, 8));
                return c;
            }
        };

        for (int i = 0; i < reportTable.getColumnCount(); i++) {
            reportTable.getColumnModel().getColumn(i).setCellRenderer(renderer);
        }

        reportTable.getColumnModel().getColumn(0).setPreferredWidth(70);
        reportTable.getColumnModel().getColumn(1).setPreferredWidth(220);
        reportTable.getColumnModel().getColumn(2).setPreferredWidth(110);
        reportTable.getColumnModel().getColumn(3).setPreferredWidth(110);
        reportTable.getColumnModel().getColumn(4).setPreferredWidth(110);

        JScrollPane scrollPane = new JScrollPane(reportTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(180, 180, 180)));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.WHITE);
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        JButton okButton = new JButton("OK");
        okButton.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        okButton.setBackground(new Color(121, 9, 37));
        okButton.setForeground(Color.WHITE);
        okButton.setFocusPainted(false);
        okButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(okButton);

        add(mainPanel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        setPreferredSize(new Dimension(760, 560));
        pack();
        setLocationRelativeTo(getParent());
    }

    public void addReportRow(String empId, String name, String totalHours, String grossPay, String netPay) {
        tableModel.addRow(new Object[]{empId, name, totalHours, grossPay, netPay});
    }
}