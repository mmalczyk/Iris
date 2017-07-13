package main.comparator;

import main.encoder.ByteCode;
import main.interfaces.IComparator;

public class ComparatorStub implements IComparator {

    @Override
    public double compare(ByteCode byteCodeA, ByteCode byteCodeB) {
        return 0;
    }
}
