package main.interfaces;

import main.Utils.ImageData;
import main.PluginFactory;

public interface INormaliser extends IVisibleResult{
    INormaliser INSTANCE =
            (INormaliser) PluginFactory.getPlugin(INormaliser.class);

    ImageData normalize(ImageData imageData);


}
