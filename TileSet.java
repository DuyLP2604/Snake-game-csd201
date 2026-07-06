import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import javax.imageio.ImageIO;

/**
 * TileSet phiên bản mới.
 *
 * Bản cũ chỉ hỗ trợ cắt lưới đều (mỗi ô cùng kích thước tileSize, đánh số
 * tuần tự). Các asset pack thực tế (rừng / biển / sa mạc) lại có sprite
 * kích thước khác nhau (cây to, cây nhỏ, đá, bụi...) nằm rải rác trên ảnh,
 * nên lớp này cho phép cắt trực tiếp theo toạ độ pixel (x0,y0) -> (x1,y1)
 * của từng sprite, có cache lại để không phải cắt lại nhiều lần.
 */
public class TileSet {

    private BufferedImage sheet;
    private final HashMap<String, BufferedImage> cache = new HashMap<>();

    public TileSet(String path) {
        try {
            sheet = ImageIO.read(new File(path));
        } catch (Exception e) {
            System.err.println("Khong the tai tileset: " + path);
            e.printStackTrace();
        }
    }

    public boolean isLoaded() {
        return sheet != null;
    }

    /**
     * Cắt một vùng ảnh theo toạ độ pixel tuyệt đối trên sheet gốc.
     * Trả về null nếu tileset chưa load được hoặc toạ độ không hợp lệ.
     */
    public BufferedImage getRegion(int x0, int y0, int x1, int y1) {
        if (sheet == null) return null;

        String key = x0 + "," + y0 + "," + x1 + "," + y1;
        BufferedImage cached = cache.get(key);
        if (cached != null) return cached;

        int sw = sheet.getWidth();
        int sh = sheet.getHeight();
        int cx0 = Math.max(0, Math.min(x0, sw));
        int cy0 = Math.max(0, Math.min(y0, sh));
        int cx1 = Math.max(0, Math.min(x1, sw));
        int cy1 = Math.max(0, Math.min(y1, sh));

        int w = cx1 - cx0;
        int h = cy1 - cy0;
        if (w <= 0 || h <= 0) return null;

        BufferedImage sub = sheet.getSubimage(cx0, cy0, w, h);
        cache.put(key, sub);
        return sub;
    }

    /**
     * Tạo một ô màu đặc (dùng cho nền cơ bản khi asset pack không có sẵn
     * sprite nền phù hợp, ví dụ ô cỏ/cát trơn hoặc ô đánh dấu cổng).
     */
    public static BufferedImage solidTile(int size, Color color) {
        BufferedImage img = new BufferedImage(size, size, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = img.createGraphics();
        g2.setColor(color);
        g2.fillRect(0, 0, size, size);
        g2.dispose();
        return img;
    }
}
