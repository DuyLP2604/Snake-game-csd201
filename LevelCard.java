import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class LevelCard extends JPanel {

    public LevelCard(
            String title,
            ImageIcon icon,
            Runnable onClick) {

        setLayout(new BorderLayout());
        setBackground(new Color(30, 30, 45));
        setBorder(new LineBorder(Color.GRAY, 2));
        setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);

        Image scaled = icon.getImage().getScaledInstance(
                220,
                160,
                Image.SCALE_SMOOTH
        );

        imageLabel.setIcon(new ImageIcon(scaled));

        JLabel titleLabel = new JLabel(
                title,
                SwingConstants.CENTER
        );

        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(
                new Font("Consolas", Font.BOLD, 18)
        );

        add(imageLabel, BorderLayout.CENTER);
        add(titleLabel, BorderLayout.SOUTH);

        MouseAdapter listener = new MouseAdapter() {

            @Override
            public void mouseEntered(MouseEvent e) {
                setBorder(new LineBorder(Color.GREEN, 3));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setBorder(new LineBorder(Color.GRAY, 2));
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                onClick.run();
            }
        };

        addMouseListener(listener);
        imageLabel.addMouseListener(listener);
        titleLabel.addMouseListener(listener);
    }
}