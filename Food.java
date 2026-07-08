import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.util.Random;
import javax.swing.ImageIcon;

public class Food {
    public enum FoodType {
        NORMAL, BONUS, SPEED, POISON
    }

    private static final long LIFE_BONUS_MS  = 10_000;  // 10 giây
    private static final long LIFE_SPEED_MS  = 7_000;   // 7 giây
    private static final long LIFE_POISON_MS = 20_000;  // 20 giây

    private static final Image[] FRUIT_IMAGES = {
        new ImageIcon("orange.png").getImage(),
        new ImageIcon("lemon.png").getImage(),
        new ImageIcon("strawberry.png").getImage(),
        new ImageIcon("grapes.png").getImage(),
        new ImageIcon("grape.png").getImage(),
        new ImageIcon("banana.png").getImage(),
        new ImageIcon("apple.png").getImage(),
        new ImageIcon("melon.png").getImage()
    };
    private static final Image BONUS_IMAGE  = new ImageIcon("giftbox.png").getImage();
    private static final Image SPEED_IMAGE  = new ImageIcon("speed.png").getImage();
    private static final Image POISON_IMAGE = new ImageIcon("poison.png").getImage();

    private int x, y, tileSize;
    private FoodType type;
    private Random random;
    private int cols, rows;

    private Image currentImage; 
    private long spawnTime;   
    private long lifetimeMs;  

    // Constructor
    public Food(int cols, int rows, int tileSize) {
        this.cols = cols;
        this.rows = rows;
        this.tileSize = tileSize;
        this.random = new Random();
    }
    public void spawn(Snake snake, int[][] map, boolean forceNormal) {
        boolean validPosition = false;
        int newX = 0, newY = 0;
        int attempts = 0; // Đếm số lần thử

        // Chỉ cho phép thử tối đa 200 lần để tránh treo game
        while (!validPosition && attempts < 200) {
            attempts++;
            newX = random.nextInt(cols);
            newY = random.nextInt(rows);
            validPosition = true;

            for (Point p : snake.getBody()) {
                if (p.x == newX && p.y == newY) {
                    validPosition = false;
                    break;
                }
            }

            if (validPosition && GameConfig.isObstacle(map[newY][newX])) {
                validPosition = false;
            }
        }

        // Nếu kẹt quá không tìm được chỗ trống, đẩy mồi ra ngoài bản đồ và cho hết hạn luôn
        if (!validPosition) {
            this.x = -100; 
            this.y = -100;
            this.lifetimeMs = 0; 
            this.spawnTime = 0;
            return;
        }

        this.x = newX;
        this.y = newY;

        if (forceNormal) {
            type = FoodType.NORMAL;
        } else {
            int rand = random.nextInt(100);
            if (rand < 50)       type = FoodType.BONUS;  
            else if (rand < 80)  type = FoodType.SPEED;  
            else                 type = FoodType.POISON; 
        }

        switch (type) {
            case NORMAL -> {
                int randomFruitIndex = random.nextInt(FRUIT_IMAGES.length);
                currentImage = FRUIT_IMAGES[randomFruitIndex];
            }
            case BONUS -> {
                currentImage = BONUS_IMAGE;
                lifetimeMs = LIFE_BONUS_MS;
            }
            case SPEED -> {
                currentImage = SPEED_IMAGE;
                lifetimeMs = LIFE_SPEED_MS;
            }
            case POISON -> {
                currentImage = POISON_IMAGE;
                lifetimeMs = LIFE_POISON_MS;
            }
        }

        spawnTime = System.currentTimeMillis();
    }

    public void spawnSpecialNear(Snake snake, int[][] map, Point center, int radius) {
        boolean validPosition = false;
        int newX = 0, newY = 0;
        int attempts = 0; // Đếm số lần thử

        // Chỉ cho phép thử tối đa 100 lần trong khu vực hẹp
        while (!validPosition && attempts < 100) {
            attempts++;
            int minX = Math.max(0, center.x - radius);
            int maxX = Math.min(cols - 1, center.x + radius);
            int minY = Math.max(0, center.y - radius);
            int maxY = Math.min(rows - 1, center.y + radius);

            newX = minX + random.nextInt(maxX - minX + 1);
            newY = minY + random.nextInt(maxY - minY + 1);

            validPosition = true;

            for (Point p : snake.getBody()) {
                if (p.x == newX && p.y == newY) {
                    validPosition = false;
                    break;
                }
            }

            if (validPosition && GameConfig.isObstacle(map[newY][newX])) {
                validPosition = false;
            }
        }

        // Nếu xung quanh đầu rắn bị bao vây kín mít, hủy sinh mồi này
        if (!validPosition) {
            this.x = -100;
            this.y = -100;
            this.lifetimeMs = 0;
            this.spawnTime = 0;
            return;
        }

        this.x = newX;
        this.y = newY;

        int rand = random.nextInt(100);
        if (rand < 50)       type = FoodType.BONUS;  
        else if (rand < 80)  type = FoodType.SPEED;  
        else                 type = FoodType.POISON; 

        switch (type) {
            case BONUS -> {
                currentImage = BONUS_IMAGE;
                lifetimeMs = LIFE_BONUS_MS;
            }
            case SPEED -> {
                currentImage = SPEED_IMAGE;
                lifetimeMs = LIFE_SPEED_MS;
            }
            case POISON -> {
                currentImage = POISON_IMAGE;
                lifetimeMs = LIFE_POISON_MS;
            }
            default -> {}
        }

        spawnTime = System.currentTimeMillis();
    }

    public void draw(Graphics g) {
        g.drawImage(currentImage, x * tileSize, y * tileSize, tileSize, tileSize, null);
    }

    public boolean isEaten(Point snakeHead) {
        return snakeHead.x == x && snakeHead.y == y;
    }

    public boolean isExpired() {
        if (type == FoodType.NORMAL) {
            return false; 
        }
        return System.currentTimeMillis() - spawnTime > lifetimeMs;
    }

    // Getters
    public int getX() { return x; }
    public int getY() { return y; }
    public FoodType getType() { return type; }
}