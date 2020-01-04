package ch.fhnw.edu.cpib.scanner;

import ch.fhnw.edu.cpib.scanner.enumerations.Terminals;
import ch.fhnw.edu.cpib.scanner.enumerations.Types;

public class Literal extends Base {
    private final boolean isBoolean;
    private final long value;
    private Types castType;

    public Literal(Terminals terminal) {
        super(terminal);
        this.isBoolean = true;
        this.value = 0; // Default, not used
    }

    public Literal(Terminals terminal, long value) {
        super(terminal);
        this.isBoolean = false;
        this.value = value;
    }

    public Literal(Terminals terminal, long value, boolean isBoolean) {
        super(terminal);
        this.isBoolean = isBoolean;
        this.value = value;
    }

    public long getValue() {
        return value;
    }

    public Types getType() {
        if (castType != null) {
            // type is casted
            return castType;
        }
        // otherwise get real type
        return isBoolean ? Types.BOOL : Types.INT32; // FIXME: WTF?

    }

    public boolean getBoolValue() throws IllegalStateException {
        if (!isBoolean)
            throw new IllegalStateException("Type is not bool");

        return value > 0;
    }

    public long getIntValue() throws IllegalStateException {
        if (isBoolean)
            throw new IllegalStateException("Type is bool");

        return value;
    }

    public long getNatValue() throws IllegalStateException {
        if (isBoolean)
            throw new IllegalStateException("Type is bool");

        return value;
    }

    public void doTypeCasting(Types type) {
        this.castType = type;
    }

    @Override public String toString() {
        return "(" + super.toString() + ", " + (this.isBoolean ? "BoolVal" : "") + getValue() + ")";
    }
}
