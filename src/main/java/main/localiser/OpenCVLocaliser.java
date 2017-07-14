package main.localiser;

import main.display.DisplayableModule;
import main.interfaces.ILocaliser;
import main.utils.Circle;
import main.utils.ImageData;
import main.utils.ImageUtils;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

/**
 * Created by Magda on 30/06/2017.
 */
public class OpenCVLocaliser extends DisplayableModule implements ILocaliser {

    //TODO image mask

    private ImageData imageData;

    private Mat irisAreaMask(Mat src, Circle pupilCircle, double radius) {
        //https://stackoverflow.com/questions/18460053/how-to-black-out-everything-outside-a-circle-in-open-cv

        //TODO the color might not be right
        Mat mask = new Mat(src.rows(), src.cols(), src.type(), new Scalar(0, 0, 0, 0));
        Imgproc.circle(
                mask,
                pupilCircle.getCenter(),
                (int) radius,
                new Scalar(255., 255., 255., 255.), -1, 8, 0);
        return mask;
    }


    private double upperBoundOfIrisRadius(Circle pupilCircle) {
        //http://www3.nd.edu/~flynn/papers/HollingsworthBowyerFlynn_CVIU2008.pdf
        double pupilRadius = pupilCircle.getRadius();
        //pupil to iris radius - 0.2 to 0.7
        //TODO vary ratio until you get only one circle
        double maxRatio = 100 / 15;
        //double minRatio = 100/8;
        //double minIrisRadius = pupilRadius*minRatio;
        return pupilRadius * maxRatio;
        //TODO radius has to be inside bounds of image
    }

    private Circle circleClosestToTheMiddle(Mat src, Mat circles) {
        Point middle = new Point(src.rows(), src.cols());
        Circle mostCentralCircle = new Circle(circles.get(0, 0));
        double smallestDistanceToCenter = ImageUtils.distance(middle, mostCentralCircle.getCenter());
        Circle circle;
        Point circleCenter;
        double distance;
        for (int i = 1; i < circles.cols(); i++) {
            circle = new Circle(circles.get(0, i));
            circleCenter = circle.getCenter();
            distance = ImageUtils.distance(middle, circleCenter);
            if (distance < smallestDistanceToCenter) {
                smallestDistanceToCenter = distance;
                mostCentralCircle = circle;
            }
        }
        return mostCentralCircle;
    }

    private void findEyeArea(Mat src) {
        Mat gray = src.clone();
        Imgproc.GaussianBlur(gray, gray, new Size(3, 3), 0, 0);
        Mat circles = new Mat();

        int maxRadius = (int) (2. * Math.max(src.width(), src.height()) / 3.);
        int minRadius = (int) (maxRadius / 2.);

        Imgproc.HoughCircles(
                gray, //Input image
                circles, //Memory Storage
                Imgproc.HOUGH_GRADIENT, //Detection method
                1, //Inverse ratio of accumulator resolution
                50, //Minimum distance between the centers of the detected circles
                50, //Higher threshold for canny edge detector
                50, //Threshold at the center detection stage
                minRadius, //min radius
                maxRadius //max radius
        );

        display.displayIf(src, circles, displayTitle("eye area"));

        Circle circle = circleClosestToTheMiddle(src, circles);
        display.displayIf(src, new Circle[]{circle}, displayTitle("circle nearest center"));

    }

    private void findPupil(Mat src) {
        Mat gray = src.clone();

        Imgproc.GaussianBlur(gray, gray, new Size(3, 3), 0, 0);

        Imgproc.adaptiveThreshold(
                gray,
                gray,
                255,
                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                Imgproc.THRESH_BINARY,
                25, //block size
                6);

        Imgproc.GaussianBlur(gray, gray, new Size(3, 3), 0, 0);

        display.displayIf(gray, displayTitle("binarised pupil"));

        Mat circles = new Mat();
        Imgproc.HoughCircles(
                gray, //Input image
                circles, //Memory Storage
                Imgproc.HOUGH_GRADIENT, //Detection method
                1, //Inverse ratio
                //TODO make distance relative to the size of the photo
                50, //Minimum distance between the centers of the detected circles
                100, //Higher threshold for canny edge detector
                50, //Threshold at the center detection stage
                //TODO make radius relative to the size of the photo
                10, //min radius
                200 //max radius
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


    private void findIris(Mat src) {
        //http://opencvlover.blogspot.com/2012/07/hough-circle-in-javacv.html
        //https://stackoverflow.com/questions/26867276/iris-and-pupil-detection-in-image-with-java-and-opencv

        Mat gray = src.clone();

        Imgproc.GaussianBlur(gray, gray, new Size(3, 3), 0, 0);

        Imgproc.adaptiveThreshold(
                gray,
                gray,
                255,
                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                Imgproc.THRESH_BINARY,
                25, //block size
                6);

        Imgproc.GaussianBlur(gray, gray, new Size(3, 3), 0, 0);


        Mat circles = new Mat();

        Imgproc.HoughCircles(
                gray, //Input image
                circles, //Memory Storage
                Imgproc.HOUGH_GRADIENT, //Detection method
                2, //Inverse ratio
                100, //Minimum distance between the centers of the detected circles
                100, //Higher threshold for canny edge detector
                50, //Threshold at the center detection stage
                70, //min radius
                100 //max radius
        );


        //TODO check how this method fares
        //assert(circles.total() == 1);

        Circle circle = new Circle(circles.get(0, 0));
        imageData.setIrisCircle(circle);

        //TODO check if all these clone() are necessary
        display.displayIf(src, circles, displayTitle("iris circle"));

    }

    @Override
    public ImageData localise(ImageData imageData) {

        this.imageData = imageData;
        Mat src = imageData.getImageMat();
        assert src.channels() == 1; //greyscale

        //TODO include this
        //equalizeHist(src, src); //improve contrast

        findPupil(src);

        double radius = upperBoundOfIrisRadius(imageData.getPupilCircle());
        Mat irisAreaMask = irisAreaMask(src, imageData.getPupilCircle(), radius);

        Mat maskedImage = new Mat(src.size(), src.type(), new Scalar(0, 0, 0, 0));
        src.copyTo(maskedImage, irisAreaMask);

        findIris(maskedImage);

        showIrisAndPupil(imageData);

        return imageData;
    }

    private void showIrisAndPupil(ImageData imageData) {
        Circle pupil = imageData.getPupilCircle();
        Circle iris = imageData.getIrisCircle();
        Mat circles = new MatOfPoint3(pupil.toPoint3(), iris.toPoint3()).t(); //transpose
        Mat image = imageData.getImageMat();

        display.displayIf(image, circles, displayTitle("iris and pupil circles"));
    }

}
