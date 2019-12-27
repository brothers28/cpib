package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.cst.interfaces.IIfElseNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// ifelseNTS ::= ELSE cpsCmd
public class IfElseNTS extends Production implements IIfElseNTS {
    protected final IToken T_else;
    protected final ICpsCmd N_cpsCmd;

    public IfElseNTS(final IToken T_else, final ICpsCmd N_cpsCmd) {
        this.T_else = T_else;
        this.N_cpsCmd = N_cpsCmd;
    }

    @Override public ch.fhnw.edu.cpib.ast.CpsCmd toAbsSyn() {
        return N_cpsCmd.toAbsSyn();
    }
}
