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


	String namelocalvars = "local_string";
	String namelocalvari = "local_int";
	String namelocalvarb = "local_bool";
	String namelocalvarp = "local_ptr";

	ClassDef currentClass = null;
	String currentClassName = "";

	private SymTable _symTable;
	ClassScope classScope;
	MethodScope methodScope;

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
		//		return "%" + currentClass.getType() + "*";
		return "%" + t + "*";
	}


	public void defineFunction(Method m) {
		String methodName = m.getName();
		methodScope = classScope.getMethod(methodName);
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
//		System.out.println(";; " + methodName + " -> " + returnValue );
		if(returnValue.equalsIgnoreCase("SELF_TYPE") 
				//|| returnValue.equalsIgnoreCase("Object")
				){
			//			returnValue = "%"+currentClass.getType()+"*";
			returnValue = "%"+ currentClassName +"*";			
		}else if (returnValue.equalsIgnoreCase("Int") ){
			returnValue = "i32";
		}else if (returnValue.equalsIgnoreCase("Bool") ){
			returnValue = "i1";
		}else if (returnValue.equalsIgnoreCase("String") ){
			returnValue = "i8*";
		}else{
			if(returnValue.equalsIgnoreCase("Object")
					){
				//			returnValue = "%"+currentClass.getType()+"*";
				returnValue = "%Object*";	
			}else{

				//			returnValue = "%"+currentClass.getType()+"*";	
				returnValue = "%"+ returnValue +"*";	
			}
		}
		if(methodName.equalsIgnoreCase("main")){	
			returnValue = "%"+ currentClassName +"*";	
		}

		//		String content = "define "+returnValue+" @"+currentClass.getType()+"_"+m.getName()+"(" ;
		String content = "define "+returnValue+" @"+ currentClassName +"_"+m.getName()+"(" ;
		content += "%"+ currentClassName +"* %m";
		String pars = "";

		if(m.getParams().size()>0){
			for(int i = 0 ; i < m.getParams().size() ; i++){
				//				content += ", "+ refactorType( (m.getParams().get(i).getType())) + " %"+ (m.getParams().get(i).getId());
				String p = (m.getParams().get(i).getType());
				pars += ", "+ refactorType( p) + " %"+ (m.getParams().get(i).getId())  ;
			}
		}
		content += pars;

		content +=") {\n";
		//		content += "    %_tmp_1 = bitcast %"+currentClass.getType()+"* %m to %Object*" ;
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
					System.out.println(";; TERMINAR BOOL");
					System.out.println("\n    %ptr_param"+countParam+" = alloca i1" +
							"\n    store i1 %"+ (m.getParams().get(i).getId())+", i1* %ptr_param"+countParam +
							"\n    %local_bool"+(++boolCount)+" = load i1* %ptr_param"+countParam);
					//					System.out.println( "\n    ret i1 %local_bool"+boolCount);
				}else if (type.equalsIgnoreCase("String") ){
					//					System.out.println( "\n    ret i8* %local_string"+stringCount);
					System.out.println(";; TERMINAR STRING");
					System.out.println("\n    %ptr_param"+countParam+" = alloca i8*" +
							"\n    store i8* %"+ (m.getParams().get(i).getId())+", i8** %ptr_param"+countParam +
							"\n    %local_str"+(++stringCount)+" = load i8** %ptr_param"+countParam);
				}else{
					//					System.out.println( "    ;;;; PARAMETRO NO BASICO");
					System.out.println(";; TERMINAR OTRO");

					System.out.println("\n    %ptr_param"+countParam+" = alloca %"+type+"*" +
							"\n    store %"+type+"* %"+ (m.getParams().get(i).getId())+", %"+type+"** %ptr_param"+countParam +
							"\n    %local_ptr"+(++ptrCount)+" = load %"+type+"** %ptr_param"+countParam);
				}
				countParam++;

				content += ", "+ refactorType( (m.getParams().get(i).getType())) + " %"+ (m.getParams().get(i).getId());
				System.out.println(";; TERMINAR FINAL");
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
				//			System.out.println( "\n    ret %"+mainClass+"* %m");
				//			System.out.println( "\n    ret %"+currentClass.getType()+"* %m");
				System.out.println( "\n    ret "+ returnValue +" %m");	
				System.out.println("}");

				return;
			}

		}
		System.out.println("}");
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


		//		printClasses(c);
		printBasic();


		for(ClassDef c: _root) {  
			currentClass = c;
			currentClassName = c.getType();
			printClasses(c);    
			//			            print2(c);
			//			printClasses(c);
			//	    	printConstans(c);
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
		// TODO Auto-generated method stub

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
		/*
		mainClass = c.getType();
		String content = "%"+c.getType()+" = type { i8* }\n\n";
		System.out.println(content);

        classScope = _symTable.findClass(c.getType());
		 */

		String myClass = c.getType();

		classScope = _symTable.findClass(c.getType());
		if(classScope.getMethod("main") != null){
			mainClass = myClass;
		}

//		System.out.println(";;;;;;3; myClass: "+myClass);
		int lengthClass = myClass.length() + 1;
		System.out.println("@.type."+myClass+" = private constant ["+lengthClass+" x i8] c\""+myClass+"\\00\"");
		System.out.println();

		System.out.println("%" + myClass + "= type {");

		ArrayList<String> typeList = new ArrayList<>();
		typeList.add("i8*");

		//		System.out.println("; REVISAR ESTO + .index");
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
				//				classScope.getField(variable.getId()).index  = index++; // lista de fields
				typeList.add(type);				
			}
		}

		System.out.print("    "+typeList.get(0));

		for(int i = 1 ; i < typeList.size() ; i++){
			System.out.print(",\n    "+typeList.get(i));        	
		}        
		System.out.println("\n};");

		//System.out.printf("class %s", c.getType());
		if( c.getSuper() != null ) {
			//System.out.printf(" : %s", c.getSuper());
		}
		System.out.println();




		String baseClass = "define %"+myClass+"* @new"+myClass+"() {" +
				//        		"\n    ; "+myClass+"* ptr = ("+myClass+"*)malloc(sizeof("+myClass+"))" +
				"\n    %vptr = call i8* @malloc( i64 ptrtoint (%"+myClass+"* getelementptr (%"+myClass+"* null, i32 1) to i64) )" +
				"\n    %ptr = bitcast i8* %vptr to %"+myClass+"* " +
				//        		"\n    ; ptr->type = @.type."+myClass+"" +
				"\n    %typePtr = getelementptr %"+myClass+"* %ptr, i32 0, i32 0" +
				"\n    store i8* bitcast( ["+lengthClass+" x i8]* @.type."+myClass+" to i8*), i8** %typePtr" +
				"\n    ; inicializacion de los los fields del objeto recién creado";
		System.out.println(baseClass);
		for(Feature f: c.getBody()) {
			if (f instanceof Variable){

				printFields( (Variable)f );
			}
		}

		//		System.out.println("LOCALES");


		/*
classScope.fieldList // lista de fields
		 */
		System.out.println(
				"    ret %"+myClass+"* %ptr" +
				"\n}");

		//		classScope = _symTable.findClass(c.getType());
		//		print(c);


	}


	private void printFields(Variable var) {
		// TODO Auto-generated method stub
		//		System.out.println("("+ var.getClass() +")");
		if(var.getValue() instanceof NewExpr){
			System.out.println("; inicializando Objeto");
			NewExpr valueexpr = (NewExpr)var.getValue();

			Field field = classScope.getField(var.getId());
			int index = field.index;

			String value = "";

			//            System.out.println("    %" + getNextLocalString() + " = getelementptr %" + currentClass.getType() +"* %ptr, i32 0, i32 " + index);
			System.out.println("    %" + getNextLocalString() + " = getelementptr inbounds %" + currentClassName +"* %ptr, i32 0, i32 " + index);
			System.out.println("    %ptr_"+ index +" = call %" + field.getType() +"* @new" + field.getType() + "();");
			System.out.println("    store %" + field.getType() +"* %ptr_" + index + ", %" + field.getType() +"** %" + getLocalString() );
			/*
            System.out.println("    %" + getNextLocalString() + " = getelementptr %" + currentClass.getType() +"* %ptr, i32 0, i32 " + field.iposition);
            System.out.println("    %ptr_"+ field.iposition +" = call %" + field.getType() +"* @new" + field.getType() + "();");
            System.out.println("    store %" + field.getType() +"* %ptr_" + field.iposition + ", %" + field.getType() +"** %" + getLocalString() );
			 */
		}
		// FALTAN LOS OTROS TIPOS DE FIELDS
		else //if(var.getValue() instanceof ValueExpr)
		{
			//			ValueExpr valExpr = (ValueExpr)var.getValue();
			Field field = classScope.getField(var.getId());
			String value = "0";
			String type = var.getType();;
			if(type.equalsIgnoreCase("Int")){
				System.out.println("; inicializando Int");
				if(var.getValue() != null){
					value = var.getValue().toString();
				}

				//	    	System.out.println("    %"+getNextLocalInt() + " = getelementptr %"+currentClass.getType() + "* %ptr, i32 0, i32 "+field.index);
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
						else
							value = var.getValue().toString();

					}
					else{
						value = "";
					}
				}
				System.out.println(";;;; 2value: "+value);
				int stringLength = value.length() + 1;

				//	            System.out.println("    %" + getNextLocalString() + " = getelementptr %" + currentClass.getType() +"* %ptr, i32 0, i32 " + field.index);
				System.out.println("    %" + getNextLocalString() + " = getelementptr inbounds %" + currentClassName +"* %ptr, i32 0, i32 " + field.index);
				System.out.println("    store i8* bitcast( [" + stringLength + " x i8]* @" + var.getId() +"_c to i8*), i8** %" + getLocalString());

			}

			else if(var.getType().equals("Bool")){
				System.out.println("; inicializando bool");
				if( var.getValue() != null ) {

					ValueExpr valueexpr = (ValueExpr)var.getValue();

					value = valueexpr.getValue().toString() ; 	//var.getValue().toString();
					if(value.equalsIgnoreCase("true")){
						value ="1";
					}else{
						value = "0";
					}

				}
				else{
					value = "0";
				}

				//	            System.out.println("    %" + getNextLocalBool() + " = getelementptr %" + currentClass.getType() +"* %ptr, i32 0, i32 " + field.index);
				System.out.println("    %" + getNextLocalBool() + " = getelementptr inbounds %" + currentClassName +"* %ptr, i32 0, i32 " + field.index);
				System.out.println("    store i1 " + value + ", i1* %" + getLocalBool());
			}

			else{
				System.out.println("; ---- inicializando POR DEFAULT Objeto de tipo: "+type);

			}
		}
		System.out.println("; fin inicializacion");

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
		return namelocalvarp + ptrCount;
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
						defineFunction(m1);
					}
				}
			}

		}
		currentClass = c;
		currentClassName = c.getType();
		//        System.out.println();
		classScope = _symTable.findClass(c.getType());
		for(Feature f: c.getBody()) {
			print(f);
		}
	}


	private ClassDef getFatherClass(String className) {
		// TODO Auto-generated method stub

		for(ClassDef c: _root) {  
			if(className.equalsIgnoreCase(c.getType()))
			{
				currentClass = c;
				return c;
			}
			//			            print2(c);
			//			printClasses(c);
			//	    	printConstans(c);
		}

		return null;
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


			AssignExpr assign = ((AssignExpr)e);
			print(assign.getValue(), indent+1);

			if(assign.getExprType().equals("Int")){
				Field field1 = classScope.getField(assign.getId());
				int index1 =  field1.index;
				printIndent(1);
				//        		System.out.println("store i32 %local_int"+intCount +", i32* " + id);
				String var1 = getLocalInt();

				//				String varname = assign.getId();
				//				Field field = classScope.getField(varname);

				String var2 = getNextLocalInt();
				System.out.println( "%" + var2+" = getelementptr inbounds %Program* %m, i32 0, i32 "+index1);
				System.out.println("    store i32 %"+var1+", i32* %" + var2);
			}
			else if(assign.getExprType().equals("String")){
				printIndent(1);
				System.out.println("store i8* %local_string"+stringCount +", i8** " + id);
			}
			else if(assign.getExprType().equals("Bool")){
				printIndent(1);
				System.out.println("store i1 %local_bool"+boolCount +", i1* " + id);
			}

			/*
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
				}
			}
			else if(ex instanceof ValueExpr)
			{
				System.out.println(";; ValueExpr");
				Object value = ((ValueExpr) ex).getValue().toString();
				String type =  ( (AssignExpr)e ).getExprType();

				print(((AssignExpr)e).getValue(), indent+1);

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
			 */

		}
		else if(e instanceof DispatchExpr) {

			DispatchExpr call = (DispatchExpr)e;
			// metodo
			StringBuilder out = new StringBuilder();

			//			out.append("call ").append(call.getName());
			String originType = ( call.getExprType() );
			String returnType = refactorType( call.getExprType() );


			//			System.out.println(type);

			String nameMethod = (call.getName());

//			System.out.println(";;;; llamando + " + nameMethod );
			
			/*
			if (call.getArgs().size() > 0) {
				// printIndent(indent+1);
				// System.out.println("args");
				for(Expr arg: call.getArgs()) {
					print(arg, indent+2);
					if(arg.getExprType().equalsIgnoreCase("Int")){								
						System.out.println("-------- , i32 %local_int"+ (intCount) ) ;
					} else if(arg.getExprType().equalsIgnoreCase("Bool")){
						System.out.println ("-------- , i1 %local_bool"+ (boolCount) ) ;
					} else if(arg.getExprType().equalsIgnoreCase("String")){
						System.out.println ("-------- , i8* %local_string"+ (stringCount) ) ;
					} else {
						System.out.println ("-------- , %"+ ( arg.getExprType() )+"* %"+ ( getLocalPtr() ) ) ;
					}
				}
			}*/

			/*
			String returnType = call.getExpr().getExprType();
			String type2 = call.getExpr().toString();
			System.out.println(";---  = "+type2);
			 */


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
			String pars = "";
			// TERMINAR PARAMETROS	
			if (call.getArgs().size() > 0) {
				// printIndent(indent+1);
				// System.out.println("args");
				for(Expr arg: call.getArgs()) {
					print(arg, indent+2);

					if(arg.getExprType().equalsIgnoreCase("Int")){								
						pars += (" , i32 %local_int"+ (intCount) ) ;
					} else if(arg.getExprType().equalsIgnoreCase("Bool")){
						pars += (" , i1 %local_bool"+ (boolCount) ) ;
					} else if(arg.getExprType().equalsIgnoreCase("String")){
						pars += (" , i8* %local_string"+ (stringCount) ) ;
					} else {
						pars += (" , %"+ ( arg.getExprType() )+"* %"+ ( getLocalPtr() ) ) ;
					}
				}
			}

			if(nameMethod.equalsIgnoreCase("out_string")){

				System.out.println("    call %IO* @IO_out_string(%IO* null, i8* %local_string"+stringCount+")");		
			}
			else if(nameMethod.equalsIgnoreCase("out_int")){

				String content = "";
				System.out.println("    call %IO* @IO_out_int(%IO* null, i32 %local_int"+intCount+")");	
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
				if(nameMethod.equalsIgnoreCase("concat")){
					System.out.println("    %local_string"+(++stringCount)+" = call i8* @String_concat( i8* %local_string"+(stringCount-2)+", i8* %local_string"+(stringCount-1)+")");		
				}
				else if(nameMethod.equalsIgnoreCase("substr")){
					// ESTA BIEN EL ORDEN????
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
//					System.out.println(";; ------------------- Dentro del else");


					/*
					if (call.getArgs().size() > 0) {

						int localInt = intCount;
						int localBool= boolCount;
						int localString= stringCount;

						for(Expr arg: call.getArgs()) {

							if(arg.getExprType().equalsIgnoreCase("Int")){								
								pars = (" , i32 %local_int"+ (intCount) ) + pars;
							} else if(arg.getExprType().equalsIgnoreCase("Bool")){
								pars = (" , i1 %local_bool"+ (boolCount) ) + pars;
							} else if(arg.getExprType().equalsIgnoreCase("String")){
								pars = (" , i8* %local_string"+ (stringCount) ) + pars;
							}
						}
					}
					 */

					String type = ";;; instancia de UnaryExpr";
					Expr callExp = call.getExpr();
					if( callExp != null ){
						type = callExp.getExprType();
					}else{
						type = currentClassName;
					}
//					System.out.println(";;;; type: "+type);

					pars = "";
					Method m = getParamsTypes(nameMethod, type);	
					if(call.getArgs() == null)
					{
						System.out.println("aaaaaaaaaaaa");
					}if(m.getParams() == null)
					{
						System.out.println("nnnnnnnn");
					}
					//					System.out.println(";; TAMAÑOS: c: "+call.getArgs().size() + " - m: " +m.getParams().size()  );
					if (call.getArgs().size() > 0) {
						// printIndent(indent+1);
						// System.out.println("args");
						int i = 0;
						for(Expr arg: call.getArgs()) {
							print(arg, indent+2);

							if(arg.getExprType().equalsIgnoreCase("Int")){								
								//pars += (" , i32 %local_int"+ (intCount) ) ;							
								pars += ", i32 %"+ getLocalInt(); 
							} else if(arg.getExprType().equalsIgnoreCase("Bool")){
								//pars += (" , i1 %local_bool"+ (boolCount) ) ;						
								pars += ", i1 %"+ getLocalBool(); 
							} else if(arg.getExprType().equalsIgnoreCase("String")){
								//pars += (" , i8* %local_string"+ (stringCount) ) ;						
								pars += ", i8* %"+ getLocalString(); 
							} else {
								String p = (m.getParams().get(i).getType());
								String a = arg.getExprType();
								
								if( p.equalsIgnoreCase(a) ){
									pars += (" , %"+ ( a )+"* %"+ ( getLocalPtr() ) ) ;
								}else{
									/*
									System.out.println(";;; a:= "+a);
									System.out.println(";;; p:= "+p);
									*/
									if( a.equalsIgnoreCase("IO") && p.equalsIgnoreCase("Object") ){
										pars += (" , %"+ ( p )+"* %"+ ( getLocalPtr() ) ) ;
										break;
									}

									String v1 = "%" + getLocalPtr();
									String v2 = "%" + ( getNextLocalPtr() );

									// AGREGAR ESPACIO
									// AGREGAR IDENTACION
									System.out.println(v2+" = bitcast %"+a+"* "+( v1)+" to %"+p+"*");
									pars += (" , %"+ ( p )+"* "+ v2 ) ;

								}
							}

							i++;
						}
					}
					/*			
					if(m.getParams().size() > 0 ){
						for(int i = 0 ; i < m.getParams().size() ; i++){
							String p = (m.getParams().get(i).getType());
							System.out.println(";; ["+i+"] = "+p);
						}
					}*/
					/*
					if(call.getExpr() != null){
							call.getExpr().getExprType();; //type; 	//currentClassName;
					}
					 */
					/*
					if(type.equalsIgnoreCase("Object")){
						Expr arg1 = call.getArgs().get(0);
						returnType = currentClassName; //arg1.getExprType();					
						System.out.println(";;;-- "+returnType);
					}
					 */
					/*
					if(call.getExpr() != null)	{
						returnType = call.getExpr().getExprType();
					}else
					{
						System.out.println("; ESTe CASO");

					}

					 */
					pars = "%"+type+"* null" + pars;


					if(originType.equalsIgnoreCase("Int")){
						System.out.println("    %local_int"+ (intCount+1) +" = call i32 @Main_"+nameMethod+"("+pars+")");
						intCount++;
					} else if(originType.equalsIgnoreCase("Bool")){
						System.out.println("    %local_bool"+ (boolCount+1) +" = call i1 @Main_"+nameMethod+"("+pars+")");
						boolCount++;
					} else if(originType.equalsIgnoreCase("String") ){
						//						System.out.println("; --- aaaaaa: " + (call.getExprType() ) );
						//						System.out.println("; --- bbbb: " + ( returnType ));
						System.out.println(";; AQUI3");
						System.out.println("    %local_string"+ (stringCount+1) +" = call i8* @"+( type )+"_"+nameMethod+"("+pars+")");
						stringCount++;
					}else if(originType.equalsIgnoreCase("Object") ){
//						System.out.println(";; AQUI2");
						System.out.println("    %"+getNextLocalPtr() +" = call "+returnType+" @"+type+"_"+nameMethod+"("+pars+")");
					}else{
						System.out.println(";; AQUI1");
						System.out.println("    %"+getNextLocalPtr() +" = call "+returnType+" @"+type+"_"+nameMethod+"("+pars+")");

					}
					/*
					System.out.print(";;;;;;; originType <-> " + originType);
					System.out.println(";;;;;;; returnType <-> " + returnType);
					System.out.println(";; <------------------- FIN del else");
*/
					/*
					if(type.equalsIgnoreCase("Int")){
						System.out.println("    %local_int"+ (intCount+1) +" = call i32 @Main_"+nameMethod+"(%Main* null, i32 %local_int"+ (intCount) +")");
						intCount++;
					} else if(type.equalsIgnoreCase("Bool")){
						System.out.println("    %local_bool"+ (boolCount+1) +" = call i32 @Main_"+nameMethod+"(%Main* null, i1 %local_bool"+ (boolCount) +")");
						boolCount++;
					} else if(type.equalsIgnoreCase("String")){
						System.out.println("    %local_string"+ (stringCount+1) +" = call i32 @Main_"+nameMethod+"(%Main* null, i8* %local_string"+ (stringCount) +")");
						stringCount++;
					}*/

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


			System.out.println("; type 1009: " + type);

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
			System.out.println("    %" + getNextLocalPtr() + " = call %"+newExpr.getExprType()+"* @new"+newExpr.getExprType()+"()");
			// AQUI SE IMPRIME NEW FIELD new IO
			//			printTag(String.format("new %s",newExpr.getType()), e);

			//			assert newExpr.getType().equals(e.getExprType()) : String.format("Incompatible types %s %s", newExpr.getType(), e.getExprType());
		}
		else if(e instanceof UnaryExpr) {
			UnaryExpr expr = (UnaryExpr)e;



			String op = operator(expr.getOp());
			//			System.out.println("OP: "+op);
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
					System.out.print("; Tipo no basico: " + type);
					if(valExp instanceof IdExpr){
						IdExpr idExp = (IdExpr)valExp;
						String id = idExp.getId();
						System.out.println(" --> id: "+id);
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
		else if (e instanceof CaseExpr) {
//			System.out.println(";;; --- aqui CaseExpr");
			CaseExpr caseExpr = ((CaseExpr)e);

			//			printTag("instanceof", e);
			print(caseExpr.getValue(), indent+1);
			String exType = caseExpr.getExprType();
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
			/*
			String id = "%local_bool" + (boolCount);
			System.out.println("    %ptr_if"+auxCase+" = alloca i1");
			System.out.println("    store i1 "+id+", i1* %ptr_if"+auxCase+"");
			System.out.println("    %cond"+auxCase+" = load i1* %ptr_if"+auxCase+"");
			if(type.equalsIgnoreCase("Int")){
				System.out.println("    %ptr_int"+auxCase+" = alloca i32* ");
			} else if(type.equalsIgnoreCase("Bool")){
				System.out.println("    %ptr_bool"+auxCase+" = alloca i1* ");
			} else if(type.equalsIgnoreCase("String")){
				System.out.println("    %ptr_string"+auxCase+" = alloca i8*");
			}*/
			String ifID = "";
					
			
			for(Case c : caseExpr.getCases()) {
				ifID = auxCase + "_" + auxCount++;
				// printIndent(indent+1);
				String caseClass = c.getType();
				String caseId = c.getId();
				Expr ex = caseExpr.getValue(); 
				/*
				System.out.println(";;;;;====== type: " + exType );
				System.out.println(";;;; caseClass: " + caseClass + " - caseId: " + caseId);
*/
				
				// CAMBIAR POR i32 @strcmp(i8*, i8*)
				System.out.println("  %case_aux" + ifID + "  = icmp eq i32  "+auxCount+", 2 ;;;;;;;;;;;; CAMBIAME CAMBIAR POR i32 @strcmp(i8*, i8*) ;;;;;;;");

				System.out.println("br i1 %case_aux" + ifID + ", label %CaseEqual" + ifID + ", label %CaseUnequal" + ifID );
				System.out.println("CaseEqual" + ifID + ":");
				
				
//				System.out.printf(";;; case type: %s , id-> %s\n", c.getType(), c.getId());
				print(c.getValue(), indent+2);
				System.out.println("br label %EndCase" + auxCase + "_0" );
				System.out.println("CaseUnequal" + ifID + ":");
			}
			System.out.println("br label %EndCase" + auxCase + "_0");
			System.out.println("; Para finalizar");
			System.out.println("EndCase" + auxCase + "_0:");

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
			//			String id = "@" + ((IdExpr)e).getId();
			String type = ((IdExpr)e).getExprType();

			//			System.out.println("ID: "+id + " es "+type);

			////////////////////////////////////////////////////////////


			IdExpr id = ((IdExpr)e);
			if(id.getId().equalsIgnoreCase("iostream")){
//				System.out.println(";--- iostream");
				return;
			}
			if(id.getExprType() == null){       
				System.out.println(";--- id.getExprType");
				return; 		

			}

			if(id.getExprType().equals("Int")){        		

				String globalvar = id.getId();
				String localvar = getNextLocalInt();

				if(methodScope.hasParamField(globalvar)){
					System.out.println("    %" + localvar + " = add i32 %" + globalvar + ", 0");
				}
				else{
					// %_tmp_ptr_2 = getelementptr inbounds %Arbol* %self, i32 0, i32 1
					Field field = classScope.getField(globalvar);
					if(field != null){
						System.out.println("    %" + localvar + " = getelementptr inbounds %"+ classScope.getClassType() +"* %m, i32 0, i32 " + field.index);
						System.out.println("    %" + getNextLocalInt() + " = load i32* %" + localvar);
						//printout(1,"%" + localvar + " = load i32* @" + globalvar);
					}else{
						System.out.println(";-- variable local: "+globalvar);
					}
				}

			}
			else if(id.getExprType().equals("String")){
				String globalvar = id.getId();
				String localvar = getNextLocalString();
				printIndent(1);


				if(methodScope.hasParamField(globalvar)){
					System.out.println("    %" + localvar + " = load i8* %" + globalvar);
				}
				else{
					Field field = classScope.getField(globalvar);
					System.out.println("    %" + localvar + " = getelementptr %" + classScope.getClassType() +"* %m, i32 0, i32 " + field.index);
					System.out.println("    %" + getNextLocalString() + " = load i8** %" + localvar);
				}

			}
			else if(id.getExprType().equals("Bool")){

				String globalvar = id.getId();
				String localvar = getNextLocalBool();

				if(methodScope.hasParamField(globalvar)){

					printIndent(1);
					System.out.println("%" + getNextLocalBool() + " = icmp eq i1 %" + globalvar + ", 1");
				}
				else{
					Field field = classScope.getField(globalvar);
					System.out.println("    %" + localvar + " = getelementptr inbounds %"+ classScope.getClassType() +"* %m, i32 0, i32 " + field.index);
					System.out.println("    %" + getNextLocalBool() + " = load i1* %" + localvar);
				}
			}
			else{


				String globalvar = id.getId();
				String localvar = getNextLocalPtr();

				if(methodScope.hasParamField(globalvar)){

					// printIndent(1);
					// System.out.println("%" + getNextLocalVarp() + " = icmp eq i1 %" + globalvar + ", 1");
				}
				else{

					Field field = classScope.getField(globalvar);

					if(field != null) {
						if(!field.getType().equals("SELF_TYPE")){
							System.out.println("    %" + localvar + " = getelementptr inbounds %"+ classScope.getClassType() +"* %m, i32 0, i32 " + field.index);
							System.out.println("    %" + getNextLocalPtr() + " = load %" + field.getType() +"** %" + localvar);
						}
					}else
					{
						System.out.println("; -------- FUe null field, globalvar: " + globalvar );
					}

				}
			}


			////////////////////////////////////////////////////////////

			/*
			if(type.equalsIgnoreCase("Int")){
                if(methodScope.hasParamField(id)){
        			System.out.println("    %local_int"+(++intCount )+" = add i32 %"+id  + ", 0");
                }
                else{
//                	printout(1,"%" + localvar + " = load i32* @" + globalvar);
        			System.out.println("    %local_int"+(++intCount )+" = load i32* @"+id );
                }				
	//			System.out.println("    %local_int"+(++intCount )+" = load i32* "+id +(";; 886 "+e.getClass()) );
			}
			else if(type.equalsIgnoreCase("String")){
                if(methodScope.hasParamField(id)){
                	System.out.println("    %local_string"+(++stringCount )+" = load i8* %" + id);
                }
                else{
        			System.out.println("    %local_string"+(++stringCount )+" = load i8** @"+id );
                }				
			}else if(type.equalsIgnoreCase("Bool")){
                if(methodScope.hasParamField(id)){
        			System.out.println("    %local_bool"+(++boolCount )+" = icmp eq i1 %"+id  + ", 1");
                }
                else{
//                	printout(1,"%" + localvar + " = load i32* @" + globalvar);
        			System.out.println("    %local_bool"+(++boolCount )+" = load i1* @"+id );
                }				
	//			System.out.println("    %local_int"+(++intCount )+" = load i32* "+id +(";; 886 "+e.getClass()) );
			}
			 */



			/*
			else if(type.equalsIgnoreCase("String")){
				// PREGUNTAR 
				String value = stringFieldList.get(id);
				//				System.out.println("id es: "+id);
				//				System.out.println("Value es: "+value);
//				System.out.println("    %local_string"+(++stringCount )+" = getelementptr ["+(value.length()+1)+" x i8]* "+id+", i8 0, i8 0");
				System.out.println("    %local_string"+(++stringCount )+" = load i8** "+id);

			}
			else if(type.equalsIgnoreCase("Bool")){
				// PREGUNTAR 
				String value = boolFieldList.get(id);
				System.out.println();
				System.out.println("    %local_bool"+(++boolCount )+" = load i1* "+id);
			}*/

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
				//				System.out.println("    %local_string"+(++stringCount )+" = getelementptr ["+stringLength+" x i8]* "+variable+", i8 0, i8 0");
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


	private Method getParamsTypes(String nameMethod, String classMethod) {
		// TODO Auto-generated method stub
		if(classMethod.equalsIgnoreCase("") || classMethod.equalsIgnoreCase("Object"))
			return null;
//		System.out.println(";.......m: " + nameMethod + " - C: "+classMethod);

		ClassDef methodClass = null;
		String s2 = classMethod;
		for(ClassDef c: _root) {  
			String s1 = c.getType();
//			System.out.println(";;;; s1 "+s1+ " - "+ s2 + "s2");
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
//				System.out.println(";;;; m1 "+m1+ " - "+ m2 + "m2");
				if(m1.equalsIgnoreCase( m2 )){
					return m;			
				}
			}			
		}
//		System.out.println(";;; NO HUBO MATCH C:" + classMethod + " , m: " + nameMethod);
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