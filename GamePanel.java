import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.Timer;

public class GamePanel extends JPanel implements KeyListener {

    private int timeRemaining;
    private Timer gameTimer;
    private Timer countdownTimer;

    private List<Point> shortestPath = null;
    private int hintsRemaining = GameConfig.MAX_HINTS;
    private boolean showHint = false;

    private String statusMessage = null;
    private Timer statusTimer;

    private java.util.Stack<GameState> history = new java.util.Stack<>();
    private Snake snake;
    private List<Food> activeFoods = new ArrayList<>();
    private Timer specialFoodTimer;

    private boolean running = false;
    private final GameFrame parent;
    private final ScoreManager scores;
    private final ScoreHistory scoreHistory = new ScoreHistory();
    private MapManager mapManager;
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
        timeRemaining =
                GameConfig.LEVEL_TIME_LIMIT[
                        parent.getSelectedLevel()
                        ];

        startCountdown();

        snake = new Snake(
                start,
                5,
                Snake.RIGHT,
                200
        );
        activeFoods.clear();
        for (int i = 0; i < 5; i++) {
            Food f = new Food(GameConfig.COLS, GameConfig.ROWS, GameConfig.TILE_SIZE);
            f.spawn(snake, mapManager.getMap(), true, activeFoods); 
            activeFoods.add(f);
        }

        // Timer thi thoảng thả thêm (Bonus, Speed, Bomb) (Mỗi 7.5 giây 1 trái)
        if (specialFoodTimer != null) specialFoodTimer.stop();
        specialFoodTimer = new Timer(7500, e -> spawnSpecialFood(snake.getHead()));
        specialFoodTimer.start();

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

    private void spawnSpecialFood(Point snakeHead) {
        if (!running) return;
        Food f = new Food(GameConfig.COLS, GameConfig.ROWS, GameConfig.TILE_SIZE);
        
        // Gọi hàm mới tạo bên Food.java (bán kính 12 ô xung quanh đầu rắn)
        f.spawnSpecialNear(snake, mapManager.getMap(), snakeHead, 12, activeFoods); 
        activeFoods.add(f);
        repaint();
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
        for (Food f : activeFoods) {
            f.draw(g);
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

        if (timeRemaining <= 10) {

            if (timeRemaining % 2 == 0) {
                g.setColor(Color.RED);
            } else {
                g.setColor(Color.WHITE);
            }

        } else {
            g.setColor(Color.WHITE);
        }

        g.drawString(
                "Time Left: " + timeRemaining + "s",
                625,
                55
        );

        if (!running) {
            // Tô nền tối che màn hình chơi
            g.setColor(new Color(0, 0, 0, 180));
            g.fillRect(0, 0, getWidth(), getHeight());
            
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 36));
            String gameOverText = "GAME OVER";
            int gameOverX = (getWidth() - g2.getFontMetrics().stringWidth(gameOverText)) / 2;
            g2.drawString(gameOverText, gameOverX, getHeight() / 2 - 30);

            g2.setColor(Color.YELLOW);
            g2.setFont(new Font("Arial", Font.BOLD, 20));
            String scoreText = "Your Score: " + scores.getCurrentScore();
            int scoreX = (getWidth() - g2.getFontMetrics().stringWidth(scoreText)) / 2;
            g2.drawString(scoreText, scoreX, getHeight() / 2 + 15);

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Arial", Font.PLAIN, 16));
            String restartText = "Press 'R' to Restart";
            int restartX = (getWidth() - g2.getFontMetrics().stringWidth(restartText)) / 2;
            g2.drawString(restartText, restartX, getHeight() / 2 + 55);
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
                // ĐÃ SỬA: Tìm trái cây gần nhất để dẫn đường
                Food targetFood = null;
                double minDist = Double.MAX_VALUE;
                for (Food f : activeFoods) {
                    if (f.getType() == Food.FoodType.NORMAL || f.getType() == Food.FoodType.BONUS) {
                        double dist = Point.distance(snake.getHead().x, snake.getHead().y, f.getX(), f.getY());
                        if (dist < minDist) {
                            minDist = dist;
                            targetFood = f;
                        }
                    }
                }

                List<Point> path = PathFinder.findPath(snake, targetFood, mapManager);

                if (path != null && !path.isEmpty()) {
                    shortestPath = path;
                    showHint = true;
                    hintsRemaining--;
                } else {
                    showStatusMessage("No path available!. Try using Undo to escape dead end");
                }
                repaint();
            }
            return;
        }
        // Bấm L để hiện điểm số 10 lần chơi gần nhất
        if (k == KeyEvent.VK_L) {
            StringBuilder sb = new StringBuilder("10 điểm gần nhất:\n");
            List<Integer> list = scoreHistory.getRecentScores();
            if (list.isEmpty()) {
                sb.append("Chưa có ván nào kết thúc.");
            } else {
                int i = 1;
                for (int s : list) {
                    sb.append(i++).append(". ").append(s).append(" điểm\n");
                }
            }
            JOptionPane.showMessageDialog(this, sb.toString());
            return;
        }

        // Xử lý sau khi tiến bước
        if (moved) {
            // Kiểm tra tự va chạm hoặc lỡ ăn bom
            if (snake.checkSelfCollision() || !snake.isAlive()) {
                Sound.play("music_gameover.wav");
                running = false;
                gameTimer.stop();
                if (countdownTimer != null) {
                    countdownTimer.stop();
                }
                scoreHistory.addScore(scores.getCurrentScore());
            }

            if(mapManager.isGateOpened()){
                Point head = snake.getHead();

                if (mapManager.isGate(head.x, head.y)) {
                    running = false;
                    gameTimer.stop();
                    if (countdownTimer != null) {
                        countdownTimer.stop();
                    }
                    scoreHistory.addScore(scores.getCurrentScore());
                    parent.showVictoryPanel(
                            scores.getCurrentScore(),
                            String.format(
                                    "%02d:%02d",
                                    elapsedSeconds / 60,
                                    elapsedSeconds % 60
                            )
                    );
                    Sound.play("music_victory.wav");
                }
            }
            repaint();
        }
    }

    private void saveCurrentState() {
        GameState state = new GameState(snake.getBody(), scores.getCurrentScore(), activeFoods);
        history.push(state);

        if (history.size() > 20) {
            history.remove(0);
        }
    }

    private void restartGame() {
        mapManager.reset(parent.getSelectedLevel());
        startGame();
    }

    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}

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

    private boolean tryMove(int newDirection) {
        int oldDirection = snake.getDirection();
        saveCurrentState();
        snake.setDirectionDirect(newDirection);

        activeFoods.removeIf(Food::isExpired);
        int prevFoodCount = activeFoods.size();

        // Di chuyển và kiểm tra xem có ăn mồi trong list activeFoods hay không
        boolean moved = snake.move(activeFoods, mapManager.getMap(), GameConfig.COLS, GameConfig.ROWS);

        if (!moved) {
            Sound.play("music_hit.wav");
            snake.setDirectionDirect(oldDirection);
            history.pop();
        } else {
            // Nếu di chuyển thành công, kiểm tra xem có ăn mồi hay không
            Food eatenFood = snake.getLastEatenFood();

            if (eatenFood != null) {

                switch (eatenFood.getType()) {

                    case NORMAL:
                        scores.increaseScore(1);
                        Sound.play("music_food.wav");
                        break;

                    case BONUS:
                        scores.increaseScore(2);
                        Sound.play("music_bonus.wav");
                        break;

                    case SPEED:
                        scores.increaseScore(1);
                        Sound.play("music_speed.wav");
                        break;

                    case POISON:
                        break;
                }

                activeFoods.remove(eatenFood);

                mapManager.updateGate(scores.getCurrentScore());

                Food newFood = new Food(
                        GameConfig.COLS,
                        GameConfig.ROWS,
                        GameConfig.TILE_SIZE
                );

                newFood.spawn(
                        snake,
                        mapManager.getMap(),
                        true,
                        activeFoods
                );

                activeFoods.add(newFood);
            }
        }

        showHint = false;
        return moved;
    }

    private void startCountdown() {

        if (countdownTimer != null) {
            countdownTimer.stop();
        }

        countdownTimer = new Timer(
                1000,
                e -> {

                    timeRemaining--;

                    if (timeRemaining <= 0) {

                        running = false;

                        gameTimer.stop();

                        if (countdownTimer != null) {
                            countdownTimer.stop();
                        }

                        Sound.play("music_gameover.wav");

                        scoreHistory.addScore(scores.getCurrentScore());

                        repaint();
                    }

                    repaint();
                }
        );

        countdownTimer.start();
    }
}