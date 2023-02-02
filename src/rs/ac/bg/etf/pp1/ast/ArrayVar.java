// generated with ast extension for cup
// version 0.8
// 2/1/2023 23:59:19


package rs.ac.bg.etf.pp1.ast;

public class ArrayVar extends IdentSqList {

    private IdentSqList IdentSqList;
    private String I2;
    private EmptySqBrackets EmptySqBrackets;

    public ArrayVar (IdentSqList IdentSqList, String I2, EmptySqBrackets EmptySqBrackets) {
        this.IdentSqList=IdentSqList;
        if(IdentSqList!=null) IdentSqList.setParent(this);
        this.I2=I2;
        this.EmptySqBrackets=EmptySqBrackets;
        if(EmptySqBrackets!=null) EmptySqBrackets.setParent(this);
    }

    public IdentSqList getIdentSqList() {
        return IdentSqList;
    }

    public void setIdentSqList(IdentSqList IdentSqList) {
        this.IdentSqList=IdentSqList;
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

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(IdentSqList!=null) IdentSqList.accept(visitor);
        if(EmptySqBrackets!=null) EmptySqBrackets.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(IdentSqList!=null) IdentSqList.traverseTopDown(visitor);
        if(EmptySqBrackets!=null) EmptySqBrackets.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(IdentSqList!=null) IdentSqList.traverseBottomUp(visitor);
        if(EmptySqBrackets!=null) EmptySqBrackets.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ArrayVar(\n");

        if(IdentSqList!=null)
            buffer.append(IdentSqList.toString("  "+tab));
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

        buffer.append(tab);
        buffer.append(") [ArrayVar]");
        return buffer.toString();
    }
}
