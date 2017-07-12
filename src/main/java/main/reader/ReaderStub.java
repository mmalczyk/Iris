package main.reader;


import main.interfaces.IReader;
import main.utils.ImageData;

import java.nio.file.Path;

public class ReaderStub implements IReader {
    @Override
    public ImageData read(Path filePath) {
        return null;
    }
}
