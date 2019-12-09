package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IDecl;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ICpsDecl;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IGlobalNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.Token;

import java.util.ArrayList;

// globalNTS ::= GLOBAL cpsDecl
public class GlobalNTS extends Production implements IGlobalNTS {
    protected final Token T_global;
    protected final ICpsDecl N_cpsDecl;

    public GlobalNTS(final Token t_global,
                     final ICpsDecl n_cpsDecl) {
        T_global = t_global;
        N_cpsDecl = n_cpsDecl;
    }

    @Override
    public ArrayList<ch.fhnw.edu.cpib.absSynTree.interfaces.IDecl> toAbsSyn() {
        return N_cpsDecl.toAbsSyn();
    }
}
