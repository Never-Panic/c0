package miniplc0java.instruction;

public class CallMainIns extends Instruction {

    private boolean isVoid;
    private int mainOffset;

    public CallMainIns () {}

    public void setVoid(boolean aVoid) {
        isVoid = aVoid;
    }

    public void setMainOffset(int mainOffset) {
        this.mainOffset = mainOffset;
    }

    @Override
    public String toString() {
        if (isVoid) {
            return (new Instruction(Operation.Stackalloc,0)).toString() + '\n'
                    + (new Instruction(Operation.Call,mainOffset)).toString();
        } else {
            return (new Instruction(Operation.Stackalloc,1)).toString() + '\n'
                    + (new Instruction(Operation.Call,mainOffset)).toString() + '\n'
                    + (new Instruction(Operation.PopN, 1));
        }
    }
}
