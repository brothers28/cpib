package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.StoDecl;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ICpsStoDecl;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IProcDeclNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.Token;

import java.util.ArrayList;

/**
 * @Author Hussein Farzi
 */

// procDeclNTS ::= LOCAL cpsStoDecl
public class ProcDeclNTS extends Production implements IProcDeclNTS {
    protected final Token T_local;
    protected final ICpsStoDecl N_cpsStoDecl;

    public ProcDeclNTS(final Token t_local,
                       final ICpsStoDecl n_cpsStoDecl) {
        T_local = t_local;
        N_cpsStoDecl = n_cpsStoDecl;
    }

    @Override
    public ArrayList<ch.fhnw.edu.cpib.absSynTree.StoDecl> toAbsSyn() {
        return N_cpsStoDecl.toAbsSyn();
    }
}
