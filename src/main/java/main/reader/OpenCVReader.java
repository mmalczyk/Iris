package main.reader;

import main.display.DisplayableModule;
import main.interfaces.IReader;
import main.utils.ImageData;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import java.nio.file.Files;
import java.nio.file.Path;

import static org.opencv.imgcodecs.Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE;

/**
 * Created by Magda on 05/07/2017.
 */
public class OpenCVReader extends DisplayableModule implements IReader {

    public OpenCVReader() {
        super(moduleName);
    }

    @Override
    public ImageData read(Path filePath) {
        assert Files.exists(filePath);

        Mat src = Imgcodecs.imread(filePath.toString(), CV_LOAD_IMAGE_GRAYSCALE);

        ImageData imageData = new ImageData();
        imageData.setPath(filePath);
        imageData.setImageMat(src);

        assert src.channels() == 1; //greyscale
        assert src.width() > 0 && src.height() > 0;

        return imageData;
    }
}
