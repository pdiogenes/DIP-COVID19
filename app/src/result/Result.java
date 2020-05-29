package result;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import object.Component;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Pedro
 */
public class Result {
    public static Random rand = new Random();
    
    public static Mat resultado(List<Component> resultados, Mat original){
        // creates an empty image to paint the results on
        Mat draw = original.clone();
        Imgproc.cvtColor(draw, draw, Imgproc.COLOR_GRAY2BGR);
        
        for (int i = 0; i < resultados.size(); i++) {
            // initiates an array of contours
            ArrayList<MatOfPoint> contours = new ArrayList<>();
            Mat hierarchy = new Mat();
            // finds contour for each object
            // this will only find 1 contour because of the previous Labelling function
            Imgproc.findContours(resultados.get(i).getImage(), contours, hierarchy, Imgproc.RETR_EXTERNAL,
                    Imgproc.CHAIN_APPROX_SIMPLE);
            // paints the contours
            for (int j = 0; j < contours.size(); j++) {
                Imgproc.drawContours(draw, contours, j,
                        new Scalar(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)), -1);
            }
        }
        return draw;
    }
}
