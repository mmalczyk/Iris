package main.reader;

import main.Utils.ImageData;
import main.interfaces.IReader;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.nio.file.Path;

/**
 * Created by Magda on 05/07/2017.
 */
public class OpenCVReader implements IReader{

    @Override
    public ImageData read(Path filePath) {
        Mat src = Imgcodecs.imread(filePath.toString());

        ImageData imageData = new ImageData();
        imageData.setPath(filePath);
        imageData.setImageMat(src);
        return imageData;
    }
}
