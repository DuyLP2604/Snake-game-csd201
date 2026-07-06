import java.awt.Point;
import java.util.*;

public class PathFinder {

    private static final int[] DX = {0, 1, 0, -1};
    private static final int[] DY = {-1, 0, 1, 0};

    public static List<Point> findPath(
            Snake snake,
            Food food,
            MapManager mapManager) {

        Point start = snake.getHead();
        Point goal = new Point(food.getX(), food.getY());

        int[][] map = mapManager.getMap();

        int rows = map.length;
        int cols = map[0].length;

        boolean[][] visited = new boolean[rows][cols];
        Point[][] parent = new Point[rows][cols];

        Queue<Point> queue = new LinkedList<>();

        queue.add(start);
        visited[start.y][start.x] = true;

        while (!queue.isEmpty()) {

            Point current = queue.poll();

            if (current.equals(goal)) {
                return buildPath(parent, start, goal);
            }

            for (int i = 0; i < 4; i++) {

                int nx = current.x + DX[i];
                int ny = current.y + DY[i];

                if (nx < 0 || ny < 0 || nx >= cols || ny >= rows) {
                    continue;
                }

                if (visited[ny][nx]) {
                    continue;
                }

                // Chỉ coi WALL (1) là vật cản
                if (map[ny][nx] == TileType.WALL) {
                    continue;
                }

                Point next = new Point(nx, ny);

                // Tránh thân rắn
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