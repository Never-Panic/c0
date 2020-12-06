package miniplc0java.Expr;

import miniplc0java.SymbolTable.Type;
import miniplc0java.analyser.Analyser;
import miniplc0java.error.AnalyzeError;
import miniplc0java.error.CompileError;
import miniplc0java.error.ErrorCode;
import miniplc0java.error.TokenizeError;
import miniplc0java.instruction.Instruction;
import miniplc0java.instruction.Operation;
import miniplc0java.tokenizer.Token;
import miniplc0java.tokenizer.TokenType;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class OperatorAsExpr extends Expr{

    private Stack<Token> OperatorStack = new Stack<>();
    private Stack<Type> TypeStack  = new Stack<>();

    //运算符的优先级
    static Map<TokenType, Integer> precedence = new HashMap<>();
    static {
        //设置优先级
        precedence.put(TokenType.MUL, 2);
        precedence.put(TokenType.DIV, 2);

        precedence.put(TokenType.PLUS, 1);
        precedence.put(TokenType.MINUS, 1);

        precedence.put(TokenType.GT, 0);
        precedence.put(TokenType.LT, 0);
        precedence.put(TokenType.GE, 0);
        precedence.put(TokenType.LE, 0);
        precedence.put(TokenType.EQ, 0);
        precedence.put(TokenType.NEQ, 0);
    }

    public OperatorAsExpr(Analyser analyser) {
        super(analyser);
    }

    //移进函数
    private void move (Token token, Type type) {
        OperatorStack.push(token);
        TypeStack.push(type);
    }

    //规约函数，总是规约栈顶的符号，并输出对应的机器码
    //由于先前的expr已经在栈上留了一个数据，所以当我们规约的时候，直接输出其机器码就可以了
    private void parse () throws AnalyzeError, TokenizeError {
        Token t = OperatorStack.pop();
        // 检查运算符两边类型是否一样
        Type Rtype = TypeStack.pop();
        Type Ltype = TypeStack.pop();
        if (Rtype!=Ltype) throw new AnalyzeError(ErrorCode.TypeNotMatch, analyser.peek().getStartPos());
        if (Rtype==Type.Void) throw new AnalyzeError(ErrorCode.UseVoid, analyser.peek().getStartPos());
        TypeStack.push(Rtype);

        if (Rtype == Type.Int){
            if (t.getTokenType()==TokenType.MUL) Analyser.AddInstruction(new Instruction(Operation.MulI, null));
            else if (t.getTokenType()==TokenType.DIV) Analyser.AddInstruction(new Instruction(Operation.DivI, null));
            else if (t.getTokenType()==TokenType.PLUS) Analyser.AddInstruction(new Instruction(Operation.AddI, null));
            else if (t.getTokenType()==TokenType.MINUS) Analyser.AddInstruction(new Instruction(Operation.SubI, null));
            else if (t.getTokenType()==TokenType.GT) {
                Analyser.AddInstruction(new Instruction(Operation.CmpI, null));
                Analyser.AddInstruction(new Instruction(Operation.SetGt, null));
            }
            else if (t.getTokenType()==TokenType.LT) {
                Analyser.AddInstruction(new Instruction(Operation.CmpI, null));
                Analyser.AddInstruction(new Instruction(Operation.SetLt, null));
            }
            else if (t.getTokenType()==TokenType.GE) {
                Analyser.AddInstruction(new Instruction(Operation.CmpI, null));
                Analyser.AddInstruction(new Instruction(Operation.SetLt, null));
                Analyser.AddInstruction(new Instruction(Operation.Not, null));
            }
            else if (t.getTokenType()==TokenType.LE) {
                Analyser.AddInstruction(new Instruction(Operation.CmpI, null));
                Analyser.AddInstruction(new Instruction(Operation.SetGt, null));
                Analyser.AddInstruction(new Instruction(Operation.Not, null));
            }
            else if (t.getTokenType()==TokenType.EQ) {
                Analyser.AddInstruction(new Instruction(Operation.CmpI, null));
                Analyser.AddInstruction(new Instruction(Operation.Not, null));
            }
            else if (t.getTokenType()==TokenType.NEQ) {
                Analyser.AddInstruction(new Instruction(Operation.CmpI, null));
            }
            else throw new AnalyzeError(ErrorCode.InvalidInput, t.getStartPos());
        } else {
            if (t.getTokenType()==TokenType.MUL) Analyser.AddInstruction(new Instruction(Operation.MulF, null));
            else if (t.getTokenType()==TokenType.DIV) Analyser.AddInstruction(new Instruction(Operation.DivF, null));
            else if (t.getTokenType()==TokenType.PLUS) Analyser.AddInstruction(new Instruction(Operation.AddF, null));
            else if (t.getTokenType()==TokenType.MINUS) Analyser.AddInstruction(new Instruction(Operation.SubF, null));
            else if (t.getTokenType()==TokenType.GT) {
                Analyser.AddInstruction(new Instruction(Operation.CmpF, null));
                Analyser.AddInstruction(new Instruction(Operation.SetGt, null));
            }
            else if (t.getTokenType()==TokenType.LT) {
                Analyser.AddInstruction(new Instruction(Operation.CmpF, null));
                Analyser.AddInstruction(new Instruction(Operation.SetLt, null));
            }
            else if (t.getTokenType()==TokenType.GE) {
                Analyser.AddInstruction(new Instruction(Operation.CmpF, null));
                Analyser.AddInstruction(new Instruction(Operation.SetLt, null));
                Analyser.AddInstruction(new Instruction(Operation.Not, null));
            }
            else if (t.getTokenType()==TokenType.LE) {
                Analyser.AddInstruction(new Instruction(Operation.CmpF, null));
                Analyser.AddInstruction(new Instruction(Operation.SetGt, null));
                Analyser.AddInstruction(new Instruction(Operation.Not, null));
            }
            else if (t.getTokenType()==TokenType.EQ) {
                Analyser.AddInstruction(new Instruction(Operation.CmpF, null));
                Analyser.AddInstruction(new Instruction(Operation.Not, null));
            }
            else if (t.getTokenType()==TokenType.NEQ) {
                Analyser.AddInstruction(new Instruction(Operation.CmpF, null));
            }
            else throw new AnalyzeError(ErrorCode.InvalidInput, t.getStartPos());
        }


    }

    //分析函数
    public Type AnalyseOperatorAsExpr () throws CompileError {
        //要避免左递归
        Type LType = AnalyseNotOperatorAsExpr();//已经在栈上留了一个数据
        TypeStack.push(LType);

        while (isBinaryOrAs(analyser.peek())) {
            // 因为as的优先级比任何双目运算符都要高，所以遇见as输出就完事了
            if (analyser.peek().getTokenType()==TokenType.AS_KW) {
                analyser.next();
                Token peek = analyser.peek();
                if (peek.getTokenType()==TokenType.IDENT) {
                    analyser.next();
                    if (peek.getValue().equals("int")) {
                        if (LType == Type.Double) Analyser.AddInstruction(new Instruction(Operation.FtoI, null));
                        else if (LType == Type.Void) throw new AnalyzeError(ErrorCode.UseVoid, analyser.peek().getStartPos());
                        TypeStack.pop();
                        TypeStack.push(Type.Int);
                        LType = Type.Int;

                    } else if (peek.getValue().equals("double")) {
                        if (LType == Type.Int) Analyser.AddInstruction(new Instruction(Operation.ItoF, null));
                        else if (LType == Type.Void) throw new AnalyzeError(ErrorCode.UseVoid, analyser.peek().getStartPos());
                        TypeStack.pop();
                        TypeStack.push(Type.Double);
                        LType = Type.Double;

                    } else throw new AnalyzeError(ErrorCode.InvalidInput, analyser.peek().getStartPos());
                } else throw new AnalyzeError(ErrorCode.InvalidInput, analyser.peek().getStartPos());
                continue;
            }

            //不是as，是双目运算符
            if (OperatorStack.isEmpty()) {
                Token t = analyser.next();
                LType = AnalyseNotOperatorAsExpr();
                move(t, LType);
            } else {
                //栈非空
                if (precedence.get(OperatorStack.peek().getTokenType()) >= precedence.get(analyser.peek().getTokenType())) {
                    //进行规约，跳转到下一个循环
                    parse();
                } else {
                    //移进
                    Token t = analyser.next();
                    LType = AnalyseNotOperatorAsExpr();
                    move(t, LType);
                }
            }
        }

        while (!OperatorStack.isEmpty()) parse();

        return LType;
    }

    //判断是否为双目运算符或者as
    private boolean isBinaryOrAs (Token t) {
        TokenType type = t.getTokenType();
        if (type==TokenType.MUL || type==TokenType.DIV || type==TokenType.PLUS || type==TokenType.MINUS || type==TokenType.GT || type==TokenType.LT
                || type==TokenType.GE || type==TokenType.LE || type==TokenType.EQ || type==TokenType.NEQ) return true;
        if (type==TokenType.AS_KW) return true;
        return false;
    }

}
