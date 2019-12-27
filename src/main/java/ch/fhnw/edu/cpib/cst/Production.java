package ch.fhnw.edu.cpib.cst;
import ch.fhnw.edu.cpib.cst.interfaces.IProduction;
import ch.fhnw.edu.cpib.scanner.interfaces.IToken;

import java.lang.reflect.Field;

public abstract class Production implements IProduction {

    @Override
    public String toString(String indent) {
        String subindent = indent + " ";
        String s = "";
        try {
            Field[] fields = this.getClass().getDeclaredFields();
            for (Field field : fields) {
                if(field.getType() == IToken.class) {
                    s += indent + field.get(this) + "\n";
                } else if (field.get(this) instanceof IProduction) {
                    s += ((IProduction)field.get(this)).toString(subindent);
                }
                // System.out.println(field.getType());
            }
        } catch (IllegalArgumentException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return s;
    }
}
