package hist;

import java.util.ArrayList;
import java.util.List;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;
import org.opencv.core.MatOfInt;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Pedro
 */
public class Histogram { 
    
    // https://docs.opencv.org/3.4/d8/dbc/tutorial_histogram_calculation.html
    
    Mat image, hist;
    final int histSize = 256;

    public Mat getHist() {
        return hist;
    }
    
    
    public Histogram(Mat image){
        this.image = image;
        this.createHist();
    }
    
    public Mat createHistImage(){
        // sets the image size
        int histW = 512, histH = 400;
        // sets the size of each histogram bucket
        int binW = (int) Math.round((double) histW / histSize);
        // creates the histogram image
        Mat histImage = new Mat( histH, histW, CvType.CV_8UC3, new Scalar( 0,0,0) );
        // normalizes the histogram
        Core.normalize(hist, hist, 0, histImage.rows(), Core.NORM_MINMAX);
        // gets data for values
        float[] bHistData = new float[(int) (hist.total() * hist.channels())];
        hist.get(0, 0, bHistData);
        
        // draws the histogram
        for( int i = 1; i < histSize; i++ ) {
            Imgproc.line(histImage, new Point(binW * (i - 1), histH - Math.round(bHistData[i - 1])),
                    new Point(binW * (i), histH - Math.round(bHistData[i])), new Scalar(255, 0, 0), 2);
        }
        
        return histImage;
    }
    
    public void createHist(){
        // creates the data structure for the histogram
        List<Mat> images = new ArrayList<>();
        images.add(this.image);
        float[] range = {0, 256}; //the upper boundary is exclusive
        MatOfFloat histRange = new MatOfFloat(range);
        boolean accumulate = false;
        Mat histogram = new Mat();
        // calculates the histogram
        Imgproc.calcHist(images, new MatOfInt(0), new Mat(), histogram, new MatOfInt(histSize), histRange, accumulate);
        this.hist = histogram;
    }
}
