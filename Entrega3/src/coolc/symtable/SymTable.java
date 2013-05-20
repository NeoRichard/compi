package coolc.symtable;

import coolc.ast.AssignExpr;
import coolc.ast.BinaryExpr;
import coolc.ast.BinaryOp;
import coolc.ast.Block;
import coolc.ast.Case;
import coolc.ast.CaseExpr;
import coolc.ast.ClassDef;
import coolc.ast.DispatchExpr;
import coolc.ast.Expr;
import coolc.ast.Feature;
import coolc.ast.IdExpr;
import coolc.ast.IfExpr;
import coolc.ast.LetExpr;
import coolc.ast.Method;
import coolc.ast.NewExpr;
import coolc.ast.Program;
import coolc.ast.UnaryExpr;
import coolc.ast.UnaryOp;
import coolc.ast.ValueExpr;
import coolc.ast.Variable;
import coolc.ast.WhileExpr;

import java.util.*;

/**
 * 
 * 
 * @author richard
 * Trabajo basado en el proyecto del primer semestre del año 2012
 * 
 * Para seguir el orden de impresion, fue necesario cambiar el HashTable a LinkedHashMap, 
 * en la clase Scope.
http://docs.oracle.com/javase/6/docs/api/java/util/LinkedHashMap.html
 *
 */

public class SymTable {

	//	Hashtable<String, ClassScope> classes;
	public LinkedHashMap<String, ClassScope> classes;

	//	Hashtable<String, Scope> scopes;


	
	
	public LinkedHashMap<String, ClassScope> getClasses() {
		return classes;
	}




	/////////////////////////////////////////////////////////////////////////////////
	public void print() {


		String clave, valor;
		// Get a set of the entries
		Set set = classes.entrySet();
		// Get an iterator
		Iterator i = set.iterator();
		// Display elements
		while(i.hasNext()) {
			Map.Entry me = (Map.Entry)i.next();
			clave = (String) me.getKey();




			ClassScope cs = classes.get( clave );



			valor = cs.getParent();

			System.out.print("Class " + clave);
			if( valor == null ){
				System.out.println(" : Object");
			} else {
				System.out.println(" : " + valor);
			}

			int indent = 1;

			print("fields", indent);
			for( Symbol s : cs.getSymbols().values() ) {

				if(!(s instanceof MethodSymbol)) {
					print(s.type + " " + s.id, indent+1);
				}
			}

			print("methods", indent);
			for( Symbol s : cs.getSymbols().values() ) {
				if(s instanceof MethodSymbol) {
					// manejo metodos
					//					print(s.type + " " + s.id, indent+1);
					printMethod((MethodSymbol)s, indent + 1);
				}
			}

		}
	}




	private void printMethod(MethodSymbol ms, int i) {
		// TODO Auto-generated method stub

		printIndent(i);
		System.out.printf("%s %s \n", ms.type, ms.id);

		if(ms.methodScope.parameters.size() > 0)
		{
			printIndent(i+1);
			System.out.println("arguments");

			for(Symbol sym : ms.methodScope.parameters.values())
			{
				printIndent(i+2);
				System.out.printf("%s %s ", sym.type, sym.id);
				System.out.println();

			}

		}


		printIndent(i+1);
		System.out.println("body");


		printBody(ms.methodScope, i+1);

	}




	private void printBody(Scope methodScope, int i) {
		// TODO Auto-generated method stub
		for( Scope s : methodScope.subscopes )
		{
			for(Symbol sym: s.symbols.values())
			{
				printIndent(i+1);
				System.out.println("scope ");
				printIndent(i+2);
				System.out.println(sym.type + " " + sym.id);
			}

			printBody(s, i+1);
		}
	}




	private void print(String string, int indent) {
		// TODO Auto-generated method stub
		printIndent(indent);
		System.out.println(string);
	}




	/////////////////////////////////////////////////////////////////////////////////
	private void print(Program root) {
		for(ClassDef c: root) {            
			print(c);
			System.out.println("\n");
		}
	}

	private  void print(ClassDef c) {
		printIndent(0);
		System.out.printf("class %s", c.getType());

		if( c.getSuper() != null ) {
			System.out.printf(" : %s", c.getSuper());
		} else  {
			System.out.printf(" : Object");
		}

		System.out.println();

		printIndent(1);
		System.out.println("fields: ");
		for(Feature f: c.getBody()) {
			printFeatureVariable(f);
		}
		printIndent(1);
		System.out.println("methods ");
		for(Feature f: c.getBody()) {
			printFeatureMethod(f);
		}
		for(Feature f: c.getBody()) {
			printFeatureElse(f);
		}
	}

	private  void printFeatureMethod(Feature f) {

		if(f instanceof Method) {
			Method m = (Method)f;
			printIndent(2);
			System.out.printf("%s %s \n", m.getType(), m.getName());

			if(m.getParams().size() > 0){
				printIndent(3);
				System.out.println("arguments");

				for(Variable var: m.getParams()) {
					printIndent(4);
					System.out.printf("%s %s ", var.getType(), var.getId());
					System.out.println();

					if( var.getValue() != null ){
						throw new RuntimeException("WTF? initializing parameter definition?");
					}
				}
			}



			printIndent(3);
			System.out.println("body");

			print(m.getBody(), 0);

		}

	}

	private  void printFeatureVariable(Feature f) {
		if (f instanceof Variable) {
			Variable var = (Variable)f;
			printIndent(2);
			System.out.printf("%s %s\n", var.getType(), var.getId());

			/*
            if( var.getValue() != null ) {
                print(var.getValue(), 3);
            }*/

		}
	}

	private  void printFeatureElse(Feature f) {
		if(!(f instanceof Method)  && !(f instanceof Variable)){
			throw new RuntimeException("Unknown feature type " + f.getClass());
		}

	}

	private  void printIndent(int indent) {
		//    	indent--;
		for (int i = indent; i > 0 ; i-- ) {
			System.out.print("    ");
		}  
	}


	@SuppressWarnings("unchecked")
	private  void print(Expr e, int indent) {


		printIndent(indent);

		if(e instanceof Block) {
			for(Expr child: ((Block)e).getStatements()) {
				print(child, indent);
			}
		}
		else if(e instanceof WhileExpr) {
			WhileExpr loop = (WhileExpr)e;

			//			System.out.println("while");
			print(loop.getCond(), indent);

			//			printIndent(indent);
			//			System.out.println("loop");
			print(loop.getValue(), indent);


		}
		else if(e instanceof AssignExpr) {
			/*
            System.out.printf("assign %s\n", ((AssignExpr)e).getId());
            print(((AssignExpr)e).getValue(), indent+1);
			 */
		}        
		else if(e instanceof DispatchExpr) {
			DispatchExpr call = (DispatchExpr)e;

			//			System.out.printf("call %s", call.getName());
			if(call.getType() != null) {
				//				System.out.printf(" as %s", call.getType());
				print(call.getType(), indent);
			}
			//			System.out.println();
			if( call.getExpr() != null ) {
				//				printIndent(indent+1);
				//				System.out.println("callee");
				print(call.getExpr(), indent);
			}

			if (call.getArgs().size() > 0) {
				//				printIndent(indent+1);
				//				System.out.println("args");
				for(Expr arg: call.getArgs()) {
					print(arg, indent);
				}
			}

		}
		else if(e instanceof IfExpr) {
			IfExpr cond = (IfExpr)e;

			//			System.out.println("if");
			print(cond.getCond(), indent);

			//			printIndent(indent);
			//			System.out.println("then");
			print(cond.getTrue(), indent);

			//			printIndent(indent);
			//			System.out.println("else");
			print(cond.getFalse(), indent);

		}
		else if(e instanceof NewExpr) 
		{
			//			System.out.printf("new %s\n",((NewExpr)e).getType());
		}
		else if(e instanceof UnaryExpr) {
			UnaryExpr expr = (UnaryExpr)e;
			//			System.out.printf("unary %s\n", operator(expr.getOp()));
			print(expr.getValue(), indent);
		}
		else if(e instanceof BinaryExpr) {
			BinaryExpr expr = (BinaryExpr)e;
			//			System.out.printf("binary %s\n", operator(expr.getOp()));
			print(expr.getLeft(), indent);
			print(expr.getRight(), indent);
		}
		else if (e instanceof CaseExpr) {
			CaseExpr caseExpr = ((CaseExpr)e);
			print(caseExpr.getValue(), indent+1);

			int i = 0;
			for(Case c : caseExpr.getCases()) {
				printIndent(indent+3);
				System.out.println("scopeCase"+indent);

				printIndent(indent+5);
				System.out.printf(" %s %s\n", c.getType(), c.getId());

				print(c.getValue(), indent);
			}

		}
		else if (e instanceof LetExpr) {
			LetExpr let = (LetExpr)e;
			printIndent(indent+4);
			System.out.println("scope");
			for(Variable var : let.getDecl()) {
				printIndent(indent+5);
				System.out.printf("%s %s\n", var.getType(), var.getId());
				if(var.getValue() != null) {
					print(var.getValue(), indent+1);
				}
			}

			print(let.getValue(), indent);

		}
		else if(e instanceof IdExpr) {
			//            System.out.printf("id %s\n", ((IdExpr)e).getId());
			//			System.out.println();
		}
		else if(e instanceof ValueExpr) {
			Object value = ((ValueExpr)e).getValue();

			if(value instanceof String) {
				//				System.out.printf("str \"%s\"\n", ((String)value).replace("\n", "\\n")
				//						.replace("\t", "\\t").replace("\f", "\\f").replace("\b", "\\b"));
			}
			else if(value instanceof Integer) {
				//				System.out.printf("int %d\n", value);
			}
			else if(value instanceof Boolean) {
				//				System.out.printf("bool %s\n", value);
			}
			else {
				throw new RuntimeException(String.format("Unsupported constant type %s\n", e.getClass()));
			}
		}
		else {

			if( e != null) {
				throw new RuntimeException(String.format("Unsupported node type %s\n", e.getClass()));
			}
			else {
				throw new RuntimeException(String.format("Null node", e.getClass()));
			}

		}

	}

	public static String operator(UnaryOp op) {
		switch(op) {
		case ISVOID:    return "isvoid";
		case NEGATE:    return "~"; 
		case NOT:       return "not";
		}

		return null;
	}

	public static String operator(BinaryOp op) {
		switch(op) {
		case PLUS:      return "+";
		case MINUS:     return "-";
		case MULT:      return "*";
		case DIV:       return "/";            
		case LT:        return "<";
		case LTE:       return "<=";
		case EQUALS:    return "=";
		}
		return null;
	}
	/////////////////////////////////////////////////////////////////////////////////

	public SymTable(Program root) {

		classes = new LinkedHashMap<String, ClassScope>();

		create(root);

		//		print(root);


	}

	private void create(Program root) {

		for(ClassDef c: root) {     
			//       	String parent = c.getSuper();  
			//			if( parent == null )
			//				{ parent = ("Object"); } 			
			String id = c.getType();

			//        	System.out.println("- Clase: "+id+ " : "+parent);
			ClassScope cs = new ClassScope(id);
			//			classes.put(c.getType(), cs);       
			create(c);
		}
	}

	private void create(ClassDef c) {
		// TODO Auto-generated method stub

		String id = c.getType();


		if(classes.get(id) != null)
		{
			System.out.println("Duplicate class "+id);
			return;
		}

		String parent = c.getSuper();
		classes.put(id, new ClassScope(parent));

		ClassScope cs = classes.get(id);

		for(Feature f: c.getBody()) {

			if(f instanceof Method) {
				Method met = (Method)f;
				addMethod(met, cs);
			}

			else if (f instanceof Variable) {
				Variable var = (Variable)f;
				addField(var, cs);
			}

			else {
				System.out.println("Else create ClassDef");
				throw new RuntimeException("Unknown feature type " + f.getClass());
			}
		}



	}

	private void addMethod(Method m, ClassScope cs) {
		// TODO Auto-generated method stub

		String name = m.getName();
		String type = m.getType();


		if(cs.find(name) != null)
		{
			System.out.println("Duplicate method "+name);
			return;
		}

		MethodScope mscp = new MethodScope(cs);
		MethodSymbol msym = new MethodSymbol( name, type, cs, mscp);
		cs.add(name, msym);


		for(Variable var: m.getParams()) {
			Symbol s = new Symbol(var.getId(), var.getType(), msym.methodScope);
			msym.methodScope.addParameter(var.getId(), s);

			if( var.getValue() != null ){
				throw new RuntimeException("WTF? initializing parameter definition?");
			}
		}

		//		System.out.println("body");
		addExpr(m.getBody(), mscp, mscp);

	}

	private void addField(Variable var, ClassScope cs) {

		String name = var.getId();
		String type = var.getType();
		Symbol s = new Symbol( name, type, cs);


		if(cs.find(name) != null)
		{
			System.out.println("Duplicate var " + name);
			return;
		}
		cs.add(var.getId(), s);
	}




	@SuppressWarnings("unchecked")
	private  void addExpr(Expr e, Scope cs, MethodScope ms) {


		if(e instanceof Block) {
			for(Expr child: ((Block)e).getStatements()) {
				addExpr(child, cs, ms);
			}
		}
		else if(e instanceof WhileExpr) {
			WhileExpr loop = (WhileExpr)e;

			addExpr(loop.getCond(), cs, ms);
			addExpr(loop.getValue(), cs, ms);


		}
		else if(e instanceof AssignExpr) {
			/*
            System.out.printf("assign %s\n", ((AssignExpr)e).getId());
            print(((AssignExpr)e).getValue(), indent+1);
			 */
		}        
		else if(e instanceof DispatchExpr) {
			DispatchExpr call = (DispatchExpr)e;
			/*
            System.out.printf("call %s", call.getName());
            if(call.getType() != null) {
                System.out.printf(" as %s", call.getType());
            }
            System.out.println();
			 */
			if( call.getExpr() != null ) {
				//                printIndent(indent+1);
				//                System.out.println("callee");

				//                print(call.getExpr(), indent+2);
				addExpr(call.getExpr(), cs, ms);
			}

			if (call.getArgs().size() > 0) {
				//                printIndent(indent+1);
				//                System.out.println("args");
				for(Expr arg: call.getArgs()) {
					addExpr(arg, cs, ms);

				}
			}
		}
		else if(e instanceof IfExpr) {
			IfExpr cond = (IfExpr)e;

			//            System.out.println("if");
			//            print(cond.getCond(), indent+1);

			addExpr(cond.getCond(), cs, ms);

			//            printIndent(indent);
			//            System.out.println("then");
			//            print(cond.getTrue(), indent+1);
			addExpr(cond.getTrue(), cs, ms);

			//            printIndent(indent);
			//            System.out.println("else");
			//            print(cond.getFalse(), indent+1);
			addExpr(cond.getFalse(), cs, ms);

		}
		else if(e instanceof NewExpr) 
		{
			//            System.out.printf("new %s\n",((NewExpr)e).getType());
		}
		else if(e instanceof UnaryExpr) {
			UnaryExpr expr = (UnaryExpr)e;
			//            System.out.printf("unary %s\n", operator(expr.getOp()));
			//            print(expr.getValue(), indent + 1);
			addExpr(expr.getValue(), cs, ms);
		}
		else if(e instanceof BinaryExpr) {
			BinaryExpr expr = (BinaryExpr)e;
			//            System.out.printf("binary %s\n", operator(expr.getOp()));
			//            print(expr.getLeft(), indent + 1);   
			//            print(expr.getRight(), indent + 1);   
			addExpr(expr.getLeft(), cs, ms);   
			addExpr(expr.getRight(), cs, ms);   
		}
		else if (e instanceof CaseExpr) {
			CaseExpr caseExpr = ((CaseExpr)e);
			CaseScope cscp = new CaseScope(cs, ms);

			for(Case c : caseExpr.getCases()) {
				String id = c.getId();
				String type = c.getType();
				cscp.add(id, new Symbol(id, type, cscp));

				if(c.getValue() != null) {
					addExpr(c.getValue(), cscp, ms);
				}
			}

			addExpr(caseExpr.getValue(), cs, ms);
		}
		else if (e instanceof LetExpr) {

			LetExpr let = (LetExpr)e;
			LetScope lscp = new LetScope(cs, ms);

			for(Variable var : let.getDecl()) {

				String id = var.getId();
				String type = var.getType();
				lscp.add(id, new Symbol(id, type, lscp));

				if(var.getValue() != null) {
					addExpr(var.getValue(), lscp, ms);
				}
			}
			addExpr(let.getValue(), lscp, ms);

		}
		else if(e instanceof IdExpr) {
			//            System.out.printf("id %s\n", ((IdExpr)e).getId());
			//			System.out.println();


			String name = ((IdExpr)e).getId();
			if(cs.find(name) != null)
			{
				System.out.println("Duplicate var " + name);
				return;
			}


			if(findId(cs, name) == null)
			{
				System.out.println("Undefined variable " + name);
				return;
			}
		}
		else if(e instanceof ValueExpr) {
			Object value = ((ValueExpr)e).getValue();

			if(value instanceof String) {
				//				System.out.printf("str \"%s\"\n", ((String)value).replace("\n", "\\n")
				//						.replace("\t", "\\t").replace("\f", "\\f").replace("\b", "\\b"));
			}
			else if(value instanceof Integer) {
				//				System.out.printf("int %d\n", value);
			}
			else if(value instanceof Boolean) {
				//				System.out.printf("bool %s\n", value);
			}
			else {
				throw new RuntimeException(String.format("Unsupported constant type %s\n", e.getClass()));
			}
		}
		else {

			if( e != null) {
				throw new RuntimeException(String.format("Unsupported node type %s\n", e.getClass()));
			}
			else {
				throw new RuntimeException(String.format("Null node", e.getClass()));
			}

		}

	}




	private Symbol findId(Scope cs, String name) {
		// TODO Auto-generated method stub
		if(cs.find(name) != null){
			return cs.find(name);
		}else 
			if(cs instanceof MethodScope)
			{
				MethodScope m = (MethodScope)cs;

				Symbol s;
				if(( s = m.parameters.get(name)) != null)
				{
					return s;
				}
				return findId(m.parent, name);
			}
			else if(cs instanceof LetScope)
			{
				LetScope l = (LetScope)cs;
				return findId(l.parent, name);
			}
			else 
				if(cs instanceof CaseScope)
				{
					CaseScope c = (CaseScope)cs;
					return findId(c.parent, name);
				}
		return null;
	}


	/*
	public void print(){

		Enumeration<String> e = classes.keys();
		String clave, valor;

		while( e.hasMoreElements() ){
			clave = e.nextElement();

			ClassScope cs = classes.get( clave );
			valor = cs.getParent();

			System.out.print("Class " + clave);
			if( valor == null ){
				System.out.println(" : Object");
			} else {
				System.out.println(" : " + valor);
			}

			int indent = 2;

			print("fields", indent);
			for( Symbol s : cs.getSymbols().values() ) {

				if(!(s instanceof MethodSymbol)) {
					print(s.type + " " + s.id, indent+2);
				}
			}

			print("methods", indent);
			for( Symbol s : cs.getSymbols().values() ) {
				if(s instanceof MethodSymbol) {
					// manejo metodos
					print(s.type + " " + s.id, indent+3);

//					
//		            Method m = (Method)( cs.get(s.id));
//					
//		            for(Variable var: m.getParams()) {
//		                System.out.printf("%s %s -> ", var.getType(), var.getId());
//
//		                if( var.getValue() != null ){
//		                    throw new RuntimeException("WTF? initializing parameter definition?");
//		                }
//		            }


				}
			}



		}    	

	}

	public void print(String s, int indent) {
		String spaces = "";

		for (int i = indent; i > 0 ; i-- ) {
			spaces += ("  ");
		}  
		System.out.println(spaces + s);


	}

	 */

}