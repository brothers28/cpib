package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.ast.interfaces.IDecl;

import java.util.ArrayList;

public interface IGlobalNTS extends IProduction {
    public ArrayList<IDecl> toAbsSyntax();
}
