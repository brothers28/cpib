package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IStoDecl;
import ch.fhnw.edu.cpib.cst.interfaces.ITypedIdent;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.keywords.Changemode;

// stoDecl ::= CHANGEMODE typedIdent
public class StoDeclChangeMode extends Production implements IStoDecl {
    private IToken ts_changeMode;
    private ITypedIdent nts_typedIdent;

    public StoDeclChangeMode(IToken ts_changeMode, ITypedIdent nts_typedIdent) {
        this.ts_changeMode = ts_changeMode;
        this.nts_typedIdent = nts_typedIdent;
    }

    @Override public ch.fhnw.edu.cpib.ast.StoDecl toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.StoDecl(((Changemode) ts_changeMode).getChangemode(),
                nts_typedIdent.toAbsSyntax());
    }
}
