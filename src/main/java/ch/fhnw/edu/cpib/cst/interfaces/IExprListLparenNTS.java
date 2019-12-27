package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.ast.interfaces.IExpr;

import java.util.ArrayList;

public interface IExprListLparenNTS extends IProduction {
    public ArrayList<IExpr> toAbsSyn();
}
