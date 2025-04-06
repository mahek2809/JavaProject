import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class TimeTableNotifier extends JFrame {
    private JTable timetableTable;
    private DefaultTableModel tableModel;
    private JTextField courseField, dayField, timeField;
    private JButton addButton, removeButton;
    private JLabel notificationLabel;
    private Timer notificationTimer;

    public TimeTableNotifier() {
        setTitle("Timetable Notifier");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(248, 249, 250));

        // Create header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(248, 249, 250));
        JLabel titleLabel = new JLabel("Timetable Manager");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 150, 243));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create input panel
        JPanel inputPanel = createInputPanel();
        mainPanel.add(inputPanel, BorderLayout.NORTH);

        // Create table
        createTable();
        mainPanel.add(new JScrollPane(timetableTable), BorderLayout.CENTER);

        // Create notification panel
        JPanel notificationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        notificationPanel.setBackground(new Color(248, 249, 250));
        notificationLabel = new JLabel("No upcoming classes");
        notificationLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        notificationPanel.add(notificationLabel);
        mainPanel.add(notificationPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Start notification timer
        startNotificationTimer();
    }

    public void setVisible(boolean visible) {
        super.setVisible(visible);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        panel.setBackground(new Color(248, 249, 250));

        courseField = new JTextField(15);
        dayField = new JTextField(10);
        timeField = new JTextField(10);

        addButton = createStyledButton("Add Class", new Color(33, 150, 243));
        removeButton = createStyledButton("Remove Class", new Color(244, 67, 54));

        addButton.addActionListener(e -> addClass());
        removeButton.addActionListener(e -> removeClass());

        panel.add(new JLabel("Course:"));
        panel.add(courseField);
        panel.add(new JLabel("Day:"));
        panel.add(dayField);
        panel.add(new JLabel("Time:"));
        panel.add(timeField);
        panel.add(addButton);
        panel.add(removeButton);

        return panel;
    }

    private void createTable() {
        String[] columns = {"Course", "Day", "Time"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        timetableTable = new JTable(tableModel);
        timetableTable.setRowHeight(35);
        timetableTable.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        timetableTable.setShowGrid(true);
        timetableTable.setGridColor(new Color(224, 224, 224));
        timetableTable.setSelectionBackground(new Color(187, 222, 251));
        timetableTable.setSelectionForeground(Color.BLACK);

        JTableHeader header = timetableTable.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(33, 150, 243));
        header.setForeground(Color.WHITE);
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

    private void addClass() {
        String course = courseField.getText().trim();
        String day = dayField.getText().trim();
        String time = timeField.getText().trim();

        if (course.isEmpty() || day.isEmpty() || time.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Please fill in all fields",
                "Input Error",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        tableModel.addRow(new Object[]{course, day, time});
        courseField.setText("");
        dayField.setText("");
        timeField.setText("");
    }

    private void removeClass() {
        int selectedRow = timetableTable.getSelectedRow();
        if (selectedRow >= 0) {
            tableModel.removeRow(selectedRow);
        } else {
            JOptionPane.showMessageDialog(this,
                "Please select a class to remove",
                "Selection Error",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void startNotificationTimer() {
        notificationTimer = new Timer(60000, e -> checkUpcomingClasses());
        notificationTimer.start();
    }

    private void checkUpcomingClasses() {
        LocalTime currentTime = LocalTime.now();
        String currentDay = LocalDate.now().getDayOfWeek().toString();
        
        for (int i = 0; i < tableModel.getRowCount(); i++) {
            String day = (String) tableModel.getValueAt(i, 1);
            String time = (String) tableModel.getValueAt(i, 2);
            
            if (day.equalsIgnoreCase(currentDay)) {
                try {
                    LocalTime classTime = LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm"));
                    long minutesUntil = java.time.Duration.between(currentTime, classTime).toMinutes();
                    
                    if (minutesUntil > 0 && minutesUntil <= 15) {
                        String course = (String) tableModel.getValueAt(i, 0);
                        notificationLabel.setText("Upcoming class: " + course + " in " + minutesUntil + " minutes");
                        notificationLabel.setForeground(new Color(244, 67, 54));
                        return;
                    }
                } catch (Exception e) {
                    // Invalid time format
                }
            }
        }
        
        notificationLabel.setText("No upcoming classes");
        notificationLabel.setForeground(Color.BLACK);
    }
}