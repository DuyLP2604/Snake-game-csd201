import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.*;

public class MapManager {
    private int[][] map;
    private Point spawnPoint;
    private Point gatePoint;
    private boolean gateOpened = false;

    private TileSet tileSet;
    // Ánh xạ loại ô (0-9, xem TileType.java) sang ảnh sprite đã cắt sẵn
    private HashMap<Integer, BufferedImage> tileImages;

    public MapManager(int level) {
        tileImages = new HashMap<>();
        loadTileSet(level);
        loadMap(level);
        findSpecialTiles();
    }

    private void loadMap(int level) {
        map = new int[GameConfig.ROWS][GameConfig.COLS];
        String fileName = "resources/maps/level" + level + ".txt";
        try {
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(fileName));
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < GameConfig.ROWS) {
                for (int col = 0; col < Math.min(line.length(), GameConfig.COLS); col++) {
                    char ch = line.charAt(col);
                    if (Character.isDigit(ch)) {
                        map[row][col] = ch - '0';
                    }
                }
                row++;
            }
            br.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Cannot load map:\n" + fileName);
        }
    }

    private void findSpecialTiles() {
        for (int r = 0; r < GameConfig.ROWS; r++) {
            for (int c = 0; c < GameConfig.COLS; c++) {
                if (map[r][c] == 3) { // Điểm hồi sinh
                    spawnPoint = new Point(c, r);
                    map[r][c] = 0; // Khi chơi, làm ẩn đi, vẽ như nền cỏ
                }
                if (map[r][c] == 4) { // Cổng
                    gatePoint = new Point(c, r);
                }
            }
        }
    }

    // ================== NẠP TILESET THEO TỪNG LEVEL ==================
    // Mỗi level dùng 1 ảnh asset pack riêng, sprite được cắt theo toạ độ
    // pixel thủ công (vì các ảnh gốc không phải lưới đều).
    // Đặt 3 file ảnh này cùng thư mục chạy chương trình (ngang hàng với
    // thư mục "resources"):
    //   ForestAssets.png   (Level 1)
    //   OceanTileset.png   (Level 2)
    //   DesertTiles.png    (Level 3)

    private void loadTileSet(int level) {
        switch (level) {
            case 2 -> loadOceanTileSet();
            case 3 -> loadDesertTileSet();
            default -> loadForestTileSet();
        }
    }

    private void loadForestTileSet() {
        tileSet = new TileSet("resources/forest/ForestAssets.png");
        int s = GameConfig.TILE_SIZE;

        tileImages.put(0, TileSet.solidTile(s, new Color(94, 168, 96)));     // GROUND - nền cỏ (không có sprite nền phù hợp, dùng màu đặc)
        tileImages.put(1, tileSet.getRegion(1616, 82, 1716, 328));           // WALL   - cột đá lớn
        tileImages.put(2, tileSet.getRegion(84, 528, 304, 768));            // TREE   - cây to
        tileImages.put(3, tileImages.get(0));                                // SPAWN  - ẩn, vẽ như nền cỏ
        tileImages.put(4, TileSet.solidTile(s, new Color(255, 213, 79)));    // GATE   - ô vàng đánh dấu cổng
        tileImages.put(5, tileSet.getRegion(1177, 887, 1715, 975));         // ROCK   - cụm đá phủ rêu
        tileImages.put(6, tileSet.getRegion(106, 776, 280, 971));           // BUSH   - bụi cây tròn
        tileImages.put(7, tileSet.getRegion(1417, 826, 1503, 839));         // FLOWER - hoa nhỏ
        tileImages.put(8, tileSet.getRegion(1010, 784, 1601, 856));         // WATER  - vũng nước
        tileImages.put(9, tileSet.getRegion(84, 457, 148, 500));            // DECOR  - bụi cỏ nhỏ
    }

    private void loadOceanTileSet() {
        tileSet = new TileSet("resources/ocean/OceanTileset.png");
        int s = GameConfig.TILE_SIZE;

        tileImages.put(0, tileSet.getRegion(40, 1404, 212, 1560));   // GROUND - đảo cát
        tileImages.put(1, tileSet.getRegion(32, 0, 288, 160));       // WALL   - vách san hô
        tileImages.put(2, tileSet.getRegion(32, 1144, 288, 1344));   // TREE   - rừng tảo biển rậm
        tileImages.put(3, tileImages.get(0));                         // SPAWN  - ẩn, vẽ như đảo cát
        tileImages.put(4, TileSet.solidTile(s, new Color(255, 213, 79))); // GATE - ô vàng đánh dấu cổng
        tileImages.put(5, tileSet.getRegion(64, 512, 128, 640));     // ROCK   - cột đá ngầm
        tileImages.put(6, tileSet.getRegion(128, 964, 156, 1022));   // BUSH   - tảo biển nhỏ
        tileImages.put(7, tileSet.getRegion(210, 782, 240, 820));    // FLOWER - san hô tím
        tileImages.put(8, tileSet.getRegion(40, 1732, 216, 1888));   // WATER  - mặt nước biển
        tileImages.put(9, tileSet.getRegion(112, 372, 142, 396));    // DECOR  - sỏi đá nhỏ
    }

    private void loadDesertTileSet() {
        tileSet = new TileSet("resources/desert/DesertTiles.png");
        int s = GameConfig.TILE_SIZE;

        tileImages.put(0, TileSet.solidTile(s, new Color(230, 196, 120)));  // GROUND - nền cát
        tileImages.put(1, tileSet.getRegion(128, 0, 240, 144));             // WALL   - khối đất đá gồ ghề
        tileImages.put(2, tileSet.getRegion(93, 12, 115, 47));              // TREE   - xương rồng to
        tileImages.put(3, tileImages.get(0));                                // SPAWN  - ẩn, vẽ như nền cát
        tileImages.put(4, TileSet.solidTile(s, new Color(255, 213, 79)));   // GATE   - ô vàng đánh dấu cổng
        tileImages.put(5, tileSet.getRegion(33, 2, 47, 14));                // ROCK   - đá nhỏ
        tileImages.put(6, tileSet.getRegion(50, 35, 61, 62));               // BUSH   - xương rồng vừa
        tileImages.put(7, tileSet.getRegion(0, 53, 16, 80));                // FLOWER - cây cọ nhỏ
        tileImages.put(8, TileSet.solidTile(s, new Color(93, 173, 226)));   // WATER  - không có sprite nước trong pack sa mạc, dùng màu đặc
        tileImages.put(9, tileSet.getRegion(16, 67, 32, 80));               // DECOR  - xương rồng nhỏ
    }

    public void updateGate(int score) {
        gateOpened = score >= GameConfig.GATE_SCORE;
    }

    public boolean isGateOpened() {
        return gateOpened;
    }

    public Point getSpawnPoint() {
        return spawnPoint;
    }

    public Point getGatePoint() {
        return gatePoint;
    }

    public int[][] getMap() {
        return map;
    }

    public boolean isWall(int x, int y) {
        int t = map[y][x];
        // Coi vách đá và cây là tường
        return t == 1 || t == 2 || t == 5;
    }

    public boolean isDecoration(int x, int y) {
        int t = map[y][x];
        // Không thể đi xuyên bụi cỏ, hoa, đá
        return t == 7 || t == 8 || t == 9;
    }

    public boolean isGate(int x, int y) {
        return map[y][x] == 4;
    }

    public void reset(int level) {
        gateOpened = false;
        loadMap(level);
        findSpecialTiles();
    }

    public void draw(Graphics g) {
        for (int r = 0; r < GameConfig.ROWS; r++) {
            for (int c = 0; c < GameConfig.COLS; c++) {
                int x = c * GameConfig.TILE_SIZE;
                int y = r * GameConfig.TILE_SIZE;
                int tileType = map[r][c];

                BufferedImage img = tileImages.get(tileType);
                if (img == null) img = tileImages.get(0); // fallback về nền

                if (img != null) {
                    g.drawImage(
                            img,
                            x, y,
                            GameConfig.TILE_SIZE, GameConfig.TILE_SIZE,
                            null
                    );
                }
            }
        }
    }
}
