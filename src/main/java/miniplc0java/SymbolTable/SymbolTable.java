package miniplc0java.SymbolTable;

import miniplc0java.error.AnalyzeError;
import miniplc0java.error.ErrorCode;

import java.util.ArrayList;
import java.util.List;

public class SymbolTable {
    List<Symbol> symbolList = new ArrayList<>();
    private int argCount = 1; // arg0 被返回值占用
    private int varCount = 0;
    private int funcCount = 1; // func0 被_start占用

    public void addSymbol (Symbol symbol) throws AnalyzeError {

        int level = symbol.level;
        String name = symbol.name;

        // 查看是否重名
        for (Symbol s: symbolList){
            // 添加只查当前层
            if (s.level == level && s.name.equals(name)) throw new AnalyzeError(ErrorCode.DuplicateDeclaration, null);
        }

        // 设置序号，更新count
        if (symbol.kind.equals("arg")) {
            symbol.num = argCount;
            argCount++;
        } else if (symbol.kind.equals("var")) {
            symbol.num = varCount;
            varCount++;
        } else if (symbol.kind.equals("func")) {
            symbol.num = funcCount;
            funcCount++;
        }

        symbolList.add(symbol);
    }

    // 找函数
    public Symbol searchFuncSymbol (String name) {
        for (Symbol s: symbolList) {
            if (s.level == -1 && s.kind.equals("func") && s.name.equals(name)) return s;
        }
        return null;
    }

    // 找变量和参数，一层层往上找
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

        // 删除除了全局变量和函数外的所有东西，即要删除arg
        if (level == 0) {
            symbolList.removeIf(s -> s.level != -1);
        } else if (level > 0) {
            // 删除当前层的var
            symbolList.removeIf(s -> s.level == level);
        }
    }
}
