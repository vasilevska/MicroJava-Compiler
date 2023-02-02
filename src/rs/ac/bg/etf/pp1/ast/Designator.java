// generated with ast extension for cup
// version 0.8
// 1/1/2023 22:40:19


package rs.ac.bg.etf.pp1.ast;

public class Designator implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private String I1;
    private OptionalDesignList OptionalDesignList;

    public Designator (String I1, OptionalDesignList OptionalDesignList) {
        this.I1=I1;
        this.OptionalDesignList=OptionalDesignList;
        if(OptionalDesignList!=null) OptionalDesignList.setParent(this);
    }

    public String getI1() {
        return I1;
    }

    public void setI1(String I1) {
        this.I1=I1;
    }

    public OptionalDesignList getOptionalDesignList() {
        return OptionalDesignList;
    }

    public void setOptionalDesignList(OptionalDesignList OptionalDesignList) {
        this.OptionalDesignList=OptionalDesignList;
    }

    public SyntaxNode getParent() {
        return parent;
    }

    public void setParent(SyntaxNode parent) {
        this.parent=parent;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line=line;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OptionalDesignList!=null) OptionalDesignList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptionalDesignList!=null) OptionalDesignList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptionalDesignList!=null) OptionalDesignList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("Designator(\n");

        buffer.append(" "+tab+I1);
        buffer.append("\n");

        if(OptionalDesignList!=null)
            buffer.append(OptionalDesignList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [Designator]");
        return buffer.toString();
    }
}
