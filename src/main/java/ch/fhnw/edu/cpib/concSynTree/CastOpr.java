package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.absSynTree.CastFactor;
import ch.fhnw.edu.cpib.absSynTree.TypeIdent;
import ch.fhnw.edu.cpib.concSynTree.interfaces.ICastOpr;
import ch.fhnw.edu.cpib.concSynTree.interfaces.IMonadicOpr;
import ch.fhnw.edu.cpib.scanner.Base;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.Token;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.scanner.keywords.Type;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;

/**
 * @Author Hussein Farzi
 */

// castOpr ::= LBRACKET ATOMTYPE RBRACKET
public class CastOpr extends Production implements ICastOpr {
    protected final Token T_lBracket;
    protected final Token T_type;
    protected final Token T_rBracket;

    public CastOpr(final Token t_lBracket,
            final Token t_type,
            final Token t_rBracket) {
        T_lBracket = t_lBracket;
        T_type = t_type;
        T_rBracket = t_rBracket;
    }

    @Override
    public Types toAbsSyn() {
       // return new Operator((Token) T_lBracket, ((Type) T_type).getType()) {
        // FIXME: Was muss hier als Abstract Object zurückgegeben werden
        return ((Type)T_type).getType();
    }
}

