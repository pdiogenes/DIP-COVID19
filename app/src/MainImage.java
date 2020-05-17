
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import utilities.DeepCopy;
import utilities.Grayscale;

public class MainImage extends JComponent implements MouseListener, MouseMotionListener{
    private BufferedImage image, sampleImage;
    private Graphics2D g2;
    private String current_state;
    private Shape shape = null;
    Point startDrag, endDrag;
    Point sampleP1 = new Point(), sampleP2 = new Point();
    final MainMenu mm;

    // constructor for the canvas, adds the mouse listener and initiates the object
    // lists
    public MainImage(BufferedImage img, MainMenu m){
        mm = m;
        this.image = img;
        setDoubleBuffered(false);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    // paints the canvas whenever repaint() is called
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.image, 0, 0, null);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setStroke(new BasicStroke(2));
        g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.50f));

        if (shape != null) {
            g2.setPaint(Color.BLACK);
            g2.draw(shape);
            g2.setPaint(Color.YELLOW);
            g2.fill(shape);
        }
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

    private Rectangle2D.Float makeRectangle(int x1, int y1, int x2, int y2) {
        return new Rectangle2D.Float(Math.min(x1, x2), Math.min(y1, y2), Math.abs(x1 - x2), Math.abs(y1 - y2));
    }
    
    private void fixPoints(int x1, int y1, int x2, int y2){
        if(x1 > x2){
            sampleP1.x = x2;
            sampleP2.x = x1;
            if (y1 > y2){
                sampleP1.y = y2;
                sampleP2.y = y1;
            } else {
                sampleP1.y = y1;
                sampleP2.y = y2;
            }
        } else{
            sampleP1.x = x1;
            sampleP2.x = x2;
            if (y1 > y2){
                sampleP1.y = y2;
                sampleP2.y = y1;
            } else {
                sampleP1.y = y1;
                sampleP2.y = y2;
            }
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        startDrag = new Point(e.getX(), e.getY());
        endDrag = startDrag;
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (endDrag != null && startDrag != null && (endDrag != startDrag)) {
            try {
                shape = makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY());
                fixPoints(startDrag.x, startDrag.y, e.getX(), e.getY());
                BufferedImage subImage = image.getSubimage(sampleP1.x, sampleP1.y, sampleP2.x - sampleP1.x,
                        sampleP2.y - sampleP1.y);
                sampleImage = DeepCopy.copyImage(Grayscale.getGray(subImage));
                mm.updateSelectedRegion(sampleImage);
                startDrag = null;
                endDrag = null;
                repaint();
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        endDrag = new Point(e.getX(), e.getY());
        shape = makeRectangle(startDrag.x, startDrag.y, e.getX(), e.getY());
        repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
    
    

}
