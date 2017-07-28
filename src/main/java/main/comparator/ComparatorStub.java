package main.comparator;

import main.display.DisplayableModule;
import main.encoder.ByteCode;
import main.interfaces.IComparator;

public class ComparatorStub extends DisplayableModule implements IComparator {

    public ComparatorStub() {
        super(moduleName);
    }

    @SuppressWarnings("unused")
    @Override
    public double compare(ByteCode byteCodeA, ByteCode byteCodeB) {
        return 0;
    }
}
