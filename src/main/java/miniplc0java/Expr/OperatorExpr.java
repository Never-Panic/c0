package miniplc0java.Expr;

import miniplc0java.analyser.Analyser;
import miniplc0java.error.AnalyzeError;
import miniplc0java.error.ErrorCode;
import miniplc0java.error.TokenizeError;
import miniplc0java.tokenizer.Token;
import miniplc0java.tokenizer.TokenType;

import java.awt.image.TileObserver;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class OperatorExpr extends Expr{


    String type = "Operator Expr";
    Stack<Token> stack = new Stack<>();

    //唯一的分析器
    Analyser analyser;
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

    public OperatorExpr(Analyser analyser) {
        this.analyser = analyser;
    }

    //移进函数
    private void move (Token t) {
        stack.push(t);
    }

    //规约函数，总是规约栈顶的符号，并输出对应的机器码
    //由于先前的expr已经在栈上留了一个数据，所以当我们规约的时候，直接输出其机器码就可以了
    private void parse () throws AnalyzeError {
        Token t = stack.pop();

        if (t.getTokenType()==TokenType.MUL) System.out.println("MUL");
        else if (t.getTokenType()==TokenType.DIV) System.out.println("DIV");
        else if (t.getTokenType()==TokenType.PLUS) System.out.println("PLUS");
        else if (t.getTokenType()==TokenType.MINUS) System.out.println("MINUS");
        else if (t.getTokenType()==TokenType.GT) System.out.println("GT");
        else if (t.getTokenType()==TokenType.LT) System.out.println("LT");
        else if (t.getTokenType()==TokenType.GE) System.out.println("GE");
        else if (t.getTokenType()==TokenType.LE) System.out.println("LE");
        else if (t.getTokenType()==TokenType.EQ) System.out.println("EQ");
        else if (t.getTokenType()==TokenType.NEQ) System.out.println("NEQ");
        else throw new AnalyzeError(ErrorCode.InvalidInput, t.getStartPos());

    }

    //分析函数
    public void AnalyseOperatorExpr () throws TokenizeError, AnalyzeError {
        //要避免左递归
        Expr expr = new Expr();
        expr.AnalyseNotOperatorExpr();//已经在栈上留了一个数据

        //至少要有一个双目运算符
        if (!isBinary(analyser.peek())) throw new AnalyzeError(ErrorCode.InvalidInput, analyser.peek().getStartPos());
        //第一个符号入栈
        move(analyser.next());

        expr.AnalyseNotOperatorExpr();

        while (isBinary(analyser.peek())) {
            if (stack.isEmpty()) {
                move(analyser.next());
            } else {
                //栈非空
                if (precedence.get(stack.peek().getTokenType()) > precedence.get(analyser.peek().getTokenType())) {
                    //进行规约，跳转到下一个循环
                    parse();
                    continue;
                } else {
                    //移进
                    move(analyser.next());
                    expr.AnalyseNotOperatorExpr();
                }
            }
        }

        while (!stack.isEmpty()) parse();

    }

    //判断是否为双目运算符
    private boolean isBinary (Token t) {
        TokenType type = t.getTokenType();
        if (type==TokenType.MUL || type==TokenType.DIV || type==TokenType.PLUS || type==TokenType.MINUS || type==TokenType.GT || type==TokenType.LT
                || type==TokenType.GE || type==TokenType.LE || type==TokenType.EQ || type==TokenType.NEQ) return true;
        return false;
    }

}
