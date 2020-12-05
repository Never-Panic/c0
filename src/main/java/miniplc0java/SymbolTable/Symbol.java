package miniplc0java.SymbolTable;

import java.util.ArrayList;
import java.util.List;

public class Symbol {


    String name;
    Kind kind; // Arg Var Func
    Type type; // Int Double Void （函数为返回值类型）
    int level; // 全局为-1； 底层为0

    // 如果是var的话，有这个值
    boolean isConstant;

    // 如果是函数，有参数类型表
    List<Type> args = new ArrayList<>();

    /*
        func,arg从1开始(arg0为返回值)
        var从0开始
     */
    int stackOffset; //栈偏移

    public Symbol(String name, Kind kind, Type type, int level) {
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.level = level;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Kind getKind() {
        return kind;
    }

    public void setKind(Kind kind) {
        this.kind = kind;
    }

    public int getLevel() {
        return level;
    }

    public int getStackOffset() {
        return stackOffset;
    }

    public boolean isConstant() {
        return isConstant;
    }

    public void setConstant(boolean constant) {
        isConstant = constant;
    }

    public List<Type> getArgs() {
        return args;
    }

    public void setArgs(List<Type> args) {
        this.args = args;
    }
}
