package utilities;

import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;

/**
 *
 * @author Pedro
 * motivation: getSubImage() is linked to original image
 * this allows it to be a full new image to be proccessed separately
 */
public class DeepCopy {
    public static BufferedImage copyImage(BufferedImage bi){
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(bi.getRaster().createCompatibleWritableRaster());
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }
}
