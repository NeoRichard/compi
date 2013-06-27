package coolc;

import coolc.parser.*;
import coolc.infrastructure.*;
import coolc.ast.*;

import java.io.*;
import java.util.*;


/*
 * 
 * Paso a seguir:
 * 1.- Traducir cada clase a type (diapo 3)
- después de leer la clase, imprimir para cada variable, el tipo (PRINT SOLO PARA IMPRIMIR ESTO)

classScope = _symTable.findClass(c.getType())
classScope.fieldList // lista de fields


2.- Agregar el template 
a) declare i8* @malloc(i64) // SOLO AL FINAL FINAL
b) para cada clase agregar el template adaptado (class_template.ll), reemplazando MiClase por la clase,
modificar línea 10: ptrtopoint -> i64 ptrtopoint

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
            "codegen - generates codegenii \n"
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
    
    private static void codegen(List<String> files) throws FileNotFoundException, IOException {
        for(String filename: files) {
            File f = new File(filename);
            Reader r = new FileReader(f);
            Lexer lex = new Lexer(r);

            Parser p = new Parser(lex);

            System.err.printf("codegen %s\n", filename);

            p.parse();

            SemanticAnalizer analizer = new SemanticAnalizer(p.getRoot());

            CodegenPrinter printer = new CodegenPrinter(p.getRoot(), true);
            //analizer.printErrors();
            printer.print();
        }

    }
    

}  