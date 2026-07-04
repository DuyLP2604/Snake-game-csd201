import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class GamePanel extends JPanel implements KeyListener {



    private java.util.Stack<GameState> history = new java.util.Stack<>();
    private Snake snake;
    private Food foodA;

    private boolean running = false;
    private final GameFrame parent;
    private final ScoreManager scores;

    private MapManager mapManager;

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
                3,
                Snake.RIGHT,
                200
        );
        spawnFoodA();
        scores.resetScore();
        running = true;

        history.clear(); // Xóa sạch lịch sử khi chơi lại

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

        // 2. VẼ RẮN VÀ THỨC ĂN ĐÈ LÊN TRÊN MAP
        if (snake != null) {
            snake.draw(g, GameConfig.TILE_SIZE);
        }
        if (foodA != null) {
            foodA.draw(g);
        }

        // 3. VẼ GIAO DIỆN ĐIỂM SỐ VÀ TRẠNG THÁI UNDO
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Score: " + scores.getCurrentScore(), 25, 30);
        g.drawString(
                "Gate : " +
                        (mapManager.isGateOpened() ?
                                "OPEN" :
                                "LOCKED"),
                25,
                70
        );
        g.drawString("Steps Saved: " + history.size() + "/20", 25, 50);

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
            saveCurrentState();
            snake.setDirectionDirect(Snake.UP);
            snake.move(foodA);
            moved = true;
        } else if (k == KeyEvent.VK_DOWN && currentDir != Snake.UP) {
            saveCurrentState();
            snake.setDirectionDirect(Snake.DOWN);
            snake.move(foodA);
            moved = true;
        } else if (k == KeyEvent.VK_LEFT && currentDir != Snake.RIGHT) {
            saveCurrentState();
            snake.setDirectionDirect(Snake.LEFT);
            snake.move(foodA);
            moved = true;
        } else if (k == KeyEvent.VK_RIGHT && currentDir != Snake.LEFT) {
            saveCurrentState();
            snake.setDirectionDirect(Snake.RIGHT);
            snake.move(foodA);
            moved = true;
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

        // Xử lý va chạm và ăn mồi sau khi tiến bước
        if (moved) {
            if (snake.checkWallCollision(
                    mapManager.getMap(),
                    GameConfig.COLS,
                    GameConfig.ROWS
            ) || snake.checkSelfCollision()) {
                running = false;
            }

            if(foodA != null && foodA.isEaten(snake.getHead())){

                scores.increaseScore(1);

                mapManager.updateGate(scores.getCurrentScore());

                spawnFoodA();

            }
            if(mapManager.isGateOpened()){

                Point head = snake.getHead();

                if(mapManager.isGate(head.x, head.y)){

                    JOptionPane.showMessageDialog(
                            this,
                            "Level Complete!"
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

}