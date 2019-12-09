package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.IDecl;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IProcDecl;


// decl ::= procDecl
public class DeclProcDecl extends Production implements IDecl {
    protected final IProcDecl N_procDecl;

    public DeclProcDecl(final IProcDecl N_procDecl) {
        this.N_procDecl = N_procDecl;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.IDecl toAbsSyn() {
        return N_procDecl.toAbsSyn();
    }
}
