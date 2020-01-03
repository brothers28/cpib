package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IExprList;
import ch.fhnw.edu.cpib.cst.interfaces.IFactorNTS;
import ch.fhnw.edu.cpib.scanner.Ident;

// factorNTS ::= exprList
public class FactorNTSExprList extends Production implements IFactorNTS {

    protected final IExprList nts_exprList;

    public FactorNTSExprList(final IExprList nts_exprList) {
        this.nts_exprList = nts_exprList;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IFactor toAbsSyntax(Ident ident) {
        return new ch.fhnw.edu.cpib.ast.FunCallFactor(ident, nts_exprList.toAbsSyntax());
    }
}
