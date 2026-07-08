import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class GameState {
    private List<Point> snakeBody;
    private int currentScore;
    private Point foodAPosition;
    private Point foodBPosition;
    private List<Food> savedFoods;

    public GameState(List<Point> body, int score, List<Food> activeFoods) {
        this.snakeBody = new ArrayList<>();
        for (Point p : body) {
            this.snakeBody.add(new Point(p.x, p.y));
        }
        this.currentScore = score;
        this.savedFoods = new ArrayList<>();
        if (activeFoods != null) {
            this.savedFoods.addAll(activeFoods);
        }
    }

    public List<Point> getSnakeBody() { return snakeBody; }
    public int getCurrentScore() { return currentScore; }
    public Point getFoodAPosition() { return foodAPosition; }
    public Point getFoodBPosition() { return foodBPosition; }
}