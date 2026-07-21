import javax.swing.*;

public class GameFrame extends JFrame {
    private MainMenu menuPanel;
    private GamePanel gamePanel;
    private int selectedLevel = 1;

    private LevelSelectPanel levelSelectPanel;

    public GameFrame() {
        setTitle("Snake Game CSD201");
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
        Sound.stopBackground();
        setContentPane(menuPanel);
        revalidate();
        menuPanel.requestFocusInWindow();
    }

    public void switchToLevelSelect() {

        levelSelectPanel = new LevelSelectPanel(this);

        setContentPane(levelSelectPanel);

        revalidate();
        repaint();

        levelSelectPanel.requestFocusInWindow();
    }

    public void startLevel(int level) {

        setSelectedLevel(level);

        //switchToGamePanel();
        new InstructionDialog(this, selectedLevel).setVisible(true);
    }

    public void showGoodbyeScreen() {

        setContentPane(
                new GoodbyePanel()
        );

        revalidate();
        repaint();

        javax.swing.Timer timer =
                new javax.swing.Timer(
                        1000,
                        e -> System.exit(0)
                );

        timer.setRepeats(false);
        timer.start();
    }

    public void showVictoryPanel(
            int score,
            String time) {

        setContentPane(
                new VictoryPanel(
                        this,
                        score,
                        time
                )
        );

        revalidate();
        repaint();
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(GameFrame::new);
    }

}
