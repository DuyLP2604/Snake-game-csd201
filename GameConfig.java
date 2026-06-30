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
}
