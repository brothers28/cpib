package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICmd;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// cmd ::= SKIP
public class CmdSkip extends Production implements ICmd {
    private IToken ts_skip;

    public CmdSkip(IToken ts_skip) {
        this.ts_skip = ts_skip;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.ICmd toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.SkipCmd();
    }
}
