import java.io.*;
import java.nio.file.*;

public class ScoreManager {
    private int currentScore = 0;
    private int highScore = 0;
    private final Path filePath;

    public ScoreManager() {
        filePath = Paths.get("highscore.txt"); 
        highScore = loadHighScore();
    }
    //increase Score + uploads
    public void increaseScore(int points) {
        currentScore += points;
        if (currentScore > highScore) {
            highScore = currentScore;
            saveHighScore();
        }
    }
    //reset point when start new
    public void resetScore() {
        currentScore = 0;
    }
    public int getCurrentScore() {
        return currentScore;
    }
    public int getHighScore() {
        return highScore;
    }
    //Read the tallest from file
    private int loadHighScore() {
        try {
            if (!Files.exists(filePath)) return 0;
            BufferedReader reader = Files.newBufferedReader(filePath);
            String line = reader.readLine();
            reader.close();
            return Integer.parseInt(line.trim());
        } catch (Exception e) {
            return 0;
        }
    }
    //Write higher score in file
    private void saveHighScore() {
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            writer.write(String.valueOf(highScore));
        }
        catch (IOException e) {
            System.out.println("Don't save Score: " + e.getMessage());
        }
    }
}