package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IStoDecl;
import ch.fhnw.edu.cpib.cst.interfaces.ITypedIdent;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.keywords.Changemode;

// stoDecl ::= CHANGEMODE typedIdent
public class StoDeclChangeMode extends Production implements IStoDecl {
    protected final IToken T_changeMode;
    protected final ITypedIdent N_typedIdent;

    public StoDeclChangeMode(final IToken T_changeMode, final ITypedIdent N_typedIdent) {
        this.T_changeMode = T_changeMode;
        this.N_typedIdent = N_typedIdent;
    }

    @Override public ch.fhnw.edu.cpib.ast.StoDecl toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.StoDecl(((Changemode) T_changeMode).getChangemode(),
                N_typedIdent.toAbsSyntax());
    }
}
