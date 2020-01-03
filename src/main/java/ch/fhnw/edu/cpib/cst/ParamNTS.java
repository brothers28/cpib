package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IParam;
import ch.fhnw.edu.cpib.cst.interfaces.IParamNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// paramNTS ::= COMMA param paramNTS
public class ParamNTS extends Production implements IParamNTS {
    protected final IToken ts_comma;
    protected final IParam nts_param;
    protected final IParamNTS nts_paramNTS;

    public ParamNTS(final IToken ts_comma, final IParam nts_param, final IParamNTS nts_paramNTS) {
        this.ts_comma = ts_comma;
        this.nts_param = nts_param;
        this.nts_paramNTS = nts_paramNTS;

    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.Param> toAbsSyntax(ArrayList<ch.fhnw.edu.cpib.ast.Param> temp) {
        temp.add(nts_param.toAbsSyntax());
        return nts_paramNTS.toAbsSyntax(temp);
    }
}
