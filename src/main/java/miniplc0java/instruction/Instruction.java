package miniplc0java.instruction;

import java.util.Objects;

public class Instruction {
    private Operation opt;
    private Object Operand; // 操作数

    // TODO 生成二进制代码

    public Instruction (Operation opt, Object Op) {
        this.opt = opt;
        this.Operand = Op;
    }

    @Override
    public String toString() {
        if (Operand == null) {
            return opt.toString();
        }
        return opt+"("+Operand+")";
    }

    //    @Override
//    public String toString() {
//        switch (this.opt) {
//            case ADD:
//            case DIV:
//            case ILL:
//            case MUL:
//            case SUB:
//            case WRT:
//                return String.format("%s", this.opt);
//            case LIT:
//            case LOD:
//            case STO:
//                return String.format("%s %s", this.opt, this.x);
//            default:
//                return "ILL";
//        }
//    }
}
