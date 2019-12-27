package ch.fhnw.edu.cpib.cst.interfaces;

import java.util.ArrayList;

public interface ICpsStoDecl extends IProduction {
    public ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> toAbsSyn();
}
