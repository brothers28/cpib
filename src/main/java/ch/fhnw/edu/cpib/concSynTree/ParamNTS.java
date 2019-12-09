package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.Param;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IParam;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IParamNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.Token;

import java.util.ArrayList;

// paramNTS ::= COMMA param paramNTS
public class ParamNTS extends Production implements IParamNTS {
    protected final Token T_comma;
    protected final IParam N_param;
    protected final IParamNTS N_paramNTS;

    public ParamNTS(final Token T_comma,
                    final IParam N_param,
                    final IParamNTS N_paramNTS) {
        this.T_comma = T_comma;
        this.N_param = N_param;
        this.N_paramNTS = N_paramNTS;

    }

    @Override
    public ArrayList<ch.fhnw.edu.cpib.absSynTree.Param> toAbsSyn(ArrayList<ch.fhnw.edu.cpib.absSynTree.Param> temp) {
        temp.add(N_param.toAbsSyn());
        return N_paramNTS.toAbsSyn(temp);
    }
}
