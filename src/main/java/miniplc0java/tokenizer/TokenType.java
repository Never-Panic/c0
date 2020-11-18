package miniplc0java.tokenizer;

public enum TokenType {


    // 关键字
    FN_KW,
    LET_KW,
    CONST_KW,
    AS_KW,
    WHILE_KW,
    IF_KW,
    ELSE_KW,
    RETURN_KW,
    BREAK_KW,
    CONTINUE_KW,

    // 字面量
    UINT_LITERAL,
    STRING_LITERAL,
    DOUBLE_LITERAL,
    CHAR_LITERAL,

    // 标识符
    IDENT,

    // 运算符
    PLUS,
    MINUS,
    MUL,
    DIV,
    ASSIGN,
    EQ,
    NEQ,
    LT,
    GT,
    LE,
    GE,
    L_PAREN,
    R_PAREN,
    L_BRACE,
    R_BRACE,
    ARROW,
    COMMA,
    COLON,
    SEMICOLON,

    //注释
    COMMENT,

    // miniplc0
    /** 空 */
    None,
    /** 文件尾 */
    EOF;

    @Override
    public String toString() {
        switch (this) {

            // 关键字
            case FN_KW:
                return "fn";
            case LET_KW:
                return "let";
            case CONST_KW:
                return "const";
            case AS_KW:
                return "as";
            case WHILE_KW:
                return "while";
            case IF_KW:
                return "if";
            case ELSE_KW:
                return "else";
            case RETURN_KW:
                return "return";
            case BREAK_KW:
                return "break";
            case CONTINUE_KW:
                return "continue";

            // 字面量
            case UINT_LITERAL:
                return "UnsignedInteger";
            case STRING_LITERAL:
                return "String";
            case DOUBLE_LITERAL:
                return "Double";
            case CHAR_LITERAL:
                return "Char";

            // 标识符
            case IDENT:
                return "Identifier";

            // 运算符
            case PLUS:
                return "+";
            case MINUS:
                return "-";
            case MUL:
                return "*";
            case DIV:
                return "/";
            case ASSIGN:
                return "=";
            case EQ:
                return "==";
            case NEQ:
                return "!=";
            case LT:
                return "<";
            case GT:
                return ">";
            case LE:
                return "<=";
            case GE:
                return ">=";
            case L_PAREN:
                return "(";
            case R_PAREN:
                return ")";
            case L_BRACE:
                return "{";
            case R_BRACE:
                return "}";
            case ARROW:
                return "->";
            case COMMA:
                return ",";
            case COLON:
                return ":";
            case SEMICOLON:
                return ";";

            //注释
            case COMMENT:
                return "Comment";

            //plc0
            case None:
                return "NullToken";
            case EOF:
                return "EOF";
            default:
                return "InvalidToken";
        }
    }
}
