import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import javax.swing.*;

public class MapManager {
    private int[][] map;
    private Point spawnPoint;
    private Point gatePoint;
    private boolean gateOpened = false;
    private int currentLevel;

    private TileSet tileSet;
    // Ánh xạ loại ô (0-9, xem TileType.java) sang ảnh sprite đã cắt sẵn
    private HashMap<Integer, BufferedImage> tileImages;
    private Image gateImage = new ImageIcon("resources/gate.png").getImage();
    public MapManager(int level) {
        tileImages = new HashMap<>();
        loadTileSet(level);
        loadMap(level);
        findSpecialTiles();
        this.currentLevel = level;

        switch (level) {
            case 1: Sound.playBackground("forest_backgound_music.wav");
                    break;
            case 2: Sound.playBackground("ocean_backgound_music.wav");
                    break;
            case 3: Sound.playBackground("desert_backgound_music.wav");
                    break;
        }
    }

    private void loadMap(int level) {
        map = new int[GameConfig.ROWS][GameConfig.COLS];
        String fileName = "resources/maps/level" + level + ".txt";
        try {
            java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(fileName));
            String line;
            int row = 0;
            while ((line = br.readLine()) != null && row < GameConfig.ROWS) {
                // Mỗi ô là một số nguyên (0, 1, 2, ..., 10, 11, ...), ngăn cách bởi dấu phẩy.
                // Cho phép id tile > 9, không còn giới hạn 1 ký tự/ô như trước.
                String[] tokens = line.trim().split(",");
                for (int col = 0; col < Math.min(tokens.length, GameConfig.COLS); col++) {
                    String tok = tokens[col].trim();
                    if (!tok.isEmpty()) {
                        try {
                            map[row][col] = Integer.parseInt(tok);
                        } catch (NumberFormatException nfe) {
                            map[row][col] = 0; // token lỗi -> coi như nền
                        }
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
    // Đặt 3 file ảnh vào đúng thư mục con trong "resources" (giống cấu
    // trúc resources/maps đang có):
    //   resources/forest/tile.png   (Level 1 - Easy)
    //   resources/ocean/tile.png            (Level 2 - Medium)
    //   resources/desert/DesertTiles.png            (Level 3 - Hard)

    private void loadTileSet(int level) {
        switch (level) {
            case 2 -> loadOceanTileSet();
            case 3 -> loadDesertTileSet();
            default -> loadForestTileSet();
        }
    }

    private void loadOceanTileSet() {
        tileSet = new TileSet("resources/ocean/tile.png");
        int s = GameConfig.TILE_SIZE;

        tileImages.put(0, tileSet.getRegion(594,505,609,521));   // GROUND - đảo cát
        tileImages.put(1, tileSet.getRegion(202,357,214,368));       // WALL   - vách san hô
        tileImages.put(2, tileSet.getRegion(480,604,498,624));   // TREE   - rừng tảo biển rậm
        tileImages.put(3, tileImages.get(0));                         // SPAWN  - ẩn, vẽ như đảo cát
        tileImages.put(4, TileSet.solidTile(s, new Color(255, 213, 79))); // GATE - ô vàng đánh dấu cổng
        tileImages.put(5, tileSet.getRegion(627,534,645,554));     // ROCK   - cột đá ngầm
        tileImages.put(6, tileSet.getRegion(787,444,805,464));   // BUSH   - tảo biển nhỏ
        tileImages.put(7, tileSet.getRegion(257,435,275,451));    // FLOWER - san hô tím
        tileImages.put(8, tileSet.getRegion(544,418,561,434));   // DECOR1
        tileImages.put(9, tileSet.getRegion(579,582,596,598));    // DECOR2
        tileImages.put(10, tileSet.getRegion(783,588,800,604));    // Sò to góc trên bên trái
        tileImages.put(11, tileSet.getRegion(796,588,813,604));    // Sò to góc trên bên phải
        tileImages.put(12, tileSet.getRegion(783,604,800,620));    // Sò to góc dưới bên trái
        tileImages.put(13, tileSet.getRegion(796,604,813,620));    // Sò to góc dưới bên phải

    }

    private void loadForestTileSet() {
        // Bộ tileset 16x16 chuẩn (lưới đều), đẹp và tile liền mạch hơn nhiều
        // so với ảnh preview asset pack cũ.
        tileSet = new TileSet("resources/forest/tile.png");
        int s = GameConfig.TILE_SIZE;

        tileImages.put(0, tileSet.getRegion(245,68,260,84));      // GROUND - cỏ nền
        tileImages.put(1, tileSet.getRegion(216,317,230,332));    // WALL   - hàng rào bụi cây (tile liền mạch, ghép sát nhau không lộ mối nối)
        tileImages.put(2, tileSet.getRegion(342,89,359,114));     // TREE   - cây to
        tileImages.put(3, tileImages.get(0));                        // SPAWN  - ẩn, vẽ như nền cỏ
        tileImages.put(4, TileSet.solidTile(s, new Color(255, 213, 79))); // GATE - ô vàng đánh dấu cổng
        tileImages.put(5, tileSet.getRegion(452,215,466,231));    // ROCK   - đá nhỏ
        tileImages.put(6, tileSet.getRegion(450,274,465,291));     // BUSH   - bụi nhỏ
        tileImages.put(7, tileSet.getRegion(428,273,446,291));     // FLOWER - hoa/quả nhỏ
        tileImages.put(8, tileSet.getRegion(223,51,243,72));       // TREE ROOT - gốc cây bị chặt
        tileImages.put(9, tileSet.getRegion(596,113,578,131));    // DECOR  - mảng đất/sỏi nhỏ
        tileImages.put(10, tileSet.getRegion(810,100,830,121));   // TREE1  - góc trên bên trái cây to
        tileImages.put(11, tileSet.getRegion(828,100,848,121));   // TREE2  - góc trên bên phải cây to
        tileImages.put(12, tileSet.getRegion(810,116,830,137));   // TREE3  - góc dưới bên trái cây to
        tileImages.put(13, tileSet.getRegion(828,116,848,137));   // TREE4  - góc dưới bên phải cây to
        tileImages.put(14, tileSet.getRegion(810,140,830,162));   // TREE4  - gốc cây bên trái cây to
        tileImages.put(15, tileSet.getRegion(828,140,848,162));   // TREE4  - gốc cây bên phải cây to
    }

    private void loadDesertTileSet() {
        tileSet = new TileSet("resources/desert/DesertTiles.png");
        int s = GameConfig.TILE_SIZE;

        tileImages.put(0, tileSet.getRegion(629,795,649,816));  // GROUND - nền cát
        tileImages.put(1, tileSet.getRegion(292,835,312,856));             // WALL   - khối đất đá gồ ghề
        tileImages.put(2, tileSet.getRegion(947,291,972,319));              // TREE   - xương rồng to
        tileImages.put(3, tileImages.get(0));                                // SPAWN  - ẩn, vẽ như nền cát
        tileImages.put(4, TileSet.solidTile(s, new Color(255, 213, 79)));   // GATE   - ô vàng đánh dấu cổng
        tileImages.put(5, tileSet.getRegion(486,475,521,518));                // ROCK   - đá nhỏ
        tileImages.put(6, tileSet.getRegion(606,333,631,361));               // BUSH   - xương rồng vừa
        tileImages.put(7, tileSet.getRegion(1018,261,1043,289));                // FLOWER - cây cọ nhỏ
        tileImages.put(8, tileSet.getRegion(521,632,546,660));   // WATER  - không có sprite nước trong pack sa mạc, dùng màu đặc
        tileImages.put(9, tileSet.getRegion(455,224,480,252));               // DECOR  - xương rồng nhỏ
        tileImages.put(10, tileSet.getRegion(329,420,355,447));   // TREE1  - góc trên bên trái cây dừa
        tileImages.put(11, tileSet.getRegion(355,420,375,447));   // TREE2  - góc trên bên phải cây dừa
        tileImages.put(12, tileSet.getRegion(329,444,355,471));   // TREE3  - góc dưới bên trái cây dừa
        tileImages.put(13, tileSet.getRegion(355,444,375,471));   // TREE4  - góc dưới bên phải cây dừa
        tileImages.put(14, tileSet.getRegion(329,468,355,495));   // TREE4  - gốc cây bên trái cây dừa
        tileImages.put(15, tileSet.getRegion(355,468,375,495));   // TREE4  - gốc cây bên phải cây dừa
    }

    public void updateGate(int score) {
        gateOpened = score >= GameConfig.getGateScore(currentLevel);
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
        // Có thể đi xuyên bụi cỏ, hoa.
        return t == 7 || t == 8 || t == 9;
    }

    public boolean isGate(int x, int y) {
        if (gatePoint == null) return false;
        
        return x >= gatePoint.x && x < gatePoint.x + 3 &&
               y >= gatePoint.y && y < gatePoint.y + 3;
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
                
                if (img == null || tileType == 4) {
                    img = tileImages.get(0); 
                }

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

        if (gatePoint != null) {
            int gateX = gatePoint.x * GameConfig.TILE_SIZE;
            int gateY = gatePoint.y * GameConfig.TILE_SIZE;
            
            int gateSizeMultiplier = 3; 
            int bigSize = GameConfig.TILE_SIZE * gateSizeMultiplier;
            
            g.drawImage(gateImage, gateX, gateY, bigSize, bigSize, null);
        }
    }
}