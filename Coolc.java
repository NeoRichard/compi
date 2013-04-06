package coolc;

import coolc.parser.*;

import java.io.*;
import java.util.*;

public class Coolc {

	public static void printInstructions() {
		System.out.println(
				"Usage Coolc <action> file1 file2 ... filen\n" +
						"Available actions: \n" +
						"scan - scans each files and outputs a token list\n" +
						"parse - parses each file and outputs the syntax tree\n"
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

			for(int token = lex.yylex(); token != Parser.EOF; token = lex.yylex()) {

				Position pos = lex.getStartPos();

				switch(token) {

				case Parser.STRING:
					String s = (String) lex.getLVal().toString();
					s = s.substring(1, s.length()-1);
					s = s.replace("\\n", "");

					System.out.printf("%s:%2d:%2d  String<%s>\n", f.getPath(), pos.line, pos.column, s);
					break;

				case Parser.TYPE:
					System.out.printf("%s:%2d:%2d  Type<%s>\n", f.getPath(), pos.line, pos.column, lex.getLVal());
					break;


				case Parser.T_FALSE:
					System.out.printf("%s:%2d:%2d  Boolean<False>\n", f.getPath(), pos.line, pos.column);
					break;
				case Parser.T_TRUE:
					System.out.printf("%s:%2d:%2d  Boolean<True>\n", f.getPath(), pos.line, pos.column);
					break;


				case Parser.T_NEG:
					System.out.printf("%s:%2d:%2d  %s\n", f.getPath(), pos.line, pos.column, Parser.getTokenName(token));
					break;



				case Parser.ID:
					System.out.printf("%s:%2d:%2d  Id<%s>\n", f.getPath(), pos.line, pos.column, lex.getLVal());
					break;

				case Parser.INT:
					System.out.printf("%s:%2d:%2d  Int<%s>\n", f.getPath(), pos.line, pos.column, lex.getLVal());
					break;

				default:
					System.out.printf("%s:%2d:%2d  %s\n", f.getPath(), pos.line, pos.column, Parser.getTokenName(token));
					break;
				}

			}
		}
	}

	private static void parse(List<String> files) throws FileNotFoundException, IOException { 

	}



}  