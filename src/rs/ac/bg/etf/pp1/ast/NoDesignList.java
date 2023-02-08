// generated with ast extension for cup
// version 0.8
// 7/1/2023 23:48:58


package rs.ac.bg.etf.pp1.ast;

public class NoDesignList extends DesignList {

    public NoDesignList () {
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
        buffer.append("NoDesignList(\n");

        buffer.append(tab);
        buffer.append(") [NoDesignList]");
        return buffer.toString();
    }
}
