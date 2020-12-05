package miniplc0java.Function;

import miniplc0java.Stmt.BlockStmt;
import miniplc0java.SymbolTable.Kind;
import miniplc0java.SymbolTable.Symbol;
import miniplc0java.SymbolTable.SymbolTable;
import miniplc0java.SymbolTable.Type;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.AnalyzeError;
import miniplc0java.error.CompileError;
import miniplc0java.error.ErrorCode;
import miniplc0java.error.TokenizeError;
import miniplc0java.tokenizer.Token;
import miniplc0java.tokenizer.TokenType;

import java.util.ArrayList;

public class Function {
    Analyser analyser;
    public Function (Analyser analyser) {
        this.analyser = analyser;
    }


    public void AnalyseFunction () throws CompileError {
        analyser.expect(TokenType.FN_KW);
        Token Ident = analyser.expect(TokenType.IDENT);
        analyser.expect(TokenType.L_PAREN);

        ArrayList<Type> args = new ArrayList<>();

        if (analyser.peek().getTokenType() != TokenType.R_PAREN) args = AnalyseFunctionParamList ();

        analyser.expect(TokenType.R_PAREN);
        analyser.expect(TokenType.ARROW);

        Token ty = analyser.expect(TokenType.IDENT);
        Symbol symbol;
        // 可以递归，所以在这里填表
        if (((String)ty.getValue()).equals("int")){
            symbol = new Symbol((String)Ident.getValue(), Kind.Func, Type.Int, SymbolTable.LEVEL);
        } else if (((String)ty.getValue()).equals("double")) {
            symbol = new Symbol((String)Ident.getValue(), Kind.Func, Type.Double, SymbolTable.LEVEL);
        } else if (((String)ty.getValue()).equals("void")) {
            symbol = new Symbol((String)Ident.getValue(), Kind.Func, Type.Void, SymbolTable.LEVEL);
        } else throw new AnalyzeError(ErrorCode.InvalidInput, analyser.peek().getStartPos());
        symbol.setArgs(args);

        SymbolTable symbolTable = SymbolTable.getInstance();
        symbolTable.addSymbol(symbol);

        // todo 输出到instructions
        System.out.println("fn["+symbol.getStackOffset()+"]{");

        BlockStmt blockStmt = new BlockStmt(analyser);
        blockStmt.AnalyseBlockStmt();

        System.out.println("}");

    }

    private ArrayList<Type> AnalyseFunctionParamList () throws CompileError {
        ArrayList<Type> args = new ArrayList<>();

        args.add(AnalyseFunctionParam());

        while (analyser.peek().getTokenType() == TokenType.COMMA){
            analyser.next();
            args.add(AnalyseFunctionParam());
        }
        return args;
    }

    private Type AnalyseFunctionParam () throws CompileError {
        boolean isConst = false;

        if (analyser.peek().getTokenType() == TokenType.CONST_KW) {
            isConst = true;
            analyser.next();
        }

        Token Ident = analyser.expect(TokenType.IDENT);
        analyser.expect(TokenType.COLON);
        Token ty = analyser.expect(TokenType.IDENT);

        Symbol symbol;

        SymbolTable symbolTable = SymbolTable.getInstance();

        if (((String)ty.getValue()).equals("int")) {
            symbol = new Symbol((String)Ident.getValue(), Kind.Arg, Type.Int, 0);
            symbol.setConstant(isConst);
            symbolTable.addSymbol(symbol);
            return Type.Int;
        } else if (((String)ty.getValue()).equals("double")) {
            symbol = new Symbol((String)Ident.getValue(), Kind.Arg, Type.Double, 0);
            symbol.setConstant(isConst);
            symbolTable.addSymbol(symbol);
            return Type.Double;
        } else throw new AnalyzeError(ErrorCode.InvalidInput, analyser.peek().getStartPos());
    }

}
