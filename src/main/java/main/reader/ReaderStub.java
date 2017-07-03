package main.reader;


import main.Utils.ImageData;
import main.interfaces.IReader;

import java.nio.file.Path;

public class ReaderStub implements IReader {
    @Override
    public ImageData read(Path filePath) {
        return null;
    }
}
