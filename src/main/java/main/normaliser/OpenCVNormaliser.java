package main.normaliser;

import main.Utils.Circle;
import main.Utils.FilterConstants;
import main.Utils.ImageData;
import main.Utils.ImageUtils;
import main.interfaces.INormaliser;
import main.writer.Display;
import org.opencv.core.*;

/**
 * Created by Magda on 30/06/2017.
 */
public class OpenCVNormaliser extends Display implements INormaliser {

    //Daugman's rubber sheet model
    //https://en.wikipedia.org/wiki/Bilinear_interpolation
    //https://www.ripublication.com/gjbmit/gjbmitv1n2_01.pdf -> publication with equations for normalisation

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }


    @Override
    public ImageData normalize(ImageData imageData) {
        FilterConstants filterStats = new FilterConstants();

        Mat imageMat = imageData.getImageMat();
        int rows = (int) filterStats.getTotalRows();
        int cols = (int) filterStats.getTotalCols();
        int type = imageData.getImageMat().type();
        //TODO I don't like this conversion - long to int
        int size = (int) (rows * cols * imageMat.step1(0));

        Mat normMat = new
                Mat(rows, cols, type);

        byte[] pxlArray = new byte[size];

        Circle pupil = imageData.getPupilCircle();
        Circle iris = imageData.getIrisCircle();

        for (int r = 0; r < rows; r++) {
            for (int th = 0; th < cols; th++) {
                Point p = CoordinateConverter.toXY(r, th, pupil, iris);

                assert withinBounds(p, imageMat);

                imageMat.get((int) p.x, (int) p.y, pxlArray);
                normMat.put(r, th, pxlArray);
            }
        }


        imageData.setNormMat(normMat);
        imageData.setFilterConstants(filterStats); //consts for filtering in Encoder

        showNormalisedArea(imageData);

        displayIf(normMat, "normalised", 1);
        return imageData;
    }

    private boolean withinBounds(Point p, Mat imageMat) {
        return p.x >= 0 && p.x < imageMat.rows() && p.y >= 0 && p.y < imageMat.cols();
    }

    private void showNormalisedArea(ImageData imageData) {
        Point3 pupil = imageData.getPupilCircle().toPoint3();
        Point3 iris = imageData.getIrisCircle().toPoint3();
        pupil.x = iris.x;
        pupil.y = iris.y;
        Mat circles = new MatOfPoint3(pupil, iris).t(); //transpose
        Mat image = imageData.getImageMat().clone();

        ImageUtils.drawCirclesOnImage(image, circles);
        displayIf(image, "area before norm", 3);
    }
}
