package main.normaliser;

import main.Utils.ImageData;
import main.Utils.ImageUtils;
import main.interfaces.INormaliser;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint3;
import org.opencv.core.Point3;

/**
 * Created by Magda on 30/06/2017.
 */
public class OpenCVNormaliser implements INormaliser {

    //Daugman's rubber sheet model
    //https://en.wikipedia.org/wiki/Bilinear_interpolation

    private int rows;
    private int cols;

    private boolean showResults;

    public boolean getShowResults() {
        return showResults;
    }

    private ImageUtils imageUtils = new ImageUtils(showResults);

    public void setShowResults(boolean showResults) {
        this.showResults = showResults;
        this.imageUtils.setShowResults(showResults);
    }

    public void initNormaliser() {
        //TODO rozdzielczość
        //TODO fix rows cols type
        rows = 10;
        cols = 10;
        int type = 10;
        Mat normalisedIris = new Mat(rows, cols, type);

    }

    //TODO is angle in degrees? it needs to be
    private int xAtTh(int th, double radius, int xs){
        //point on pupil boundary at angle th
        float x = (float)(radius * Math.cos(th * Math.PI / 180F)) + xs;
        return (int) x;
    }

    private int yAtTh(int th, double radius, int xs){
        //point on pupil boundary at angle th
        float x = (float)(radius * Math.cos(th * Math.PI / 180F)) + xs;
        return (int) x;
    }

    private int calcX(int r, int th, int pupilRadius, int irisRadius, int xs){
        return (1-r) * xAtTh(th, pupilRadius, xs) + r*xAtTh(th, irisRadius, xs);
    }

    private int calcY(int r, int th, int pupilRadius, int irisRadius, int ys){
        return (1-r) * yAtTh(th, pupilRadius, ys) + r*yAtTh(th, irisRadius, ys);
    }

    private double getRowsCount(ImageData imageData){
        //TODO move to ImageData
        //width of iris alone
        double pupilR = imageData.getPupilCircle().getRadius();
        double irisR = imageData.getIrisCircle().getRadius();
        return (irisR-pupilR)/2; //get radius out of diameter
    }

    private double getColsCount(ImageData imageData)
    {
        //TODO move to ImageData
        //circumference of iris in the middle
        double pupilR = imageData.getPupilCircle().getRadius();
        double irisR = imageData.getIrisCircle().getRadius();
        double middleR = (pupilR+irisR)/2;
        return 2*Math.PI*middleR;
    }

    @Override
    public ImageData normalize(ImageData imageData) {
        initNormaliser();

        Mat imageMat = imageData.getImageMat();
        rows = (int) getRowsCount(imageData);
        cols = (int) getColsCount(imageData);
        int type = imageData.getImageMat().type();
        //TODO I don't like this conversion
        int size = (int) (rows*cols*imageMat.step1(0));

        Mat normMat = new
                Mat(rows, cols, type);

        byte[] pxlArray = new byte[size];

        Point3 pupilCircle = imageData.getPupilCircle().toPoint3();
        Point3 irisCircle = imageData.getIrisCircle().toPoint3();

        for (int r = 0; r<rows; r++){
            for (int th = 0; th<cols; th++){
                int x = calcX(
                        r,
                        th,
                        (int)pupilCircle.z,
                        (int)irisCircle.z,
                        (int)irisCircle.x);
                int y = calcY(
                        r,
                        th,
                        (int)pupilCircle.z,
                        (int)irisCircle.z,
                        (int)irisCircle.y);

                if (withinBounds(x,y, imageMat)){
                    //TODO why is this out of bounds in the first place
                    imageMat.get(x, y, pxlArray);
                    normMat.put(r, th, pxlArray);
                }
                else{
//                    TODO what when x y are out of bounds?
     }
            }
        }


        imageData.setNormMat(normMat);

        showNormalisedArea(imageData);

        imageUtils.showImageIfNeeded("normalised", normMat, 3);
        return imageData;
    }

    private boolean withinBounds(int x, int y, Mat imageMat) {
        return x>=0 && x < imageMat.rows() && y >=0 && y < imageMat.cols();
    }

    private void showNormalisedArea(ImageData imageData){
        Point3 pupil = imageData.getPupilCircle().toPoint3();
        Point3 iris = imageData.getIrisCircle().toPoint3();
        pupil.x = iris.x;
        pupil.y = iris.y;
        Mat circles = new MatOfPoint3(pupil, iris).t(); //transpose
        Mat image = imageData.getImageMat().clone();

        imageUtils.drawCirclesOnImage(image, circles);
        imageUtils.showImageIfNeeded("area before norm", image);
    }

}
