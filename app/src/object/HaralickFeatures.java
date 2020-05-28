package object;

import de.lmu.ifi.dbs.jfeaturelib.features.Haralick;
import ij.process.ColorProcessor;
import java.awt.image.BufferedImage;

/**
 *
 * @author Pedro
 */
public class HaralickFeatures {

    private double energia;
    private double contraste;
    //private double correlacao; 
    private double variancia;
    private double entropia;


    public HaralickFeatures(BufferedImage img) {
        ColorProcessor image = new ColorProcessor(img);
        Haralick descriptor = new Haralick();
        descriptor.run(image);
        double[] feats = descriptor.getFeatures().get(0);

        this.energia = feats[0];
        this.contraste = feats[1];
        //this.correlacao = feats[2];
        this.variancia = feats[3];
        this.entropia = feats[8];
    }

    public double getEnergia() {
        return energia;
    }
    
    public double getContraste() {
        return contraste;
    }
    /*public double getCorrelacao() {
        return correlacao;
    }*/
    public double getVariancia() {
        return variancia;
    }
    public double getEntropia() {
        return entropia;
    }
}
