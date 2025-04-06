import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.border.*;

public class MentalHealthSupport extends JFrame {
    private JTextArea resourcesArea;
    private JButton closeButton;

    public MentalHealthSupport() {
        setTitle("Mental Health Support");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(new Color(248, 249, 250));

        // Header
        JPanel headerPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        headerPanel.setBackground(new Color(248, 249, 250));
        JLabel titleLabel = new JLabel("Mental Health Resources");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.setForeground(new Color(156, 39, 176));
        headerPanel.add(titleLabel);
        mainPanel.add(headerPanel, BorderLayout.NORTH);

        // Resources text area
        resourcesArea = new JTextArea();
        resourcesArea.setEditable(false);
        resourcesArea.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        resourcesArea.setLineWrap(true);
        resourcesArea.setWrapStyleWord(true);
        resourcesArea.setBackground(Color.WHITE);
        resourcesArea.setText(
            "1. University Counseling Services\n" +
            "   - Location: Student Services Building, Room 101\n" +
            "   - Hours: Monday-Friday, 9:00 AM - 5:00 PM\n" +
            "   - Phone: (555) 123-4567\n\n" +
            "2. 24/7 Crisis Hotline\n" +
            "   - Phone: (555) 987-6543\n" +
            "   - Available anytime for immediate support\n\n" +
            "3. Online Resources\n" +
            "   - Mental Health First Aid: www.mhfa.org\n" +
            "   - Student Mental Health: www.studentmentalhealth.org\n\n" +
            "4. Self-Care Tips\n" +
            "   - Take regular breaks\n" +
            "   - Maintain a healthy sleep schedule\n" +
            "   - Stay connected with friends and family\n" +
            "   - Practice mindfulness and meditation\n" +
            "   - Exercise regularly"
        );

        JScrollPane scrollPane = new JScrollPane(resourcesArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // Close button
        closeButton = new JButton("Close");
        closeButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        closeButton.setBackground(new Color(156, 39, 176));
        closeButton.setForeground(Color.WHITE);
        closeButton.setFocusPainted(false);
        closeButton.setBorder(new EmptyBorder(10, 20, 10, 20));
        closeButton.addActionListener(e -> dispose());

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setBackground(new Color(248, 249, 250));
        buttonPanel.add(closeButton);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}