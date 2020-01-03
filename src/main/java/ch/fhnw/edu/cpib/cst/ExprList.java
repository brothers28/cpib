package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.IExprList;
import ch.fhnw.edu.cpib.cst.interfaces.IExprListLparenNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// exprList ::= LPAREN exprListLparenNTS RPAREN
public class ExprList extends Production implements IExprList {
    protected final IToken ts_lParen;
    protected final IExprListLparenNTS nts_exprListLparenNTS;
    protected final IToken ts_rParen;

    public ExprList(final IToken ts_lParen, final IExprListLparenNTS nts_exprListLparenNTS, final IToken ts_rParen) {
        this.ts_lParen = ts_lParen;
        this.ts_rParen = ts_rParen;
        this.nts_exprListLparenNTS = nts_exprListLparenNTS;
    }

    @Override public ArrayList<IExpr> toAbsSyntax() {
        return nts_exprListLparenNTS.toAbsSyntax();
    }
}
