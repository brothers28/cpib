package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.ast.TypedIdent;

public interface ITypedIdent extends IProduction {
    TypedIdent toAbsSyntax();
}
