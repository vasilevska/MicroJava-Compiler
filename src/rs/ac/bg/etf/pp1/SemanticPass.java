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
	Vector<Scope> scope = new Vector<Scope>(4);
	
	boolean returnFound = false;
	boolean errorDetected = false;
	int nVars;
	public static final Struct BOOL_STRUCT = new Struct(Struct.Bool);
	
    public SemanticPass() {
        Tab.currentScope().addToLocals(new Obj(Obj.Type, "bool", BOOL_STRUCT));
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
    
    
	public void visit(Program program){
    	nVars = Tab.currentScope.getnVars();
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
    		}else{
    			report_error("Greska: Ime " + type.getI1() + " ne predstavlja tip!", type);
    			type.struct = Tab.noType;
    		}
    	}
    }
	
	public void visit(VarDecl varDecl){
		varDeclCount++;
		report_info("Deklarisana promenljiva "+ varDecl.getI2(), varDecl);
		Obj varNode = Tab.insert(Obj.Var, varDecl.getI2(), varDecl.getType().struct);
	}
	
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
    
    public void visit(MethodDecl methodDecl){
    	if(!returnFound && currentMethod.getType() != Tab.noType){
			report_error("Semanticka greska na liniji " + methodDecl.getLine() + ": funkcija " + currentMethod.getName() + " nema return iskaz!", null);
    	}
    	Tab.chainLocalSymbols(currentMethod);
    	Tab.closeScope();
    	
    	returnFound = false;
    	currentMethod = null;
    }
	
	/*
    public void visit(PrintStmt print) {
    	if(print.getExpr().struct != Tab.intType && print.getExpr().struct!= Tab.charType) report_error ("Semanticka greska na liniji " + print.getLine() + ": Operand instrukcije PRINT mora biti char ili int tipa", null );
		printCallCount++;
	}
    
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
    
    public void visit(Term term){
    	term.struct = term.getFactor().struct;
    }
    
    public void visit(TermExpr termExpr){
    	termExpr.struct = termExpr.getTerm().struct;
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
    
    public void visit(Const cnst){
    	cnst.struct = Tab.intType;
    }
    
    public void visit(Var var){
    	var.struct = var.getDesignator().obj.getType();
    }
    
    public void visit(ReturnExpr returnExpr){
    	returnFound = true;
    	Struct currMethType = currentMethod.getType();
    	if(!currMethType.compatibleWith(returnExpr.getExpr().struct)){
			report_error("Greska na liniji " + returnExpr.getLine() + " : " + "tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije " + currentMethod.getName(), null);
    	}
    }
    
    public void visit(Assignment assignment){
    	if(!assignment.getExpr().struct.assignableTo(assignment.getDesignator().obj.getType()))
    		report_error("Greska na liniji " + assignment.getLine() + " : " + "nekompatibilni tipovi u dodeli vrednosti! ", null);
    }
    
     */

   
}
