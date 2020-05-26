package object;

import java.awt.image.BufferedImage;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

/**
 *
 * @author Pedro PIROGENIS
 */
public class Component {
    
    private double area;
    private double perimeter;
    private double circularity;
    private boolean isSample = false;
    private HaralickFeatures haralick;
    private Mat image;
    

    public Component(Mat image, double area){
        this.image = image;
        this.area = area;
        this.haralick = new HaralickFeatures((BufferedImage)HighGui.toBufferedImage(image));
    }

    public double getArea() {
        return area;
    }
    

    public double getCircularity() {
        return circularity;
    }

    public HaralickFeatures getHaralick() {
        return haralick;
    }

    public Mat getImage() {
        return image;
    }
    
    public void calculateFeatures(){
        // circularity
        double circularity = ( Math.pow(this.perimeter, 2) / (4 * Math.PI * this.area) );
        this.circularity = circularity;
    }
    
    public void setPerimeter(double perimeter){
        this.perimeter = perimeter;
    }
}
