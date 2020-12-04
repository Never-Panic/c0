package miniplc0java.SymbolTable;

public class Symbol {


    // TODO isConstant isDeclared
    String name;
    String kind; // 参数：arg 变量：var 函数：func
    String type; // int double ?void
    int level; // 全局为-1； 底层为0

    /*
        func,arg从1开始(arg0为返回值)
        var从0开始
     */
    int stackOffset; //栈偏移

    public Symbol(String name, String kind, String type, int level) {
        this.name = name;
        this.kind = kind;
        this.type = type;
        this.level = level;
    }

    public int getStackOffset() {
        return stackOffset;
    }
}
