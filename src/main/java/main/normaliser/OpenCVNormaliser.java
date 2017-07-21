package main.normaliser;

import main.display.DisplayableModule;
import main.interfaces.INormaliser;
import main.utils.Circle;
import main.utils.FilterConstants;
import main.utils.ImageData;
import org.opencv.core.Mat;
import org.opencv.core.Point;

/**
 * Created by Magda on 30/06/2017.
 */
public class OpenCVNormaliser extends DisplayableModule implements INormaliser {

    //TODO normaliser breaks in area outside image frame
    //TODO normalisation looks a bit off at the moment -> perhaps normalised image resolution is too large; check filter constants
    //TODO check if iris and pupil is included in imageData


    //Daugman's rubber sheet model
    //https://en.wikipedia.org/wiki/Bilinear_interpolation
    //https://www.ripublication.com/gjbmit/gjbmitv1n2_01.pdf -> publication with equations for normalisation

    @Override
    public ImageData normalize(ImageData imageData) {
        checkForInputErrors(imageData);

        FilterConstants filterStats = new FilterConstants();

        Mat imageMat = imageData.getImageMat();
        int rows = (int) filterStats.getTotalRows();
        int cols = (int) filterStats.getTotalCols();
        int type = imageData.getImageMat().type();
        //TODO I don't like this conversion - long to int
        int size = (int) (imageMat.total() * imageMat.step1(0));

        Mat normMat = new
                Mat(rows, cols, type);

        byte[] pxlArray = new byte[size];

        Circle pupil = imageData.getFirstPupilCircle();
        Circle iris = imageData.getFirstIrisCircle();

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

    private void checkForInputErrors(ImageData imageData) {
        assert imageData.getImageMat() != null;
        assert imageData.getImageMat().total() > 0;
        if (imageData.irisesFound() == 0)
            throw new UnsupportedOperationException("No iris found; can't normalise");
        if (imageData.pupilsFound() == 0)
            throw new UnsupportedOperationException("No pupil found; can't normalise");
    }

    private boolean withinBounds(Point p, Mat imageMat) {
        return p.x >= 0 && p.x < imageMat.rows() && p.y >= 0 && p.y < imageMat.cols();
    }

    private void showNormalisedArea(ImageData imageData) {
        Circle pupil = imageData.getFirstPupilCircle();
        Circle iris = imageData.getFirstIrisCircle();
        pupil.setX(iris.getX());
        pupil.setY(iris.getY());
        Mat image = imageData.getImageMat();

        display.displayIf(image, new Circle[]{pupil, iris}, displayTitle("area before normalisation"));
    }
}
