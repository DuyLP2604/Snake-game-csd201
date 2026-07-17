import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;


public class InstructionDialog extends JDialog {

    private final GameFrame parent;

    public InstructionDialog(GameFrame parent) {
        super(parent, "Rules", true);
        this.parent = parent;

        setSize(600,500);
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

        content.add(createRow(Color.RED, false, "Normal Food : +1 Score"));
        content.add(createRow(Color.YELLOW, false, "Bonus Food : +2 Score"));
        content.add(createRow(Color.CYAN, false, "Speed Food : +1 Score & Permanently Increase Speed"));
        content.add(createRow(Color.MAGENTA, false, "Poison Food"));
        content.add(createRow(Color.YELLOW, true, "Gate : Opens at 20 Points"));


        JLabel note = new JLabel(
                "<html><b>NOTE</b><br>" +
                        "• Reach 20 points to unlock the Gate.<br>" +
                        "• Enter the Gate to complete the level.</html>");

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

    private JPanel createRow(Color color, boolean isGate, String text) {

        JPanel row = new JPanel(new FlowLayout(FlowLayout.LEFT));
        row.setBackground(new Color(20, 20, 35));

        JPanel icon = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(color);

                if (isGate) {
                    g2.fillRect(4, 4, 20, 20);

                    g2.setColor(Color.WHITE);
                    g2.drawRect(4, 4, 20, 20);
                } else {
                    g2.fillOval(4, 4, 20, 20);

                    g2.setColor(Color.WHITE);
                    g2.drawOval(4, 4, 20, 20);
                }
            }
        };

        icon.setPreferredSize(new Dimension(28, 28));
        icon.setOpaque(false);

        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Arial", Font.PLAIN, 18));

        row.add(icon);
        row.add(Box.createHorizontalStrut(15));
        row.add(label);

        return row;
    }
}
