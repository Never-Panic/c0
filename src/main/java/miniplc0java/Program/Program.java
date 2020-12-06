package miniplc0java.Program;

import miniplc0java.Function.Function;
import miniplc0java.Stmt.DeclStmt;
import miniplc0java.Stmt.Stmt;
import miniplc0java.SymbolTable.Kind;
import miniplc0java.SymbolTable.Symbol;
import miniplc0java.SymbolTable.SymbolTable;
import miniplc0java.SymbolTable.Type;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.AnalyzeError;
import miniplc0java.error.CompileError;
import miniplc0java.error.ErrorCode;
import miniplc0java.error.TokenizeError;
import miniplc0java.instruction.CallMainIns;
import miniplc0java.instruction.FuncDefIns;
import miniplc0java.instruction.Instruction;
import miniplc0java.tokenizer.TokenType;

import java.util.ArrayList;

public class Program {
    Analyser analyser;
    SymbolTable symbolTable = SymbolTable.getInstance();

    public Program (Analyser analyser) {
        this.analyser = analyser;
    }

    public void AnalyseProgram () throws CompileError {

        // _start函数的自动生成
        FuncDefIns funcDefIns = new FuncDefIns();
        Analyser.AddInstruction(funcDefIns);

        /** decl_stmt*/
        // 也就是_start函数的内容
        DeclStmt declStmt = new DeclStmt(analyser);
        while (analyser.peek().getTokenType() != TokenType.FN_KW) {
            declStmt.AnalyseDeclStmt();
        }

        // call main
        CallMainIns callMainIns = new CallMainIns();
        Analyser.AddInstruction(callMainIns);

        // 回填funcDefIns
        /// 返回值占据的 slot 数
        funcDefIns.setReturn_slots(0);
        /// 参数占据的 slot 数
        funcDefIns.setArg_slots(0);
        /// 局部变量占据的 slot 数
        funcDefIns.setLoc_slots(0);
        /// 指令个数
        funcDefIns.setBody_count(Analyser.instructions.size()-1);



        /** function*/
        Function function = new Function(analyser);
        while (analyser.peek().getTokenType() != TokenType.EOF) {
            function.AnalyseFunction();
        }

        // 一个合法的 c0 程序必须存在一个名为 main 的函数作为程序入口
        Symbol main = symbolTable.searchFuncSymbol("main");
        if (main == null) throw new AnalyzeError(ErrorCode.NoMainFunc, analyser.peek().getStartPos());


        // 设置 callMainIns
        callMainIns.setVoid(main.getType() == Type.Void);
        callMainIns.setMainOffset(main.getStackOffset());


        //设置_start的指令数量
        if (main.getType() == Type.Void) {
            // +1
            funcDefIns.setBody_count(funcDefIns.getBody_count()+1);
        } else {
            // +2
            funcDefIns.setBody_count(funcDefIns.getBody_count()+2);
        }


        // 最后再来填全局变量_start, 添加到符号表
        Symbol symbol = new Symbol("_start", Kind.Func, Type.Void, -1);
        symbol.setValue("_start");
        symbolTable.addSymbol(symbol);


        /// 函数名称在全局变量中的位置
        funcDefIns.setNum(symbol.getStackOffset()-1 + symbolTable.getGlobalCount());


    }

}
