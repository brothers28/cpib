package ch.fhnw.edu.cpib.concSynTree.interfaces;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr;

public interface ITerm2NTS extends IProduction {
    public IExpr toAbsSyn(IExpr expr);
}
