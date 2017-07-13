package main.localiser;

import main.display.DisplayableModule;
import main.interfaces.ILocaliser;
import main.utils.Circle;
import main.utils.ImageData;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint3;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
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

    private void findPupil(Mat src) {
        Mat gray = src.clone();
//        Imgproc.cvtColor(gray, gray, Imgproc.COLOR_BGR2GRAY);

        Imgproc.GaussianBlur(gray, gray, new Size(3, 3), 0, 0);

        Imgproc.adaptiveThreshold(
                gray,
                gray,
                255,
                Imgproc.ADAPTIVE_THRESH_GAUSSIAN_C,
                Imgproc.THRESH_BINARY,
                25, //block size
                6);

        display.displayIf(gray, displayTitle("binarised pupil"));

        Imgproc.GaussianBlur(gray, gray, new Size(3, 3), 0, 0);

        Mat circles = new Mat();

        Imgproc.HoughCircles(
                gray, //Input image
                circles, //Memory Storage
                Imgproc.HOUGH_GRADIENT, //Detection method
                1, //Inverse ratio
                50, //Minimum distance between the centers of the detected circles
                100, //Higher threshold for canny edge detector
                50, //Threshold at the center detection stage
                10, //min radius
                100 //max radius
        );

        //assert(circles.total() == 1);

        //example use of hughcircles
        //https://github.com/badlogic/opencv-fun/blob/master/src/pool/tests/HoughCircles.java
        //getting first circle only for now
        //TODO what about the other circles huh?
        Circle circle = new Circle(circles.get(0, 0));
        imageData.setPupilCircle(circle);

        Mat src_copy = src.clone();
        display.displayImageWithCirclesIf(src_copy, circles, displayTitle("pupil circle"));

    }


    private void findIris(Mat src) {
        //http://opencvlover.blogspot.com/2012/07/hough-circle-in-javacv.html
        //https://stackoverflow.com/questions/26867276/iris-and-pupil-detection-in-image-with-java-and-opencv

        Mat gray = src.clone();
        //Imgproc.cvtColor(gray, gray, Imgproc.COLOR_BGR2GRAY);

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

        Mat src_copy = src.clone();
        display.displayImageWithCirclesIf(src_copy, circles, displayTitle("iris circle"));

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
        Mat image = imageData.getImageMat().clone();

        display.displayImageWithCirclesIf(image, circles, displayTitle("iris and pupil circles"));
    }

}
