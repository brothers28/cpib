package ch.fhnw.edu.cpib.cst.interfaces;

import ch.fhnw.edu.cpib.ast.StoDecl;
import ch.fhnw.edu.cpib.ast.interfaces.IDecl;
import ch.fhnw.edu.cpib.cst.Production;
import ch.fhnw.edu.cpib.scanner.Ident;
import ch.fhnw.edu.cpib.scanner.enumerations.Changemodes;
import ch.fhnw.edu.cpib.scanner.enumerations.Flowmodes;
import ch.fhnw.edu.cpib.scanner.enumerations.Mechmodes;

import java.util.ArrayList;

public interface Epsilon {
    class GlobalNTS extends Production implements IGlobalNTS {
        @Override public ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IDecl> toAbsSyntax() {
            return new ArrayList<>();
        }
    }

    class CpsDeclNTS extends Production implements ICpsDeclNTS {
        @Override public ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IDecl> toAbsSyntax(ArrayList<IDecl> temp) {
            return temp;
        }
    }

    class FunDeclNTS extends Production implements IFunDeclNTS {
        @Override public ArrayList<ch.fhnw.edu.cpib.ast.StoDecl> toAbsSyntax() {
            return new ArrayList<>();
        }
    }

    class ParamListNTS extends Production implements IParamListNTS {
        @Override public ArrayList<ch.fhnw.edu.cpib.ast.Param> toAbsSyntax() {
            return new ArrayList<>();
        }
    }

    class ParamNTS extends Production implements IParamNTS {
        @Override public ArrayList<ch.fhnw.edu.cpib.ast.Param> toAbsSyntax(ArrayList<ch.fhnw.edu.cpib.ast.Param> temp) {
            return temp;
        }
    }

    class FlowModeNTS extends Production implements IFlowModeNTS {
        @Override public Flowmodes toAbsSyntax() {
            return null;
        }
    }

    class ChangeModeNTS extends Production implements IChangeModeNTS {
        @Override public Changemodes toAbsSyntax() {
            return null;
        }
    }

    class MechModeNTS extends Production implements IMechModeNTS {
        @Override public Mechmodes toAbsSyntax() {
            return null;
        }
    }

    class CpsCmdNTS extends Production implements ICpsCmdNTS {
        @Override public ArrayList<ch.fhnw.edu.cpib.ast.interfaces.ICmd> toAbsSyntax(
                ArrayList<ch.fhnw.edu.cpib.ast.interfaces.ICmd> temp) {
            return temp;
        }
    }

    class IfElseNTS extends Production implements IIfElseNTS {
        @Override public ch.fhnw.edu.cpib.ast.CpsCmd toAbsSyntax() {
            return null;
        }
    }

    class ExprNTS extends Production implements IExprNTS {
        @Override public ch.fhnw.edu.cpib.ast.interfaces.IExpr toAbsSyntax(ch.fhnw.edu.cpib.ast.interfaces.IExpr expr) {
            return expr;
        }
    }

    class Term1NTS extends Production implements ITerm1NTS {
        @Override public ch.fhnw.edu.cpib.ast.interfaces.IExpr toAbsSyntax(ch.fhnw.edu.cpib.ast.interfaces.IExpr expr) {
            return expr;
        }
    }

    class Term2NTS extends Production implements ITerm2NTS {
        @Override public ch.fhnw.edu.cpib.ast.interfaces.IExpr toAbsSyntax(ch.fhnw.edu.cpib.ast.interfaces.IExpr expr) {
            return expr;
        }
    }

    class Term3NTS extends Production implements ITerm3NTS {
        @Override public ch.fhnw.edu.cpib.ast.interfaces.IExpr toAbsSyntax(ch.fhnw.edu.cpib.ast.interfaces.IExpr expr) {
            return expr;
        }
    }

    class FactorNTS extends Production implements IFactorNTS {
        @Override public ch.fhnw.edu.cpib.ast.interfaces.IFactor toAbsSyntax(Ident ident) {
            return new ch.fhnw.edu.cpib.ast.InitFactor(ident, false);
        }
    }

    class ExprListLParenNTS extends Production implements IExprListLparenNTS {
        @Override public ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IExpr> toAbsSyntax() {
            return new ArrayList<>();
        }
    }

    class ExprListNTS extends Production implements IExprListNTS {
        @Override public ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IExpr> toAbsSyntax(
                ArrayList<ch.fhnw.edu.cpib.ast.interfaces.IExpr> temp) {
            return temp;
        }
    }

    class CpsStoDeclNTS extends Production implements ICpsStoDeclNTS {
        @Override public ArrayList<StoDecl> toAbsSyntax(ArrayList<StoDecl> temp) {
            return temp;
        }
    }

    class ProcDeclNTS extends Production implements IProcDeclNTS {
        @Override public ArrayList<StoDecl> toAbsSyntax() {
            return new ArrayList<>();
        }
    }
}
