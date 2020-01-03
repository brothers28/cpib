package ch.fhnw.edu.cpib.cst.interfaces;

public interface ICpsCmd extends IProduction {
    ch.fhnw.edu.cpib.ast.CpsCmd toAbsSyntax();
}
