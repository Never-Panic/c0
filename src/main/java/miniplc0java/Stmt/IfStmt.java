package miniplc0java.Stmt;

import miniplc0java.Expr.Expr;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.CompileError;
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

        System.out.println("BrTrue(1)"); // 判断成功，进入该if语句的块

        // 判断失败，则跳转这个block结束时的下一条语句
        // TODO 这里就需要 instruction list 了！！！  跳转这个block结束时的下一条语句
        System.out.println("Br(????)");

        // if block
        BlockStmt blockStmt = new BlockStmt(analyser);
        blockStmt.AnalyseBlockStmt();

        // TODO 此时可以回填第一个Br

        // TODO 跳转到所有判断分支结束的时候
        System.out.println("Br(????)");

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

        // TODO  此时可以回填第二个Br

    }
}
