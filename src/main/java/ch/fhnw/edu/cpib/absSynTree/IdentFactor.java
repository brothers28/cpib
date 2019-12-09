package ch.fhnw.edu.cpib.absSynTree;

import ch.fhnw.edu.cpib.absSynTree.interfaces.IFactor;
import ch.fhnw.edu.cpib.scanner.Ident;

public abstract class IdentFactor extends AbsSynTreeNode implements IFactor {
	protected Ident ident;
}
