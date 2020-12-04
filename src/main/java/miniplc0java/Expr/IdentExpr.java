package miniplc0java.Expr;

import miniplc0java.SymbolTable.Symbol;
import miniplc0java.SymbolTable.SymbolTable;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.CompileError;
import miniplc0java.tokenizer.Token;
import miniplc0java.tokenizer.TokenType;

public class IdentExpr extends Expr {
    public IdentExpr (Analyser analyser) {
        super(analyser);
    }

    //分析函数
    public void AnalyseIdentExpr () throws CompileError {
        Token Ident = analyser.expect(TokenType.IDENT);
        SymbolTable symbolTable = SymbolTable.getInstance();

        // TODO 先假设都是level=0 并且是 局部变量, 全局变量要替换为GlobA
        Symbol s = symbolTable.searchVarArgSymbol((String)Ident.getValue(), 0);
        System.out.println("LocA(" + s.getStackOffset() + ")");
        System.out.println("Load64");
    }

}
