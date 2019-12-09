package ch.fhnw.edu.cpib.concSynTree;

import ch.fhnw.edu.cpib.concSynTree.interfaces.ICmd;
import ch.fhnw.edu.cpib.scanner.Token;

// cmd ::= SKIP
public class CmdSkip extends Production implements ICmd {
    protected final Token T_skip;

    public CmdSkip(final Token T_skip) {
        this.T_skip = T_skip;
    }

    @Override
    public ch.fhnw.edu.cpib.absSynTree.interfaces.ICmd toAbsSyn() {
        return new ch.fhnw.edu.cpib.absSynTree.SkipCmd();
    }
}