package miniplc0java.Expr;

import miniplc0java.SymbolTable.Kind;
import miniplc0java.SymbolTable.Symbol;
import miniplc0java.SymbolTable.SymbolTable;
import miniplc0java.SymbolTable.Type;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.AnalyzeError;
import miniplc0java.error.CompileError;
import miniplc0java.error.ErrorCode;
import miniplc0java.instruction.Instruction;
import miniplc0java.instruction.Operation;
import miniplc0java.tokenizer.Token;
import miniplc0java.tokenizer.TokenType;

import java.util.ArrayList;
import java.util.List;

public class IdentAssignCallExpr extends Expr {
    public IdentAssignCallExpr(Analyser analyser) {
        super(analyser);
    }

    //分析函数
    public Type AnalyseIdentAssignCallExpr () throws CompileError {
        Token Ident = analyser.expect(TokenType.IDENT);
        SymbolTable symbolTable = SymbolTable.getInstance();

        // 如果下一个是赋值号，说明可以继续分析为AssignExpr
        if (analyser.peek().getTokenType() == TokenType.ASSIGN) {

            Symbol s = symbolTable.searchVarArgSymbol((String)Ident.getValue(), SymbolTable.LEVEL);
            if (s==null) throw new AnalyzeError(ErrorCode.NoSuchSymbol, analyser.peek().getStartPos());

            //首先要检查一下是不是常量，常量无法赋值
            if (s.isConstant()) {
                throw new AnalyzeError(ErrorCode.AssignToConstant, analyser.peek().getStartPos());
            }

            if (s.getLevel() == -1) {
                Analyser.AddInstruction(new Instruction(Operation.GlobA, s.getStackOffset()));
            } else {
                if (s.getKind() == Kind.Arg) {
                    Analyser.AddInstruction(new Instruction(Operation.ArgA, s.getStackOffset()));
                } else {
                    Analyser.AddInstruction(new Instruction(Operation.LocA, s.getStackOffset()));
                }
            }

            analyser.next();
            Type RType = AnalyseExpr();

            Analyser.AddInstruction(new Instruction(Operation.Store64, null));

            // 检查一下赋值语句两端类型是否相同
            if (RType!=s.getType()) throw new AnalyzeError(ErrorCode.TypeNotMatch, analyser.peek().getStartPos());

            return Type.Void;

        }
        // 分析 call expr
        else if (analyser.peek().getTokenType() == TokenType.L_PAREN) {

            Symbol s = symbolTable.searchFuncSymbol((String)Ident.getValue());
            // 先判断是不是库函数
            if (((String)Ident.getValue()).equals("getint")) {
                return AnalyseGetInt();
            }
            else if (((String)Ident.getValue()).equals("getdouble")) {
                return AnalyseGetDouble();
            }
            else if (((String)Ident.getValue()).equals("getchar")) {
                return AnalyseGetChar();
            }
            else if (((String)Ident.getValue()).equals("putint")) {
                return AnalysePutInt();
            }
            else if (((String)Ident.getValue()).equals("putdouble")) {
                return AnalysePutDouble();
            }
            else if (((String)Ident.getValue()).equals("putchar")) {
                return AnalysePutChar();
            }
            else if (((String)Ident.getValue()).equals("putstr")) {
                return AnalysePutStr();
            }
            else if (((String)Ident.getValue()).equals("putln")) {
                return AnalysePutLn();
            }
            else if (s==null) throw new AnalyzeError(ErrorCode.NoSuchSymbol, analyser.peek().getStartPos());

            analyser.next();// (

            if (analyser.peek().getTokenType() == TokenType.R_PAREN) {
                analyser.next();
                // 说明无参数
                if (s.getArgs().size() != 0) throw new AnalyzeError(ErrorCode.ArgsNotMatch, analyser.peek().getStartPos());
                if (s.getType() == Type.Void) Analyser.AddInstruction(new Instruction(Operation.Stackalloc, 0));
                else Analyser.AddInstruction(new Instruction(Operation.Stackalloc, 1));

                Analyser.AddInstruction(new Instruction(Operation.Call, s.getStackOffset()));
                return s.getType();
            } else {

                if (s.getType() == Type.Void) Analyser.AddInstruction(new Instruction(Operation.Stackalloc, 0));
                else Analyser.AddInstruction(new Instruction(Operation.Stackalloc, 1));

                // 分析参数列表
                List<Type> args = new ArrayList<>();
                args.add(AnalyseExpr());

                while (analyser.peek().getTokenType() == TokenType.COMMA) {
                    analyser.next();
                    args.add(AnalyseExpr());
                }
                analyser.expect(TokenType.R_PAREN);

                // 检查参数列表是否一致
                if (args.size() == s.getArgs().size()) {
                    for (int i=0; i<args.size(); i++) {
                        if (args.get(i) != s.getArgs().get(i)) throw new AnalyzeError(ErrorCode.ArgsNotMatch, analyser.peek().getStartPos());
                    }
                } else throw new AnalyzeError(ErrorCode.ArgsNotMatch, analyser.peek().getStartPos());

                Analyser.AddInstruction(new Instruction(Operation.Call, s.getStackOffset()));
                return s.getType();
            }

        }
        else {
            // 如果不是 = 也不是 （，那就是单纯的的IdentExpr
            Symbol s = symbolTable.searchVarArgSymbol((String)Ident.getValue(), SymbolTable.LEVEL);
            if (s==null) throw new AnalyzeError(ErrorCode.NoSuchSymbol, analyser.peek().getStartPos());

            if (s.getLevel() == -1) {
                Analyser.AddInstruction(new Instruction(Operation.GlobA, s.getStackOffset()));
            } else {
                if (s.getKind() == Kind.Arg) {
                    Analyser.AddInstruction(new Instruction(Operation.ArgA, s.getStackOffset()));
                } else {
                    Analyser.AddInstruction(new Instruction(Operation.LocA, s.getStackOffset()));
                }
            }

            Analyser.AddInstruction(new Instruction(Operation.Load64, null));

            return s.getType();
        }
    }

    public Type AnalyseGetInt () throws CompileError {
        analyser.expect(TokenType.L_PAREN);
        analyser.expect(TokenType.R_PAREN);
        Analyser.AddInstruction(new Instruction(Operation.ScanI, null));
        return Type.Int;
    }

    public Type AnalyseGetDouble () throws CompileError {
        analyser.expect(TokenType.L_PAREN);
        analyser.expect(TokenType.R_PAREN);
        Analyser.AddInstruction(new Instruction(Operation.ScanF, null));
        return Type.Double;
    }

    public Type AnalyseGetChar () throws CompileError {
        analyser.expect(TokenType.L_PAREN);
        analyser.expect(TokenType.R_PAREN);
        Analyser.AddInstruction(new Instruction(Operation.ScanC, null));
        return Type.Int;
    }

    public Type AnalysePutInt() throws CompileError {
        analyser.expect(TokenType.L_PAREN);
        Type exprType = new Expr(analyser).AnalyseExpr();
        if (exprType != Type.Int) throw new AnalyzeError(ErrorCode.TypeNotMatch, analyser.peek().getStartPos());
        analyser.expect(TokenType.R_PAREN);

        Analyser.AddInstruction(new Instruction(Operation.PrintI, null));

        return Type.Void;
    }

    public Type AnalysePutDouble() throws CompileError {
        analyser.expect(TokenType.L_PAREN);
        Type exprType = new Expr(analyser).AnalyseExpr();
        if (exprType != Type.Double) throw new AnalyzeError(ErrorCode.TypeNotMatch, analyser.peek().getStartPos());
        analyser.expect(TokenType.R_PAREN);

        Analyser.AddInstruction(new Instruction(Operation.PrintF, null));

        return Type.Void;
    }

    public Type AnalysePutChar() throws CompileError {
        analyser.expect(TokenType.L_PAREN);
        Type exprType = new Expr(analyser).AnalyseExpr();
        if (exprType != Type.Int) throw new AnalyzeError(ErrorCode.TypeNotMatch, analyser.peek().getStartPos());
        analyser.expect(TokenType.R_PAREN);

        Analyser.AddInstruction(new Instruction(Operation.PrintC, null));

        return Type.Void;
    }

    public Type AnalysePutStr() throws CompileError {
        analyser.expect(TokenType.L_PAREN);
        Type exprType = new Expr(analyser).AnalyseExpr();
        if (exprType != Type.Int) throw new AnalyzeError(ErrorCode.TypeNotMatch, analyser.peek().getStartPos());
        analyser.expect(TokenType.R_PAREN);

        Analyser.AddInstruction(new Instruction(Operation.PrintS, null));

        return Type.Void;
    }

    public Type AnalysePutLn() throws CompileError {
        analyser.expect(TokenType.L_PAREN);
        analyser.expect(TokenType.R_PAREN);

        Analyser.AddInstruction(new Instruction(Operation.Println, null));

        return Type.Void;
    }

}
