
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.event.MouseInputListener;
import processing.ImageResize;



public class MainImage extends JComponent implements MouseInputListener {

    // creates lists for the created and clipped objets
    // Image in which we're going to draw
    private BufferedImage image;
    // Graphics2D object ==> used to draw on
    private Graphics2D g2;
    // current and old points, used for the plotting methods
    private Point current_point = null, old_point = null;
    boolean clicked = false;
    // tells the app which plotting method it should use
    private String current_state;

    // object counter
    private static int num_objetos;
    private static int num_objetos_recortado;

    // constructor for the canvas, adds the mouse listener and initiates the object
    // lists
    public MainImage(BufferedImage img) {
        this.image = img;
        setDoubleBuffered(false);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // paints the canvas whenever repaint() is called
    protected void paintComponent(Graphics g) {
        BufferedImage newImg = ImageResize.resize(this.image, getSize().width, getSize().height);
        g.drawImage(newImg, 0, 0, null);
    }

    // repaints white canvas
    public void clear() {
        g2.setPaint(Color.white);
        // draw white on entire draw area to clear
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.setPaint(Color.black);
        repaint();
    }

    // changes colour to red
    public void black() {
        g2.setPaint(Color.black);
    }

    // changes app state (tells it which functionality it should execute)
    void setState(String state) {
        this.current_state = state;
    }

    // returns current state
    String getState() {
        return this.current_state;
    }

    // opens the last saved image


    /*
     * handles object re-drawing (after transformation or after opening a file)
     */
   

    // unused mouseListener events
    public void mouseMoved(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
