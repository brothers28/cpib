package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.ast.interfaces.IExpr;

public interface ITerm1 extends IProduction {
    public IExpr toAbsSyn();
}
