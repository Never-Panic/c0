package miniplc0java.Expr;

import miniplc0java.SymbolTable.Type;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.AnalyzeError;
import miniplc0java.error.CompileError;
import miniplc0java.error.ErrorCode;
import miniplc0java.tokenizer.TokenType;

public class NegateExpr extends Expr {
    public NegateExpr (Analyser analyser) {super(analyser);}

    //分析函数
    public Type AnalyseNegateExpr() throws CompileError {
        analyser.expect(TokenType.MINUS);
        //分析不是Operator As的表达式，因为取反优先级更高，如果这条语句分析了OperatorAsExpr，则优先级出错
        Type type = AnalyseNotOperatorAsExpr();
        //总是在最后输出NEG
        if (type == Type.Int) {
            System.out.println("NegI");
        } else if (type == Type.Double) {
            System.out.println("NegF");
        } else throw new AnalyzeError(ErrorCode.UseVoid, analyser.peek().getStartPos());

        return type;
    }
}
