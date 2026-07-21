import javax.swing.*;
import java.awt.*;

public class VictoryPanel extends JPanel {

    public VictoryPanel(
            GameFrame parent,
            int score,
            String time) {

        ImageIcon logoIcon = new ImageIcon(
                getClass().getResource("/resources/background/snakelogo.png")
        );

        Image scaledImage = logoIcon.getImage().getScaledInstance(
                460, 280,
                Image.SCALE_SMOOTH
        );

        logoIcon = new ImageIcon(scaledImage);

        setLayout(new BorderLayout());
        setBackground(new Color(18,18,24));

        JLabel logoLabel = new JLabel(logoIcon);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel title =
                new JLabel(
                        "LEVEL COMPLETE",
                        SwingConstants.CENTER
                );

        title.setFont(
                new Font(
                        "Consolas",
                        Font.BOLD,
                        40
                )
        );

        title.setForeground(Color.GREEN);

        JLabel info =
                new JLabel(
                        "Score: " + score + " | Time: " + time,
                        SwingConstants.CENTER
                );

        info.setForeground(Color.WHITE);
        info.setFont(new Font("Arial", Font.PLAIN, 20));

        JButton menuBtn =
                new JButton("MAIN MENU");

        menuBtn.addActionListener(
                e -> parent.switchToMenu()
        );

        JPanel bottom = new JPanel();
        bottom.setBackground(getBackground());
        bottom.add(menuBtn);

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(getBackground());
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        info.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(logoLabel);
        centerPanel.add(Box.createVerticalStrut(20));
        centerPanel.add(info);

        add(title, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }
}