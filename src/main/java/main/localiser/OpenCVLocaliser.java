package main.localiser;

import main.display.DisplayableModule;
import main.interfaces.ILocaliser;
import main.utils.Circle;
import main.utils.ImageData;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Magda on 30/06/2017.
 */
public class OpenCVLocaliser extends DisplayableModule implements ILocaliser {

    private ImageData imageData;

    private void findPupil(Mat orgSrc) {
        //Mat src is a ROI out of the original image
        Mat src = orgSrc.clone();

        Imgproc.threshold(src, src, 0, 255, Imgproc.THRESH_OTSU);
        Imgproc.GaussianBlur(src, src, new Size(3, 3), 0, 0);

        display.displayIf(src, displayTitle("binarised pupil"));

        assert src.width() == src.height();
        int minRadius = (int) (0.1 * src.width());
        int maxRadius = (int) (0.8 * src.width());
        int minDistance = src.width();
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

        //TODO define behaviour when no circle found
        assert (circles.total() > 0); //total number of array elements

        //example use of hughcircles
        //https://github.com/badlogic/opencv-fun/blob/master/src/pool/tests/HoughCircles.java
        //TODO what about the other circles huh?
        Circle circle = new Circle(circles.get(0, 0));
        imageData.setPupilCircle(circle);

        display.displayIf(src, circles, displayTitle("pupil circle"));

    }

    private void findIris(Mat org_src) {
        //http://opencvlover.blogspot.com/2012/07/hough-circle-in-javacv.html
        //https://stackoverflow.com/questions/26867276/iris-and-pupil-detection-in-image-with-java-and-opencv

        Mat src = org_src.clone();

        Imgproc.threshold(src, src, 0, 255, Imgproc.THRESH_OTSU);
        Imgproc.GaussianBlur(src, src, new Size(3, 3), 0, 0);

        display.displayIf(src, displayTitle("binarised iris"));

        Mat circles = new Mat();

        //TODO adjust radius and min distance to image size?
        Imgproc.HoughCircles(
                src, //Input image
                circles, //Memory Storage
                Imgproc.HOUGH_GRADIENT, //Detection method
                2, //Inverse ratio
                100, //Minimum distance between the centers of the detected circles
                100, //Higher threshold for canny edge detector
                50, //Threshold at the center detection stage
                70, //min radius
                100 //max radius
        );

        assert (circles.total() > 0);

        Circle circle = new Circle(circles.get(0, 0));
        imageData.setIrisCircle(circle);

        display.displayIf(org_src, circles, displayTitle("iris circle"));

    }

    @Override
    public ImageData localise(ImageData imageData) {

        this.imageData = imageData;
        Mat src = imageData.getImageMat();
        assert src.channels() == 1; //greyscale

        findIris(src);
        Mat roi = rebaseToROI(imageData, src);
        findPupil(roi);
        showIrisAndPupil(imageData);

        return imageData;
    }

    private Mat rebaseToROI(ImageData imageData, Mat src) {
        Mat roi = applyMask(src, imageData.getIrisCircle());
        imageData.setImageMat(roi);
        Circle irisCircle = imageData.getIrisCircle();
        irisCircle.setX(irisCircle.getRadius());
        irisCircle.setY(irisCircle.getRadius());
        return roi;
    }

    private Mat circularAreaMask(Mat roi, int radius) {
        //https://stackoverflow.com/questions/18460053/how-to-black-out-everything-outside-a-circle-in-open-cv
        Mat mask = new Mat(roi.size(), roi.type(), new Scalar(0));
        Imgproc.circle(
                mask,
                new Point(radius, radius),
                radius,
                new Scalar(255.),
                -1);
        return mask;
    }

    private Mat applyMask(Mat src, Circle circle) {
        int x = (int) circle.getX();
        int y = (int) circle.getY();
        int r = (int) circle.getRadius();

        Rect roiRect = new Rect(x - r, y - r, r * 2, r * 2);
        Mat roi = new Mat(src, roiRect);
        Mat areaMask = circularAreaMask(roi, r);

        Mat maskedImage = new Mat(roi.size(), roi.type(), backgroundMaskColor(roi));
        roi.copyTo(maskedImage, areaMask);
        return maskedImage;
    }

    private Scalar backgroundMaskColor(Mat src) {
        return new Scalar((int) (Core.sumElems(src).val[0] / src.total()));
    }

    private void showIrisAndPupil(ImageData imageData) {
        Circle pupil = imageData.getPupilCircle();
        Circle iris = imageData.getIrisCircle();
        Mat circles = new MatOfPoint3(pupil.toPoint3(), iris.toPoint3()).t(); //transpose
        Mat image = imageData.getImageMat();

        display.displayIf(image, circles, displayTitle("iris and pupil circles"));
    }


}
