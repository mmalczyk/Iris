package main.interfaces;

import main.PluginFactory;
import main.utils.ImageData;

import java.nio.file.Path;

public interface IReader {
    IReader INSTANCE =
            (IReader) PluginFactory.getPlugin(IReader.class);

    ImageData read(Path filePath);
}

