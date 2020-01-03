package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ITypedIdent;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.keywords.Type;

// typedIdent ::= IDENT COLON TYPE
public class TypedIdent extends Production implements ITypedIdent {
    protected IToken ts_ident;
    protected IToken ts_colon;
    protected IToken ts_type;

    public TypedIdent(IToken ts_ident, IToken ts_colon, IToken ts_type) {
        this.ts_ident = ts_ident;
        this.ts_colon = ts_colon;
        this.ts_type = ts_type;
    }

    @Override public ch.fhnw.edu.cpib.ast.TypedIdent toAbsSyntax() {
        return new ch.fhnw.edu.cpib.ast.TypedIdent((Ident) ts_ident, ((Type) ts_type).getType());
    }
}
