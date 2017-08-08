package main.comparator;

import main.display.DisplayableModule;
import main.interfaces.IComparator;
import main.utils.ImageData;
import org.opencv.core.Mat;

import static org.opencv.core.Core.NORM_HAMMING;
import static org.opencv.core.Core.norm;

public class MatComparator extends DisplayableModule implements IComparator {
    public MatComparator() {
        super(moduleName);
    }

    @Override
    public HammingDistance compare(ImageData imageDataA, ImageData imageDataB) {
        Mat matA = imageDataA.getNormMat();
        Mat matB = imageDataB.getNormMat();
        assert matA.size().equals(matB.size());
        double result = norm(matA, matB, NORM_HAMMING);
        result = (result / 8.) / (matA.height() * matB.width()); //hamming distance is bits that differ not bytes;
        assert result >= 0 && result <= 1;
        return new HammingDistance(result);
    }
}
