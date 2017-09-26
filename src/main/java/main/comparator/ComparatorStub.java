package main.comparator;

import main.display.DisplayableModule;
import main.interfaces.IComparator;
import main.utils.ImageData;
import org.opencv.core.Mat;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class ComparatorStub extends DisplayableModule implements IComparator {

    public ComparatorStub() {
        super(moduleName);
    }

    @Override
    public HammingDistance compare(ImageData imageDataA, ImageData imageDataB) {
        return null;
    }

    @Override
    public HammingDistance matCompare(Mat matA, Mat matB) {
        return null;
    }

    @Override
    public List<Mat> getPartialResults() {
        return new ArrayList<>();
    }


}
