// generated with ast extension for cup
// version 0.8
// 3/1/2023 22:17:39


package rs.ac.bg.etf.pp1.ast;

public class AddTerms extends AddTermList {

    private AddTermList AddTermList;
    private AddOpp AddOpp;
    private Term Term;

    public AddTerms (AddTermList AddTermList, AddOpp AddOpp, Term Term) {
        this.AddTermList=AddTermList;
        if(AddTermList!=null) AddTermList.setParent(this);
        this.AddOpp=AddOpp;
        if(AddOpp!=null) AddOpp.setParent(this);
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
    }

    public AddTermList getAddTermList() {
        return AddTermList;
    }

    public void setAddTermList(AddTermList AddTermList) {
        this.AddTermList=AddTermList;
    }

    public AddOpp getAddOpp() {
        return AddOpp;
    }

    public void setAddOpp(AddOpp AddOpp) {
        this.AddOpp=AddOpp;
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(AddTermList!=null) AddTermList.accept(visitor);
        if(AddOpp!=null) AddOpp.accept(visitor);
        if(Term!=null) Term.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(AddTermList!=null) AddTermList.traverseTopDown(visitor);
        if(AddOpp!=null) AddOpp.traverseTopDown(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(AddTermList!=null) AddTermList.traverseBottomUp(visitor);
        if(AddOpp!=null) AddOpp.traverseBottomUp(visitor);
        if(Term!=null) Term.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("AddTerms(\n");

        if(AddTermList!=null)
            buffer.append(AddTermList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AddOpp!=null)
            buffer.append(AddOpp.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [AddTerms]");
        return buffer.toString();
    }
}
