package main.encoder.processor;

import main.encoder.ByteCode;
import main.utils.ImageData;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Integer.max;
import static org.opencv.core.Core.BORDER_CONSTANT;
import static org.opencv.core.Core.copyMakeBorder;
import static org.opencv.imgproc.Imgproc.filter2D;

/**
 * Created by Magda on 11/07/2017.
 */
public class NormGaborFilter extends AbstractGaborFilter {

    public NormGaborFilter() {
        gaborFilterType = GaborFilterType.NORM;
    }

    public List<Mat> process(ImageData imageData) {
        Mat image = imageData.getNormMat();
        List<Mat> resultSteps = new ArrayList<>();
        List<Mat> realFilters;
        List<Mat> imaginaryFilters;
        boolean processImaginary = false;

        // choose a filter to test
        GaborFilterType filterType = imageData.getGaborFilterType();
        if (filterType == GaborFilterType.FULL) {
            realFilters = buildFiltersReal();
            imaginaryFilters = buildFiltersImaginary();
            processImaginary = true;
        } else if (filterType == GaborFilterType.GRID) {
            realFilters = buildFiltersReal();
            imaginaryFilters = buildFiltersImaginary();
            processImaginary = true;
        } else if (filterType == GaborFilterType.OSIR) {
            realFilters = FiltersOsiris();
            imaginaryFilters = null;
        } else {
            realFilters = null;
            imaginaryFilters = null;
            throw new NotImplementedException();
        }

        imageData.setRealFilters(realFilters);
        imageData.setImgFilters(imaginaryFilters);
        int filterSize = realFilters.size();
        int filterWidth = max(maxFiltersCols(realFilters), maxFiltersCols(imaginaryFilters));
        if (filterWidth % 2 != 0)
            filterWidth++;
        int resultMatSize = processImaginary ? filterSize * 2 : filterSize;

        // test
        int iRows = image.rows();
        int iCols = image.cols();

        // create result Mat
        Mat resultMat = new Mat(iRows * resultMatSize, iCols, image.type());
        // expand source mat
        image.convertTo(image, CvType.CV_32F);        //gabor kernels work with this type
        int margin = filterWidth / 2;
        int excludedRows = iRows;
        int excludedCols = iCols + 2 * margin;
        Mat exMat = new Mat(excludedRows, excludedCols, image.type()); // CV_32F
        copyMakeBorder(image, exMat, 0, 0, margin, margin, BORDER_CONSTANT); //, new Scalar(0));
        // create mats for intermediate results of filtering
        Mat irMat1 = new Mat(excludedRows, excludedCols, image.type());  // CV_32F
        Mat irMat2 = new Mat(excludedRows, excludedCols, resultMat.type());
        Mat iiMat1 = new Mat(excludedRows, excludedCols, image.type());  // CV_32F
        Mat iiMat2 = new Mat(excludedRows, excludedCols, resultMat.type());

        // apply filters
        Mat kernel, kernelImg;
        Mat ROImat, ROIresult;
        int p = 0;
        for (int i = 0; i < filterSize; i++) {

            kernel = realFilters.get(i);

            filter2D(exMat, irMat1, -1, kernel);
            // phase quantization of real part
            Imgproc.threshold(irMat1, irMat2, 0, 255, Imgproc.THRESH_BINARY);
            ROImat = new Mat(irMat2, new Rect(0 + margin, 0, 0 + iCols, 0 + iRows));
            // copy to destination mat
            ROIresult = new Mat(resultMat, new Rect(0, p * iRows, 0 + iCols, 0 + iRows));
            // ROImat.copyTo(ROIresult);   // copyTo doesn't support ROI
            for (int r = 0; r < iRows; r++) {
                for (int c = 0; c < iCols; c++) {
                    ROIresult.put(r, c, ROImat.get(r, c));
                }
            }
            resultSteps.add(ROImat);
            p++;

            // process imaginary kernel
            if (processImaginary) {
                kernelImg = imaginaryFilters.get(i);
                filter2D(exMat, iiMat1, -1, kernelImg);
                // phase quantization of imaginary part
                Imgproc.threshold(iiMat1, iiMat2, 0, 255, Imgproc.THRESH_BINARY);
                ROImat = new Mat(iiMat2, new Rect(0 + margin, 0, 0 + iCols, 0 + iRows));
                // copy to destinaton mat
                ROIresult = new Mat(resultMat, new Rect(0, p * iRows, 0 + iCols, 0 + iRows));
                // ROImat.copyTo(ROIresult);   // copyTo doesn't support ROI
                for (int r = 0; r < iRows; ++r) {
                    for (int c = 0; c < iCols; ++c) {
                        ROIresult.put(r, c, ROImat.get(r, c));
                    }
                }
                resultSteps.add(ROImat);
                ++p;
            }
        }

        // save result to imageData
        imageData.setCodeMatForm(resultMat);
        imageData.SetCodeMatCount(resultMatSize);
        imageData.setByteCode(new ByteCode(resultMat));
        return resultSteps;
    }

    // count max columns in filters
    private int maxFiltersCols(List<Mat> filters) {
        int filterWidth = 0;
        if (filters != null) {
            for (int i = 0; i < filters.size(); ++i) {
                filterWidth = max(filterWidth, filters.get(i).cols());
            }
        }
        return filterWidth;
    }
}
