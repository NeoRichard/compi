package coolc;

import coolc.parser.*;
import coolc.infrastructure.*;
import coolc.ast.*;

import java.io.*;
import java.util.*;

/*
 * Ejecutar con 
 * llvm-link-3.2 output.ll coolstd.ll | lli-3.2
 * y los que usen in_string o in_int, debe hacerse el linking e intepretación por separado
llvm-link-3.2 output.ll coolstd.ll -o output.bc

lli-3.2 output.bc
 */

/*
 * EL PROGRAMA DEBE MANEJAR:
 * - tipos básicos (String, Int, Bool) <- esto implica una sola clase
 * - funciones
 * - Expresiones Binarias
 * - Asignaciones
 * - Expresion if
 * 
 * 
 * Tipo Cool 	Valor inicial 	Tipo LLVM
	Int 			0 				i32
	Bool 			false 			i1
	String 			"" 				i8*
 *
 *Según las reglas de Cool, sólo debe existir un método main(), y se
llama sobre una nueva instancia de la clase que lo contiene.
Para esta entrega no se usan las clases, por lo que basta con que
generen el código del main, y traten el self como un puntero null
 *
 *
 * Su objetivo es entregar programas en LLVM IR ejecutables
 * 
 * Se les dará una implementación de los métodos de los tipos básicos
(coolstd.ll)
 *
 *
 * Compilar código
clang -O1 -S -o - -emit-llvm file.c
-O1 es optimizacion
 *
 */


public class Coolc {
	
	

    public static void printInstructions() {
        System.out.println(
            "Usage Coolc <action> file1 file2 ... filen\n" +
            "Available actions: \n" +
            "scan - scans each files and outputs a token list\n" +
            "parse - parses each file and outputs the syntax tree\n" +
            "symtable - generates and prints the symbol table\n" +
            "semantic - performs semanic analysis\n" +
            "codegen - print cool program equivalent\n"
            );
    }

    public static void main(String[] args) throws FileNotFoundException, IOException {


        if (args.length < 2) {
            printInstructions();
            return;
        }


        String action = args[0];

        List<String> files = Arrays.asList(args).subList(1, args.length);

        switch(action) {

            case "scan":
                scan(files);
                break;

            case "parse":
                parse(files);
                break;

            case "symtable":
                symTable(files);
                break;

            case "semantic":
                semantic(files);
                break;

            case "codegen":
            	codegen(files);
                break;

            default:
                printInstructions();
                return;
        }


    }

    private static void scan(List<String> files) throws FileNotFoundException, IOException {

        for(String filename: files) {
            File f = new File(filename);
            Reader r = new FileReader(f);
            Lexer lex = new Lexer(r);
            Random rand = new Random();

            for(int token = lex.yylex(); token != Parser.EOF; token = lex.yylex()) {

                Position pos = lex.getStartPos();

                switch(token) {

                    case Parser.ID:
                        System.out.printf("%s:%2d:%2d  Id<%s>\n", f.getPath(), pos.line, pos.column, lex.getLVal());
                        break;

                    case Parser.INTEGER:
                        System.out.printf("%s:%2d:%2d  Int<%s>\n", f.getPath(), pos.line, pos.column, lex.getLVal());
                        break;
                        

                    case Parser.BOOL:
                        System.out.printf("%s:%2d:%2d  Boolean<%s>\n", f.getPath(), pos.line, pos.column, (boolean)lex.getLVal() ? "True" : "False");
                        break;
                        
                    case Parser.TYPE:
                        System.out.printf("%s:%2d:%2d  String<%s>\n", f.getPath(), pos.line, pos.column, lex.getLVal());
                        break;

                    case Parser.STRING:
                        String strval =((String)lex.getLVal()).replace("\n","").replace("\b", "").replace("\t", "").replace(">", "");
                        strval = strval.substring(0, Math.min(strval.length(), 20));

                        System.out.printf("%s:%2d:%2d  String<%s>\n", f.getPath(), pos.line, pos.column, strval);
                        break;

                    default:
                        System.out.printf("%s:%2d:%2d  %s\n", f.getPath(), pos.line, pos.column, Parser.getTokenName(token));
                        break;
                }

            }
        }
    }

    private static void parse(List<String> files) throws FileNotFoundException, IOException { 



        for(String filename: files) {
            File f = new File(filename);
            Reader r = new FileReader(f);
            Lexer lex = new Lexer(r);

            Parser p = new Parser(lex);

            System.err.printf("parsing %s\n", filename);

            p.parse();

            AstPrinter printer = new AstPrinter(p.getRoot());
            printer.print();
        }

    }

    private static void symTable(List<String> files) throws FileNotFoundException, IOException {
        for(String filename: files) {
            File f = new File(filename);
            Reader r = new FileReader(f);
            Lexer lex = new Lexer(r);

            Parser p = new Parser(lex);

            System.err.printf("generating symTable %s\n", filename);

            p.parse();

            SymTable table = new SymTable(p.getRoot());

            table.printErrors();
            table.print();
            
        }

    }

    private static void codegen(List<String> files) throws FileNotFoundException, IOException {
        for(String filename: files) {
            File f = new File(filename);
            Reader r = new FileReader(f);
            Lexer lex = new Lexer(r);

            Parser p = new Parser(lex);

            System.err.printf("codegen %s\n", filename);

            p.parse();

            SemanticAnalizer analizer = new SemanticAnalizer(p.getRoot());

//            analizer.printErrors();
            if(  true)
        	try {
    			System.setOut(new PrintStream(new FileOutputStream("/home/richard/Escritorio/Entrega4/output.ll")));
    		} catch (FileNotFoundException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}

//            CodeGenPrinter printer = new CodeGenPrinter(p.getRoot(), true);
            ClassPrinter printer = new ClassPrinter(p.getRoot(), true);
            printer.print();
        }

    }


    private static void semantic(List<String> files) throws FileNotFoundException, IOException {    	
        for(String filename: files) {
            File f = new File(filename);
            Reader r = new FileReader(f);
            Lexer lex = new Lexer(r);

            Parser p = new Parser(lex);

            System.err.printf("semantic analysis %s\n", filename);

            p.parse();

            SemanticAnalizer analizer = new SemanticAnalizer(p.getRoot());

            AstPrinter printer = new AstPrinter(p.getRoot(), true);
            analizer.printErrors();
            printer.print();
        }

    }

}  