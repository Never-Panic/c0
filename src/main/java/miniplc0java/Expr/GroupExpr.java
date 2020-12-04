package miniplc0java.Expr;

import miniplc0java.SymbolTable.Type;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.CompileError;
import miniplc0java.tokenizer.TokenType;

public class GroupExpr extends Expr {
    public GroupExpr (Analyser analyser){super(analyser);}

    //分析函数
    public Type AnalyseGroupExpr () throws CompileError {
        analyser.expect(TokenType.L_PAREN);
        Type type = AnalyseExpr();
        analyser.expect(TokenType.R_PAREN);
        return type;
    }
}
