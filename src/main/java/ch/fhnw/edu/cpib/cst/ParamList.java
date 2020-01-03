package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IParamList;
import ch.fhnw.edu.cpib.cst.interfaces.IParamListNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// paramList ::= LPAREN paramListNTS RPAREN
public class ParamList extends Production implements IParamList {
    private IToken ts_lparen;
    private IParamListNTS nts_paramListNTS;
    private IToken ts_rparen;

    public ParamList(IToken ts_lparen, IParamListNTS nts_paramListNTS, IToken ts_rparen) {
        this.ts_lparen = ts_lparen;
        this.nts_paramListNTS = nts_paramListNTS;
        this.ts_rparen = ts_rparen;

    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.Param> toAbsSyntax() {
        return nts_paramListNTS.toAbsSyntax();
    }
}
