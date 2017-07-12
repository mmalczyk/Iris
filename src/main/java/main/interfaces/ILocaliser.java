package main.interfaces;

import main.PluginFactory;
import main.utils.ImageData;

public interface ILocaliser extends IDisplay {
    ILocaliser INSTANCE =
            (ILocaliser) PluginFactory.getPlugin(ILocaliser.class);

    ImageData localise(ImageData imageData);
}
