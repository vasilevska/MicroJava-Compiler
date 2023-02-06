// generated with ast extension for cup
// version 0.8
// 6/1/2023 21:0:13


package rs.ac.bg.etf.pp1.ast;

public class DesignatorExpr extends Designator {

    private ArrDesignator ArrDesignator;
    private Expr Expr;

    public DesignatorExpr (ArrDesignator ArrDesignator, Expr Expr) {
        this.ArrDesignator=ArrDesignator;
        if(ArrDesignator!=null) ArrDesignator.setParent(this);
        this.Expr=Expr;
        if(Expr!=null) Expr.setParent(this);
    }

    public ArrDesignator getArrDesignator() {
        return ArrDesignator;
    }

    public void setArrDesignator(ArrDesignator ArrDesignator) {
        this.ArrDesignator=ArrDesignator;
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
        if(ArrDesignator!=null) ArrDesignator.accept(visitor);
        if(Expr!=null) Expr.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(ArrDesignator!=null) ArrDesignator.traverseTopDown(visitor);
        if(Expr!=null) Expr.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(ArrDesignator!=null) ArrDesignator.traverseBottomUp(visitor);
        if(Expr!=null) Expr.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("DesignatorExpr(\n");

        if(ArrDesignator!=null)
            buffer.append(ArrDesignator.toString("  "+tab));
        else
            buffer.append(tab+"  null");
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
