package miniplc0java.Stmt;

public class Jump {
    private int JumpNum;

    public Jump(int jumpNum) {
        JumpNum = jumpNum;
    }

    public int getJumpNum() {
        return JumpNum;
    }

    public void setJumpNum(int jumpNum) {
        JumpNum = jumpNum;
    }

    @Override
    public String toString() {
        return Integer.toString(JumpNum);
    }
}
