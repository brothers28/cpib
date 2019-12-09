package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.ICpsDeclNTS;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IDecl;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.Token;

import java.util.ArrayList;

// cpsDeclNTS ::= SEMICOLON decl cpsDeclNTS
public class CpsDeclNTS extends Production implements ICpsDeclNTS {
    protected final Token T_semicolon;
    protected final IDecl N_decl;
    protected final ICpsDeclNTS N_cpsDeclNTS;

    public CpsDeclNTS(final Token t_semicolon,
                      final IDecl n_decl,
                      final ICpsDeclNTS n_cpsDeclNTS) {
        T_semicolon = t_semicolon;
        N_decl = n_decl;
        N_cpsDeclNTS = n_cpsDeclNTS;
    }

    @Override
    public ArrayList<ch.fhnw.edu.cpib.absSynTree.interfaces.IDecl> toAbsSyn(ArrayList<ch.fhnw.edu.cpib.absSynTree.interfaces.IDecl> temp) {
        temp.add(N_decl.toAbsSyn());
        return N_cpsDeclNTS.toAbsSyn(temp);
    }
}
