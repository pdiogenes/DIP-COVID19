package utilities;

import java.awt.image.BufferedImage;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

/**
 *
 * @author Pedro
 */
public class MatConversion {
    public static BufferedImage Mat2BufferedImage(Mat mat) {
        BufferedImage out;
        byte[] data = new byte[mat.width() * mat.height() * (int) mat.elemSize()];
        int type;
        mat.get(0, 0, data);

        if (mat.channels() == 1)
            type = BufferedImage.TYPE_BYTE_GRAY;
        else
            type = BufferedImage.TYPE_3BYTE_BGR;

        out = new BufferedImage(mat.width(), mat.height(), type);

        out.getRaster().setDataElements(0, 0, mat.width(), mat.height(), data);
        return out;
    }

    public static Mat BufferedImage2Mat(BufferedImage bi) {
        Mat out;
        byte[] data;
        int r, g, b;
        int width = bi.getWidth();
        int height = bi.getHeight();

        if (bi.getType() == BufferedImage.TYPE_INT_RGB) {
            out = new Mat(height, width, CvType.CV_8UC3);
            data = new byte[width * height * (int) out.elemSize()];
            int[] dataBuff = bi.getRGB(0, 0, width, height, null, 0, width);
            for (int i = 0; i < dataBuff.length; i++) {
                data[i * 3] = (byte) ((dataBuff[i] >> 16) & 0xFF);
                data[i * 3 + 1] = (byte) ((dataBuff[i] >> 8) & 0xFF);
                data[i * 3 + 2] = (byte) ((dataBuff[i] >> 0) & 0xFF);
            }
        } else {
            out = new Mat(height, width, CvType.CV_8UC1);
            data = new byte[width * height * (int) out.elemSize()];
            int[] dataBuff = bi.getRGB(0, 0, width, height, null, 0, width);
            for (int i = 0; i < dataBuff.length; i++) {
                r = (byte) ((dataBuff[i] >> 16) & 0xFF);
                g = (byte) ((dataBuff[i] >> 8) & 0xFF);
                b = (byte) ((dataBuff[i] >> 0) & 0xFF);
                data[i] = (byte) ((0.21 * r) + (0.71 * g) + (0.07 * b)); // luminosity
            }
        }
        out.put(0, 0, data);
        return out;
    }
}
