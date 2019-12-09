package ch.fhnw.edu.cpib.parser;

import ch.fhnw.edu.cpib.absSynTree.TypeIdent;

import java.util.HashMap;
import java.util.Map;

public class DeepCopy {
    public DeepCopy() {

    }

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
