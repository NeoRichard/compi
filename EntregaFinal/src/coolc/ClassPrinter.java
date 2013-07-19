package coolc;

import java.beans.Expression;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import coolc.ast.*;


public class ClassPrinter {

	private Program _root;
	private boolean _printTypes;

	ArrayList<String>clases;
	String mainClass="";

	LinkedHashMap<String, String> stringList = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> intList = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> boolList = new LinkedHashMap<String, String>();

	LinkedHashMap<String, String> stringFieldList = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> intFieldList = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> boolFieldList = new LinkedHashMap<String, String>();

	LinkedHashMap<Integer, String> ifList = new LinkedHashMap<Integer, String>();
	private int ifCount = 0;

	private int stringCount = 0;
	private int intCount = 0;
	private int boolCount = 0;

	public ClassPrinter(Program root) {
		this(root, false);
	}

	public ClassPrinter(Program root, boolean printTypes) {
		_root = root;
		_printTypes = printTypes;
		clases = new ArrayList<>();


		ConstantPrinter constant = new ConstantPrinter(root);
		constant.print();

		this.stringList = constant.getStringList();
		this.intList = constant.getIntList();
		this.boolList = constant.getBoolList(); 

		this.stringFieldList = constant.getStringFieldList();
		this.intFieldList = constant.getIntFieldList();
		this.boolFieldList = constant.getBoolFieldList(); 

	}


	public void intType(String type){
		int N=0;
		if(type.equalsIgnoreCase("Int"))
			N = 32;
		else 
			if(type.equalsIgnoreCase("char"))
				N = 8;
			else 
				if(type.equalsIgnoreCase("bool"))
					N = 1;

		String content = "i"+N;
		System.out.println(content);
	}

	public void arrayType(int n, String T){
		String content = "["+n+" x "+T+"]";
		System.out.println(content);
	}

	public void pointerType(String T){
		String content = T+"* ";
		System.out.println(content);
	}

	public void compositeType(String[] T){
		String content = "{";
		if(T.length>0)
			content += T[0]+", ";
		for(int i = 1 ; i < T.length ; i++)
			content += T[i]+", ";

		content += "}";
		System.out.println(content);
	}

	/*
	public void callString(String s){
		String stringContent = s;
		int stringCount = 1;
		int stringLength = s.length()+1;

		String constantContent = "@.String.empty = constant ["+stringLength+" x i8] c"+stringContent+"\\00";

		String content = //("@msg"+stringCount+" = constant ["+(stringLength)+" x i8] c\""+stringContent+"\\00\"");    	
				"call i32 @puts(i8* bitcast( ["+stringLength+" x i8]* @.String.empty to i8* ))";
		System.out.println(content);
	}*/

	public String refactorType(String t){
		return "i32";
	}

	public void defineFunction(Method m) {
		/*
        System.out.printf("method %s : ", m.getName());
        for(Variable var: m.getParams()) {
            System.out.printf("%s %s -> ", var.getType(), var.getId());

            if( var.getValue() != null ){
                throw new RuntimeException("WTF? initializing parameter definition?");
            }
        }
        System.out.println(m.getType());
        print(m.getBody(), 3);*/

		String returnValue = m.getType();
		if(returnValue.equalsIgnoreCase("SELF_TYPE") || returnValue.equalsIgnoreCase("Object"))
			returnValue = mainClass;

		// %IO* @out_string(%IO* %self, i8* %x)



		String content = "define %"+returnValue+"* @"+mainClass+"_"+m.getName()+"(" ;
		if(m.getParams().size()>0){
			content += refactorType( (m.getParams().get(0).getType())) + " %"+ (m.getParams().get(0).getId());
		}else{
			content += ("%"+mainClass+"* %m");
		}
		for(int i = 0 ; i < m.getParams().size() ; i++)
			content += refactorType( (m.getParams().get(i).getType())) + " %"+ (m.getParams().get(i).getId());

		content +=") {\n";
		content += "    %_tmp_1 = bitcast %"+mainClass+"* %m to %IO*\n" ;
		System.out.println(content);

		print(m.getBody(), 3);
		System.out.println( "\n    ret %Main* %m");

		System.out.println("}");

	}
	/*

	public void defineFunction2(DispatchExpr m) {

		// %IO* @out_string(%IO* %self, i8* %x)

		String content = "%"+m.getType()+"* @"+m.getName()+"(" ;
		if(m.getArgs().size()>0){
			String paramType = (m.getArgs().get(0).getExprType());
			content += refactorType( paramType) + " %";
			if(paramType.equalsIgnoreCase("String"))
				content += stringList.get( m.getArgs().get(0) );
			for(int i = 1 ; i < m.getArgs().size() ; i++)
				//					content += refactorType( (m.getArgs().get(i).getExprType())) + " %"+ (m.getArgs().get(i).getId());
			{
				String paramType2 = (m.getArgs().get(i).getExprType());
				content += refactorType( paramType2) + " %";

			}
		}
		content +=		") {\n" + 
				"%_tmp_1 = add i32 %a, %b\n" + 
				"ret i32 %_tmp_1\n" + 
				"}";
		System.out.println(content);

	}
	 */
	public void defineBinary()
	{

	}
	public void defineAssign(Variable var, Object value)
	{

	}
	public void defineIf(Variable var, Object value)
	{
		//////////////////////////
		// REVISAR printTag()
		//////////////////////////
	}


	public void print() {
		//        System.out.println("program");
		/*
    	printVariables();
    	printMethods(mainClass);
		 */


		//		printClasses(c);
		printBasic();

		for(ClassDef c: _root) {   
			printClasses(c);    
			//			            print2(c);
			//			printClasses(c);
			//	    	printConstans(c);
		}
		printMain();        


		for(ClassDef c: _root) {   
			print(c);
		}


		printLibraries();
	}

	private void printBasic() {
		String content =
				"%Object = type { i8* }\n" +
						"%IO = type { i8* }\n";
		System.out.println(content);
	}

	private void printMain() {

		String content = "define i32 @main() {\n" +
				"    call %Main* @"+mainClass+"_main(%Main* null)\n"+
				"    ret i32 0\n"+
				"}";
		System.out.println(content);

	}

	private void printClasses(ClassDef c) {
		// printIndent(1);
		/*
        System.out.printf("class %s", c.getType());
        if( c.getSuper() != null ) {

        	System.out.printf(" : %s", c.getSuper());
        }
        System.out.println();
		 */
		mainClass = c.getType();
		String content = "%"+c.getType()+" = type { i8* }\n\n";
		System.out.println(content);
		/*
		for(Feature f: c.getBody()) {
			print(f);
		}
		 */
	}


	public void printLibraries(){
		String content = // "DESCOMENTAR";

				"declare %Object* @Object_abort(%Object*) \n" +
				"declare i8* @Object_type_name(%Object*) \n" +
				"declare %IO* @IO_out_string(%IO*, i8*) \n" +
				"declare %IO* @IO_out_int(%IO*, i32 ) \n" +
				"declare i8* @IO_in_string(%IO* )\n" +
				"declare i32 @IO_in_int(%IO* )\n" +
				"declare i32 @String_length(i8*) \n" +
				"declare i8* @String_concat(i8*, i8*) \n" +
				"declare i8* @String_substr(i8*, i32, i32 ) \n" +
				"declare i32 @strcmp(i8*, i8*)";

		System.out.println(content);
	}
	/*
	public void printMethods(String className){

		String s = "";
		String content =
				"define i32 @main() {\n" +
						"    call %"+className+"* @"+className+"_main(%"+className+"* null)\n" +
						"    ret i32 0\n" +
						"}\n";



//- <className>_main
		String mainMethod = 
				"define %Main* @"+className+"_main(%"+className+"* %m) {\n" +
						"    %_tmp_1 = bitcast %"+className+"* %m to %IO*\n" +
						"    call %IO* @IO_out_string(%IO* %_tmp_1, i8* bitcast ([13 x i8]* @msg to i8*))\n" +
						"    ret %Main* %m\n" +
						"}\n";
		System.out.println(content + "\n" + mainMethod);
	}
	 */

	private void print(ClassDef c) {
		// printIndent(1);
		//        System.out.printf("class %s", c.getType());
		if( c.getSuper() != null ) {
			//            System.out.printf(" : %s", c.getSuper());
		}
		//        System.out.println();
		for(Feature f: c.getBody()) {
			print(f);
		}
	}


	private void print(Feature f) {
		if(f instanceof Method) {
			Method m = (Method)f;
			// printIndent(2);
			defineFunction(m);
			/*
            System.out.printf("method %s : ", m.getName());
            for(Variable var: m.getParams()) {
                System.out.printf("%s %s -> ", var.getType(), var.getId());

                if( var.getValue() != null ){
                    throw new RuntimeException("WTF? initializing parameter definition?");
                }
            }
            System.out.println(m.getType());
            print(m.getBody(), 3);
			 */

		}
		else if (f instanceof Variable) {
			Variable var = (Variable)f;
			// printIndent(2);

			// FALTA AQUI: field Int count
			//			System.out.printf("field %s %s\n", var.getType(), var.getId());
			if( var.getValue() != null ) {
				//print(var.getValue(), 3);
			}
		}
		else {
			throw new RuntimeException("Unknown feature type " + f.getClass());
		}
	}

	public static void printIndent(int indent) {
		for (int i = indent; i > 0 ; i-- ) {
			System.out.print("    ");
		}  
	}

	public static void printIndent(int indent, StringBuilder sb) {
		for (int i = indent; i > 0 ; i-- ) {
			sb.append("    ");
		}
	}

	private void printTag(String tag, Expr e) {
		System.out.print(tag);
		if(_printTypes) {
			String type = e.getExprType();
			if(type != null) {
				//				System.out.printf(" [%s]", type);
			}
			else {
				//				System.out.print(" ERROR");
			}
		}
		System.out.println();
	}


	@SuppressWarnings("unchecked")
	private void print(Expr e, int indent) {

		assert e != null : "node is null";

		// printIndent(indent);

		if(e instanceof Block) {
			//			printTag("block", e);
			//			System.out.println("block");

			for(Expr child: ((Block)e).getStatements()) {
				print(child, indent+1);
			}
		}
		else if(e instanceof WhileExpr) {
			System.out.println("WhileExpr");
			WhileExpr loop = (WhileExpr)e;

			assert "Object".equals(loop.getExprType()) : "while type must be Object";

			printTag("while", e);

			print(loop.getCond(), indent+1);

			// printIndent(indent);
			System.out.println("loop");

			print(loop.getValue(), indent+1);


		}
		else if(e instanceof AssignExpr) {

			//			printTag(String.format("assign %s", ((AssignExpr)e).getId()), e);
			//			System.out.println("AssignExpr....");
			String id = "@"+( (AssignExpr)e ).getId();
			Expr ex = ( (AssignExpr)e ).getValue();
			if(ex instanceof BinaryExpr)
			{

				BinaryExpr binEx = (BinaryExpr)ex;

				System.out.println(";; BinaryExpr");		
				String type = binEx.getExprType();

				print((binEx), indent+1);
				System.out.println(";; <--BinaryExpr");


				if(type.equalsIgnoreCase("Int")){
					System.out.println("    store i32 %local_int"+intCount+", i32* "+id);
					intCount++;			
				}/*
				else if(type.equalsIgnoreCase("Bool")){
					System.out.println("%local_bool"+(++boolCount)+" = load i1* "+boolList.get(value));
					System.out.println("store i1 %local_bool"+(boolCount)+", i1* "+id);
					boolCount++;			
				} else if(type.equalsIgnoreCase("String")){
					System.out.println("%local_string"+(++stringCount)+" = load i8* "+stringList.get(value));
					System.out.println("store i8 %local_string"+(stringCount)+", i8* "+id);
					boolCount++;			
				}*/


			}else if(ex instanceof ValueExpr)
			{
				System.out.println(";; ValueExpr");
				Object value = ((ValueExpr) ex).getValue().toString();
				String type =  ( (AssignExpr)e ).getExprType();

				print(((AssignExpr)e).getValue(), indent+1);
				/*
				 * 
%local_int3 = load i32* @int1
store i32 %local_int3, i32* @entero
%local_int4 = load i32* @entero
				 */

				if(type.equalsIgnoreCase("Int")){
					System.out.println("%local_int"+(++intCount)+" = load i32* "+intList.get(value));
					System.out.println("store i32 %local_int"+(intCount)+", i32* "+id);
					intCount++;			
				} else if(type.equalsIgnoreCase("Bool")){
					System.out.println("%local_bool"+(++boolCount)+" = load i1* "+boolList.get(value));
					System.out.println("store i1 %local_bool"+(boolCount)+", i1* "+id);
					boolCount++;			
				} else if(type.equalsIgnoreCase("String")){
					System.out.println("%local_string"+(++stringCount)+" = load i8* "+stringList.get(value));
					System.out.println("store i8 %local_string"+(stringCount)+", i8* "+id);
					boolCount++;			
				}
			}

		}
		else if(e instanceof DispatchExpr) {

			DispatchExpr call = (DispatchExpr)e;
			// metodo
			StringBuilder out = new StringBuilder();

			//			out.append("call ").append(call.getName());
			String type = call.getExprType();
			//			System.out.println(type);

			String nameMethod = (call.getName());


			if(call.getType() != null) {
				out.append(" as ").append(call.getType());
			}
			printTag(out.toString(), e);

			//			defineFunction(call);

			if( call.getExpr() != null ) {
				// printIndent(indent+1);
				//				System.out.println("callee");
				print(call.getExpr(), indent+2);
			}
			if (call.getArgs().size() > 0) {
				// printIndent(indent+1);
				// System.out.println("args");
				for(Expr arg: call.getArgs()) {
					print(arg, indent+2);
				}
			}

			if(nameMethod.equalsIgnoreCase("out_string")){

				System.out.print("    call %IO* @IO_out_string(%IO* %_tmp_1, i8* %local_string"+stringCount+")");		
			}
			else if(nameMethod.equalsIgnoreCase("out_int")){

				String content = "";
				System.out.println("    call %IO* @IO_out_int(%IO* %_tmp_1, i32 %local_int"+intCount+")");	
				System.out.println(content);
			}
			else if(nameMethod.equalsIgnoreCase("in_string")){


				System.out.println( "    %local_string"+stringCount+" = call i8* @IO_in_string(%IO* %_tmp_1)");
				//					System.out.println("    call %IO* @IO_out_string(%IO* %_tmp_1, i8* %local_string"+stringCount+")");		
			}	
			else if(nameMethod.equalsIgnoreCase("in_int")){


				System.out.println( "    %local_int"+intCount+" = call i32 @IO_in_int(%IO* %_tmp_1)");
				//					System.out.println("    call %IO* @IO_out_string(%IO* %_tmp_1, i8* %local_string"+stringCount+")");		
			}						
			else{
				// METODOS DE STRINGS
				// FUNCIONES DE STRINGS
				// ESTA BIEN ?
				// %nuevo_string = @String_concat( i8* %<último string>, i8* %<penúltimo string>)
				if(nameMethod.equalsIgnoreCase("concat")){
					System.out.println("    %local_string"+(++stringCount)+" = call i8* @String_concat( i8* %local_string"+(stringCount-2)+", i8* %local_string"+(stringCount-1)+")");		
				}
				else if(nameMethod.equalsIgnoreCase("substr")){
					// ESTA BIEN EL ORDEN????
					System.out.println("    %local_string"+(stringCount+1)+" = call i8* @String_substr( i8* %local_string"+(stringCount)+", i32 %local_int"+(intCount)+", i32 %local_int"+(intCount-1)+")");		
					stringCount++;


				}
				else if(nameMethod.equalsIgnoreCase("length")){
					System.out.println("    %local_int"+(++intCount)+" = call i32 @String_length( i8* %local_string"+(stringCount)+")");		
				}

			}
		}
		else if(e instanceof IfExpr) {
			IfExpr cond = (IfExpr)e;



			//			printTag("if", e);
			print(cond.getCond(), indent+1);
			String type = cond.getExprType();
			int auxIf = ifCount++;

			String id = "%local_bool" + (boolCount);
			System.out.println("    %ptr_if"+auxIf+" = alloca i1");
			System.out.println("    store i1 "+id+", i1* %ptr_if"+auxIf+"");
			System.out.println("    %cond"+auxIf+" = load i1* %ptr_if"+auxIf+"");



			if(type.equalsIgnoreCase("Int")){
				System.out.println("    %ptr_int"+auxIf+" = alloca i32* ");
			} else if(type.equalsIgnoreCase("Bool")){
				System.out.println("    %ptr_bool"+auxIf+" = alloca i1* ");
			} else if(type.equalsIgnoreCase("String")){
				System.out.println("    %ptr_string"+auxIf+" = alloca i8*");
			}

			/*

//		    System.out.println("    %local_bool"+(boolCount+1)+" = load i1* %local_bool"+(boolList.get(key)));
			String value = boolList.get(id);

			System.out.println("    %cond"+auxIf+" = load i1* "+id);
			 */
			/*
			 * PENDIENTE REVISAR STRING
			 */
			String result = "";
			if(type.equalsIgnoreCase("Int")){
				result = "%v1 = alloca i32";
			}
			else if(type.equalsIgnoreCase("Bool")){
				result = "%v1 = alloca i1";
			}
			else if(type.equalsIgnoreCase("String")){
				// PENDIENTE STRING 
				//				result = "%v1 = alloca i32";
			}
			System.out.println(result);
			System.out.println("br i1 %cond"+auxIf+", label %IfEqual"+auxIf+", label %IfUnequal"+auxIf+"");
			System.out.println();

			System.out.println("IfEqual"+auxIf+":");
			print(cond.getTrue(), indent+1);
			System.out.println("");			

			if(type.equalsIgnoreCase("Int")){
				result = "    store i32 %local_int"+intCount+", i32* %v1\n";
				System.out.println(result);			
			}
			else if(type.equalsIgnoreCase("Bool")){
				result = "    store i1 %local_bool"+boolCount+", i1* %v1\n";
				System.out.println(result);			
			}
			else if(type.equalsIgnoreCase("String")){
				System.out.println("    store i8* %local_string"+stringCount+", i8** %ptr_string"+auxIf+"");
			}

			System.out.println("br label %EndIf"+auxIf);
			System.out.println();

			System.out.println("IfUnequal"+auxIf+":");
			print(cond.getFalse(), indent+1);
			System.out.println("");

			/*
			if(type.equalsIgnoreCase("Int")){
				result = "store i32 %local_int"+intCount+", i32* %v1\n";
			}
			else if(type.equalsIgnoreCase("Bool")){
				result = "store i1 %local_bool"+boolCount+", i1* %v1\n";
			}
			System.out.println(result);	
			 */
			if(type.equalsIgnoreCase("Int")){
				result = "store i32 %local_int"+intCount+", i32* %v1\n";
				System.out.println(result);			
			}
			else if(type.equalsIgnoreCase("Bool")){
				result = "store i1 %local_bool"+boolCount+", i1* %v1\n";
				System.out.println(result);			
			}else  if(type.equalsIgnoreCase("String")){	
				System.out.println("    store i8* %local_string"+stringCount+", i8** %ptr_string"+auxIf+"");
			}
			System.out.println("br label %EndIf"+auxIf);

			System.out.println("");

			System.out.println("EndIf"+auxIf+":");
			if(type.equalsIgnoreCase("String")){
				System.out.println("    %local_string"+(++stringCount)+" = load i8** %ptr_string"+auxIf+"");
			}
			else if(type.equalsIgnoreCase("Int")){
				result = "    %local_int"+(++intCount)+" = load i32*  %v1";
			}
			else if(type.equalsIgnoreCase("Bool")){
				result = "    %local_bool"+(++boolCount)+" = load i1*  %v1";
			}
			System.out.println(result);

			/*
			System.out.println("    %local_bool"+(boolCount +1)+" = load i1*  %local_bool"+(boolCount ));
			boolCount++;


			String variable = boolList.get(value.toString());
			System.out.println("    %local_bool"+(++boolCount )+" = load i1* "+variable);
//			System.out.println("    %local_bool"+(++boolCount )+" = load i1* "+id);

			// var0 = i1
			// si var0 = 1 -> goto IF_THEN1, goto ELSE1


			//			assert "Bool".equals(cond.getCond().getExprType());

			// printIndent(indent);
//			System.out.println("then");
			// REVISAR
//			print(cond.getTrue(), indent+1);

			// printIndent(indent);
//			System.out.println("else");
//			print(cond.getFalse(), indent+1);

			// IF_THEN1: 
			print(cond.getTrue(), indent+1);

			// goto END_IF1

			// ELSE:
			print(cond.getFalse(), indent+1);

			// END_IF1:
			// 



//			System.out.println("ES: "+(cond.getCond()));
			boolean b = true; // Boolean.parseBoolean(cond.getCond().toString());
			Expr ex = (cond.getCond());
			String type = ex.getExprType();
			 */


			/*		
			Object value = ((ValueExpr)e).getValue();

			if(value instanceof Boolean) {

				b = Boolean.parseBoolean( value.toString());
				if(b){
					print(cond.getTrue(), indent+1);
				}else{

					print(cond.getFalse(), indent+1);
				}
			}
			 */


		}
		else if(e instanceof NewExpr) 
		{
			NewExpr newExpr = (NewExpr)e;
			printTag(String.format("new %s",newExpr.getType()), e);

			//			assert newExpr.getType().equals(e.getExprType()) : String.format("Incompatible types %s %s", newExpr.getType(), e.getExprType());
		}
		else if(e instanceof UnaryExpr) {
			UnaryExpr expr = (UnaryExpr)e;

			printTag(String.format("unary %s", operator(expr.getOp())), e);
			print(expr.getValue(), indent + 1);
		}
		else if(e instanceof BinaryExpr) {
			BinaryExpr expr = (BinaryExpr)e;
			//			printTag(String.format("binary %s", operator(expr.getOp())), e);
			String op = operator(expr.getOp());
			//			System.out.println("OP: "+op);
			String operation = "";
			if(op.equalsIgnoreCase("+")){
				operation = "add";
			}else if(op.equalsIgnoreCase("-")){
				operation = "sub";
			}else if(op.equalsIgnoreCase("*")){
				operation = "mul";
			}else if(op.equalsIgnoreCase("/")){
				operation = "sdiv";
			}else if(op.equalsIgnoreCase("=")){

				if(expr.getLeft().getExprType().equals("String")){
					print(expr.getLeft(), indent + 1);   
					String var1 = "%local_string"+stringCount;
					print(expr.getRight(), indent + 1); 
					String var2 = "%local_string"+stringCount;

					String content = "%local_int"+(++intCount)+" = call i32 @strcmp(i8* "+var1+", i8*"+var2+")";
					System.out.println("    "+content);
					System.out.println(" %local_bool"+(++boolCount)+" = icmp eq i32 0, %local_int"+intCount);
					return;
				}
				else if(expr.getLeft().getExprType().equals("Int")){
					operation = "icmp eq";
					
				}

			}else if(op.equalsIgnoreCase("<")){
			
				print(expr.getLeft(), indent + 1);   
				String var1 = "%local_int"+intCount;
				print(expr.getRight(), indent + 1); 
				String var2 = "%local_int"+intCount;
								
				System.out.println("    %local_bool"+(++boolCount)+" = icmp slt i32 "+var1+",  "+var2+"");
				return;
			}
			else if(op.equalsIgnoreCase("<=")){
				
				print(expr.getLeft(), indent + 1);   
				String var1 = "%local_int"+intCount;
				print(expr.getRight(), indent + 1); 
				String var2 = "%local_int"+intCount;
								
				System.out.println("    %local_bool"+(++boolCount)+" = icmp sle i32 "+var1+",  "+var2+"");
				return;
			}

			print(expr.getLeft(), indent + 1);   
			String var1 = "%local_int"+intCount;
			print(expr.getRight(), indent + 1); 
			String var2 = "%local_int"+intCount;
			String content = "%local_int"+(++intCount)+" = "+operation+" i32 "+var1+", "+var2;
			System.out.println("    "+content);

		}
		else if (e instanceof CaseExpr) {
			CaseExpr caseExpr = ((CaseExpr)e);

			printTag("instanceof", e);
			print(caseExpr.getValue(), indent+1);

			for(Case c : caseExpr.getCases()) {
				// printIndent(indent+1);
				System.out.printf("case %s %s\n", c.getType(), c.getId());
				print(c.getValue(), indent+2);
			}

		}
		else if (e instanceof LetExpr) {
			LetExpr let = (LetExpr)e;
			printTag("let", e);

			// printIndent(indent+1);
			System.out.println("vars");
			for(Variable var : let.getDecl()) {
				// printIndent(indent+2);
				System.out.printf("%s %s\n", var.getType(), var.getId());
				if(var.getValue() != null) {
					print(var.getValue(), indent+3);
				}
			}

			print(let.getValue(), indent+1);
		}
		else if(e instanceof IdExpr) {
			//            printTag(String.format("id %s", ((IdExpr)e).getId()), e);
			String id = "@" + ((IdExpr)e).getId();
			String type = ((IdExpr)e).getExprType();

			//			System.out.println("ID: "+id + " es "+type);

			if(type.equalsIgnoreCase("Int")){
				System.out.println("    %local_int"+(++intCount )+" = load i32* "+id);
			}
			else if(type.equalsIgnoreCase("String")){
				// PREGUNTAR 
				String value = stringFieldList.get(id);
				//				System.out.println("id es: "+id);
				//				System.out.println("Value es: "+value);
				System.out.println("    %local_string"+(++stringCount )+" = getelementptr ["+(value.length()+1)+" x i8]* "+id+", i8 0, i8 0");

			}
			else if(type.equalsIgnoreCase("Bool")){
				// PREGUNTAR 
				String value = boolFieldList.get(id);
				System.out.println();
				System.out.println("    %local_bool"+(++boolCount )+" = load i1* "+id);
			}

			/*
			if(value instanceof String) {
				String variable = stringList.get(value);
				System.out.println("    %local_string"+(++stringCount )+" = getelementptr ["+(((String)(value)).length()+1)+" x i8]* "+variable+", i8 0, i8 0");


			}
			else if(value instanceof Integer) {
				String variable = intList.get( value.toString() );
			//	System.out.println("    %local_int"+(++intCount )+" = getelementptr ["+(value)+" x i8]* "+variable+", i8 0, i8 0");
				System.out.println("    %local_int"+(++intCount )+" = load i32* "+variable);



			}
			else if(value instanceof Boolean) {
				String variable = boolList.get(value.toString());
				System.out.println("    %local_bool"+(++boolCount )+" = load i1* "+variable);
			}
			 */
		}
		else if(e instanceof ValueExpr) {
			Object value = ((ValueExpr)e).getValue();
			// http://lists.cs.uiuc.edu/pipermail/llvmdev/2010-June/032621.html
			// PHI instruction
			// FORO			%.tmp_if_ret3 = phi %IO* [ %.tmp_out_ret3, %.ifTrue0 ], [ %.tmp_out_ret4, %.ifFalse0 ]

			if(value instanceof String) {


				int stringLength = (((String)(value)).length()+1);
				value =  ((String)value).replace("\n", "\\0A")
						.replace("\t", "\\09").replace("\f", "\\0c").replace("\b", "\\08");
				String variable = stringList.get(value);
				System.out.println("    %local_string"+(++stringCount )+" = getelementptr ["+stringLength+" x i8]* "+variable+", i8 0, i8 0");


				/*
				value =  ((String)value).replace("\n", "\\n")
						.replace("\t", "\\t").replace("\f", "\\f").replace("\b", "\\b");
				                System.out.println(String.format("str \"%s\"", value));
				 */
				//                printTag(String.format("str \"%s\"", value), e);
			}
			else if(value instanceof Integer) {
				String variable = intList.get( value.toString() );
				System.out.println("    %local_int"+(++intCount )+" = load i32* "+variable);
			}
			else if(value instanceof Boolean) {

				//                assert "Bool".equals(e.getExprType());
				//                printTag(String.format("bool %s", value), e);
				//            	System.out.println(String.format("bool %s", value)+ e);

				String variable = boolList.get(value.toString());
				System.out.println("    %local_bool"+(++boolCount )+" = load i1* "+variable);
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


	public String operator(UnaryOp op) {
		switch(op) {
		case ISVOID:    return "isvoid";
		case NEGATE:    return "~"; 
		case NOT:       return "not";
		}

		return null;
	}


	public String operator(BinaryOp op) {
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