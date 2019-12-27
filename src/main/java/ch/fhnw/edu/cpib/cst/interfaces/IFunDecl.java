package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.ast.interfaces.IDecl;

public interface IFunDecl extends IProduction {
    public IDecl toAbsSyn();
}
