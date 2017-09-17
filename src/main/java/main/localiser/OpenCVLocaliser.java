package main.localiser;

import main.display.DisplayableModule;
import main.interfaces.ILocaliser;
import main.utils.Circle;
import main.utils.ImageData;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import static java.lang.Math.min;
import static org.opencv.core.Core.BORDER_CONSTANT;
import static org.opencv.core.Core.copyMakeBorder;
import static org.opencv.imgproc.Imgproc.Canny;
import static org.opencv.imgproc.Imgproc.equalizeHist;

/**
 * Created by Magda on 30/06/2017.
 */
public class OpenCVLocaliser extends DisplayableModule implements ILocaliser {

    //note: white areas (reflections) are filled with color

    public OpenCVLocaliser() {
        super(moduleName);
    }

    private ImageData imageData;

    @Override
    public ImageData localise(ImageData imageData) {

        this.imageData = imageData;

        assert imageData.getImageMat() != null;

        Mat src = imageData.getImageMat();

        assert src.channels() == 1; //greyscale
        assert src.width() > 0 && src.height() > 0;

        src = removeReflections(src);
        boolean foundPupil = findPupil(src);
        if (foundPupil) {
            Mat roi = rebaseToROI(imageData, src);
            boolean foundIris = findIris(roi);
            if (foundIris) {
                showIrisAndPupil(imageData);
            }
        }

        return imageData;
    }

    private boolean findPupil(Mat orgSrc) {
        Mat src = orgSrc.clone();

        int threshold = 70;
        Canny(src, src, threshold, threshold * 2);
        Imgproc.GaussianBlur(src, src, new Size(3, 3), 0, 0);

        //radius depends on whether findPupil is used before or after findIris
        int minRadius, maxRadius, minDistance;
        if (src.width() == src.height()) {
            minRadius = (int) (0.1 * src.width());
            maxRadius = (int) (0.8 * src.width());
            minDistance = src.width();
        } else {
            minRadius = 10;
            maxRadius = min(src.width(), src.height()) / 2;
            minDistance = min(src.width(), src.height());
        }
        Mat circles = new Mat();
        Imgproc.HoughCircles(
                src, //Input image
                circles, //Memory Storage
                Imgproc.HOUGH_GRADIENT, //Detection method
                2, //Inverse ratio
                minDistance,
                100, //Higher threshold for canny edge detector
                50, //Threshold at the center detection stage
                minRadius,
                maxRadius
        );

        //example use of hughcircles
        //https://github.com/badlogic/opencv-fun/blob/master/src/pool/tests/HoughCircles.java
        int length = circles.cols();
        for (int i = 0; i < length; i++) {
            Circle circle = new Circle(circles.get(0, i));
            imageData.addPupilCircle(circle);
        }

        display.displayIf(src, circles, displayTitle("pupil circle"));

        return circles.cols() > 0;
    }

    private boolean findIris(Mat org_src) {
        Mat src = org_src.clone();

        equalizeHist(src, src);
        int threshold = 70;
        Canny(src, src, threshold, threshold * 2);
        Imgproc.GaussianBlur(src, src, new Size(3, 3), 0, 0);

        display.displayIf(src, displayTitle("canny iris"));

        Mat circles = new Mat();

        //TODO adjust radius and min distance to image size?
        Imgproc.HoughCircles(
                src, //Input image
                circles, //Memory Storage
                Imgproc.HOUGH_GRADIENT, //Detection method
                2, //Inverse ratio
                100, //Minimum distance between the centers of the detected circles
                150, //Higher threshold for canny edge detector
                120, //Threshold at the center detection stage
                70, //min radius
                min(src.width(), src.height()) //max radius
        );

        for (int i = 0; i < circles.cols(); i++)
            imageData.addIrisCircle(new Circle(circles.get(0, i)));

        display.displayIf(org_src, circles, displayTitle("iris circle"));

        return circles.cols() > 0;
    }

    private Mat removeReflections(Mat src) {
        //TODO could make this more efficient with a byte array
        Scalar color = generateBackgroundColor(src);
        for (int i = 0; i < src.rows(); i++) {
            for (int j = 0; j < src.width(); j++) {
                double[] org_color = src.get(i, j);
                if (org_color[0] > 200)
                    src.put(i, j, color.val[0]);
            }
        }
        return src;
    }

    private Mat rebaseToROI(ImageData imageData, Mat src) {
        Circle maskCircle = imageData.getFirstPupilCircle().copy();
        maskCircle.setRadius(min(
                min(imageData.getImageMat().width(), imageData.getImageMat().height()),
                maskCircle.getRadius() * 4.));

        //correcting opencv error in step1
        Mat roi_step_error = focusOnArea(src, maskCircle);
        Mat roi = new Mat(roi_step_error.size(), roi_step_error.type());
        roi_step_error.copyTo(roi);

        imageData.setImageMat(roi);

        Circle pupilCircle = imageData.getFirstPupilCircle();
        pupilCircle.setX(maskCircle.getRadius());
        pupilCircle.setY(maskCircle.getRadius());

        return roi;
    }

    private Mat focusOnArea(Mat src, Circle circle) {
        final int r = (int) circle.getRadius();
        final int x = (int) circle.getX();
        final int y = (int) circle.getY();

        int height = src.height();
        int width = src.width();

        int right = (x + r >= width) ? x + r - width + 1 : 0;
        //right and bottom boundaries are NOT inclusive
        int bottom = (y + r >= height) ? y + r - height + 1 : 0;
        int top = 0, left = 0; //origin point; boundaries are inclusive
        if (r > x) {
            left = r - x;
            //adjust coordinates
            circle.setX(x + left);
        }
        if (r > y) {
            top = r - y;
            circle.setY(y + top);
        }

        //TODO don't make border if it's not necessary
        //prevent out of bounds error
        Mat dst = new Mat(src.height() + top + bottom, src.width() + left + right, src.type());
        Scalar color = generateBackgroundColor(src);
        copyMakeBorder(src, dst, top, bottom, left, right, BORDER_CONSTANT, color);

        int adjX = x + left - r; //-r because Rect is initialised with coordinates of its top left corner
        int adjY = y + top - r;
        int size = 2 * r;
        return new Mat(dst, new Rect(adjX, adjY, size, size));
    }

    private Scalar generateBackgroundColor(Mat src) {
        return new Scalar((int) ((Core.sumElems(src).val[0] / src.total())));
    }

    private void showIrisAndPupil(ImageData imageData) {
        Circle pupil = imageData.getFirstPupilCircle();
        Circle iris = imageData.getFirstIrisCircle();
        Mat circles = new MatOfPoint3(pupil.toPoint3(), iris.toPoint3()).t(); //transpose
        Mat image = imageData.getImageMat();

        Mat markedImage = display.displayIf(image, circles, displayTitle("iris and pupil circles"));
        imageData.setImageWithMarkedCircles(markedImage);
    }


}
