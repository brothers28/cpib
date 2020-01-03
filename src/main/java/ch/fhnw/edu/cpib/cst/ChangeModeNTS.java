package ch.fhnw.edu.cpib.cst;

import ch.fhnw.edu.cpib.cst.interfaces.IChangeModeNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Changemodes;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;
import ch.fhnw.edu.cpib.scanner.keywords.Changemode;

// changeModeNTS ::= CHANGEMODE
public class ChangeModeNTS extends Production implements IChangeModeNTS {
    private IToken ts_changeMode;

    public ChangeModeNTS(IToken ts_changeMode) {
        this.ts_changeMode = ts_changeMode;
    }

    @Override public Changemodes toAbsSyntax() {
        return ((Changemode) ts_changeMode).getChangemode();
    }
}
