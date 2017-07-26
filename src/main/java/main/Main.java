package main;

import main.display.Display;
import main.encoder.ByteCode;
import main.encoder.processor.GaborFilterType;
import main.interfaces.*;
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

    //TODO set toDisplayableMat with command line arguments or settings
    private static void setDisplay() {
        Display.displayModule(reader.getClass(), false);
        Display.displayModule(localiser.getClass(), true);
        Display.displayModule(normaliser.getClass(), false);
        Display.displayModule(encoder.getClass(), false);
        Display.displayModule(comparator.getClass(), false);
        Display.displayModule(writer.getClass(), false);
    }

    private static ByteCode irisToCode(String arg) {
        Path path = FileSystems.getDefault().getPath(arg);

        ImageData image = reader.read(path);
        image = localiser.localise(image);
        //TODO define behaviour when no iris found
        image = normaliser.normalize(image);
        //TODO is this the place to do it?
        image.setGaborFilterType(GaborFilterType.FULL);
//        image.setGaborFilterType(GaborFilterType.SELECTIVE);
        ByteCode code = encoder.encode(image);
        writer.write(code);

        return code;
    }
}
