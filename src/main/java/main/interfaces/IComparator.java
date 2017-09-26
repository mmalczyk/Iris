package main.interfaces;


import main.comparator.HammingDistance;
import main.settings.ModuleName;
import main.settings.PluginFactory;
import main.utils.ImageData;
import org.opencv.core.Mat;

import java.util.List;

public interface IComparator {

    ModuleName moduleName = ModuleName.Comparator;
    IComparator INSTANCE =
            (IComparator) PluginFactory.getPlugin(moduleName);

    HammingDistance compare(ImageData imageDataA, ImageData imageDataB);

    HammingDistance matCompare(Mat matA, Mat matB);

    List<Mat> getPartialResults();

}
