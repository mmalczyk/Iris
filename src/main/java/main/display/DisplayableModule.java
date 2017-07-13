package main.display;

/**
 * Created by Magda on 13/07/2017.
 */
public abstract class DisplayableModule {

    protected Display display;

    protected DisplayableModule() {
        initDisplay();
    }

    protected String displayTitle(String text) {
        return this.getClass().getSimpleName() + ": " + text;
    }

    private void initDisplay() {
        display = new Display(this.getClass());
    }

}
