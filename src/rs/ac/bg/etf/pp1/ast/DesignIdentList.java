// generated with ast extension for cup
// version 0.8
// 2/1/2023 22:9:53


package rs.ac.bg.etf.pp1.ast;

public class DesignIdentList extends OptionalDesignList {

    private OptionalDesignList OptionalDesignList;
    private String I2;

    public DesignIdentList (OptionalDesignList OptionalDesignList, String I2) {
        this.OptionalDesignList=OptionalDesignList;
        if(OptionalDesignList!=null) OptionalDesignList.setParent(this);
        this.I2=I2;
    }

    public OptionalDesignList getOptionalDesignList() {
        return OptionalDesignList;
    }

    public void setOptionalDesignList(OptionalDesignList OptionalDesignList) {
        this.OptionalDesignList=OptionalDesignList;
    }

    public String getI2() {
        return I2;
    }

    public void setI2(String I2) {
        this.I2=I2;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OptionalDesignList!=null) OptionalDesignList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptionalDesignList!=null) OptionalDesignList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptionalDesignList!=null) OptionalDesignList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignIdentList(\n");

        if(OptionalDesignList!=null)
            buffer.append(OptionalDesignList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I2);
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignIdentList]");
        return buffer.toString();
    }
}
