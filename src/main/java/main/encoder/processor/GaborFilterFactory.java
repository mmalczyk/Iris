package main.encoder.processor;

import main.utils.FilterConstants;
import main.utils.ImageData;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Magda on 11/07/2017.
 */
public class GaborFilterFactory {
    public static IGaborFilter getFilter(ImageData imageData) {
        assert imageData.getFilterConstants() != null;
        assert imageData.getGaborFilterType() != null;

        GaborFilterType type = imageData.getGaborFilterType();
        FilterConstants filterConstants = imageData.getFilterConstants();

        IGaborFilter gaborFilter;
        if (type == GaborFilterType.FULL)
            gaborFilter = new FullGaborFilter(filterConstants);
        else if (type == GaborFilterType.GRID)
            gaborFilter = new SelectiveGaborFilter(filterConstants);
        else
            throw new NotImplementedException();
        return gaborFilter;
    }
}
