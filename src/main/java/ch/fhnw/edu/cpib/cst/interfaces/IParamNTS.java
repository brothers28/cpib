package ch.fhnw.edu.cpib.cst.interfaces;

import java.util.ArrayList;

public interface IParamNTS extends IProduction {
    public ArrayList<ch.fhnw.edu.cpib.ast.Param> toAbsSyn(ArrayList<ch.fhnw.edu.cpib.ast.Param> temp);
}

