// generated with ast extension for cup
// version 0.8
// 6/1/2023 21:0:13


package rs.ac.bg.etf.pp1.ast;

public class NoCondTerm extends CondTermList {

    public NoCondTerm () {
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NoCondTerm(\n");

        buffer.append(tab);
        buffer.append(") [NoCondTerm]");
        return buffer.toString();
    }
}
