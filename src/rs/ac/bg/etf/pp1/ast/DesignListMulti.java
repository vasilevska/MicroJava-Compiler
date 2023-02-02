// generated with ast extension for cup
// version 0.8
// 2/1/2023 22:9:53


package rs.ac.bg.etf.pp1.ast;

public class DesignListMulti extends DesignList {

    private DesignList DesignList;
    private OptionalDesign OptionalDesign;

    public DesignListMulti (DesignList DesignList, OptionalDesign OptionalDesign) {
        this.DesignList=DesignList;
        if(DesignList!=null) DesignList.setParent(this);
        this.OptionalDesign=OptionalDesign;
        if(OptionalDesign!=null) OptionalDesign.setParent(this);
    }

    public DesignList getDesignList() {
        return DesignList;
    }

    public void setDesignList(DesignList DesignList) {
        this.DesignList=DesignList;
    }

    public OptionalDesign getOptionalDesign() {
        return OptionalDesign;
    }

    public void setOptionalDesign(OptionalDesign OptionalDesign) {
        this.OptionalDesign=OptionalDesign;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(DesignList!=null) DesignList.accept(visitor);
        if(OptionalDesign!=null) OptionalDesign.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(DesignList!=null) DesignList.traverseTopDown(visitor);
        if(OptionalDesign!=null) OptionalDesign.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(DesignList!=null) DesignList.traverseBottomUp(visitor);
        if(OptionalDesign!=null) OptionalDesign.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignListMulti(\n");

        if(DesignList!=null)
            buffer.append(DesignList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(OptionalDesign!=null)
            buffer.append(OptionalDesign.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignListMulti]");
        return buffer.toString();
    }
}
