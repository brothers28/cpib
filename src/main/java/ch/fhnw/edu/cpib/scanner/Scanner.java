package ch.fhnw.edu.cpib.scanner;

import ch.fhnw.edu.cpib.errors.LexicalError;
import ch.fhnw.edu.cpib.scanner.enumerations.*;
import ch.fhnw.edu.cpib.scanner.keywords.Changemode;
import ch.fhnw.edu.cpib.scanner.keywords.Flowmode;
import ch.fhnw.edu.cpib.scanner.keywords.Mechmode;
import ch.fhnw.edu.cpib.scanner.keywords.Type;
import ch.fhnw.edu.cpib.scanner.symbols.AddOpr;
import ch.fhnw.edu.cpib.scanner.symbols.BoolOpr;
import ch.fhnw.edu.cpib.scanner.symbols.MultOpr;
import ch.fhnw.edu.cpib.scanner.symbols.RelOpr;

import java.util.Map;

import static java.util.Map.entry;

public class Scanner {
    public static final char COMMENT_SYMBOL = '/';
    public static final char UNDERSCORE_SYMBOL = '_';
    public static final char SINGLE_QUOTE_SYMBOL = '\'';

    public static final Map<String, Token> keywords;
    public static final Map<String, Token> symbols;

    static {
        // Build symbols map
        symbols = Map.ofEntries(
                entry("<", new RelOpr(Operators.LT)),
                entry("<=", new RelOpr(Operators.LE)),
                entry(">", new RelOpr(Operators.GT)),
                entry(">=", new RelOpr(Operators.GE)),
                entry("=", new RelOpr(Operators.EQ)),
                entry("/=", new RelOpr(Operators.NE)),
                entry("+", new AddOpr(Operators.PLUS)),
                entry("-", new AddOpr(Operators.MINUS)),
                entry("*", new MultOpr(Operators.TIMES)),
                entry("&", new BoolOpr(Operators.AND)),
                entry("|", new BoolOpr(Operators.OR)),
                entry("&&", new BoolOpr(Operators.CAND)),
                entry("||", new BoolOpr(Operators.COR)),
                entry("(", Terminals.LPAREN),
                entry(")", Terminals.RPAREN),
                entry("[", Terminals.LBRACKET),
                entry("]", Terminals.RBRACKET),
                entry(",", Terminals.COMMA),
                entry(";", Terminals.SEMICOLON),
                entry(":", Terminals.COLON),
                entry(":=", Terminals.BECOMES));

        // Build keywords map
        keywords = Map
                .ofEntries(entry("while", new Base(Terminals.WHILE)),
                        entry("endwhile", new Base(Terminals.ENDWHILE)),
                        entry("do", new Base(Terminals.DO)),
                        entry("program", new Base(Terminals.PROGRAM)),
                        entry("endprogram", new Base(Terminals.ENDPROGRAM)),
                        entry("proc", new Base(Terminals.PROC)),
                        entry("endproc", new Base(Terminals.ENDPROC)),
                        entry("call", new Base(Terminals.CALL)),
                        entry("debugin", new Base(Terminals.DEBUGIN)),
                        entry("debugout", new Base(Terminals.DEBUGOUT)),
                        entry("else", new Base(Terminals.ELSE)),
                        entry("endfun", new Base(Terminals.ENDFUN)),
                        entry("endif", new Base(Terminals.ENDIF)),
                        entry("fun", new Base(Terminals.FUN)),
                        entry("global", new Base(Terminals.GLOBAL)),
                        entry("if", new Base(Terminals.IF)),
                        entry("init", new Base(Terminals.INIT)),
                        entry("local", new Base(Terminals.LOCAL)),
                        entry("not", new Base(Terminals.NOTOPR)),
                        entry("returns", new Base(Terminals.RETURNS)),
                        entry("skip", new Base(Terminals.SKIP)),
                        entry("then", new Base(Terminals.THEN)),
                        entry("bool", new Type(Types.BOOL)),
                        entry("int64", new Type(Types.INT64)),
                        entry("nat64", new Type(Types.NAT64)),
                        entry("const", new Changemode(Changemodes.CONST.CONST)),
                        entry("var", new Changemode(Changemodes.VAR)),
                        entry("copy", new Mechmode(Mechmodes.COPY)),
                        entry("ref", new Mechmode(Mechmodes.REF)),
                        entry("div", new MultOpr(Operators.DIV)),
                        entry("divE", new MultOpr(Operators.DIV_E)),
                        entry("mod", new MultOpr(Operators.MOD)),
                        entry("modE", new MultOpr(Operators.MOD_E)),
                        entry("false", new Literal(Terminals.BOOLVALFALSE)),
                        entry("true", new Literal(Terminals.BOOLVALTRUE)),
                        entry("in", new Flowmode(Flowmodes.IN)),
                        entry("inout", new Flowmode(Flowmodes.INOUT)),
                        entry("out", new Flowmode(Flowmodes.OUT)));
    }

    /**
     * Converts character sequence to tokens.
     *
     * @param cs Char sequence
     * @return Token list
     */
    public TokenList scan(CharSequence cs) throws LexicalError {
        // Assert that if last char exists, it must be a "New Line"
        assert cs.length() == 0 || cs.charAt(cs.length() - 1) == '\n';

        TokenList result = new TokenList();
        int state = 0;
        StringBuffer lexAcc = null; // for constructing the identifier
        StringBuffer syAcc = null; // for constructing the symbols
        long numAccu = 0L; // for constructing the literal value

        for (int i = 0; i < cs.length(); i++) {
            char c = cs.charAt(i);

            switch (state) {
            case 0:
                // State: Init, scan new token
                if (isSymbol(c) || isCommentSymbol(c)) {
                    state = 1;
                    syAcc = new StringBuffer();
                    syAcc.append(c);
                } else if (Character.isDigit(c)) {
                    state = 2;
                    int digit = Character.digit(c, 10);
                    numAccu = digit;
                } else if (Character.isAlphabetic(c)) {
                    state = 3;
                    lexAcc = new StringBuffer();
                    lexAcc.append(c);
                } else if (c == ' ' || c == '\n' || c == '\r') {
                    // Whitespace or new line -> do nothing
                } else {
                    throw new LexicalError("Unknown character: " + c);
                }
                break;
            case 1:
                // State: Scanning symbol
                if (isFollowingSymbol(c, syAcc)) {
                    state = 0;
                    syAcc.append(c);

                    Token token = symbols.get(syAcc.toString());
                    result.add(token);
                } else if (isCommentSymbol(c)){
                        state = 0;
                        syAcc.append(c);

                        if (syAcc.length() == 2 && syAcc.toString().equals(COMMENT_SYMBOL + "" + COMMENT_SYMBOL)){
                            // Following is a line comment;
                            state = 4;
                            syAcc.delete(0, lexAcc.length());
                        }
                } else {
                    state = 0;
                    i = i - 1; // one back for next lexeme

                    Token token = symbols.get(syAcc.toString());
                    result.add(token);
                }
                break;
            case 2:
                // State: Scanning literal
                if ( c == SINGLE_QUOTE_SYMBOL){
                    // Do nothing
                } else if (Character.isDigit(c)) {
                    state = 2;
                    int digit = Character.digit(c, 10);
                    numAccu = numAccu * 10 + digit;
                    if (numAccu > Integer.MAX_VALUE) {
                        throw new LexicalError("Integer literal too large!");
                    }
                } else {
                    state = 0;
                    i = i - 1; // one back for next lexeme
                    result.add(new Literal(Terminals.LITERAL, (int) numAccu));
                }
                break;
            case 3:
                // State: Scanning identifier
                if ( c == SINGLE_QUOTE_SYMBOL){
                    // Do nothing
                } else if (Character.isAlphabetic(c) || Character.isDigit(c) || c == UNDERSCORE_SYMBOL) {
                    state = 3;
                    lexAcc.append(c);
                } else {
                    state = 0;
                    i = i - 1; // one back for next lexeme
                    if (isKeyword(lexAcc)) {
                        Token token = keywords.get(lexAcc.toString());
                        result.add(token);
                    } else {
                        result.add(new Ident(Terminals.IDENT, lexAcc.toString()));
                    }
                }
                break;
            case 4 :
                // State: Comment
                // Ignore until new line
                if (c == '\n' || c == '\r') {
                    state = 0;
                }
                break;
            default:
                throw new InternalError("Default case in scanner.");
            }
        }

        result.add(new Base(Terminals.SENTINEL));
        return result;
    }

    /**
     * Determines if character is subsequent part of symbol.
     *
     * @param c Character to check
     * @return True, if character is subsequent symbol
     */
    private boolean isFollowingSymbol(char c, StringBuffer previous) {
        switch (previous.toString()) {
        case "/":
            return c == '=';
        case "&":
            return c == '&';
        case "|":
            return c == '|';
        case ">":
            return c == '=';
        case "<":
            return c == '=';
        case ":":
            return c == '=';
        default:
            return false;
        }
    }

    /**
     * Checks if character is regular symbol of iml.
     *
     * @param c Current character
     * @return True if regular iml symbol
     */
    private boolean isSymbol(char c) {
        return symbols.containsKey(String.valueOf(c));
    }

    /**
     * Checks if character is keyword of iml.
     *
     * @param key Word to test
     * @return True if keyword
     */
    private boolean isKeyword(StringBuffer key) {
        return keywords.containsKey(key.toString());
    }

    /**
     * Checks if character is a line comment symbol.
     *
     * @param c Current character
     * @return True if symbol is "/"
     */
    private boolean isCommentSymbol(char c) {
        return COMMENT_SYMBOL == c;
    }


}
