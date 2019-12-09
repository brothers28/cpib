package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.Param;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IParam;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IParamListNTS;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IParamNTS;

import java.util.ArrayList;

// paramListNTS ::= param paramNTS
public class ParamListNTS extends Production implements IParamListNTS {
    protected final IParam N_param;
    protected final IParamNTS N_paramNTS;

    public ParamListNTS(final IParam N_param,
                        final IParamNTS N_paramNTS) {
        this.N_param = N_param;
        this.N_paramNTS = N_paramNTS;

    }

    @Override
    public ArrayList<ch.fhnw.edu.cpib.absSynTree.Param> toAbsSyn() {
        ArrayList<ch.fhnw.edu.cpib.absSynTree.Param> temp = new ArrayList<>();
        temp.add(N_param.toAbsSyn());

        return N_paramNTS.toAbsSyn(temp);
    }
}
