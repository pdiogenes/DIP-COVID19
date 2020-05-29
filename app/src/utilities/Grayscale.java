package utilities;

import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;

/**
 *
 * @author Pedro 
 * converts a regular buffered image into a grayscale one
 * useless since opencv has a conversion method
 */
public class Grayscale {
    public static BufferedImage getGray(BufferedImage image) {
        BufferedImage g = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        ColorConvertOp op = new ColorConvertOp(image.getColorModel().getColorSpace(), g.getColorModel().getColorSpace(), null);
        op.filter(image, g);
        return g;
    }
}
