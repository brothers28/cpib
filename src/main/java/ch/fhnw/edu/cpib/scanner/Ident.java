package ch.fhnw.edu.cpib.scanner;

import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;

public class Ident extends Base {
    private final String ident;

    public Ident(Terminals terminal, String ident) {
        super(terminal);
        this.ident = ident;
    }

    public String getIdent() {
        return ident;
    }

    public String toString() {
        return "(" + super.toString() + ", \"" + getIdent() + "\")";
    }
}
