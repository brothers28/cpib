package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IParam;
import ch.fhnw.edu.cpib.cst.interfaces.IParamListNTS;
import ch.fhnw.edu.cpib.cst.interfaces.IParamNTS;

import java.util.ArrayList;

// paramListNTS ::= param paramNTS
public class ParamListNTS extends Production implements IParamListNTS {
    protected final IParam nts_param;
    protected final IParamNTS nts_paramNTS;

    public ParamListNTS(final IParam nts_param, final IParamNTS nts_paramNTS) {
        this.nts_param = nts_param;
        this.nts_paramNTS = nts_paramNTS;

    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.Param> toAbsSyntax() {
        ArrayList<ch.fhnw.edu.cpib.ast.Param> temp = new ArrayList<>();
        temp.add(nts_param.toAbsSyntax());

        return nts_paramNTS.toAbsSyntax(temp);
    }
}
