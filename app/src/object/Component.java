package object;

import java.awt.image.BufferedImage;
import java.util.ArrayList;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
/**
 *
 * @author Pedro
 */
public class Component {

    private double area;
    private double perimeter;
    private double circularity;
    private boolean isSample = false;
    private HaralickFeatures haralick;
    private Mat image, original;
    public int x, y;

    public Component(Mat image, double area) {
        this.image = image;
        this.area = area;
    }
    
    public Component(Mat image, double area, int x, int y) {
        this.image = image;
        this.area = area;
        this.x = x;
        this.y = y;
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

    public void setPerimeter(double perimeter) {
        this.perimeter = perimeter;
    }

    public void setOriginal(Mat og) {
        this.original = og.clone();
    }
    
    public Mat getOriginal(){
        return this.original;
    }

    public boolean compareFeatures(Component c) {
        int matches = 0;
        
         System.out.println("Energia: " + this.haralick.getEnergia() + " x " + c.haralick.getEnergia());
        if( Math.abs(this.haralick.getEnergia() - c.haralick.getEnergia()) < 1){
            matches++;
        }
        
         System.out.println("Contrasete: " + this.haralick.getContraste() + " x " + c.haralick.getContraste());
        if( Math.abs(this.haralick.getContraste() - c.haralick.getContraste()) < 2){
            matches++;
        }
        
         System.out.println("Variância: " + this.haralick.getVariancia() + " x " + c.haralick.getVariancia());
        if( Math.abs(this.haralick.getVariancia() - c.haralick.getVariancia()) < 1){
            matches++;
        }
        
         System.out.println("Entropia: " + this.haralick.getEntropia() + " x " + c.haralick.getEntropia());
        if( Math.abs(this.haralick.getEntropia() - c.haralick.getEntropia()) < 1){
            matches++;
        }
        
        if (matches >= 3) return true;
        else return false;
    }

    public void calculateFeatures(){
        // finds contour of object to find its arc length
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(this.getImage(), contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        
        // each object only has 1 contour so we fetch it
        MatOfPoint objContour = contours.get(0);
        
        // perimeter
        double cont_perimeter = Imgproc.arcLength(new MatOfPoint2f(contours.get(0).toArray()), true);
        this.perimeter = cont_perimeter;
        
        // circularity
        double circularity = (Math.pow(this.perimeter, 2) / (4 * Math.PI * this.area));
        this.circularity = circularity;

        // haralick 
        this.haralick = new HaralickFeatures((BufferedImage) HighGui.toBufferedImage(this.original));
    }
}
