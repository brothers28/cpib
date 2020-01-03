package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.ast.interfaces.IExpr;

public interface ITerm3NTS extends IProduction {
    IExpr toAbsSyntax(IExpr expr);
}
