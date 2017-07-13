package main.writer;

import main.encoder.ByteCode;
import main.interfaces.IWriter;

public class WriterStub implements IWriter {
    @Override
    public boolean write(ByteCode code) {
        return false;
    }
}
