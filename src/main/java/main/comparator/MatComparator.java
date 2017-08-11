package main.comparator;

import main.display.DisplayableModule;
import main.interfaces.IComparator;
import main.utils.ImageData;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.List;

public class MatComparator extends DisplayableModule implements IComparator {
    private ArrayList<Mat> partialResults;

    public MatComparator() {
        super(moduleName);
        partialResults = new ArrayList<>();
    }

    @Override
    public HammingDistance compare(ImageData imageDataA, ImageData imageDataB) {
        Mat matA = imageDataA.getCodeMatForm();
        Mat matB = imageDataB.getCodeMatForm();

        assert matA.size().equals(matB.size());

        int totalSize = matA.height() * matA.width();
        double result = totalSize;
        int moveRight = 1;
        for (int i = 0; i < matA.width() / moveRight && result > 0. && result / (double) totalSize > 0.12; i++) {
            double hammingDistance = hammingDistance(matA, matB);
            if (hammingDistance < result)
                result = hammingDistance;
            matB = shiftColumns(matB, moveRight);
        }

        result = result / totalSize;
        assert result >= 0 && result <= 1;


        return new HammingDistance(result);
    }

    private double hammingDistance(Mat matA, Mat matB) {
        double distance = 0;
        for (int i = 0; i < matA.height(); i++)
            for (int j = 0; j < matA.width(); j++) {
                if (matA.get(i, j)[0] != matB.get(i, j)[0])
                    distance++;
            }
        return distance;
    }

    @Override
    public List<Mat> getPartialResults() {
        return partialResults;
    }

    private Mat shiftColumns(Mat inMat, int moveRight) {
//        ImageUtils.showBufferedImage(inMat.clone(), "shiftColumns");

        assert moveRight >= 0;
        if (moveRight == 0)
            return inMat.clone();

        Mat baseMat = inMat.clone();

        //partialResults.add(baseMat.clone());

        Mat rect = inMat.submat(new Rect(0, 0, moveRight, baseMat.rows())); // your smaller mat
        //ImageUtils.showBufferedImage(rect, "rect");
        Mat subMat = baseMat.submat(new Rect(baseMat.cols() - moveRight, 0, moveRight, baseMat.rows()));
        assert rect.size().equals(subMat.size());
        rect.copyTo(subMat);

        //partialResults.add(baseMat.clone());

        rect = inMat.submat(new Rect(moveRight, 0, baseMat.cols() - moveRight, baseMat.rows())); // your smaller mat
        subMat = baseMat.submat(new Rect(0, 0, baseMat.cols() - moveRight, baseMat.rows()));
        assert rect.size().equals(subMat.size());
        rect.copyTo(subMat);

        partialResults.add(baseMat.clone());

        return baseMat;
    }

}
