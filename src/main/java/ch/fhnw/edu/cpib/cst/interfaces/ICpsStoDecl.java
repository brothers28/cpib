package ch.fhnw.edu.cpib.cst.interfaces;

import java.util.ArrayList;

public interface ICpsStoDecl extends IProduction {
    ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> toAbsSyntax();
}
