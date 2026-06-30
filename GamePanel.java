import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;

public class GamePanel extends JPanel implements KeyListener, ActionListener {


    public static final int TILE_SIZE = 15;
    public static final int COLS = 51;
    public static final int ROWS = 38;
    private static final int DELAY = 200; // ms

    private Timer timer;
    private Snake snake;


    private Food foodA; 
    private Food foodB; 

    private boolean running = false;
    private final GameFrame parent;
    private final ScoreManager scores;

    public GamePanel(GameFrame parent) {
        this.parent = parent;
        this.scores = new ScoreManager();

        setPreferredSize(new Dimension(COLS * TILE_SIZE, ROWS * TILE_SIZE));
        setBackground(Color.DARK_GRAY);
        setFocusable(true);
        addKeyListener(this);

        startGame();
    }

    public void startGame() {
 
        Point start = new Point(COLS / 2, ROWS / 2);
        snake = new Snake(start, 1, Snake.RIGHT, DELAY);


        spawnFoodA();

        maintainFoodRule();

        scores.resetScore();
        running = true;

        if (timer != null) timer.stop();
        timer = new Timer(DELAY, this);
        timer.start();

        requestFocusInWindow();
        repaint();
    }



    private boolean overlap(Food a, Food b) {
        if (a == null || b == null) return false;
        return a.getX() == b.getX() && a.getY() == b.getY();
    }

    private boolean isEdible(Food.FoodType t) {
        return t == Food.FoodType.NORMAL || t == Food.FoodType.BONUS;
    }

    private void spawnFoodA() {
        if (foodA == null) foodA = new Food(COLS, ROWS, TILE_SIZE);

        do {
            foodA.spawn(snake);
        } while (foodB != null && overlap(foodA, foodB));
    }


    private void spawnFoodBEdible() {
        if (foodB == null) foodB = new Food(COLS, ROWS, TILE_SIZE);
        int guard = 0;
        do {
            foodB.spawn(snake);
            guard++;
        } while ((!isEdible(foodB.getType()) || overlap(foodA, foodB)) && guard < 100);
    }


    private void maintainFoodRule() {
        boolean aIsPower = (foodA.getType() == Food.FoodType.SPEED || foodA.getType() == Food.FoodType.POISON);
        if (aIsPower) {
            if (foodB == null || !isEdible(foodB.getType())) {
                spawnFoodBEdible();
            }
        } else {
            
        }
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

  
        if (foodA != null) foodA.draw(g);
        if (foodB != null) foodB.draw(g);

     
        List<Point> body = snake.getBody();
        for (int i = 0; i < body.size(); i++) {
            Point p = body.get(i);
            g.setColor(i == 0 ? Color.YELLOW : new Color(80, 180, 255));
            g.fillRect(p.x * TILE_SIZE, p.y * TILE_SIZE, TILE_SIZE, TILE_SIZE);
        }

        // HUD
        g.setColor(Color.WHITE);
        g.setFont(new Font("Monospaced", Font.BOLD, 14));
        g.drawString("Score: " + scores.getCurrentScore(), 8, 18);
        g.drawString("Highscore: " + scores.getHighScore(), 140, 18);
        g.drawString("Speed(ms): " + snake.getSpeed(), 300, 18);
        g.drawString("ESC: Menu", 430, 18);

        if (!running) {
            String msg = "GAME OVER - Press R to Restart";
            g.setFont(new Font("Arial", Font.BOLD, 28));
            FontMetrics fm = g.getFontMetrics();
            int w = fm.stringWidth(msg);
            int x = (getWidth() - w) / 2;
            int y = getHeight() / 2;

            g.setColor(new Color(0, 0, 0, 150));
            g.fillRect(0, 0, getWidth(), getHeight());

            g.setColor(Color.RED);
            g.drawString(msg, x, y);
        }
    }



    @Override
    public void actionPerformed(ActionEvent e) {
        if (!running) {
            repaint();
            return;
        }


        snake.move(foodA);

      
        timer.setDelay(snake.getSpeed());


        if (!snake.isAlive() || snake.checkWallCollision(COLS, ROWS) || snake.checkSelfCollision()) {
            running = false;
            timer.stop();
            repaint();
            return;
        }


        boolean ateA = (foodA != null && foodA.isEaten(snake.getHead()));
        if (ateA) {
            switch (foodA.getType()) {
                case NORMAL -> scores.increaseScore(1);
                case BONUS  -> scores.increaseScore(3);
                case SPEED  -> {  }
                case POISON -> { 
                    running = false;
                    timer.stop();
                    repaint();
                    return;
                }
            }
         
            spawnFoodA();
        }


        boolean ateB = (foodB != null && foodB.isEaten(snake.getHead()));
        if (ateB) {
            
            snake.eat(foodB); 
            switch (foodB.getType()) {
                case NORMAL -> scores.increaseScore(1);
                case BONUS  -> scores.increaseScore(3);
                case SPEED  -> {  }
                case POISON -> {
                    running = false;
                    timer.stop();
                    repaint();
                    return;
                }
            }
            
            foodB = null;
        }

 
        if (foodA != null && foodA.isExpired()) {
            spawnFoodA();
        }
        if (foodB != null && foodB.isExpired()) {
            foodB = null; 
        }

    
        maintainFoodRule();

        repaint();
    }

    private void restartGame() {
        startGame();
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int k = e.getKeyCode();

        if (k == KeyEvent.VK_UP)    snake.setDirection(Snake.UP);
        if (k == KeyEvent.VK_RIGHT) snake.setDirection(Snake.RIGHT);
        if (k == KeyEvent.VK_DOWN)  snake.setDirection(Snake.DOWN);
        if (k == KeyEvent.VK_LEFT)  snake.setDirection(Snake.LEFT);

        if (k == KeyEvent.VK_R && !running) restartGame();

        if (k == KeyEvent.VK_ESCAPE) {
            if (timer != null) timer.stop();
            if (parent != null) parent.switchToMenu();
        }
    }

    @Override public void keyReleased(KeyEvent e) { }
    @Override public void keyTyped(KeyEvent e) { }
}
