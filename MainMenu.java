import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class MainMenu extends JPanel implements ActionListener {
    private JButton startButton, aboutButton, historyButton, exitButton;
    private GameFrame parentFrame;

    // Bảng màu thiết kế tối giản Neon phẳng
    private final Color COLOR_BG = new Color(18, 18, 24);
    private final Color COLOR_TEXT_GREEN = new Color(0, 255, 150);
    private final Color COLOR_BTN_BG = new Color(30, 30, 45);
    private final Color COLOR_BTN_TXT = new Color(220, 220, 240);

    // Khai báo biến lưu trữ hình ảnh
    private Image backgroundImage;
    private Image logoImage;

    public MainMenu(GameFrame frame) {
        this.parentFrame = frame;

        // Tải hình ảnh (Yêu cầu 2 file này nằm cùng thư mục gốc của project)
        backgroundImage = new ImageIcon("resources/background/background.jpg").getImage();
        logoImage = new ImageIcon("resources/background/snakelogo.png").getImage();

        setLayout(new GridBagLayout());
        
        // Khởi tạo các nút bấm
        startButton = createModernButton("START GAME");
        aboutButton = createModernButton("About");
        historyButton = createModernButton("History");
        exitButton = createModernButton("EXIT GAME");

        startButton.setBorder(new LineBorder(COLOR_TEXT_GREEN, 2));
        startButton.setForeground(COLOR_TEXT_GREEN);

        startButton.addActionListener(this);
        aboutButton.addActionListener(this);
        historyButton.addActionListener(this);          
        exitButton.addActionListener(this);

        // Quy hoạch GridBagLayout độc lập theo từng dòng
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL; // Ép các nút có độ rộng đồng đều nhau

        // Nút START GAME 
        gbc.gridy = 0;
        gbc.insets = new Insets(250, 0, 5, 0);
        add(startButton, gbc);

        // Nút About
        gbc.gridy = 1;
        gbc.insets = new Insets(5, 0, 5, 0);
        add(aboutButton, gbc);

        // Nút History
        gbc.gridy = 2;
        gbc.insets = new Insets(5, 0, 5, 0);
        add(historyButton, gbc);
        
        // Nút Exit
        gbc.gridy = 3;
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
        }else if (e.getSource() == aboutButton) {
            new AboutDialog(parentFrame).setVisible(true);
        } else if (e.getSource() == historyButton) {
            ScoreHistory scoreHistory = new ScoreHistory();
            java.util.List<Integer> list = scoreHistory.getRecentScores();
            StringBuilder sb = new StringBuilder("TOP 10 Kỷ lục gần nhất:\n\n");
            if (list.isEmpty()) {
                sb.append("Chưa có ván nào kết thúc.");
            } else {
                int i = 1;
                for (int s : list) {
                    sb.append("Ván ").append(i++).append(": ").append(s).append(" điểm\n");
                }
            }
            JOptionPane.showMessageDialog(this, sb.toString(), "Lịch sử điểm số", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == exitButton) {
            parentFrame.showGoodbyeScreen();
        }
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 1. Vẽ Background thay vì tô màu trơn
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        } else { // Fallback nếu không tìm thấy ảnh
            g2d.setColor(COLOR_BG);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }

        // 
        if (logoImage != null) {
            int logoWidth = 750;
            int logoHeight = 520;
            int x = (getWidth() - logoWidth) / 2;
            int y = -70; // Cách đỉnh màn hình 40px
            g2d.drawImage(logoImage, x, y, logoWidth, logoHeight, this);
        }
    }
}