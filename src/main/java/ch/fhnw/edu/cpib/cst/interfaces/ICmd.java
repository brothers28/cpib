package ch.fhnw.edu.cpib.cst.interfaces;

public interface ICmd extends IProduction {
    ch.fhnw.edu.cpib.ast.interfaces.ICmd toAbsSyntax();
}
