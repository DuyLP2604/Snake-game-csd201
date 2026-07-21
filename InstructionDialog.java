import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class InstructionDialog extends JDialog {

    private final GameFrame parent;

    public InstructionDialog(GameFrame parent, int level) {
        super(parent, "Rules", true);
        int gateScore = GameConfig.GATE_SCORES[level];
        this.parent = parent;

        ImageIcon normalFoodIcon = new ImageIcon(

                getClass().getResource("resources/fruit/apple.png")
        );

        ImageIcon bonusFoodIcon = new ImageIcon(
                getClass().getResource("resources/fruit/giftbox.png")
        );

        ImageIcon speedFoodIcon = new ImageIcon(
                getClass().getResource("resources/fruit/speed.png")
        );

        ImageIcon poisonFoodIcon = new ImageIcon(
                getClass().getResource("resources/fruit/poison.png")
        );

        ImageIcon gateIcon = new ImageIcon(
                getClass().getResource("resources/fruit/gate_closed.png")
        );

        ImageIcon timerIcon = new ImageIcon(
                getClass().getResource("resources/timer.png")
        );

        setSize(600,570);
        setLocationRelativeTo(parent);
        setResizable(false);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(new Color(25,25,35));
        panel.setBorder(new EmptyBorder(20,20,20,20));

        JLabel title = new JLabel("RULE", SwingConstants.CENTER);

        title.setFont(new Font("Consolas", Font.BOLD, 28));
        title.setForeground(Color.GREEN);

        panel.add(title, BorderLayout.NORTH);

        JPanel content = new JPanel();
        content.setBackground(new Color(25,25,35));
        content.setLayout(new GridLayout(8,1,5,5));

        Font font = new Font("Arial", Font.PLAIN, 16);

        content.add(createRow(
                normalFoodIcon,
                "Normal Food : +1 Score"
        ));

        content.add(createRow(
                bonusFoodIcon,
                "Bonus Food : +2 Score"
        ));

        content.add(createRow(
                speedFoodIcon,
                "Speed Food : +1 Score & Permanently Increase Speed"
        ));

        content.add(createRow(
                poisonFoodIcon,
                "Poison Food : Instant Death"
        ));

        content.add(createRow(
                gateIcon,
                "Gate : Opens at " + gateScore + " Points"
        ));
        content.add(createRow(timerIcon,
                "Time Limit : " + GameConfig.LEVEL_TIME_LIMIT[level] + " Seconds"
        ));


        JLabel note = new JLabel(
                "<html><b>NOTE</b><br>" +
                        "• Reach " + gateScore + " points to unlock the Gate.<br>" +
                        "• Enter the Gate to complete the level.<br>" +
                        "• Complete the level before time runs out.</html>"
        );

        note.setForeground(Color.WHITE);
        note.setFont(new Font("Arial", Font.PLAIN, 10));

        content.add(note);

        panel.add(content, BorderLayout.CENTER);

        JButton start = new JButton("START GAME");

        start.setPreferredSize(new Dimension(180,45));
        start.setFont(new Font("Consolas",Font.BOLD,18));
        start.setBackground(new Color(50,50,70));
        start.setForeground(Color.GREEN);
        start.setFocusPainted(false);

        start.addActionListener(e -> {

            dispose();

            parent.switchToGamePanel();

        });

        JPanel bottom = new JPanel();
        bottom.setBackground(new Color(30,30,45));
        bottom.add(start);

        panel.add(bottom, BorderLayout.SOUTH);

        setContentPane(panel);
    }

    private JPanel createRow(ImageIcon image, String text) {

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setBackground(new Color(20, 20, 35));

        Image scaledImage = image.getImage().getScaledInstance(
                32,
                32,
                Image.SCALE_SMOOTH
        );

        JLabel icon = new JLabel(
                new ImageIcon(scaledImage)
        );

        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 18));

        row.add(icon);
        row.add(Box.createHorizontalStrut(15));
        row.add(label);

        return row;
    }
}
