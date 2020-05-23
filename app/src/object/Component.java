package object;

import org.opencv.core.Mat;

/**
 *
 * @author Pedro
 */
public class Component {
    
    private double area;
    private double perimeter;
    private double circularity;
    private HaralickFeatures haralick;
    private Mat image;

    public Component(Mat image, double area){
        this.image = image;
        this.area = area;
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
