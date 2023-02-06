// generated with ast extension for cup
// version 0.8
// 6/1/2023 20:16:11


package rs.ac.bg.etf.pp1.ast;

public class MulFactors extends MulFactorList {

    private MulFactorList MulFactorList;
    private MulOpp MulOpp;
    private Factor Factor;

    public MulFactors (MulFactorList MulFactorList, MulOpp MulOpp, Factor Factor) {
        this.MulFactorList=MulFactorList;
        if(MulFactorList!=null) MulFactorList.setParent(this);
        this.MulOpp=MulOpp;
        if(MulOpp!=null) MulOpp.setParent(this);
        this.Factor=Factor;
        if(Factor!=null) Factor.setParent(this);
    }

    public MulFactorList getMulFactorList() {
        return MulFactorList;
    }

    public void setMulFactorList(MulFactorList MulFactorList) {
        this.MulFactorList=MulFactorList;
    }

    public MulOpp getMulOpp() {
        return MulOpp;
    }

    public void setMulOpp(MulOpp MulOpp) {
        this.MulOpp=MulOpp;
    }

    public Factor getFactor() {
        return Factor;
    }

    public void setFactor(Factor Factor) {
        this.Factor=Factor;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(MulFactorList!=null) MulFactorList.accept(visitor);
        if(MulOpp!=null) MulOpp.accept(visitor);
        if(Factor!=null) Factor.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(MulFactorList!=null) MulFactorList.traverseTopDown(visitor);
        if(MulOpp!=null) MulOpp.traverseTopDown(visitor);
        if(Factor!=null) Factor.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(MulFactorList!=null) MulFactorList.traverseBottomUp(visitor);
        if(MulOpp!=null) MulOpp.traverseBottomUp(visitor);
        if(Factor!=null) Factor.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("MulFactors(\n");

        if(MulFactorList!=null)
            buffer.append(MulFactorList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(MulOpp!=null)
            buffer.append(MulOpp.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Factor!=null)
            buffer.append(Factor.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [MulFactors]");
        return buffer.toString();
    }
}
