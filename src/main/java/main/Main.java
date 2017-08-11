package main;

import main.comparator.HammingDistance;
import main.encoder.processor.GaborFilterType;
import main.interfaces.*;
import main.settings.DisplaySettings;
import main.settings.ModuleName;
import main.utils.ImageData;
import org.opencv.core.Core;
import org.opencv.core.Mat;

import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.List;


public class Main {

    //TODO error logging?

    private static final IEncoder encoder = IEncoder.INSTANCE;
    private static final IReader reader = IReader.INSTANCE;
    private static final ILocaliser localiser = ILocaliser.INSTANCE;
    private static final INormaliser normaliser = INormaliser.INSTANCE;
    private static final IComparator comparator = IComparator.INSTANCE;
    private static final IWriter writer = IWriter.INSTANCE;

    private static ImageData finalResult1;
    private static ImageData finalResult2;
    private static List<Mat> comparatorPartialResult;

    public static ImageData getFinalResult1() {
        return finalResult1;
    }

    public static ImageData getFinalResult2() {
        return finalResult2;
    }
    private static HammingDistance HD;

    public static HammingDistance getHammingDistance() {
        return HD;
    }

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        setDisplay();
    }

    public static void main(String[] args) {
        if (args.length == 0)
            System.out.print("No arguments supplied");
        else {
            if (args.length <= 2) {
                finalResult1 = irisToCode(args[0]);
                if (args.length == 2) {
                    finalResult2 = irisToCode((args[1]));
                    HD = comparator.compare(finalResult1, finalResult2);
                    comparatorPartialResult = comparator.getPartialResults();
                    System.out.println("Hamming Distance: " + HD.getHD());

                }
            } else
                throw new UnsupportedOperationException("Too many arguments");
        }
    }

    private static void setDisplay() {
        DisplaySettings.setDisplay(ModuleName.Reader);
        DisplaySettings.setDisplay(ModuleName.Localiser);
        DisplaySettings.setDisplay(ModuleName.Normaliser);
        DisplaySettings.setDisplay(ModuleName.Encoder);
        DisplaySettings.setDisplay(ModuleName.Comparator);
        DisplaySettings.setDisplay(ModuleName.Writer);
    }

    //TODO move this to a separate class alongside with HammingDistance
    private static ImageData irisToCode(String arg) {
        Path path = FileSystems.getDefault().getPath(arg);

        ImageData imageData = reader.read(path);
        imageData = localiser.localise(imageData);
        imageData = normaliser.normalize(imageData);
        //TODO put this in the settings file
        imageData.setGaborFilterType(GaborFilterType.FULL);
//        imageData.setGaborFilterType(GaborFilterType.GRID);
        imageData = encoder.encode(imageData);
        writer.write(imageData.getByteCode());
        return imageData;
    }

    public static List<Mat> getComparatorPartialResult() {
        return comparatorPartialResult;
    }
}
