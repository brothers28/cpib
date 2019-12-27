package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICastOpr;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.keywords.Type;

// castOpr ::= LBRACKET ATOMTYPE RBRACKET
public class CastOpr extends Production implements ICastOpr {
    protected final IToken T_lBracket;
    protected final IToken T_type;
    protected final IToken T_rBracket;

    public CastOpr(final IToken t_lBracket, final IToken t_type, final IToken t_rBracket) {
        T_lBracket = t_lBracket;
        T_type = t_type;
        T_rBracket = t_rBracket;
    }

    @Override public Types toAbsSyn() {
        // return new Operator((Token) T_lBracket, ((Type) T_type).getType()) {
        // FIXME: Was muss hier als Abstract Object zurückgegeben werden
        return ((Type) T_type).getType();
    }
}

