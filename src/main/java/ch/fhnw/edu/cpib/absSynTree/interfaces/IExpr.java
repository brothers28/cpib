package ch.fhnw.edu.cpib.absSynTree.interfaces;

import ch.fhnw.edu.cpib.scanner.enumerations.LRValue;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;

public interface IExpr extends IAbsSynTreeNode {

	public Types getType();

	public LRValue getLRValue();
}
