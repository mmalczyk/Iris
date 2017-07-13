package main.normaliser;

import main.display.DisplayableModule;
import main.interfaces.INormaliser;
import main.utils.Circle;
import main.utils.FilterConstants;
import main.utils.ImageData;
import main.utils.ImageUtils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint3;
import org.opencv.core.Point;
import org.opencv.core.Point3;

/**
 * Created by Magda on 30/06/2017.
 */
public class OpenCVNormaliser extends DisplayableModule implements INormaliser {

    //TODO normaliser breaks in area outside image frame
    //TODO normalisation looks a bit off at the moment -> perhaps normalised image resolution is too large; check filter constants

    //Daugman's rubber sheet model
    //https://en.wikipedia.org/wiki/Bilinear_interpolation
    //https://www.ripublication.com/gjbmit/gjbmitv1n2_01.pdf -> publication with equations for normalisation

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
        imageData.setFilterConstants(filterStats);

        showNormalisedArea(imageData);

        display.displayIf(normMat, displayTitle("normalised"));
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
        display.displayIf(image, displayTitle("area before normalisation"));
    }
}
