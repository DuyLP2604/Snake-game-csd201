import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class GameState {
    private List<Point> snakeBody;
    private int currentScore;
    private Point foodAPosition;
    private Point foodBPosition;

    public GameState(List<Point> body, int score, Point foodA, Point foodB) {
        // Sao chép sâu (Deep copy) danh sách tọa độ để tránh bị tham chiếu
        this.snakeBody = new ArrayList<>();
        for (Point p : body) {
            this.snakeBody.add(new Point(p.x, p.y));
        }
        this.currentScore = score;
        this.foodAPosition = foodA != null ? new Point(foodA.x, foodA.y) : null;
        this.foodBPosition = foodB != null ? new Point(foodB.x, foodB.y) : null;
    }

    public List<Point> getSnakeBody() { return snakeBody; }
    public int getCurrentScore() { return currentScore; }
    public Point getFoodAPosition() { return foodAPosition; }
    public Point getFoodBPosition() { return foodBPosition; }
}