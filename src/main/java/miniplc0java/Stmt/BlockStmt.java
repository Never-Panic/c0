package miniplc0java.Stmt;

import miniplc0java.SymbolTable.SymbolTable;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.CompileError;
import miniplc0java.tokenizer.TokenType;

public class BlockStmt extends Stmt {

    public BlockStmt (Analyser analyser) {
        super(analyser);
    }

    public void AnalyseBlockStmt () throws CompileError {
        analyser.expect(TokenType.L_BRACE);

        SymbolTable.LEVEL++;
        while (analyser.peek().getTokenType() != TokenType.R_BRACE) {
            AnalyseStmt();
        }

        // 退出该层时，将该层的符号表弹出
        SymbolTable.getInstance().deleteSymbolOfLevel(SymbolTable.LEVEL);
        SymbolTable.LEVEL--;

        analyser.expect(TokenType.R_BRACE);
    }
}
