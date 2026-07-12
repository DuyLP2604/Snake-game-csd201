import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class GamePanel extends JPanel implements KeyListener {


    private List<Point> shortestPath = null;
    private int hintsRemaining = GameConfig.MAX_HINTS;
    private boolean showHint = false;

    private String statusMessage = null;
    private Timer statusTimer;

    private java.util.Stack<GameState> history = new java.util.Stack<>();
    private Snake snake;
    private Food foodA;

    private boolean running = false;
    private final GameFrame parent;
    private final ScoreManager scores;
    private final ScoreHistory scoreHistory = new ScoreHistory();
    private MapManager mapManager;
    private javax.swing.Timer gameTimer;
    private int elapsedSeconds = 0;

    public GamePanel(GameFrame parent) {
        this.parent = parent;
        this.scores = new ScoreManager();

        // Khởi tạo ma trận động
        mapManager = new MapManager(parent.getSelectedLevel());


        setPreferredSize(
                new Dimension(
                        GameConfig.COLS * GameConfig.TILE_SIZE,
                        GameConfig.ROWS * GameConfig.TILE_SIZE));

        setBackground(Color.DARK_GRAY);

        setFocusable(true);

        addKeyListener(this);

        requestFocusInWindow();
        startGame();
    }


    public void startGame() {
        Point start = mapManager.getSpawnPoint();

        snake = new Snake(
                start,
                5,
                Snake.RIGHT,
                200
        );
        spawnFoodA();
        scores.resetScore();
        running = true;

        history.clear(); // Xóa sạch lịch sử khi chơi lại
        elapsedSeconds = 0;
        if (gameTimer != null) gameTimer.stop();
        gameTimer = new javax.swing.Timer(1000, e -> {
            if (running) {
                elapsedSeconds++;
                repaint();
            }
        });
        gameTimer.start();

        // Reset Hint
        hintsRemaining = GameConfig.MAX_HINTS;
        showHint = false;
        shortestPath = null;

        requestFocusInWindow();
        repaint();
    }

    private void spawnFoodA() {
        if (foodA == null) {
            foodA = new Food(
                    GameConfig.COLS,
                    GameConfig.ROWS,
                    GameConfig.TILE_SIZE
            );
        }
        // ĐÃ SỬA: Truyền currentMap vào hàm spawn của Food để né sinh mồi đè lên ô tường biên
        foodA.spawn(snake, mapManager.getMap());
    }

    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        // 1. VẼ BẢN ĐỒ (MAP)
        mapManager.draw(g);

        // Draw hint
        if (showHint && shortestPath != null) {

            Graphics2D g2 = (Graphics2D) g;

            Composite oldComposite = g2.getComposite();

            g2.setComposite(
                    AlphaComposite.getInstance(
                            AlphaComposite.SRC_OVER,
                            0.8f
                    )
            );

            // color for the shortest path
            switch (parent.getSelectedLevel()){
                case 1:
                    g2.setColor(new Color(255, 80, 80));
                    break;
                case 2:
                    g2.setColor(new Color(255, 255, 0));
                    break;
                case 3:
                    g2.setColor(new Color(0, 255, 255));
                    break;
            }

            // max steps for hint
            int maxSteps = 60;

            for (int i = 0;
                 i < Math.min(maxSteps, shortestPath.size());
                 i++) {

                Point p = shortestPath.get(i);

                int size = GameConfig.TILE_SIZE / 3;

                g2.fillRect(
                        p.x * GameConfig.TILE_SIZE
                                + GameConfig.TILE_SIZE / 3,
                        p.y * GameConfig.TILE_SIZE
                                + GameConfig.TILE_SIZE / 3,
                        size,
                        size
                );
            }

            g2.setComposite(oldComposite);
        }

        // show notif for no path found because of dead end
        if (statusMessage != null) {

            Graphics2D g2 = (Graphics2D) g;

            String text = statusMessage;

            g2.setFont(new Font("Arial", Font.BOLD, 16));
            FontMetrics fm = g2.getFontMetrics();

            int paddingX = 14;
            int paddingY = 8;

            int textWidth = fm.stringWidth(text);
            int textHeight = fm.getHeight();

            int boxW = textWidth + paddingX * 2;
            int boxH = textHeight + paddingY * 2;

            int x = 170;
            int y = 35;

            g2.setColor(new Color(0, 0, 0, 170));
            g2.fillRoundRect(x, y, boxW, boxH, 16, 16);

            g2.setColor(new Color(255, 255, 255, 60));
            g2.drawRoundRect(x, y, boxW, boxH, 16, 16);

            g2.setColor(new Color(255, 220, 0));
            g2.drawString(
                    text,
                    x + paddingX,
                    y + paddingY + fm.getAscent()
            );
        }

        // 2. VẼ RẮN VÀ THỨC ĂN ĐÈ LÊN TRÊN MAP
        if (snake != null) {
            snake.draw(g, GameConfig.TILE_SIZE);
        }
        if (foodA != null) {
            foodA.draw(g);
        }

        // 3. VẼ GIAO DIỆN ĐIỂM SỐ VÀ TRẠNG THÁI UNDO
        g.setColor(Color.WHITE);

        g.setFont(new Font("Arial", Font.BOLD, 18));

        g.drawString(
                "Level: " + parent.getSelectedLevel(),
                20,
                30
        );

        g.drawString(
                "Score: " + scores.getCurrentScore(),
                120,
                30
        );

        g.drawString(
                "Hints: " + hintsRemaining + "/" + GameConfig.MAX_HINTS + "(Press H)",
                250,
                30
        );

        g.drawString(
                "Undo: " + history.size() + "/20",
                470,
                30
        );

        g.drawString(
                "Gate: " +
                        (mapManager.isGateOpened()
                                ? "OPEN"
                                : "LOCKED"),
                620,
                30
        );
        int minutes = elapsedSeconds / 60;
        int seconds = elapsedSeconds % 60;
        g.drawString(
                "Time: " + String.format("%02d:%02d", minutes, seconds),
                20,
                55
        );

        // Hiển thị chữ GAME OVER nếu thua cuộc
        if (!running) {
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("GAME OVER", getWidth() / 2 - 90, getHeight() / 2);
            g.setFont(new Font("Arial", Font.PLAIN, 16));
            g.setColor(Color.WHITE);
            g.drawString("Press 'R' to Restart", getWidth() / 2 - 65, getHeight() / 2 + 40);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();

        if (!running) {
            if (k == KeyEvent.VK_R) restartGame();
            return;
        }

        int currentDir = snake.getDirection();
        boolean moved = false;

        // Bấm phím di chuyển theo lượt (Tiến lên)
        if (k == KeyEvent.VK_UP && currentDir != Snake.DOWN) {
            moved = tryMove(Snake.UP);
        }
        else if (k == KeyEvent.VK_DOWN && currentDir != Snake.UP) {
            moved = tryMove(Snake.DOWN);
        }
        else if (k == KeyEvent.VK_LEFT && currentDir != Snake.RIGHT) {
            moved = tryMove(Snake.LEFT);
        }
        else if (k == KeyEvent.VK_RIGHT && currentDir != Snake.LEFT) {
            moved = tryMove(Snake.RIGHT);
        }

        // Nhấn phím ngược hướng di chuyển để kích hoạt UNDO (Lùi lại)
        else if ((k == KeyEvent.VK_UP && currentDir == Snake.DOWN) ||
                (k == KeyEvent.VK_DOWN && currentDir == Snake.UP) ||
                (k == KeyEvent.VK_LEFT && currentDir == Snake.RIGHT) ||
                (k == KeyEvent.VK_RIGHT && currentDir == Snake.LEFT)) {

            if (!history.isEmpty()) {
                GameState prevState = history.pop();
                snake.setBody(prevState.getSnakeBody());
                repaint();
                return;
            }
        }

        if (k == KeyEvent.VK_ESCAPE) {
            if (parent != null) parent.switchToMenu();
        }

        // Show hint
        if (k == KeyEvent.VK_H) {

            if (hintsRemaining > 0) {

                List<Point> path = PathFinder.findPath(
                        snake,
                        foodA,
                        mapManager
                );

                if (path != null && !path.isEmpty()) {

                    shortestPath = path;
                    showHint = true;
                    hintsRemaining--;

                } else {

                    showStatusMessage(
                            "No path available!. Try using Undo to escape dead end"
                    );
                }

                repaint();
            }

            return;
        }

        // Xử lý va chạm và ăn mồi sau khi tiến bước
        if (moved) {

            if (snake.checkSelfCollision()) {
                running = false;
                gameTimer.stop();
            }

            if(foodA != null && foodA.isEaten(snake.getHead())){

                scores.increaseScore(1);

                mapManager.updateGate(scores.getCurrentScore());

                spawnFoodA();

            }
            if(mapManager.isGateOpened()){

                Point head = snake.getHead();

                if (mapManager.isGate(head.x, head.y)) {
                    running = false;
                    gameTimer.stop();
                    JOptionPane.showMessageDialog(
                            this,
                            "Level Complete! Score: " + scores.getCurrentScore() +
                                    " - Time: " + String.format("%02d:%02d", elapsedSeconds / 60, elapsedSeconds % 60)
                    );
                }

            }

            repaint();
        }
    }

    private void saveCurrentState() {
        Point posA = foodA != null ? new Point(foodA.getX(), foodA.getY()) : null;
        GameState state = new GameState(snake.getBody(), scores.getCurrentScore(), posA, null);
        history.push(state);

        if (history.size() > 20) {
            history.remove(0);
        }
    }

    private void restartGame() {
        mapManager.reset(parent.getSelectedLevel());
        startGame();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    private void showStatusMessage(String msg) {

        statusMessage = msg;

        if (statusTimer != null) {
            statusTimer.stop();
        }

        statusTimer = new Timer(5000, e -> {
            statusMessage = null;
            repaint();
        });

        statusTimer.setRepeats(false);
        statusTimer.start();

        repaint();
    }

    // check if the snake moves
    private boolean tryMove(int newDirection) {

        int oldDirection = snake.getDirection();

        saveCurrentState();

        snake.setDirectionDirect(newDirection);

        boolean moved = snake.move(
                foodA,
                mapManager.getMap(),
                GameConfig.COLS,
                GameConfig.ROWS
        );

        if (!moved) {
            snake.setDirectionDirect(oldDirection);
            history.pop();
        }

        showHint = false;
        return moved;
    }

}