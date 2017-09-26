package main.normaliser;

import main.display.DisplayableModule;
import main.interfaces.INormaliser;
import main.utils.Circle;
import main.utils.ImageData;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import static java.lang.Integer.min;
import static main.normaliser.NormaliserConstants.*;

/**
 * Created by Magda on 30/06/2017.
 */
public class OpenCVNormaliser extends DisplayableModule implements INormaliser {
    //Daugman's rubber sheet model
    //https://en.wikipedia.org/wiki/Bilinear_interpolation
    //https://www.ripublication.com/gjbmit/gjbmitv1n2_01.pdf -> publication with equations for normalisation

    public OpenCVNormaliser() {
        super(moduleName);
    }

    private ImageData adjustIrisEdges(ImageData imageData) {
        int irisRadius = IRIS_RADIUS;
        int pupilRadius = PUPIL_RADIUS;
        imageData.getFirstIrisCircle().setRadius(irisRadius);
        imageData.getFirstPupilCircle().setRadius(pupilRadius);

        if (imageData.getFirstPupilCircle().getRadius() > pupilRadius) {
            imageData.getFirstPupilCircle().setRadius(pupilRadius);
        }
        if (imageData.getFirstIrisCircle().getRadius() > irisRadius) {
            imageData.getFirstIrisCircle().setRadius(irisRadius);
        }

        return imageData;
    }

    @Override
    public ImageData normalize(ImageData imageData) {
        checkForInputErrors(imageData);

        //imageData = adjustIrisEdges(imageData);

        Mat imageMat = imageData.getImageMat();
        int rows = NORMALISED_IRIS_HEIGHT;
        int COL_s = NORMALISED_IRIS_WIDTH;
        int type = imageData.getImageMat().type();
        Mat normMat = new Mat(rows, COL_s, type);
        Circle pupil = imageData.getFirstPupilCircle();
        Circle iris = imageData.getFirstIrisCircle();

        int px, py;
        for (int r = 0; r < rows; r++) {
            for (int th = 0; th < COL_s; th++) {
                Point p = CoordinateConverter.toXY(r, th, pupil, iris, COL_s, rows);
                px = (int) Math.round(p.x);
                py = (int) Math.round(p.y);
                if (withinBounds(px, py, imageMat)) {
                    double[] pixel = imageMat.get(px, py);
                    normMat.put(r, th, pixel);
                }
            }
        }

        Imgproc.equalizeHist(normMat, normMat);
        Mat exclusionMap = buildExclusionMap(normMat);    // build map of exclusions (f.e. for eyelids)
        Mat filteredMat = linkMat(normMat, exclusionMap);
        // save to imageData and show
        showNormalisedArea(imageData);
        // imageData.setNormMat(normMat);
        imageData.setNormMat(filteredMat);
        imageData.setExclusionMap(exclusionMap);
        display.displayIf(normMat, displayTitle("normalised"));
        display.displayIf(exclusionMap, displayTitle("exclusion map"));
        display.displayIf(filteredMat, displayTitle("normalized with exclusion"));
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
        return p.x >= 0 && p.x < imageMat.width() && p.y >= 0 && p.y < imageMat.height();
    }

    private boolean withinBounds(int x, int y, Mat imageMat) {
        return x >= 0 && x < imageMat.width() && y >= 0 && y < imageMat.height();
    }

    private void showNormalisedArea(ImageData imageData) {
        Circle pupil = imageData.getFirstPupilCircle();
        Circle iris = imageData.getFirstIrisCircle();
        pupil.setX(iris.getX());
        pupil.setY(iris.getY());
        Mat image = imageData.getImageMat();

        display.displayIf(image, new Circle[]{pupil, iris}, displayTitle("area before normalisation"));
    }


    //build exclusion map eg for eyelids)
    private Mat buildExclusionMap(Mat map) {
        Mat m;
        Mat exclusionMat = new Mat(map.size(), map.type(), new Scalar(255)); //white
        m = new Mat(exclusionMat, new Rect(EXCLUDED_COL_1, EXCLUDED_ROW_1, EXCLUDED_WIDTH_1, EXCLUDED_HEIGHT_1));
        m.setTo(new Scalar(0));
        m = new Mat(exclusionMat, new Rect(EXCLUDED_COL_2, EXCLUDED_ROW_2, EXCLUDED_WIDTH_2, EXCLUDED_HEIGHT_2));
        m.setTo(new Scalar(0));
        m = new Mat(exclusionMat, new Rect(EXCLUDED_COL_3, EXCLUDED_ROW_3, EXCLUDED_WIDTH_3, EXCLUDED_HEIGHT_3));
        m.setTo(new Scalar(0));
        m = new Mat(exclusionMat, new Rect(EXCLUDED_COL_4, EXCLUDED_ROW_4, EXCLUDED_WIDTH_4, EXCLUDED_HEIGHT_4));
        m.setTo(new Scalar(0));
        return exclusionMat;
    }

    // normMat + exclusion template
    private Mat linkMat(Mat matA, Mat matB) {
        int rowMax = min(matA.rows(), matB.rows());
        int columnMax = min(matA.cols(), matB.cols());

        Mat mat = matA.clone();
        double[] zero = {0.};

        for (int i = 0; i < rowMax; ++i) {
            for (int j = 0; j < columnMax; ++j) {
                double[] b = matB.get(i, j);
                if (b[0] == 0)
                    mat.put(i, j, zero);
            }
        }
        return mat;
    }
}
