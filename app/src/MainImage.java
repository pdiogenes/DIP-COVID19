
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
import java.io.IOException;

import javax.swing.JComponent;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import processing.Labelling;
import processing.Threshold;
import utilities.DeepCopy;
import utilities.Grayscale;
import utilities.MatConversion;

public class MainImage extends JComponent implements MouseListener, MouseMotionListener, MouseWheelListener {
    // sampleImage is the selected sample of the image
    private BufferedImage image, sampleImage;
    private Graphics2D g2;
    private String current_state;
    // shape to draw the selected area with
    private Shape shape = null;
    Point startDrag, endDrag;
    Point sampleP1 = new Point(), sampleP2 = new Point();
    final MainMenu mm;
    double zoomFactor = 1;

    // constructor for the canvas, adds the mouse listeners
    public MainImage(BufferedImage img, MainMenu m) {
        mm = m;
        this.image = img;

        setDoubleBuffered(false);
        addMouseListener(this);
        addMouseMotionListener(this);
        addMouseWheelListener(this);
    }

    // paints the canvas whenever repaint() is called
    protected void paintComponent(Graphics g) {
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

    // retirar depois
    public void test() {
        Mat teste = null;
        try {
            teste = MatConversion.BufferedImage2Mat(Grayscale.getGray(sampleImage));
        } catch (IOException e) {
        }

        teste = Threshold.threshold(teste);
        teste = Labelling.rotulacao(teste);
        BufferedImage bteste = (BufferedImage) HighGui.toBufferedImage(teste);
        mm.drawSample(bteste);
        // BufferedImage t = Threshold.threshold(sampleImage);
        // mm.drawSample(t);
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
                BufferedImage subImage = image.getSubimage(sampleP1.x, sampleP1.y, sampleP2.x - sampleP1.x,
                        sampleP2.y - sampleP1.y);
                sampleImage = DeepCopy.copyImage(Grayscale.getGray(subImage));
                mm.drawSample(sampleImage);
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
