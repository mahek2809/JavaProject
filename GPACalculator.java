import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class GPACalculator extends JFrame {
    private JTable courseTable;
    private DefaultTableModel tableModel;
    private JButton addButton, resetButton;
    private JLabel gpaLabel;
    private JPanel resultPanel;
    private final Color[] GRADE_COLORS = {
        new Color(0, 150, 136),  // A+ - Teal
        new Color(76, 175, 80),  // A - Green
        new Color(139, 195, 74), // A- - Light Green
        new Color(205, 220, 57), // B+ - Lime
        new Color(255, 235, 59), // B - Yellow
        new Color(255, 193, 7),  // B- - Amber
        new Color(255, 152, 0),  // C+ - Orange
        new Color(255, 87, 34),  // C - Deep Orange
        new Color(244, 67, 54),  // C- - Red
        new Color(233, 30, 99),  // D+ - Pink
        new Color(156, 39, 176), // D - Purple
        new Color(121, 85, 72)   // F - Brown
    };

    public GPACalculator() {
        setTitle("GPA Calculator");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(248, 249, 250));

        // Create header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(248, 249, 250));
        JLabel titleLabel = new JLabel("GPA Calculator");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 150, 243));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create table
        createTable();

        // Create buttons panel
        JPanel buttonPanel = createButtonPanel();

        // Create result panel
        resultPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        resultPanel.setBackground(new Color(248, 249, 250));
        gpaLabel = new JLabel("GPA: 0.00");
        gpaLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        gpaLabel.setForeground(new Color(33, 150, 243));
        resultPanel.add(gpaLabel);

        // Add components to main panel
        mainPanel.add(new JScrollPane(courseTable), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        mainPanel.add(resultPanel, BorderLayout.EAST);

        add(mainPanel);
    }

    private void createTable() {
        String[] columns = {"Course Name", "Credits", "Marks", "Grade", "Grade Points"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column <= 2; // Only course name, credits, and marks are editable
            }
        };

        courseTable = new JTable(tableModel);
        courseTable.setRowHeight(35);
        courseTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        courseTable.setShowGrid(true);
        courseTable.setGridColor(new Color(224, 224, 224));
        courseTable.setSelectionBackground(new Color(187, 222, 251));
        courseTable.setSelectionForeground(Color.BLACK);

        // Custom header
        JTableHeader header = courseTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(33, 150, 243));
        header.setForeground(Color.WHITE);

        // Add table model listener to update grades and GPA automatically
        tableModel.addTableModelListener(e -> {
            if (e.getColumn() == 2) { // Marks column changed
                updateGradesAndGPA();
            }
        });

        // Custom cell renderer
        courseTable.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int column) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                
                // Set default background and foreground colors
                if (!isSelected) {
                    c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(245, 245, 245));
                    c.setForeground(Color.BLACK);
                }
                
                // Handle grade column specifically
                if (column == 3 && value != null) { // Grade column
                    String grade = value.toString();
                    int colorIndex = getGradeColorIndex(grade);
                    if (colorIndex >= 0) {
                        c.setBackground(GRADE_COLORS[colorIndex]);
                        c.setForeground(Color.WHITE);
                    }
                }
                
                return c;
            }
        });
    }

    private void updateGradesAndGPA() {
        double totalPoints = 0;
        double totalCredits = 0;

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String creditsStr = String.valueOf(tableModel.getValueAt(i, 1));
            String marksStr = String.valueOf(tableModel.getValueAt(i, 2));

            if (creditsStr != null && !creditsStr.trim().isEmpty() &&
                marksStr != null && !marksStr.trim().isEmpty()) {
                try {
                    double credits = Double.parseDouble(creditsStr);
                    double marks = Double.parseDouble(marksStr);
                    String grade = calculateGrade(marks);
                    double gradePoints = calculateGradePoints(grade);
                    
                    tableModel.setValueAt(grade, i, 3);
                    tableModel.setValueAt(String.format("%.2f", gradePoints), i, 4);
                    
                    totalPoints += (credits * gradePoints);
                    totalCredits += credits;
                } catch (NumberFormatException e) {
                    // Skip invalid entries
                    continue;
                }
            }
        }

        double gpa = totalCredits > 0 ? totalPoints / totalCredits : 0;
        gpaLabel.setText(String.format("GPA: %.2f", gpa));
        
        // Update GPA label color based on value
        if (gpa >= 3.5) {
            gpaLabel.setForeground(new Color(76, 175, 80)); // Green
        } else if (gpa >= 2.5) {
            gpaLabel.setForeground(new Color(255, 152, 0)); // Orange
        } else {
            gpaLabel.setForeground(new Color(244, 67, 54)); // Red
        }
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(248, 249, 250));

        addButton = createStyledButton("Add Course", new Color(33, 150, 243));
        resetButton = createStyledButton("Reset", new Color(244, 67, 54));

        addButton.addActionListener(e -> addNewRow());
        resetButton.addActionListener(e -> resetCalculator());

        panel.add(addButton);
        panel.add(resetButton);

        return panel;
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    private void addNewRow() {
        tableModel.addRow(new Object[]{"", "", "", "", ""});
    }

    private String calculateGrade(double marks) {
        if (marks >= 90) return "A+";
        if (marks >= 85) return "A";
        if (marks >= 80) return "A-";
        if (marks >= 75) return "B+";
        if (marks >= 70) return "B";
        if (marks >= 65) return "B-";
        if (marks >= 60) return "C+";
        if (marks >= 55) return "C";
        if (marks >= 50) return "C-";
        if (marks >= 45) return "D+";
        if (marks >= 40) return "D";
        return "F";
    }

    private double calculateGradePoints(String grade) {
        switch (grade) {
            case "A+": return 4.0;
            case "A": return 4.0;
            case "A-": return 3.7;
            case "B+": return 3.3;
            case "B": return 3.0;
            case "B-": return 2.7;
            case "C+": return 2.3;
            case "C": return 2.0;
            case "C-": return 1.7;
            case "D+": return 1.3;
            case "D": return 1.0;
            default: return 0.0;
        }
    }

    private int getGradeColorIndex(String grade) {
        switch (grade) {
            case "A+": return 0;
            case "A": return 1;
            case "A-": return 2;
            case "B+": return 3;
            case "B": return 4;
            case "B-": return 5;
            case "C+": return 6;
            case "C": return 7;
            case "C-": return 8;
            case "D+": return 9;
            case "D": return 10;
            case "F": return 11;
            default: return -1;
        }
    }

    private void resetCalculator() {
        tableModel.setRowCount(0);
        gpaLabel.setText("GPA: 0.00");
        gpaLabel.setForeground(new Color(33, 150, 243));
    }
}