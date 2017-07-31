package main;

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
                System.out.print(comparator.compare(byteCodeA, byteCodeB));
            }
        }
    }

    private static void setDisplay() {
        DisplaySettings.tuneDisplay(ModuleName.Reader);
        DisplaySettings.tuneDisplay(ModuleName.Localiser);
        DisplaySettings.tuneDisplay(ModuleName.Normaliser);
        DisplaySettings.tuneDisplay(ModuleName.Encoder);
        DisplaySettings.tuneDisplay(ModuleName.Comparator);
        DisplaySettings.tuneDisplay(ModuleName.Writer);
    }

    //TODO move this to a separate class
    private static ByteCode irisToCode(String arg) {
        Path path = FileSystems.getDefault().getPath(arg);

        ImageData imageData = reader.read(path);
        imageData = localiser.localise(imageData);
        imageData = normaliser.normalize(imageData);
        //TODO put this in the settings file
        imageData.setGaborFilterType(GaborFilterType.FULL);
//        imageData.setGaborFilterType(GaborFilterType.SELECTIVE);
        ByteCode code = encoder.encode(imageData);
        writer.write(code);

        return code;
    }
}
