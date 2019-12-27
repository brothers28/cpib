package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.ast.interfaces.IExpr;

public interface IExprNTS extends IProduction {
    public IExpr toAbsSyn(IExpr expr);
}
