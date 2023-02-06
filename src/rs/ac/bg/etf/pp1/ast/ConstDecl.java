// generated with ast extension for cup
// version 0.8
// 6/1/2023 23:49:18


package rs.ac.bg.etf.pp1.ast;

public class ConstDecl implements SyntaxNode {

    private SyntaxNode parent;
    private int line;
    private Type Type;
    private AssignConst AssignConst;
    private RepeatConst RepeatConst;

    public ConstDecl (Type Type, AssignConst AssignConst, RepeatConst RepeatConst) {
        this.Type=Type;
        if(Type!=null) Type.setParent(this);
        this.AssignConst=AssignConst;
        if(AssignConst!=null) AssignConst.setParent(this);
        this.RepeatConst=RepeatConst;
        if(RepeatConst!=null) RepeatConst.setParent(this);
    }

    public Type getType() {
        return Type;
    }

    public void setType(Type Type) {
        this.Type=Type;
    }

    public AssignConst getAssignConst() {
        return AssignConst;
    }

    public void setAssignConst(AssignConst AssignConst) {
        this.AssignConst=AssignConst;
    }

    public RepeatConst getRepeatConst() {
        return RepeatConst;
    }

    public void setRepeatConst(RepeatConst RepeatConst) {
        this.RepeatConst=RepeatConst;
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
        if(AssignConst!=null) AssignConst.accept(visitor);
        if(RepeatConst!=null) RepeatConst.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Type!=null) Type.traverseTopDown(visitor);
        if(AssignConst!=null) AssignConst.traverseTopDown(visitor);
        if(RepeatConst!=null) RepeatConst.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Type!=null) Type.traverseBottomUp(visitor);
        if(AssignConst!=null) AssignConst.traverseBottomUp(visitor);
        if(RepeatConst!=null) RepeatConst.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ConstDecl(\n");

        if(Type!=null)
            buffer.append(Type.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AssignConst!=null)
            buffer.append(AssignConst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(RepeatConst!=null)
            buffer.append(RepeatConst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ConstDecl]");
        return buffer.toString();
    }
}
