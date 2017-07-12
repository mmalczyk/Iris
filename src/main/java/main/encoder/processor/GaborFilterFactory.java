package main.encoder.processor;

import main.Utils.FilterConstants;
import main.Utils.ImageData;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Magda on 11/07/2017.
 */
public class GaborFilterFactory {
    public static IGaborFilter getFilter(ImageData imageData) {
        GaborFilterType type = imageData.getGaborFilterType();
        FilterConstants filterConstants = imageData.getFilterConstants();

        IGaborFilter gaborFilter;
        if (type == GaborFilterType.FULL)
            gaborFilter = new FullGaborFilter(filterConstants);
        else if (type == GaborFilterType.SELECTIVE)
            gaborFilter = new SelectiveGaborFilter(filterConstants);
        else
            throw new NotImplementedException();
        return gaborFilter;
    }
}
