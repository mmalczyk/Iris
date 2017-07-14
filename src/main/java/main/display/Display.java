package main.display;

import main.utils.Circle;
import main.utils.ImageUtils;
import org.opencv.core.Mat;

import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Magda on 13/07/2017.
 */
public class Display {

    //TODO something probably should be simplified
    //TODO toDisplayableMat in color

    //Main class sets toDisplayableMat settings, modules call toDisplayableMat functions on their own

    private static final Map<String, Boolean> isModuleDisplayedDict = new Hashtable<>(); //doesn't allow null objects
    private static final int resizeLimit = 50;
    private final Class displayedModule;

    public Display(Class object) {
        displayedModule = object;
    }

    public static void displayModule(Class object, boolean toDisplay) throws IllegalArgumentException {
        //TODO will this work or do I need a string
        String className = object.getName();
        Boolean value = isModuleDisplayedDict.putIfAbsent(className, toDisplay);
        if (value != null)
            throw new IllegalArgumentException("Class " + className + " is already in the dictionary");
    }

    public static void clear() {
        isModuleDisplayedDict.clear();
    }

    public static boolean moduleNotInDictionary(Class object) {
        return isModuleDisplayedDict.get(object.getName()) == null;
    }

    private static boolean canDisplay(Class object) throws IllegalArgumentException {
        //TODO this will always return true unless an exception if thrown
        String className = object.getName();
        Boolean returnValue = isModuleDisplayedDict.get(className);
        if (returnValue == null)
            throw new IllegalArgumentException("Class " + className + " is not in the dictionary");
        return returnValue;
    }

    public void displayIf(Mat mat, String title, int resize) throws IllegalArgumentException {
        //conditional resize
        assert resize > 1;
        if (canDisplay(displayedModule)) {
            if (mat.rows() * resize > resizeLimit || mat.cols() * resize > resizeLimit)
                displayIf(mat, title);
            else {
                title += " " + resize + "x";
                ImageUtils.showImage(title, mat, resize);
            }
        }
    }

    //TODO migrate ImageUtils showImage functions here
    public void displayIf(Mat mat, String title) throws IllegalArgumentException {
        if (canDisplay(displayedModule))
            ImageUtils.showImage(title, mat);
    }

    public void displayIf(Mat src, Mat circles, String title) throws IllegalArgumentException {
        Mat dest = ImageUtils.drawCircles(src, circles);
        displayIf(dest, title);
    }

    public void displayIf(Mat src, Circle[] circles, String title) throws IllegalArgumentException {
        Mat dest = ImageUtils.drawCircles(src, circles);
        displayIf(dest, title);
    }

    private boolean canDisplay() {
        return canDisplay(displayedModule);
    }

}
