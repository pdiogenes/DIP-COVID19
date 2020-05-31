import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import java.util.List;
import java.util.ArrayList;

import javax.swing.JComponent;

import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;

import object.Component;
import processing.CrossCorrelation;
import processing.LBP;
import processing.Labelling;
import result.Result;

/**
 *
 * @author Pedro
 */
public class MainImage extends JComponent implements MouseListener, MouseMotionListener, MouseWheelListener {
    // sampleImage is the selected sampleMat of the image
    private BufferedImage image, sampleImage;
    private Graphics2D g2;
    private String current_state;

    // shape to draw the selected area with
    private Shape shape = null;
    Point startDrag, endDrag;
    Point sampleP1 = new Point(), sampleP2 = new Point();
    final MainMenu mm;
    double zoomFactor = 1;
    List<Component> sampleLabels = new ArrayList<Component>();
    List<Component> allObjects = new ArrayList<Component>();

    private Mat sampleT, img, imgThresh, sampleMat;

    // constructor for the canvas, adds the mouse listeners
    public MainImage(Mat img, MainMenu m) {
        mm = m;
        this.img = img.clone();
        this.image = (BufferedImage) HighGui.toBufferedImage(img);

        setDoubleBuffered(false);
        addMouseWheelListener(this);
    }
    
    public void addListeners(){
        addMouseListener(this);
        addMouseMotionListener(this);
    }
    
    public String getState() {
        return current_state;
    }

    public void setState(String current_state) {
        this.current_state = current_state;
    }

    // paints the canvas whenever repaint() is called
    protected void paintComponent(Graphics g) {
        // for the zoom functionality
        // https://stackoverflow.com/questions/33925884/zoom-in-and-out-of-jpanel
        
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // zoom in and out of image
        AffineTransform at = new AffineTransform();
        at.scale(zoomFactor, zoomFactor);
        g2.transform(at);

        g2.drawImage(this.image, 0, 0, null);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setStroke(new BasicStroke(2));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));

        // draws a square on the selected area
        if (shape != null) {
            g2.setPaint(Color.BLACK);
            g2.draw(shape);
            g2.setPaint(Color.RED);
            g2.fill(shape);
        }
    }

    void setSampleMat(Mat mat, Mat imgT) {
        this.sampleT = mat.clone();
        this.imgThresh = imgT.clone();
        BufferedImage s = (BufferedImage) HighGui.toBufferedImage(sampleT);
        mm.drawSample(s);
        this.init();
        
        switch(this.current_state){
            case "LBP":
                this.lbp();
                break;
            case "Cross":
                this.cross();
                break;
            case "Haralick":
                this.haralick();
                break;
            case "Circ":
                this.circ();
                break;
            case "Area":
                this.area();
                break;
            default: break;
        }
    }

    // opens the last saved image

    private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {
        return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }

    private void fixPoints(int x1, int y1, int x2, int y2) {
        if (x1 > x2) {
            sampleP1.x = x2;
            sampleP2.x = x1;
            if (y1 > y2) {
                sampleP1.y = y2;
                sampleP2.y = y1;
            } else {
                sampleP1.y = y1;
                sampleP2.y = y2;
            }
        } else {
            sampleP1.x = x1;
            sampleP2.x = x2;
            if (y1 > y2) {
                sampleP1.y = y2;
                sampleP2.y = y1;
            } else {
                sampleP1.y = y1;
                sampleP2.y = y2;
            }
        }
    }

    private double getZoomFactor() {
        return this.zoomFactor;
    }

    private void setZoomFactor(double factor) {
        if (factor < this.zoomFactor) {
            this.zoomFactor = this.zoomFactor / 1.1;
        } else {
            this.zoomFactor = factor;
        }
    }


    public void lbp(){
        List<Component> detectedObjects = new ArrayList<Component>();
        
        Component sampleObject = sampleLabels.get(0);
        Mat sampleLBPH = LBP.calcLBP(sampleObject.getOriginal());
        
        for(int i = 0; i < allObjects.size(); i++){
            Component c = allObjects.get(i);
            Mat objectLBPH = LBP.calcLBP(c.getOriginal());
            double comparison = LBP.compareLBP(sampleLBPH, objectLBPH);
            
            if(comparison > 0.95){
                detectedObjects.add(c);
            }
        }

        Mat resultado = Result.resultado(detectedObjects, img);
        BufferedImage s = (BufferedImage) HighGui.toBufferedImage(resultado);
        mm.show_result(resultado, detectedObjects.size());

    }
    
    public void cross(){
        List<Component> detectedObjects = new ArrayList<Component>();
        
        Component sampleObject = sampleLabels.get(0);
        for(int i = 0; i < allObjects.size(); i++){
            Component c = allObjects.get(i);
            
            boolean comparison = CrossCorrelation.match(c, sampleObject);
            if(comparison){
                detectedObjects.add(c);
            }
        }
        
        Mat resultado = Result.resultado(detectedObjects, img);
        BufferedImage s = (BufferedImage) HighGui.toBufferedImage(resultado);
        mm.show_result(resultado, detectedObjects.size());
    }
    
    public void haralick(){
        List<Component> detectedObjects = new ArrayList<Component>();
        
        Component sampleObject = sampleLabels.get(0);
        for(int i = 0; i < allObjects.size(); i++){
            Component c = allObjects.get(i);
            
            if(c.compareFeatures(sampleObject)){
                detectedObjects.add(c);
            }
            System.out.println("");
        }
        
        
        Mat resultado = Result.resultado(detectedObjects, img);
        BufferedImage s = (BufferedImage) HighGui.toBufferedImage(resultado);
        mm.show_result(resultado, detectedObjects.size());
    }
    
    public void circ(){
        List<Component> detectedObjects = new ArrayList<Component>();
        
        Component sampleObject = sampleLabels.get(0);
        for(int i = 0; i < allObjects.size(); i++){
            Component c = allObjects.get(i);
            double diff = Math.abs(c.getCircularity() - sampleObject.getCircularity());
            
            if(diff < 1){
                detectedObjects.add(c);
            }
        }
        
        Mat resultado = Result.resultado(detectedObjects, img);
        BufferedImage s = (BufferedImage) HighGui.toBufferedImage(resultado);
        mm.show_result(resultado, detectedObjects.size());
    }
    
    public void area(){
        List<Component> detectedObjects = new ArrayList<Component>();
        
        Component sampleObject = sampleLabels.get(0);
        for(int i = 0; i < allObjects.size(); i++){
            Component c = allObjects.get(i);
            double diff = Math.abs(c.getArea() - sampleObject.getArea());
            if(diff < 300){
                detectedObjects.add(c);
            }
        }
        
        Mat resultado = Result.resultado(detectedObjects, img);
        BufferedImage s = (BufferedImage) HighGui.toBufferedImage(resultado);
        mm.show_result(resultado, detectedObjects.size());
    }
    
    public void all(){
        List<Component> detectedCross = new ArrayList<Component>();
        List<Component> detectedCrossLbp = new ArrayList<Component>();
        List<Component> detectedCrossLbpHaralick = new ArrayList<Component>();
        
        Component sampleObject = sampleLabels.get(0);
        
        for(int i = 0; i < allObjects.size(); i++){
            Component c = allObjects.get(i);
            
            boolean comparison = CrossCorrelation.match(c, sampleObject);
            if(comparison){
                detectedCross.add(c);
            }
        }
        
        Mat sampleLBPH = LBP.calcLBP(sampleObject.getOriginal());
        
        for(int i = 0; i < detectedCross.size(); i++){
            Component c = detectedCross.get(i);
            Mat objectLBPH = LBP.calcLBP(c.getOriginal());
            double comparison = LBP.compareLBP(sampleLBPH, objectLBPH);
            
            if(comparison > 0.95){
                detectedCrossLbp.add(c);
            }
        }
        
        for(int i = 0; i < detectedCrossLbp.size(); i++){
            Component c = detectedCrossLbp.get(i);
            
            if(c.compareFeatures(sampleObject)){
                detectedCrossLbpHaralick.add(c);
            }
            System.out.println("");
        }
        
        Mat resultado = Result.resultado(detectedCrossLbpHaralick, img);
        BufferedImage s = (BufferedImage) HighGui.toBufferedImage(resultado);
        mm.show_result(resultado, detectedCrossLbpHaralick.size());
    }
    
    public void init() {
        // gets labels for objects
        sampleLabels = Labelling.getLabels(sampleT);
        Component sampleObject = sampleLabels.get(0);
        int minArea = (int) (sampleObject.getArea()/2.5);
        allObjects = Labelling.getLabels2(imgThresh, minArea);
        
        for(Component smpl : sampleLabels){
            smpl.setOriginal(Labelling.getImagesForLabel(smpl.getImage(), sampleMat));
            smpl.calculateFeatures();
        }
        
        for(Component c : allObjects){
            c.setOriginal(Labelling.getImagesForLabel(c.getImage(), img));
            c.calculateFeatures();
        }
        
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        int newX = (int) (e.getX() / zoomFactor);
        int newY = (int) (e.getY() / zoomFactor);
        startDrag = new Point(newX, newY);
        endDrag = startDrag;
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        int newX = (int) (e.getX() / zoomFactor);
        int newY = (int) (e.getY() / zoomFactor);

        if (newX < 0)
            newX = 0;
        else if (newX > this.getWidth())
            newX = this.getWidth();

        if (newY < 0)
            newY = 0;
        else if (newY > this.getHeight())
            newY = this.getHeight();

        if (endDrag != null && startDrag != null && (endDrag != startDrag)) {
            try {
                shape = makeRectangle(startDrag.x, startDrag.y, newX, newY);
                fixPoints(startDrag.x, startDrag.y, newX, newY);
                sampleMat = img.submat(sampleP1.y, sampleP2.y, sampleP1.x, sampleP2.x);
                sampleImage = (BufferedImage) HighGui.toBufferedImage(sampleMat);
                mm.drawSample(sampleImage);
                mm.changeButtonState(true);
                startDrag = null;
                endDrag = null;
                repaint();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        int newX = (int) (e.getX() / zoomFactor);
        int newY = (int) (e.getY() / zoomFactor);

        if (newX < 0)
            newX = 0;
        else if (newX > this.getWidth())
            newX = this.getWidth();

        if (newY < 0)
            newY = 0;
        else if (newY > this.getHeight())
            newY = this.getHeight();

        endDrag = new Point(newX, newY);
        shape = makeRectangle(startDrag.x, startDrag.y, newX, newY);
        repaint();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        // Zoom in
        if (e.getWheelRotation() < 0) {
            setZoomFactor(1.1 * getZoomFactor());
            repaint();
        }
        // Zoom out
        if (e.getWheelRotation() > 0) {
            setZoomFactor(getZoomFactor() / 1.1);
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

}
