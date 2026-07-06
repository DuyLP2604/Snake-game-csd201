import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel implements ActionListener {
    private JButton startButton, helpButton, exitButton;
    private GameFrame parentFrame;

    // Bảng màu thiết kế tối giản Neon phẳng
    private final Color COLOR_BG = new Color(18, 18, 24);
    private final Color COLOR_TEXT_GREEN = new Color(0, 255, 150);
    private final Color COLOR_BTN_BG = new Color(30, 30, 45);
    private final Color COLOR_BTN_TXT = new Color(220, 220, 240);

    public MainMenu(GameFrame frame) {
        this.parentFrame = frame;

        setLayout(new GridBagLayout());
        setBackground(COLOR_BG);

        // Khởi tạo các nút bấm
        startButton = createModernButton("START GAME");
        helpButton = createModernButton("HOW TO PLAY");
        exitButton = createModernButton("EXIT GAME");

        startButton.setBorder(new LineBorder(COLOR_TEXT_GREEN, 2));
        startButton.setForeground(COLOR_TEXT_GREEN);

        // Gắn sự kiện lắng nghe
        startButton.addActionListener(this);
        helpButton.addActionListener(this);
        exitButton.addActionListener(this);

        // Quy hoạch GridBagLayout độc lập theo từng dòng (Tuyệt đối không đè nút)
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Ép các nút có độ rộng đồng đều nhau

        // Hàng 1: Nút Start Game
        gbc.gridy = 0;
        gbc.insets = new Insets(80, 0, 5, 0);
        add(startButton, gbc);

        // Hàng 2: Nút Help
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 5, 0);
        add(helpButton, gbc);

        // Hàng 3: Nút Exit
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 0, 5, 0);
        add(exitButton, gbc);
    }

    private JButton createModernButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(240, 48));
        button.setFont(new Font("Consolas", Font.BOLD, 16));
        button.setBackground(COLOR_BTN_BG);
        button.setForeground(COLOR_BTN_TXT);
        button.setBorder(new LineBorder(new Color(60, 60, 80), 1));
        button.setFocusPainted(false);
        button.setContentAreaFilled(true);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button != startButton) button.setBorder(new LineBorder(Color.WHITE, 1));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                if (button != startButton) button.setBorder(new LineBorder(new Color(60, 60, 80), 1));
            }
        });
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            parentFrame.switchToLevelSelect();
        } else if (e.getSource() == helpButton) {
            UIManager.put("OptionPane.background", COLOR_BG);
            UIManager.put("Panel.background", COLOR_BG);
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            JOptionPane.showMessageDialog(this,
                    "• Use ARROW KEYS to navigate the snake.\n" +
                            "• Eat food on the field to grow up.\n" +
                            "• Do NOT smash into walls or yourself!",
                    "SYSTEM GUIDE", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setColor(COLOR_TEXT_GREEN);
        g2d.setFont(new Font("Consolas", Font.BOLD, 54));

        FontMetrics metrics = g2d.getFontMetrics();
        int x = (getWidth() - metrics.stringWidth("SNAKE GAME")) / 2;
        int y = 90; // Tiêu đề nằm cố định ở cao độ 90px không bị nhảy lung tung

        g2d.drawString("SNAKE GAME", x, y);
    }
}