package main.localiser;

import main.Utils.ImageData;
import main.Utils.ImageUtils;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.opencv_core;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_highgui.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.cvLoadImage;
import static org.bytedeco.javacpp.opencv_imgproc.*;

//TODO potentially change code from JavaCV to opencv bindings
//it's updated by bytedeco but it's not the official version
//TODO finish OpenCV tutorial

/**
 * Created by Magda on 31/05/2017.
 */
public class JavaCVLocaliser extends BasicLocaliser{

    private ImageData imageData;

    private IplImage irisAreaMask(IplImage src, CvPoint3D32f pupilCircle, double radius){
        //https://stackoverflow.com/questions/18460053/how-to-black-out-everything-outside-a-circle-in-open-cv

        IplImage mask = ImageUtils.blankIplImageCopy(src);
        cvSetZero(mask);
        CvPoint pupilCenter = getCircleCenter(pupilCircle);
        cvCircle(mask, pupilCenter, (int)radius, new CvScalar(255., 255.,255., 255.),-1, 8, 0 );

        return mask;
    }


    private double upperBoundOfIrisRadius(CvPoint3D32f pupilCircle){
        //http://www3.nd.edu/~flynn/papers/HollingsworthBowyerFlynn_CVIU2008.pdf
        double pupilRadius = getRadius(pupilCircle);
        //pupil to iris radius - 0.2 to 0.7
        //TODO vary ratio until you get only one circle
        double maxRatio = 100/15;
        //double minRatio = 100/8;
        //double minIrisRadius = pupilRadius*minRatio;
        return pupilRadius*maxRatio;
        //TODO radius has to be inside bounds of image
    }

    //TODO refactor these into class Circle or something
    private CvPoint3D32f getCircle(BytePointer circleBlock){
        return new CvPoint3D32f(circleBlock);
    }

    private CvPoint getCircleCenter(CvPoint3D32f circle){
        return cvPointFrom32f(new CvPoint2D32f(circle.x(), circle.y()));
    }

    private double getRadius(CvPoint3D32f circle){
        return circle.z();
    }
    //TODO new class end

    private void showImageIfNeeded(String name, IplImage src){
        if (getShowResults())
            cvShowImage(name,src);
    }

    private void printCircles(IplImage src, CvSeq circles){
        for(int i = 0; i < circles.total(); i++){
            CvPoint3D32f circle = getCircle(cvGetSeqElem(circles, i));
            CvPoint center = getCircleCenter(circle);
            double radius = getRadius(circle);
            cvCircle(src, center, (int)Math.round(radius), CvScalar.GREEN, 6, CV_AA, 0);
        }

        showImageIfNeeded("Result",src);
    }

    private void findPupil(IplImage src_org){
        IplImage src = ImageUtils.blankIplImageCopy(src_org);
        cvCopy(src_org, src);

        IplImage gray = cvCreateImage(cvGetSize(src), 8, 1);
        cvCvtColor(src, gray, CV_BGR2GRAY);
        cvAdaptiveThreshold(
                gray,
                gray,
                255,
                CV_ADAPTIVE_THRESH_GAUSSIAN_C,
                CV_THRESH_BINARY,
                25, //block size
                6);
        showImageIfNeeded("binarised",gray);
        cvSmooth(gray, gray);
        //normalised block filter

        CvMemStorage mem = CvMemStorage.create();

        CvSeq circles = cvHoughCircles(
                gray, //Input image
                mem, //Memory Storage
                CV_HOUGH_GRADIENT, //Detection method
                1, //Inverse ratio
                50, //Minimum distance between the centers of the detected circles
                100, //Higher threshold for canny edge detector
                50, //Threshold at the center detection stage
                10, //min radius
                100 //max radius
        );

        //TODO check how this method fares
        //assert(circles.total() == 1);

        imageData.setPupilCircle(getCircle(cvGetSeqElem(circles, 0)));

        printCircles(src, circles);

    }


    private void findIris(IplImage src) {
        //http://opencvlover.blogspot.com/2012/07/hough-circle-in-javacv.html
        //https://stackoverflow.com/questions/26867276/iris-and-pupil-detection-in-image-with-java-and-opencv

        IplImage gray = cvCreateImage(cvGetSize(src), 8, 1);

        cvCvtColor(src, gray, CV_BGR2GRAY);

        cvSmooth(gray, gray);


        cvAdaptiveThreshold(
                gray,
                gray,
                255,
                CV_ADAPTIVE_THRESH_GAUSSIAN_C,
                CV_THRESH_BINARY,
                25, //block size
                6);

        //http://docs.opencv.org/2.4/doc/tutorials/imgproc/gausian_median_blur_bilateral_filter/gausian_median_blur_bilateral_filter.html
        //normalised block filter
        showImageIfNeeded("binarised",gray);

        cvSmooth(gray, gray);

        CvMemStorage mem = CvMemStorage.create();

        CvSeq circles = cvHoughCircles(
                gray, //Input image
                mem, //Memory Storage
                CV_HOUGH_GRADIENT, //Detection method

                2, //Inverse ratio
                100, //Minimum distance between the centers of the detected circles
                100, //Higher threshold for canny edge detector
                50, //Threshold at the center detection stage
                70, //min radius
                100 //max radius
        );

        //TODO check how this method fares
        //assert(circles.total() == 1);

        CvPoint3D32f circle = new CvPoint3D32f(cvGetSeqElem(circles, 0));
        CvPoint center = cvPointFrom32f(new CvPoint2D32f(circle.x(), circle.y()));
        int radius = Math.round(circle.z());
        cvCircle(src, center, radius, CvScalar.GREEN, 6, CV_AA, 0);

        imageData.setIrisCircleObsolete(circle);

        showImageIfNeeded("final result",src);
        cvWaitKey(0);
    }

    @Override
    public ImageData localise(ImageData imageData) {

        this.imageData = imageData;

        opencv_core.IplImage src = cvLoadImage(imageData.getPath().toString());
        cvWaitKey(0);
        imageData.setImage(src);

        findPupil(src);

        double radius = upperBoundOfIrisRadius(imageData.getPupilCircleObsolete());
        IplImage irisAreaMask = irisAreaMask(src, imageData.getPupilCircleObsolete(), radius);

        IplImage maskedImage = ImageUtils.blankIplImageCopy(src);
        cvCopy(src, maskedImage, irisAreaMask);
        showImageIfNeeded("maskedImage",maskedImage);

        findIris(maskedImage);

        cvWaitKey(0);

        return imageData;
    }



}

