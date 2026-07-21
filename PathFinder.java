import java.awt.Point;
import java.util.*;

public class PathFinder {

    private static final int[] DX = {0, 1, 0, -1};
    private static final int[] DY = {-1, 0, 1, 0};

    private static final Set<Integer> BLOCKED_TILES = Set.of(
            1, 2, 5, 8, 10, 11, 12, 13, 14, 15
    );

    public static List<Point> findPath(
            Snake snake,
            Food food,
            MapManager mapManager) {

        Point start = snake.getHead();
        Point goal;

        if (mapManager.isGateOpened()) {

            goal = new Point(
                    mapManager.getGatePoint().x + 1,
                    mapManager.getGatePoint().y + 1
            );

        } else {

            goal = new Point(
                    food.getX(),
                    food.getY()
            );
        }

        int[][] map = mapManager.getMap();

        int rows = map.length;
        int cols = map[0].length;

        // mark visited location
        boolean[][] visited = new boolean[rows][cols];

        // find path
        Point[][] parent = new Point[rows][cols];

        Queue<Point> queue = new LinkedList<>();

        queue.add(start);

        // put the snake head to queue first
        visited[start.y][start.x] = true;

        while (!queue.isEmpty()) {

            Point current = queue.poll();

            // if reach the food
            if (current.equals(goal)) {
                return buildPath(parent, start, goal);
            }

            // process a block in 4 direction
            for (int i = 0; i < 4; i++) {

                int nx = current.x + DX[i];
                int ny = current.y + DY[i];

                // avoid going out the map
                if (nx < 0 || ny < 0 || nx >= cols || ny >= rows) {
                    continue;
                }

                // skip visited location
                if (visited[ny][nx]) {
                    continue;
                }

                // skip if it is a blocked title
                if (GameConfig.isObstacle(map[ny][nx])) {
                    continue;
                }

                Point next = new Point(nx, ny);

                // avoid the snake body
                boolean onSnake = false;

                for (Point bodyPart : snake.getBody()) {

                    if (bodyPart.equals(next)) {
                        onSnake = true;
                        break;
                    }
                }

                if (onSnake) {
                    continue;
                }

                // mark as visited
                visited[ny][nx] = true;
                parent[ny][nx] = current;

                queue.add(next);
            }
        }

        return new ArrayList<>();
    }

    private static List<Point> buildPath(
            Point[][] parent,
            Point start,
            Point goal) {

        List<Point> path = new ArrayList<>();

        Point current = goal;

        while (current != null) {

            path.add(current);

            if (current.equals(start)) {
                break;
            }

            current = parent[current.y][current.x];
        }

        Collections.reverse(path);

        return path;
    }
}