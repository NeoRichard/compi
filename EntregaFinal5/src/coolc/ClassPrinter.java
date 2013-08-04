package coolc;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import org.omg.CORBA.IdentifierHelper;

import coolc.ast.*;
import coolc.infrastructure.*;


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
	private int caseCount = 0;

	private int stringCount = 0;
	private int intCount = 0;
	private int boolCount = 0;
	private int ptrCount = 0;
	private int whileCount = 0;

	private int stringLetCount = 0;
	private int intLetCount = 0;
	private int boolLetCount = 0;
	private int ptrLetCount = 0;

	String namelocalvars = "local_string";
	String namelocalvari = "local_int";
	String namelocalvarb = "local_bool";
	String namelocalvarp = "local_ptr";

	String nameletvars = "let_string";
	String nameletvari = "let_int";
	String nameletvarb = "let_bool";
	String nameletvarp = "let_ptr";

	ClassDef currentClass = null;
	String currentClassName = "";

	private SymTable _symTable;
	ClassScope classScope;
	MethodScope methodScope;
	Scope currentScope;
	private String currentPtr;

	public ClassPrinter(Program root) {
		this(root, false);
	}

	public ClassPrinter(Program root, boolean printTypes) {
		_root = root;
		_printTypes = printTypes;
		clases = new ArrayList<>();


		_symTable = new SymTable(root);

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

	public String getCurrentSuperClass(){
		return "%" + currentClass.getSuper() + "*";
	}
	public String refactorType(String t){
		if (t.equalsIgnoreCase("Int") ){
			return "i32";
		}else if (t.equalsIgnoreCase("Bool") ){
			return "i1";
		}else if (t.equalsIgnoreCase("String") ){
			return "i8*";
		}
		else if(t.equals("SELF_TYPE")){
			return "%" + currentClassName + "*";
		}
		return "%" + t + "*";
	}


	public void defineMethod(Method m) {
		String methodName = m.getName();
		methodScope = classScope.getMethod(methodName);
		currentScope = methodScope;

		String returnValue = m.getType();
		if(returnValue.equalsIgnoreCase("SELF_TYPE") ){
			returnValue = "%"+ currentClassName +"*";			
		}else if (returnValue.equalsIgnoreCase("Int") ){
			returnValue = "i32";
		}else if (returnValue.equalsIgnoreCase("Bool") ){
			returnValue = "i1";
		}else if (returnValue.equalsIgnoreCase("String") ){
			returnValue = "i8*";
		}else{
			if(returnValue.equalsIgnoreCase("Object"))
			{
				returnValue = "%Object*";	
			}else{
				returnValue = "%"+ returnValue +"*";	
			}
		}
		if(methodName.equalsIgnoreCase("main")){	
			returnValue = "%"+ currentClassName +"*";	
		}

		String content = "define "+returnValue+" @"+ currentClassName +"_"+m.getName()+"(" ;
		content += "%"+ currentClassName +"* %m";
		String pars = "";

		if(m.getParams().size()>0){
			for(int i = 0 ; i < m.getParams().size() ; i++){
				String p = (m.getParams().get(i).getType());
				pars += ", "+ refactorType( p) + " %"+ (m.getParams().get(i).getId())  ;
			}
		}
		content += pars;

		content +=") {\n";
		content += "    %_tmp_1 = bitcast %"+ currentClassName +"* %m to %Object*" ;

		System.out.println(content);
		int countParam = 0;

		if(m.getParams().size()>0){
			for(int i = 0 ; i < m.getParams().size() ; i++){

				String type = m.getParams().get(i).getType();
				if (type.equalsIgnoreCase("Int") ){

					System.out.println("\n    %ptr_param"+countParam+" = alloca i32" +
							"\n    store i32 %"+ (m.getParams().get(i).getId())+", i32* %ptr_param"+countParam +
							"\n    %local_int"+(++intCount)+" = load i32* %ptr_param"+countParam);			

				}else if (type.equalsIgnoreCase("Bool") ){
					System.out.println("\n    %ptr_param"+countParam+" = alloca i1" +
							"\n    store i1 %"+ (m.getParams().get(i).getId())+", i1* %ptr_param"+countParam +
							"\n    %local_bool"+(++boolCount)+" = load i1* %ptr_param"+countParam);
				}else if (type.equalsIgnoreCase("String") ){
					System.out.println("\n    %ptr_param"+countParam+" = alloca i8*" +
							"\n    store i8* %"+ (m.getParams().get(i).getId())+", i8** %ptr_param"+countParam +
							"\n    %local_str"+(++stringCount)+" = load i8** %ptr_param"+countParam);
				}else{
					System.out.println("\n    %ptr_param"+countParam+" = alloca %"+type+"*" +
							"\n    store %"+type+"* %"+ (m.getParams().get(i).getId())+", %"+type+"** %ptr_param"+countParam +
							"\n    %local_ptr"+(++ptrCount)+" = load %"+type+"** %ptr_param"+countParam);
				}
				countParam++;

				content += ", "+ refactorType( (m.getParams().get(i).getType())) + " %"+ (m.getParams().get(i).getId());
			}
		}

		print(m.getBody(), 3);

		if (m.getType().equalsIgnoreCase("Int") ){
			System.out.println( "\n    ret i32 %local_int"+intCount);
		}else if (m.getType().equalsIgnoreCase("Bool") ){
			System.out.println( "\n    ret i1 %local_bool"+boolCount);
		}else if (m.getType().equalsIgnoreCase("String") ){
			System.out.println( "\n    ret i8* %local_string"+stringCount);
		}else{
			if(m.getParams() != null ){
				if( m.getParams().size() > 0){
					if(!m.getParams().get(0).getType().equalsIgnoreCase(returnValue)){
						System.out.println("    %_1 = bitcast %"+ currentClassName +"* %m to " + returnValue );
						System.out.println( "\n    ret "+ returnValue +" %_1");		
						System.out.println("}");

						return;
					}
				}
				System.out.println( "\n    ret "+ returnValue +" %m");	
				System.out.println("}");

				return;
			}

		}
		System.out.println("}");
	}

	public void print() {

		printBasic();

		for(ClassDef c: _root) {  
			currentClass = c;
			currentClassName = c.getType();
			printClasses(c);
		}
		printIO();
		printMain();

		for(ClassDef c: _root) {   
			currentClass = c;
			currentClassName = c.getType();
			if(c != null)
				print(c);
		}

		printLibraries();
	}

	private void printIO() {
		System.out.println();
		System.out.println("define %IO* @newIO() {");
		System.out.println("    %vptr = call i8* @malloc( i64 ptrtoint (%IO* getelementptr (%IO* null, i32 1) to i64) )");
		System.out.println("    %ptr = bitcast i8* %vptr to %IO*");
		System.out.println("    %typePtr = getelementptr %IO* %ptr, i32 0, i32 0");
		System.out.println("    store i8* bitcast( [3 x i8]* @.type.IO to i8*), i8** %typePtr");
		System.out.println("    ret %IO* %ptr");
		System.out.println("}");
		System.out.println();
	}

	private void printBasic() {
		String content =
				"%Object = type { i8* }\n" +
						"%IO = type { i8* }\n" +
						"@.type.IO = private constant [3 x i8] c\"IO\\00\"";
		System.out.println(content);
	}

	private void printMain() {

		String content = "define i32 @main() {\n" +
				"    %ptr = call %Program* @newProgram() \n" +
				"    call %"+mainClass+"* @"+mainClass+"_main(%"+mainClass+"* %ptr)\n"+
				"    ret i32 0\n"+
				"}";
		System.out.println(content);

	}

	private void printClasses(ClassDef c) {

		String myClass = c.getType();

		classScope = _symTable.findClass(c.getType());
		if(classScope.getMethod("main") != null){
			mainClass = myClass;
		}

		int lengthClass = myClass.length() + 1;
		System.out.println("@.type."+myClass+" = private constant ["+lengthClass+" x i8] c\""+myClass+"\\00\"");
		System.out.println();

		System.out.println("%" + myClass + "= type {");

		ArrayList<String> typeList = new ArrayList<>();
		typeList.add("i8*");

		for(Feature f: c.getBody()) {
			if(f instanceof Variable){
				Variable variable = (Variable)f;
				String type = variable.getType();
				if(type.equalsIgnoreCase("Int")){
					type = "i32";
				}else if(type.equalsIgnoreCase("String")){
					type = "i8*";
				}else if(type.equalsIgnoreCase("Bool")){
					type = "i1";
				}else{
					type = "%"+type+"*";
				}
				typeList.add(type);				
			}
		}

		System.out.print("    "+typeList.get(0));

		for(int i = 1 ; i < typeList.size() ; i++){
			System.out.print(",\n    "+typeList.get(i));        	
		}        
		System.out.println("\n};");

		if( c.getSuper() != null ) {
			//System.out.printf(" : %s", c.getSuper());
		}
		System.out.println();

		String baseClass = "define %"+myClass+"* @new"+myClass+"() {" +
				"\n    %vptr = call i8* @malloc( i64 ptrtoint (%"+myClass+"* getelementptr (%"+myClass+"* null, i32 1) to i64) )" +
				"\n    %ptr = bitcast i8* %vptr to %"+myClass+"* " +
				"\n    %typePtr = getelementptr %"+myClass+"* %ptr, i32 0, i32 0" +
				"\n    store i8* bitcast( ["+lengthClass+" x i8]* @.type."+myClass+" to i8*), i8** %typePtr" +
				"\n    ; inicializacion de los los fields del objeto recién creado";
		System.out.println(baseClass);
		for(Feature f: c.getBody()) {
			if (f instanceof Variable){

				printFields( (Variable)f );
			}
		}
		System.out.println(
				"    ret %"+myClass+"* %ptr" +
				"\n}");

	}


	private void printFields(Variable var) {

		if(var.getValue() instanceof NewExpr){
			String type = var.getType();;
			NewExpr valueexpr = (NewExpr)var.getValue();
			Field field = classScope.getField(var.getId());
			int index = field.index;
			String value = valueexpr.getExprType();
			System.out.println("; inicializando Objeto de tipo: " + type + " con valor " + value);

			if(type.equalsIgnoreCase("Int"))
			{
				String nextvar = getNextLocalInt();
				System.out.println("    %" + nextvar + " = getelementptr inbounds %" + currentClassName +"* %ptr, i32 0, i32 " + index);
				System.out.println("    store i32 " + value + ", %" + field.getType() +"** %" + nextvar );		
			}
			else if(type. equalsIgnoreCase("Bool"))	{
				String nextvar = getNextLocalBool();
				System.out.println("    %" + nextvar + " = getelementptr inbounds %" + currentClassName +"* %ptr, i32 0, i32 " + index);
				System.out.println("    store i1 " + value + ", %" + field.getType() +"** %" + nextvar );
			}
			else if(type.equalsIgnoreCase("String")){
				System.out.println("    %" + getNextLocalString() + " = getelementptr inbounds %" + currentClassName +"* %ptr, i32 0, i32 " + index);
				System.out.println("    %ptr_"+ index +" = call %" + field.getType() +"* @new" + field.getType() + "();");
				System.out.println("    store %" + field.getType() +"* %ptr_" + index + ", %" + field.getType() +"** %" + getLocalString() );

			}
			else{
				String nextvar = getNextLocalPtr();
				System.out.println("    %" + nextvar + " = getelementptr inbounds %" + currentClassName +"* %ptr, i32 0, i32 " + index);
				System.out.println("    %ptr_"+ index +" = call %" + field.getType() +"* @new" + field.getType() + "();");
				System.out.println("    store %" + field.getType() +"* %ptr_" + index + ", %" + field.getType() +"** %" + nextvar );
			}
		}
		else //if(var.getValue() instanceof ValueExpr)
		{
			Field field = classScope.getField(var.getId());
			String value = "0";
			String type = var.getType();;
			if(type.equalsIgnoreCase("Int")){
				System.out.println("; inicializando Int");
				if(var.getValue() != null){
					value = var.getValue().toString();
				}
				System.out.println("    %"+getNextLocalInt() + " = getelementptr inbounds %"+ currentClassName + "* %ptr, i32 0, i32 "+field.index);
				System.out.println("    store i32 "+value +", i32* %" + getLocalInt());

			}

			else if(var.getType().equals("String")){
				System.out.println("; inicializando String");
				if( var.getValue() != null ) {
					value = var.getValue().toString();
					if(var.getValue() instanceof ValueExpr)
					{
						Object o = ((ValueExpr)var.getValue()).getValue();
						if(o instanceof String) {							
							value = o.toString();
						}
						System.out.println("; o: "+o.toString());
					}
				}
				else{
					if(var.getId() != null){

						if(var.getValue() == null)
							value = "";
						else{
							value = var.getValue().toString();
						}
					}
					else{
						value = "";
					}
				}
				int stringLength = value.length() + 1;
				System.out.println("    %" + getNextLocalString() + " = getelementptr inbounds %" + currentClassName +"* %ptr, i32 0, i32 " + field.index);
				System.out.println("    store i8* bitcast( [" + stringLength + " x i8]* @" + var.getId() +"_c to i8*), i8** %" + getLocalString());

			}

			else if(var.getType().equals("Bool")){
				System.out.println("; inicializando bool");
				if( var.getValue() != null ) {

					ValueExpr valueexpr = (ValueExpr)var.getValue();

					value = valueexpr.getValue().toString();
					if(value.equalsIgnoreCase("true")){
						value ="1";
					}else{
						value = "0";
					}
				}
				else{
					value = "0";
				}
				System.out.println("    %" + getNextLocalBool() + " = getelementptr inbounds %" + currentClassName +"* %ptr, i32 0, i32 " + field.index);
				System.out.println("    store i1 " + value + ", i1* %" + getLocalBool());
			}

			else{
//				System.out.println("; inicializando POR DEFAULT Objeto de tipo: "+type);

			}
		}
	}


	public String getLetInt(){
		return nameletvari + intLetCount;
	}

	public String getNextLetInt(){
		return nameletvari + ++intLetCount;
	}

	public String getLetString(){
		return nameletvars + stringLetCount;
	}

	public String getNextLetString(){
		return nameletvars + ++stringLetCount;
	}

	public String getLetBool(){
		return nameletvarb + boolLetCount;
	}

	public String getNextLetBool(){
		return nameletvarb + ++boolLetCount;
	}

	public String getLetPtr(){
		String s = nameletvarp + ptrLetCount;
		currentPtr = s;
		return s;
	}

	public String getNextLetPtr(){
		return nameletvarp + ++ptrLetCount;
	}

	public String getLocalInt(){
		return namelocalvari + intCount;
	}

	public String getNextLocalInt(){
		return namelocalvari + ++intCount;
	}

	public String getLocalString(){
		return namelocalvars + stringCount;
	}

	public String getNextLocalString(){
		return namelocalvars + ++stringCount;
	}

	public String getLocalBool(){
		return namelocalvarb + boolCount;
	}

	public String getNextLocalBool(){
		return namelocalvarb + ++boolCount;
	}

	public String getLocalPtr(){
		String s = namelocalvarp + ptrCount;
		currentPtr = s;
		return s;
	}

	public String getNextLocalPtr(){
		return namelocalvarp + ++ptrCount;
	}


	public void printLibraries(){
		String content = // "DESCOMENTAR";

				"declare %Object* @Object_abort(%Object*) \n" +
				"declare i8* @Object_type_name(%Object*) \n" +
				"declare %IO* @IO_out_string(%IO*, i8*) \n" +
				"declare %IO* @IO_out_int(%IO*, i32) \n" +
				"declare i8* @IO_in_string(%IO*)\n" +
				"declare i32 @IO_in_int(%IO*)\n" +
				"declare i32 @String_length(i8*) \n" +
				"declare i8* @String_concat(i8*, i8*) \n" +
				"declare i8* @String_substr(i8*, i32, i32) \n" +
				"declare i32 @strcmp(i8*, i8*) \n" +
				"declare i8* @malloc(i64)";

		System.out.println(content);
	}

	private void print(ClassDef c) {
		// printIndent(1);
		//        System.out.printf("class %s", c.getType());
		if( c.getSuper() != null ) {
			//            System.out.printf(" : %s", c.getSuper());
			String stringFather = c.getSuper();
			ClassDef father = getFatherClass(stringFather);
			currentClass = father;
			classScope = _symTable.findClass(father.getType());
			for(Feature f: father.getBody()) {
				if(f instanceof Method) {
					Method m1 = (Method)f;
					boolean write = true;
					String s1 = m1.getName();

					for(Feature f2: c.getBody()) {
						if( f2 instanceof Method ){
							Method m2 = (Method)f2;
							String s2 = m2.getName();
							//							System.out.println(";;;;;;; s1: "+ s1 + " - s2: "+s2);
							if( s1.equalsIgnoreCase( s2 ) )
							{
								write = false;
								break;
							}
						}
					}
					if(write){
						defineMethod(m1);
					}
				}
			}

		}
		currentClass = c;
		currentClassName = c.getType();
		classScope = _symTable.findClass(c.getType());
		for(Feature f: c.getBody()) {
			print(f);
		}
	}


	private ClassDef getFatherClass(String className) {
		for(ClassDef c: _root) {  
			if(className != null)
			{
				if(className.equalsIgnoreCase(c.getType()))
				{
					currentClass = c;
					return c;
				}
			}
		}
		return null;
	}


	private ArrayList getChildClass(String className) {
		ArrayList<ClassDef> list = new ArrayList<>();
		for(ClassDef c: _root) {  
			if(className != null)
			{
				if(className.equalsIgnoreCase(c.getSuper()))
				{
					list.add(c);
				}
			}
		}
		return list;
	}

	private void print(Feature f) {
		if(f instanceof Method) {
			Method m = (Method)f;
			defineMethod(m);
		}
		else if (f instanceof Variable) {
			Variable var = (Variable)f;

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
		System.out.println(tag);
		if(_printTypes) {
			String type = e.getExprType();
			if(type != null) {
				//				System.out.printf(" [%s]", type);
			}
			else {
				//				System.out.print(" ERROR");
			}
		}
		//		System.out.println();
	}


	@SuppressWarnings("unchecked")
	private void print(Expr e, int indent) {
		assert e != null : "node is null";
		if(e instanceof Block) {
			//			printTag("block", e);
			for(Expr child: ((Block)e).getStatements()) {
				print(child, indent+1);
			}
		}
		else if(e instanceof WhileExpr) {
			WhileExpr loop = (WhileExpr)e;
			assert "Object".equals(loop.getExprType()) : "while type must be Object";
			/*
//			printTag("while", e);
			print(loop.getCond(), indent+1);
			// printIndent(indent);
//			System.out.println("loop");
			print(loop.getValue(), indent+1);
			 */
			String type = loop.getValue().getExprType();

			String cond = "whileCond" + whileCount;

			System.out.println("  ;; condicion: ---> ");			
			print(loop.getCond(), indent+1);
			System.out.println("  ;; <--- condicion: ");	

			System.out.println("  ;; loop: ===> ");						
			print(loop.getValue(), indent+1);
			System.out.println("  ;; <=== loop: ");	

		}
		else if(e instanceof AssignExpr) {

			//			printTag(String.format("assign %s", ((AssignExpr)e).getId()), e);
			//			System.out.println("AssignExpr....");

			String id = "@"+( (AssignExpr)e ).getId();


			AssignExpr assign = ((AssignExpr)e);
			print(assign.getValue(), indent+1);

			if(assign.getExprType().equals("Int")){
				Field field1 = classScope.getField(assign.getId());
				if(field1 != null){
					int index1 =  field1.index;
					printIndent(1);

					String var1 = getLocalInt();

					String var2 = getNextLocalInt();
					System.out.println( "%" + var2+" = getelementptr inbounds %Program* %m, i32 0, i32 "+index1);
					System.out.println("    store i32 %"+var1+", i32* %" + var2);
				}
				else
				{

					Field f = methodScope.getField(assign.getId());
					if(f != null )
					{
						String currentvar = getLocalInt();
						String nextvar = getNextLocalInt();
						System.out.println("    %" + nextvar + " = add i32 %" + currentvar  + ", 0");
					}
					else
					{
						String currentvar = getLetInt();
						String nextvar = getNextLetInt();
						System.out.println("    %" + nextvar + " = add i32 %" + currentvar  + ", 0");
					}
				}
			}
			else if(assign.getExprType().equals("String")){
				printIndent(1);
				System.out.println("store i8* %local_string"+stringCount +", i8** " + id);
			}
			else if(assign.getExprType().equals("Bool")){
				printIndent(1);
				System.out.println("store i1 %local_bool"+boolCount +", i1* " + id);
			}

		}
		else if(e instanceof DispatchExpr) {
			//			System.out.println(";;; -------->");

			DispatchExpr call = (DispatchExpr)e;
			StringBuilder out = new StringBuilder();
			String originType = ( call.getExprType() );
			String returnType = refactorType( call.getExprType() );
			String nameMethod = (call.getName());

			String type = ";;; instancia de UnaryExpr";
			Expr callExp = call.getExpr();
			if( callExp != null ){
				type = callExp.getExprType();
			}else{
				type = currentClassName;
			};

			String pars = "";
			Method m = getParamsTypes(nameMethod, type);	


			if(call.getType() != null) {
				out.append(" as ").append(call.getType());
			}
			//			printTag(out.toString(), e);

			if( call.getExpr() != null ) {
				//				System.out.println("callee");
				print(call.getExpr(), indent+2);
			}
			if (call.getArgs().size() > 0) 
			{
				int i = 0;

				for(Expr arg: call.getArgs()) 
				{
					print(arg, indent+2);
					if(arg.getExprType().equalsIgnoreCase("Int"))
					{	
						if(m == null || m.getParams() == null || m.getParams().get(i) == null || m.getParams().get(i).getType().equalsIgnoreCase("Int"))
						{
							pars += ", i32 %"+ getLocalInt(); 
						}else{
							pars += ", i32 %"+ getLocalInt(); 
						}
					} else if(arg.getExprType().equalsIgnoreCase("Bool")){
						pars += ", i1 %"+ getLocalBool(); 
					} else if(arg.getExprType().equalsIgnoreCase("String")){
						pars += ", i8* %"+ getLocalString(); 
					} else {
						String p = (m.getParams().get(i).getType());
						String a = arg.getExprType();
						if( p.equalsIgnoreCase(a) ){
							pars += (" , %"+ ( a )+"* %"+ ( getLocalPtr() ) ) ;
						}else
						{
							String v1 = "%" + getLocalPtr();
							String v2 = "%" + ( getNextLocalPtr() );
							System.out.println("    " + v2+" = bitcast %"+a+"* "+( v1)+" to %"+p+"*");
							pars += (" , %"+ ( p )+"* "+ v2 ) ;
						}
					}
					i++;
				}
			}
			if(nameMethod.equalsIgnoreCase("out_string")){
				System.out.println("    call %IO* @IO_out_string(%IO* null, i8* %"+getLocalString()+")");		
			}
			else 
				if(nameMethod.equalsIgnoreCase("out_int")){
					String content = "";
					System.out.println("    call %IO* @IO_out_int(%IO* null, i32 %"+getLocalInt()+")");	
					System.out.println(content);
				}
				else if(nameMethod.equalsIgnoreCase("in_string")){
					System.out.println( "    %local_string"+stringCount+" = call i8* @IO_in_string(%IO* %_tmp_1)");
				}	
				else if(nameMethod.equalsIgnoreCase("in_int")){
					System.out.println( "    %local_int"+intCount+" = call i32 @IO_in_int(%IO* %_tmp_1)");
				}						
				else{
					if(nameMethod.equalsIgnoreCase("concat")){
						System.out.println("    %local_string"+(++stringCount)+" = call i8* @String_concat( i8* %local_string"+(stringCount-2)+", i8* %local_string"+(stringCount-1)+")");		
					}
					else if(nameMethod.equalsIgnoreCase("substr")){
						System.out.println("    %local_string"+(stringCount+1)+" = call i8* @String_substr( i8* %local_string"+(stringCount)+", i32 %local_int"+(intCount-1)+", i32 %local_int"+(intCount)+")");		
						stringCount++;
					}
					else if(nameMethod.equalsIgnoreCase("length")){
						System.out.println("    %local_int"+(++intCount)+" = call i32 @String_length( i8* %local_string"+(stringCount)+")");		
					}
					// Object
					else if(nameMethod.equals("type_name")){  
						System.out.println("%" + getNextLocalString() + " = call i8* @Object_type_name(%Object* %_tmp_1)");
					}
					else {

						if(call.getArgs() == null)
						{
							System.out.println("aaaaaaaaaaaa");
						}if(m.getParams() == null)
						{
							System.out.println("nnnnnnnn");
						}
						if(call.getArgs().size() > 0){
							Expr e1 = call.getArgs().get(0);
							String current = getLocalPtr();
//							System.out.println(";;;;;;;;;;;;;; Llamando " + nameMethod + " de tipo [" + type + "] sobre: %" +  current);
						}
						pars = "%"+type+"* null" + pars;

						if(originType.equalsIgnoreCase("Int")){
							System.out.println("    %local_int"+ (intCount+1) +" = call i32 @Main_"+nameMethod+"("+pars+")");
							intCount++;
						} else if(originType.equalsIgnoreCase("Bool")){
							System.out.println("    %local_bool"+ (boolCount+1) +" = call i1 @Main_"+nameMethod+"("+pars+")");
							boolCount++;
						} else if(originType.equalsIgnoreCase("String") ){
							System.out.println("    %local_string"+ (stringCount+1) +" = call i8* @"+( type )+"_"+nameMethod+"("+pars+")");
							stringCount++;
						}else if(originType.equalsIgnoreCase("Object") ){
							System.out.println("    %"+getNextLocalPtr() +" = call "+returnType+" @"+type+"_"+nameMethod+"("+pars+")");
						}else{
							System.out.println("    %"+getNextLocalPtr() +" = call "+returnType+" @"+type+"_"+nameMethod+"("+pars+")");
						}
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

			String result = "";
			if(type.equalsIgnoreCase("Int")){
				result = "%v"+auxIf+" = alloca i32";
			}
			else if(type.equalsIgnoreCase("Bool")){
				result = "%v"+auxIf+" = alloca i1";
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
				result = "    store i32 %local_int"+intCount+", i32* %v"+auxIf+"\n";
				System.out.println(result);			
			}
			else if(type.equalsIgnoreCase("Bool")){
				result = "    store i1 %local_bool"+boolCount+", i1* %v"+auxIf+"\n";
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

			if(type.equalsIgnoreCase("Int")){
				result = "store i32 %local_int"+intCount+", i32* %v"+auxIf+"\n";
				System.out.println(result);			
			}
			else if(type.equalsIgnoreCase("Bool")){
				result = "store i1 %local_bool"+boolCount+", i1* %v"+auxIf+"\n";
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
				result = "    %local_int"+(++intCount)+" = load i32*  %v"+auxIf+"";
			}
			else if(type.equalsIgnoreCase("Bool")){
				result = "    %local_bool"+(++boolCount)+" = load i1*  %v"+auxIf+"";
			}
			System.out.println(result);


		}
		else if(e instanceof NewExpr) 
		{
			NewExpr newExpr = (NewExpr)e;
			System.out.println("    %" + getNextLocalPtr() + " = call %"+newExpr.getExprType()+"* @new"+newExpr.getExprType()+"()");
			//			printTag(String.format("new %s",newExpr.getType()), e);
			//			assert newExpr.getType().equals(e.getExprType()) : String.format("Incompatible types %s %s", newExpr.getType(), e.getExprType());
		}
		else if(e instanceof UnaryExpr) {
			UnaryExpr expr = (UnaryExpr)e;
			String op = operator(expr.getOp());
			String operation = "";
			print(expr.getValue(), indent + 1);
			if(op.equalsIgnoreCase("NOT")){
				String var = "%local_bool"+boolCount;
				printIndent(1);
				System.out.println("%local_bool"+(++boolCount)+" = icmp eq i1 " + var + ", 0");
			}else if(op.equalsIgnoreCase("~")){
				String var = "%local_int"+intCount;
				printIndent(1);
				System.out.println("%local_int"+(++intCount)+" = mul i32 " + var + ", -1");
				printIndent(1);
				System.out.println("%local_int"+(++intCount)+" = sub i32 -1, " + var + "");
			}else if(op.equalsIgnoreCase("isvoid")){
				Expr valExp = ((UnaryExpr) e).getValue();
				String type = valExp.getExprType();
				if(isBasic(type)){
					System.out.println("%" + getNextLocalBool() +" = icmp eq i1 1, 0");
				}else
				{
					if(valExp instanceof IdExpr){
						IdExpr idExp = (IdExpr)valExp;
						String id = idExp.getId();
						System.out.println("%" + getNextLocalBool() +" = icmp eq i1 1, 1");
					}
				}
			}
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
			}else if(op.equalsIgnoreCase("="))
			{
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
					//					operation = "icmp eq";
					print(expr.getLeft(), indent + 1);   
					String var1 = "%local_int"+intCount;
					print(expr.getRight(), indent + 1); 
					String var2 = "%local_int"+intCount;

					System.out.println("    %local_bool"+(++boolCount)+" = icmp eq i32 "+var1+",  "+var2+"");
					return;
				}

				else if(expr.getLeft().getExprType().equals("Bool")){
					//					operation = "icmp eq";
					print(expr.getLeft(), indent + 1);   
					String var1 = "%local_bool"+boolCount;
					print(expr.getRight(), indent + 1); 
					String var2 = "%local_bool"+boolCount;

					System.out.println("    %local_bool"+(++boolCount)+" = icmp eq i1 "+var1+",  "+var2+"");
					return;
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
		else if (e instanceof CaseExpr) 
		{
			CaseExpr caseExpr = ((CaseExpr)e);
			String tipoRetorno = methodScope.getReturnType();

			//			printTag("instanceof", e);
			print(caseExpr.getValue(), indent+1);

			String exType = caseExpr.getValue().getExprType();
			String caseParam = getLocalPtr();
			String paramID = "m";
			if(caseExpr.getValue() instanceof IdExpr)
			{
				IdExpr asdEx = (IdExpr)(caseExpr.getValue());
				paramID = asdEx.getId();
			}
			String id = caseExpr.getExprType();
			Expr o = caseExpr.getValue();
			String paramType = "";
			if(o instanceof IdExpr)
			{
				IdExpr ie = (IdExpr)o;
				paramType = ie.getExprType();
			}else{
				paramType = o.toString();
			}

			int auxCase = caseCount++;
			int auxCount = 0;

			String ifID = auxCase + "_" + auxCount++;
			String n0 = "%_tmp_ptr_" + ifID;

			String type0 = "%_tmp_str_" + ifID;
			System.out.println("    " + n0 + " = getelementptr inbounds %Object* %"+getLocalPtr()+", i32 0, i32 0");
			System.out.println("    " + type0 + " = load i8** " + n0);

			String endCase = "EndCase" + auxCase;
			int scopeIndex = 0;
			for(Case c : caseExpr.getCases()) 
			{
				ifID = auxCase + "_" + auxCount++;
				String caseClass = c.getType();
				String caseValue = c.getValue().getExprType();
				String caseId = c.getId();
				Expr ex = caseExpr.getValue(); 
				if(! caseClass.equalsIgnoreCase("Object"))
				{
					/**********************************************************************************************************************************************/				
					System.out.println("");
					String n1 = "%_tmp_ptr_" + ifID;
					String type1 = "%_tmp_str_" + ifID;				
					System.out.println("    %local_" + caseClass + " = call %" + caseClass + "* @new" + caseClass + "()");
					System.out.println("    %local_ptr" + caseClass + " = bitcast %" + caseClass + "* %local_" + caseClass + " to %Object*");
					System.out.println("    " + type1 + " = call i8* @Object_type_name(%Object* %local_ptr" + caseClass + ")");
					System.out.println("  %case_aux_i" + ifID + "  = call i32 @strcmp(i8* " + type0 + ", i8* " + type1+ ")");
					System.out.println("  %case_aux" + ifID + "  = icmp eq i32   %case_aux_i" + ifID + ", 0");
					System.out.println("");
					String auxClass = caseClass;
					
					System.out.println(";;; Revisando Herencia de: " + auxClass );
					ArrayList<ClassDef> childs = getChildClass(auxClass);
					if(childs != null){
						String ifID2 = ifID;
						for(int i = 0 ; i < childs.size() ; i++)
						{
							boolean exist = false;
							for(Case c2 : caseExpr.getCases())
							{
								if(c2.getType().equalsIgnoreCase(childs.get(i).getType()))
								{
									
									System.out.println(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
									System.out.println(";; SI EXISTE LA CLASE Hijo: " + c2.getType());
									System.out.println(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
									 
									exist = true;
									break;
								}
							}
							if(!exist)
							{

								
								System.out.println(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
								System.out.println(";; NO EXISTE LA CLASE hijo " + childs.get(i).getType() +", y tengo que hacerla: ");
								System.out.println(";;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;");
								
								String parentClass = childs.get(i).getType();
								System.out.println("");
								ifID += i;
								String n2 = "%_tmp_ptr_" +auxCase + "_" + ifID;
								String type2 = "%_tmp_str_" + auxCase + "_" + ifID;		

								caseClass = childs.get(i).getType();
								String classi = caseClass + i;
								System.out.println("    %local_" + classi + " = call %" + caseClass + "* @new" + caseClass + "()");
								System.out.println("    %local_ptr" + classi + " = bitcast %" + caseClass + "* %local_" + classi + " to %Object*");
								System.out.println(type2 + " = call i8* @Object_type_name(%Object* %local_ptr" + classi + ")");
								System.out.println("  %case_aux_i" + ifID + "  = call i32 @strcmp(i8* " + type0 + ", i8* " + type2+ ")");
								System.out.println("  %case_aux" + ifID + "  = icmp eq i32   %case_aux_i" + ifID + ", 0");
								System.out.println("");
								String if0 = "%CaseEqual" + ifID;
								String else0 ="%CaseUnequal" + ifID;
								System.out.println("  br i1 %case_aux" + ifID + ", label " + if0 + ", label " +else0 );
								System.out.println("CaseEqual" + ifID + ":");

								String currentvar = getLetPtr();
								String nextvar = getNextLetPtr();
								System.out.println("%" +  nextvar + "= bitcast %Object* %" + caseParam + " to %" + auxClass+ "*");
								print(c.getValue(), indent+2);
								System.out.println("  br label %" + endCase + "_0" );
								System.out.println();
								System.out.println("CaseUnequal" + ifID + ":");

							}

						}
						ifID = ifID2;
					}


					/**********************************************************************************************************************************************/
					String if0 = "%CaseEqual" + ifID;
					String else0 ="%CaseUnequal" + ifID;
					System.out.println("  br i1 %case_aux" + ifID + ", label " + if0 + ", label " +else0 );
					System.out.println("CaseEqual" + ifID + ":");
					System.out.println(";;1804");
					////////////////////////////////////////////////////////////////////////////////////////////////////////
					String currentvar = getLetPtr();
					String nextvar = getNextLetPtr();


					currentScope = currentScope.getScopes().get(scopeIndex);
					System.out.println("%" + nextvar+ " = bitcast %Object* %" + caseParam + " to %" + auxClass+ "*");
					print(c.getValue(), indent+2);	// DESCOMENTAME
					//					System.out.println(";;;; [AQUI SE HARIA EL PRINT]");
					currentScope = methodScope;
					////////////////////////////////////////////////////////////////////////////////////////////////////////
					System.out.println("  br label %" + endCase + "_0" );
					System.out.println();
					System.out.println("CaseUnequal" + ifID + ":");
				}
				//////////////////// caseClass = Object
				else
				{
					/**********************************************************************************************************************************************/				

					// Obtener la variable y guardarla en una local. Castear segun el tipo
					System.out.println("; SI object (" + caseClass + " ), así que solo tengo que guardar");

					String currentvar =  getLocalPtr();
					String nextvar = getNextLetPtr();

					System.out.println("%" + nextvar + "= bitcast %Object* %" + caseParam + " to %Object*");
					currentScope = currentScope.getScopes().get(scopeIndex);
					print(c.getValue(), indent+2);	// DESCOMENTAME
					
					currentScope = methodScope;

					break;
				}
				///////////////////////////
			}
			System.out.println("br label %" + endCase + "_0");
			System.out.println("; Para finalizar");
			System.out.println(endCase + "_0:");

		}
		else if (e instanceof LetExpr) {
			currentScope = currentScope.getScopes().get(0);
			LetExpr let = (LetExpr)e;
			//			printTag("let", e);
			System.out.println(";;; vars");
			for(Variable var : let.getDecl()) {
				//				System.out.printf("%s %s\n", var.getType(), var.getId());
				if(var.getValue() != null) {
					print(var.getValue(), indent+3);
					String type = var.getType();
					System.out.print(";; deberia cargar: " + var.getId() + " de tipo " + type );
					if(type.equalsIgnoreCase("Int")){
						System.out.println(" con ");
						System.out.println("     %" + getNextLetInt() + " = add i32 0, %" + getLocalInt()  );
					}
				}
			}
			print(let.getValue(), indent+1);

			currentScope = methodScope;
		}
		else if(e instanceof IdExpr) {
			//            printTag(String.format("id %s", ((IdExpr)e).getId()), e);
			String type = ((IdExpr)e).getExprType();
			IdExpr id = ((IdExpr)e);

			if(type == null){       
				System.out.println(";--- id.getExprType == null");
				return; 		
			}

			if(type.equals("Int")){        		
				String globalvar = id.getId();
				if(methodScope.hasParamField(globalvar)){
					String currentvar = getLocalInt();
					String nextvar = getNextLocalInt();
					System.out.println("    %" + nextvar + " = add i32 %" + globalvar + ", 0");
				}
				else{
					Field field = classScope.getField(globalvar);
					if(field != null){
						String currentvar = getLocalInt();
						String nextvar = getNextLocalInt();
						System.out.println("    %" + nextvar + " = getelementptr inbounds %"+ classScope.getClassType() +"* %m, i32 0, i32 " + field.index);
						System.out.println("    %" + getNextLocalInt() + " = load i32* %" + nextvar);
					}else{
						String currentvar = getLetInt();
						String nextvar = getNextLetInt();
						System.out.println(";-- variable local: "+globalvar + " = " +currentvar + " <- NO LA SUMO");
						System.out.println("    %" + nextvar + " = add i32 0, %" + currentvar );
					}
				}
			}
			else if(type.equals("String")){
				String globalvar = id.getId();

				printIndent(1);
				if(methodScope.hasParamField(globalvar)){
					String currentvar = getLocalString();
					String nextvar = getNextLocalString();
					System.out.println("    %" + nextvar + " = load i8* %" + globalvar);
				}
				else{
					Field f = currentScope.getField(globalvar);
					if(f != null)
					{
						String currentvar = getLetString();
						String nextvar = getNextLetString();
						System.out.println("    %" + nextvar + " = getelementptr %" + classScope.getClassType() +"* %m, i32 0, i32 " + f.index);
						System.out.println("    %" + getNextLetString() + " = load i8** %" + nextvar);

					}else{					
						String currentvar = getLocalString();
						String nextvar = getNextLocalString();

						Field field = classScope.getField(globalvar);
						System.out.println("    %" + nextvar + " = load i8* %" + globalvar);
						System.out.println("    %" + getNextLocalString() + " = load i8** %" + nextvar);
					}
				}

			}
			else if(type.equals("Bool")){

				String globalvar = id.getId();

				if(methodScope.hasParamField(globalvar)){

					String currentvar = getLocalBool();
					String nextvar = getNextLocalBool();
					System.out.println("    %" + nextvar + " = icmp eq i1 %" + globalvar + ", 1");
				}
				else{

					Field field = classScope.getField(globalvar);
					if(field != null){
						String currentvar = getLocalBool();
						String nextvar = getNextLocalBool();
						System.out.println("    %" + nextvar + " = getelementptr inbounds %"+ classScope.getClassType() +"* %m, i32 0, i32 " + field.index);
						System.out.println("    %" + getNextLocalBool() + " = load i1* %" + nextvar);
					}else{
						String currentvar = getLetBool();
						String nextvar = getNextLetBool();

						if(methodScope.hasParamField(globalvar)){
							Field f = methodScope.getField(globalvar);
							System.out.println("    %" + nextvar + " = getelementptr inbounds %"+ classScope.getClassType() +"* %m, i32 0, i32 " + f.index);
							System.out.println("    %" + getNextLocalBool() + " = load i1* %" + nextvar);
						}else
						{
							for(int i = 0 ; i < methodScope.getScopes().size() ; i++)
							{
								Scope ms = methodScope.getScopes().get(i);
								if(ms.getField(globalvar) != null)
								{
									currentScope = ms;
									break;
								}
							}

							Field f = currentScope.getField(globalvar);
							System.out.println("    %" + nextvar + " = icmp eq i1 %" + currentvar + ", 1");
						}
					}
				}
			}
			else{
				String globalvar = id.getId();

				if(methodScope.hasParamField(globalvar)){
					String currentvar = getLocalPtr();
					String nextvar = getNextLocalPtr();
					System.out.println("    %" + nextvar + " = alloca %" + type + "*");
					System.out.println("    store %" + type + "* %" + globalvar + ", %" + type + "** %" + nextvar );
					System.out.println("    %" + getNextLocalPtr() + " = load %" + type + "** %" + nextvar);
				}
				else
				{
					Field f = classScope.getField(globalvar);
					if(f != null)
					{
						int index = f.index;
						if( index != 0 )
						{
							String currentvar = getLocalPtr();
							String nextvar = getNextLocalPtr();
							System.out.println("    %" + nextvar + " = getelementptr inbounds %" + currentClassName +"* %m, i32 0, i32 " + index);
							System.out.println("    %" + getNextLocalPtr() + " = load %" + type + "** %" + nextvar + " ;;;; <- esto");
						}

					}else
					{

						String currentvar = getLetPtr();
						String nextvar = getNextLetPtr();
						System.out.println("    %" + nextvar + " = alloca %" + type + "*");
						System.out.println("    store %" + type + "* %" + currentvar + ", %" + type + "** %" + nextvar );
						System.out.println("    %" + getNextLetPtr() + " = load %" + type + "** %" + nextvar);

					}
				}
			}
		}
		else if(e instanceof ValueExpr) {
			Object value = ((ValueExpr)e).getValue();

			if(value instanceof String) {

				int stringLength = (((String)(value)).length()+1);
				value =  ((String)value).replace("\n", "\\0A")
						.replace("\t", "\\09").replace("\f", "\\0c").replace("\b", "\\08");
				String variable = stringList.get(value);
				stringCount ++;
				System.out.println("    %local_string"+(stringCount )+" = load i8** "+variable);


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


	private Method getParamsTypes(String nameMethod, String classMethod) {
		// TODO Auto-generated method stub
		if(classMethod == null || classMethod== null )
			return null;
		if(classMethod.equalsIgnoreCase("") || classMethod.equalsIgnoreCase("Object"))
			return null;

		ClassDef methodClass = null;
		String s2 = classMethod;
		for(ClassDef c: _root) {  
			String s1 = c.getType();
			if(s1.equalsIgnoreCase( s2 )){
				methodClass = c;
				break;
			}
		}
		if(methodClass == null)
			return null;
		for(Feature f: methodClass.getBody()) {
			if(f instanceof Method) {
				Method m = (Method)f;
				String m1 = m.getName();
				String m2 = nameMethod;
				if(m1.equalsIgnoreCase( m2 )){
					return m;			
				}
			}			
		}

		return getParamsTypes(nameMethod, methodClass.getSuper());
	}

	private boolean isBasic(String t) {
		// TODO Auto-generated method stub
		if(t.equalsIgnoreCase("Int") || t.equalsIgnoreCase("Bool") || t.equalsIgnoreCase("String") )
			return true;
		return false;
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