package main.writer;

import main.interfaces.IWriter;

public class WriterStub implements IWriter {
    @Override
    public boolean write(byte[] code) {
        return false;
    }
}
