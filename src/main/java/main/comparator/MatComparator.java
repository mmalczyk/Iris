package main.comparator;

import main.display.DisplayableModule;
import main.interfaces.IComparator;
import main.utils.ImageData;
import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.max;
import static java.lang.Integer.min;
import static main.comparator.ComparatorConsts.*;


public class MatComparator extends DisplayableModule implements IComparator {
    private ArrayList<Mat> partialResults;

    public MatComparator() {
        super(moduleName);
        partialResults = new ArrayList<>();
    }

    @Override
    public HammingDistance compare(ImageData imageDataA, ImageData imageDataB) {
        return matCompare(imageDataA.getCodeMatForm(),
                imageDataA.getExclusionMat(),
                imageDataA.getCodeMatCount(),
                imageDataB.getCodeMatForm(),
                imageDataB.getExclusionMat(),
                imageDataB.getCodeMatCount());
    }

    @Override
    //
    public HammingDistance matCompare(Mat matA, Mat matB) {
        assert matA.size().equals(matB.size());
        double hd = hammingDistance(matA, null, matB, null, ComparisonType.EQUAL);
        return new HammingDistance(hd);
    }


    public HammingDistance matCompare(Mat matA, Mat maskA, int imageCountA, Mat matB, Mat maskB, int imageCountC) {
        double hd = 1;

        // flying window concept
        int imageCount = max(1, min(imageCountA, imageCountC));
        int rowCount = matA.rows() / imageCount;
        int columnCount = matA.cols();
        boolean maskA_present_for_row = false;
        boolean maskB_present_for_row = false;
        boolean maskA_present = maskA != null && !maskA.empty();
        boolean maskB_present = maskB != null && !maskB.empty();

        // test parameters
        assert matA.size().equals(matB.size());
        if (maskA_present) {
            assert maskA.cols() == columnCount;
            maskA_present_for_row = maskA.rows() == rowCount;
            assert maskA_present_for_row || maskA.cols() == matA.cols();
        }
        if (maskB_present) {
            assert maskB.cols() == columnCount;
            maskB_present_for_row = maskB.rows() == rowCount;
            assert maskB_present_for_row || maskB.cols() == matB.cols();
        }


        // size of flying rectangle
        int roidWidth = columnCount - MARGIN_LEFT - MARGIN_RIGHT;
        int roiHeight = rowCount - MARGIN_UP - MARGIN_DOWN;
        int i, j, t, colA, rowA, colB, rowB;
        Mat roiA, roiB, roiMaskA = null, roiMaskB = null;
        int widthB = matB.width();
        int heightB = matB.height();
        int marginRow = max(MARGIN_UP, MARGIN_DOWN);
        int marginCol = max(MARGIN_LEFT, MARGIN_RIGHT);

        for (int i_roi = 0; i_roi < imageCount; ++i_roi) {

            colA = MARGIN_LEFT;
            rowA = rowCount * i_roi + MARGIN_UP;
            roiA = new Mat(matA, new Rect(colA, rowA, roidWidth, roiHeight));

            if (maskA_present_for_row) {
                roiMaskA = new Mat(maskA, new Rect(MARGIN_LEFT, MARGIN_UP, roidWidth, roiHeight));    // map of exclusions for roiA
            } else if (maskA_present) {
                roiMaskA = new Mat(maskA, new Rect(colA, rowA, roidWidth, roiHeight));    // full map of exclusions for roiA
            }

            i = 0;
            j = 0;
            do {
                do {
                    t = i == 0 ? 2 : 0;     // prevent reocurring testing of the same window if row==0
                    do {
                        rowB = 0;
                        colB = 0;

                        switch (t) {
                            case 0:
                                colB = colA + j;
                                rowB = rowA + i;
                                break;
                            case 1:
                                colB = colA - j;
                                rowB = rowA + i;
                                break;
                            case 2:
                                colB = colA + j;
                                rowB = rowA - i;
                                break;
                            case 3:
                                colB = colA - j;
                                rowB = rowA - i;
                                break;
                        }

                        if (rowB >= 0 && colB >= 0
                                && rowB + roiHeight <= heightB
                                && colB + roidWidth <= widthB) {

                            roiB = new Mat(matB, new Rect(colB, rowB, roidWidth, roiHeight));
                            if (maskB_present_for_row) {
                                roiMaskB = new Mat(maskB, new Rect(colB, MARGIN_UP, roidWidth, roiHeight)); // map of exclusions for roiB
                            } else if (maskB_present) {
                                roiMaskB = new Mat(maskB, new Rect(colB, rowB, roidWidth, roiHeight));    // full map of exclusions for roiB
                            }
                            double hd1 = hammingDistance(roiA, roiMaskA, roiB, roiMaskB, COMPARISON_FUNCTION);
                            if (hd1 < hd) {
                                hd = hd1;
                            }

                        }
                        t++;
                        if (j == 0)
                            t++;   // prevent reocurring testing of the same window if col==0
                    }
                    while (hd > 0 && t < 4);
                    ++j;
                }
                while (hd > 0 && j < marginCol);
                ++i;
            }
            while (hd > 0 && i < marginRow);
        }
        assert hd >= 0 && hd <= 1;
        return new HammingDistance(hd);
    }

    private double hammingDistance(Mat matA, Mat maskA, Mat matB, Mat maskB, ComparisonType comparisonType) {
        assert matA.size().equals(matB.size());

        int max_j = matA.width();
        int max_i = matA.height();

        boolean maskA_present = maskA != null;
        boolean maskB_present = maskB != null;

        int total = max_i * max_j;
        assert total > 0;

        int distance = 0;
        boolean a, b;
        for (int j = 0; j < max_j; j++) {
            for (int i = 0; i < max_i; i++) {
                if (maskA_present && maskA.get(i, j)[0] == 0 || maskB_present && maskB.get(i, j)[0] == 0) {
                    total--;
                } else {
                    a = matA.get(i, j)[0] != 0;
                    b = matB.get(i, j)[0] != 0;
                    if (
                            comparisonType == ComparisonType.EQUAL && a == b ||
                                    comparisonType == ComparisonType.NOT_EQUAL && a != b ||
                                    comparisonType == ComparisonType.XOR && a ^ b
                            ) {
                        distance++;
                    }
                }
            }
        }

        return (double) distance / (double) total;
    }


    @Override
    public List<Mat> getPartialResults() {
        return partialResults;
    }


}

