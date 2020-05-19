package processing;

import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;

/**
 *
 * @author Pedro
 */
public class ImageResize {

    // método para redimensionar a imagem para as dimensões do container
    public static BufferedImage resize(BufferedImage image, int w, int h) {
        BufferedImage bi = new BufferedImage(w, h, BufferedImage.TRANSLUCENT);
        Graphics2D g2d = (Graphics2D) bi.createGraphics();
        g2d.addRenderingHints(new RenderingHints(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY));
        g2d.drawImage(image, 0, 0, w, h, null);
        g2d.dispose();
        return bi;
    }

}
