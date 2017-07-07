package main.interfaces;

import org.opencv.core.Mat;

/**
 * Created by Magda on 29/06/2017.
 */
public interface IDisplay {

    boolean getShowResults();

    void showResults();

    void dontShowResults();

    void displayIf(Mat mat, String name);

    void displayIf(Mat mat, String name, int resize);
}
