// Virtual Machine Java 2015, V01
// Edgar F.A. Lederer, FHNW and Uni Basel, 2015

package ch.fhnw.edu.cpib.vm;

public class Data {
    static interface IBaseData {
        IBaseData copy();
    }

    static class IntData implements IBaseData {
        private int i; // FIXME: According to https://de.wikipedia.org/wiki/Integer_(Datentyp) should be twice as big -> long

        IntData(int i) {
            this.i = i;
        }

        int getData() {
            return i;
        }

        public IntData copy() {
            return intCopy(this);
        }

        @Override public String toString() {
            return "IntData: " + i;
        }
    }

    // nat extension
    static class NatData implements IBaseData {
        public static final long MAX_VALUE = 9223372036854775807L; // FIXME: According to https://de.wikipedia.org/wiki/Integer_(Datentyp) should be twice as big -> biginteger
        public static final long MIN_VALUE = 0L;

        private long i;

        NatData(long i) {
            // Check boundaries
            if (i > MAX_VALUE)
                throw new RuntimeException("Nat overflow.");
            if (i < MIN_VALUE)
                throw new RuntimeException("Nat underflow.");

            this.i = i;
        }

        long getData() {
            return i;
        }

        public NatData copy() {
            return natCopy(this);
        }

        @Override public String toString() {
            return "NatData: " + i;
        }
    }

    static IntData intNew(int i) {
        return new IntData(i);
    }

    static int intGet(IBaseData a) {
        return ((IntData) a).getData();
    }

    static IntData intCopy(IBaseData a) {
        return intNew(intGet(a));
    }

    // nat extension
    static NatData natNew(long i) {
        return new NatData(i);
    }

    // nat extension
    static long natGet(IBaseData a) {
        return ((NatData) a).getData();
    }

    // nat extension
    static NatData natCopy(IBaseData a) {
        return natNew(natGet(a));
    }

    // booleans -> integers
    static IntData boolNew(boolean b) {
        return intNew(b ? 1 : 0);
    }

    // booleans -> integers
    static boolean boolGet(IBaseData a) {
        return ((IntData) a).getData() != 0;
    }

    static class FloatData implements IBaseData {
        private float f;

        FloatData(float f) {
            this.f = f;
        }

        float getData() {
            return f;
        }

        public FloatData copy() {
            return floatCopy(this);
        }
    }

    static FloatData floatNew(float f) {
        return new FloatData(f);
    }

    static float floatGet(IBaseData a) {
        return ((FloatData) a).getData();
    }

    static FloatData floatCopy(IBaseData a) {
        return floatNew(floatGet(a));
    }

    static IntData intInv(IBaseData a) {
        return intNew(-intGet(a));
    }

    // nat extension
    static NatData natInv(IBaseData a) {
        return natNew(-natGet(a));
    }

    static FloatData floatInv(IBaseData a) {
        return floatNew(-floatGet(a));
    }

    // boolean -> integers
    static IntData boolInv(IBaseData a) {
        return boolNew(intGet(a) == 1 ? false : true);
    }

    static IntData intAdd(IBaseData a, IBaseData b) {
        return intNew(intGet(a) + intGet(b));
    }

    static IntData intSub(IBaseData a, IBaseData b) {
        return intNew(intGet(a) - intGet(b));
    }

    static IntData intMult(IBaseData a, IBaseData b) {
        return intNew(intGet(a) * intGet(b));
    }

    static IntData intDivTrunc(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return intNew(intGet(a) / intGet(b));
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer division by zero.");
        }
    }

    static IntData intModTrunc(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return intNew(intGet(a) % intGet(b));
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer remainder by zero.");
        }
    }

    // New
    static IntData intDivFloor(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return intNew(Math.floorDiv(intGet(a), intGet(b)));
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer division by zero.");
        }
    }

    // New
    static IntData intModFloor(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return intNew(Math.floorMod(intGet(a), intGet(b)));
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer remainder by zero.");
        }
    }

    static IntData intDivEucl(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            int r = intGet(a) / intGet(b);
            if (intGet(a) < 0 && r * intGet(b) != intGet(a)) {
                r -= java.lang.Math.signum(intGet(b));
            }
            return intNew(r);
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer division by zero.");
        }
    }

    // New
    static IntData intModEucl(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            int r = intGet(a) - intGet(intDivEucl(a, b)) * intGet(b);
            return intNew(r);
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer remainder by zero.");
        }
    }

    static IntData intEQ(IBaseData a, IBaseData b) {
        return boolNew(intGet(a) == intGet(b));
    }

    static IntData intNE(IBaseData a, IBaseData b) {
        return boolNew(intGet(a) != intGet(b));
    }

    static IntData intGT(IBaseData a, IBaseData b) {
        return boolNew(intGet(a) > intGet(b));
    }

    static IntData intLT(IBaseData a, IBaseData b) {
        return boolNew(intGet(a) < intGet(b));
    }

    static IntData intGE(IBaseData a, IBaseData b) {
        return boolNew(intGet(a) >= intGet(b));
    }

    static IntData intLE(IBaseData a, IBaseData b) {
        return boolNew(intGet(a) <= intGet(b));
    }

    // nat extension
    static NatData natAdd(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return natNew(natGet(a) + natGet(b));
        } catch (RuntimeException e) {
            throw new VirtualMachine.ExecutionError(e.getMessage());
        }
    }

    // nat extension
    static NatData natSub(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return natNew(natGet(a) - natGet(b));
        } catch (RuntimeException e) {
            throw new VirtualMachine.ExecutionError(e.getMessage());
        }
    }

    // nat extension
    static NatData natMult(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return natNew(natGet(a) * natGet(b));
        } catch (RuntimeException e) {
            throw new VirtualMachine.ExecutionError(e.getMessage());
        }
    }

    // nat extension
    static NatData natDivTrunc(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return natNew(natGet(a) / natGet(b));
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer division by zero.");
        } catch (RuntimeException e) {
            throw new VirtualMachine.ExecutionError(e.getMessage());
        }
    }

    // nat extension
    static NatData natModTrunc(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return natNew(natGet(a) % natGet(b));
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer remainder by zero.");
        } catch (RuntimeException e) {
            throw new VirtualMachine.ExecutionError(e.getMessage());
        }
    }

    // nat extension
    static NatData natDivFloor(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return natNew(Math.floorDiv(natGet(a), natGet(b)));
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer division by zero.");
        } catch (RuntimeException e) {
            throw new VirtualMachine.ExecutionError(e.getMessage());
        }
    }

    // nat extension
    static NatData natModFloor(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return natNew(Math.floorMod(natGet(a), natGet(b)));
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer remainder by zero.");
        } catch (RuntimeException e) {
            throw new VirtualMachine.ExecutionError(e.getMessage());
        }
    }

    // nat extension
    static NatData natDivEucl(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            long r = natGet(a) / natGet(b);
            if (natGet(a) < 0 && r * natGet(b) != natGet(a)) {
                // FIXME: Nat can never be negative?
                r -= java.lang.Math.signum(natGet(b));
            }
            return natNew(r);
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer division by zero.");
        } catch (RuntimeException e) {
            throw new VirtualMachine.ExecutionError(e.getMessage());
        }
    }

    // nat extension
    static NatData natModEucl(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            long r = natGet(a) - natGet(natDivEucl(a, b)) * natGet(b);
            return natNew(r);
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer remainder by zero.");
        } catch (RuntimeException e) {
            throw new VirtualMachine.ExecutionError(e.getMessage());
        }
    }

    // nat extension
    static IntData natEQ(IBaseData a, IBaseData b) {
        return boolNew(natGet(a) == natGet(b));
    }

    // nat extension
    static IntData natNE(IBaseData a, IBaseData b) {
        return boolNew(natGet(a) != natGet(b));
    }

    // nat extension
    static IntData natGT(IBaseData a, IBaseData b) {
        return boolNew(natGet(a) > natGet(b));
    }

    // nat extension
    static IntData natLT(IBaseData a, IBaseData b) {
        return boolNew(natGet(a) < natGet(b));
    }

    // nat extension
    static IntData natGE(IBaseData a, IBaseData b) {
        return boolNew(natGet(a) >= natGet(b));
    }

    // nat extension
    static IntData natLE(IBaseData a, IBaseData b) {
        return boolNew(natGet(a) <= natGet(b));
    }

    // boolean -> integers
    static IntData boolAnd(IBaseData a, IBaseData b) {
        return boolNew(boolGet(a) & boolGet(b));
    }

    // boolean -> integers
    static IntData boolOr(IBaseData a, IBaseData b) {
        return boolNew(boolGet(a) | boolGet(b));
    }

    // boolean -> integers
    static IntData boolCAnd(IBaseData a, IBaseData b) {
        return boolNew(boolGet(a) && boolGet(b));
    }

    // boolean -> integers
    static IntData boolCOr(IBaseData a, IBaseData b) {
        return boolNew(boolGet(a) || boolGet(b));
    }
}
