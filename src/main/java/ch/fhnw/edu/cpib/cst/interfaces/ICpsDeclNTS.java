package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.ast.interfaces.IDecl;

import java.util.ArrayList;

public interface ICpsDeclNTS extends IProduction {
    ArrayList<IDecl> toAbsSyntax(ArrayList<IDecl> temp);
}
