package main.settings;

import main.interfaces.*;

public enum ModuleName {
    Comparator(IComparator.class.getName()),
    Encoder(IEncoder.class.getName()),
    Localiser(ILocaliser.class.getName()),
    Normaliser(INormaliser.class.getName()),
    Reader(IReader.class.getName()),
    Writer(IWriter.class.getName());

    private String name;

    ModuleName(String s) {
        this.name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
