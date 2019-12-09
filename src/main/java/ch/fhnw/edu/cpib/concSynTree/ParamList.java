package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.Param;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IParamList;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IParamListNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.Token;

import java.util.ArrayList;

// paramList ::= LPAREN paramListNTS RPAREN
public class ParamList extends Production implements IParamList {
    protected final Token T_lparen;
    protected final IParamListNTS N_paramListNTS;
    protected final Token T_rparen;

    public ParamList(final Token T_lparen,
                     final IParamListNTS N_paramListNTS,
                     final Token T_rparen) {
        this.T_lparen = T_lparen;
        this.N_paramListNTS = N_paramListNTS;
        this.T_rparen = T_rparen;

    }

    @Override
    public ArrayList<ch.fhnw.edu.cpib.absSynTree.Param> toAbsSyn() {
        return N_paramListNTS.toAbsSyn();
    }
}
