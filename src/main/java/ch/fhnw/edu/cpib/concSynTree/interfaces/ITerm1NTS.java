package ch.fhnw.edu.cpib.concSynTree.interfaces;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr;

public interface ITerm1NTS extends IProduction {
    public IExpr toAbsSyn(IExpr expr);
}
