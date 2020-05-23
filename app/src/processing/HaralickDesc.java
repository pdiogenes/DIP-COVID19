/*import de.lmu.ifi.dbs.jfeaturelib.features.Haralick;;;
import de.lmu.ifi.dbs.utilities.Arrays2;
import ij.process.ColorProcessor;
import java.io.InputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;
import javax.imageio.ImageIO;


public class HaralickDesc{ 

    public static void main(String[] args) throws IOException, URISyntaxException {
        // load the image
        InputStream stream = HaralickDesc.class.getClassLoader().getResourceAsStream("test.jpg");//PASSAR AQUI O CAMINHO DO ARQUIVO PRA ABRIR COMO INPUTSTREAM AAAAAAAAAAAAAAAAAAAAAAAAA
        System.out.println(stream);
        ColorProcessor image = new ColorProcessor(ImageIO.read(stream));

        // initialize the descriptor
        Haralick descriptor = new Haralick();

        // run the descriptor and extract the features
        descriptor.run(image);
        String s = descriptor.getDescription();
        System.out.println(s);
        //System.out.println(s);
        // obtain the features
        List<double[]> features = descriptor.getFeatures();

        
        // print the features to system out
        for (double[] feature : features) {
            
            System.out.println(Arrays2.join(feature, ", ", "%.5f"));
        }
    }
}*/