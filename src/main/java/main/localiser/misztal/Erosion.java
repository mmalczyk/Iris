package main.localiser.misztal;

import pl.edu.uj.JImageStream.filters.StatisticalFilter;
import pl.edu.uj.JImageStream.model.Pixel;

import java.util.Comparator;
import java.util.List;

/**
 * Created by Krzysztof on 14.02.2017.
 */
@SuppressWarnings("SameParameterValue")
public class Erosion extends StatisticalFilter {

    public Erosion() {
        maskSize = 3;
    }

    public Erosion(int maskSize) {
        this.maskSize = maskSize;
    }

    @Override
    protected Pixel getPixelResult(List<Pixel> list) {
        Integer red = list.stream().map(Pixel::getRed).max(Comparator.naturalOrder()).get();
        Integer green = list.stream().map(Pixel::getGreen).max(Comparator.naturalOrder()).get();
        Integer blue = list.stream().map(Pixel::getBlue).max(Comparator.naturalOrder()).get();
        Integer alpha = list.stream().map(Pixel::getAlpha).max(Comparator.naturalOrder()).get();

        return new Pixel(red, green, blue, alpha);
    }

}
