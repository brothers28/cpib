package ch.fhnw.edu.cpib.scanner;

import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;

public class Base implements Token{
    private final Terminals terminal;
    public Base(Terminals terminal){
        this.terminal = terminal;
    }

    public Terminals getTerminal(){
        return terminal;
    }

    @Override
    public String toString(){
        return getTerminal().toString();
    }
}
