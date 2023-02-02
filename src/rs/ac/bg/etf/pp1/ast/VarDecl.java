// generated with ast extension for cup
// version 0.8
// 2/1/2023 23:59:19


package rs.ac.bg.etf.pp1.ast;

public class VarDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private String I2;
    private EmptySqBrackets EmptySqBrackets;
    private IdentSqList IdentSqList;

    public VarDecl (Type Type, String I2, EmptySqBrackets EmptySqBrackets, IdentSqList IdentSqList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.I2=I2;
        this.EmptySqBrackets=EmptySqBrackets;
        if(EmptySqBrackets!=null) EmptySqBrackets.setParent(this);
        this.IdentSqList=IdentSqList;
        if(IdentSqList!=null) IdentSqList.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public String getI2() {
        return I2;
    }

    public void setI2(String I2) {
        this.I2=I2;
    }

    public EmptySqBrackets getEmptySqBrackets() {
        return EmptySqBrackets;
    }

    public void setEmptySqBrackets(EmptySqBrackets EmptySqBrackets) {
        this.EmptySqBrackets=EmptySqBrackets;
    }

    public IdentSqList getIdentSqList() {
        return IdentSqList;
    }

    public void setIdentSqList(IdentSqList IdentSqList) {
        this.IdentSqList=IdentSqList;
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
        if(Type!=null) Type.accept(visitor);
        if(EmptySqBrackets!=null) EmptySqBrackets.accept(visitor);
        if(IdentSqList!=null) IdentSqList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(EmptySqBrackets!=null) EmptySqBrackets.traverseTopDown(visitor);
        if(IdentSqList!=null) IdentSqList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(EmptySqBrackets!=null) EmptySqBrackets.traverseBottomUp(visitor);
        if(IdentSqList!=null) IdentSqList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("VarDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I2);
        buffer.append("\n");

        if(EmptySqBrackets!=null)
            buffer.append(EmptySqBrackets.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(IdentSqList!=null)
            buffer.append(IdentSqList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [VarDecl]");
        return buffer.toString();
    }
}
