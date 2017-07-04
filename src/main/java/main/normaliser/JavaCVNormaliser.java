package main.normaliser;

import main.Utils.ImageData;
import main.interfaces.INormaliser;
import org.bytedeco.javacpp.indexer.UShortRawIndexer;
import org.bytedeco.javacpp.opencv_core;

import static org.bytedeco.javacpp.opencv_highgui.cvShowImage;
import static org.bytedeco.javacpp.opencv_highgui.cvWaitKey;

/**
 * Created by Magda on 26/06/2017.
 */
public class JavaCVNormaliser implements INormaliser{
    //Daugman's rubber sheet model
    //https://en.wikipedia.org/wiki/Bilinear_interpolation


    private int rows;
    private int cols;

    private boolean showResults;

    public boolean getShowResults() {
        return showResults;
    }

    public void setShowResults(boolean showResults) {
        this.showResults = showResults;
    }

    private void showImageIfNeeded(String name, opencv_core.IplImage src){
        if (getShowResults()){
            cvShowImage(name,src);
            cvWaitKey(0);
        }
    }


    private void initNormaliser() {
        //TODO rozdzielczość
        //TODO fix rows cols type
        rows = 10;
        cols = 10;
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

    private float getRowsCount(ImageData imageData){
        //TODO check if parameter z is the correct one
        float pupilR = imageData.getPupilCircleObsolete().z();
        float irisR = imageData.getIrisCircleObsolete().z();
        return irisR-pupilR;
    }

    private double getColsCount(){
        return 10*2*Math.PI;
    }

    @Override
    public ImageData normalize(ImageData imageData) {
        initNormaliser();

        //TODO figure out scale
        opencv_core.Mat imageMat = new opencv_core.Mat(imageData.getImage());
        rows = (int) getRowsCount(imageData);
        cols = (int) getColsCount();
        //TODO type - temp fix
        opencv_core.Mat normMat = new opencv_core.Mat(rows, cols, imageData.getBuffImage().getType());


        //https://stackoverflow.com/questions/33035781/access-to-the-pixel-value-of-a-mat-using-the-javacv-api
        UShortRawIndexer imageMatIndexer = normMat.createIndexer();
        UShortRawIndexer normMatIndexer = normMat.createIndexer();

        for (int r = 0; r<rows; r++){
            for (int th = 0; th<cols; th++){
                int x = calcX(
                        r,
                        th,
                        (int)imageData.getPupilCircleObsolete().z(),
                        (int)imageData.getIrisCircleObsolete().z(),
                        (int)imageData.getIrisCircleObsolete().x());
                int y = calcY(
                        r,
                        th,
                        (int)imageData.getPupilCircleObsolete().z(),
                        (int)imageData.getIrisCircleObsolete().z(),
                        (int)imageData.getIrisCircleObsolete().x());

                if (withinBounds(x,y, imageMat)){
                    //TODO why is this out of bounds in the first place
                    //TODO error in JavaCv library
                    int pxl = imageMatIndexer.get(x, y);
                    normMatIndexer.put(r, th, pxl);
                }
                else{
                    //fill any gap with black
                    normMatIndexer.put(r,th, 0);
                }
            }
        }

        opencv_core.IplImage normImage = new opencv_core.IplImage(normMat);
        imageData.setNormalisedImage(normImage);

        showImageIfNeeded("normalised", normImage);
        return imageData;
    }

    private boolean withinBounds(int x, int y, opencv_core.Mat imageMat) {
        //TODO when does it happen that the values are out of bounds
        return x>=0 && x < imageMat.rows() && y >=0 && y < imageMat.cols();
    }
}
