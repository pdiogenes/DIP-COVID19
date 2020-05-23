package processing;

import java.awt.List;
import java.util.ArrayList;
import java.util.Random;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;

/**
 *
 * @author Vradoskein
 */
public class Labelling {
    // using OPENCV
    public static Mat rotulacao(Mat mat) {
        int rotulo = 0;
        Mat dst = new Mat();
        // Core.copyMakeBorder(mat, dst, 1, 1, 1, 1, Core.BORDER_CONSTANT, new Scalar(255, 255, 255));

        // int size = dst.height() * dst.width() * dst.channels();
        // byte[] px = new byte[size];
        // dst.get(0,0,px);
        // int[][] auxmat = new int[dst.height()][dst.width()];

        // // for(int i = 0; i < size ; i++){
        // //     System.out.print(" info " + px[i]);
        // // }

        // for (int row = 1; row < dst.height() - 1; row++) {
        //     for (int col = 1, int i = 0; col < dst.width() - 1; col++, i++) {
        //         if (px[i] == 0) {
        //             if (auxmat[row - 1][col] != 0) {
        //                 System.out.print("pos :[" +row"] ["+col"] [" + auxmat[row - 1][col] +"] ");
        //                 auxmat[row][col] = auxmat[row - 1][col];

        //             }
        //             if (auxmat[row - 1][col - 1] != 0) {
        //                 System.out.print("pos :[" +row"] ["+col"] [" +auxmat[row - 1][col - 1]+"] ");
        //                 auxmat[row][col] = auxmat[row - 1][col - 1];
        //             }
        //             if (auxmat[row][col - 1] != 0) {
        //                 System.out.print("pos :[" +row"] ["+col"] [" +auxmat[row][col - 1]+"] ");
        //                 auxmat[row][col] = auxmat[row][col - 1];
        //             }
        //             if (auxmat[row + 1][col - 1] != 0) {
        //                 System.out.print("pos :[" +row"] ["+col"] [" +auxmat[row + 1][col - 1]+"] ");
        //                 auxmat[row][col] = auxmat[row + 1][col - 1];
        //             }
        //             if (auxmat[row - 1][col] == 0 && auxmat[row - 1][col - 1] == 0 && auxmat[row][col - 1] == 0
        //                     && auxmat[row + 1][col - 1] == 0) {
        //                 rotulo++;
        //                 System.out.print(rotulo);
        //                 auxmat[row][col] = rotulo;
        //             }
        //         }
        //     }

        //     System.out.println();
        // }
        // // for (int y = 0; y < dst.height(); y++) {
        // //     for (int x = 0; x < dst.width(); x++) {
        // //         System.out.print(auxmat[x][y]);
        // //     }
        // //     System.out.println();
        // // }
        return dst;
    }
    
    public static Mat labelling(Mat src){
        //Finding Contours
        Random rand = new Random();
        double minContourArea = 50;
        ArrayList<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat();
        Imgproc.findContours(src, contours, hierarchy, Imgproc.RETR_TREE, Imgproc.CHAIN_APPROX_SIMPLE);
        Mat draw = Mat.zeros(src.size(), CvType.CV_8UC3);
        for (int i = 0; i < contours.size(); i++) {
            double cont_area = Imgproc.contourArea(contours.get(i));
            if(cont_area > minContourArea){
                System.out.println(cont_area);
                Imgproc.drawContours(draw, contours, i, new Scalar(rand.nextInt(255), rand.nextInt(255), rand.nextInt(255)), -1);
            }
        }
        return draw;
    }
}