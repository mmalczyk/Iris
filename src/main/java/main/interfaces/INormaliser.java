package main.interfaces;

import main.Utils.ImageData;
import main.PluginFactory;

public interface INormaliser extends IDisplay {
    INormaliser INSTANCE =
            (INormaliser) PluginFactory.getPlugin(INormaliser.class);

    ImageData normalize(ImageData imageData);


}
