package ch.fhnw.edu.cpib.concSynTree.interfaces;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IExpr;

import java.util.ArrayList;

public interface IExprList extends IProduction {
    public ArrayList<IExpr> toAbsSyn();
}
