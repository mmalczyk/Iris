package main.reader;


import main.interfaces.IReader;

import java.awt.*;
import java.nio.file.Path;

public class ReaderStub implements IReader {
    @Override
    public Image read(Path filePath) {
        return null;
    }
}
