package ch.fhnw.edu.cpib.concSynTree.interfaces;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr;

public interface ITerm3NTS extends IProduction {
    public IExpr toAbsSyn(IExpr expr);
}
