package main.encoder.processor;

import main.utils.ImageData;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Created by Magda on 11/07/2017.
 */
public class GaborFilterFactory {
    public static IGaborFilter getFilter(ImageData imageData) {
        assert imageData.getGaborFilterType() != null;

        GaborFilterType type = imageData.getGaborFilterType();

        IGaborFilter gaborFilter;
        if (type == GaborFilterType.FULL)
            gaborFilter = new FullGaborFilter();
        else if (type == GaborFilterType.GRID)
            gaborFilter = new GridGaborFilter();
        else
            throw new NotImplementedException();
        return gaborFilter;
    }
}
