// generated with ast extension for cup
// version 0.8
// 5/1/2023 23:14:28


package rs.ac.bg.etf.pp1.ast;

public class MethodDeclDerived1 extends MethodDecl {

    private MethodName MethodName;
    private OptionalFormPars OptionalFormPars;
    private FunctionBody FunctionBody;

    public MethodDeclDerived1 (MethodName MethodName, OptionalFormPars OptionalFormPars, FunctionBody FunctionBody) {
        this.MethodName=MethodName;
        if(MethodName!=null) MethodName.setParent(this);
        this.OptionalFormPars=OptionalFormPars;
        if(OptionalFormPars!=null) OptionalFormPars.setParent(this);
        this.FunctionBody=FunctionBody;
        if(FunctionBody!=null) FunctionBody.setParent(this);
    }

    public MethodName getMethodName() {
        return MethodName;
    }

    public void setMethodName(MethodName MethodName) {
        this.MethodName=MethodName;
    }

    public OptionalFormPars getOptionalFormPars() {
        return OptionalFormPars;
    }

    public void setOptionalFormPars(OptionalFormPars OptionalFormPars) {
        this.OptionalFormPars=OptionalFormPars;
    }

    public FunctionBody getFunctionBody() {
        return FunctionBody;
    }

    public void setFunctionBody(FunctionBody FunctionBody) {
        this.FunctionBody=FunctionBody;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MethodName!=null) MethodName.accept(visitor);
        if(OptionalFormPars!=null) OptionalFormPars.accept(visitor);
        if(FunctionBody!=null) FunctionBody.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MethodName!=null) MethodName.traverseTopDown(visitor);
        if(OptionalFormPars!=null) OptionalFormPars.traverseTopDown(visitor);
        if(FunctionBody!=null) FunctionBody.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MethodName!=null) MethodName.traverseBottomUp(visitor);
        if(OptionalFormPars!=null) OptionalFormPars.traverseBottomUp(visitor);
        if(FunctionBody!=null) FunctionBody.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MethodDeclDerived1(\n");

        if(MethodName!=null)
            buffer.append(MethodName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalFormPars!=null)
            buffer.append(OptionalFormPars.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(FunctionBody!=null)
            buffer.append(FunctionBody.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MethodDeclDerived1]");
        return buffer.toString();
    }
}
