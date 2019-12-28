package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IMechModeNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Mechmodes;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.keywords.Mechmode;

// mechModeNTS ::= MECHMODE
public class MechModeNTS extends Production implements IMechModeNTS {
    protected final IToken T_mechMode;

    public MechModeNTS(final IToken T_mechMode) {
        this.T_mechMode = T_mechMode;
    }

    @Override public Mechmodes toAbsSyntax() {
        return ((Mechmode) T_mechMode).getMechmode();
    }
}
