package rs.ac.bg.etf.pp1;

import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.log4j.Logger;

import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.symboltable.*;
import rs.etf.pp1.symboltable.concepts.*;

public class SemanticPass extends VisitorAdaptor {

	int printCallCount = 0;
	int varDeclCount = 0;
	
	Obj currentMethod = null;
	Obj currentConstr = null;
	Obj currentClass = null;
	Obj currentParent = null;
	Struct currentType = null;
	int currentConst;
	int paramCount = 0;
	int loopCount = 0;
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
		if(Tab.currentScope().findSymbol(varDecl.getI2()) != null) {
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
		// FIXME: dodavanj proveru za postojece funkcije (isto ime i parametri)
		// FIXME: dodaj overrideovanje metoda klase
		/* Kako da se izbaci postojeci cvor ako se overrideuje metoda klase ??*/
		// FIXME: actual params
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
    	currentMethod.setLevel(paramCount);
    	Tab.chainLocalSymbols(currentMethod);
    	Tab.closeScope();
    	
    	returnFound = false;
    	currentMethod = null;
    }
    
    public void visit(StatementReturnExpr returnExpr){
    	if(currentMethod == null && currentConstr == null) report_error("iz cega se returnujes brt na liniji " + returnExpr.getLine(), null);
    	returnFound = true;
    	Struct currMethType = currentMethod.getType();
    	if(!currMethType.compatibleWith(returnExpr.getExpr().struct)){
			report_error("Greska na liniji " + returnExpr.getLine() + " : " + "tip izraza u return naredbi ne slaze se sa tipom povratne vrednosti funkcije " + currentMethod.getName(), null);
    	}
    }
    
    public void visit(StatementReturnVoid returnExpr){
    	if(currentMethod == null && currentConstr == null) report_error("iz cega se returnujes br na liniji " + returnExpr.getLine(), null);
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
    	paramCount = curr.size() + 1;
    	if(currentClass != null) paramCount++;
    	curr.clear();
    }

    public void visit(NoFormPars pars) {
    	if(currentClass != null) Tab.insert(Obj.Var, "this", currentClass.getType());
    }
    
    
    //EXPRESIONS
    	//FIXME: operacije, factors
    	//FIXME: provere za odredjene ugradjene funkcije da li je odgovarajuci tip: len, ord, chr
    //EXPRESION
    	// FIXME: dodaj greske za izraze ako se ne poklapaju tipovi
    
    public void visit(ExprBase expr) {
    	expr.struct = expr.getTerm().struct;
    	if(expr.struct == Tab.noType || ( expr.getAddTermList() instanceof AddTerms && expr.getAddTermList().struct == Tab.noType)) {
    		report_error("sabirci moraju da budu int", null);
    		expr.struct = Tab.noType;
    	}
    }
    
    public void visit(ExprSub expr) {
    	expr.struct = expr.getTerm().struct;
    	if(expr.struct == Tab.noType || ( expr.getAddTermList() instanceof AddTerms && expr.getAddTermList().struct == Tab.noType)) {
    		report_error("sabirci moraju da budu int", null);
    		expr.struct = Tab.noType;
    	}
    }
    
    public void visit(Term term) {
    	term.struct = term.getFactor().struct;
    	if(term.struct == Tab.noType || ( term.getMulFactorList() instanceof MulFactors && term.getMulFactorList().struct == Tab.noType)) {
    		report_error("cinioci moraju da budu int", null);
    		term.struct = Tab.noType;
    	}
    }
    
    public void visit(MulFactors list) {
    	list.struct = list.getFactor().struct;
    	if(list.struct != Tab.intType) {
    		report_error("cinioci moraju da budu int", null);
    		list.struct = Tab.noType;
    	}   		
    }
    
    public void visit(AddTerms list) {
    	list.struct = list.getTerm().struct;
    	if(list.struct != Tab.intType) {
    		report_error("sabirci moraju da budu int", null);
    		list.struct = Tab.noType;
    	}
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
    
    public void visit(FactorBase factor) {
    	factor.struct = factor.getDesignator().obj.getType();
    }
    
    public void visit(FactorParen factor) {
    	Obj des = factor.getDesignator().obj;
    	if(des.getKind() != Obj.Meth)
    		report_error("probaj da npr pozoves metod", null);
    	
    	
    	List<Obj> formalPars = des.getLocalSymbols().stream().filter(o -> o.getKind() == Obj.Var && o.getName() != "this").limit(des.getLevel()).collect(Collectors.toList());
    	
    	if(formalPars.size() != 0)
    		report_error("broj parametara ne odgovara", null);
    	
    }
    
    public void visit(FactorActPars factor) {
    	Obj des = factor.getDesignator().obj;
    	if(des.getKind() != Obj.Meth)
    		report_error("probaj da npr pozoves metod", null);
    	
    	
    	List<Obj> formalPars = des.getLocalSymbols().stream().filter(o -> o.getKind() == Obj.Var && o.getName() != "this").limit(des.getLevel()).collect(Collectors.toList());
    	
    	if(formalPars.size() != curr.size())
    		report_error("broj parametara ne odgovara", null);
    	
    	for(int i = 0; i<curr.size(); i++) {
    		Struct fp = formalPars.get(i).getType();
    		Struct ap = curr.get(i).getType();
    		while(fp.getKind() == Struct.Array && ap.getKind() == Struct.Array) {
    			fp = fp.getElemType();
    			ap = ap.getElemType();
    		}
    		if(fp.getKind() == Struct.Array || ap.getKind() == Struct.Array || (ap.getKind() != Struct.Class && fp.getKind() == Struct.Class) || (ap.getKind() == Struct.Class && fp.getKind() != Struct.Class)) {
    			report_error("pogresan tip parametara", null);
    			return;
    		}
    		if(fp.getKind() == Struct.Class && ap.getKind() == Struct.Class) {
    			while(ap.getElemType() != null && !ap.equals(fp)) {
    				ap = ap.getElemType();
    			}
    			if(!ap.equals(fp))
    				report_error("klase prosledjenih parametara nisu kompatibilne", null);
    		}
    	}
    }
    
    public void visit(FactorNewSquare factor) {
    	factor.struct = new Struct(Struct.Array, factor.getType().struct);
    	if(factor.getExpr().struct != Tab.intType)
    		report_error("duzina niza nije integer", null);
    }
    
    public void visit(FactorNewActPars factor) {
    	factor.struct = new Struct(Struct.Class, factor.getType().struct);
    	if(factor.getType().struct.getKind() != Struct.Class)
    		report_error("Klasa od koje se instacira objekat ne postoji", null);
    }
    
    public void visit(FactorParensExpr factor) {
    	factor.struct = factor.getExpr().struct;
    }
    
    //STATEMENTS
    
    //STATEMENTS
    	
    public void visit(StatementPrintExpr print) {
    	if(print.getExpr().struct != Tab.intType && print.getExpr().struct!= Tab.charType && print.getExpr().struct!= boolType) 
    		report_error ("Semanticka greska na liniji " + print.getLine() + ": Operand instrukcije PRINT mora biti char ili int tipa", null );
	}
	
    public void visit(StatementPrintExprNumber print) {
    	if((print.getExpr().struct != Tab.intType && print.getExpr().struct!= Tab.charType && print.getExpr().struct!= boolType) || print.getN2() < 1) 
    		report_error ("Semanticka greska na liniji " + print.getLine() + ": Operand instrukcije PRINT mora biti char ili int tipa", null );
	}
    
    public void visit(StatementRead stmt) {
    	
    }
    
    public void visit(StatementContinue stmt) {
    	if(loopCount == 0) {
    		report_error("nema loop-a tu alo", null);
    	}
    }
    
    public void visit(StatementBreak stmt) {
    	if(loopCount == 0) {
    		report_error("break iz cega tacno???", null);
    	}
    }
    
    public void visit(StatementIfElse stmt) {
    	
    }

    public void visit(StatementWhile stmt) {
    	loopCount--;
    }

    public void visit(StatementForeach stmt) {
    	loopCount--;  	
    }
    
    //CLASSES
    
    //CLASES
    	//FIXME: dodaj proveru za vec postojece konstruktore
    
    public void visit(ClassName className) {
    	if(Tab.find(className.getI1()) != Tab.noObj) {
    		report_error("klasa sa imenom: " +className.getI1() +  " je vec deklarisana", null);
    		return;
    	}
    	
    	className.obj = Tab.insert(Obj.Type, className.getI1(), new Struct(Struct.Class));
    	className.obj.getType().setElementType(Tab.noType);
    	Tab.openScope();
    	currentClass = className.obj;
    	
    	ClassDecl decl = (ClassDecl) className.getParent();
    	if(decl.getOptionalClassBody() instanceof ClassBodyEmpty || decl.getOptionalClassBody() instanceof ClassBodyMeth) {
			Obj con = Tab.insert(Obj.Meth, currentClass.getName() + "~", Tab.noType);
			Tab.openScope();
			Tab.insert(Obj.Var, "this", currentClass.getType());
			Tab.chainLocalSymbols(con);
			Tab.closeScope();
    	}
    	Tab.insert(Obj.Fld, "vmtable", Tab.intType);
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
    	//FIXME:ako postoji konstruktor sa istim parametrima greska - vec definisan
    public void visit(ConstructorDecl constrDecl) {
    	Tab.chainLocalSymbols(currentConstr);
    	Tab.closeScope();
    	currentConstr = null;
    }
    
    public void visit(ConstructorName constrName) {
    	if(!constrName.getI1().equals(currentClass.getName())) {
    		report_error("constrName != className", null);
    		return;
    	}
    	constrName.obj = Tab.insert(Obj.Meth, constrName.getI1(), Tab.noType);
    	Tab.openScope();
    	currentConstr = constrName.obj;
    }
    
    //DESIGNATORS
    
    //OPERACIJE
    
    //DESIGNATORS
    
    public void visit(DesignatorIdent des) {
    	des.obj = Tab.find(des.getI1());
    	if(des.obj == Tab.noObj || (des.obj.getKind()!= Obj.Var && des.obj.getKind() != Obj.Meth && des.obj.getKind() != Obj.Con)) {
    		report_error("ili ne postoji ili ne treba da mu se pristupa", null);
    		des.obj = Tab.noObj;
    		return;
    	}
    }
    
    public void visit(DesignatorExpr des) {
    	Obj arr = des.getDesignator().obj;
    	if(arr == Tab.noObj || arr.getType().getKind() != Struct.Array || des.getExpr().struct != Tab.intType) {
    		report_error("ne moze da se pristupa promenljivoj sa tim indeksom", null);
    		return;
    	}
    	des.obj = new Obj(Obj.Elem, arr.getName(), arr.getType());
    }

    /* Da li radi u klasi? */
    public void visit(DesignatorClassField des) {
    	Obj cls = des.getDesignator().obj;
    	if(cls == Tab.noObj || cls.getType().getKind() != Struct.Class) {
    		report_error("nema te klase", null);
    		return;
    	}
    	
    	cls.getType().getMembers().forEach(o ->{
    		if(o.getName().equals(des.getI2())) des.obj = o;
    	});
    	if(des.obj == null) {
    		report_error("nema trazenog polja u klasi", null);
    	}
    }
        
    public void visit(DesignatorStatementAssignExpr des) {}
    
    public void visit(DesignatorStatementParens des) {}
    
    public void visit(DesignatorStatementActPars des) {}
    
    public void visit(DesignatorStatementInc des) {
    	int k = des.getDesignator().obj.getKind();
    	if(des.getDesignator().obj.getType()!= Tab.intType || (k != Obj.Var && k != Obj.Elem && k != Obj.Fld))
    		report_error("nevalidna promenljiva za inkrementiranje", null);
    }
    
    public void visit(DesignatorStatementDec des) {
    	int k = des.getDesignator().obj.getKind();
    	if(des.getDesignator().obj.getType()!= Tab.intType || (k != Obj.Var && k != Obj.Elem && k != Obj.Fld))
    		report_error("nevalidna promenljiva za dekrementiranje", null);
    }
    
    public void visit(DesignatorStatementListAssign des) {}
    
    
    //CONDITIONS
    
    public void visit(Condition cond) {
    	if(cond.getParent() instanceof StatementWhile) loopCount++;
    }   
    
    public void visit(FEElem el) {
    	loopCount++;
    	StatementForeach p = (StatementForeach) el.getParent(); 
    	if(p.getDesignator().obj.getType().getKind() != Struct.Array) {
    		report_error("foreach moze da se radi samo nad nizom duh", null);
    		return;
    	}
    	el.obj = Tab.find(el.getI1());
    	if(el.obj.getKind() != Obj.Var || p.getDesignator().obj.getType().getElemType() != el.obj.getType())
    		report_error("prosledjeni parametar nije tipa koji odgovara tipu elemenata niza", null);	
    }
    
    
	/*
    
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
