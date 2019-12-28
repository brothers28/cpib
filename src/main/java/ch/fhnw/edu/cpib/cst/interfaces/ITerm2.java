package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.ast.interfaces.IExpr;

public interface ITerm2 extends IProduction {
    public IExpr toAbsSyntax();
}
