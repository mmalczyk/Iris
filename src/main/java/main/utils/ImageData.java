package main.utils;

import main.encoder.processor.GaborFilterType;
import org.opencv.core.Mat;

import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Created by Magda on 28/06/2017.
 */
public class ImageData {

    private FilterConstants filterConstants;
    private ArrayList<Circle> irisCircles = new ArrayList<>();
    private ArrayList<Circle> pupilCircles = new ArrayList<>();
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

    //circles are relative to roi not src img
    //additional possible eye areas are noted
    public Circle getFirstIrisCircle() {
        return irisCircles.get(0);
    }

    public void addIrisCircle(Circle irisCircle) {
        this.irisCircles.add(irisCircle);
    }

    public Circle getFirstPupilCircle() {
        return pupilCircles.get(0);
    }

    public void addPupilCircle(Circle pupilCircle) {
        this.pupilCircles.add(pupilCircle);
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

    public int irisesFound() {
        return this.irisCircles.size();
    }

    public int pupilsFound() {
        return this.pupilCircles.size();
    }

    public void resetPupils() {
        this.pupilCircles.clear();
    }
}
