package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICpsCmd;
import ch.fhnw.edu.cpib.cst.interfaces.IIfElseNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// ifelseNTS ::= ELSE cpsCmd
public class IfElseNTS extends Production implements IIfElseNTS {
    protected IToken ts_else;
    protected ICpsCmd nts_cpsCmd;

    public IfElseNTS(IToken ts_else, ICpsCmd nts_cpsCmd) {
        this.ts_else = ts_else;
        this.nts_cpsCmd = nts_cpsCmd;
    }

    @Override public ch.fhnw.edu.cpib.ast.CpsCmd toAbsSyntax() {
        return nts_cpsCmd.toAbsSyntax();
    }
}
