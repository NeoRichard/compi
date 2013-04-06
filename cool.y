%{
package coolc.parser;

%}

%output  "Parser.java"
%language "Java"


%define parser_class_name "Parser"
%define public

%code {

    public static String getTokenName(int t) {
        return yytname_[t-255];
    }

}

%token T_UNKNOWN

/* comments */
%token T_ENDOFLINECOMMENT "--"
%token T_LDOCUMENTATIONCOMMENT "(*"
%token T_RDOCUMENTATIONCOMMENT "*)"


%token  T_LKEY "{" T_RKEY "}"

%token  T_LPAREN "(" T_RPAREN ")"
%token  T_LBRACE  "[" T_RBRACE "]"
%token  T_COLON ":"
%token  T_SEMICOLON ";"
%token  T_COMMA ","
%token  T_DOT "."

%token T_PLUS "+"
%token T_MINUS "-"
%token T_MULTIPLY "*"
%token T_DIVIDE "/"

%token T_LESS "<"
%token T_LESSEQ "<="
%token T_EQUAL "="

%token T_CARRIAGERETURN "\\b"
%token T_TAB "\\t"
%token T_NEWLINE "\\n"
%token T_BACKSPACE "\\f"

%token T_ESCAPE "\\"
%token T_AT "@"
%token T_NEG "~"

%token T_CLASS "class"
%token T_ELSE "else"
%token T_FALSE "false"
%token T_FI "fi"
%token T_IF "if"
%token T_IN "in"
%token T_INHERITS "inherits"
%token T_ISVOID "isvoid"
%token T_LET "let"
%token T_LOOP "loop"
%token T_POOL "pool"
%token T_THEN "then"
%token T_WHEN "when"
%token T_CASE "case"
%token T_ESAC "esac"
%token T_NEW "new"
%token T_OF "of"
%token T_NOT "not"
%token T_TRUE "true"


%token T_LEFTARROW "<-"
%token T_RIGHTARROW "=>"

%token <String> TYPE
%token <String> ID
%token <String> STRING
%token <Int> INT

%%

expr
    : INT
    | expr "+" expr
    | ID "(" param_list ")"
    | "[" matrix "]"
    | ID
    ;

param_list
    : /* empty */ 
    | param_list "," expr
    ;

matrix
    : vector
    | matrix ";" vector
    ;

vector
    : expr
    | vector "," expr
    ;

%%
