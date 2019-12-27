package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICmd;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// cmd ::= SKIP
public class CmdSkip extends Production implements ICmd {
    protected final IToken T_skip;

    public CmdSkip(final IToken T_skip) {
        this.T_skip = T_skip;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.ICmd toAbsSyn() {
        return new ch.fhnw.edu.cpib.ast.SkipCmd();
    }
}
