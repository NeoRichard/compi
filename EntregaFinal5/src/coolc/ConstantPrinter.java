package coolc;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import coolc.ast.*;


public class ConstantPrinter {

	private Program _root;
	private boolean _printTypes;

	ArrayList<String>clases;
	String mainClass="Main";

	public LinkedHashMap<String, String> getStringList() {
		return stringList;
	}

	public LinkedHashMap<String, String> getIntList() {
		return intList;
	}

	public LinkedHashMap<String, String> getBoolList() {
		return boolList;
	}


	int stringCount = 0;
	int intCount = 0;
	int boolCount = 0;

	LinkedHashMap<String, String> stringList = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> intList = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> boolList = new LinkedHashMap<String, String>();

	LinkedHashMap<String, String> stringFieldList = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> intFieldList = new LinkedHashMap<String, String>();
	LinkedHashMap<String, String> boolFieldList = new LinkedHashMap<String, String>();

	public ConstantPrinter(Program root) {
		this(root, false);
	}

	public LinkedHashMap<String, String> getStringFieldList() {
		return stringFieldList;
	}

	public LinkedHashMap<String, String> getIntFieldList() {
		return intFieldList;
	}

	public LinkedHashMap<String, String> getBoolFieldList() {
		return boolFieldList;
	}

	public ConstantPrinter(Program root, boolean printTypes) {
		_root = root;
		_printTypes = printTypes;
		clases = new ArrayList<>();



	}


	public void defineInt(String value){


		String prefix = "@int";
		String content = prefix+(++intCount)+" = global i32 " + value;
		System.out.println(content);
		intList.put(value, prefix+intCount);
	}

	public void defineBool(String value){
		String prefix = "@bool";

		if(value.equalsIgnoreCase("true")){
			System.out.println(prefix+(++boolCount)+" = global i1 1");
		}else{
			System.out.println(prefix+(++boolCount)+" = global i1 0");
		}
		boolList.put(value, "@"+value);

	}

	public void printVariables(){

		String content =
				"%Object = type { i8* }\n" +
						"%IO = type { i8* }\n\n" +

"%"+mainClass+" = type { i8* }\n";


		System.out.println(content);

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


	public void callString(String s){


		String stringContent = s;
		int stringCount = 1;
		int stringLength = s.length()+1;

		String constantContent = "@.String.empty = constant ["+stringLength+" x i8] c"+stringContent+"\\00";

		String content = //("@msg"+stringCount+" = constant ["+(stringLength)+" x i8] c\""+stringContent+"\\00\"");    	
				"call i32 @puts(i8* bitcast( ["+stringLength+" x i8]* @.String.empty to i8* ))";
		System.out.println(content);
	}

	public String refactorType(String t){
		return "i32";
	}

	public void defineFunction2(String returnValue, Method m) {

// %IO* @out_string(%IO* %self, i8* %x)

		String content = "%"+returnValue+"* @"+m.getName()+"(" ;
		if(m.getParams().size()>0)
			content += refactorType( (m.getParams().get(0).getType())) + " %"+ (m.getParams().get(0).getId());

		for(int i = 0 ; i < m.getParams().size() ; i++)
			content += refactorType( (m.getParams().get(i).getType())) + " %"+ (m.getParams().get(i).getId());

		content +=		") {\n" + 
				"%_tmp_1 = add i32 %a, %b\n" + 
				"ret i32 %_tmp_1\n" + 
				"}";
		System.out.println(content);

	}

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

		System.out.println("@true = global i1 1\n"+
							"@false = global i1 0");
		for(ClassDef c: _root) {            
			            print(c);
//			printClasses(c);
//	    	printConstans(c);


		}
		//        printLibraries();
	}

	private void printClasses(ClassDef c) {
		String className="";
		String text =
				"%Object = type { i8* }\n" +
						"%IO = type { i8* }\n" +
						"%"+className+" = type { i8* }\n";
		int mainIndex = -1;
		// printIndent(1);
		/*
        System.out.printf("class %s", c.getType());
        if( c.getSuper() != null ) {

        	System.out.printf(" : %s", c.getSuper());
        }
        System.out.println();
		 */

		for(Feature f: c.getBody()) {
			print(f);
		}
	}

	private void printConstans(ClassDef c) {
		String className="";
		String text =
				"%Object = type { i8* }\n" +
						"%IO = type { i8* }\n" +
						"%"+className+" = type { i8* }\n";
		int mainIndex = -1;
		// printIndent(1);
		/*
        System.out.printf("class %s", c.getType());
        if( c.getSuper() != null ) {

        	System.out.printf(" : %s", c.getSuper());
        }
        System.out.println();
		 */

		for(Feature f: c.getBody()) {
			print(f);
		}
	}

	public void printLibraries(){
		String content = 
				"declare %Object* @Object_abort(%Object*) \n" +
						"declare i8* @Object_type_name(%Object*) \n" +
						"declare %IO* @IO_out_string(%IO*, i8*) \n" +
						"declare %IO* @IO_out_int(%IO*, i32 ) \n" +
						"declare i8* @IO_in_string(%IO* \n" +
						"declare i32 @IO_in_int(%IO* \n" +
						"declare i32 @String_length(i8*) \n" +
						"declare i8* @String_concat(i8*, i8*) \n" +
						"declare i8* @String_substr(i8*, i32, i32 ) ";
		System.out.println(content);
	}
	public void printMethods(String className){

		/** METODOS: 
- main
		 */
		String s = "";
		String content =
				"define i32 @main() {\n" +
						"    call %"+className+"* @"+className+"_main(%"+className+"* null)\n" +
						"    ret i32 0\n" +
						"}\n";


		/** 
- <className>_main
		 */
		String mainMethod = 
				"define %Main* @"+className+"_main(%"+className+"* %m) {\n" +
						"    %_tmp_1 = bitcast %"+className+"* %m to %IO*\n" +
						"    call %IO* @IO_out_string(%IO* %_tmp_1, i8* bitcast ([13 x i8]* @msg to i8*))\n" +
						"    ret %Main* %m\n" +
						"}\n";
		System.out.println(content + "\n" + mainMethod);
	}


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
			//            System.out.printf("method %s : ", m.getName());
			for(Variable var: m.getParams()) {
				//                System.out.printf("%s %s -> ", var.getType(), var.getId());

				if( var.getValue() != null ){
					throw new RuntimeException("WTF? initializing parameter definition?");
				}
			}
			//            System.out.println(m.getType());

			print(m.getBody(), 3);

		}
		else if (f instanceof Variable) {
			Variable var = (Variable)f;
			/*
			// printIndent(2);
			System.out.printf("field %s %s\n", var.getType(), var.getId());
			if( var.getValue() != null ) {
				print(var.getValue(), 3);
			}*/

			// FALTA AQUI: field Int count
//			System.out.printf("field %s %s\n", var.getType(), var.getId());
			if( var.getValue() != null ) {
				//print(var.getValue(), 3);
			}

			String type = var.getType();
			if(type.equalsIgnoreCase("String")){
				String id = "@"+var.getId();
				String s = "";
				if( var.getValue() != null ) {
//					print(var.getValue(), 3);
					ValueExpr v = (ValueExpr)var.getValue();
					s = v.getValue().toString();
				}
				String stringContent = s;
				int stringLength = s.length() + 1;
//				String content = id + (" = constant ["+(stringLength)+" x i8] c\""+stringContent+"\\00\"");


            	System.out.println("@" + var.getId() + "_c = constant [" + stringLength + " x i8] c\"" + stringContent + "\\00\"");
            	System.out.println("@" + var.getId() + " = global i8* bitcast([" + stringLength + " x i8]* @" + var.getId() + "_c to i8*)");

//				System.out.println(content);
				stringFieldList.put(id, s);

			}else if(type.equalsIgnoreCase("Int")){	

				String id = "@"+var.getId();
				String s = "0";
				if( var.getValue() != null ) {
//					print(var.getValue(), 3);
					ValueExpr v = (ValueExpr)var.getValue();
					s = v.getValue().toString();
				}

				String content = id +" = global i32 " + s;
				System.out.println(content);
				intFieldList.put(id, s);
//				System.out.println("ID: "+id+" - "+s);

			}
			else if(type.equalsIgnoreCase("Bool")){
				String id = "@"+var.getId();
				String s = "0";
				if( var.getValue() != null ) {
//					print(var.getValue(), 3);
					ValueExpr v = (ValueExpr)var.getValue();
					s = v.getValue().toString();
				}
				String content = id +" = global i1 " + s;
				System.out.println(content);
				boolFieldList.put(id, s);

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
				System.out.printf(" [%s]", type);
			}
			else {
				System.out.print(" ERROR");
			}
			System.out.println();
		}
	}


	@SuppressWarnings("unchecked")
	private void print(Expr e, int indent) {

		assert e != null : "node is null";

		// printIndent(indent);

		if(e instanceof Block) {
//			printTag("block", e);

			for(Expr child: ((Block)e).getStatements()) {
				print(child, indent+1);
			}
		}
		else if(e instanceof WhileExpr) {
			WhileExpr loop = (WhileExpr)e;

			assert "Object".equals(loop.getExprType()) : "while type must be Object";

//			printTag("while", e);

			print(loop.getCond(), indent+1);

			// printIndent(indent);
//			System.out.println("loop");

			print(loop.getValue(), indent+1);


		}
		else if(e instanceof AssignExpr) {

//			printTag(String.format("assign %s", ((AssignExpr)e).getId()), e);

			print(((AssignExpr)e).getValue(), indent+1);
		}        
		else if(e instanceof DispatchExpr) {
			DispatchExpr call = (DispatchExpr)e;
// metodo
			StringBuilder out = new StringBuilder();

//			out.append("call ").append(call.getName());
			String type = call.getExprType();
//			System.out.println(type);
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
		}
		else if(e instanceof IfExpr) {
			IfExpr cond = (IfExpr)e;

//			printTag("if", e);
			print(cond.getCond(), indent+1);

//			assert "Bool".equals(cond.getCond().getExprType());

			// printIndent(indent);
//			System.out.println("then");
			print(cond.getTrue(), indent+1);

			// printIndent(indent);
//			System.out.println("else");
			print(cond.getFalse(), indent+1);

		}
		else if(e instanceof NewExpr) 
		{
			NewExpr newExpr = (NewExpr)e;
			
			// AQUI SE IMPRIME NEW FIELD new IO
//			printTag(String.format("new %s",newExpr.getType()), e);

//			assert newExpr.getType().equals(e.getExprType()) : String.format("Incompatible types %s %s", newExpr.getType(), e.getExprType());
		}
		else if(e instanceof UnaryExpr) {
			UnaryExpr expr = (UnaryExpr)e;

//			printTag(String.format("unary %s", operator(expr.getOp())), e);
			print(expr.getValue(), indent + 1);
		}
		else if(e instanceof BinaryExpr) {
			BinaryExpr expr = (BinaryExpr)e;
//			printTag(String.format("binary %s", operator(expr.getOp())), e);

			print(expr.getLeft(), indent + 1);   
			print(expr.getRight(), indent + 1);
		}
		else if (e instanceof CaseExpr) {
			CaseExpr caseExpr = ((CaseExpr)e);

//			printTag("instanceof", e);
			print(caseExpr.getValue(), indent+1);

			for(Case c : caseExpr.getCases()) {
				// printIndent(indent+1);
				print(c.getValue(), indent+2);
			}

		}
		else if (e instanceof LetExpr) {
			LetExpr let = (LetExpr)e;
			printTag(";; let", e);

			// printIndent(indent+1);
			System.out.println(";;; vars");
			for(Variable var : let.getDecl()) {
				// printIndent(indent+2);
				System.out.printf(";;;;; -> %s %s\n", var.getType(), var.getId());
				if(var.getValue() != null) {
					print(var.getValue(), indent+3);
				}
			}

			print(let.getValue(), indent+1);
		}
		else if(e instanceof IdExpr) {
			//            printTag(String.format("id %s", ((IdExpr)e).getId()), e);
		}
		else if(e instanceof ValueExpr) {
			Object value = ((ValueExpr)e).getValue();

			if(value instanceof String) {
/*
				value =  ((String)value).replace("\n", "\\n")
						.replace("\t", "\\t").replace("\f", "\\f").replace("\b", "\\b");
				*/

				int stringLength = ((String)value).length() + 1;
				value =  ((String)value).replace("\n", "\\0A")
						.replace("\t", "\\09").replace("\f", "\\0c").replace("\b", "\\08");


				String stringContent = (String) value;

				String prefix = "@str";


//				String content = (prefix+(++stringCount)+" = constant ["+(stringLength)+" x i8] c\""+stringContent+"\\00\"\n");
				stringCount++;
            	System.out.println(prefix+(stringCount) + "_c = constant [" + stringLength + " x i8] c\"" + stringContent + "\\00\"");
            	System.out.println(prefix+(stringCount) + " = global i8* bitcast([" + stringLength + " x i8]* "+prefix+(stringCount) + "_c to i8*)");

				
				
				stringList.put(stringContent, prefix+stringCount);
//				System.out.println(content);
				//                System.out.println(String.format("str \"%s\"", value));

				//                printTag(String.format("str \"%s\"", value), e);
			}
			else if(value instanceof Integer) {

				//                assert "Int".equals(e.getExprType());
				//                printTag(String.format("int %d", value), e);
				//            	System.out.println(String.format("int %d", value)+ e);
				defineInt(String.format("%d", value));

			}
			else if(value instanceof Boolean) {
				//                assert "Bool".equals(e.getExprType());
				//                printTag(String.format("bool %s", value), e);
				//            	System.out.println(String.format("bool %s", value)+ e);
				defineBool(String.format("%s", value));



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