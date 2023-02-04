package rs.ac.bg.etf.pp1;

import java.util.Vector;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticPass extends VisitorAdaptor {

	int printCallCount = 0;
	int varDeclCount = 0;
	
	Obj currentMethod = null;
	Obj currentClass = null;
	Obj currentParent = null;
	Struct currentType = null;
	int currentConst;
	int paramCount = 0;
	//Vector<Scope> scope = new Vector<Scope>(10);
	Vector<Obj> curr = new Vector<Obj>(10);
	boolean mainFound = false;
	boolean returnFound = false;
	boolean errorDetected = false;
	
	int nVars;
	public static final Struct boolType =  new Struct(Struct.Bool);
	
    public SemanticPass() {
        Tab.currentScope().addToLocals(new Obj(Obj.Type, "bool", boolType));
        
    }
    
	Logger log = Logger.getLogger(getClass());

	public void report_error(String message, SyntaxNode info) {
		errorDetected = true;
		StringBuilder msg = new StringBuilder(message);
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.error(msg.toString());
	}

	public void report_info(String message, SyntaxNode info) {
		StringBuilder msg = new StringBuilder(message); 
		int line = (info == null) ? 0: info.getLine();
		if (line != 0)
			msg.append (" na liniji ").append(line);
		log.info(msg.toString());
	}
	  
    public boolean passed(){
    	return !errorDetected;
    }
    
    
    //BASIC
    
	public void visit(Program program){
    	nVars = Tab.currentScope.getnVars();
    	Obj main = Tab.find("main");
    	if(main == Tab.noObj) this.report_error("nema main-a", null); 
    	
    	Tab.chainLocalSymbols(program.getProgName().obj);
    	Tab.closeScope();    	
    }
    
	public void visit(ProgName progName){
    	progName.obj = Tab.insert(Obj.Prog, progName.getI1(), Tab.noType);
    	Tab.openScope();
    }
	
	public void visit(Type type){
    	Obj typeNode = Tab.find(type.getI1());
    	if(typeNode == Tab.noObj){
    		report_error("Nije pronadjen tip " + type.getI1() + " u tabeli simbola! ", null);
    		type.struct = Tab.noType;
    	}else{
    		if(Obj.Type == typeNode.getKind()){
    			type.struct = typeNode.getType();
    			currentType = type.struct;
    		}else{
    			report_error("Greska: Ime " + type.getI1() + " ne predstavlja tip!", type);
    			type.struct = Tab.noType;
    		}
    	}
    }
	
	
	//VAR DECL
	
	public void visit(VarDecl varDecl){
		if(Tab.currentScope().findSymbol(varDecl.getI2()) != Tab.noObj) {
    		report_error("varijabla sa imenom: " + varDecl.getI2() +  " je vec deklarisana", null);
    		return;
    	}
		int t = Obj.Var;
		if(currentClass != null) t = Obj.Fld; 
		report_info("Deklarisana promenljiva "+ varDecl.getI2(), varDecl);
		if(varDecl.getEmptySqBrackets() instanceof ArrayBrackets) Tab.insert(t, varDecl.getI2(), new Struct(Struct.Array, varDecl.getType().struct));
		else Tab.insert(t, varDecl.getI2(), varDecl.getType().struct);
		for(Obj o: curr) Tab.insert(t, o.getName(), o.getType());
    	curr.clear();
	}
	
	public void visit(ArrayVar varDecl) {
		if(varDecl.getEmptySqBrackets() instanceof ArrayBrackets) curr.add(new Obj(Obj.Var, varDecl.getI2(), new Struct(Struct.Array, currentType)));
    	curr.add(new Obj(Obj.Var, varDecl.getI2(), currentType));
	}
	
	public void visit(NoVarDecl declList) {
		if(currentClass != null && currentMethod == null) {
			Tab.insert(Obj.Meth, currentClass.getName() + "~", Tab.noType);
		}
	}
	
	//FIXME: provera da li je program scope ako nije error
	public void visit(AssignConst con) {
		if(Tab.find(con.getI1()) != Tab.noObj) {
			report_error("Vec definisana konstanta sa istim imenom", null);
			return;
		}
		Obj node = Tab.insert(Obj.Con, con.getI1(), currentType);
		node.setAdr(currentConst);
	}
	
	public void visit(IntConst con) {currentConst = con.getN1();}
	public void visit(CharConst con) {currentConst = con.getC1();}
	public void visit(BoolConst con) {currentConst = con.getB1()? 1 : 0;}
	
	//METHODS
		// FIXME: dodavanj proveru za postojece funkcije 
		/* Kako da se izbaci postojeci cvor ako se overrideuje metoda klase ??*/
	
	public void visit(MethodNameType methodName){
    	currentMethod = Tab.insert(Obj.Meth, methodName.getI2(), methodName.getType().struct);
    	methodName.obj = currentMethod;
    	Tab.openScope();
		report_info("Obradjuje se funkcija " + methodName.getI2(), methodName);
    }
	
	public void visit(MethodNameVoid methodName){
    	currentMethod = Tab.insert(Obj.Meth, methodName.getI1(), Tab.noType);
    	methodName.obj = currentMethod;
    	Tab.openScope();
		report_info("Obradjuje se funkcija " + methodName.getI1(), methodName);
    }
    
    public void visit(MethodDeclDerived1 methodDecl){
    	if(!returnFound && currentMethod.getType() != Tab.noType){
			report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funkcija " + currentMethod.getName() + " nema return iskaz!", null);
    	}
    	Tab.chainLocalSymbols(currentMethod);
    	Tab.closeScope();
    	
    	returnFound = false;
    	currentMethod = null;
    }
    
    public void visit(StatementReturnExpr returnExpr){
    	if(currentMethod == null) report_error("iz cega se returnujes br na liniji " + returnExpr.getLine(), null);
    	returnFound = true;
    	Struct currMethType = currentMethod.getType();
    	if(!currMethType.compatibleWith(returnExpr.getExpr().struct)){
			report_error("Greska na liniji " + returnExpr.getLine() + " : " + "tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije " + currentMethod.getName(), null);
    	}
    }
    
    public void visit(StatementReturnVoid returnExpr){
    	if(currentMethod == null) report_error("iz cega se returnujes br na liniji " + returnExpr.getLine(), null);
    	returnFound = true;
    	if(currentMethod.getType() != Tab.noType ){
			report_error("Greska na liniji " + returnExpr.getLine() + " : " + "tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije " + currentMethod.getName(), null);
    	}
    }
    
    public void visit(ArrayParam param) {
    	if(param.getEmptySqBrackets() instanceof ArrayBrackets) curr.add(new Obj(Obj.Var, param.getI3(), new Struct(Struct.Array, param.getType().struct)));
    	curr.add(new Obj(Obj.Var, param.getI3(), param.getType().struct));
    }
    
    public void visit(FormPars param) {
    	if(currentClass != null) Tab.insert(Obj.Var, "this", currentClass.getType()); //FIXME: da li ovde treba struct tipa
    	if(param.getEmptySqBrackets() instanceof ArrayBrackets) Tab.insert(Obj.Var, param.getI2(), new Struct(Struct.Array, param.getType().struct));
    	else Tab.insert(Obj.Var, param.getI2(), param.getType().struct);
    	for(Obj o: curr) Tab.insert(o.getKind(), o.getName(), o.getType());
    	curr.clear();
    }

    public void visit(NoFormPars pars) {
    	if(currentClass != null) Tab.insert(Obj.Var, "this", currentClass.getType());
    }
    
    
    //EXPRESION
    	// FIXME: dodaj greske za izraze ako se ne poklapaju tipovi
    
    public void visit(ExprBase expr) {
    	expr.struct = expr.getTerm().struct;
    }
    
    public void visit(ExprSub expr) {
    	expr.struct = expr.getTerm().struct;
    }
    
    public void visit(Term term) {
    	term.struct = term.getFactor().struct;
    }
    
    public void visit(FactorNumber factor) {
    	factor.struct = Tab.intType;
    }
    
    public void visit(FactorChar factor) {
    	factor.struct = Tab.charType;
    }
    
    public void visit(FactorBool factor) {
    	factor.struct = boolType;
    }
    
    //STATEMENTS
    
    public void visit(StatementPrintExpr print) {
    	if(print.getExpr().struct != Tab.intType && print.getExpr().struct!= Tab.charType && print.getExpr().struct!= boolType) report_error ("Semanticka greska na liniji " + print.getLine() + ": Operand instrukcije PRINT mora biti char ili int tipa", null );
		printCallCount++;
	}
	
    
    //CLASES
    
    public void visit(ClassName className) {
    	if(Tab.find(className.getI1()) != Tab.noObj) {
    		report_error("klasa sa imenom: " +className.getI1() +  " je vec deklarisana", null);
    		return;
    	}
    	
    	className.obj = Tab.insert(Obj.Type, className.getI1(), new Struct(Struct.Class));
    	className.obj.getType().setElementType(Tab.noType);
    	Tab.openScope();
    	Tab.insert(Obj.Fld, "vmtable", Tab.intType);
    	currentClass = className.obj;
    	
    }
    
    public void visit(ClassDecl classDecl) {
    	Tab.chainLocalSymbols(currentClass.getType());
    	Tab.closeScope();
    	currentClass = null;
    	currentParent = null;
    }
    
    public void visit(ParentClass classParent) {
    	Obj parent = Tab.find(classParent.getType().getI1());
    	if(parent == Tab.noObj || parent.getKind() != Struct.Class) {
    		report_error("Ne postoji klasa sa imenom " + parent.getName(),null);
    		return;
    	}
    	
    	currentClass.getType().setElementType(parent.getType().getElemType());
    	parent.getType().getMembers().forEach(o -> {
    		if(o.getKind() == Obj.Fld && o.getName() != "vmtable") 
    			Tab.insert(o.getKind(), o.getName(), o.getType());
    	});
    	currentParent = parent;
    }
      
    public void visit(ConstructorDecl constrDecl) {
    	
    }
    
    public void visit(ConstructorName constrName) {
    	
    }
    
    
	/*
    
    public void visit(Designator designator){
    	Obj obj = Tab.find(designator.getName());
    	if(obj == Tab.noObj){
			report_error("Greska na liniji " + designator.getLine()+ " : ime "+designator.getName()+" nije deklarisano! ", null);
    	}
    	designator.obj = obj;
    }
    
    public void visit(FuncCall funcCall){
    	Obj func = funcCall.getDesignator().obj;
    	if(Obj.Meth == func.getKind()){
			if(Tab.noType == func.getType()){
				report_error("Semanticka greska " + func.getName() + " ne moze se koristiti u izrazima jer nema povratnu vrednost ", funcCall);
			}else{
				report_info("Pronadjen poziv funkcije " + func.getName() + " na liniji " + funcCall.getLine(), null);
				funcCall.struct = func.getType();
			}
    	}else{
			report_error("Greska na liniji " + funcCall.getLine()+" : ime " + func.getName() + " nije funkcija!", null);
			funcCall.struct = Tab.noType;
    	}
    }
        
    public void visit(AddExpr addExpr){
    	Struct te = addExpr.getExpr().struct;
    	Struct t = addExpr.getTerm().struct;
    	if(te.equals(t) && te == Tab.intType){
    		addExpr.struct = te;
    	}else{
			report_error("Greska na liniji "+ addExpr.getLine()+" : nekompatibilni tipovi u izrazu za sabiranje.", null);
			addExpr.struct = Tab.noType;
    	}
    }
        
    public void visit(Assignment assignment){
    	if(!assignment.getExpr().struct.assignableTo(assignment.getDesignator().obj.getType()))
    		report_error("Greska na liniji " + assignment.getLine() + " : " + "nekompatibilni tipovi u dodeli vrednosti! ", null);
    }
    
     */

   
}
