import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class Main extends JFrame {
    private JButton gpaButton, timetableButton, mentalHealthButton;

    public Main() {
        setTitle("Student Assistant");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Create main panel
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(248, 249, 250));

        // Create header panel
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(248, 249, 250));
        JLabel titleLabel = new JLabel("Student Assistant");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(33, 150, 243));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Create buttons panel
        JPanel buttonsPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        buttonsPanel.setBackground(new Color(248, 249, 250));

        // GPA Calculator Button
        gpaButton = createStyledButton(" GPA Calculator", new Color(33, 150, 243));
        gpaButton.addActionListener(e -> {
            GPACalculator gpaCalculator = new GPACalculator();
            gpaCalculator.setVisible(true);
        });

        // Timetable Manager Button
        timetableButton = createStyledButton(" Timetable Manager", new Color(76, 175, 80));
        timetableButton.addActionListener(e -> {
            TimetableManager timetableManager = new TimetableManager();
            timetableManager.setVisible(true);
        });

        // Mental Health Support Button
        mentalHealthButton = createStyledButton(" Mental Health Support", new Color(156, 39, 176));
        mentalHealthButton.addActionListener(e -> {
            MentalHealthSupport mentalHealthSupport = new MentalHealthSupport();
            mentalHealthSupport.setVisible(true);
        });

        buttonsPanel.add(gpaButton);
        buttonsPanel.add(timetableButton);
        buttonsPanel.add(mentalHealthButton);

        mainPanel.add(buttonsPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(new EmptyBorder(15, 30, 15, 30));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            StudentAssistant app = new StudentAssistant();
            app.setVisible(true);
        });
    }
}