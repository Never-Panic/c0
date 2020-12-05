package miniplc0java.Stmt;

import miniplc0java.analyser.Analyser;
import miniplc0java.error.CompileError;
import miniplc0java.tokenizer.TokenType;

public class Stmt {
    Analyser analyser;

    public Stmt (Analyser analyser) {
        this.analyser = analyser;
    }

    public void AnalyseStmt () throws CompileError {
        DeclStmt declStmt = new DeclStmt(analyser);
        ExprStmt exprStmt = new ExprStmt(analyser);
        BlockStmt blockStmt = new BlockStmt(analyser);
        IfStmt ifStmt = new IfStmt(analyser);

        if (analyser.peek().getTokenType() == TokenType.LET_KW||analyser.peek().getTokenType() == TokenType.CONST_KW){
            declStmt.AnalyseDeclStmt();
        } else if (analyser.peek().getTokenType() == TokenType.L_BRACE) {
            blockStmt.AnalyseBlockStmt();
        } else if (analyser.peek().getTokenType() == TokenType.SEMICOLON){
            //空语句，不做处理
            analyser.next();
        } else if (analyser.peek().getTokenType() == TokenType.IF_KW) {
            ifStmt.AnalyseIfStmt();
        }
        else {
            exprStmt.AnalyseExprStmt();
        }

    }
}
