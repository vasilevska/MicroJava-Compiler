// generated with ast extension for cup
// version 0.8
// 6/1/2023 20:16:11


package rs.ac.bg.etf.pp1.ast;

public class ConstructorDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    public rs.etf.pp1.symboltable.concepts.Obj obj = null;

    private ConstructorName ConstructorName;
    private OptionalFormPars OptionalFormPars;
    private FunctionBody FunctionBody;

    public ConstructorDecl (ConstructorName ConstructorName, OptionalFormPars OptionalFormPars, FunctionBody FunctionBody) {
        this.ConstructorName=ConstructorName;
        if(ConstructorName!=null) ConstructorName.setParent(this);
        this.OptionalFormPars=OptionalFormPars;
        if(OptionalFormPars!=null) OptionalFormPars.setParent(this);
        this.FunctionBody=FunctionBody;
        if(FunctionBody!=null) FunctionBody.setParent(this);
    }

    public ConstructorName getConstructorName() {
        return ConstructorName;
    }

    public void setConstructorName(ConstructorName ConstructorName) {
        this.ConstructorName=ConstructorName;
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
        if(ConstructorName!=null) ConstructorName.accept(visitor);
        if(OptionalFormPars!=null) OptionalFormPars.accept(visitor);
        if(FunctionBody!=null) FunctionBody.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstructorName!=null) ConstructorName.traverseTopDown(visitor);
        if(OptionalFormPars!=null) OptionalFormPars.traverseTopDown(visitor);
        if(FunctionBody!=null) FunctionBody.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstructorName!=null) ConstructorName.traverseBottomUp(visitor);
        if(OptionalFormPars!=null) OptionalFormPars.traverseBottomUp(visitor);
        if(FunctionBody!=null) FunctionBody.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstructorDecl(\n");

        if(ConstructorName!=null)
            buffer.append(ConstructorName.toString("  "+tab));
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
        buffer.append(") [ConstructorDecl]");
        return buffer.toString();
    }
}
