// generated with ast extension for cup
// version 0.8
// 4/1/2023 18:32:30


package rs.ac.bg.etf.pp1.ast;

public class FormPars implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private String I2;
    private EmptySqBrackets EmptySqBrackets;
    private ParamSqList ParamSqList;

    public FormPars (Type Type, String I2, EmptySqBrackets EmptySqBrackets, ParamSqList ParamSqList) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.I2=I2;
        this.EmptySqBrackets=EmptySqBrackets;
        if(EmptySqBrackets!=null) EmptySqBrackets.setParent(this);
        this.ParamSqList=ParamSqList;
        if(ParamSqList!=null) ParamSqList.setParent(this);
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

    public ParamSqList getParamSqList() {
        return ParamSqList;
    }

    public void setParamSqList(ParamSqList ParamSqList) {
        this.ParamSqList=ParamSqList;
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
        if(ParamSqList!=null) ParamSqList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(EmptySqBrackets!=null) EmptySqBrackets.traverseTopDown(visitor);
        if(ParamSqList!=null) ParamSqList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(EmptySqBrackets!=null) EmptySqBrackets.traverseBottomUp(visitor);
        if(ParamSqList!=null) ParamSqList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("FormPars(\n");

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

        if(ParamSqList!=null)
            buffer.append(ParamSqList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [FormPars]");
        return buffer.toString();
    }
}
