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

    public void AnalyseExpr () throws CompileError {
        OperatorAsExpr operatorAsExpr = new OperatorAsExpr(analyser);
        operatorAsExpr.AnalyseOperatorAsExpr();
    }

    //分析不是OperatorExpr 和 AsExpr的表达式，以避免左递归
    public void AnalyseNotOperatorAsExpr() throws CompileError {
        NegateExpr negateExpr = new NegateExpr(analyser);

        if (analyser.peek().getTokenType() == TokenType.MINUS) negateExpr.AnalyseNegateExpr();
        else System.out.println(analyser.next().getValue());
    }
}