// generated with ast extension for cup
// version 0.8
// 2/1/2023 23:59:19


package rs.ac.bg.etf.pp1.ast;

public class NoOptDesignList extends OptionalDesignList {

    public NoOptDesignList () {
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
        buffer.append("NoOptDesignList(\n");

        buffer.append(tab);
        buffer.append(") [NoOptDesignList]");
        return buffer.toString();
    }
}
