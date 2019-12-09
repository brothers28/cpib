package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.IChangeModeNTS;
import ch.fhnw.edu.cpib.scanner.enumerations.Changemodes;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.keywords.Changemode;
import ch.fhnw.edu.cpib.scanner.Token;

// changeModeNTS ::= CHANGEMODE
public class ChangeModeNTS extends Production implements IChangeModeNTS {
    protected final Token T_changeModeNTS;

    public ChangeModeNTS(final Token t_changeModeNTS) {
        T_changeModeNTS = t_changeModeNTS;
    }

    @Override
    public Changemodes toAbySyn() {
        return ((Changemode)T_changeModeNTS).getChangemode();
    }
}
