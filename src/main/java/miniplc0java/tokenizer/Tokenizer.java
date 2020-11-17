package miniplc0java.tokenizer;

import miniplc0java.error.TokenizeError;
import miniplc0java.error.ErrorCode;
import miniplc0java.util.Pos;

public class Tokenizer {

    private StringIter it;

    public Tokenizer(StringIter it) {
        this.it = it;
    }

    // 这里本来是想实现 Iterator<Token> 的，但是 Iterator 不允许抛异常，于是就这样了
    /**
     * 获取下一个 Token
     * 
     * @return
     * @throws TokenizeError 如果解析有异常则抛出
     */
    public Token nextToken() throws TokenizeError {
        it.readAll();

        // 跳过之前的所有空白字符
        skipSpaceCharacters();

        if (it.isEOF()) {
            return new Token(TokenType.EOF, "", it.currentPos(), it.currentPos());
        }

        char peek = it.peekChar();
        if (Character.isDigit(peek)) {
            return lexNum();
        } else if (Character.isAlphabetic(peek) || peek=='_') {
            return lexIdentOrKeyword();
        } else {
            return lexOperatorOrUnknown();
        }
    }

    private Token lexNum() throws TokenizeError {

        StringBuilder cache = new StringBuilder();
        Pos startPos = it.ptr;

        while (Character.isDigit(it.peekChar())) {
            cache.append(it.peekChar());
            it.nextChar();
        }

        if (it.peekChar() == '.') {
            it.nextChar();
            cache.append('.');

            if (!Character.isDigit(it.peekChar())) throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());

            while (Character.isDigit(it.peekChar())) {
                cache.append(it.peekChar());
                it.nextChar();
            }

            if (it.peekChar() == 'E' || it.peekChar() == 'e') {
                it.nextChar();

                if (!Character.isDigit(it.peekChar())) throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());

                StringBuilder powCache = new StringBuilder();
                while (Character.isDigit(it.peekChar())) {
                    powCache.append(it.peekChar());
                    it.nextChar();
                }

                double base = Double.parseDouble(cache.toString());
                int powNum = Integer.parseInt(powCache.toString());
                double result = base * Math.pow(10, powNum);

                return new Token(TokenType.DOUBLE_LITERAL, result, startPos, it.ptr);

            } else return new Token(TokenType.DOUBLE_LITERAL, Double.parseDouble(cache.toString()), startPos, it.ptr);
        } else {
            Pos endPos = it.ptr;
            return new Token(TokenType.UINT_LITERAL, Integer.parseInt(cache.toString()), startPos, endPos);
        }

    }

    private Token lexIdentOrKeyword() throws TokenizeError {

        StringBuilder cache = new StringBuilder();
        Pos startPos = it.ptr;

        if (it.peekChar() == '_') {
            cache.append(it.peekChar());
            it.nextChar();
        }

        while (Character.isLetter(it.peekChar()) || Character.isDigit(it.peekChar())) {
            cache.append(it.peekChar());
            it.nextChar();
        }

        Pos endPos = it.ptr;
        String str = cache.toString();

        if (str.equals("fn")) {
            return new Token(TokenType.FN_KW, str, startPos, endPos);
        } else if (str.equals("let")) {
            return new Token(TokenType.LET_KW, str, startPos, endPos);
        } else if (str.equals("const")) {
            return new Token(TokenType.CONST_KW, str, startPos, endPos);
        } else if (str.equals("as")) {
            return new Token(TokenType.AS_KW, str, startPos, endPos);
        } else if (str.equals("while")) {
            return new Token(TokenType.WHILE_KW, str, startPos, endPos);
        } else if (str.equals("if")) {
            return new Token(TokenType.IF_KW, str, startPos, endPos);
        } else if (str.equals("else")) {
            return new Token(TokenType.ELSE_KW, str, startPos, endPos);
        } else if (str.equals("return")) {
            return new Token(TokenType.RETURN_KW, str, startPos, endPos);
        } else if (str.equals("break")) {
            return new Token(TokenType.BREAK_KW, str, startPos, endPos);
        } else if (str.equals("continue")) {
            return new Token(TokenType.CONTINUE_KW, str, startPos, endPos);
        } else {
            return new Token(TokenType.IDENT, str, startPos, endPos);
        }

    }

    private Token lexOperatorOrUnknown() throws TokenizeError {

        Pos startPos = it.currentPos();
        switch (it.nextChar()) {
            case '+':
                return new Token(TokenType.PLUS, '+', it.previousPos(), it.currentPos());

            case '-':
                if (it.peekChar() == '>') {
                    it.nextChar();
                    return new Token(TokenType.ARROW, "->", startPos, it.currentPos());
                } else return new Token(TokenType.MINUS, '-', it.previousPos(), it.currentPos());

            case '*':
                return new Token(TokenType.MUL, '*', it.previousPos(), it.currentPos());

            case '/':
                return new Token(TokenType.DIV, '/', it.previousPos(), it.currentPos());

            case '=':
                if (it.peekChar() == '=') {
                    it.nextChar();
                    return new Token(TokenType.EQ, "==", startPos, it.currentPos());
                } else return new Token(TokenType.ASSIGN, '=', it.previousPos(), it.currentPos());

            case '!':
                if (it.peekChar() == '=') {
                    it.nextChar();
                    return new Token(TokenType.NEQ, "!=", startPos, it.currentPos());
                } else throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());

            case '<':
                if (it.peekChar() == '=') {
                    it.nextChar();
                    return new Token(TokenType.LE, "<=", startPos, it.currentPos());
                } else return new Token(TokenType.LT, '<', it.previousPos(), it.currentPos());

            case '>':
                if (it.peekChar() == '=') {
                    it.nextChar();
                    return new Token(TokenType.GE, ">=", startPos, it.currentPos());
                } else return new Token(TokenType.GT, '>', it.previousPos(), it.currentPos());

            case '(':
                return new Token(TokenType.L_PAREN, '(', it.previousPos(), it.currentPos());

            case ')':
                return new Token(TokenType.R_PAREN, ')', it.previousPos(), it.currentPos());

            case '{':
                return new Token(TokenType.L_BRACE, '{', it.previousPos(), it.currentPos());

            case '}':
                return new Token(TokenType.R_BRACE, '}', it.previousPos(), it.currentPos());

            case ',':
                return new Token(TokenType.COMMA, ',', it.previousPos(), it.currentPos());

            case ':':
                return new Token(TokenType.COLON, ':', it.previousPos(), it.currentPos());

            case ';':
                return new Token(TokenType.SEMICOLON, ';', it.previousPos(), it.currentPos());

            default:
                // 不认识这个输入，摸了
                throw new TokenizeError(ErrorCode.InvalidInput, it.previousPos());
        }
    }

    private void skipSpaceCharacters() {
        while (!it.isEOF() && Character.isWhitespace(it.peekChar())) {
            it.nextChar();
        }
    }
}
