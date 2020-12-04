package miniplc0java.Expr;

import miniplc0java.SymbolTable.Symbol;
import miniplc0java.SymbolTable.SymbolTable;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.AnalyzeError;
import miniplc0java.error.CompileError;
import miniplc0java.error.ErrorCode;
import miniplc0java.tokenizer.Token;
import miniplc0java.tokenizer.TokenType;

public class IdentAssignExpr extends Expr {
    public IdentAssignExpr(Analyser analyser) {
        super(analyser);
    }

    //分析函数
    public void AnalyseIdentAssignExpr () throws CompileError {
        Token Ident = analyser.expect(TokenType.IDENT);
        SymbolTable symbolTable = SymbolTable.getInstance();

        // TODO 先假设都是level=0 并且是 局部变量, 全局变量要替换为GlobA
        Symbol s = symbolTable.searchVarArgSymbol((String)Ident.getValue(), 0);
        if (s==null) throw new AnalyzeError(ErrorCode.InvalidInput, analyser.peek().getStartPos());


        // 如果下一个是赋值号，说明可以继续分析为AssignExpr
        if (analyser.peek().getTokenType() == TokenType.ASSIGN) {
            System.out.println("LocA(" + s.getStackOffset() +")");

            analyser.next();
            AnalyseExpr();

            System.out.println("Store64");
        } else {
            // 如果不是 = 也不是 （，那就是单纯的的IdentExpr

            System.out.println("LocA(" + s.getStackOffset() + ")");
            System.out.println("Load64");
        }
    }

}
