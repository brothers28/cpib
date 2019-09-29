package ch.fhnw.edu.cpib.scanner.enumerations;

import ch.fhnw.edu.cpib.scanner.Token;

public enum Terminals implements Token {
    WHILE,
    IDENT,
    RELOPR,
    LITERAL,
    SENTINEL,
    ADDOPR,
    MULTOPR,
    BOOLOPR,
    TYPE,
    CALL, // call
    CHANGEMOD,
    MECHMOD,
    DEBUGIN,
    DEBUGOUT,
    DO,
    ELSE,
    ENDFUN,
    ENDIF,
    ENDPROC,
    ENDPROGRAM,
    ENDWHILE,
    FUN, // fun
    GOLBAL,
    IF,
    FLOWMODE,
    INIT,
    LOCAL,
    NOTOPR, // not
    PROC,
    PROGRAM,
    MECHMODE,
    RETURNS,
    SKIP,
    THEN,
    LPAREN, // (
    RPAREN, // )
    COMMA,
    SEMICOLON,
    COLON, // :
    BECOMES, // :=



}
