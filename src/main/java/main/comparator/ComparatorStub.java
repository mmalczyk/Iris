package main.comparator;

import main.interfaces.IComparator;

public class ComparatorStub implements IComparator {

    @Override
    public double compare(byte[] codeA, byte[] codeB, byte[] maskA, byte[] maskB) {
        return 0;
    }
}
