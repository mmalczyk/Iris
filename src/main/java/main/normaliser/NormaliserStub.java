package main.normaliser;

import main.Utils.ImageData;
import main.interfaces.INormaliser;

public class NormaliserStub implements INormaliser {
    @Override
    public ImageData normalize(ImageData imageData) {
        return imageData;
    }

    private boolean showResults;

    public boolean getShowResults() {
        return showResults;
    }

    public void setShowResults(boolean showResults) {
        this.showResults = showResults;
    }

}
