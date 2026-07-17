import java.io.*;
import java.nio.file.*;
import java.util.*;

public class ScoreHistory {
    private static final int MAX_HISTORY = 10;
    private final Path filePath;
    private final LinkedList<Integer> recentScores = new LinkedList<>();

    public ScoreHistory() {
        filePath = Paths.get("score_history.txt");
        load();
    }

    public void addScore(int score) {
        recentScores.addFirst(score);
        while (recentScores.size() > MAX_HISTORY) {
            recentScores.removeLast();
        }
        save();
    }

    public List<Integer> getRecentScores() {
        return Collections.unmodifiableList(recentScores);
    }

    private void load() {
        recentScores.clear();
        try {
            if (!Files.exists(filePath)) return;
            for (String line : Files.readAllLines(filePath)) {
                line = line.trim();
                if (!line.isEmpty()) recentScores.add(Integer.parseInt(line));
            }
        } catch (Exception e) {
            System.out.println("Khong doc duoc lich su diem: " + e.getMessage());
        }
    }

    private void save() {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (int s : recentScores) {
                writer.write(String.valueOf(s));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Khong luu duoc lich su diem: " + e.getMessage());
        }
    }
}