package ch.fhnw.edu.cpib.cst.interfaces;

public interface IExpr extends IProduction {
    ch.fhnw.edu.cpib.ast.interfaces.IExpr toAbsSyntax();
}
