package main.comparator;

import main.display.DisplayableModule;
import main.encoder.ByteCode;
import main.interfaces.IComparator;

public class ComparatorStub extends DisplayableModule implements IComparator {

    @SuppressWarnings("unused")
    @Override
    public double compare(ByteCode byteCodeA, ByteCode byteCodeB) {
        return 0;
    }
}
