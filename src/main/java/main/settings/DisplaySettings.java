package main.settings;

import main.display.Display;

public class DisplaySettings extends AbstractSettings {

    public static boolean getDisplayStatus(ModuleName settingName) {
        String propertyValue = properties.getProperty(settingName.toString() + "_DISPLAY");
        if (propertyValue == null) {
            throw new RuntimeException("value not specified for " +
                    settingName.toString() + " in PluginFactory properties.");
        }
        try {
            return Boolean.valueOf(propertyValue.trim());
        } catch (Exception ex) {
            throw new RuntimeException("factory unable to get display status " +
                    settingName.toString());
        }

    }

    public static void tuneDisplay(ModuleName settingName) {
        Display.displayModule(settingName, getDisplayStatus(settingName));
    }
}
