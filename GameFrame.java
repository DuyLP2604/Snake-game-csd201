import javax.swing.*;

public class GameFrame extends JFrame {
    private MainMenu menuPanel;
    private GamePanel gamePanel;
    private int selectedLevel = 1;

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
    public void setSelectedLevel(int level) {
        this.selectedLevel = level;
    }

    public int getSelectedLevel() {
        return this.selectedLevel;
    }

    // Hàm chuyển sang GamePanel
    public void switchToGamePanel() {
        gamePanel = new GamePanel(this); // GamePanel sẽ gọi getSelectedLevel() từ Frame này
        setContentPane(gamePanel);
        revalidate();
        gamePanel.requestFocusInWindow();
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
