package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.ast.interfaces.IDecl;

public interface IProcDecl extends IProduction {
    public IDecl toAbsSyntax();
}
