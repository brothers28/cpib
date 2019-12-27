package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IParamList;
import ch.fhnw.edu.cpib.cst.interfaces.IParamListNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// paramList ::= LPAREN paramListNTS RPAREN
public class ParamList extends Production implements IParamList {
    protected final IToken T_lparen;
    protected final IParamListNTS N_paramListNTS;
    protected final IToken T_rparen;

    public ParamList(final IToken T_lparen, final IParamListNTS N_paramListNTS, final IToken T_rparen) {
        this.T_lparen = T_lparen;
        this.N_paramListNTS = N_paramListNTS;
        this.T_rparen = T_rparen;

    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.Param> toAbsSyn() {
        return N_paramListNTS.toAbsSyn();
    }
}
