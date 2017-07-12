package main.Utils;

import main.encoder.processor.GaborFilterType;
import org.opencv.core.Mat;

import java.nio.file.Path;

/**
 * Created by Magda on 28/06/2017.
 */
public class ImageData {

    public FilterConstants filterConstants;
    private Circle irisCircle;
    private Circle pupilCircle;
    private Path path;
    private Mat imageMat;
    private Mat normMat;

    private GaborFilterType gaborFilterType;

    public GaborFilterType getGaborFilterType() {
        return gaborFilterType;
    }

    public void setGaborFilterType(GaborFilterType gaborFilterType) {
        this.gaborFilterType = gaborFilterType;
    }

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

    public Path getPath() {
        return path;
    }

    public void setPath(Path path) {
        this.path = path;
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

    public FilterConstants getFilterConstants() {
        return filterConstants;
    }

    public void setFilterConstants(FilterConstants filterConstants) {
        this.filterConstants = filterConstants;
    }


}
