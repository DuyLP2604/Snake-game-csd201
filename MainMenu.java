import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel implements ActionListener {
    private JButton startButton, helpButton, exitButton;
    private GameFrame parentFrame;

    public MainMenu(GameFrame frame) {
        this.parentFrame = frame;
        setLayout(new GridBagLayout());
        setBackground(Color.BLACK);

        // Tạo các nút
        startButton = createButton("Start Game");
        helpButton = createButton("Help");
        exitButton = createButton("Exit");

        // Gắn sự kiện
        startButton.addActionListener(this);
        helpButton.addActionListener(this);
        exitButton.addActionListener(this);

        // Thêm nút vào panel theo layout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 0, 15, 0);
        gbc.gridx = 0;

        gbc.gridy = 0;
        add(startButton, gbc);

        gbc.gridy = 1;
        add(helpButton, gbc);

        gbc.gridy = 2;
        add(exitButton, gbc);
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(200, 50));
        button.setFont(new Font("Arial", Font.BOLD, 18));
        button.setFocusPainted(false);
        return button;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == startButton) {
            parentFrame.switchToGamePanel();
        } else if (e.getSource() == helpButton) {
            JOptionPane.showMessageDialog(this,
                    "Use arrow keys to move the snake.\nEat food to grow.\nAvoid walls and yourself!",
                    "How to Play", JOptionPane.INFORMATION_MESSAGE);
        } else if (e.getSource() == exitButton) {
            System.exit(0);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.GREEN);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        g.drawString("SNAKE GAME", 240, 120);
    }
}
