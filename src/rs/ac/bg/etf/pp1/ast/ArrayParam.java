// generated with ast extension for cup
// version 0.8
// 4/1/2023 18:32:30


package rs.ac.bg.etf.pp1.ast;

public class ArrayParam extends ParamSqList {

    private ParamSqList ParamSqList;
    private Type Type;
    private String I3;
    private EmptySqBrackets EmptySqBrackets;

    public ArrayParam (ParamSqList ParamSqList, Type Type, String I3, EmptySqBrackets EmptySqBrackets) {
        this.ParamSqList=ParamSqList;
        if(ParamSqList!=null) ParamSqList.setParent(this);
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.I3=I3;
        this.EmptySqBrackets=EmptySqBrackets;
        if(EmptySqBrackets!=null) EmptySqBrackets.setParent(this);
    }

    public ParamSqList getParamSqList() {
        return ParamSqList;
    }

    public void setParamSqList(ParamSqList ParamSqList) {
        this.ParamSqList=ParamSqList;
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public String getI3() {
        return I3;
    }

    public void setI3(String I3) {
        this.I3=I3;
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
        if(ParamSqList!=null) ParamSqList.accept(visitor);
        if(Type!=null) Type.accept(visitor);
        if(EmptySqBrackets!=null) EmptySqBrackets.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ParamSqList!=null) ParamSqList.traverseTopDown(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(EmptySqBrackets!=null) EmptySqBrackets.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ParamSqList!=null) ParamSqList.traverseBottomUp(visitor);
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(EmptySqBrackets!=null) EmptySqBrackets.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ArrayParam(\n");

        if(ParamSqList!=null)
            buffer.append(ParamSqList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I3);
        buffer.append("\n");

        if(EmptySqBrackets!=null)
            buffer.append(EmptySqBrackets.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ArrayParam]");
        return buffer.toString();
    }
}
