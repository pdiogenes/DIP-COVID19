package processing;

import java.util.ArrayList;
import java.util.List;

import object.Component;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Vradoskein
 */
public class Labelling {

    public static List<Component> getLabels(Mat image) {
        Mat label = new Mat();
        List<Component> objetos = new ArrayList<>();

        // returns the labelled binary image and the number of elements
        int c = Imgproc.connectedComponents(image, label, 4, CvType.CV_16U);

        int labelValue;
        
        // creates an image for each of the objects
        for (int lbl = 1; lbl < c; lbl++) {
            Mat mat = new Mat(label.height(), label.width(), CvType.CV_8UC1, new Scalar(0, 0, 0));
            int size = 0;
            for (int i = 0; i < label.height(); i++) {
                for (int j = 0; j < label.width(); j++) {
                    labelValue = (int) label.get(i, j)[0];
                    if (labelValue == lbl) {
                        size++;
                        mat.put(i, j, 255);
                    }
                }
            }
            // checks if the image has a decent size before adding it to the object list
            // this is done to prevent random high frequency noise to be added
            // but some still get added
            if (size > 100) {
                objetos.add(new Component(mat, size));
            }
        }

        return objetos;
    }
    
    

    public static Mat getImagesForLabel(Mat image, Mat original){
        int menorW = image.width(), menorH = image.height();
        int maiorW = 0, maiorH = 0;
        int pixelValue;
        
        // finds where the object begins and ends on its own image
        for (int i = 0; i < image.height(); i++) {
            for (int j = 0; j < image.width(); j++) {
                pixelValue = (int) image.get(i, j)[0];
                if (pixelValue == 255) {
                    if(i < menorH) menorH = i;
                    if(i > maiorH) maiorH = i;
                    if(j < menorW) menorW = j;
                    if(j > maiorW) maiorW = j;
                }
            }
        }
        
        // creates a new object image using the original provided image
        // so we can use haralick descriptors and LBP on them
        Mat newImg = original.clone();
        Mat retorno = newImg.submat(menorH, maiorH, menorW, maiorW);
        
        return retorno;
    }
}