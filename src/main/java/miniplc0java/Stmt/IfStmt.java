package miniplc0java.Stmt;

import miniplc0java.Expr.Expr;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.CompileError;
import miniplc0java.instruction.Instruction;
import miniplc0java.instruction.Operation;
import miniplc0java.tokenizer.TokenType;

public class IfStmt extends Stmt {
    public IfStmt (Analyser analyser) {
        super(analyser);
    }

    //分析函数
    public void AnalyseIfStmt () throws CompileError {
        analyser.expect(TokenType.IF_KW);
        Expr expr = new Expr(analyser);
        expr.AnalyseExpr(); // 已经将结果放在栈上了，只需要用Br即可

        Analyser.AddInstruction(new Instruction(Operation.Brtrue, 1)); // 判断成功，进入该if语句的块

        // 判断失败，则跳转这个block结束时的下一条语句
        // 跳转这个block结束时的下一条语句
        Jump jump1 = new Jump(Analyser.instructions.size());
        Analyser.AddInstruction(new Instruction(Operation.Br, jump1));

        // if block
        BlockStmt blockStmt = new BlockStmt(analyser);
        blockStmt.AnalyseBlockStmt();

        // 此时可以回填第一个Br
        jump1.setJumpNum(Analyser.instructions.size() - jump1.getJumpNum());

        // 跳转到所有判断分支结束的时候
        Jump jump2 = new Jump(Analyser.instructions.size());
        Analyser.AddInstruction(new Instruction(Operation.Br, jump2));

        if (analyser.peek().getTokenType()==TokenType.ELSE_KW){
            analyser.next();

            // else { } 必然是最后一个，不用跳了
            if (analyser.peek().getTokenType() == TokenType.L_BRACE) {
                blockStmt.AnalyseBlockStmt();
            }
            // else if expr { }
            else if (analyser.peek().getTokenType() == TokenType.IF_KW) {
                AnalyseIfStmt();
            }

        }

        // 此时可以回填第二个Br
        jump2.setJumpNum(Analyser.instructions.size() - jump2.getJumpNum()-1);

    }
}
