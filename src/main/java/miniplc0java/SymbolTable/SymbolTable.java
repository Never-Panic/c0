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

    // TODO 嵌套

    public void addSymbol (Symbol symbol) throws AnalyzeError {

        int level = symbol.level;
        String name = symbol.name;

        // 查看是否重名
        for (Symbol s: symbolList){
            // 添加只查当前层
            if (s.level == level && s.name.equals(name)) throw new AnalyzeError(ErrorCode.DuplicateDeclaration, null);
        }

        // 设置序号，更新count
        if (symbol.kind.equals("func")) {
            symbol.num = funcCount;
            funcCount++;
        } else if (symbol.kind.equals("arg")) {
            symbol.num = argCount;
            argCount++;
        } else if (symbol.kind.equals("var")) {
            if (symbol.level == -1) {
                // 全局变量
                symbol.num = globalCount;
                globalCount++;
            } else {
                // 局部变量
                symbol.num = localCount;
                localCount++;
            }
        } else throw new Error("程序写错了，Symbol的kind只能是func/arg/var");

        symbolList.add(symbol);
    }

    // 找函数
    public Symbol searchFuncSymbol (String name) {
        for (Symbol s: symbolList) {
            if (s.level == -1 && s.kind.equals("func") && s.name.equals(name)) return s;
        }
        return null;
    }

    // 找变量和参数，一层层往下找
    public Symbol searchVarArgSymbol (String name, int level) {
        while (level>=-1) {
            for (Symbol s: symbolList) {
                if (s.level == level && (s.kind.equals("var")||s.kind.equals("arg")) && s.name.equals(name)) return s;
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
