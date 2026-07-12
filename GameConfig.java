import java.util.Set;

public class GameConfig {

    public static final int TILE_SIZE = 15;

    public static final int COLS = 51;

    public static final int ROWS = 38;

    public static final int MAX_HINTS = 10;

    // Điểm để mở cổng
    public static final int GATE_SCORE = 20;

    public static final Set<Integer> OBSTACLE_TILES = Set.of(
            1, 2, 5, 8, 10, 11, 12, 13, 14, 15
    );
    public static final int[] GATE_SCORES = { 0, 15, 25, 35 };

    public static int getGateScore(int level) {
        if (level < 1 || level >= GATE_SCORES.length) return GATE_SCORE;
        return GATE_SCORES[level];
    }
    public static boolean isObstacle(int tile) {
        return OBSTACLE_TILES.contains(tile);
    }

    /*
        0 = Ground
        1 = Wall
        2 = Decoration
        3 = Snake Spawn
        4 = Gate
    */
}