package ch.fhnw.edu.cpib.parser;

import ch.fhnw.edu.cpib.absSynTree.AbsSynTree;
import ch.fhnw.edu.cpib.vm.ICodeArray;

public class AbsToCodeArray {
    public ICodeArray convert(AbsSynTree as) throws ICodeArray.CodeTooSmallError {
        return as.getCodeArray();
    }
}
