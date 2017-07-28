package main.display;

import main.settings.ModuleName;

/**
 * Created by Magda on 13/07/2017.
 */
public abstract class DisplayableModule {

    protected Display display;
    protected ModuleName displayableModuleName;

    protected DisplayableModule(ModuleName moduleName) {
        display = new Display(moduleName);
    }

    protected String displayTitle(String text) {
        return this.getClass().getSimpleName() + ": " + text;
    }

}
