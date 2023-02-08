package rs.ac.bg.etf.pp1;


import rs.ac.bg.etf.pp1.ast.*;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;
import java.util.stream.Stream;

import rs.ac.bg.etf.pp1.SemanticPass;



public class CodeGenerator extends VisitorAdaptor {

	record Pair ( Obj obj , Integer adr ) {}
	
	private int mainPc;
	private Stack<Integer> counter = new Stack<>();
	private Stack<Integer> counterOr = new Stack<>();
	private Vector<Obj> curr = new Vector<Obj>(10);
	private Vector<Obj> arrlist = new Vector<Obj>(10);
	private Vector<Struct> types = new Vector<Struct>(10);
	//private Stack<Vector<Integer>> condFacts = new Stack<>();
	private Stack<Integer> lastLoop = new Stack<>();
	private Stack<Integer> loopStart = new Stack<>();
	private Vector<Integer> breakPC = new Vector<Integer>(5);
	private Vector<Integer> continuePC = new Vector<Integer>(5);
	private Stack<Integer> bcnt = new Stack<>();
	private Stack<Integer> ccnt = new Stack<>();
	private Stack<Integer> condFixUp = new Stack<>();
	private Map<String, Pair> vmTableAdr = new HashMap<>();
	boolean constrCall = false;
	
	Obj currentClass = null;
	
	public int getMainPc(){
		return mainPc;
	}
	
	public static final Struct boolType = SemanticPass.boolType;
	
	//EXPRESSIONS
		//FIXME: drugacije loadovanje ako su objekti klase
	public void visit(Type t) {
		if(t.getParent() instanceof FactorNewActPars) {
			curr.clear();
			constrCall = true;
		}
	}

	public void visit(FactorNumber cnst) {
		Obj con = Tab.insert(Obj.Con, "$", Tab.intType);
		con.setLevel(0);
		con.setAdr(cnst.getN1());
		
		if(!constrCall) Code.load(con);
		curr.add(con);
	}
	
	public void visit(FactorChar cnst) {
		Obj con = Tab.insert(Obj.Con, "$", Tab.charType);
		con.setLevel(0);
		con.setAdr(cnst.getC1());
		
		if(!constrCall) Code.load(con);
		curr.add(con);
	}
	
	public void visit(FactorBool cnst) {
		Obj con = Tab.insert(Obj.Con, "$", boolType);
		con.setLevel(0);
		con.setAdr(cnst.getB1()? 1:0);
		
		if(!constrCall) Code.load(con);
		curr.add(con);
	}
	
	public void visit(FactorBase factor) {
		if(!constrCall) Code.load(factor.getDesignator().obj);
		curr.add(factor.getDesignator().obj);
	}
	
	public void visit(FactorParen factor) {
		methodCall(factor.getDesignator());
	}
	
	public void visit(FactorActPars factor) {
		methodCall(factor.getDesignator());
	}
	
	
	public void visit(FactorNewSquare factor) {
		Code.put(Code.newarray);
		Code.put((factor.getType().struct.equals(Tab.charType))? 0: 1);
	}
	
	public void visit(MulFactors factor) {
		if(factor.getMulOpp() instanceof MulOp) Code.put(Code.mul);
		else if(factor.getMulOpp() instanceof DivOp) Code.put(Code.div);
		else Code.put(Code.rem);
	}
	
	public void visit(AddTerms term) {
		Code.put((term.getAddOpp() instanceof AddOp)? Code.add : Code.sub);
	}
	
	public void visit(ExprSub expr) {
		Code.put(Code.neg);
	}
	
	public void visit(ActPars pars) {
		types.add(pars.getExpr().struct);
		if(pars.getExprList() instanceof Exprs) {
			Exprs el = (Exprs) pars.getExprList();
			types.add(el.getExpr().struct);
			while(el.getExprList() instanceof Exprs) {
				el = (Exprs) el.getExprList();
				types.add(el.getExpr().struct);
			}	
		}
	}
	
	
	public void visit(StatementBreak stmt) {
		Code.putJump(0);
		breakPC.add(Code.pc - 2);
		bcnt.push(bcnt.pop() + 1);
	}
	
	public void visit(StatementContinue stmt) {
		Code.putJump(loopStart.peek());
		continuePC.add(Code.pc - 2);
		ccnt.push(ccnt.pop() + 1);
	}
	
	public void visit(StatementPrintExpr printStmt){
		if(printStmt.getExpr().struct != Tab.charType){
			Code.loadConst(5);
			Code.put(Code.print);
		}else{
			Code.loadConst(1);
			Code.put(Code.bprint);
		}
	}
	
	public void visit(StatementPrintExprNumber printStmt){
		int width = printStmt.getN2();
		Code.loadConst(width);
		if(printStmt.getExpr().struct != Tab.charType){
			Code.put(Code.print);
		}else{
			Code.put(Code.bprint);
		}
	}
	
	public void visit(StatementRead stmt) {
		Obj des = stmt.getDesignator().obj;
		if(des.getType() != Tab.charType){
			Code.put(Code.read);
		}else{
			Code.put(Code.bread);
		}
		
		Code.store(des);
	}
	
	public void visit(MethodNameType methodName){
		
		methodName.obj.setAdr(Code.pc);
		// Collect arguments and local variables
		MethodDeclDerived1 methodNode = (MethodDeclDerived1) methodName.getParent();
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		
		
		// Generate the entry
		Code.put(Code.enter);
		Code.put(methodName.obj.getLevel());
		Code.put(methodName.obj.getLocalSymbols().size());
	
	}
	
	public void visit(MethodNameVoid methodName){
		
		if("main".equals(methodName.getI1())){		
			vmtable();
			mainPc = Code.pc;
		}
		methodName.obj.setAdr(Code.pc);
		
		// Collect arguments and local variables
		MethodDeclDerived1 methodNode = (MethodDeclDerived1) methodName.getParent();
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		
		// Generate the entry
		Code.put(Code.enter);
		Code.put(methodName.obj.getLevel());
		Code.put(methodName.obj.getLocalSymbols().size());
		
	}
	
	public void visit(MethodDeclDerived1 methodDecl){
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	//FIXME: mozes ovo malo da izmenis ako imas vremena cisto da ne bude bas previse ocigledno
	public void visit(FEElem el) {
		bcnt.push(0);
		ccnt.push(0);
		lastLoop.push(2);
		Obj des =((StatementForeach) el.getParent()).getDesignator().obj;
		Code.load(des);
		Code.put(Code.arraylength);
		Code.loadConst(0);
		loopStart.push(Code.pc);
		Code.put(Code.dup);
		Code.load(des);
		Code.put(Code.dup_x1);
		Code.put(Code.pop);
		Code.put(Code.aload);
		Code.store(el.obj);
	}
	
	public void visit(StatementForeach stmt) {
		int cnt = ccnt.pop();
		//loop continue adr
		for(int i = 0; i<cnt; i++) {
			Code.fixup(continuePC.lastElement()); 
			continuePC.remove(continuePC.size()-1);
		}
		Code.loadConst(1);
		Code.put(Code.add);
		Code.put(Code.dup2);
		Code.putFalseJump(Code.eq, loopStart.peek());
		//loop break adr
		cnt = bcnt.pop();
		for(int i = 0; i<cnt; i++) {
			Code.fixup(breakPC.lastElement()); 
			breakPC.remove(breakPC.size()-1);
		}
		Code.put(Code.pop);
		Code.put(Code.pop);
		lastLoop.pop();	
		loopStart.pop();
	}
	
	public void visit(While w) {
		bcnt.push(0);
		ccnt.push(0);
		lastLoop.push(1);
		loopStart.add(Code.pc);
		counter.push(0);
		counterOr.push(0);
	}
	
	public void visit(StatementWhile stmt) {
		/*
		int cnt = ccnt.pop();
		for(int i = 0; i<cnt; i++) {
			Code.fixup(continuePC.lastElement()); 
			continuePC.remove(continuePC.size()-1);
		}
		*/
		Code.putJump(loopStart.peek());
		
		for(int i = 0; i < counter.peek(); i++) {
			Code.fixup(condFixUp.pop());
		}
		counter.pop();
		
		int cnt = bcnt.pop();
		for(int i = 0; i<cnt; i++) {
			Code.fixup(breakPC.lastElement()); 
			breakPC.remove(breakPC.size()-1);
		}
		lastLoop.pop();	
		loopStart.pop();
	}
	
	public void visit(If ifstart) {
		counter.push(0);
		counterOr.push(0);
	}
	
	public void visit(Else e) {
		Code.putJump(0);
		int ifcond = Code.pc - 2;
		for(int i = 0; i < counter.peek(); i++) {
			Code.fixup(condFixUp.pop());
		}
		counter.pop();
		condFixUp.push(ifcond);
	}
	
	public void visit(StatementIfElse stmt) {
		Code.fixup(condFixUp.pop());
	}
	
	public void visit(ArrDesignator des) {
		Code.load(des.getDesignator().obj);
		if(des.getParent() instanceof FactorActPars || des.getParent() instanceof DesignatorStatementActPars) curr.clear();
	}
	public void visit(DesignatorIdent des) {
		if(des.getParent() instanceof FactorActPars || des.getParent() instanceof DesignatorStatementActPars) curr.clear();
	}
	public void visit(DesignatorClassField des) {
		if(des.getParent() instanceof FactorActPars || des.getParent() instanceof DesignatorStatementActPars) curr.clear();
	}
	
	public void visit(DesignatorStatementAssignExpr stmt) {
		Obj des = stmt.getDesignator().obj;
		Code.store(des);
	}
	
	public void visit(DesignatorStatementInc stmt) {
		Obj des = stmt.getDesignator().obj;
		Code.load(des);
		Code.put(Code.const_1);
		Code.put(Code.add);
		Code.store(des);
	}
	
	public void visit(DesignatorStatementDec stmt) {
		Obj des = stmt.getDesignator().obj;
		Code.load(des);
		Code.put(Code.const_1);
		Code.put(Code.sub);
		Code.store(des);
	}
	
	boolean flagListAs = false;
	public void visit(DesignatorArr des) {
		if(des.getParent() instanceof DesignatorStatementListAssign) arrlist.clear();
		if(des.getDesignator() instanceof DesignatorExpr) flagListAs = true;
		arrlist.add(des.getDesignator().obj);
	}
	
	public void visit(NoDesignatorArr des) {
		if(des.getParent() instanceof DesignatorStatementListAssign) arrlist.clear();
		arrlist.add(Tab.noObj);
	}
	
	public void visit(DesignatorStatementListAssign stmt) {
		Obj des = stmt.getAssignDesign().getDesignator().obj; 
			for(int i = 0; i < arrlist.size(); i++) {
				if(arrlist.get(i) != Tab.noObj) {
					Code.load(des);
					Code.put(Code.const_);
					if(!flagListAs) Code.put4(i);
					else Code.put4(arrlist.size()-1-i);
					Code.put(Code.aload);
					Code.store(arrlist.get(i));
				}
			}
		arrlist.clear();
		flagListAs = false;
	}
	
	public void visit(DesignatorStatementParens stmt) {
		methodCall(stmt.getDesignator());
	}
	
	public void visit(DesignatorStatementActPars stmt) {
		methodCall(stmt.getDesignator());
	}
	
	public void visit(CondFactBase cond) {
		Code.loadConst(1);
		Code.putFalseJump(Code.eq, 0);
		
		condFixUp.push(Code.pc - 2);
		counter.push(counter.pop() + 1);
	}
	
	public void visit(CondFactRel cond) {	
		if(cond.getRelOpp() instanceof EqOp) Code.putFalseJump(Code.eq, 0);
		if(cond.getRelOpp() instanceof NeqOp) Code.putFalseJump(Code.ne, 0);
		if(cond.getRelOpp() instanceof GreOp) Code.putFalseJump(Code.ge, 0);
		if(cond.getRelOpp() instanceof LeqOp) Code.putFalseJump(Code.le, 0);
		if(cond.getRelOpp() instanceof GrOp) Code.putFalseJump(Code.gt, 0);
		if(cond.getRelOpp() instanceof LsOp) Code.putFalseJump(Code.lt, 0);
		
		condFixUp.push(Code.pc - 2);
		counter.push(counter.pop() + 1);
	}
	
	public void visit(CondTerm cond) {
		if(!(cond.getParent() instanceof Condition)) {
			Code.putJump(0);
			for(int i = 0; i < counter.peek(); i++) {
				Code.fixup(condFixUp.pop());
			}
			counter.pop();
			counter.push(0);
			condFixUp.add(Code.pc-2);
			counterOr.push(counterOr.pop() + 1);
		}	
	}
	
	public void visit(Condition cond) {
		for(int i = 0; i < counterOr.peek(); i++) {
			Code.fixup(condFixUp.get(0));
			condFixUp.remove(0);
		}
	}
	
	private void methodCall(Designator des) {
		String name = des.obj.getName();
		if(name == "ord" || name == "len" || name == "chr") return;
		
		int offset = des.obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
		curr.clear();
	}
	
	
	
	private void defaultConstructor() {
		Obj constr = (Obj) currentClass.getType().getMembers().toArray()[1];
		constr.setAdr(Code.pc);
		Code.put(Code.enter);
		Code.put(constr.getLevel());
		Code.put(constr.getLocalSymbols().size());
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	private void vmtable() {
		int curadr; 
		for(Pair p: vmTableAdr.values()) {
			curadr = p.adr();
			Stream<Obj> methods = p.obj().getType().getMembers().stream().filter(o-> o.getKind() == Obj.Meth && !o.getName().contains("~") && !o.getName().contains("#"));
			for(Obj method: methods.toList()) {
				for(int c: method.getName().chars().toArray()) {
					Code.loadConst(c);
					Code.put(Code.putstatic);
					Code.put2(curadr);
					curadr++;
				}
				Code.put(Code.const_m1);
				Code.put(Code.putstatic);
				Code.put2(curadr);
				curadr++;
				Code.loadConst(method.getAdr());
				Code.put(Code.putstatic);
				Code.put2(curadr);
				curadr++;
			}
			Code.loadConst(-2);
            Code.put(Code.putstatic);
            Code.put2(curadr);
			curadr++;
		}
	}

	public void visit(ClassName cl) {
		currentClass = cl.obj;
	}
	
	public void visit(ClassDecl cl) {
		if(currentClass.getType().getElemType() != null) {
			//ovde za ako postoji parent
		}
		vmTableAdr.put(currentClass.getName(), new Pair(currentClass, Code.dataSize));
		Code.dataSize += currentClass.getType().getMembers().stream().filter(o -> o.getKind() == Obj.Meth).mapToInt(o -> o.getName().length() + 2).sum() + 1;
		currentClass = null;
	}
	
	public void visit(ConstructorName constr) {
		Code.put(Code.enter);
		Code.put(constr.obj.getLevel());
		Code.put(constr.obj.getLocalSymbols().size());
		
	}
	
	public void visit(ConstructorDecl constr) {
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(ClassBodyEmpty body) {
		defaultConstructor();
	}
	
	public void visit(ClassBodyMeth body) {
		defaultConstructor();
	}
	
	public void visit(FactorNewParen factor) {
		Code.put(Code.new_);
		Code.put2(factor.struct.getNumberOfFields() * 4); 
		Code.put(Code.dup);
		Code.put(Code.dup);
		Code.loadConst(vmTableAdr.get(factor.getType().getI1()).adr());
		Code.put(Code.putfield);
        Code.put2(0);
        Code.put(Code.call);
        int conAdr = vmTableAdr.get(factor.getType().getI1()).obj().getType().getMembers().stream().filter(o->o.getName().equals(factor.getType().getI1()+"~") ).toList().get(0).getAdr();
        Code.put2(conAdr - Code.pc +1);
	}
	
	public void visit(FactorNewActPars factor) {
		Code.put(Code.new_);
		Code.put2(factor.struct.getNumberOfFields() * 4); 
		Code.put(Code.dup);
		Code.put(Code.dup);
		Code.loadConst(vmTableAdr.get(factor.getType().getI1()).adr());
		Code.put(Code.putfield);
        Code.put2(0);
        for(Obj a: curr) {
        	Code.load(a);
        }
        int conAdr = -10;
        for(Obj con: vmTableAdr.get(factor.getType().getI1()).obj().getType().getMembers().stream().filter(o->o.getName().contains(factor.getType().getI1()+"#") ).toList()) {
        	boolean flag = true;
        	int i = 0;
        	for(Obj par: con.getLocalSymbols().stream().limit(con.getLevel()).toList()) {
        		if(par.getName().equals("this")) continue;
        		if(par.getKind() != types.get(i).getKind()) flag = false;
        		i++;
        	}
        	if(flag) {
        		conAdr = vmTableAdr.get(factor.getType().getI1()).obj().getType().getMembers().stream().filter(o->o.getName().equals(con.getName())).toList().get(0).getAdr();
        		break;
        	}
        }
        Code.put(Code.call);
        Code.put2(conAdr - Code.pc +1);
        curr.clear();
        constrCall = false;
	}
	
	/*
	
	public void visit(Assignment assignment){
		Code.store(assignment.getDesignator().obj);
	}
	
	public void visit(Designator designator){
		SyntaxNode parent = designator.getParent();
		
		if(Assignment.class != parent.getClass() && FuncCall.class != parent.getClass() && ProcCall.class != parent.getClass()){
			Code.load(designator.obj);
		}
	}
	
	public void visit(FuncCall funcCall){
		Obj functionObj = funcCall.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		
		Code.put2(offset);
	}
	
	public void visit(ProcCall procCall){
		Obj functionObj = procCall.getDesignator().obj;
		int offset = functionObj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
		if(procCall.getDesignator().obj.getType() != Tab.noType){
			Code.put(Code.pop);
		}
	}
	
	
	public void visit(AddExpr addExpr){
		Code.put(Code.add);
	}
	*/
}
