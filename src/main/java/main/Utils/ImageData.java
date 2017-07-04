package main.Utils;

import main.Utils.ImageUtils;
import org.bytedeco.javacpp.opencv_core;
import org.opencv.core.Mat;

import java.awt.image.BufferedImage;
import java.nio.file.Path;

import static org.bytedeco.javacpp.opencv_core.cvCopy;

/**
 * Created by Magda on 28/06/2017.
 */
public class ImageData {

    private opencv_core.CvPoint3D32f irisCircleObsolete;
    private opencv_core.CvPoint3D32f pupilCircleObsolete;
    private Circle irisCircle;
    private Circle pupilCircle;

    private Path path;

    private opencv_core.IplImage image;

    private opencv_core.IplImage maskedImage;
    private BufferedImage buffImage;

    private opencv_core.IplImage normalisedImage;

    private Mat imageMat;

    private Mat normMat;

    public Circle getIrisCircle() {
        return irisCircle;
    }

    public void setIrisCircle(Circle irisCircle) {
        this.irisCircle = irisCircle;
    }

    public Circle getPupilCircle() {
        return pupilCircle;
    }

    public void setPupilCircle(Circle pupilCircle) {
        this.pupilCircle = pupilCircle;
    }

    public opencv_core.CvPoint3D32f getIrisCircleObsolete() {
        return irisCircleObsolete;
    }

    public void setIrisCircleObsolete(opencv_core.CvPoint3D32f irisCircleObsolete) {
        this.irisCircleObsolete = irisCircleObsolete;
    }

    public opencv_core.CvPoint3D32f getPupilCircleObsolete() {
        return pupilCircleObsolete;
    }

    public void setPupilCircle(opencv_core.CvPoint3D32f pupilCircle) {
        this.pupilCircleObsolete = pupilCircle;
    }


    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
    }

    public BufferedImage getBuffImage() {
        return buffImage;
    }

    public void setBuffImage(BufferedImage buffImage) {
        this.buffImage = buffImage;
    }

    public opencv_core.IplImage getImage() {
        opencv_core.IplImage imageCopy = ImageUtils.blankIplImageCopy(image);
        cvCopy(image, imageCopy);
        return imageCopy;
    }

    public void setImage(opencv_core.IplImage image) {
        this.image = image;
    }

    public void setNormalisedImage(opencv_core.IplImage normalisedImage) {
        this.normalisedImage = normalisedImage;
    }

    public Mat getImageMat() {
        return imageMat;
    }

    public void setImageMat(Mat imageMat) {
        this.imageMat = imageMat;
    }

    public Mat getNormMat() {
        return normMat;
    }

    public void setNormMat(Mat normMat) {
        this.normMat = normMat;
    }

}
