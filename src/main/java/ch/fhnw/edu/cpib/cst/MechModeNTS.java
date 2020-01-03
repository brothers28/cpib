package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IMechModeNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Mechmodes;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.keywords.Mechmode;

// mechModeNTS ::= MECHMODE
public class MechModeNTS extends Production implements IMechModeNTS {
    private IToken ts_mechMode;

    public MechModeNTS(IToken ts_mechMode) {
        this.ts_mechMode = ts_mechMode;
    }

    @Override public Mechmodes toAbsSyntax() {
        return ((Mechmode) ts_mechMode).getMechmode();
    }
}
