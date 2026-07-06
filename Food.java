import java.awt.*;
import java.util.List;
import java.util.Random;

public class Food {
    public enum FoodType {
        NORMAL, BONUS, SPEED, POISON
    }

    private static final long LIFE_NORMAL_MS = 15_000; // 10s
    private static final long LIFE_BONUS_MS  = 10_000;  // 8s
    private static final long LIFE_SPEED_MS  = 7_000;  // 7s
    private static final long LIFE_POISON_MS = 20_000;  

    private static final long BLINK_THRESHOLD_MS = 2_000; 
    private static final long BLINK_INTERVAL_MS  = 200;   

    private int x, y, tileSize;
    private Color color;
    private FoodType type;
    private Random random;
    private int cols, rows;

    private long spawnTime;   // ms
    private long lifetimeMs;  // ms

    public Food(int cols, int rows, int tileSize) {
        this.cols = cols;
        this.rows = rows;
        this.tileSize = tileSize;
        this.random = new Random();
    }


    // Sửa đổi hàm trong Food.java nhận thêm đối số int[][] map
    public void spawn(Snake snake, int[][] map) {
        boolean validPosition = false;
        int newX = 0, newY = 0;

        while (!validPosition) {
            newX = random.nextInt(cols);
            newY = random.nextInt(rows);
            validPosition = true;

            // 1. Kiểm tra trùng thân rắn
            for (Point p : snake.getBody()) {
                if (p.x == newX && p.y == newY) {
                    validPosition = false;
                    break;
                }
            }

            // 2. Kiểm tra trùng ô block
            if (validPosition &&
                    GameConfig.isObstacle(map[newY][newX])) {
                validPosition = false;
            }
        }

        this.x = newX;
        this.y = newY;

        // ... Giữ nguyên phần tính toán tỷ lệ FoodType ngẫu nhiên ở phía sau



        int rand = random.nextInt(100);
        if (rand < 75)       type = FoodType.NORMAL; // 75%
        else if (rand < 90)  type = FoodType.BONUS;  // 15%
        else if (rand < 97)  type = FoodType.SPEED;  // 7%
        else                 type = FoodType.POISON; // 3%

     
        switch (type) {
            case BONUS  -> color = Color.YELLOW;
            case SPEED  -> color = Color.CYAN;
            case POISON -> color = Color.MAGENTA;
            default     -> color = Color.RED;
        }


        spawnTime = System.currentTimeMillis();
        lifetimeMs = switch (type) {
            case NORMAL -> LIFE_NORMAL_MS;
            case BONUS  -> LIFE_BONUS_MS;
            case SPEED  -> LIFE_SPEED_MS;
            case POISON -> LIFE_POISON_MS;
        };
    }


    public void draw(Graphics g) {
        long now = System.currentTimeMillis();
        long elapsed = now - spawnTime;
        long remain = lifetimeMs - elapsed;

        g.setColor(color);
        g.fillOval(
                x * tileSize,
                y * tileSize,
                tileSize,
                tileSize
        );
    }

    public boolean isEaten(Point snakeHead) {
        return snakeHead.x == x && snakeHead.y == y;
    }


    public boolean isExpired() {
        return System.currentTimeMillis() - spawnTime > lifetimeMs;
    }

    // Getter
    public int getX() { return x; }
    public int getY() { return y; }
    public FoodType getType() { return type; }
}
