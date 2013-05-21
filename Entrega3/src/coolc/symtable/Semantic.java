package coolc.symtable;

import coolc.parser.*;
import coolc.symtable.Semantic;
import coolc.symtable.SymTable;
import coolc.ast.*;

import java.io.*;
import java.util.*;

public class Semantic {

	static SymTable t;     

    

    public  Semantic(Program root) { 
    	t = new SymTable(root);     
    	print(root);
    	
    	
    }
    
    

    private static void print(Program root) {
//        System.out.println("program");
        for(ClassDef c: root) {            
            print(c);
        }
    }

    private static void print(ClassDef c) {

    	/*
		String id = c.getType();
	
		if(t.getClasses().get(id) != null)
		{
			System.out.println("Duplicate class "+id);
			return;
		}
		*/
		
        printIndent(1);
//        System.out.printf("class %s", c.getType());
        String superClass = c.getSuper();
        if( superClass != null ) {
//            System.out.printf(" : %s", c.getSuper());
        	checkErrorSuperClass(superClass);
        }
//        System.out.println();

        for(Feature f: c.getBody()) {
            print(f);
        }
    }

    private static void print(Feature f) {
        if(f instanceof Method) {

            Method m = (Method)f;
            printIndent(2);
//            System.out.printf("method %s : ", m.getName());
            for(Variable var: m.getParams()) {
    //            System.out.printf("%s %s -> ", var.getType(), var.getId());


            	String type = var.getType();
            	checkErrorClass(type);
            	
                if( var.getValue() != null ){
                    throw new RuntimeException("WTF? initializing parameter definition?");
                }
            }
    //        System.out.println(m.getType());

        	String type = m.getType();
        	checkErrorClass(type);

            print(m.getBody(), 3);

        }
        else if (f instanceof Variable) {
            Variable var = (Variable)f;
            String type = var.getType();
            checkErrorClass(type);
            
            
            printIndent(2);
//            System.out.printf("field %s %s\n", var.getType(), var.getId());
            
            if( var.getValue() != null ) {
                print(var.getValue(), 3);
            }
        }
        else {
            throw new RuntimeException("Unknown feature type " + f.getClass());
        }
        
    }

    private static void checkErrorClass(String type) {
		// TODO Auto-generated method stub

        if(!isBasic(type)){
        	if(t.classes.get(type) == null)
        	{
        		System.out.println(" Undefined class "+type);
        	}
        }
	}


    private static void checkErrorSuperClass(String s) {
		// TODO Auto-generated method stub

    	
        if(s.equalsIgnoreCase("String")  || s.equalsIgnoreCase("bool")
				|| s.equalsIgnoreCase("int")
				) {
        		System.out.println(" Can't inherit from "+s);
        }
	}



	private static boolean isBasic(String s) {
		// TODO Auto-generated method stub
		return s.equalsIgnoreCase("String")  || s.equalsIgnoreCase("bool")
				|| s.equalsIgnoreCase("int") || s.equalsIgnoreCase("Object")
				 || s.equalsIgnoreCase("IO") || s.equalsIgnoreCase("SELF_TYPE");
	}



	private static void printIndent(int indent) {
        for (int i = indent; i > 0 ; i-- ) {
            System.out.print("    ");
        }  
    }

    @SuppressWarnings("unchecked")
    private static void print(Expr e, int indent) {

        assert e != null : "node is null";

        printIndent(indent);

        if(e instanceof Block) {
    //        System.out.println("block");
            for(Expr child: ((Block)e).getStatements()) {
                print(child, indent+1);
            }
        }
        else if(e instanceof WhileExpr) {
            WhileExpr loop = (WhileExpr)e;

    //        System.out.println("while");
            print(loop.getCond(), indent+1);

            printIndent(indent);
    //        System.out.println("loop");
            print(loop.getValue(), indent+1);


        }
        else if(e instanceof AssignExpr) {
//            System.out.printf("assign %s\n", ((AssignExpr)e).getId());
            print(((AssignExpr)e).getValue(), indent+1);
        }        
        else if(e instanceof DispatchExpr) {
            DispatchExpr call = (DispatchExpr)e;

//            System.out.printf("call %s", call.getName());
            if(call.getType() != null) {
    //            System.out.printf(" as %s", call.getType());
            }
    //        System.out.println();
            if( call.getExpr() != null ) {
                printIndent(indent+1);
        //        System.out.println("callee");
                print(call.getExpr(), indent+2);
            }
            if (call.getArgs().size() > 0) {
                printIndent(indent+1);
        //        System.out.println("args");
                for(Expr arg: call.getArgs()) {
                    print(arg, indent+2);
                }
            }
        }
        else if(e instanceof IfExpr) {
            IfExpr cond = (IfExpr)e;

    //        System.out.println("if");
            print(cond.getCond(), indent+1);

            printIndent(indent);
    //        System.out.println("then");
            print(cond.getTrue(), indent+1);

            printIndent(indent);
    //        System.out.println("else");
            print(cond.getFalse(), indent+1);

        }
        else if(e instanceof NewExpr) 
        {
//            System.out.printf("new %s\n",((NewExpr)e).getType());

            String type = ((NewExpr)e).getType();
            checkErrorClass(type);
        }
        else if(e instanceof UnaryExpr) {
            UnaryExpr expr = (UnaryExpr)e;
//            System.out.printf("unary %s\n", operator(expr.getOp()));
            print(expr.getValue(), indent + 1);
        }
        else if(e instanceof BinaryExpr) {
            BinaryExpr expr = (BinaryExpr)e;
//            System.out.printf("binary %s\n", operator(expr.getOp()));
            print(expr.getLeft(), indent + 1);   
            print(expr.getRight(), indent + 1);   
        }
        else if (e instanceof CaseExpr) {
            CaseExpr caseExpr = ((CaseExpr)e);
    //        System.out.println("instanceof");
            print(caseExpr.getValue(), indent+1);

            for(Case c : caseExpr.getCases()) {
                String type = c.getType();
                checkErrorClass(type);

                printIndent(indent+1);
    //            System.out.printf("case %s %s\n", c.getType(), c.getId());
                print(c.getValue(), indent+2);
            }

        }
        else if (e instanceof LetExpr) {
            LetExpr let = (LetExpr)e;
    //        System.out.println("let");
            printIndent(indent+1);
    //        System.out.println("vars");
            for(Variable var : let.getDecl()) {
                printIndent(indent+2);
    //            System.out.printf("%s %s\n", var.getType(), var.getId());
                String type = var.getType();
                checkErrorClass(type);

                
                
                if(var.getValue() != null) {
                    print(var.getValue(), indent+3);
                }
            }

            print(let.getValue(), indent+1);
        }
        else if(e instanceof IdExpr) {
//            System.out.printf("id %s\n", ((IdExpr)e).getId());
        }
        else if(e instanceof ValueExpr) {
            Object value = ((ValueExpr)e).getValue();

            if(value instanceof String) {
    //            System.out.printf("str \"%s\"\n", ((String)value).replace("\n", "\\n")
    //                .replace("\t", "\\t").replace("\f", "\\f").replace("\b", "\\b"));
            }
            else if(value instanceof Integer) {
    //            System.out.printf("int %d\n", value);
            }
            else if(value instanceof Boolean) {
    //            System.out.printf("bool %s\n", value);
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




}  