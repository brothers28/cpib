package ch.fhnw.edu.cpib.cst;
import ch.fhnw.edu.cpib.ast.interfaces.IExpr;
import ch.fhnw.edu.cpib.cst.interfaces.IExprList;
import ch.fhnw.edu.cpib.cst.interfaces.IExprListLparenNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

/**
 * @Author Hussein Farzi
 */

// exprList ::= LPAREN exprListLparenNTS RPAREN
public class ExprList extends Production implements IExprList {
    protected final IToken T_lParen;
    protected final IExprListLparenNTS N_exprListLparenNTS;
    protected final IToken T_rParen;

    public ExprList(final IToken t_lParen,
                    final IExprListLparenNTS n_exprListLparenNTS,
                    final IToken t_rParen) {
        T_lParen = t_lParen;
        T_rParen = t_rParen;
        N_exprListLparenNTS = n_exprListLparenNTS;
    }

    @Override
    public ArrayList<IExpr> toAbsSyn() {
        return N_exprListLparenNTS.toAbsSyn();
    }
}
