package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IChangeModeNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Changemodes;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.keywords.Changemode;

// changeModeNTS ::= CHANGEMODE
public class ChangeModeNTS extends Production implements IChangeModeNTS {
    protected final IToken T_changeModeNTS;

    public ChangeModeNTS(final IToken t_changeModeNTS) {
        T_changeModeNTS = t_changeModeNTS;
    }

    @Override public Changemodes toAbsSyntax() {
        return ((Changemode) T_changeModeNTS).getChangemode();
    }
}
