package ch.fhnw.edu.cpib.vm.util;

import ch.fhnw.edu.cpib.ast.AstTree;
import ch.fhnw.edu.cpib.vm.ICodeArray;

public class CodeArrayGenerator {
    // TODO: Braucht es diese Klasse?! Kann ja auch direkt auf as ausgeführt werden...
    public ICodeArray convert(AstTree as) throws ICodeArray.CodeTooSmallError {
        return as.getCodeArray();
    }
}
