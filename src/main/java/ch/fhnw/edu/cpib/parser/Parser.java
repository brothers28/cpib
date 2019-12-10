package ch.fhnw.edu.cpib.parser;

import ch.fhnw.edu.cpib.absSynTree.AbsSynTree;
import ch.fhnw.edu.cpib.concSynTree.*;
import ch.fhnw.edu.cpib.concSynTree.interfaces.*;
import ch.fhnw.edu.cpib.errors.*;
import ch.fhnw.edu.cpib.scanner.Base;
import ch.fhnw.edu.cpib.scanner.Token;
import ch.fhnw.edu.cpib.scanner.TokenList;
import ch.fhnw.edu.cpib.scanner.enumerations.Operators;
import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.symbols.Operator;

public class Parser {
    // TODO: anpassen überall wo isFirstRelOperator geprüft wird
    private TokenList tokens;
    private Base currentToken;
    private Terminals currentTerminal;
    private int counter;
    private boolean isFirstRelOpr = true;
    private Operators lastRelOprValue;

    public Parser(TokenList tokens) {
        this.tokens = tokens;
        nextToken();
    }

    private void nextToken() {
        currentToken = (Base) tokens.get(counter++);
        currentTerminal = currentToken.getTerminal();
    }

    private Token consume(Terminals expectedTerminal) throws GrammarError {
        if (this.currentTerminal == expectedTerminal) {
            Token consumedTerminal = this.currentToken;
            if (this.currentTerminal != Terminals.SENTINEL) {
                nextToken();
            }
            return consumedTerminal;
        } else {
            throw new GrammarError(expectedTerminal, this.currentToken.getTerminal());
        }
    }

    public AbsSynTree parse()
            throws GrammarError, NameAlreadyDeclaredError, NameNotDeclaredError, NameAlreadyGloballyDeclaredError,
            LRValueError, InvalidParamCountError, AlreadyInitializedError, TypeCheckError, NotInitializedError,
            GlobalInitializationProhibitedError, CannotAssignToConstError {
        System.out.println("Start parsing:\n");
        IProgram program = program();
        consume(Terminals.SENTINEL);

        System.out.println("\n---------------------------------------------------\n");
        System.out.println("Concrete Syntax Tree:\n");
        System.out.println(program.toString(""));

        System.out.println("\n---------------------------------------------------\n");
        System.out.println("Abstract Syntax Tree:\n");
        AbsSynTree absSynTree = new AbsSynTree(program);
        System.out.println(absSynTree.toString());

        System.out.println("\n---------------------------------------------------\n");
        System.out.print("Scope checking:");
        absSynTree.doScopeChecking();
        System.out.println(" OK!");

        System.out.println("\n---------------------------------------------------\n");
        System.out.print("Type checking:");
        absSynTree.doTypeChecking();
        System.out.println(" OK!");

        System.out.println("\n---------------------------------------------------\n");
        System.out.print("Init checking:");
        absSynTree.doInitChecking();
        System.out.println(" OK!");

        return absSynTree;
    }

    // program ::= PROGRAM IDENT globalNTS DO cpsCmd ENDPROGRAM
    private IProgram program() throws GrammarError {
        if (currentTerminal == Terminals.PROGRAM) {
            System.out.println("program ::= PROGRAM IDENT <globalNTS> DO <cpsCmd> ENDPROGRAM");
            Token T_program = consume(Terminals.PROGRAM);
            Token T_ident = consume(Terminals.IDENT);
            IGlobalNTS N_globalNTS = globalNTS();
            Token T_do = consume(Terminals.DO);
            ICpsCmd N_cpsCmd = cpsCmd();
            Token T_endprogram = consume(Terminals.ENDPROGRAM);
            return new Program(T_program, T_ident, N_globalNTS, T_do, N_cpsCmd, T_endprogram);
        } else {
            throw new GrammarError(Terminals.PROGRAM, currentTerminal);
        }
    }

    // globalNTS ::= GLOBAL cpsDecl
    // globalNTS ::= ε
    private IGlobalNTS globalNTS() throws GrammarError {
        if (currentTerminal == Terminals.GLOBAL) {
            System.out.println("globalNTS ::= GLOBAL <cpsDecl>");
            Token T_global = consume(Terminals.GLOBAL);
            ICpsDecl N_cpsDecl = cpsDecl();
            return new GlobalNTS(T_global, N_cpsDecl);
        } else if (currentTerminal == Terminals.DO) {
            System.out.println("globalNTS ::= ε");
            return new Epsilon.GlobalNTS();
        } else {
            throw new GrammarError(Terminals.GLOBALNTS, currentTerminal);
        }
    }

    // cpsDecl ::= decl cpsDeclNTS
    private ICpsDecl cpsDecl() throws GrammarError {
        if (currentTerminal == Terminals.PROC || currentTerminal == Terminals.FUN
                || currentTerminal == Terminals.CHANGEMOD || currentTerminal == Terminals.IDENT) {
            System.out.println("cpsDecl ::= <decl> <cpsDeclNTS>");
            IDecl N_decl = decl();
            ICpsDeclNTS N_cpsDeclNTS = cpsDeclNTS();
            return new CpsDecl(N_decl, N_cpsDeclNTS);
        } else {
            throw new GrammarError(Terminals.CPSDECL, currentTerminal);
        }
    }

    // cpsDeclNTS ::= SEMICOLON decl cpsDeclNTS
    // cpsDeclNTS ::= ε
    private ICpsDeclNTS cpsDeclNTS() throws GrammarError {
        if (currentTerminal == Terminals.SEMICOLON) {
            System.out.println("cpsDeclNTS ::= SEMICOLON <decl> <cpsDeclNTS>");
            Token T_semicolon = consume(Terminals.SEMICOLON);
            IDecl N_decl = decl();
            ICpsDeclNTS N_cpsDeclNTS = cpsDeclNTS();
            return new CpsDeclNTS(T_semicolon, N_decl, N_cpsDeclNTS);
        } else if (currentTerminal == Terminals.DO) {
            System.out.println("cpsDeclNTS ::= ε");
            return new Epsilon.CpsDeclNTS();
        } else {
            throw new GrammarError(Terminals.CPSDECLNTS, currentTerminal);
        }
    }

    // decl ::= stoDecl
    // decl ::= funDecl
    // decl ::= procDecl
    private IDecl decl() throws GrammarError {
        if (currentTerminal == Terminals.CHANGEMOD || currentTerminal == Terminals.IDENT) {
            System.out.println("decl ::= <stoDecl>");
            IStoDecl N_stoDecl = stoDecl();
            return new DeclStoDecl(N_stoDecl);
        } else if (currentTerminal == Terminals.FUN) {
            System.out.println("decl ::= <funDecl>");
            IFunDecl N_funDecl = funDecl();
            return new DeclFunDecl(N_funDecl);
        } else if (currentTerminal == Terminals.PROC) {
            System.out.println("decl ::= <procDecl>");
            IProcDecl N_procDecl = procDecl();
            return new DeclProcDecl(N_procDecl);
        } else {
            throw new GrammarError(Terminals.DECL, currentTerminal);
        }
    }

    // stoDecl ::= typedIdent
    // stoDecl ::= CHANGEMOD typedIdent
    private IStoDecl stoDecl() throws GrammarError {
        if (currentTerminal == Terminals.IDENT) {
            System.out.println("stoDecl ::= <typedIdent>");
            ITypedIdent N_typedIdent = typedIdent();
            return new StoDeclTypedIdent(N_typedIdent);
        } else if (currentTerminal == Terminals.CHANGEMOD) {
            System.out.println("stoDecl ::= CHANGEMOD <typedIdent>");
            Token T_changeMode = consume(Terminals.CHANGEMOD);
            ITypedIdent N_typedIdent = typedIdent();
            return new StoDeclChangeMode(T_changeMode, N_typedIdent);
        } else {
            throw new GrammarError(Terminals.STODECL, currentTerminal);
        }
    }

    // typedIdent ::= IDENT COLON TYPE
    private ITypedIdent typedIdent() throws GrammarError {
        if (currentTerminal == Terminals.IDENT) {
            System.out.println("typedIdent ::= IDENT COLON TYPE");
            Token T_ident = consume(Terminals.IDENT);
            Token T_colon = consume(Terminals.COLON);
            Token T_type = consume(Terminals.TYPE);
            return new TypedIdent(T_ident, T_colon, T_type);
        } else {
            throw new GrammarError(Terminals.TYPEDIDENT, currentTerminal);
        }
    }

    // funDecl ::= FUN IDENT paramList RETURNS stoDecl funDeclNTS DO cpsCmd ENDFUN
    private IFunDecl funDecl() throws GrammarError {
        if (currentTerminal == Terminals.FUN) {
            System.out.println("funDecl ::= FUN IDENT <paramList> RETURNS <stoDecl> <funDeclNTS> DO <cpsCmd> ENDFUN");
            Token T_fun = consume(Terminals.FUN);
            Token T_ident = consume(Terminals.IDENT);
            IParamList N_paramList = paramList();
            Token T_returns = consume(Terminals.RETURNS);
            IStoDecl N_stoDecl = stoDecl();
            IFunDeclNTS N_funDeclNTS = funDeclNTS();
            Token T_do = consume(Terminals.DO);
            ICpsCmd N_cpsCmd = cpsCmd();
            Token T_endFun = consume(Terminals.ENDFUN);
            return new FunDecl(T_fun, T_ident, N_paramList, T_returns, N_stoDecl, N_funDeclNTS, T_do, N_cpsCmd,
                    T_endFun);
        } else {
            throw new GrammarError(Terminals.FUNDECL, currentTerminal);
        }
    }

    // funDeclNTS ::= LOCAL cpsStoNTS
    // funDeclNTS ::= ε
    private IFunDeclNTS funDeclNTS() throws GrammarError {
        if (currentTerminal == Terminals.LOCAL) {
            System.out.println("funDeclNTS := LOCAL <cpsStoDecl>");
            Token T_local = consume(Terminals.LOCAL);
            ICpsStoDecl N_cpsStoDecl = cpsStoDecl();
            return new FunDeclNTS(T_local, N_cpsStoDecl);
        } else if (currentTerminal == Terminals.DO) {
            System.out.println("funDeclNTS := ε");
            return new Epsilon.FunDeclNTS();
        } else {
            throw new GrammarError(Terminals.FUNDECLNTS, currentTerminal);
        }
    }

    // paramList ::= LPAREN paramListNTS RPAREN
    private IParamList paramList() throws GrammarError {
        if (currentTerminal == Terminals.LPAREN) {
            System.out.println("paramList ::= LPAREN <paramListNTS> RPAREN");
            Token T_lParen = consume(Terminals.LPAREN);
            IParamListNTS N_paramListNTS = paramListNTS();
            Token T_rParen = consume(Terminals.RPAREN);
            return new ParamList(T_lParen, N_paramListNTS, T_rParen);
        } else {
            throw new GrammarError(Terminals.PARAMLIST, currentTerminal);
        }
    }

    // paramListNTS ::= param paramNTS
    // paramListNTS ::= ε
    private IParamListNTS paramListNTS() throws GrammarError {
        if (currentTerminal == Terminals.IDENT || currentTerminal == Terminals.MECHMODE
                || currentTerminal == Terminals.CHANGEMOD) {
            System.out.println("paramListNTS ::= <param> <paramNTS>");
            IParam N_param = param();
            IParamNTS N_paramNTS = paramNTS();
            return new ParamListNTS(N_param, N_paramNTS);
        } else if (currentTerminal == Terminals.RPAREN) {
            System.out.println("paramListNTS ::= ε");
            return new Epsilon.ParamListNTS();
        } else {
            throw new GrammarError(Terminals.PARAMLISTNTS, currentTerminal);
        }
    }

    // paramNTS ::= COMMA param paramNTS
    // paramNTS ::= ε
    private IParamNTS paramNTS() throws GrammarError {
        if (currentTerminal == Terminals.COMMA) {
            System.out.println("paramNTS ::= COMMA <param> <paramNTS>");
            Token T_comma = consume(Terminals.COMMA);
            IParam N_param = param();
            IParamNTS N_paramNTS = paramNTS();
            return new ParamNTS(T_comma, N_param, N_paramNTS);
        } else if (currentTerminal == Terminals.RPAREN) {
            System.out.println("paramNTS ::= ε");
            return new Epsilon.ParamNTS();
        } else {
            throw new GrammarError(Terminals.PARAMNTS, currentTerminal);
        }
    }

    // param ::= flowModeNTS mechModeNTS changeModNTS typedIdent
    private IParam param() throws GrammarError {
        if (currentTerminal == Terminals.IDENT || currentTerminal == Terminals.FLOWMODE
                || currentTerminal == Terminals.CHANGEMOD || currentTerminal == Terminals.MECHMODE) {
            System.out.println("param ::= <flowModeNTS> <mechModeNTS> <changeModNTS> <typedIdent>");
            IFlowModeNTS N_flowModeNTS = flowModeNTS();
            IMechModeNTS N_mechModeNTS = mechModeNTS();
            IChangeModeNTS N_changeModeNTS = changeModNTS();
            ITypedIdent N_typedIdent = typedIdent();
            return new Param(N_flowModeNTS, N_mechModeNTS, N_changeModeNTS, N_typedIdent);
        } else {
            throw new GrammarError(Terminals.PARAM, currentTerminal);
        }
    }

    // flowModeNTS ::= FLOWMODE
    // floModeNTS ::= ε
    private IFlowModeNTS flowModeNTS() throws GrammarError {
        if (currentTerminal == Terminals.FLOWMODE) {
            System.out.println("flowModeNTS ::= FLOWMODE");
            Token T_flowMode = consume(Terminals.FLOWMODE);
            return new FlowModeNTS(T_flowMode);
        } else if (currentTerminal == Terminals.IDENT || currentTerminal == Terminals.MECHMODE) {
            System.out.println("flowModeNTS ::= ε");
            return new Epsilon.FlowModeNTS();
        } else {
            throw new GrammarError(Terminals.FLOWMODENTS, currentTerminal);
        }
    }

    // changeModNTS ::= CHANGEMOD
    // changeModNTS::= ε
    private IChangeModeNTS changeModNTS() throws GrammarError {
        if (currentTerminal == Terminals.CHANGEMOD) {
            System.out.println("changeModNTS ::= CHANGEMOD");
            Token T_changeMode = consume(Terminals.CHANGEMOD);
            return new ChangeModeNTS(T_changeMode);
        } else if (currentTerminal == Terminals.IDENT || currentTerminal == Terminals.MECHMODE) {
            System.out.println("changeModNTS ::= ε");
            return new Epsilon.ChangeModeNTS();
        } else {
            throw new GrammarError(Terminals.CHANGEMODENTS, currentTerminal);
        }
    }

    // mechModeNTS ::= MECHMODE
    // mechModeNTS::= ε
    private IMechModeNTS mechModeNTS() throws GrammarError {
        if (currentTerminal == Terminals.MECHMODE) {
            System.out.println("mechModeNTS ::= MECHMODE");
            Token T_mechMode = consume(Terminals.MECHMODE);
            return new MechModeNTS(T_mechMode);
        } else if (currentTerminal == Terminals.IDENT) {
            System.out.println("mechModeNTS ::= ε");
            return new Epsilon.MechModeNTS();
        } else {
            throw new GrammarError(Terminals.MECHMODENTS, currentTerminal);
        }
    }

    // cpsCmd ::= cmd cpsCmdNTS
    private ICpsCmd cpsCmd() throws GrammarError {
        if (currentTerminal == Terminals.DEBUGOUT || currentTerminal == Terminals.DEBUGIN
                || currentTerminal == Terminals.CALL || currentTerminal == Terminals.WHILE
                || currentTerminal == Terminals.IF || currentTerminal == Terminals.LPAREN
                || currentTerminal == Terminals.ADDOPR || currentTerminal == Terminals.NOTOPR
                || currentTerminal == Terminals.IDENT || currentTerminal == Terminals.LITERAL
                || currentTerminal == Terminals.SKIP) {
            System.out.println("cpsCMD ::= <cmd> <cpsCmdNTS>");
            ICmd N_cmd = cmd();
            ICpsCmdNTS N_cpsCmdNTS = cpsCmdNTS();
            return new CpsCmd(N_cmd, N_cpsCmdNTS);
        } else {
            throw new GrammarError(Terminals.CPSCMD, currentTerminal);
        }
    }

    // cpsCmdNTS ::= SEMICOLON cmd cpsCmdNTS
    // cpsCmdNTS ::= ε
    private ICpsCmdNTS cpsCmdNTS() throws GrammarError {
        if (currentTerminal == Terminals.SEMICOLON) {
            System.out.println("cpsCmdNTS ::= SEMICOLON <cmd> <cpsCmdNTS>");
            Token T_semicolon = consume(Terminals.SEMICOLON);
            isFirstRelOpr = true;
            ICmd N_cmd = cmd();
            ICpsCmdNTS N_cpsCmdNTS = cpsCmdNTS();
            return new CpsCmdNTS(T_semicolon, N_cmd, N_cpsCmdNTS);
        } else if (currentTerminal == Terminals.ENDPROC || currentTerminal == Terminals.ENDWHILE
                || currentTerminal == Terminals.ENDIF || currentTerminal == Terminals.ELSE
                || currentTerminal == Terminals.ENDFUN || currentTerminal == Terminals.ENDPROGRAM) {
            System.out.println("cpsCmdNTS ::= ε");
            return new Epsilon.CpsCmdNTS();
        } else {
            throw new GrammarError(Terminals.CPSCMDNTS, currentTerminal);
        }
    }

    // cmd ::= SKIP
    // cmd ::= expr BECOMES expr
    // cmd ::= IF expr THEN cpsCmd ifelseNTS ENDIF
    // cmd ::= WHILE expr DO cpsCmd ENDWHILE
    // cmd ::= CALL IDENT exprList
    // cmd ::= DEBUGIN expr
    // cmd ::= DEBUGOUT expr
    private ICmd cmd() throws GrammarError {
        if (currentTerminal == Terminals.SKIP) {
            System.out.println("cmd ::= SKIP");
            Token T_skip = consume(Terminals.SKIP);
            return new CmdSkip(T_skip);
        } else if (currentTerminal == Terminals.LPAREN || currentTerminal == Terminals.ADDOPR
                || currentTerminal == Terminals.NOTOPR || currentTerminal == Terminals.IDENT
                || currentTerminal == Terminals.LITERAL) {
            System.out.println("cmd ::= <expr> BECOMES <expr>");
            IExpr N_expr1 = expr();
            Token T_becomes = consume(Terminals.BECOMES);
            IExpr N_expr2 = expr();
            return new CmdExpr(N_expr1, T_becomes, N_expr2);
        } else if (currentTerminal == Terminals.IF) {
            System.out.println("cmd ::= IF <expr> THEN <cpsCmd> <ifelseNTS> ENDIF");
            Token T_if = consume(Terminals.IF);
            IExpr N_expr = expr();
            Token T_then = consume(Terminals.THEN);
            isFirstRelOpr = true;
            ICpsCmd N_cpsCmd = cpsCmd();
            IIfElseNTS N_ifelseNTS = ifElseNTS();
            Token T_endif = consume(Terminals.ENDIF);
            return new CmdIfThen(T_if, N_expr, T_then, N_cpsCmd, N_ifelseNTS, T_endif);
        } else if (currentTerminal == Terminals.WHILE) {
            System.out.println("cmd ::= WHILE <expr> DO <cpsCmd> ENDWHILE");
            Token T_while = consume(Terminals.WHILE);
            IExpr N_expr = expr();
            Token T_do = consume(Terminals.DO);
            isFirstRelOpr = true;
            ICpsCmd N_cpsCmd = cpsCmd();
            Token T_endwhile = consume(Terminals.ENDWHILE);
            return new CmdWhileDo(T_while, N_expr, T_do, N_cpsCmd, T_endwhile);
        } else if (currentTerminal == Terminals.CALL) {
            System.out.println("cmd ::= CALL IDENT <exprList>");
            Token T_call = consume(Terminals.CALL);
            Token T_ident = consume(Terminals.IDENT);
            IExprList N_exprList = exprList();
            return new CmdCallIdentExprList(T_call, T_ident, N_exprList);
        } else if (currentTerminal == Terminals.DEBUGIN) {
            System.out.println("cmd ::= DEBUGIN <expr>");
            Token T_debugIn = consume(Terminals.DEBUGIN);
            IExpr N_expr = expr();
            return new CmdDebugIn(T_debugIn, N_expr);
        } else if (currentTerminal == Terminals.DEBUGOUT) {
            System.out.println("cmd ::= DEBUGOUT <expr>");
            Token T_debugOut = consume(Terminals.DEBUGOUT);
            IExpr N_expr = expr();
            return new CmdDebugOut(T_debugOut, N_expr);
        } else {
            throw new GrammarError(Terminals.CMD, currentTerminal);
        }
    }

    // ifelseNTS ::= ELSE cpsCmd
    // ifelseNTS ::= ε
    private IIfElseNTS ifElseNTS() throws GrammarError {
        if (currentTerminal == Terminals.ELSE) {
            System.out.println("ifelseNTS ::= ELSE <cpsCmd>");
            Token T_else = consume(Terminals.ELSE);
            ICpsCmd N_cpsCmd = cpsCmd();
            return new IfElseNTS(T_else, N_cpsCmd);
        } else if (currentTerminal == Terminals.ENDIF) {
            System.out.println("ifelseNTS ::= ε");
            return new Epsilon.IfElseNTS();
        } else {
            throw new GrammarError(Terminals.IFELSENTS, currentTerminal);
        }
    }

    // expr ::= term1 exprNTS
    private IExpr expr() throws GrammarError {
        if (currentTerminal == Terminals.LPAREN || currentTerminal == Terminals.ADDOPR
                || currentTerminal == Terminals.NOTOPR || currentTerminal == Terminals.IDENT
                || currentTerminal == Terminals.LITERAL) {
            System.out.println("expr ::= <term1> <exprNTS>");
            ITerm1 N_term1 = term1();
            IExprNTS N_exprNTS = exprNTS();
            return new Expr(N_term1, N_exprNTS);
        } else {
            throw new GrammarError(Terminals.EXPR, currentTerminal);
        }
    }

    // exprNTS ::= BOOLOPR term1 exprNTS
    // exprNTS ::= ε
    private IExprNTS exprNTS() throws GrammarError {
        if (currentTerminal == Terminals.BOOLOPR) {
            System.out.println("exprNTS ::= BOOLOPR <term1> <exprNTS>");
            Token T_boolOpr = consume(Terminals.BOOLOPR);
            ITerm1 N_term1 = term1();
            IExprNTS N_exprNTS = exprNTS();
            return new ExprNTS(T_boolOpr, N_term1, N_exprNTS);
        } else if (currentTerminal == Terminals.COMMA || currentTerminal == Terminals.RPAREN
                || currentTerminal == Terminals.COLON || currentTerminal == Terminals.DO
                || currentTerminal == Terminals.THEN || currentTerminal == Terminals.ENDPROC
                || currentTerminal == Terminals.ENDWHILE || currentTerminal == Terminals.ENDIF
                || currentTerminal == Terminals.ELSE || currentTerminal == Terminals.ENDFUN
                || currentTerminal == Terminals.ENDPROGRAM || currentTerminal == Terminals.SEMICOLON
                || currentTerminal == Terminals.BECOMES) {
            System.out.println("exprNTS ::= ε");
            return new Epsilon.ExprNTS();
        } else {
            throw new GrammarError(Terminals.EXPRNTS, currentTerminal);
        }
    }

    // term1 ::= term2 term1NTS
    private ITerm1 term1() throws GrammarError {
        if (currentTerminal == Terminals.LPAREN || currentTerminal == Terminals.ADDOPR
                || currentTerminal == Terminals.NOTOPR || currentTerminal == Terminals.IDENT
                || currentTerminal == Terminals.LITERAL) {
            System.out.println("term1 ::= <term2> <term1NTS>");
            ITerm2 N_term2 = term2();
            ITerm1NTS N_term1NTS = term1NTS();
            return new Term1(N_term2, N_term1NTS);
        } else {
            throw new GrammarError(Terminals.TERM1, currentTerminal);
        }
    }

    // term1NTS ::= RELOPR term2 term1NTS
    // term1NTS ::= ε
    private ITerm1NTS term1NTS() throws GrammarError {
        if (currentTerminal == Terminals.RELOPR) {
            System.out.println("term1NTS ::= RELOPR <term2> <term1NTS>");
            Operators currentRelOprValue = ((Operator) currentToken).getOperator();
            if (isFirstRelOpr) {
                Token T_relOpr = consume(Terminals.RELOPR);
                isFirstRelOpr = false;
                lastRelOprValue = currentRelOprValue;
                ITerm2 N_term2 = term2();
                ITerm1NTS N_term1NTS = term1NTS();
                return new Term1NTS(T_relOpr, N_term2, N_term1NTS);
            } else if (lastRelOprValue == Operators.EQ || (
                    (lastRelOprValue == Operators.LT || lastRelOprValue == Operators.LE) && (
                            currentRelOprValue == Operators.LT || currentRelOprValue == Operators.LE
                                    || currentRelOprValue == Operators.EQ)) || (
                    (lastRelOprValue == Operators.GT || lastRelOprValue == Operators.GE) && (
                            currentRelOprValue == Operators.GT || currentRelOprValue == Operators.GE
                                    || currentRelOprValue == Operators.EQ))) {
                Token T_relOpr = consume(Terminals.RELOPR);
                lastRelOprValue = currentRelOprValue;
                ITerm2 N_term2 = term2();
                ITerm1NTS N_term1NTS = term1NTS();
                return new Term1NTS(T_relOpr, N_term2, N_term1NTS);
            } else {
                throw new GrammarError("Not allowed relation operator: " + currentRelOprValue);
            }
        } else if (currentTerminal == Terminals.COMMA || currentTerminal == Terminals.RPAREN
                || currentTerminal == Terminals.COLON || currentTerminal == Terminals.DO
                || currentTerminal == Terminals.THEN || currentTerminal == Terminals.ENDPROC
                || currentTerminal == Terminals.ENDWHILE || currentTerminal == Terminals.ENDIF
                || currentTerminal == Terminals.ELSE || currentTerminal == Terminals.ENDFUN
                || currentTerminal == Terminals.ENDPROGRAM || currentTerminal == Terminals.SEMICOLON
                || currentTerminal == Terminals.BECOMES || currentTerminal == Terminals.BOOLOPR) {
            System.out.println("term1NTS ::= ε");
            return new Epsilon.Term1NTS();
        } else {
            throw new GrammarError(Terminals.TERM1NTS, currentTerminal);
        }
    }

    // term2 ::= term3 term2NTS
    private ITerm2 term2() throws GrammarError {
        if (currentTerminal == Terminals.LPAREN || currentTerminal == Terminals.ADDOPR
                || currentTerminal == Terminals.NOTOPR || currentTerminal == Terminals.IDENT
                || currentTerminal == Terminals.LITERAL) {
            System.out.println("term2 ::= <term3> <term2NTS>");
            ITerm3 N_term3 = term3();
            ITerm2NTS N_term2NTS = term2NTS();
            return new Term2(N_term3, N_term2NTS);
        } else {
            throw new GrammarError(Terminals.TERM2, currentTerminal);
        }
    }

    // term2NTS ::= ADDOPR term3 term2NTS
    // term2NTS ::= ε
    private ITerm2NTS term2NTS() throws GrammarError {
        if (currentTerminal == Terminals.ADDOPR) {
            System.out.println("term2NTS ::= ADDOPR <term3> <term2NTS>");
            Token T_addOpr = consume(Terminals.ADDOPR);
            ITerm3 N_term3 = term3();
            ITerm2NTS N_term2NTS = term2NTS();
            return new Term2NTS(T_addOpr, N_term3, N_term2NTS);
        } else if (currentTerminal == Terminals.COMMA || currentTerminal == Terminals.RPAREN
                || currentTerminal == Terminals.COLON || currentTerminal == Terminals.DO
                || currentTerminal == Terminals.THEN || currentTerminal == Terminals.ENDPROC
                || currentTerminal == Terminals.ENDWHILE || currentTerminal == Terminals.ENDIF
                || currentTerminal == Terminals.ELSE || currentTerminal == Terminals.ENDFUN
                || currentTerminal == Terminals.ENDPROGRAM || currentTerminal == Terminals.SEMICOLON
                || currentTerminal == Terminals.BECOMES || currentTerminal == Terminals.BOOLOPR
                || currentTerminal == Terminals.RELOPR) {
            System.out.println("term2NTS ::= ε");
            return new Epsilon.Term2NTS();
        } else {
            throw new GrammarError(Terminals.TERM2NTS, currentTerminal);
        }
    }

    // term3 ::= factor term3NTS
    private ITerm3 term3() throws GrammarError {
        if (currentTerminal == Terminals.LPAREN || currentTerminal == Terminals.ADDOPR
                || currentTerminal == Terminals.NOTOPR || currentTerminal == Terminals.IDENT
                || currentTerminal == Terminals.LITERAL) {
            System.out.println("term3 ::= <factor> <term3NTS>");
            IFactor N_factor = factor();
            ITerm3NTS N_term3NTS = term3NTS();
            return new Term3(N_factor, N_term3NTS);
        } else {
            throw new GrammarError(Terminals.TERM3, currentTerminal);
        }
    }

    // term3NTS ::= MULTOPR factor term3NTS
    // term3NTS ::= ε
    private ITerm3NTS term3NTS() throws GrammarError {
        if (currentTerminal == Terminals.MULTOPR) {
            System.out.println("term3NTS ::= MULTOPR <factor> <term3NTS>");
            Token T_multOpr = consume(Terminals.MULTOPR);
            IFactor N_factor = factor();
            ITerm3NTS N_term3NTS = term3NTS();
            return new Term3NTS(T_multOpr, N_factor, N_term3NTS);
        } else if (currentTerminal == Terminals.COMMA || currentTerminal == Terminals.RPAREN
                || currentTerminal == Terminals.COLON || currentTerminal == Terminals.DO
                || currentTerminal == Terminals.THEN || currentTerminal == Terminals.ENDPROC
                || currentTerminal == Terminals.ENDWHILE || currentTerminal == Terminals.ENDIF
                || currentTerminal == Terminals.ELSE || currentTerminal == Terminals.ENDFUN
                || currentTerminal == Terminals.ENDPROGRAM || currentTerminal == Terminals.SEMICOLON
                || currentTerminal == Terminals.BECOMES || currentTerminal == Terminals.BOOLOPR
                || currentTerminal == Terminals.RELOPR || currentTerminal == Terminals.ADDOPR) {
            System.out.println("term3NTS ::= ε");
            return new Epsilon.Term3NTS();
        } else {
            throw new GrammarError(Terminals.TERM3NTS, currentTerminal);
        }
    }

    // factor ::= LITERAL
    // factor ::= IDENT factorNTS
    // factor ::= monadicOpr factor
    // factor ::= LPAREN expr RPAREN
    private IFactor factor() throws GrammarError {
        if (currentTerminal == Terminals.LITERAL) {
            System.out.println("factor ::= LITERAL");
            Token T_literal = consume(Terminals.LITERAL);
            return new FactorLiteral(T_literal);
        } else if (currentTerminal == Terminals.IDENT) {
            System.out.println("factor ::= IDENT <factorNTS>");
            Token T_ident = consume(Terminals.IDENT);
            IFactorNTS N_factorNTS = factorNTS();
            return new FactorIdent(T_ident, N_factorNTS);
        } else if (currentTerminal == Terminals.ADDOPR || currentTerminal == Terminals.NOTOPR) {
            System.out.println("factor ::= <monadicOpr> <factor>");
            IMonadicOpr N_monadicOpr = monadicOpr();
            IFactor N_factor = factor();
            return new FactorMonadicOpr(N_monadicOpr, N_factor);
        } else if (currentTerminal == Terminals.LPAREN) {
            System.out.println("factor ::= LPAREN <expr> RPAREN");
            Token T_lParen = consume(Terminals.LPAREN);
            IExpr N_expr = expr();
            Token T_rParen = consume(Terminals.RPAREN);
            return new FactorLParen(T_lParen, N_expr, T_rParen);
        } else {
            throw new GrammarError(Terminals.FACTOR, currentTerminal);
        }
    }

    // factorNTS ::= INIT
    // factorNTS ::= exprList
    // factorNTS ::= ε
    private IFactorNTS factorNTS() throws GrammarError {
        if (currentTerminal == Terminals.INIT) {
            System.out.println("factorNTS ::= INIT");
            Token T_init = consume(Terminals.INIT);
            return new FactorNTSInit(T_init);
        } else if (currentTerminal == Terminals.LPAREN) {
            System.out.println("factorNTS ::= <exprList>");
            IExprList N_exprList = exprList();
            return new FactorNTSExprList(N_exprList);
        } else if (currentTerminal == Terminals.COMMA || currentTerminal == Terminals.RPAREN
                || currentTerminal == Terminals.COLON || currentTerminal == Terminals.DO
                || currentTerminal == Terminals.THEN || currentTerminal == Terminals.ENDPROC
                || currentTerminal == Terminals.ENDWHILE || currentTerminal == Terminals.ENDIF
                || currentTerminal == Terminals.ELSE || currentTerminal == Terminals.ENDFUN
                || currentTerminal == Terminals.ENDPROGRAM || currentTerminal == Terminals.SEMICOLON
                || currentTerminal == Terminals.BECOMES || currentTerminal == Terminals.BOOLOPR
                || currentTerminal == Terminals.RELOPR || currentTerminal == Terminals.ADDOPR
                || currentTerminal == Terminals.MULTOPR) {
            System.out.println("factorNTS ::= ε");
            return new Epsilon.FactorNTS();
        } else {
            throw new GrammarError(Terminals.FACTORNTS, currentTerminal);
        }
    }

    // exprList ::= LPAREN exprListLparenNTS RPAREN
    private IExprList exprList() throws GrammarError {
        if (currentTerminal == Terminals.LPAREN) {
            System.out.println("exprList ::= LPAREN <exprListLparenNTS> RPAREN");
            Token T_lParen = consume(Terminals.LPAREN);
            IExprListLparenNTS N_exprListLparenNTS = exprListLparenNTS();
            Token T_rParen = consume(Terminals.RPAREN);
            return new ExprList(T_lParen, N_exprListLparenNTS, T_rParen);
        } else {
            throw new GrammarError(Terminals.EXPRLIST, currentTerminal);
        }
    }

    // exprListLparenNTS ::= expr exprListNTS
    // exprListLparenNTS ::= ε
    private IExprListLparenNTS exprListLparenNTS() throws GrammarError {
        if (currentTerminal == Terminals.LPAREN || currentTerminal == Terminals.ADDOPR
                || currentTerminal == Terminals.NOTOPR || currentTerminal == Terminals.IDENT
                || currentTerminal == Terminals.LITERAL) {
            System.out.println("exprListLparenNTS ::= <expr> <exprListNTS>");
            IExpr N_expr = expr();
            IExprListNTS N_exprListNTS = exprListNTS();
            return new ExprListLParenNTS(N_expr, N_exprListNTS);
        } else if (currentTerminal == Terminals.RPAREN) {
            System.out.println("exprListLparenNTS ::= ε");
            return new Epsilon.ExprListLParenNTS();
        } else {
            throw new GrammarError(Terminals.EXPRLISTLPARENNTS, currentTerminal);
        }
    }

    // exprListNTS ::= COMMA expr exprListNTS
    // exprListNTS ::= ε
    private IExprListNTS exprListNTS() throws GrammarError {
        if (currentTerminal == Terminals.COMMA) {
            System.out.println("exprListNTS ::= COMMA <expr> <exprListNTS>");
            Token T_comma = consume(Terminals.COMMA);
            IExpr N_expr = expr();
            IExprListNTS N_exprListNTS = exprListNTS();
            return new ExprListNTS(T_comma, N_expr, N_exprListNTS);
        } else if (currentTerminal == Terminals.RPAREN) {
            System.out.println("exprListNTS ::= ε");
            return new Epsilon.ExprListNTS();
        } else {
            throw new GrammarError(Terminals.EXPRLISTNTS, currentTerminal);
        }
    }

    // monadicOpr ::= NOT
    // monadicOpr ::= ADDOPR
    private IMonadicOpr monadicOpr() throws GrammarError {
        if (currentTerminal == Terminals.NOTOPR) {
            System.out.println("monadicOpr ::= NOT");
            Token T_not = consume(Terminals.NOTOPR);
            return new MonadicOprNot(T_not);
        } else if (currentTerminal == Terminals.ADDOPR) {
            System.out.println("monadicOpr ::= ADDOPR");
            Token T_addOpr = consume(Terminals.ADDOPR);
            return new MonadicOprAddOpr(T_addOpr);
        } else {
            throw new GrammarError(Terminals.MONADICOPR, currentTerminal);
        }
    }

    // cpsStoDecl ::= stoDecl cpsStoDeclNTS
    private ICpsStoDecl cpsStoDecl() throws GrammarError {
        if (currentTerminal == Terminals.CHANGEMOD || currentTerminal == Terminals.IDENT) {
            System.out.println("cpsStoDecl ::= <stoDecl> <cpsStoDeclNTS>");
            IStoDecl N_stoDecl = stoDecl();
            ICpsStoDeclNTS N_cpsStoDeclNTS = cpsStoDeclNTS();
            return new CpsStoDecl(N_stoDecl, N_cpsStoDeclNTS);
        } else {
            throw new GrammarError(Terminals.CPSSTODECL, currentTerminal);
        }
    }

    // cpsStoDeclNTS ::= SEMICOLON stoDecl cpsStoDeclNTS
    // cpsStoDeclNTS ::= ε
    private ICpsStoDeclNTS cpsStoDeclNTS() throws GrammarError {
        if (currentTerminal == Terminals.SEMICOLON) {
            System.out.println("cpsStoDeclNTS ::= SEMICOLON <stoDecl> <cpsStoDeclNTS>");
            Token T_semicolon = consume(Terminals.SEMICOLON);
            IStoDecl N_stoDecl = stoDecl();
            ICpsStoDeclNTS N_cpsStoDeclNTS = cpsStoDeclNTS();
            return new CpsStoDeclNTS(T_semicolon, N_stoDecl, N_cpsStoDeclNTS);
        } else if (currentTerminal == Terminals.DO) {
            System.out.println("cpsStoDeclNTS ::= ε");
            return new Epsilon.CpsStoDeclNTS();
        } else {
            throw new GrammarError(Terminals.CPSSTODECLNTS, currentTerminal);
        }
    }

    // procDecl ::= PROC IDENT paramList procDeclNTS DO cpsCmd ENDPROC
    private IProcDecl procDecl() throws GrammarError {
        if (currentTerminal == Terminals.PROC) {
            System.out.println("procDecl ::= PROC IDENT <paramList> <procDeclNTS> DO <cpsCmd> ENDPROC");
            Token T_proc = consume(Terminals.PROC);
            Token T_ident = consume(Terminals.IDENT);
            IParamList N_paramList = paramList();
            IProcDeclNTS N_procDeclNTS = procDeclNTS();
            Token T_do = consume(Terminals.DO);
            ICpsCmd N_cpsCmd = cpsCmd();
            Token T_endProc = consume(Terminals.ENDPROC);
            return new ProcDecl(T_proc, T_ident, N_paramList, N_procDeclNTS, T_do, N_cpsCmd, T_endProc);
        } else {
            throw new GrammarError(Terminals.PROCDECL, currentTerminal);
        }
    }

    // procDeclNTS ::= LOCAL cpsStoDecl
    // procDeclNTS ::= ε
    private IProcDeclNTS procDeclNTS() throws GrammarError {
        if (currentTerminal == Terminals.LOCAL) {
            System.out.println("procDeclNTS ::= LOCAL <cpsStoDecl>");
            Token T_local = consume(Terminals.LOCAL);
            ICpsStoDecl N_cpsStoDecl = cpsStoDecl();
            return new ProcDeclNTS(T_local, N_cpsStoDecl);
        } else if (currentTerminal == Terminals.DO) {
            System.out.println("procDeclNTS ::= ε");
            return new Epsilon.ProcDeclNTS();
        } else {
            throw new GrammarError(Terminals.PROCDECLNTS, currentTerminal);
        }
    }
}
