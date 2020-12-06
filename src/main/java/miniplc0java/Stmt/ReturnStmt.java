package miniplc0java.Stmt;

import miniplc0java.Expr.Expr;
import miniplc0java.Function.Function;
import miniplc0java.SymbolTable.Type;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.AnalyzeError;
import miniplc0java.error.CompileError;
import miniplc0java.error.ErrorCode;
import miniplc0java.instruction.Instruction;
import miniplc0java.instruction.Operation;
import miniplc0java.tokenizer.TokenType;

public class ReturnStmt extends Stmt {
    public ReturnStmt (Analyser analyser) {
        super(analyser);
    }


    public void AnalyseReturnStmt () throws CompileError {
        analyser.expect(TokenType.RETURN_KW);

        if (Function.CurrentFuncType == Type.Void) {
            // 空函数直接输出Ret
            analyser.expect(TokenType.SEMICOLON);
            Analyser.AddInstruction(new Instruction(Operation.Ret, null));
        } else {
            Analyser.AddInstruction(new Instruction(Operation.ArgA, 0));

            // int / double
            Expr expr = new Expr(analyser);
            Type returnType = expr.AnalyseExpr();

            // 检查是否与当前函数类型相匹配
            if (returnType != Function.CurrentFuncType) throw new AnalyzeError(ErrorCode.TypeNotMatch, analyser.peek().getStartPos());

            Analyser.AddInstruction(new Instruction(Operation.Store64, null));
            Analyser.AddInstruction(new Instruction(Operation.Ret, null));

            analyser.expect(TokenType.SEMICOLON);
        }


    }
}
