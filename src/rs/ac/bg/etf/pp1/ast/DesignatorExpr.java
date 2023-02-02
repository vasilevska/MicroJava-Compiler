// generated with ast extension for cup
// version 0.8
// 2/1/2023 23:59:19


package rs.ac.bg.etf.pp1.ast;

public class DesignatorExpr extends Designator {

    private OptionalDesignList OptionalDesignList;
    private String I2;
    private Expr Expr;

    public DesignatorExpr (OptionalDesignList OptionalDesignList, String I2, Expr Expr) {
        this.OptionalDesignList=OptionalDesignList;
        if(OptionalDesignList!=null) OptionalDesignList.setParent(this);
        this.I2=I2;
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
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

    public Expr getExpr() {
        return Expr;
    }

    public void setExpr(Expr Expr) {
        this.Expr=Expr;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(OptionalDesignList!=null) OptionalDesignList.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(OptionalDesignList!=null) OptionalDesignList.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(OptionalDesignList!=null) OptionalDesignList.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorExpr(\n");

        if(OptionalDesignList!=null)
            buffer.append(OptionalDesignList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(" "+tab+I2);
        buffer.append("\n");

        if(Expr!=null)
            buffer.append(Expr.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [DesignatorExpr]");
        return buffer.toString();
    }
}
