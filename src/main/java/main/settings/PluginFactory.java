package main.settings;

/**
 * Created by Magda on 07.02.2017.
 * Based on the Plugin pattern by Martin Fowler
 */
public class PluginFactory extends AbstractSettings {

    public static Object getPlugin(ModuleName settingName) {
        String implName = properties.getProperty(settingName.toString());
        if (implName == null) {
            throw new RuntimeException("object not specified for " +
                    settingName.toString() + " in PluginFactory properties.");
        }
        try {
            return Class.forName(implName).newInstance();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("Inaccessible module: " + e.getMessage());
        }
    }
}