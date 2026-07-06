import javax.swing.*;
import java.awt.*;

public class LevelSelectPanel extends JPanel {

    private final GameFrame parent;
    private JPanel cardPanel;

    public LevelSelectPanel(GameFrame parent) {

        this.parent = parent;

        ImageIcon forestIcon = new ImageIcon(
                getClass().getResource("resources/forest/level1_preview.png")
        );

        ImageIcon oceanIcon = new ImageIcon(
                getClass().getResource("resources/ocean/level2_preview.png")
        );

        ImageIcon desertIcon = new ImageIcon(
                getClass().getResource("resources/desert/level3_preview.png")
        );

        cardPanel = new JPanel(new GridLayout(1, 3, 20, 0));
        cardPanel.setBackground(Color.DARK_GRAY);
        cardPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        40, 40, 40, 40
                )
        );

        setBackground(Color.DARK_GRAY);
        setLayout(new BorderLayout());

        JLabel title = new JLabel("SELECT LEVEL", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 32));
        title.setForeground(Color.GREEN);
        title.setBorder(
                BorderFactory.createEmptyBorder(
                        30, 0, 20, 0
                )
        );

        add(title, BorderLayout.NORTH);

        LevelCard level1Card = new LevelCard(
                "Forest",
                forestIcon,
                () -> parent.startLevel(1)
        );

        LevelCard level2Card = new LevelCard(
                "Ocean",
                oceanIcon,
                () -> parent.startLevel(2)
        );

        LevelCard level3Card = new LevelCard(
                "Desert",
                desertIcon,
                () -> parent.startLevel(3)
        );

        cardPanel.add(level1Card);
        cardPanel.add(level2Card);
        cardPanel.add(level3Card);

        add(cardPanel, BorderLayout.CENTER);

        JButton backButton = createBackButton(parent);

        JPanel bottomPanel = new JPanel();

        bottomPanel.setBackground(
                Color.DARK_GRAY
        );

        bottomPanel.add(backButton);

        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JButton createBackButton(GameFrame parent) {
        JButton backButton = new JButton("BACK");

        backButton.setPreferredSize(new Dimension(150, 45));
        backButton.setFont(new Font("Consolas", Font.BOLD, 16));

        backButton.setBackground(new Color(30, 30, 45));
        backButton.setForeground(Color.WHITE);

        backButton.setFocusPainted(false);
        backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        backButton.setBorder(
                BorderFactory.createLineBorder(
                        Color.GREEN,
                        2
                )
        );

        backButton.addMouseListener(
                new java.awt.event.MouseAdapter() {

                    @Override
                    public void mouseEntered(
                            java.awt.event.MouseEvent e) {

                        backButton.setForeground(
                                Color.GREEN
                        );
                    }

                    @Override
                    public void mouseExited(
                            java.awt.event.MouseEvent e) {

                        backButton.setForeground(
                                Color.WHITE
                        );
                    }
                }
        );

        backButton.addActionListener(
                e -> parent.switchToMenu()
        );
        return backButton;
    }
}