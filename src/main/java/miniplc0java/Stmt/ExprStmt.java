package miniplc0java.Stmt;

import miniplc0java.Expr.Expr;
import miniplc0java.SymbolTable.Type;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.CompileError;
import miniplc0java.tokenizer.TokenType;

public class ExprStmt extends Stmt{
    public ExprStmt (Analyser analyser) {
        super(analyser);
    }

    public void AnalyseExprStmt () throws CompileError {
        Expr expr = new Expr(analyser);
        Type type = expr.AnalyseExpr();
        analyser.expect(TokenType.SEMICOLON);

        // 表达式如果有值，将会被丢弃
        if (type==Type.Int || type==Type.Double) {
            System.out.println("PopN(1)");
        }
    }
}
