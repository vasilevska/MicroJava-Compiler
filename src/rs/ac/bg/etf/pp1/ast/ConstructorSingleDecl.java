// generated with ast extension for cup
// version 0.8
// 5/1/2023 23:14:28


package rs.ac.bg.etf.pp1.ast;

public class ConstructorSingleDecl extends ConstructorDeclList {

    private ConstructorDecl ConstructorDecl;

    public ConstructorSingleDecl (ConstructorDecl ConstructorDecl) {
        this.ConstructorDecl=ConstructorDecl;
        if(ConstructorDecl!=null) ConstructorDecl.setParent(this);
    }

    public ConstructorDecl getConstructorDecl() {
        return ConstructorDecl;
    }

    public void setConstructorDecl(ConstructorDecl ConstructorDecl) {
        this.ConstructorDecl=ConstructorDecl;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(ConstructorDecl!=null) ConstructorDecl.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ConstructorDecl!=null) ConstructorDecl.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ConstructorDecl!=null) ConstructorDecl.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstructorSingleDecl(\n");

        if(ConstructorDecl!=null)
            buffer.append(ConstructorDecl.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstructorSingleDecl]");
        return buffer.toString();
    }
}
