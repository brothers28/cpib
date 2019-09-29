package ch.fhnw.edu.cpib.scanner;

import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;

public class Base {
    private final Terminals terminal;
    Base(Terminals terminal){
        this.terminal = terminal;
    }

    Terminals getTerminal(){
        return terminal;
    }

    @Override
    public String toString(){
        return getTerminal().toString();
    }
}
