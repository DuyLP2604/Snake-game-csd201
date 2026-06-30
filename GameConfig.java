public class GameConfig {
    public static final int TILE_SIZE = 15;
    public static final int COLS = 51;
    public static final int ROWS = 38;


    public static final int TIMER_DELAY_MS = 200;


    // Food probabilities (sum should be 100)
    public static final int FOOD_RATE_NORMAL = 75;
    public static final int FOOD_RATE_BONUS = 15;
    public static final int FOOD_RATE_SPEED = 7;
    public static final int FOOD_RATE_POISON = 3;


    // Food lifetimes (ms)
    public static final long LIFE_NORMAL_MS = 15_000;
    public static final long LIFE_BONUS_MS = 10_000;
    public static final long LIFE_SPEED_MS = 7_000;
    public static final long LIFE_POISON_MS = 20_000;


    // Blink configuration
    public static final long BLINK_THRESHOLD_MS = 2_000;
    public static final long BLINK_INTERVAL_MS = 200;


    // Speed configuration
    public static final int BASE_SPEED = 200; // in ms per tick
    public static final int SPEED_STEP = 5;
    public static final int MIN_SPEED = 50;
    public static final int SPEED_PERM_DELTA_MS = 30;


    // Score values
    public static final int SCORE_NORMAL = 1;
    public static final int SCORE_BONUS = 3;
    // Bản đồ Level 1: Chỉ có tường bao quanh biên ngoài cùng
    public static final int[][] MAP_LEVEL_1 = createBorderMap();

    // Bản đồ Level 2: Có thêm chướng ngại vật ở giữa
    public static final int[][] MAP_LEVEL_2 = createLevel2Map();

    private static int[][] createBorderMap() {
        int[][] map = new int[ROWS][COLS];
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (r == 0 || r == ROWS - 1 || c == 0 || c == COLS - 1) {
                    map[r][c] = 1; // 1 đại diện cho Tường
                } else {
                    map[r][c] = 0; // 0 đại diện cho Ô trống
                }
            }
        }
        return map;
    }

    private static int[][] createLevel2Map() {
        int[][] map = createBorderMap();
        // Thêm tường chắn dọc ở giữa bên trái
        for (int r = 8; r < 30; r++) {
            map[r][15] = 1;
        }
        // Thêm tường chắn dọc ở giữa bên phải
        for (int r = 8; r < 30; r++) {
            map[r][35] = 1;
        }
        return map;
    }
}
