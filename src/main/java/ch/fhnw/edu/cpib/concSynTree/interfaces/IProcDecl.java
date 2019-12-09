package ch.fhnw.edu.cpib.concSynTree.interfaces;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IDecl;

public interface IProcDecl extends IProduction {
    public IDecl toAbsSyn();
}
