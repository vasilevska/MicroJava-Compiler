// generated with ast extension for cup
// version 0.8
// 5/1/2023 23:14:28


package rs.ac.bg.etf.pp1.ast;

public class NewRepeatConst extends RepeatConst {

    private RepeatConst RepeatConst;
    private AssignConst AssignConst;

    public NewRepeatConst (RepeatConst RepeatConst, AssignConst AssignConst) {
        this.RepeatConst=RepeatConst;
        if(RepeatConst!=null) RepeatConst.setParent(this);
        this.AssignConst=AssignConst;
        if(AssignConst!=null) AssignConst.setParent(this);
    }

    public RepeatConst getRepeatConst() {
        return RepeatConst;
    }

    public void setRepeatConst(RepeatConst RepeatConst) {
        this.RepeatConst=RepeatConst;
    }

    public AssignConst getAssignConst() {
        return AssignConst;
    }

    public void setAssignConst(AssignConst AssignConst) {
        this.AssignConst=AssignConst;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(RepeatConst!=null) RepeatConst.accept(visitor);
        if(AssignConst!=null) AssignConst.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(RepeatConst!=null) RepeatConst.traverseTopDown(visitor);
        if(AssignConst!=null) AssignConst.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(RepeatConst!=null) RepeatConst.traverseBottomUp(visitor);
        if(AssignConst!=null) AssignConst.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("NewRepeatConst(\n");

        if(RepeatConst!=null)
            buffer.append(RepeatConst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AssignConst!=null)
            buffer.append(AssignConst.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [NewRepeatConst]");
        return buffer.toString();
    }
}
