package miniplc0java.Stmt;

import miniplc0java.Expr.Expr;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.CompileError;
import miniplc0java.instruction.Instruction;
import miniplc0java.instruction.Operation;
import miniplc0java.tokenizer.TokenType;

public class WhileStmt extends Stmt {
    public WhileStmt (Analyser analyser) {
        super(analyser);
    }

    public void AnalyseWhileStmt () throws CompileError {
        analyser.expect(TokenType.WHILE_KW);

        // 标记一下expr开始的位置
        Jump jump2 = new Jump(Analyser.instructions.size());

        Expr expr = new Expr(analyser);
        expr.AnalyseExpr();

        Analyser.AddInstruction(new Instruction(Operation.Brtrue, 1));

        // 判断失败，跳转出block
        Jump jump1 = new Jump(Analyser.instructions.size());
        Analyser.AddInstruction(new Instruction(Operation.Br, jump1));

        BlockStmt blockStmt = new BlockStmt(analyser);
        blockStmt.AnalyseStmt();

        // 跳回到expr
        jump2.setJumpNum(jump2.getJumpNum() - Analyser.instructions.size() - 1);
        Analyser.AddInstruction(new Instruction(Operation.Br, jump2));

        // 回填jump1
        jump1.setJumpNum(Analyser.instructions.size() - jump1.getJumpNum() - 1);


    }


}
