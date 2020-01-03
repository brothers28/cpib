package ch.fhnw.edu.cpib.cst.interfaces;

public interface IProgram extends IProduction {
    ch.fhnw.edu.cpib.ast.Program toAbsSyntax();
}
