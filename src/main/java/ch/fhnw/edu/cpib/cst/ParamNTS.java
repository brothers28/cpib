package ch.fhnw.edu.cpib.cst;
import ch.fhnw.edu.cpib.cst.interfaces.IParam;
import ch.fhnw.edu.cpib.cst.interfaces.IParamNTS;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.util.ArrayList;

// paramNTS ::= COMMA param paramNTS
public class ParamNTS extends Production implements IParamNTS {
    protected final IToken T_comma;
    protected final IParam N_param;
    protected final IParamNTS N_paramNTS;

    public ParamNTS(final IToken T_comma,
                    final IParam N_param,
                    final IParamNTS N_paramNTS) {
        this.T_comma = T_comma;
        this.N_param = N_param;
        this.N_paramNTS = N_paramNTS;

    }

    @Override
    public ArrayList<ch.fhnw.edu.cpib.ast.Param> toAbsSyn(ArrayList<ch.fhnw.edu.cpib.ast.Param> temp) {
        temp.add(N_param.toAbsSyn());
        return N_paramNTS.toAbsSyn(temp);
    }
}
