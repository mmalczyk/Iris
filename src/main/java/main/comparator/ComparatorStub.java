package main.comparator;

import main.display.DisplayableModule;
import main.interfaces.IComparator;
import main.utils.ImageData;

@SuppressWarnings("unused")
public class ComparatorStub extends DisplayableModule implements IComparator {

    public ComparatorStub() {
        super(moduleName);
    }

    @Override
    public HammingDistance compare(ImageData imageDataA, ImageData imageDataB) {
        return null;
    }
}
