package main.localiser.misztal;

import pl.edu.uj.JImageStream.api.core.Filter;
import pl.edu.uj.JImageStream.model.Pixel;

import java.util.stream.IntStream;

/**
 * Created by Krzysztof on 14.02.2017.
 */
public class OtsuBinarization extends Filter {

    private int threshold;
    private boolean weight;

    public OtsuBinarization(boolean weight) {
        this.weight = weight;
    }
    public OtsuBinarization() {
        this.weight = false;
    }

    @Override
    public void setUp() {
        super.setUp();
        int sourceHeight = getSourceHeight();
        int sourceWidth = getSourceWidth();

        double[] histogram = new double[256];

        for (int i = 0; i < sourceHeight; i++) {
            for (int j = 0; j < sourceWidth; j++) {
                int k = (int) IntStream.of(getPixel(j, i).getColors()).limit(3).average().getAsDouble();
                histogram[k]++;
            }
        }

        int total = sourceHeight * sourceWidth;

        float sum = 0;
        for (int i = 0; i < 256; i++) {
            sum += i * histogram[i];
        }

        float sumB = 0;
        int wB = 0,
                wF;

        float varMax = 0;
        threshold = 0;

        for (int i = 0; i < 256; i++) {
            if (weight) {
                histogram[i] = histogram[i] * Math.exp(-i / 100.);
            }

            wB += histogram[i];
            if (wB == 0) {
                continue;
            }
            wF = total - wB;

            if (wF == 0) {
                break;
            }

            sumB += (float) (i * histogram[i]);
            float mB = sumB / wB;
            float mF = (sum - sumB) / wF;

            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = i;
            }
        }
        if (threshold == 0) {
            ++threshold;
        }

    }

    @Override
    public void apply(int x, int y) {

        Pixel pixel = getPixel(x, y);
        if (IntStream.of(pixel.getColors()).limit(3).average().getAsDouble() < threshold) {
            setPixel(x, y, new Pixel(0, 0, 0, 255));
        } else {
            setPixel(x, y, new Pixel(255, 255, 255, 255));
        }

    }
}
