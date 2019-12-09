package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.StoDecl;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IStoDecl;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ITypedIdent;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.keywords.Changemode;
import ch.fhnw.edu.cpib.scanner.Token;

// stoDecl ::= CHANGEMODE typedIdent
public class StoDeclChangeMode extends Production implements IStoDecl {
    protected final Token T_changeMode;
    protected final ITypedIdent N_typedIdent;

    public StoDeclChangeMode(final Token T_changeMode,
                             final ITypedIdent N_typedIdent) {
        this.T_changeMode = T_changeMode;
        this.N_typedIdent = N_typedIdent;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.StoDecl toAbsSyn() {
        return new ch.fhnw.edu.cpib.absSynTree.StoDecl(((Changemode)T_changeMode).getChangemode(), N_typedIdent.toAbsSyn());
    }
}
