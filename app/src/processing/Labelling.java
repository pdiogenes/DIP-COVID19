package processing;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;

/**
 *
 * @author Vradoskein
 */
public class Labelling {
    // using OPENCV
    // public static Mat rotulacao(Mat mat) {
    //     int rotulo = 0;
    //     Mat dst = new Mat();
    //     Core.copyMakeBorder(mat, dst, 1, 1, 1, 1, Core.BORDER_CONSTANT, new Scalar(255, 255, 255));

    //     int[][] auxmat = new int[dst.height()][dst.width()];

    //     for (int y = 1; y < dst.height() - 1; y++) {
    //         for (int x = 1; x < dst.width() - 1; x++) {
    //             if (dst.get(x, y)[0] == 0) {
    //                 if (auxmat[x - 1][y] != 0) {
    //                     System.out.print(auxmat[x - 1][y]);
    //                     auxmat[x][y] = auxmat[x - 1][y];

    //                 }
    //                 if (auxmat[x - 1][y - 1] != 0) {
    //                     System.out.print(auxmat[x - 1][y - 1]);
    //                     auxmat[x][y] = auxmat[x - 1][y - 1];
    //                 }
    //                 if (auxmat[x][y - 1] != 0) {
    //                     System.out.print(auxmat[x][y - 1]);
    //                     auxmat[x][y] = auxmat[x][y - 1];
    //                 }
    //                 if (auxmat[x + 1][y - 1] != 0) {
    //                     System.out.print(auxmat[x + 1][y - 1]);
    //                     auxmat[x][y] = auxmat[x + 1][y - 1];
    //                 }
    //                 if (auxmat[x - 1][y] == 0 && auxmat[x - 1][y - 1] == 0 && auxmat[x][y - 1] == 0
    //                         && auxmat[x + 1][y - 1] == 0) {
    //                     rotulo++;
    //                     System.out.print(rotulo);
    //                     auxmat[x][y] = rotulo;
    //                 }
    //             }
    //         }
    //         System.out.println("line");
    //         System.out.println();
    //     }
    //     for (int y = 0; y < dst.height(); y++) {
    //         for (int x = 0; x < dst.width(); x++) {
    //             System.out.print(auxmat[x][y]);
    //         }
    //         System.out.println();
    //     }
    //     return dst;
    // }
}