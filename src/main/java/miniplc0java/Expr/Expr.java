package miniplc0java.Expr;

import miniplc0java.SymbolTable.Type;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.CompileError;
import miniplc0java.tokenizer.TokenType;

public class Expr {

    //唯一的分析器
    Analyser analyser;

    public Expr (Analyser analyser){
        this.analyser = analyser;
    }

    // 只能分析一句
    public Type AnalyseExpr () throws CompileError {
        OperatorAsExpr operatorAsExpr = new OperatorAsExpr(analyser);
        return operatorAsExpr.AnalyseOperatorAsExpr();
    }

    // TODO 赋值语句需要特殊处理（不能被使用）（利用 *属性* 文法）  (属于语义分析的范畴)
    //分析不是OperatorExpr 和 AsExpr的表达式，以避免左递归
    public Type AnalyseNotOperatorAsExpr() throws CompileError {
        NegateExpr negateExpr = new NegateExpr(analyser);
        LiteralExpr literalExpr = new LiteralExpr(analyser);
        GroupExpr groupExpr = new GroupExpr(analyser);
        IdentAssignCallExpr identAssignCallExpr = new IdentAssignCallExpr(analyser);

        if (analyser.peek().getTokenType() == TokenType.MINUS) return negateExpr.AnalyseNegateExpr();
        else if (analyser.peek().getTokenType() == TokenType.L_PAREN) return groupExpr.AnalyseGroupExpr();
//        else if (analyser.peek().getTokenType() == TokenType.IDENT) return identAssignCallExpr.AnalyseIdentAssignCallExpr();
        else return literalExpr.AnalyseLiteralExpr();
    }
}