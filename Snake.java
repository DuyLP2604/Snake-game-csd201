import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Snake {

    public static final int UP = 0;
    public static final int RIGHT = 1;
    public static final int DOWN = 2;
    public static final int LEFT = 3;

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
        for (int i = 0; i < initialLength; i++) {
            body.add(new Point(startPosition.x - i, startPosition.y)); // initial body
        }
        direction = startDirection;
        speed = initialSpeed;
        normalSpeed = initialSpeed;  // baseline ban đầu
        alive = true;
    }

    // Getters
    public List<Point> getBody() { return body; }
    public int getSpeed() { return speed; }
    public int getLength() { return body.size(); }
    public boolean isAlive() { return alive; }
    public Point getHead() { return body.get(0); }

    // Tick di chuyển
    public void move(Food food) {
        if (!alive) return;

        Point head = body.get(0);
        Point newHead = new Point(head);

        switch (direction) {
            case UP:    newHead.y -= 1; break;
            case RIGHT: newHead.x += 1; break;
            case DOWN:  newHead.y += 1; break;
            case LEFT:  newHead.x -= 1; break;
        }

 
        body.add(0, newHead);

  
        boolean ate = eat(food);

        // tăng đốt mượt
        if (pendingGrowth > 0) {
            pendingGrowth--;
        } else {
            if (!body.isEmpty()) body.remove(body.size() - 1);
        }

        // va chạm
        if (checkSelfCollision() || checkWallCollision(GamePanel.COLS, GamePanel.ROWS)) {
            alive = false;
        }
    }


    public void grow() {
        pendingGrowth += 1;

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


    public boolean checkWallCollision(int width, int height) {
        Point head = body.get(0);
        return (head.x < 0 || head.y < 0 || head.x >= width || head.y >= height);
    }


    public void draw(Graphics g, int cellSize) {
        g.setColor(Color.GREEN);
        for (Point p : body) {
            g.fillRect(p.x * cellSize, p.y * cellSize, cellSize, cellSize);
        }
    }


    public void setDirection(int dir) {
        if (
            (direction == UP && dir != DOWN) ||
            (direction == DOWN && dir != UP) ||
            (direction == LEFT && dir != RIGHT) ||
            (direction == RIGHT && dir != LEFT)
        ) {
            direction = dir;
        }
    }


    public boolean eat(Food food) {
        if (food == null) return false;

        if (getHead().x == food.getX() && getHead().y == food.getY()) {
            Food.FoodType t = food.getType();
            switch (t) {
                case NORMAL -> pendingGrowth += 1;    
                case BONUS  -> pendingGrowth += 2;    
                case SPEED  -> applyPermanentSpeedBoost();
                case POISON -> alive = false;         
            }
            return true;
        }
        return false;
    }
}
