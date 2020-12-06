package miniplc0java.analyser;

import miniplc0java.Program.Program;
import miniplc0java.SymbolTable.SymbolTable;
import miniplc0java.error.CompileError;
import miniplc0java.error.ExpectedTokenError;
import miniplc0java.error.TokenizeError;
import miniplc0java.instruction.Instruction;
import miniplc0java.tokenizer.Token;
import miniplc0java.tokenizer.TokenType;
import miniplc0java.tokenizer.Tokenizer;

import java.util.*;


//gradle fatjar
//java -jar ./build/libs/miniplc0java.jar -l -o output.txt input.txt

public final class Analyser {

    Tokenizer tokenizer;
    //只加不减，用来计算跳转指令的相对值
    static public ArrayList<Instruction> instructions = new ArrayList<>();

    /** 当前偷看的 token */
    Token peekedToken = null;

    /** 符号表 */
    SymbolTable symbolTable = SymbolTable.getInstance();

    /** 下一个变量的栈偏移 */
    int nextOffset = 0;

    /** 添加一条指令 **/
    public static void AddInstruction (Instruction instruction) {
        instructions.add(instruction);
    }

    public Analyser(Tokenizer tokenizer) {
        this.tokenizer = tokenizer;
    }

    public List<Instruction> analyse() throws CompileError {

        Program program = new Program(this);
        program.AnalyseProgram();

        return instructions;
    }


    /**
     * 查看下一个 Token
     * next() 和 peek() 会返回同一个东西
     * @return
     * @throws TokenizeError
     */
    public Token peek() throws TokenizeError {
        if (peekedToken == null) {
            peekedToken = tokenizer.nextToken();
        }
        return peekedToken;
    }

    /**
     * 获取下一个 Token
     * 
     * @return
     * @throws TokenizeError
     */
    public Token next() throws TokenizeError {
        if (peekedToken != null) {
            var token = peekedToken;
            peekedToken = null;
            return token;
        } else {
            return tokenizer.nextToken();
        }
    }

    /**
     * 如果下一个 token 的类型是 tt，则返回 true
     * 
     * @param tt
     * @return
     * @throws TokenizeError
     */
    public boolean check(TokenType tt) throws TokenizeError {
        var token = peek();
        return token.getTokenType() == tt;
    }

    /**
     * 如果下一个 token 的类型是 tt，则前进一个 token 并返回这个 token
     * 
     * @param tt 类型
     * @return 如果匹配则返回这个 token，否则返回 null
     * @throws TokenizeError
     */
    public Token nextIf(TokenType tt) throws TokenizeError {
        var token = peek();
        if (token.getTokenType() == tt) {
            return next();
        } else {
            return null;
        }
    }

    /**
     * 如果下一个 token 的类型是 tt，则前进一个 token 并返回，否则抛出异常
     *
     * 返回和next() peek()一样
     *
     * @param tt 类型
     * @return 这个 token
     * @throws CompileError 如果类型不匹配
     */
    public Token expect(TokenType tt) throws CompileError {
        var token = peek();
        if (token.getTokenType() == tt) {
            return next();
        } else {
            throw new ExpectedTokenError(tt, token);
        }
    }


}
