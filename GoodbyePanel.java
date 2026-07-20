import javax.swing.*;
import java.awt.*;

public class GoodbyePanel extends JPanel {

    public GoodbyePanel() {

        setBackground(new Color(18,18,24));

        setLayout(new GridBagLayout());

        JLabel label = new JLabel("THANKS FOR PLAYING. GOODBYE!");

        label.setFont(
                new Font(
                        "Consolas",
                        Font.BOLD,
                        48
                )
        );

        label.setForeground(
                new Color(0,255,150)
        );

        add(label);
    }
}