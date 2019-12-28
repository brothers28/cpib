package ch.fhnw.edu.cpib.cst.interfaces;

public interface ICmd extends IProduction {
    public ch.fhnw.edu.cpib.ast.interfaces.ICmd toAbsSyntax();
}
