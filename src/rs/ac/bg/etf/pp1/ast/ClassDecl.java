// generated with ast extension for cup
// version 0.8
// 5/1/2023 23:14:28


package rs.ac.bg.etf.pp1.ast;

public class ClassDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private ClassName ClassName;
    private ExtendClass ExtendClass;
    private VarDeclList VarDeclList;
    private OptionalClassBody OptionalClassBody;

    public ClassDecl (ClassName ClassName, ExtendClass ExtendClass, VarDeclList VarDeclList, OptionalClassBody OptionalClassBody) {
        this.ClassName=ClassName;
        if(ClassName!=null) ClassName.setParent(this);
        this.ExtendClass=ExtendClass;
        if(ExtendClass!=null) ExtendClass.setParent(this);
        this.VarDeclList=VarDeclList;
        if(VarDeclList!=null) VarDeclList.setParent(this);
        this.OptionalClassBody=OptionalClassBody;
        if(OptionalClassBody!=null) OptionalClassBody.setParent(this);
    }

    public ClassName getClassName() {
        return ClassName;
    }

    public void setClassName(ClassName ClassName) {
        this.ClassName=ClassName;
    }

    public ExtendClass getExtendClass() {
        return ExtendClass;
    }

    public void setExtendClass(ExtendClass ExtendClass) {
        this.ExtendClass=ExtendClass;
    }

    public VarDeclList getVarDeclList() {
        return VarDeclList;
    }

    public void setVarDeclList(VarDeclList VarDeclList) {
        this.VarDeclList=VarDeclList;
    }

    public OptionalClassBody getOptionalClassBody() {
        return OptionalClassBody;
    }

    public void setOptionalClassBody(OptionalClassBody OptionalClassBody) {
        this.OptionalClassBody=OptionalClassBody;
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
        if(ClassName!=null) ClassName.accept(visitor);
        if(ExtendClass!=null) ExtendClass.accept(visitor);
        if(VarDeclList!=null) VarDeclList.accept(visitor);
        if(OptionalClassBody!=null) OptionalClassBody.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ClassName!=null) ClassName.traverseTopDown(visitor);
        if(ExtendClass!=null) ExtendClass.traverseTopDown(visitor);
        if(VarDeclList!=null) VarDeclList.traverseTopDown(visitor);
        if(OptionalClassBody!=null) OptionalClassBody.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ClassName!=null) ClassName.traverseBottomUp(visitor);
        if(ExtendClass!=null) ExtendClass.traverseBottomUp(visitor);
        if(VarDeclList!=null) VarDeclList.traverseBottomUp(visitor);
        if(OptionalClassBody!=null) OptionalClassBody.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ClassDecl(\n");

        if(ClassName!=null)
            buffer.append(ClassName.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(ExtendClass!=null)
            buffer.append(ExtendClass.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(VarDeclList!=null)
            buffer.append(VarDeclList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalClassBody!=null)
            buffer.append(OptionalClassBody.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ClassDecl]");
        return buffer.toString();
    }
}
