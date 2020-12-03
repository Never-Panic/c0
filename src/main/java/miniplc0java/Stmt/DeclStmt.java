package miniplc0java.Stmt;

import miniplc0java.SymbolTable.Symbol;
import miniplc0java.SymbolTable.SymbolTable;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.AnalyzeError;
import miniplc0java.error.CompileError;
import miniplc0java.error.ErrorCode;
import miniplc0java.tokenizer.Token;
import miniplc0java.tokenizer.TokenType;

public class DeclStmt extends Stmt{
    public DeclStmt (Analyser analyser) {super(analyser);};
    public SymbolTable symbolTable = SymbolTable.getInstance();

    //分析函数
    //TODO 类型的语义分析
    public void AnalyseDeclStmt () throws CompileError {
        if (analyser.peek().getTokenType() == TokenType.LET_KW) {
            analyser.next();

            Token Ident = analyser.expect(TokenType.IDENT);
            analyser.expect(TokenType.COLON);
            Token ty = analyser.expect(TokenType.IDENT);

            // TODO 加符号表时候的level现在随便设了个0, 并且现在默认是初始化的var
            if (((String)ty.getValue()).equals("int")) {
                symbolTable.addSymbol(new Symbol((String)Ident.getValue(), "var", "int", 0));
            } else if (((String)ty.getValue()).equals("double")) {
                symbolTable.addSymbol(new Symbol((String)Ident.getValue(), "var", "double", 0));
            } else throw new AnalyzeError(ErrorCode.InvalidInput, analyser.peek().getStartPos());

            // TODO 该轮到分析=了，我先去洗澡去了

        } else if (analyser.peek().getTokenType() == TokenType.CONST_KW) {

        }
    }
}
