package miniplc0java.Expr;

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
    public void AnalyseExpr () throws CompileError {
        OperatorAsExpr operatorAsExpr = new OperatorAsExpr(analyser);
        operatorAsExpr.AnalyseOperatorAsExpr();
    }

    // TODO 赋值语句需要特殊处理（不能被使用）（利用 *属性* 文法）
    //分析不是OperatorExpr 和 AsExpr的表达式，以避免左递归
    public void AnalyseNotOperatorAsExpr() throws CompileError {
        NegateExpr negateExpr = new NegateExpr(analyser);
        LiteralExpr literalExpr = new LiteralExpr(analyser);
        GroupExpr groupExpr = new GroupExpr(analyser);

        if (analyser.peek().getTokenType() == TokenType.MINUS) negateExpr.AnalyseNegateExpr();
        else if (analyser.peek().getTokenType() == TokenType.L_PAREN) groupExpr.AnalyseGroupExpr();
        else literalExpr.AnalyseLiteralExpr();
    }
}