import javax.swing.*;

public class GameFrame extends JFrame {
    private MainMenu menuPanel;
    private GamePanel gamePanel;

    public GameFrame() {
        setTitle("Snake Game OOP");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setSize(800, 600);
        setLocationRelativeTo(null);

        // Tạo menu đầu tiên
        menuPanel = new MainMenu(this);
        setContentPane(menuPanel);
        setVisible(true);
    }

    // Hàm chuyển sang GamePanel
    public void switchToGamePanel() {
        gamePanel = new GamePanel(this);
        setContentPane(gamePanel);
        revalidate();
        gamePanel.requestFocusInWindow(); // để nhận phím điều khiển
    }

    // Hàm quay lại menu 
    public void switchToMenu() {
        setContentPane(menuPanel);
        revalidate();
        menuPanel.requestFocusInWindow();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameFrame::new);
    }
}
