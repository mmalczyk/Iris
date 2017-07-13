package main.reader;


import main.display.DisplayableModule;
import main.interfaces.IReader;
import main.utils.ImageData;

import java.nio.file.Path;

@SuppressWarnings("unused")
public class ReaderStub extends DisplayableModule implements IReader {
    @Override
    public ImageData read(Path filePath) {
        return null;
    }
}
