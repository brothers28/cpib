package ch.fhnw.edu.cpib.concSynTree.interfaces;

import ch.fhnw.edu.cpib.absSynTree.TypeIdent;

public interface ITypedIdent extends IProduction {
    public TypeIdent toAbsSyn();
}
