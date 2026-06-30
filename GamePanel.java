import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class GamePanel extends JPanel implements KeyListener {

    public static final int TILE_SIZE = 15;
    public static final int COLS = 51;
    public static final int ROWS = 38;

    private java.util.Stack<GameState> history = new java.util.Stack<>();
    private Snake snake;
    private Food foodA;

    private boolean running = false;
    private final GameFrame parent;
    private final ScoreManager scores;

    private int[][] currentMap;
    private Image wallImage;
    private Image floorImage;

    public GamePanel(GameFrame parent) {
        this.parent = parent;
        this.scores = new ScoreManager();

        // Khởi tạo ma trận động
        this.currentMap = new int[ROWS][COLS];

        // Tải hình ảnh (Nếu chưa có file ảnh, hệ thống sẽ tự vẽ các ô màu xám)
        try {
            wallImage = new ImageIcon("C:\\Users\\TAN LOI\\Downloads\\wall.jpg").getImage();
            floorImage = new ImageIcon("resources/floor.png").getImage();
        } catch (Exception e) {
            System.out.println("Không thể tải ảnh bản đồ, hệ thống sẽ tự dùng màu khối!");
        }

        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        addKeyListener(this);

        initMapData(); // Tạo khung tường bao quanh ma trận
        startGame();
    }

    // Sửa đổi hàm initMapData() trong GamePanel.java
    private void initMapData() {
        int level = parent.getSelectedLevel(); // Lấy số Level từ GameFrame
        int[][] sourceMap;

        // Lựa chọn map nguồn từ cấu hình tương ứng với Level
        if (level == 2) {
            sourceMap = GameConfig.MAP_LEVEL_2;
        } else {
            sourceMap = GameConfig.MAP_LEVEL_1;
        }

        // Sao chép sâu ma trận cấu hình sang ma trận động chạy trong màn chơi
        for (int r = 0; r < ROWS; r++) {
            System.arraycopy(sourceMap[r], 0, this.currentMap[r], 0, COLS);
        }
    }

    public void startGame() {
        Point start = new Point(COLS / 2, ROWS / 2);
        snake = new Snake(start, 3, Snake.RIGHT, 200); // Độ dài ban đầu bằng 3 để dễ thử nghiệm Undo
        spawnFoodA();
        scores.resetScore();
        running = true;

        history.clear(); // Xóa sạch lịch sử khi chơi lại

        requestFocusInWindow();
        repaint();
    }

    private void spawnFoodA() {
        if (foodA == null) {
            foodA = new Food(COLS, ROWS, TILE_SIZE);
        }
        // ĐÃ SỬA: Truyền currentMap vào hàm spawn của Food để né sinh mồi đè lên ô tường biên
        foodA.spawn(snake, currentMap);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 1. VẼ BẢN ĐỒ (MAP)
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                int xPixel = c * TILE_SIZE;
                int yPixel = r * TILE_SIZE;

                // Vẽ nền đất/cỏ nếu có ảnh
                if (floorImage != null) {
                    g.drawImage(floorImage, xPixel, yPixel, TILE_SIZE, TILE_SIZE, this);
                }

                // Nếu ô ma trận là tường (bằng 1)
                if (currentMap[r][c] == 1) {
                    if (wallImage != null) {
                        g.drawImage(wallImage, xPixel, yPixel, TILE_SIZE, TILE_SIZE, this);
                    } else {
                        // Nếu không tìm thấy ảnh, tự động vẽ khối vuông xám có viền đen
                        g.setColor(Color.GRAY);
                        g.fillRect(xPixel, yPixel, TILE_SIZE, TILE_SIZE);
                        g.setColor(Color.BLACK);
                        g.drawRect(xPixel, yPixel, TILE_SIZE, TILE_SIZE);
                    }
                }
            }
        }

        // 2. VẼ RẮN VÀ THỨC ĂN ĐÈ LÊN TRÊN MAP
        if (snake != null) {
            snake.draw(g, TILE_SIZE);
        }
        if (foodA != null) {
            foodA.draw(g);
        }

        // 3. VẼ GIAO DIỆN ĐIỂM SỐ VÀ TRẠNG THÁI UNDO
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 14));
        g.drawString("Score: " + scores.getCurrentScore(), 25, 30);
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
            if (snake.checkWallCollision(currentMap, COLS, ROWS) || snake.checkSelfCollision()) {
                running = false;
            }

            if (foodA != null && foodA.isEaten(snake.getHead())) {
                scores.increaseScore(1);
                spawnFoodA();
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
        startGame();
    }

    @Override public void keyReleased(KeyEvent e) { }
    @Override public void keyTyped(KeyEvent e) { }

}