package miniplc0java.instruction;

public enum Operation {
    Push(0x01),
    PopN(0x03),
    LocA(0x0a),
    ArgA(0x0b),
    GlobA(0x0c),
    Load64(0x13),
    Store64(0x17),
    Stackalloc(0x1a),
    AddI(0x20),
    SubI(0x21),
    MulI(0x22),
    DivI(0x23),
    AddF(0x24),
    SubF(0x25),
    MulF(0x26),
    DivF(0x27),
    Not(0x2e),
    CmpI(0x30),
    CmpF(0x32),
    NegI(0x34),
    NegF(0x35),
    ItoF(0x36),
    FtoI(0x37),
    SetGt(0x3a),
    SetLt(0x39),
    Br(0x41),
    Brtrue(0x43),
    Call(0x48),
    Ret(0x49),
    ScanI(0x50),
    ScanF(0x52),
    ScanC(0x51),
    PrintI(0x54),
    PrintF(0x56),
    PrintC(0x55),
    PrintS(0x57),
    Println(0x58);


    private Integer num;

    Operation(Integer num) {
        this.num = num;
    }

    public Integer getNum() {
        return num;
    }
}
