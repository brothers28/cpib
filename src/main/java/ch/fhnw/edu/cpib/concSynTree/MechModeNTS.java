package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.IMechModeNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Mechmodes;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.keywords.Mechmode;
import ch.fhnw.edu.cpib.scanner.Token;

// mechModeNTS ::= MECHMODE
public class MechModeNTS extends Production implements IMechModeNTS {
    protected final Token T_mechMode;

    public MechModeNTS(final Token T_mechMode) {
        this.T_mechMode = T_mechMode;
    }

    @Override
    public Mechmodes toAbsSyn() {
        return ((Mechmode)T_mechMode).getMechmode();
    }
}