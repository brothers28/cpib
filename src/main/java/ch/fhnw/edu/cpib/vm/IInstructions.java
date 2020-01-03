// Virtual Machine Java 2015, V01
// Edgar F.A. Lederer, FHNW and Uni Basel, 2015

package ch.fhnw.edu.cpib.vm;

import ch.fhnw.edu.cpib.scanner.enumerations.Types;
import ch.fhnw.edu.cpib.vm.IVirtualMachine.ExecutionError;

// idea: the compiler should not need a reference to the VM object

public interface IInstructions {

    // non-executable form of instructions
    interface IInstr {
        IExecInstr toExecInstr(VirtualMachine vm);
    }

    // executable form of instructions
    interface IExecInstr extends IInstr {
        void execute() throws ExecutionError;
    }

    // stop instruction
    class Stop implements IInstr {
        public String toString() {
            return "Stop";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new StopExec();
        }
    }

    // stack instruction
    class Dup implements IInstr {
        public String toString() {
            return "Dup";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new DupExec();
        }
    }

    // routine operations

    class AllocBlock implements IInstr {
        protected int size;

        public AllocBlock(int size) {
            this.size = size;
        }

        public String toString() {
            return "AllocBlock(" + size + ")";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new AllocBlockExec(size);
        }
    }

    class AllocStack implements IInstr {
        protected int maxSize;

        public AllocStack(int maxSize) {
            this.maxSize = maxSize;
        }

        public String toString() {
            return "AllocStack(" + maxSize + ")";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new AllocStackExec(maxSize);
        }
    }

    class Call implements IInstr {
        protected int routAddress;

        public Call(int routAddress) {
            this.routAddress = routAddress;
        }

        public String toString() {
            return "Call(" + routAddress + ")";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new CallExec(routAddress);
        }
    }

    class Return implements IInstr {
        protected int size;

        public Return(int size) {
            this.size = size;
        }

        public String toString() {
            return "Return(" + size + ")";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new ReturnExec(size);
        }
    }

    // load immediate value (value -> stack)
    class LoadImInt implements IInstr {
        protected long value;

        public LoadImInt(long value) {
            this.value = value;
        }

        public String toString() {
            return "LoadImInt(" + value + ")";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new LoadImIntExec(value);
        }
    }

    class LoadImNat implements IInstr {
        protected long value;

        public LoadImNat(long value) {
            this.value = value;
        }

        public String toString() {
            return "LoadImNat(" + value + ")";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new LoadImNatExec(value);
        }
    }

    // New
    // load immediate value (value -> stack)
    class LoadImBool implements IInstr {
        protected boolean value;

        public LoadImBool(boolean value) {
            this.value = value;
        }

        public String toString() {
            return "LoadImBool(" + value + ")";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new LoadImBoolExec(value);
        }
    }

    // load address relative to frame pointer (address -> stack)
    class LoadAddrRel implements IInstr {
        protected int relAddress;

        public LoadAddrRel(int relAddress) {
            this.relAddress = relAddress;
        }

        public String toString() {
            return "LoadAddrRel(" + relAddress + ")";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new LoadAddrRelExec(relAddress);
        }
    }

    // New
    // load address absolute (address -> stack)
    class LoadAddrAbs implements IInstr {
        protected int absAddress;

        public LoadAddrAbs(int absAddress) {
            this.absAddress = absAddress;
        }

        public String toString() {
            return "LoadAddrAbs(" + absAddress + ")";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new LoadAddrAbsExec(absAddress);
        }
    }

    // load instruction with address on stack
    // load (inside stack -> top of stack) operation
    class Deref implements IInstr {
        Types castType;

        public Deref() {
        }

        public Deref(Types type) {
            castType = type;
        }

        public String toString() {
            return "Deref";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new DerefExec(castType);
        }
    }

    // store instruction with address on stack
    // store (top of stack -> inside stack) operation
    class Store implements IInstr {
        public String toString() {
            return "Store";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new StoreExec();
        }
    }

    // monadic instructions

    class NegInt implements IInstr {
        public String toString() {
            return "NegInt";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new NegIntExec();
        }
    }

    // nat extension
    class NegNat implements IInstr {
        public String toString() {
            return "NegNat";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new NegNatExec();
        }
    }

    // New
    class NegBool implements IInstr {
        public String toString() {
            return "NegBool";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new NegBoolExec();
        }
    }

    // dyadic instructions

    class AddInt implements IInstr {
        public String toString() {
            return "AddInt";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new AddIntExec();
        }
    }

    class SubInt implements IInstr {
        public String toString() {
            return "SubInt";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new SubIntExec();
        }
    }

    class MultInt implements IInstr {
        public String toString() {
            return "MultInt";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new MultIntExec();
        }
    }

    class DivTruncInt implements IInstr {
        public String toString() {
            return "DivTruncInt";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new DivTruncIntExec();
        }
    }

    class ModTruncInt implements IInstr {
        public String toString() {
            return "ModTruncInt";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new ModTruncIntExec();
        }
    }

    // New
    class DivEuclInt implements IInstr {
        public String toString() {
            return "DivEuclInt";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new DivEuclIntExec();
        }
    }

    // New
    class ModEuclInt implements IInstr {
        public String toString() {
            return "ModEuclInt";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new ModEuclIntExec();
        }
    }

    // New
    class DivFloorInt implements IInstr {
        public String toString() {
            return "DivFloorInt";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new DivFloorIntExec();
        }
    }

    // New
    class ModFloorInt implements IInstr {
        public String toString() {
            return "ModFloorInt";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new ModFloorIntExec();
        }
    }

    class EqInt implements IInstr {
        public String toString() {
            return "EqInt";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new EqIntExec();
        }
    }

    class NeInt implements IInstr {
        public String toString() {
            return "NeInt";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new NeIntExec();
        }
    }

    class GtInt implements IInstr {
        public String toString() {
            return "GtInt";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new GtIntExec();
        }
    }

    class LtInt implements IInstr {
        public String toString() {
            return "LtInt";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new LtIntExec();
        }
    }

    class GeInt implements IInstr {
        public String toString() {
            return "GeInt";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new GeIntExec();
        }
    }

    class LeInt implements IInstr {
        public String toString() {
            return "LeInt";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new LeIntExec();
        }
    }

    // nat extension
    class AddNat implements IInstr {
        public String toString() {
            return "AddNat";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new AddNatExec();
        }
    }

    // nat extension
    class SubNat implements IInstr {
        public String toString() {
            return "SubNat";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new SubNatExec();
        }
    }

    // nat extension
    class MultNat implements IInstr {
        public String toString() {
            return "MultNat";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new MultNatExec();
        }
    }

    // nat extension
    class DivTruncNat implements IInstr {
        public String toString() {
            return "DivTruncNat";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new DivTruncNatExec();
        }
    }

    // nat extension
    class ModTruncNat implements IInstr {
        public String toString() {
            return "ModTruncNat";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new ModTruncNatExec();
        }
    }

    // nat extension
    class DivEuclNat implements IInstr {
        public String toString() {
            return "DivEuclNat";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new DivEuclNatExec();
        }
    }

    // nat extension
    class ModEuclNat implements IInstr {
        public String toString() {
            return "ModEuclNat";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new ModEuclNatExec();
        }
    }

    // nat extension
    class DivFloorNat implements IInstr {
        public String toString() {
            return "DivFloorNat";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new DivFloorNatExec();
        }
    }

    // nat extension
    class ModFloorNat implements IInstr {
        public String toString() {
            return "ModFloorNat";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new ModFloorNatExec();
        }
    }

    // nat extension
    class EqNat implements IInstr {
        public String toString() {
            return "EqNat";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new EqNatExec();
        }
    }

    // nat extension
    class NeNat implements IInstr {
        public String toString() {
            return "NeNat";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new NeNatExec();
        }
    }

    // nat extension
    class GtNat implements IInstr {
        public String toString() {
            return "GtNat";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new GtNatExec();
        }
    }

    // nat extension
    class LtNat implements IInstr {
        public String toString() {
            return "LtNat";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new LtNatExec();
        }
    }

    // nat extension
    class GeNat implements IInstr {
        public String toString() {
            return "GeNat";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new GeNatExec();
        }
    }

    // nat extension
    class LeNat implements IInstr {
        public String toString() {
            return "LeNat";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new LeNatExec();
        }
    }

    // nat extension
    class AndBool implements IInstr {
        public String toString() {
            return "AndBool";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new AndBoolExec();
        }
    }

    // nat extension
    class OrBool implements IInstr {
        public String toString() {
            return "OrBool";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new OrBoolExec();
        }
    }

    // nat extension
    class CAndBool implements IInstr {
        public String toString() {
            return "CAndBool";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new CAndBoolExec();
        }
    }

    // nat extension
    class COrBool implements IInstr {
        public String toString() {
            return "COrBool";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new COrBoolExec();
        }
    }

    // jump instructions

    class UncondJump implements IInstr {
        protected int jumpAddr;

        public UncondJump(int jumpAddr) {
            this.jumpAddr = jumpAddr;
        }

        public String toString() {
            return "UncondJump(" + jumpAddr + ")";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new UncondJumpExec(jumpAddr);
        }
    }

    class CondJump implements IInstr {
        protected int jumpAddr;

        public CondJump(int jumpAddr) {
            this.jumpAddr = jumpAddr;
        }

        public String toString() {
            return "CondJump(" + jumpAddr + ")";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new CondJumpExec(jumpAddr);
        }
    }

    // New
    class RelJump implements IInstr {
        protected int jumpAddr;

        public RelJump(int jumpAddr) {
            this.jumpAddr = jumpAddr;
        }

        public String toString() {
            return "RelJump(" + jumpAddr + ") + index";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new RelJumpExec(jumpAddr);
        }
    }

    // input (input -> stack) and output (stack -> output) instructions

    class InputBool implements IInstr {
        protected String indicator;

        public InputBool(String indicator) {
            this.indicator = indicator;
        }

        public String toString() {
            return "InputBool(\"" + indicator + "\")";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new InputBoolExec(indicator);
        }
    }

    class InputInt implements IInstr {
        protected String indicator;

        public InputInt(String indicator) {
            this.indicator = indicator;
        }

        public String toString() {
            return "InputInt(\"" + indicator + "\")";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new InputIntExec(indicator);
        }
    }

    class InputNat implements IInstr {
        protected String indicator;

        public InputNat(String indicator) {
            this.indicator = indicator;
        }

        public String toString() {
            return "InputNat(\"" + indicator + "\")";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new InputNatExec(indicator);
        }
    }

    class OutputBool implements IInstr {
        protected String indicator;

        public OutputBool(String indicator) {
            this.indicator = indicator;
        }

        public String toString() {
            return "OutputBool(\"" + indicator + "\")";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new OutputBoolExec(indicator);
        }
    }

    class OutputInt implements IInstr {
        protected String indicator;

        public OutputInt(String indicator) {
            this.indicator = indicator;
        }

        public String toString() {
            return "OutputInt(\"" + indicator + "\")";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new OutputIntExec(indicator);
        }
    }

    class OutputNat implements IInstr {
        protected String indicator;

        public OutputNat(String indicator) {
            this.indicator = indicator;
        }

        public String toString() {
            return "OutputNat(\"" + indicator + "\")";
        }

        public IExecInstr toExecInstr(VirtualMachine vm) {
            return vm.new OutputNatExec(indicator);
        }
    }
}
