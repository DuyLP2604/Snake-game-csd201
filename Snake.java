import java.awt.Graphics;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public class Snake {

    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;
    
    // Load 5 file ảnh của bạn vào game
    private ImageIcon rightmouth = new ImageIcon("resources/rightmouth.png");
    private ImageIcon leftmouth  = new ImageIcon("resources/leftmouth.png");
    private ImageIcon upmouth    = new ImageIcon("resources/upmouth.png");
    private ImageIcon downmouth  = new ImageIcon("resources/downmouth.png");
    private ImageIcon snakeimage = new ImageIcon("resources/snakeimage.png");
    
    private List<Point> body;
    private int direction;
    private int speed;          
    private int normalSpeed;   
    private boolean alive;

    private final int BASE_SPEED = 200;
    private final int SPEED_STEP = 5; 
    private final int MIN_SPEED = 50;

    private int pendingGrowth = 0;  
    private static final int SPEED_PERM_DELTA_MS = 30;

    // Constructor
    public Snake(Point startPosition, int initialLength, int startDirection, int initialSpeed) {
        body = new ArrayList<>();
        
        // Khởi tạo rắn với length = 9, tính luôn head
        int fixedLength = 9; 
        for (int i = 0; i < fixedLength; i++) {
            body.add(new Point(startPosition.x - i, startPosition.y));
        }
        
        direction = startDirection;
        speed = initialSpeed;
        normalSpeed = initialSpeed;
        alive = true;
    }

    public List<Point> getBody() { return body; }
    public int getSpeed() { return speed; }
    public int getLength() { return body.size(); }
    public boolean isAlive() { return alive; }
    public Point getHead() { return body.get(0); }

    public boolean move(List<Food> foods, int[][] map, int width, int height) {
        if (!alive) return false;

        Point head = body.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case UP    -> newHead.y--;
            case RIGHT -> newHead.x++;
            case DOWN  -> newHead.y++;
            case LEFT  -> newHead.x--;
        }

        if (newHead.x < 0 || newHead.y < 0 || newHead.x >= width || newHead.y >= height) {
            return false;
        }

        if (GameConfig.isObstacle(map[newHead.y][newHead.x])) {
            return false;
        }

        body.add(0, newHead);

        // Duyệt ngược danh sách mồi để kiểm tra va chạm và xóa đi nếu bị ăn
        for (int i = foods.size() - 1; i >= 0; i--) {
            if (eat(foods.get(i))) {
                foods.remove(i); // Ăn trúng thì xóa quả đó khỏi map
                break; // Chỉ ăn 1 quả trong 1 bước di chuyển
            }
        }

        if (pendingGrowth > 0) {
            pendingGrowth--;
        } else {
            body.remove(body.size() - 1);
        }

        return true;
    }

    public void grow() {
        pendingGrowth += 0; 
    }

    private void increaseSpeed() {
        speed = Math.max(MIN_SPEED, BASE_SPEED - (getLength() - 3) * SPEED_STEP);
        normalSpeed = speed; 
    }

    private void applyPermanentSpeedBoost() {
        normalSpeed = Math.max(MIN_SPEED, normalSpeed - SPEED_PERM_DELTA_MS);
        speed = normalSpeed; 
    }

    public boolean checkSelfCollision() {
        Point head = body.get(0);
        for (int i = 1; i < body.size(); i++) {
            if (head.equals(body.get(i))) {
                return true;
            }
        }
        return false;
    }

    public boolean checkWallCollision(int[][] map, int width, int height) {
        Point head = body.get(0);
        if (head.x < 0 || head.y < 0 || head.x >= width || head.y >= height) {
            return true;
        }
        return GameConfig.OBSTACLE_TILES.contains(map[head.y][head.x]);
    }

    // Draw the snake on the game panel
    public void draw(Graphics g, int cellSize) {
        for (int i = 0; i < body.size(); i++) {
            Point p = body.get(i);
            int x = p.x * cellSize;
            int y = p.y * cellSize;

            if (i == 0) {
                switch (direction) {
                    case UP    -> g.drawImage(upmouth.getImage(), x, y, cellSize, cellSize, null);
                    case DOWN  -> g.drawImage(downmouth.getImage(), x, y, cellSize, cellSize, null);
                    case LEFT  -> g.drawImage(leftmouth.getImage(), x, y, cellSize, cellSize, null);
                    case RIGHT -> g.drawImage(rightmouth.getImage(), x, y, cellSize, cellSize, null);
                }
            } else {
                g.drawImage(snakeimage.getImage(), x, y, cellSize, cellSize, null);
            }
        }
    }

    public void setDirection(int dir) {
        if ((direction == UP && dir != DOWN) ||
            (direction == DOWN && dir != UP) ||
            (direction == LEFT && dir != RIGHT) ||
            (direction == RIGHT && dir != LEFT)) {
            direction = dir;
        }
    }

    public int getDirection() { return this.direction; }

    public boolean eat(Food food) {
        if (food == null) return false;

        if (getHead().x == food.getX() && getHead().y == food.getY()) {
            Food.FoodType t = food.getType();
            switch (t) {
                // Để += 0 ở tất cả các loại mồi để đảm bảo rắn tuyệt đối không dài ra
                case NORMAL -> pendingGrowth += 0; 
                case BONUS  -> pendingGrowth += 0;
                case SPEED  -> applyPermanentSpeedBoost();
                case POISON -> alive = false;         
            }
            return true;
        }
        return false;
    }

    public void setDirectionDirect(int dir) { this.direction = dir; }

    public void setBody(List<Point> newBody) {
        this.body = new ArrayList<>();
        for (Point p : newBody) {
            this.body.add(new Point(p.x, p.y));
        }
    }
}