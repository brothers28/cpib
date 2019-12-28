package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IParam;
import ch.fhnw.edu.cpib.cst.interfaces.IParamListNTS;
import ch.fhnw.edu.cpib.cst.interfaces.IParamNTS;

import java.util.ArrayList;

// paramListNTS ::= param paramNTS
public class ParamListNTS extends Production implements IParamListNTS {
    protected final IParam N_param;
    protected final IParamNTS N_paramNTS;

    public ParamListNTS(final IParam N_param, final IParamNTS N_paramNTS) {
        this.N_param = N_param;
        this.N_paramNTS = N_paramNTS;

    }

    @Override public ArrayList<ch.fhnw.edu.cpib.ast.Param> toAbsSyntax() {
        ArrayList<ch.fhnw.edu.cpib.ast.Param> temp = new ArrayList<>();
        temp.add(N_param.toAbsSyntax());

        return N_paramNTS.toAbsSyntax(temp);
    }
}
