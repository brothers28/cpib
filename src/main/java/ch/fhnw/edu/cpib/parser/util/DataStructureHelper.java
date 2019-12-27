package ch.fhnw.edu.cpib.parser.util;

import ch.fhnw.edu.cpib.ast.TypeIdent;

import java.util.HashMap;
import java.util.Map;

public class DataStructureHelper {

    public static HashMap<String, TypeIdent> deepCopy(HashMap<String, TypeIdent> map) {
        HashMap<String, TypeIdent> tmp = new HashMap<>();
        for(Map.Entry<String, TypeIdent> entry : map.entrySet()){
            try {
                tmp.put(entry.getKey(), (TypeIdent) entry.getValue().clone());
            } catch (CloneNotSupportedException e) {
                System.out.println("Clone error");
                e.printStackTrace();
            }
        }
        return tmp;
    }
}
