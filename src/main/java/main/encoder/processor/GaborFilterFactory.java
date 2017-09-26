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
        else if (type == GaborFilterType.NORM)
            gaborFilter = new NormGaborFilter();
        else if (type == GaborFilterType.COMP)
            gaborFilter = new NormGaborFilter();
        else if (type == GaborFilterType.OSIR)
            gaborFilter = new NormGaborFilter();
        else
            throw new NotImplementedException();
        return gaborFilter;
    }
}
