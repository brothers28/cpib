package ch.fhnw.edu.cpib.cst;
import ch.fhnw.edu.cpib.ast.TypeIdent;
import ch.fhnw.edu.cpib.cst.interfaces.ITypedIdent;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.keywords.Type;

// typedIdent ::= IDENT COLON TYPE
public class TypedIdent extends Production implements ITypedIdent {
    protected final IToken T_ident;
    protected final IToken T_colon;
    protected final IToken T_type;

    public TypedIdent(final IToken t_ident,
                      final IToken t_colon,
                      final IToken t_type) {
        T_ident = t_ident;
        T_colon = t_colon;
        T_type = t_type;
    }

    @Override
    public TypeIdent toAbsSyn() {
        return new TypeIdent((Ident)T_ident, ((Type)T_type).getType());
    }
}
