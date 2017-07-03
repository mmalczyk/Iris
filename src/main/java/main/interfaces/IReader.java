package main.interfaces;

import main.Utils.ImageData;
import main.PluginFactory;

import java.nio.file.Path;

public interface IReader {
    IReader INSTANCE =
            (IReader) PluginFactory.getPlugin(IReader.class);

    ImageData read(Path filePath);
}

