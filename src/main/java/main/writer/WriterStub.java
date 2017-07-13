package main.writer;

import main.display.DisplayableModule;
import main.encoder.ByteCode;
import main.interfaces.IWriter;

public class WriterStub extends DisplayableModule implements IWriter {
    @Override
    public boolean write(ByteCode code) {
        return false;
    }
}
