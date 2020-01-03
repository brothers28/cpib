package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IParamList;
import ch.fhnw.edu.cpib.cst.interfaces.IParamListNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// paramList ::= LPAREN paramListNTS RPAREN
public class ParamList extends Production implements IParamList {
    protected final IToken ts_lparen;
    protected final IParamListNTS nts_paramListNTS;
    protected final IToken ts_rparen;

    public ParamList(final IToken ts_lparen, final IParamListNTS nts_paramListNTS, final IToken ts_rparen) {
        this.ts_lparen = ts_lparen;
        this.nts_paramListNTS = nts_paramListNTS;
        this.ts_rparen = ts_rparen;

    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.Param> toAbsSyntax() {
        return nts_paramListNTS.toAbsSyntax();
    }
}
