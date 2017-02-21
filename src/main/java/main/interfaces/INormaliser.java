package main.interfaces;

import main.PluginFactory;

import java.awt.*;

public interface INormaliser {
    INormaliser INSTANCE =
            (INormaliser) PluginFactory.getPlugin(INormaliser.class);

    Image normalize(Image image);
}
