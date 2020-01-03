package ch.fhnw.edu.cpib.ast.interfaces;

import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;

public interface IExpr extends IAstNode {

    Types getType();

    LRValue getLRValue();

    void executeTypeCast(Types type);

}
