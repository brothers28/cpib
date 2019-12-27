package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IExprList;
import ch.fhnw.edu.cpib.cst.interfaces.IFactorNTS;
import ch.fhnw.edu.cpib.scanner.Ident;

// factorNTS ::= exprList
public class FactorNTSExprList extends Production implements IFactorNTS {

    protected final IExprList N_exprList;

    public FactorNTSExprList(final IExprList n_exprList) {
        N_exprList = n_exprList;
    }

    @Override public ch.fhnw.edu.cpib.ast.interfaces.IFactor toAbsSyn(Ident ident) {
        return new ch.fhnw.edu.cpib.ast.FunCallFactor(ident, N_exprList.toAbsSyn());
    }
}
