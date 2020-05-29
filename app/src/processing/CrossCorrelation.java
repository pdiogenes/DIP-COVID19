package processing;

import object.Component;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Pedro
 */
public class CrossCorrelation { // https://docs.opencv.org/3.4/de/da9/tutorial_template_matching.html
    public static boolean match(Component c, Component s){
        Mat result = new Mat();
        
        Mat image = c.getImage();
        Mat sample = s.getImage();
        
        
        int result_cols = image.cols() - sample.cols() + 1;
        int result_rows = image.rows() - sample.rows() + 1;
        
        result.create(result_rows, result_cols, CvType.CV_32FC1);
        Imgproc.matchTemplate(image, sample, result, Imgproc.TM_CCORR_NORMED);
        
        double minMatchQuality = 0.5;
                
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        Point matchLoc = mmr.maxLoc;
        
        if (mmr.maxVal >= minMatchQuality){
            return true;
        } else return false;
        
    }
    
    /*public static Mat match(Component c, Mat image){
        Mat result = new Mat();
        Mat img_display = new Mat();
        
        Mat sample = c.getOriginal();
        
        image.copyTo(img_display);
        
        if(sample.width() < image.width() || sample.height() < image.height()) return null;
        
        int result_cols = image.cols() - sample.cols() + 1;
        int result_rows = image.rows() - sample.rows() + 1;
        
        result.create(result_rows, result_cols, CvType.CV_32FC1);
        Imgproc.matchTemplate(image, sample, result, Imgproc.TM_CCORR_NORMED);
        
        double minMatchQuality = 0.5;
                
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);
        Point matchLoc = mmr.maxLoc;
        Imgproc.rectangle(img_display, matchLoc, new Point(matchLoc.x + sample.cols(), matchLoc.y + sample.rows()),
                new Scalar(255, 255, 255), 2, 8, 0);
        
        result.convertTo(result, CvType.CV_8UC1, 255.0);
        if (mmr.maxVal >= minMatchQuality){
            return img_display;
        } else return null;
        
    }*/
}
