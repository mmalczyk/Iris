package main.writer;

import main.Utils.ImageUtils;
import main.interfaces.IDisplay;
import org.opencv.core.Mat;

/**
 * Created by Magda on 07/07/2017.
 */
public class Display implements IDisplay {

    //TODO refactor display usage for encapsulation not inheritance
    //TODO migrate ImageUtils showImage functions here
    //TODO add automatic resize display function

    private boolean showResults = false;

    public Display(boolean showResults) {
        this.showResults = showResults;
    }

    public Display() {
    }

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
        if (showResults) {
            ImageUtils.showImage(name, mat);
        }
    }

    @Override
    public void displayIf(Mat mat, String name, int resize) {
        if (showResults) {
            ImageUtils.showImage(name, mat, resize);
        }
    }
}
