package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IExprList;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IExprListLparenNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.Token;

import java.util.ArrayList;

/**
 * @Author Hussein Farzi
 */

// exprList ::= LPAREN exprListLparenNTS RPAREN
public class ExprList extends Production implements IExprList {
    protected final Token T_lParen;
    protected final IExprListLparenNTS N_exprListLparenNTS;
    protected final Token T_rParen;

    public ExprList(final Token t_lParen,
                    final IExprListLparenNTS n_exprListLparenNTS,
                    final Token t_rParen) {
        T_lParen = t_lParen;
        T_rParen = t_rParen;
        N_exprListLparenNTS = n_exprListLparenNTS;
    }

    @Override
    public ArrayList<IExpr> toAbsSyn() {
        return N_exprListLparenNTS.toAbsSyn();
    }
}
