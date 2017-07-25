package main.localiser;

import main.display.DisplayableModule;
import main.interfaces.ILocaliser;
import main.utils.Circle;
import main.utils.ImageData;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;

import static java.lang.Math.min;
import static org.opencv.imgproc.Imgproc.Canny;
import static org.opencv.imgproc.Imgproc.equalizeHist;

/**
 * Created by Magda on 30/06/2017.
 */
public class OpenCVLocaliser extends DisplayableModule implements ILocaliser {

    //note: white areas (reflections) are filled with color

    private ImageData imageData;

    @Override
    public ImageData localise(ImageData imageData) {

        this.imageData = imageData;
        Mat src = imageData.getImageMat();
        assert src.channels() == 1; //greyscale

        src = removeReflections(src);
        boolean foundPupil = findPupil(src);
        if (foundPupil) {
            Mat roi = rebaseToROI(imageData, src);
            //imageData.resetPupils();
            boolean foundIris = findIris(roi);
            if (foundIris) {
//                findPupil(roi);
                showIrisAndPupil(imageData);
            }
        }

        return imageData;
    }

    private boolean findPupil(Mat orgSrc) {
        //Mat src is a ROI out of the original image
        Mat src = orgSrc.clone();


        int threshold = 70;
        Canny(src, src, threshold, threshold * 2);
//        Imgproc.threshold(src, src, 0, 255, Imgproc.THRESH_OTSU);
        Imgproc.GaussianBlur(src, src, new Size(3, 3), 0, 0);

        //display.displayIf(src, displayTitle("canny pupil"));
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

//        assert (circles.total() > 0); //total number of array elements

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
        //http://opencvlover.blogspot.com/2012/07/hough-circle-in-javacv.html
        //https://stackoverflow.com/questions/26867276/iris-and-pupil-detection-in-image-with-java-and-opencv

        Mat src = org_src.clone();
/*
        Imgproc.threshold(src, src, 0, 255, Imgproc.THRESH_OTSU);
        Imgproc.GaussianBlur(src, src, new Size(3, 3), 0, 0);
*/
        equalizeHist(src, src);
        int threshold = 70;
        Canny(src, src, threshold, threshold * 2);
        Imgproc.GaussianBlur(src, src, new Size(3, 3), 0, 0);

        display.displayIf(src, displayTitle("canny iris"));

/*

        Imgproc.threshold(src, src, 0, 255, Imgproc.THRESH_OTSU);
        Imgproc.GaussianBlur(src, src, new Size(3, 3), 0, 0);

        display.displayIf(src, displayTitle("binarised iris"));

*/


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

//        assert (circles.total() > 0);

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
        //TODO remove that circular mask

        Circle maskCircle = imageData.getFirstPupilCircle().copy();
        maskCircle.setRadius(min(
                min(imageData.getImageMat().width(), imageData.getImageMat().height()),
                maskCircle.getRadius() * 4.)); //maybe it should be 8.
        Mat roi = applyMask(src, maskCircle);
        imageData.setImageMat(roi);

        Circle pupilCircle = imageData.getFirstPupilCircle();
        pupilCircle.setX(maskCircle.getRadius());
        pupilCircle.setY(maskCircle.getRadius());

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
        try {
            Mat roi = new Mat(src, roiRect);
            Mat areaMask = circularAreaMask(roi, r);

            Scalar color = generateBackgroundColor(roi);
            Mat maskedImage = new Mat(roi.size(), roi.type(), color);
            roi.copyTo(maskedImage, areaMask);
            return maskedImage;
        } catch (CvException e) {
            //TODO when is this exception thrown
            System.out.println(e.getMessage());
            e.printStackTrace();
            return src;
        }
    }

    private Scalar generateBackgroundColor(Mat src) {
        return new Scalar((int) ((Core.sumElems(src).val[0] / src.total())));
    }

    private void showIrisAndPupil(ImageData imageData) {
        Circle pupil = imageData.getFirstPupilCircle();
        Circle iris = imageData.getFirstIrisCircle();
        Mat circles = new MatOfPoint3(pupil.toPoint3(), iris.toPoint3()).t(); //transpose
        Mat image = imageData.getImageMat();

        display.displayIf(image, circles, displayTitle("iris and pupil circles"));
    }


}
