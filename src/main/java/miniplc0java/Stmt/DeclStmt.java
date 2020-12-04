package miniplc0java.Stmt;

import miniplc0java.Expr.Expr;
import miniplc0java.SymbolTable.Kind;
import miniplc0java.SymbolTable.Symbol;
import miniplc0java.SymbolTable.SymbolTable;
import miniplc0java.SymbolTable.Type;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.AnalyzeError;
import miniplc0java.error.CompileError;
import miniplc0java.error.ErrorCode;
import miniplc0java.tokenizer.Token;
import miniplc0java.tokenizer.TokenType;

public class DeclStmt extends Stmt{
    public DeclStmt (Analyser analyser) {super(analyser);}
    public SymbolTable symbolTable = SymbolTable.getInstance();

    //分析函数
    public void AnalyseDeclStmt () throws CompileError {
        if (analyser.peek().getTokenType() == TokenType.LET_KW) {
            analyser.next();

            Token Ident = analyser.expect(TokenType.IDENT);
            analyser.expect(TokenType.COLON);
            Token ty = analyser.expect(TokenType.IDENT);

            // TODO 加符号表时候的level现在随便设了个0
            Symbol symbol;

            if (((String)ty.getValue()).equals("int")) {
                symbol = new Symbol((String)Ident.getValue(), Kind.Var, Type.Int, 0);
                symbol.setConstant(false);
                symbolTable.addSymbol(symbol);
            } else if (((String)ty.getValue()).equals("double")) {
                symbol = new Symbol((String)Ident.getValue(), Kind.Var, Type.Double, 0);
                symbol.setConstant(false);
                symbolTable.addSymbol(symbol);
            } else throw new AnalyzeError(ErrorCode.InvalidInput, analyser.peek().getStartPos());

            //TODO 现在只模拟了局部变量，全局变量要替换为GlobA
            if (analyser.peek().getTokenType() == TokenType.ASSIGN) {
                //出现赋值语句，需要输出
                System.out.println("LocA(" + symbol.getStackOffset() + ")");

                analyser.next();
                Expr expr = new Expr(analyser);
                Type RType = expr.AnalyseExpr();
                //检查类型是否一致
                if (RType!=symbol.getType()) throw new AnalyzeError(ErrorCode.TypeNotMatch, analyser.peek().getStartPos());

                System.out.println("Store64");
            }

            analyser.expect(TokenType.SEMICOLON);


        } else if (analyser.peek().getTokenType() == TokenType.CONST_KW) {
            analyser.next();

            Token Ident = analyser.expect(TokenType.IDENT);
            analyser.expect(TokenType.COLON);
            Token ty = analyser.expect(TokenType.IDENT);

            // TODO 加符号表时候的level现在随便设了个0
            Symbol symbol;

            if (((String)ty.getValue()).equals("int")) {
                symbol = new Symbol((String)Ident.getValue(), Kind.Var, Type.Int, 0);
                symbol.setConstant(true);
                symbolTable.addSymbol(symbol);
            } else if (((String)ty.getValue()).equals("double")) {
                symbol = new Symbol((String)Ident.getValue(), Kind.Var, Type.Double, 0);
                symbol.setConstant(true);
                symbolTable.addSymbol(symbol);
            } else throw new AnalyzeError(ErrorCode.InvalidInput, analyser.peek().getStartPos());

            analyser.expect(TokenType.ASSIGN);

            //常量必须出现赋值语句，需要输出
            System.out.println("LocA(" + symbol.getStackOffset() + ")");

            Expr expr = new Expr(analyser);
            Type RType = expr.AnalyseExpr();
            //检查类型是否一致
            if (RType!=symbol.getType()) throw new AnalyzeError(ErrorCode.TypeNotMatch, analyser.peek().getStartPos());

            System.out.println("Store64");

            analyser.expect(TokenType.SEMICOLON);

        } else throw new AnalyzeError(ErrorCode.InvalidInput, analyser.peek().getStartPos());
    }
}
