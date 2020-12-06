package miniplc0java.instruction;

import java.util.ArrayList;
import java.util.List;

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


    public List<Byte> getBytes () {
        List<Byte> output = new ArrayList<>();
        if (isVoid) {
            output.addAll((new Instruction(Operation.Stackalloc,0)).getBytes());
            output.addAll((new Instruction(Operation.Call,mainOffset)).getBytes());
        } else {
            output.addAll((new Instruction(Operation.Stackalloc,1)).getBytes());
            output.addAll((new Instruction(Operation.Call,mainOffset)).getBytes());
            output.addAll((new Instruction(Operation.PopN, 1)).getBytes());
        }
        return output;
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
