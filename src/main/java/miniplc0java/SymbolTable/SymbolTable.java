package miniplc0java.SymbolTable;

import miniplc0java.error.AnalyzeError;
import miniplc0java.error.ErrorCode;

import java.util.ArrayList;
import java.util.List;

public class SymbolTable {
    List<Symbol> symbolList = new ArrayList<>();
    private int argCount = 1; // arg0 被返回值占用
    private int funcCount = 1; // func0 被_start占用

    private int localCount = 0;
    private int globalCount = 0;

    // 全局唯一的层级指针，表示当前分析到的语句在第几层，一开始为-1，即为全局层级
    public static int LEVEL = -1;

    // 单例模式
    private static SymbolTable instance = new SymbolTable();
    private SymbolTable(){}
    public static SymbolTable getInstance() {
        return instance;
    }

    public void addSymbol (Symbol symbol) throws AnalyzeError {

        int level = symbol.level;
        String name = symbol.name;

        // 查看是否重名
        for (Symbol s: symbolList){
            // 添加只查当前层
            if (s.level == level && s.name.equals(name)) throw new AnalyzeError(ErrorCode.DuplicateDeclaration, null);
        }

        // 设置序号，更新count
        if (symbol.kind == Kind.Func) {
            symbol.stackOffset = funcCount;
            funcCount++;

            if (symbol.getLevel() != -1) throw new AnalyzeError(ErrorCode.CannotDecaleFuncInsideBlock, null);

        } else if (symbol.kind== Kind.Arg) {
            symbol.stackOffset = argCount;
            argCount++;
        } else if (symbol.kind == Kind.Var) {
            if (symbol.level == -1) {
                // 全局变量
                symbol.stackOffset = globalCount;
                globalCount++;
            } else {
                // 局部变量
                symbol.stackOffset = localCount;
                localCount++;
            }
        }

        symbolList.add(symbol);
    }

    // 找函数
    public Symbol searchFuncSymbol (String name) {
        for (Symbol s: symbolList) {
            if (s.level == -1 && s.kind==Kind.Func && s.name.equals(name)) return s;
        }
        return null;
    }

    // 找变量和参数，一层层往下找
    public Symbol searchVarArgSymbol (String name, int level) {
        while (level>=-1) {
            for (Symbol s: symbolList) {
                if (s.level == level && (s.kind==Kind.Var||s.kind==Kind.Arg) && s.name.equals(name)) return s;
            }
            level--;
        }
        return null;
    }

    // 一个块结束的时候调用
    public void deleteSymbolOfLevel (int level) {

        // 删除除了全局变量和函数外的所有东西，即要删除arg，重置argCount/localCount
        if (level == 0) {
            symbolList.removeIf(s -> s.level != -1);
            argCount = 1;
            localCount = 0;
        } else if (level > 0) {
            // 删除当前层的var
            symbolList.removeIf(s -> s.level == level);
        }

    }
}
