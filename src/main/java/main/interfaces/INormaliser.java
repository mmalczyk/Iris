package main.interfaces;

import main.settings.ModuleName;
import main.settings.PluginFactory;
import main.utils.ImageData;

public interface INormaliser {
    ModuleName moduleName = ModuleName.Normaliser;
    INormaliser INSTANCE =
            (INormaliser) PluginFactory.getPlugin(moduleName);

    ImageData normalize(ImageData imageData);

}
