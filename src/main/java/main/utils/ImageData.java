package main.utils;

import main.encoder.ByteCode;
import main.encoder.processor.GaborFilterType;
import org.opencv.core.Mat;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Magda on 28/06/2017.
 */
public class ImageData {

    private ArrayList<Circle> irisCircles = new ArrayList<>();
    private ArrayList<Circle> pupilCircles = new ArrayList<>();
    private Path path;
    private Mat imageMat;
    private Mat normMat;
    private Mat exclusionMat; //map of exclusions
    private Mat imageWithMarkedCircles;
    private Mat codeMatForm;
    private ByteCode byteCode;
    private int codeMatCount;
    private List<Mat> realFilters;
    private List<Mat> imgFilters;

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

    public Mat getExclusionMat() {
        return exclusionMat;
    }

    public void setExclusionMap(Mat exclMat) {
        this.exclusionMat = exclMat;
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

    public Mat getImageWithMarkedCircles() {
        return imageWithMarkedCircles;
    }

    public void setImageWithMarkedCircles(Mat imageWithMarkedCircles) {
        this.imageWithMarkedCircles = imageWithMarkedCircles;
    }

    public ByteCode getByteCode() {
        return byteCode;
    }

    public void setByteCode(ByteCode byteCode) {
        this.byteCode = byteCode;
    }

    public Mat getCodeMatForm() {
        return codeMatForm;
    }

    public void setCodeMatForm(Mat codeMatForm) {
        this.codeMatForm = codeMatForm;
    }

    public int getCodeMatCount() {
        return codeMatCount;
    }

    public void SetCodeMatCount(int codeMatCount) {
        this.codeMatCount = codeMatCount;
    }

    public List<Mat> getImgFilters() {
        return imgFilters;
    }

    public void setImgFilters(List<Mat> imgFilters) {
        this.imgFilters = imgFilters;
    }

    public List<Mat> getRealFilters() {
        return realFilters;
    }

    public void setRealFilters(List<Mat> realFilters) {
        this.realFilters = realFilters;
    }
}

