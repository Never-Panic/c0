package miniplc0java.Expr;

import miniplc0java.analyser.Analyser;
import miniplc0java.error.AnalyzeError;
import miniplc0java.error.CompileError;
import miniplc0java.error.ErrorCode;
import miniplc0java.tokenizer.Token;
import miniplc0java.tokenizer.TokenType;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class OperatorAsExpr extends Expr{

    Stack<Token> stack = new Stack<>();

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
    private void move (Token t) {
        stack.push(t);
    }

    //规约函数，总是规约栈顶的符号，并输出对应的机器码
    //由于先前的expr已经在栈上留了一个数据，所以当我们规约的时候，直接输出其机器码就可以了
    private void parse () throws AnalyzeError {
        Token t = stack.pop();

        // TODO  将print函数转换为添加命令
        if (t.getTokenType()==TokenType.MUL) System.out.println("MUL");
        else if (t.getTokenType()==TokenType.DIV) System.out.println("DIV");
        else if (t.getTokenType()==TokenType.PLUS) System.out.println("ADD");
        else if (t.getTokenType()==TokenType.MINUS) System.out.println("SUB");
        else if (t.getTokenType()==TokenType.GT) System.out.println("GT");
        else if (t.getTokenType()==TokenType.LT) System.out.println("LT");
        else if (t.getTokenType()==TokenType.GE) System.out.println("GE");
        else if (t.getTokenType()==TokenType.LE) System.out.println("LE");
        else if (t.getTokenType()==TokenType.EQ) System.out.println("EQ");
        else if (t.getTokenType()==TokenType.NEQ) System.out.println("NEQ");
        else throw new AnalyzeError(ErrorCode.InvalidInput, t.getStartPos());

    }

    //分析函数
    public void AnalyseOperatorAsExpr () throws CompileError {
        //要避免左递归
        AnalyseNotOperatorAsExpr();//已经在栈上留了一个数据

        while (isBinaryOrAs(analyser.peek())) {
            // 因为as的优先级比任何双目运算符都要高，所以遇见as输出就完事了
            // TODO 判断类型转换是否符合语义，并且浮点数的加减指令和整数不一样，之后再考虑吧
            if (analyser.peek().getTokenType()==TokenType.AS_KW) {
                analyser.next();
                Token peek = analyser.peek();
                if (peek.getTokenType()==TokenType.IDENT) {
                    if (peek.getValue().equals("int")) {
                        analyser.next();
                        System.out.println("cast to int");

                    } else if (peek.getValue().equals("double")) {
                        analyser.next();
                        System.out.println("cast to double");

                    } else {
                        System.out.println(peek.getValue());
                        throw new AnalyzeError(ErrorCode.InvalidInput, analyser.peek().getStartPos());
                    }

                } else {
                    throw new AnalyzeError(ErrorCode.InvalidInput, analyser.peek().getStartPos());
                }
                continue;
            }

            //不是as，是双目运算符
            if (stack.isEmpty()) {
                move(analyser.next());
                AnalyseNotOperatorAsExpr();
            } else {
                //栈非空
                if (precedence.get(stack.peek().getTokenType()) >= precedence.get(analyser.peek().getTokenType())) {
                    //进行规约，跳转到下一个循环
                    parse();
                } else {
                    //移进
                    move(analyser.next());
                    AnalyseNotOperatorAsExpr();
                }
            }
        }

        while (!stack.isEmpty()) parse();

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
