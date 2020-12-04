package miniplc0java.Stmt;

import miniplc0java.analyser.Analyser;
import miniplc0java.error.CompileError;

public class Stmt {
    Analyser analyser;

    public Stmt (Analyser analyser) {
        this.analyser = analyser;
    }

    public void AnalyseStmt () throws CompileError {
        DeclStmt declStmt = new DeclStmt(analyser);
        declStmt.AnalyseDeclStmt();
    }
}
