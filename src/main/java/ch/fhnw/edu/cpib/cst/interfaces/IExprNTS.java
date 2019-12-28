package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.ast.interfaces.IExpr;

public interface IExprNTS extends IProduction {
    public IExpr toAbsSyntax(IExpr expr);
}
