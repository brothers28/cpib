package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.ast.interfaces.IExpr;

public interface ITerm3NTS extends IProduction {
    public IExpr toAbsSyntax(IExpr expr);
}
