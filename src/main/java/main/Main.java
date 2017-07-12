package main;

import main.encoder.processor.GaborFilterType;
import main.interfaces.*;
import main.utils.ImageData;

import java.nio.file.FileSystems;
import java.nio.file.Path;


public class Main {

    private static final IComparator comparator = IComparator.INSTANCE;
    private static final IEncoder encoder = IEncoder.INSTANCE;
    private static final INormaliser normaliser = INormaliser.INSTANCE;
    private static final IReader reader = IReader.INSTANCE;
    private static final IWriter writer = IWriter.INSTANCE;
    private static final ILocaliser localiser = ILocaliser.INSTANCE;


    public static void main(String[] args) {
        if (args.length == 0)
            System.out.print("No arguments supplied");
        else {
            //TODO this is a temp fix
            int length = 10;
            byte[] codeA = new byte[length],
                    maskA = new byte[length],
                    codeB = new byte[length],
                    maskB = new byte[length];
            irisToCode(args[0]);
            if (args.length == 2) {
                irisToCode((args[1]));
                System.out.print(comparator.compare(codeA, maskA, codeB, maskB));
            }
        }
    }

    private static byte[] irisToCode(String arg) {
//        localiser.showResults(true);
//        normaliser.showResults(true);
        encoder.showResults();

        Path path = FileSystems.getDefault().getPath(arg);
        ImageData image = reader.read(path);
        image = localiser.localise(image);
        image = normaliser.normalize(image);

        //TODO is this the place to do it?
        image.setGaborFilterType(GaborFilterType.FULL);
//        image.setGaborFilterType(GaborFilterType.SELECTIVE);
        byte[] code = encoder.encode(image);
        writer.write(code);
        return code;
    }
}
