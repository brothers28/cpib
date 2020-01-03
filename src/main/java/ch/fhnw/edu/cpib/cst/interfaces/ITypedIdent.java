package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.ast.TypeIdent;

public interface ITypedIdent extends IProduction {
    TypeIdent toAbsSyntax();
}
