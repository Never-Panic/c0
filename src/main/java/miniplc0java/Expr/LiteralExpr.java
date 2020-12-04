package miniplc0java.Expr;

import miniplc0java.SymbolTable.Type;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.AnalyzeError;
import miniplc0java.error.ErrorCode;
import miniplc0java.error.TokenizeError;
import miniplc0java.tokenizer.TokenType;

public class LiteralExpr extends Expr {
    public LiteralExpr (Analyser analyser) {super(analyser);}

    //分析函数
    public Type AnalyseLiteralExpr () throws TokenizeError, AnalyzeError {
        if (analyser.peek().getTokenType() == TokenType.UINT_LITERAL) {
            System.out.println("Push("+analyser.next().getValue()+")");
            return Type.Int;

        } else if (analyser.peek().getTokenType() == TokenType.DOUBLE_LITERAL) {
            System.out.println("Push("+analyser.next().getValue()+")");
            return Type.Double;

        } else if (analyser.peek().getTokenType() == TokenType.STRING_LITERAL) {
            // TODO 分析下该怎么用，好像是构造一个全局变量

        } else if (analyser.peek().getTokenType() == TokenType.CHAR_LITERAL) {
            System.out.println("Push("+(int)((char)analyser.next().getValue())+")");
            return Type.Int;

        } else throw new AnalyzeError(ErrorCode.InvalidInput, analyser.peek().getStartPos());

        return null;
    }
}
