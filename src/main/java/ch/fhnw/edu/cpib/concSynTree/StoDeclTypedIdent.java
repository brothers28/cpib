package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.StoDecl;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IStoDecl;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ITypedIdent;

// stoDecl ::= typedIdent
public class StoDeclTypedIdent extends Production implements IStoDecl {
    protected final ITypedIdent N_typedIdent;

    public StoDeclTypedIdent(final ITypedIdent N_typedIdent) {
        this.N_typedIdent = N_typedIdent;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.StoDecl toAbsSyn() {
        return new ch.fhnw.edu.cpib.absSynTree.StoDecl(N_typedIdent.toAbsSyn());
    }
}
