package main.interfaces;

import main.settings.ModuleName;
import main.settings.PluginFactory;
import main.utils.ImageData;

import java.nio.file.Path;

public interface IReader {
    ModuleName moduleName = ModuleName.Reader;
    IReader INSTANCE =
            (IReader) PluginFactory.getPlugin(moduleName);

    ImageData read(Path filePath);
}

