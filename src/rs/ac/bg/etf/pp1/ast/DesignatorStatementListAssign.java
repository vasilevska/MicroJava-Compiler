// generated with ast extension for cup
// version 0.8
// 6/1/2023 23:49:18


package rs.ac.bg.etf.pp1.ast;

public class DesignatorStatementListAssign extends DesignatorStatement {

    private OptionalDesign OptionalDesign;
    private DesignList DesignList;
    private AssignDesign AssignDesign;

    public DesignatorStatementListAssign (OptionalDesign OptionalDesign, DesignList DesignList, AssignDesign AssignDesign) {
        this.OptionalDesign=OptionalDesign;
        if(OptionalDesign!=null) OptionalDesign.setParent(this);
        this.DesignList=DesignList;
        if(DesignList!=null) DesignList.setParent(this);
        this.AssignDesign=AssignDesign;
        if(AssignDesign!=null) AssignDesign.setParent(this);
    }

    public OptionalDesign getOptionalDesign() {
        return OptionalDesign;
    }

    public void setOptionalDesign(OptionalDesign OptionalDesign) {
        this.OptionalDesign=OptionalDesign;
    }

    public DesignList getDesignList() {
        return DesignList;
    }

    public void setDesignList(DesignList DesignList) {
        this.DesignList=DesignList;
    }

    public AssignDesign getAssignDesign() {
        return AssignDesign;
    }

    public void setAssignDesign(AssignDesign AssignDesign) {
        this.AssignDesign=AssignDesign;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OptionalDesign!=null) OptionalDesign.accept(visitor);
        if(DesignList!=null) DesignList.accept(visitor);
        if(AssignDesign!=null) AssignDesign.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptionalDesign!=null) OptionalDesign.traverseTopDown(visitor);
        if(DesignList!=null) DesignList.traverseTopDown(visitor);
        if(AssignDesign!=null) AssignDesign.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptionalDesign!=null) OptionalDesign.traverseBottomUp(visitor);
        if(DesignList!=null) DesignList.traverseBottomUp(visitor);
        if(AssignDesign!=null) AssignDesign.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorStatementListAssign(\n");

        if(OptionalDesign!=null)
            buffer.append(OptionalDesign.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(DesignList!=null)
            buffer.append(DesignList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AssignDesign!=null)
            buffer.append(AssignDesign.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorStatementListAssign]");
        return buffer.toString();
    }
}
