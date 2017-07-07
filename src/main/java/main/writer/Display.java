package main.writer;

import main.Utils.ImageUtils;
import main.interfaces.IDisplay;
import org.opencv.core.Mat;

/**
 * Created by Magda on 07/07/2017.
 */
public class Display implements IDisplay {

    private boolean showResults = false;

    @Override
    public boolean getShowResults() {
        return showResults;
    }

    @Override
    public void showResults() {
        this.showResults = true;
    }

    @Override
    public void dontShowResults() {
        this.showResults = false;
    }

    @Override
    public void displayIf(Mat mat, String name) {
        if (showResults)
            ImageUtils.showImage(name, mat);
    }

    @Override
    public void displayIf(Mat mat, String name, int resize) {
        if (showResults)
            ImageUtils.showImage(name, mat, resize);
    }
}
