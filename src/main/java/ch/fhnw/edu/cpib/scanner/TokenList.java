package ch.fhnw.edu.cpib.scanner;

import java.util.ArrayList;

public class TokenList {
    private ArrayList<Base> tokenList = new ArrayList<Base>();
    private int position = 0;

    public void add(Base token){
        tokenList.add(token);
    }

    public void reset(){
        position = 0;
    }

    public Base nextToken() {
        return tokenList.get(position++);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Base b : tokenList){
            sb.append(b.toString());
        }
        return sb.toString();
    }
}
