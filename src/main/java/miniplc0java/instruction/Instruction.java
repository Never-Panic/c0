package miniplc0java.instruction;

import miniplc0java.Stmt.Jump;
import miniplc0java.analyser.Analyser;
import miniplc0java.util.PrintUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Instruction {
    private Operation opt;
    private Object Operand; // 操作数
    private int num;

    public Instruction (Operation opt, Object Op) {
        this.opt = opt;
        this.Operand = Op;
        num = Analyser.instructions.size();
    }

    // 默认构造器
    public Instruction () {};


    public List<Byte> getBytes () {
        List<Byte> output = new ArrayList<>();

        // operation
        List<Byte> operation = PrintUtil.int2bytes(1, opt.getNum());
        output.addAll(operation);

        // operand
        if (Operand==null) {
            return output;
        } else {
            List<Byte> operand = new ArrayList<>();

            if (opt == Operation.Push) {
                // num:u64
                // todo Double

                try {
                    operand = PrintUtil.long2bytes(8, (int) Operand);
                } catch (Exception e) {
                    operand = PrintUtil.double2bytes(8, (double) Operand);
                }

            } else if (opt == Operation.PopN) {
                // num:u32
                operand = PrintUtil.int2bytes(4, (int) Operand);
            } else if (opt == Operation.LocA) {
                // off:u32
                operand = PrintUtil.int2bytes(4, (int) Operand);
            } else if (opt == Operation.ArgA) {
                // off:u32
                operand = PrintUtil.int2bytes(4, (int) Operand);
            } else if (opt == Operation.GlobA) {
                // n:u32
                operand = PrintUtil.int2bytes(4, (int) Operand);
            } else if (opt == Operation.Stackalloc) {
                // size:u32
                operand = PrintUtil.int2bytes(4, (int) Operand);
            } else if (opt == Operation.Br) {
                // off:i32
                operand = PrintUtil.int2bytes(4, ((Jump) Operand).getJumpNum());
            } else if (opt == Operation.Brtrue) {
                // off:i32
                operand = PrintUtil.int2bytes(4, (int) Operand);
            } else if (opt == Operation.Call) {
                // id:u32
                operand = PrintUtil.int2bytes(4, (int) Operand);
            }

            output.addAll(operand);
        }

        return output;
    }


    @Override
    public String toString() {
        if (Operand == null) {
            return num+": "+opt.toString();
        }
        return num+": "+opt+"("+Operand+")";
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
