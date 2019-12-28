// Virtual Machine Java 2015, V01
// Edgar F.A. Lederer, FHNW and Uni Basel, 2015

package ch.fhnw.edu.cpib.vm;

public class Data {
    static interface IBaseData {
        IBaseData copy();
    }

    // nat extension
    static class NumData implements IBaseData {
        private long i;

        NumData() {
        }

        NumData(long i) {
            this.i = i;
        }

        long getData() {
            return i;
        }

        public NumData copy() {
            return numCopy(this);
        }
    }

    static class IntData extends NumData {
        public static final long MAX_VALUE = Integer.MAX_VALUE;
        public static final long MIN_VALUE = Integer.MIN_VALUE;

        private long i;

        IntData(long i) {
            // Check boundaries
            if (i > MAX_VALUE)
                throw new RuntimeException("Int overflow.");
            if (i < MIN_VALUE)
                throw new RuntimeException("Int underflow.");

            this.i = i;
        }

        long getData() {
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
    static class NatData extends NumData {
        public static final long MAX_VALUE = 4294967295L;
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

    // num data
    static NumData numNew(long i) {
        return new NumData(i);
    }

    static long numGet(IBaseData a) {
        return ((NumData) a).getData();
    }

    static NumData numCopy(IBaseData a) {
        return numNew(numGet(a));
    }

    // int data
    static IntData intNew(long i) {
        return new IntData(i);
    }

    static int intGet(IBaseData a) {
        return (int) ((IntData) a).getData();
    }

    static IntData intCopy(IBaseData a) {
        return intNew(numGet(a));
    }

    static IntData intInv(IBaseData a) {
        return intNew(-numGet(a));
    }

    static IntData intAdd(IBaseData a, IBaseData b) {
        return intNew(numGet(a) + numGet(b));
    }

    static IntData intSub(IBaseData a, IBaseData b) {
        return intNew(numGet(a) - numGet(b));
    }

    static IntData intMult(IBaseData a, IBaseData b) {
        return intNew(numGet(a) * numGet(b));
    }

    static IntData intDivTrunc(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return intNew(numGet(a) / numGet(b));
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer division by zero.");
        }
    }

    static IntData intModTrunc(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return intNew(numGet(a) % numGet(b));
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer remainder by zero.");
        }
    }

    static IntData intDivFloor(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return intNew(Math.floorDiv(numGet(a), numGet(b)));
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer division by zero.");
        }
    }

    static IntData intModFloor(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return intNew(Math.floorMod(numGet(a), numGet(b)));
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer remainder by zero.");
        }
    }

    static IntData intDivEucl(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            long r = numGet(a) / numGet(b);
            if (numGet(a) < 0 && r * numGet(b) != numGet(a)) {
                r -= java.lang.Math.signum(numGet(b));
            }
            return intNew(r);
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer division by zero.");
        }
    }

    static IntData intModEucl(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            long r = numGet(a) - numGet(intDivEucl(a, b)) * numGet(b);
            return intNew(r);
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer remainder by zero.");
        }
    }

    static IntData intEQ(IBaseData a, IBaseData b) {
        return boolNew(numGet(a) == numGet(b));
    }

    static IntData intNE(IBaseData a, IBaseData b) {
        return boolNew(numGet(a) != numGet(b));
    }

    static IntData intGT(IBaseData a, IBaseData b) {
        return boolNew(numGet(a) > numGet(b));
    }

    static IntData intLT(IBaseData a, IBaseData b) {
        return boolNew(numGet(a) < numGet(b));
    }

    static IntData intGE(IBaseData a, IBaseData b) {
        return boolNew(numGet(a) >= numGet(b));
    }

    static IntData intLE(IBaseData a, IBaseData b) {
        return boolNew(numGet(a) <= numGet(b));
    }

    // nat data
    // nat extension
    static NatData natNew(long i) {
        return new NatData(i);
    }

    static long natGet(IBaseData a) {
        return ((NatData) a).getData();
    }

    static NatData natCopy(IBaseData a) {
        return natNew(numGet(a));
    }

    static NatData natInv(IBaseData a) {
        return natNew(-numGet(a));
    }

    static NatData natAdd(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return natNew(numGet(a) + numGet(b));
        } catch (RuntimeException e) {
            throw new VirtualMachine.ExecutionError(e.getMessage());
        }
    }

    static NatData natSub(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return natNew(numGet(a) - numGet(b));
        } catch (RuntimeException e) {
            throw new VirtualMachine.ExecutionError(e.getMessage());
        }
    }

    static NatData natMult(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return natNew(numGet(a) * numGet(b));
        } catch (RuntimeException e) {
            throw new VirtualMachine.ExecutionError(e.getMessage());
        }
    }

    static NatData natDivTrunc(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return natNew(numGet(a) / numGet(b));
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer division by zero.");
        } catch (RuntimeException e) {
            throw new VirtualMachine.ExecutionError(e.getMessage());
        }
    }

    static NatData natModTrunc(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return natNew(numGet(a) % numGet(b));
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer remainder by zero.");
        } catch (RuntimeException e) {
            throw new VirtualMachine.ExecutionError(e.getMessage());
        }
    }

    static NatData natDivFloor(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return natNew(Math.floorDiv(numGet(a), numGet(b)));
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer division by zero.");
        } catch (RuntimeException e) {
            throw new VirtualMachine.ExecutionError(e.getMessage());
        }
    }

    static NatData natModFloor(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            return natNew(Math.floorMod(numGet(a), numGet(b)));
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer remainder by zero.");
        } catch (RuntimeException e) {
            throw new VirtualMachine.ExecutionError(e.getMessage());
        }
    }

    static NatData natDivEucl(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            long r = numGet(a) / numGet(b);
            if (numGet(a) < 0 && r * numGet(b) != numGet(a)) {
                // FIXME: Nat can never be negative?
                r -= java.lang.Math.signum(numGet(b));
            }
            return natNew(r);
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer division by zero.");
        } catch (RuntimeException e) {
            throw new VirtualMachine.ExecutionError(e.getMessage());
        }
    }

    static NatData natModEucl(IBaseData a, IBaseData b) throws IVirtualMachine.ExecutionError {
        try {
            long r = numGet(a) - numGet(natDivEucl(a, b)) * numGet(b);
            return natNew(r);
        } catch (ArithmeticException e) {
            throw new VirtualMachine.ExecutionError("Integer remainder by zero.");
        } catch (RuntimeException e) {
            throw new VirtualMachine.ExecutionError(e.getMessage());
        }
    }

    static IntData natEQ(IBaseData a, IBaseData b) {
        return boolNew(numGet(a) == numGet(b));
    }

    static IntData natNE(IBaseData a, IBaseData b) {
        return boolNew(numGet(a) != numGet(b));
    }

    static IntData natGT(IBaseData a, IBaseData b) {
        return boolNew(numGet(a) > numGet(b));
    }

    static IntData natLT(IBaseData a, IBaseData b) {
        return boolNew(numGet(a) < numGet(b));
    }

    static IntData natGE(IBaseData a, IBaseData b) {
        return boolNew(numGet(a) >= numGet(b));
    }

    static IntData natLE(IBaseData a, IBaseData b) {
        return boolNew(numGet(a) <= numGet(b));
    }

    // bool data
    // booleans -> integers
    static IntData boolNew(boolean b) {
        return intNew(b ? 1 : 0);
    }

    static boolean boolGet(IBaseData a) {
        return ((IntData) a).getData() != 0;
    }

    static IntData boolInv(IBaseData a) {
        return boolNew(numGet(a) == 1 ? false : true);
    }

    static IntData boolAnd(IBaseData a, IBaseData b) {
        return boolNew(boolGet(a) & boolGet(b));
    }

    static IntData boolOr(IBaseData a, IBaseData b) {
        return boolNew(boolGet(a) | boolGet(b));
    }

    static IntData boolCAnd(IBaseData a, IBaseData b) {
        return boolNew(boolGet(a) && boolGet(b));
    }

    static IntData boolCOr(IBaseData a, IBaseData b) {
        return boolNew(boolGet(a) || boolGet(b));
    }

    // float data
    static FloatData floatNew(float f) {
        return new FloatData(f);
    }

    static float floatGet(IBaseData a) {
        return ((FloatData) a).getData();
    }

    static FloatData floatCopy(IBaseData a) {
        return floatNew(floatGet(a));
    }

    static FloatData floatInv(IBaseData a) {
        return floatNew(-floatGet(a));
    }
}
