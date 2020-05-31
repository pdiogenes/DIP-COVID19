package processing;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Pedro
 */
public class Threshold {

    // using opencv, with parameters
    public static Mat threshold(Mat mat, int t) {
        Mat dst = new Mat();
        Imgproc.medianBlur(mat, dst, 3);
        Imgproc.threshold(dst, dst, t, 255, Imgproc.THRESH_BINARY_INV);
        return dst;
    }

    // no OPENCV.threshold
    public static Mat threshold2(Mat original, int t) {
        int width = original.width();
        int height = original.height();

        // creates a new image
        Mat newMat = new Mat(original.height(), original.width(), original.type());

        for (int h = 0; h < height; h++) {
            for (int w = 0; w < width; w++) {
                // gets the pixel grayscale value
                double value = original.get(w, h)[0];
                if (value > t) {
                    newMat.put(h, w, 255.0);
                } else {
                    newMat.put(h, w, 0.0);
                }
            }
        }
        return newMat;
    }

}
