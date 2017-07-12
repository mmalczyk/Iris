package main.interfaces;

import main.PluginFactory;
import main.Utils.ImageData;

public interface INormaliser extends IDisplay {
    INormaliser INSTANCE =
            (INormaliser) PluginFactory.getPlugin(INormaliser.class);

    ImageData normalize(ImageData imageData);


}
