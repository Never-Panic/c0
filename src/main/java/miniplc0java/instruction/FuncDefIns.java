package miniplc0java.instruction;

import miniplc0java.util.PrintUtil;

import java.util.ArrayList;
import java.util.List;

public class FuncDefIns extends Instruction {
    // 5 个参数

    /// 函数名称在全局变量中的位置
    private int num;
    /// 返回值占据的 slot 数
    private int return_slots;
    /// 参数占据的 slot 数
    private int arg_slots;
    /// 局部变量占据的 slot 数
    private int loc_slots;
    /// 指令个数
    private int body_count;


    public FuncDefIns () {};




    public List<Byte> getBytes () {
        List<Byte> output = new ArrayList<>();

        // functions[0].name
        List<Byte> name = PrintUtil.int2bytes(4, num);
        output.addAll(name);

        // functions[0].ret_slots
        List<Byte> retSlots = PrintUtil.int2bytes(4, return_slots);
        output.addAll(retSlots);

        // functions[0].param_slots
        List<Byte> paramsSlots = PrintUtil.int2bytes(4, arg_slots);
        output.addAll(paramsSlots);

        // functions[0].loc_slots
        List<Byte> locSlots = PrintUtil.int2bytes(4, loc_slots);
        output.addAll(locSlots);

        // functions[0].body.count
        List<Byte> bodyCount = PrintUtil.int2bytes(4, body_count);
        output.addAll(bodyCount);

        return output;
    }


    @Override
    public String toString() {
        return "FuncDefIns{" +
                "num=" + num +
                ", loc_slots=" + loc_slots +
                ", arg_slots=" + arg_slots +
                ", return_slots=" + return_slots +
                ", body_count=" + body_count +
                '}';
    }


    public void setNum(int num) {
        this.num = num;
    }

    public void setReturn_slots(int return_slots) {
        this.return_slots = return_slots;
    }

    public void setArg_slots(int arg_slots) {
        this.arg_slots = arg_slots;
    }

    public void setLoc_slots(int loc_slots) {
        this.loc_slots = loc_slots;
    }

    public void setBody_count(int body_count) {
        this.body_count = body_count;
    }

    public int getBody_count() {
        return body_count;
    }
}
