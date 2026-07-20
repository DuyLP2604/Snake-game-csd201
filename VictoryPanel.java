import javax.swing.*;
import java.awt.*;

public class VictoryPanel extends JPanel {

    public VictoryPanel(
            GameFrame parent,
            int score,
            String time) {

        setLayout(new BorderLayout());
        setBackground(new Color(18,18,24));

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

        add(title, BorderLayout.NORTH);
        add(info, BorderLayout.CENTER);
        add(bottom, BorderLayout.SOUTH);
    }
}