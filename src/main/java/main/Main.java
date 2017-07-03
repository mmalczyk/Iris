package main;

import main.Utils.ImageData;
import main.interfaces.*;

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
        else{
            //TODO this is a temp fix
            int length = 10;
            byte[] codeA = new byte[length],
                    maskA = new byte[length],
                    codeB = new byte[length],
                    maskB = new byte[length];
            irisToCode(args[0], codeA, maskA);
            if (args.length == 2) {
                irisToCode((args[1]), codeB, maskB);
                System.out.print(comparator.compare(codeA, maskA, codeB, maskB));

            }
        }
    }

    private static byte[] irisToCode(String arg, byte[] code, byte[] mask) {
        Path path = FileSystems.getDefault().getPath(arg);
        ImageData image = reader.read(path);

        localiser.setShowResults(true);
        localiser.localise(image);

        normaliser.setShowResults(true);
        image = normaliser.normalize(image);
        //TODO what about the image mask?
        code = encoder.encode(image.getBuffImage());
        writer.write(code);
        return code;
    }
}
