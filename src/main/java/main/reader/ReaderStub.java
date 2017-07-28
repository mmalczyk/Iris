package main.reader;


import main.display.DisplayableModule;
import main.interfaces.IReader;
import main.utils.ImageData;

import java.nio.file.Path;

@SuppressWarnings("unused")
public class ReaderStub extends DisplayableModule implements IReader {
    public ReaderStub() {
        super(moduleName);
    }

    @Override
    public ImageData read(Path filePath) {
        return null;
    }
}
