package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.ICastOpr;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.keywords.Type;

// castOpr ::= LBRACKET ATOMTYPE RBRACKET
public class CastOpr extends Production implements ICastOpr {
    protected IToken ts_lBracket;
    protected IToken ts_type;
    protected IToken ts_rBracket;

    public CastOpr(IToken ts_lBracket, IToken ts_type, IToken ts_rBracket) {
        this.ts_lBracket = ts_lBracket;
        this.ts_type = ts_type;
        this.ts_rBracket = ts_rBracket;
    }

    @Override public Types toAbsSyntax() {
        return ((Type) ts_type).getType();
    }
}

