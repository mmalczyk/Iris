package main.interfaces;

import main.Utils.ImageData;
import main.PluginFactory;

public interface ILocaliser extends IDisplay{
    ILocaliser INSTANCE =
            (ILocaliser) PluginFactory.getPlugin(ILocaliser.class);

    ImageData localise(ImageData imageData);
}
