package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.TypeIdent;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ITypedIdent;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.Token;
import ch.fhnw.edu.cpib.scanner.keywords.Type;

// typedIdent ::= IDENT COLON TYPE
public class TypedIdent extends Production implements ITypedIdent {
    protected final Token T_ident;
    protected final Token T_colon;
    protected final Token T_type;

    public TypedIdent(final Token t_ident,
                      final Token t_colon,
                      final Token t_type) {
        T_ident = t_ident;
        T_colon = t_colon;
        T_type = t_type;
    }

    @Override
    public TypeIdent toAbsSyn() {
        return new TypeIdent((Ident)T_ident, ((Type)T_type).getType());
    }
}
