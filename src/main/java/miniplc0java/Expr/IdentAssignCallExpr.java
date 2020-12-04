package miniplc0java.Expr;

import miniplc0java.SymbolTable.Symbol;
import miniplc0java.SymbolTable.SymbolTable;
import miniplc0java.SymbolTable.Type;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.AnalyzeError;
import miniplc0java.error.CompileError;
import miniplc0java.error.ErrorCode;
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
            // TODO 先假设都是level=0
            Symbol s = symbolTable.searchVarArgSymbol((String)Ident.getValue(), 0);
            if (s==null) throw new AnalyzeError(ErrorCode.NoSuchSymbol, analyser.peek().getStartPos());

            //首先要检查一下是不是常量，常量无法赋值
            if (s.isConstant()) {
                throw new AnalyzeError(ErrorCode.AssignToConstant, analyser.peek().getStartPos());
            }

            System.out.println("LocA(" + s.getStackOffset() +")");

            analyser.next();
            AnalyseExpr();

            System.out.println("Store64");

            return Type.Void;

        }
        else if (analyser.peek().getTokenType() == TokenType.L_PAREN) {
            // 分析 call expr
            analyser.next();

            Symbol s = symbolTable.searchFuncSymbol((String)Ident.getValue());
            if (s==null) throw new AnalyzeError(ErrorCode.NoSuchSymbol, analyser.peek().getStartPos());

            if (analyser.peek().getTokenType() == TokenType.R_PAREN) {
                analyser.next();
                // 说明无参数
                if (s.getArgs().size() != 0) throw new AnalyzeError(ErrorCode.ArgsNotMatch, analyser.peek().getStartPos());
                System.out.println("StackAlloc(1)");
                System.out.println("Call(" + s.getStackOffset() + ")");
                return s.getType();
            } else {
                System.out.println("StackAlloc(1)");

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

                System.out.println("Call(" + s.getStackOffset() + ")");
                return s.getType();
            }

        }
        else {
            // 如果不是 = 也不是 （，那就是单纯的的IdentExpr
            // TODO 先假设都是level=0
            Symbol s = symbolTable.searchVarArgSymbol((String)Ident.getValue(), 0);
            if (s==null) throw new AnalyzeError(ErrorCode.NoSuchSymbol, analyser.peek().getStartPos());

            System.out.println("LocA(" + s.getStackOffset() + ")");
            System.out.println("Load64");

            return s.getType();
        }
    }

}
