import javax.swing.*;
import java.awt.*;

public class IntroPanel extends JPanel {

    public IntroPanel(GameFrame parent) {

        setBackground(new Color(18,18,24));
        setLayout(new BorderLayout());

        ImageIcon logo = new ImageIcon(
                getClass().getResource(
                        "/resources/background/snakelogo.png"
                )
        );

        Image scaledImage = logo.getImage().getScaledInstance(
                460, 280,
                Image.SCALE_SMOOTH
        );

        logo = new ImageIcon(scaledImage);

        JLabel logoLabel = new JLabel(logo);
        logoLabel.setHorizontalAlignment(SwingConstants.CENTER);

        JLabel title = new JLabel(
                "SNAKE GAME",
                SwingConstants.CENTER
        );

        title.setFont(
                new Font(
                        "Consolas",
                        Font.BOLD,
                        52
                )
        );

        title.setForeground(Color.GREEN);

        JLabel group = new JLabel(
                "Created By Group 2",
                SwingConstants.CENTER
        );

        group.setForeground(Color.WHITE);
        group.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        22
                )
        );

        JPanel centerPanel = new JPanel();
        centerPanel.setBackground(getBackground());
        centerPanel.setLayout(new BoxLayout(
                centerPanel,
                BoxLayout.Y_AXIS
        ));

        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        group.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(Box.createVerticalGlue());

        centerPanel.add(logoLabel);

        centerPanel.add(Box.createVerticalStrut(15));

        centerPanel.add(title);

        centerPanel.add(Box.createVerticalStrut(10));

        centerPanel.add(group);

        centerPanel.add(Box.createVerticalStrut(30));

        JLabel loading = new JLabel("Loading...");
        loading.setForeground(Color.GRAY);
        loading.setFont(new Font("Consolas", Font.ITALIC, 16));
        loading.setAlignmentX(Component.CENTER_ALIGNMENT);

        centerPanel.add(loading);

        centerPanel.add(Box.createVerticalGlue());

        add(centerPanel);

        Timer timer = new Timer(
                2500,
                e -> parent.switchToMenu()
        );

        timer.setRepeats(false);
        timer.start();
    }
}