package coolc.parser;

%%

%{

    private Object yylval;

    public Object getLVal() {
        return yylval;
    }

    public Position getStartPos() {
        return new Position(yyline, yycolumn);
    }

    public Position getEndPos() {
        return new Position(yyline, yycolumn);
    }

    public void yyerror(String msg) {
        //throw new Exception();
    }

%}

%public
%class Lexer
%unicode
%line
%column


%ignorecase
STRING	=  \" [^\"]* \"

ID		= [a-z][a-zA-Z_0-9]*

TYPE	= [A-Z][a-zA-Z_0-9]*


%x comment
%x multiline_comment

%implements Parser.Lexer
%int

%%


 /* keywords */
	"class" {
                return Parser.T_CLASS; 
            }
	"else" {
                return Parser.T_ELSE; 
            }
	"false" {
                return Parser.T_FALSE; 
            }
	"fi" {
                return Parser.T_FI; 
            }
	"if" {
                return Parser.T_IF; 
            }
	"in" {
                return Parser.T_IN; 
            }
	"inherits" {
                return Parser.T_INHERITS; 
            }
	"isvoid" {
                return Parser.T_ISVOID; 
            }


	"let" {
                return Parser.T_LET; 
            }
	"loop" {
                return Parser.T_LOOP; 
            }

	"pool" {
                return Parser.T_POOL; 
            }
	"then" {
                return Parser.T_THEN; 
            }
	"when" {
                return Parser.T_WHEN; 
            }
	"case" {
                return Parser.T_CASE; 
            }
	"esac" {
                return Parser.T_ESAC; 
            }

	"new" {
                return Parser.T_NEW; 
            }
	"of" {
                return Parser.T_OF; 
            }
	"not" {
                return Parser.T_NOT; 
            }
	"true" {
                return Parser.T_TRUE; 
            }
            
    "pool" {
                return Parser.T_POOL; 
            }
    [0-9]+  {   
                yylval = Integer.parseInt(yytext());
                return Parser.INT;
            }


    
    "<-"     { return Parser.T_LEFTARROW; }
    "=>"     { return Parser.T_RIGHTARROW; }
    
    "<"     { return Parser.T_LESS; }
    "<="     { return Parser.T_LESSEQ; }
    "="     { return Parser.T_EQUAL; }


    "+"     { return Parser.T_PLUS; }
    "-"     { return Parser.T_MINUS; }
    "*"     { return Parser.T_MULTIPLY; }
    "/"     { return Parser.T_DIVIDE; }

    "{"     { return Parser.T_LKEY; }
    "}"     { return Parser.T_RKEY; }

    "~"     { return Parser.T_NEG; }
    
    "("     { return Parser.T_LPAREN; }
    ")"     { return Parser.T_RPAREN; }
    "["     { return Parser.T_LBRACE; }
    "]"     { return Parser.T_RBRACE; }    
    ":"     { return Parser.T_COLON; } 
    ";"     { return Parser.T_SEMICOLON; }
    ","     { return Parser.T_COMMA; }
    "."     { return Parser.T_DOT; }
    
    "--" [^"\n"]* [\n]     { 
        // ignoramos el comentario 
         }
    "(*"     { return Parser.T_LDOCUMENTATIONCOMMENT; }
    "*)"     { return Parser.T_RDOCUMENTATIONCOMMENT; }
    
    [ \t\r\n]+  { 
        // ignoramos el espacio en blanco 
    }
    

    {STRING}    {
                yylval = yytext();
                return Parser.STRING; 
            }

    {TYPE}    {
                yylval = yytext();
                return Parser.TYPE; 
            }

    {ID}    {
                yylval = yytext();
                return Parser.ID; 
            }
    

    .           { System.out.printf("Unknown character [%s]\n", yytext());
                       return Parser.T_UNKNOWN;    }
    
    <<EOF>> { return Parser.EOF; }
