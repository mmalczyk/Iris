package main.interfaces;

import main.settings.ModuleName;
import main.settings.PluginFactory;
import main.utils.ImageData;

public interface ILocaliser {
    ModuleName moduleName = ModuleName.Localiser;
    ILocaliser INSTANCE =
            (ILocaliser) PluginFactory.getPlugin(moduleName);

    ImageData localise(ImageData imageData);
}
