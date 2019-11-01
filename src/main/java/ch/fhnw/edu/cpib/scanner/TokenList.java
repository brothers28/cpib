package ch.fhnw.edu.cpib.scanner;

import java.util.ArrayList;

public class TokenList {
    private ArrayList<Token> tokenList = new ArrayList<>();
    private int position = 0;

    public void add(Token token){
        tokenList.add(token);
    }

    public void reset(){
        position = 0;
    }

    public Token nextToken() {
        return tokenList.get(position++);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (Token t : tokenList){
            sb.append(t.toString());
            sb.append(", ");
        }
        sb.deleteCharAt(sb.lastIndexOf(","));
        sb.append("]");
        return sb.toString();
    }
}
