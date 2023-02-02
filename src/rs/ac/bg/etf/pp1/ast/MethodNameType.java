// generated with ast extension for cup
// version 0.8
// 2/1/2023 16:38:13


package rs.ac.bg.etf.pp1.ast;

public class MethodNameType extends MethodName {

    private Type Type;
    private String I2;
    private OptionalFormPars OptionalFormPars;

    public MethodNameType (Type Type, String I2, OptionalFormPars OptionalFormPars) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.I2=I2;
        this.OptionalFormPars=OptionalFormPars;
        if(OptionalFormPars!=null) OptionalFormPars.setParent(this);
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

    public OptionalFormPars getOptionalFormPars() {
        return OptionalFormPars;
    }

    public void setOptionalFormPars(OptionalFormPars OptionalFormPars) {
        this.OptionalFormPars=OptionalFormPars;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Type!=null) Type.accept(visitor);
        if(OptionalFormPars!=null) OptionalFormPars.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(OptionalFormPars!=null) OptionalFormPars.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(OptionalFormPars!=null) OptionalFormPars.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodNameType(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I2);
        buffer.append("\n");

        if(OptionalFormPars!=null)
            buffer.append(OptionalFormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodNameType]");
        return buffer.toString();
    }
}
