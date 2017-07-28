package main.writer;

import main.display.DisplayableModule;
import main.encoder.ByteCode;
import main.interfaces.IWriter;

public class WriterStub extends DisplayableModule implements IWriter {

    public WriterStub() {
        super(moduleName);
    }

    @Override
    public boolean write(ByteCode code) {
        return false;
    }
}
