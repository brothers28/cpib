package ch.fhnw.edu.cpib.cst.interfaces;

import java.util.ArrayList;

public interface IParamList extends IProduction {
    public ArrayList<ch.fhnw.edu.cpib.ast.Param> toAbsSyn();
}
