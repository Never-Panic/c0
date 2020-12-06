package miniplc0java.Expr;

import miniplc0java.SymbolTable.Kind;
import miniplc0java.SymbolTable.Symbol;
import miniplc0java.SymbolTable.SymbolTable;
import miniplc0java.SymbolTable.Type;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.AnalyzeError;
import miniplc0java.error.ErrorCode;
import miniplc0java.error.TokenizeError;
import miniplc0java.instruction.Instruction;
import miniplc0java.instruction.Operation;
import miniplc0java.tokenizer.Token;
import miniplc0java.tokenizer.TokenType;

public class LiteralExpr extends Expr {
    public LiteralExpr (Analyser analyser) {super(analyser);}

    //分析函数
    public Type AnalyseLiteralExpr () throws TokenizeError, AnalyzeError {
        if (analyser.peek().getTokenType() == TokenType.UINT_LITERAL) {
            Analyser.AddInstruction(new Instruction(Operation.Push, analyser.next().getValue()));
            return Type.Int;

        } else if (analyser.peek().getTokenType() == TokenType.DOUBLE_LITERAL) {
            Analyser.AddInstruction(new Instruction(Operation.Push, analyser.next().getValue()));
            return Type.Double;

        } else if (analyser.peek().getTokenType() == TokenType.STRING_LITERAL) {
            // TODO 构造一个全局变量，输出在o0开头
            Token Str = analyser.next();

            SymbolTable symbolTable = SymbolTable.getInstance();
            // 必须是const，Type无所谓
            Symbol symbol = new Symbol((String) Str.getValue(), Kind.Var, Type.Int, -1);
            symbol.setConstant(true);

            symbolTable.addSymbol(symbol);

            // 现在就需要计算offset = 偏移量 + 已经定义的函数数量
            Analyser.AddInstruction(new Instruction(Operation.Push, symbolTable.getGlobalCount() - 1 + symbolTable.getFuncCount() - 2));

            // 随便返回一个int
            return Type.Int;

        } else if (analyser.peek().getTokenType() == TokenType.CHAR_LITERAL) {
            Analyser.AddInstruction(new Instruction(Operation.Push, (int)((char)analyser.next().getValue())));
            return Type.Int;

        } else throw new AnalyzeError(ErrorCode.InvalidInput, analyser.peek().getStartPos());

    }
}
