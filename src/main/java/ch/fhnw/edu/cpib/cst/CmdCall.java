package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.ast.ProcCallCmd;
import ch.fhnw.edu.cpib.cst.interfaces.ICmd;
import ch.fhnw.edu.cpib.cst.interfaces.IExprList;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

// cmd ::= CALL IDENT exprList
public class CmdCall extends Production implements ICmd {
    protected final IToken ts_call;
    protected final IToken ts_ident;
    protected final IExprList nts_exprList;

    public CmdCall(final IToken ts_call, final IToken ts_ident, final IExprList nts_exprList) {
        this.ts_call = ts_call;
        this.ts_ident = ts_ident;
        this.nts_exprList = nts_exprList;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.ICmd toAbsSyntax() {
        return new ProcCallCmd((Ident) ts_ident, nts_exprList.toAbsSyntax());
    }
}
