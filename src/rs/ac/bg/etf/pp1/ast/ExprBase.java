// generated with ast extension for cup
// version 0.8
// 2/1/2023 23:59:19


package rs.ac.bg.etf.pp1.ast;

public class ExprBase extends Expr {

    private Term Term;
    private AddTermList AddTermList;

    public ExprBase (Term Term, AddTermList AddTermList) {
        this.Term=Term;
        if(Term!=null) Term.setParent(this);
        this.AddTermList=AddTermList;
        if(AddTermList!=null) AddTermList.setParent(this);
    }

    public Term getTerm() {
        return Term;
    }

    public void setTerm(Term Term) {
        this.Term=Term;
    }

    public AddTermList getAddTermList() {
        return AddTermList;
    }

    public void setAddTermList(AddTermList AddTermList) {
        this.AddTermList=AddTermList;
    }

    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    public void childrenAccept(Visitor visitor) {
        if(Term!=null) Term.accept(visitor);
        if(AddTermList!=null) AddTermList.accept(visitor);
    }

    public void traverseTopDown(Visitor visitor) {
        accept(visitor);
        if(Term!=null) Term.traverseTopDown(visitor);
        if(AddTermList!=null) AddTermList.traverseTopDown(visitor);
    }

    public void traverseBottomUp(Visitor visitor) {
        if(Term!=null) Term.traverseBottomUp(visitor);
        if(AddTermList!=null) AddTermList.traverseBottomUp(visitor);
        accept(visitor);
    }

    public String toString(String tab) {
        StringBuffer buffer=new StringBuffer();
        buffer.append(tab);
        buffer.append("ExprBase(\n");

        if(Term!=null)
            buffer.append(Term.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        if(AddTermList!=null)
            buffer.append(AddTermList.toString("  "+tab));
        else
            buffer.append(tab+"  null");
        buffer.append("\n");

        buffer.append(tab);
        buffer.append(") [ExprBase]");
        return buffer.toString();
    }
}
