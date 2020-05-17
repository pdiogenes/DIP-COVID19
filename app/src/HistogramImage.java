
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JComponent;
import net.sourceforge.chart2d.*;



public class HistogramImage extends JComponent{

    private BufferedImage image;
    private Graphics2D g2;

    // constructor for the canvas, adds the mouse listener and initiates the object
    // lists
    public HistogramImage(BufferedImage image) {
        //this.image = image;
    }

    // paints the canvas whenever repaint() is called
    protected void paintComponent(Graphics g) {
        /*
        if (null == null) {
            // image to draw null ==> we create
            image = new BufferedImage(500, 500, BufferedImage.TRANSLUCENT);
            g2 = (Graphics2D) image.getGraphics();
            // enable antialiasing
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            // clear draw area
            clear();
        }

        g.drawImage(image, 0, 0, null);
        */
    }

    // repaints white canvas
    public void clear() {
        g2.setPaint(Color.white);
        // draw white on entire draw area to clear
        g2.fillRect(0, 0, getSize().width, getSize().height);
        g2.setPaint(Color.black);
        repaint();
    }
    
    public LBChart2D createChart(BufferedImage gray) {
        // Chart2D configuration
        Object2DProperties object2DProps = new Object2DProperties();
        object2DProps.setObjectTitleText("Gray intensities histogram");
        Chart2DProperties chart2DProps = new Chart2DProperties();
        chart2DProps.setChartDataLabelsPrecision(-1);
        object2DProps.setObjectMagnifyWhenResize(false);
        LegendProperties legendProps = new LegendProperties();
        String[] legendLabels = {"Gray"};
        legendProps.setLegendLabelsTexts(legendLabels);
        GraphChart2DProperties graphChart2DProps = new GraphChart2DProperties();
        graphChart2DProps.setLabelsAxisTitleText("Gray");
        graphChart2DProps.setNumbersAxisTitleText("Count");
        graphChart2DProps.setNumbersAxisBetweenLabelsAndTicksGapExistence(false);
        GraphProperties graphProps = new GraphProperties();

        // Dataset
        int[] counts = new int[256];
        for (int r = 0; r < gray.getHeight(); r++) {
            for (int c = 0; c < gray.getWidth(); c++) {
                int v = (gray.getRGB(c, r) & 0xff);
                counts[v]++;
            }
        }
        Dataset dataset = new Dataset(1, counts.length, 1);
        for (int i = 0; i < counts.length; i++) {
            dataset.set(0, i, 0, counts[i]);
        }

        
        MultiColorsProperties multiColorsProps = new MultiColorsProperties();
        LBChart2D chart2D = new LBChart2D();
        chart2D.setObject2DProperties(object2DProps);
        chart2D.setChart2DProperties(chart2DProps);
        chart2D.setLegendProperties(legendProps);
        chart2D.setGraphChart2DProperties(graphChart2DProps);
        chart2D.addGraphProperties(graphProps);
        chart2D.addDataset(dataset);
        chart2D.addMultiColorsProperties(multiColorsProps);

        //Optional validation:  Prints debug messages if invalid only.
        if (!chart2D.validate(false)) {
            chart2D.validate(true);
        }
        return chart2D;
    }


   

}
