package main.interfaces;

import main.PluginFactory;

import java.awt.*;
import java.nio.file.Path;

public interface IReader {
    IReader INSTANCE =
            (IReader) PluginFactory.getPlugin(IReader.class);

    Image read(Path filePath);
}
