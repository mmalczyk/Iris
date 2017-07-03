package main.localiser;

import main.Utils.ImageData;
import main.interfaces.ILocaliser;

public class LocaliserStub implements ILocaliser {
    @Override
    public ImageData localise(ImageData imageData) {
        return imageData;
    }

    public boolean getShowResults() {
        return showResults;
    }

    public void setShowResults(boolean showResults) {
        this.showResults = showResults;
    }

    private boolean showResults;
}
