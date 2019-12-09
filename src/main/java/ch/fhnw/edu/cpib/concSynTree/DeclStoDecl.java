package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.IDecl;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IStoDecl;

// decl ::= stoDecl
public class DeclStoDecl extends Production implements IDecl {
    protected final IStoDecl N_stoDecl;

    public DeclStoDecl(final IStoDecl N_stoDecl) {
        this.N_stoDecl = N_stoDecl;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.IDecl toAbsSyn() {
        return N_stoDecl.toAbsSyn();
    }
}
