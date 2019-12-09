package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.IDecl;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IFunDecl;

// decl ::= funDecl
public class DeclFunDecl extends Production implements IDecl {
    protected final IFunDecl N_funDecl;

    public DeclFunDecl(final IFunDecl N_funDecl) {
        this.N_funDecl = N_funDecl;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.IDecl toAbsSyn() {
        return N_funDecl.toAbsSyn();
    }
}
