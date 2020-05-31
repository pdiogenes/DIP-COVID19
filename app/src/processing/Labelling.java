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
        int c = Imgproc.connectedComponents(image, label, 8, CvType.CV_16U);

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
        System.out.println("Finished");
        return objetos;
    }
    
    public static List<Component> getLabels2(Mat image, int minArea) {
        Mat label = new Mat();
        List<Component> objetos = new ArrayList<>();
        List<Mat> imagens = new ArrayList<>();
        List<Integer> areas = new ArrayList<>();

        // returns the labelled binary image and the number of elements
        int c = Imgproc.connectedComponents(image, label, 8, CvType.CV_16U);
        
        for(int i = 0; i < c; i++){
            Mat mat = new Mat(label.height(), label.width(), CvType.CV_8UC1, new Scalar(0, 0, 0));
            imagens.add(mat);
            areas.add(0);
        }

        int labelValue;
        for (int i = 0; i < label.height(); i++) {
            for (int j = 0; j < label.width(); j++) {
                labelValue = (int) label.get(i, j)[0];
                if (labelValue != 0) {
                    areas.set(labelValue, areas.get(labelValue) + 1);
                    imagens.get(labelValue).put(i, j, 255);
                }
            }
        }
        
        for(int i = 0; i < c; i++){
            if(areas.get(i) < minArea){
                imagens.remove(i);
                areas.remove(i);
                i--; c--;
            }
        }
        
        for(int i = 0; i < c; i++){
            objetos.add( new Component(imagens.get(i), areas.get(i)) );
        }
        
        System.out.println("Finished");
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
    
    public static List<Component> getLocalLabels(Mat image) {
        Mat label = new Mat();
        List<Component> objetos = new ArrayList<>();


        int labelValue;
        int divisionSize = 4;
        
        // creates an image for each of the objects
        
        for(int y = 0; y < divisionSize; y++){
            for(int x = 0; x < divisionSize; x++){
                Mat sub = image.submat(y * image.height()/divisionSize ,  (y + 1 )* image.height()/divisionSize, 
                        x * image.width()/divisionSize ,  (x + 1 )* image.width()/divisionSize);
                int c = Imgproc.connectedComponents(sub, label, 4, CvType.CV_16U);
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
                        objetos.add(new Component(mat, size, x * image.width()/divisionSize, y * image.height()/divisionSize));
                    }
                }
            }
        }
        
        List<Component> listaFinal = new ArrayList<Component>();
        for(Component c : objetos){
            Mat m = new Mat(image.height(), image.width(), CvType.CV_8UC1, new Scalar(0, 0, 0));
            Mat m2 = c.getImage();
            for(int i = 0; i < m2.height(); i++){
                for(int j = 0; j < m2.width(); j++){
                    m.put(i + c.y, j + c.x, m2.get(i, j));
                }
            }
            listaFinal.add(new Component(m, c.getArea()));
        }

        return listaFinal;
    }
}