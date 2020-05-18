package processing;

import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Pedro
 */
public class Threshold {
    public static Mat threshold(Mat mat) {
        Mat dst = new Mat();
        Imgproc.threshold(mat, dst, 200, 255, Imgproc.THRESH_BINARY);
        return dst;
    }
}
