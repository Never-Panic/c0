package miniplc0java;

import miniplc0java.Function.Function;
import miniplc0java.Stmt.DeclStmt;
import miniplc0java.Stmt.Stmt;
import miniplc0java.SymbolTable.SymbolTable;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.AnalyzeError;
import miniplc0java.error.CompileError;
import miniplc0java.error.ErrorCode;
import miniplc0java.error.TokenizeError;
import miniplc0java.tokenizer.TokenType;

public class Program {
    Analyser analyser;
    SymbolTable symbolTable = SymbolTable.getInstance();

    public Program (Analyser analyser) {
        this.analyser = analyser;
    }

    public void AnalyseProgram () throws CompileError {

        //decl_stmt*
        DeclStmt declStmt = new DeclStmt(analyser);
        while (analyser.peek().getTokenType() != TokenType.FN_KW) {
            declStmt.AnalyseDeclStmt();
        }

        //function*
        Function function = new Function(analyser);
        while (analyser.peek().getTokenType() != TokenType.EOF) {
            function.AnalyseFunction();
        }

        // 一个合法的 c0 程序必须存在一个名为 main 的函数作为程序入口
        if (symbolTable.searchFuncSymbol("main") == null) throw new AnalyzeError(ErrorCode.NoMainFunc, analyser.peek().getStartPos());
    }
}
