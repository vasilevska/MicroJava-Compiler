package rs.ac.bg.etf.pp1;


import rs.ac.bg.etf.pp1.CounterVisitor.FormParamCounter;
import rs.ac.bg.etf.pp1.CounterVisitor.VarCounter;
import rs.ac.bg.etf.pp1.ast.*;
import rs.etf.pp1.mj.runtime.Code;
import rs.etf.pp1.symboltable.Tab;
import rs.etf.pp1.symboltable.concepts.Obj;
import rs.etf.pp1.symboltable.concepts.Struct;

import java.util.Stack;
import java.util.Vector;

import rs.ac.bg.etf.pp1.SemanticPass;

public class CodeGenerator extends VisitorAdaptor {

	private int mainPc;
	private int counter = 0;
	private Vector<Obj> curr = new Vector<Obj>(10);
	private Vector<Struct> types = new Vector<Struct>(10);
	//private Stack<Vector<Integer>> condFacts = new Stack<>();
	
	public int getMainPc(){
		return mainPc;
	}
	
	public static final Struct boolType = SemanticPass.boolType;
	
	//EXPRESSIONS
		//FIXME: drugacije loadovanje ako su objekti klase
	public void visit(FactorNumber cnst) {
		Obj con = Tab.insert(Obj.Con, "$", Tab.intType);
		con.setLevel(0);
		con.setAdr(cnst.getN1());
		
		Code.load(con);
	}
	
	public void visit(FactorChar cnst) {
		Obj con = Tab.insert(Obj.Con, "$", Tab.charType);
		con.setLevel(0);
		con.setAdr(cnst.getC1());
		
		Code.load(con);
	}
	
	public void visit(FactorBool cnst) {
		Obj con = Tab.insert(Obj.Con, "$", boolType);
		con.setLevel(0);
		con.setAdr(cnst.getB1()? 1:0);
		
		Code.load(con);
	}
	
	public void visit(FactorBase factor) {
		Code.load(factor.getDesignator().obj);
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
		
		if("main".equalsIgnoreCase(methodName.getI2())){
			mainPc = Code.pc;
		}
		methodName.obj.setAdr(Code.pc);
		// Collect arguments and local variables
		MethodDeclDerived1 methodNode = (MethodDeclDerived1) methodName.getParent();
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);
		// Generate the entry
		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(fpCnt.getCount() + varCnt.getCount());
	
	}
	
	public void visit(MethodNameVoid methodName){
		
		if("main".equalsIgnoreCase(methodName.getI1())){
			mainPc = Code.pc;
		}
		methodName.obj.setAdr(Code.pc);
		
		// Collect arguments and local variables
		MethodDeclDerived1 methodNode = (MethodDeclDerived1) methodName.getParent();
		VarCounter varCnt = new VarCounter();
		methodNode.traverseTopDown(varCnt);
		
		FormParamCounter fpCnt = new FormParamCounter();
		methodNode.traverseTopDown(fpCnt);
		// Generate the entry
		Code.put(Code.enter);
		Code.put(fpCnt.getCount());
		Code.put(fpCnt.getCount() + varCnt.getCount());
	
	}
	
	public void visit(MethodDeclDerived1 methodDecl){
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(StatementReturnExpr returnExpr){
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	public void visit(StatementReturnVoid returnNoExpr){
		Code.put(Code.exit);
		Code.put(Code.return_);
	}
	
	
	public void visit(ArrDesignator des) {
		Code.load(des.getDesignator().obj);
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
		
	public void visit(DesignatorArr des) {
		curr.add(des.getDesignator().obj);
	}
	
	public void visit(NoDesignatorArr des) {
		curr.add(Tab.noObj);
	}
	
	public void visit(DesignatorStatementListAssign stmt) {
		Obj des = stmt.getAssignDesign().getDesignator().obj;
		for(int i = 0; i < curr.size(); i++) {
			if(curr.get(i) != Tab.noObj) {
				Code.load(des);
				Code.put(Code.const_);
				Code.put4(i);
				Code.put(Code.aload);
				Code.store(curr.get(i));
			}
		}
		curr.clear();
	}
	
	public void visit(DesignatorStatementParens stmt) {
		methodCall(stmt.getDesignator());
	}
	
	public void visit(DesignatorStatementActPars stmt) {
		methodCall(stmt.getDesignator());
	}
	
	public void visit(CondFactBase cond) {
		Code.loadConst(Code.const_1);
		Code.putFalseJump(Code.eq, 0);
		//FIXME: lista nekakva na koju se stavlja adresa skoka na stacku koja treba da se patchuje?
	}
	
	public void visit(CondFactRel cond) {
		
		if(cond.getRelOpp() instanceof EqOp) Code.putFalseJump(Code.eq, 0);
		if(cond.getRelOpp() instanceof NeqOp) Code.putFalseJump(Code.ne, 0);
		if(cond.getRelOpp() instanceof GreOp) Code.putFalseJump(Code.ge, 0);
		if(cond.getRelOpp() instanceof LeqOp) Code.putFalseJump(Code.le, 0);
		if(cond.getRelOpp() instanceof GrOp) Code.putFalseJump(Code.gt, 0);
		if(cond.getRelOpp() instanceof LsOp) Code.putFalseJump(Code.lt, 0);
		//FIXME: dodaj na listu isto kao gore
	}
	
	public void visit(CondFactList cond) {
		
	}
	
	public void visit(CondTerm cond) {
		
	}
	
	
	
	private void methodCall(Designator des) {
		String name = des.obj.getName();
		if(name == "ord" || name == "len" || name == "chr") return;
		
		int offset = des.obj.getAdr() - Code.pc;
		Code.put(Code.call);
		Code.put2(offset);
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
