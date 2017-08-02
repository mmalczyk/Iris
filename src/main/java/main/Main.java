package main;

import main.comparator.HammingDistance;
import main.encoder.ByteCode;
import main.encoder.processor.GaborFilterType;
import main.interfaces.*;
import main.settings.DisplaySettings;
import main.settings.ModuleName;
import main.utils.ImageData;
import org.opencv.core.Core;

import java.nio.file.FileSystems;
import java.nio.file.Path;


public class Main {

    //TODO error logging?

    private static final IReader reader = IReader.INSTANCE;
    private static final ILocaliser localiser = ILocaliser.INSTANCE;
    private static final INormaliser normaliser = INormaliser.INSTANCE;
    private static final IEncoder encoder = IEncoder.INSTANCE;
    private static final IComparator comparator = IComparator.INSTANCE;
    private static final IWriter writer = IWriter.INSTANCE;

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
            ByteCode byteCodeA = irisToCode(args[0]);
            if (args.length == 2) {
                ByteCode byteCodeB = irisToCode((args[1]));
                HD = comparator.compare(byteCodeA, byteCodeB);
                System.out.println("Hamming Distance: " + HD.getHD());
            }
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
    private static ByteCode irisToCode(String arg) {
        Path path = FileSystems.getDefault().getPath(arg);

        ImageData imageData = reader.read(path);
        imageData = localiser.localise(imageData);
        imageData = normaliser.normalize(imageData);
        //TODO put this in the settings file
//        imageData.setGaborFilterType(GaborFilterType.FULL);
        imageData.setGaborFilterType(GaborFilterType.SELECTIVE);
        ByteCode code = encoder.encode(imageData);
        writer.write(code);

        return code;
    }
}
