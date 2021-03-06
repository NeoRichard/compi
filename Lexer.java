/* The following code was generated by JFlex 1.4.3 on 05-04-13 11:57 PM */

package coolc.parser;


/**
 * This class is a scanner generated by 
 * <a href="http://www.jflex.de/">JFlex</a> 1.4.3
 * on 05-04-13 11:57 PM from the specification file
 * <tt>cool.l</tt>
 */
public class Lexer implements Parser.Lexer {

  /** This character denotes the end of file */
  public static final int YYEOF = -1;

  /** initial size of the lookahead buffer */
  private static final int ZZ_BUFFERSIZE = 16384;

  /** lexical states */
  public static final int YYINITIAL = 0;
  public static final int multiline_comment = 4;
  public static final int comment = 2;

  /**
   * ZZ_LEXSTATE[l] is the state in the DFA for the lexical state l
   * ZZ_LEXSTATE[l+1] is the state in the DFA for the lexical state l
   *                  at the beginning of a line
   * l is of the form l = 2*k, k a non negative integer
   */
  private static final int ZZ_LEXSTATE[] = { 
     0,  0,  1,  1,  1, 1
  };

  /** 
   * Translates characters to character classes
   */
  private static final String ZZ_CMAP_PACKED = 
    "\11\0\1\73\1\72\2\0\1\73\22\0\1\73\1\0\1\1\5\0"+
    "\1\62\1\63\1\55\1\54\1\70\1\51\1\71\1\56\12\47\1\66"+
    "\1\67\1\50\1\52\1\53\2\0\1\12\1\4\1\6\1\40\1\16"+
    "\1\20\1\4\1\26\1\22\2\4\1\10\1\4\1\24\1\36\1\42"+
    "\1\4\1\30\1\14\1\32\1\46\1\34\1\44\3\4\1\64\1\0"+
    "\1\65\1\0\1\3\1\0\1\11\1\2\1\5\1\37\1\15\1\17"+
    "\1\2\1\25\1\21\2\2\1\7\1\2\1\23\1\35\1\41\1\2"+
    "\1\27\1\13\1\31\1\45\1\33\1\43\3\2\1\57\1\0\1\60"+
    "\1\61\uff81\0";

  /** 
   * Translates characters to character classes
   */
  private static final char [] ZZ_CMAP = zzUnpackCMap(ZZ_CMAP_PACKED);

  /** 
   * Translates DFA states to action switch labels.
   */
  private static final int [] ZZ_ACTION = zzUnpackAction();

  private static final String ZZ_ACTION_PACKED_0 =
    "\2\0\2\1\1\2\1\3\1\2\1\3\1\2\1\3"+
    "\1\2\1\3\1\2\1\3\1\2\1\3\1\2\1\3"+
    "\1\2\1\3\1\2\1\3\1\2\1\3\1\2\1\3"+
    "\1\4\1\5\1\6\1\7\1\10\1\11\1\12\1\13"+
    "\1\14\1\15\1\16\1\17\1\20\1\21\1\22\1\23"+
    "\1\24\1\25\1\26\1\0\1\27\2\2\2\3\2\2"+
    "\2\3\2\2\2\3\1\2\1\30\1\3\1\30\1\2"+
    "\1\31\1\32\1\3\1\31\1\32\2\2\2\3\2\2"+
    "\2\3\2\33\1\2\1\3\1\2\1\3\1\34\1\35"+
    "\1\0\1\36\1\37\1\40\2\2\2\3\1\41\1\2"+
    "\1\41\1\3\2\2\2\3\1\2\1\3\2\2\2\3"+
    "\1\42\1\43\1\42\1\43\2\2\2\3\1\2\1\3"+
    "\1\2\1\3\1\44\1\2\1\45\1\3\1\45\2\46"+
    "\1\47\1\50\1\47\1\50\1\2\1\3\2\2\2\3"+
    "\1\51\1\52\1\51\1\52\2\53\2\54\2\55\2\56"+
    "\2\2\2\3\1\57\1\2\1\57\1\3\1\2\1\3"+
    "\2\60";

  private static int [] zzUnpackAction() {
    int [] result = new int[160];
    int offset = 0;
    offset = zzUnpackAction(ZZ_ACTION_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAction(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /** 
   * Translates a state to a row index in the transition table
   */
  private static final int [] ZZ_ROWMAP = zzUnpackRowMap();

  private static final String ZZ_ROWMAP_PACKED_0 =
    "\0\0\0\74\0\74\0\170\0\264\0\360\0\u012c\0\u0168"+
    "\0\u01a4\0\u01e0\0\u021c\0\u0258\0\u0294\0\u02d0\0\u030c\0\u0348"+
    "\0\u0384\0\u03c0\0\u03fc\0\u0438\0\u0474\0\u04b0\0\u04ec\0\u0528"+
    "\0\u0564\0\u05a0\0\u05dc\0\u0618\0\u0654\0\u0690\0\74\0\u06cc"+
    "\0\74\0\74\0\74\0\74\0\u0708\0\74\0\74\0\74"+
    "\0\74\0\74\0\74\0\74\0\u0744\0\170\0\74\0\u0780"+
    "\0\u07bc\0\u07f8\0\u0834\0\u0870\0\u08ac\0\u08e8\0\u0924\0\u0960"+
    "\0\u099c\0\u09d8\0\u0a14\0\u0a50\0\264\0\u0a8c\0\360\0\u0ac8"+
    "\0\264\0\u0b04\0\u0b40\0\360\0\u0b7c\0\u0bb8\0\u0bf4\0\u0c30"+
    "\0\u0c6c\0\u0ca8\0\u0ce4\0\u0d20\0\u0d5c\0\264\0\360\0\u0d98"+
    "\0\u0dd4\0\u0e10\0\u0e4c\0\74\0\74\0\u0e88\0\74\0\74"+
    "\0\74\0\u0ec4\0\u0f00\0\u0f3c\0\u0f78\0\264\0\u0fb4\0\360"+
    "\0\u0ff0\0\u102c\0\u1068\0\u10a4\0\u10e0\0\u111c\0\u1158\0\u1194"+
    "\0\u11d0\0\u120c\0\u1248\0\264\0\264\0\360\0\360\0\u1284"+
    "\0\u12c0\0\u12fc\0\u1338\0\u1374\0\u13b0\0\u13ec\0\u1428\0\74"+
    "\0\u1464\0\264\0\u14a0\0\360\0\264\0\360\0\264\0\264"+
    "\0\360\0\360\0\u14dc\0\u1518\0\u1554\0\u1590\0\u15cc\0\u1608"+
    "\0\264\0\264\0\360\0\360\0\264\0\360\0\264\0\360"+
    "\0\264\0\360\0\264\0\360\0\u1644\0\u1680\0\u16bc\0\u16f8"+
    "\0\264\0\u1734\0\360\0\u1770\0\u17ac\0\u17e8\0\264\0\360";

  private static int [] zzUnpackRowMap() {
    int [] result = new int[160];
    int offset = 0;
    offset = zzUnpackRowMap(ZZ_ROWMAP_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackRowMap(String packed, int offset, int [] result) {
    int i = 0;  /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int high = packed.charAt(i++) << 16;
      result[j++] = high | packed.charAt(i++);
    }
    return j;
  }

  /** 
   * The transition table of the DFA
   */
  private static final int [] ZZ_TRANS = zzUnpackTrans();

  private static final String ZZ_TRANS_PACKED_0 =
    "\1\3\1\4\1\5\1\3\1\6\1\7\1\10\1\11"+
    "\1\12\1\5\1\6\1\5\1\6\1\13\1\14\1\15"+
    "\1\16\1\17\1\20\1\21\1\22\1\5\1\6\1\5"+
    "\1\6\1\23\1\24\1\5\1\6\1\25\1\26\1\5"+
    "\1\6\1\27\1\30\1\31\1\32\1\5\1\6\1\33"+
    "\1\34\1\35\1\36\1\3\1\37\1\40\1\41\1\42"+
    "\1\43\1\44\1\45\1\46\1\47\1\50\1\51\1\52"+
    "\1\53\1\54\2\55\74\0\1\56\1\57\72\56\2\0"+
    "\46\5\26\0\46\6\26\0\5\5\2\60\2\61\35\5"+
    "\26\0\5\6\2\62\2\63\35\6\26\0\13\5\2\64"+
    "\16\5\2\65\11\5\26\0\13\6\2\66\16\6\2\67"+
    "\11\6\26\0\5\5\2\70\2\5\2\71\33\5\26\0"+
    "\5\6\2\72\2\6\2\73\33\6\26\0\7\5\2\74"+
    "\6\5\2\75\25\5\26\0\7\6\2\76\6\6\2\77"+
    "\25\6\26\0\11\5\2\100\2\5\2\101\2\5\2\102"+
    "\23\5\26\0\11\6\2\103\2\6\2\104\2\6\2\105"+
    "\23\6\26\0\13\5\2\106\16\5\2\107\11\5\26\0"+
    "\13\6\2\110\16\6\2\111\11\6\26\0\23\5\2\112"+
    "\2\113\17\5\26\0\23\6\2\114\2\115\17\6\26\0"+
    "\15\5\2\116\27\5\26\0\15\6\2\117\27\6\26\0"+
    "\33\5\2\120\11\5\26\0\33\6\2\121\11\6\26\0"+
    "\23\5\2\122\21\5\26\0\23\6\2\123\21\6\73\0"+
    "\1\33\75\0\1\124\1\125\72\0\1\126\75\0\1\127"+
    "\103\0\1\130\65\0\1\131\110\0\2\55\2\0\7\5"+
    "\2\132\35\5\26\0\11\5\2\133\33\5\26\0\7\6"+
    "\2\134\35\6\26\0\11\6\2\135\33\6\26\0\27\5"+
    "\2\136\15\5\26\0\33\5\2\137\11\5\26\0\27\6"+
    "\2\140\15\6\26\0\33\6\2\141\11\6\26\0\11\5"+
    "\2\142\33\5\26\0\7\5\2\143\35\5\26\0\11\6"+
    "\2\144\33\6\26\0\7\6\2\145\35\6\26\0\5\5"+
    "\2\146\37\5\26\0\5\6\2\147\37\6\26\0\31\5"+
    "\2\150\13\5\26\0\23\5\2\151\21\5\26\0\31\6"+
    "\2\152\13\6\26\0\23\6\2\153\21\6\26\0\41\5"+
    "\2\154\3\5\26\0\27\5\2\155\15\5\26\0\41\6"+
    "\2\156\3\6\26\0\27\6\2\157\15\6\26\0\13\5"+
    "\2\160\31\5\26\0\43\5\2\161\1\5\26\0\13\6"+
    "\2\162\31\6\26\0\43\6\2\163\1\6\26\0\33\5"+
    "\2\164\11\5\26\0\33\6\2\165\11\6\26\0\13\5"+
    "\2\166\31\5\26\0\13\6\2\167\31\6\24\0\72\126"+
    "\1\170\1\126\2\0\11\5\2\171\33\5\26\0\13\5"+
    "\2\172\31\5\26\0\11\6\2\173\33\6\26\0\13\6"+
    "\2\174\31\6\26\0\37\5\2\175\5\5\26\0\37\6"+
    "\2\176\5\6\26\0\13\5\2\177\31\5\26\0\3\5"+
    "\2\200\41\5\26\0\13\6\2\201\31\6\26\0\3\6"+
    "\2\202\41\6\26\0\11\5\2\203\33\5\26\0\11\6"+
    "\2\204\33\6\26\0\33\5\2\205\11\5\26\0\13\5"+
    "\2\206\31\5\26\0\33\6\2\207\11\6\26\0\13\6"+
    "\2\210\31\6\26\0\21\5\2\211\23\5\26\0\13\5"+
    "\2\212\31\5\26\0\21\6\2\213\23\6\26\0\13\6"+
    "\2\214\31\6\26\0\5\5\2\215\37\5\26\0\5\6"+
    "\2\216\37\6\26\0\21\5\2\217\23\5\26\0\21\6"+
    "\2\220\23\6\26\0\11\5\2\221\33\5\26\0\11\6"+
    "\2\222\33\6\26\0\13\5\2\223\31\5\26\0\13\6"+
    "\2\224\31\6\26\0\17\5\2\225\25\5\26\0\25\5"+
    "\2\226\17\5\26\0\17\6\2\227\25\6\26\0\25\6"+
    "\2\230\17\6\26\0\35\5\2\231\7\5\26\0\17\5"+
    "\2\232\25\5\26\0\35\6\2\233\7\6\26\0\17\6"+
    "\2\234\25\6\26\0\27\5\2\235\15\5\26\0\27\6"+
    "\2\236\15\6\26\0\11\5\2\237\33\5\26\0\11\6"+
    "\2\240\33\6\24\0";

  private static int [] zzUnpackTrans() {
    int [] result = new int[6180];
    int offset = 0;
    offset = zzUnpackTrans(ZZ_TRANS_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackTrans(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      value--;
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }


  /* error codes */
  private static final int ZZ_UNKNOWN_ERROR = 0;
  private static final int ZZ_NO_MATCH = 1;
  private static final int ZZ_PUSHBACK_2BIG = 2;

  /* error messages for the codes above */
  private static final String ZZ_ERROR_MSG[] = {
    "Unkown internal scanner error",
    "Error: could not match input",
    "Error: pushback value was too large"
  };

  /**
   * ZZ_ATTRIBUTE[aState] contains the attributes of state <code>aState</code>
   */
  private static final int [] ZZ_ATTRIBUTE = zzUnpackAttribute();

  private static final String ZZ_ATTRIBUTE_PACKED_0 =
    "\1\0\1\10\1\11\33\1\1\11\1\1\4\11\1\1"+
    "\7\11\1\1\1\0\1\11\44\1\2\11\1\0\3\11"+
    "\36\1\1\11\50\1";

  private static int [] zzUnpackAttribute() {
    int [] result = new int[160];
    int offset = 0;
    offset = zzUnpackAttribute(ZZ_ATTRIBUTE_PACKED_0, offset, result);
    return result;
  }

  private static int zzUnpackAttribute(String packed, int offset, int [] result) {
    int i = 0;       /* index in packed string  */
    int j = offset;  /* index in unpacked array */
    int l = packed.length();
    while (i < l) {
      int count = packed.charAt(i++);
      int value = packed.charAt(i++);
      do result[j++] = value; while (--count > 0);
    }
    return j;
  }

  /** the input device */
  private java.io.Reader zzReader;

  /** the current state of the DFA */
  private int zzState;

  /** the current lexical state */
  private int zzLexicalState = YYINITIAL;

  /** this buffer contains the current text to be matched and is
      the source of the yytext() string */
  private char zzBuffer[] = new char[ZZ_BUFFERSIZE];

  /** the textposition at the last accepting state */
  private int zzMarkedPos;

  /** the current text position in the buffer */
  private int zzCurrentPos;

  /** startRead marks the beginning of the yytext() string in the buffer */
  private int zzStartRead;

  /** endRead marks the last character in the buffer, that has been read
      from input */
  private int zzEndRead;

  /** number of newlines encountered up to the start of the matched text */
  private int yyline;

  /** the number of characters up to the start of the matched text */
  private int yychar;

  /**
   * the number of characters from the last newline up to the start of the 
   * matched text
   */
  private int yycolumn;

  /** 
   * zzAtBOL == true <=> the scanner is currently at the beginning of a line
   */
  private boolean zzAtBOL = true;

  /** zzAtEOF == true <=> the scanner is at the EOF */
  private boolean zzAtEOF;

  /** denotes if the user-EOF-code has already been executed */
  private boolean zzEOFDone;

  /* user code: */

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



  /**
   * Creates a new scanner
   * There is also a java.io.InputStream version of this constructor.
   *
   * @param   in  the java.io.Reader to read input from.
   */
  public Lexer(java.io.Reader in) {
    this.zzReader = in;
  }

  /**
   * Creates a new scanner.
   * There is also java.io.Reader version of this constructor.
   *
   * @param   in  the java.io.Inputstream to read input from.
   */
  public Lexer(java.io.InputStream in) {
    this(new java.io.InputStreamReader(in));
  }

  /** 
   * Unpacks the compressed character translation table.
   *
   * @param packed   the packed character translation table
   * @return         the unpacked character translation table
   */
  private static char [] zzUnpackCMap(String packed) {
    char [] map = new char[0x10000];
    int i = 0;  /* index in packed string  */
    int j = 0;  /* index in unpacked array */
    while (i < 164) {
      int  count = packed.charAt(i++);
      char value = packed.charAt(i++);
      do map[j++] = value; while (--count > 0);
    }
    return map;
  }


  /**
   * Refills the input buffer.
   *
   * @return      <code>false</code>, iff there was new input.
   * 
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  private boolean zzRefill() throws java.io.IOException {

    /* first: make room (if you can) */
    if (zzStartRead > 0) {
      System.arraycopy(zzBuffer, zzStartRead,
                       zzBuffer, 0,
                       zzEndRead-zzStartRead);

      /* translate stored positions */
      zzEndRead-= zzStartRead;
      zzCurrentPos-= zzStartRead;
      zzMarkedPos-= zzStartRead;
      zzStartRead = 0;
    }

    /* is the buffer big enough? */
    if (zzCurrentPos >= zzBuffer.length) {
      /* if not: blow it up */
      char newBuffer[] = new char[zzCurrentPos*2];
      System.arraycopy(zzBuffer, 0, newBuffer, 0, zzBuffer.length);
      zzBuffer = newBuffer;
    }

    /* finally: fill the buffer with new input */
    int numRead = zzReader.read(zzBuffer, zzEndRead,
                                            zzBuffer.length-zzEndRead);

    if (numRead > 0) {
      zzEndRead+= numRead;
      return false;
    }
    // unlikely but not impossible: read 0 characters, but not at end of stream    
    if (numRead == 0) {
      int c = zzReader.read();
      if (c == -1) {
        return true;
      } else {
        zzBuffer[zzEndRead++] = (char) c;
        return false;
      }     
    }

	// numRead < 0
    return true;
  }

    
  /**
   * Closes the input stream.
   */
  public final void yyclose() throws java.io.IOException {
    zzAtEOF = true;            /* indicate end of file */
    zzEndRead = zzStartRead;  /* invalidate buffer    */

    if (zzReader != null)
      zzReader.close();
  }


  /**
   * Resets the scanner to read from a new input stream.
   * Does not close the old reader.
   *
   * All internal variables are reset, the old input stream 
   * <b>cannot</b> be reused (internal buffer is discarded and lost).
   * Lexical state is set to <tt>ZZ_INITIAL</tt>.
   *
   * @param reader   the new input stream 
   */
  public final void yyreset(java.io.Reader reader) {
    zzReader = reader;
    zzAtBOL  = true;
    zzAtEOF  = false;
    zzEOFDone = false;
    zzEndRead = zzStartRead = 0;
    zzCurrentPos = zzMarkedPos = 0;
    yyline = yychar = yycolumn = 0;
    zzLexicalState = YYINITIAL;
  }


  /**
   * Returns the current lexical state.
   */
  public final int yystate() {
    return zzLexicalState;
  }


  /**
   * Enters a new lexical state
   *
   * @param newState the new lexical state
   */
  public final void yybegin(int newState) {
    zzLexicalState = newState;
  }


  /**
   * Returns the text matched by the current regular expression.
   */
  public final String yytext() {
    return new String( zzBuffer, zzStartRead, zzMarkedPos-zzStartRead );
  }


  /**
   * Returns the character at position <tt>pos</tt> from the 
   * matched text. 
   * 
   * It is equivalent to yytext().charAt(pos), but faster
   *
   * @param pos the position of the character to fetch. 
   *            A value from 0 to yylength()-1.
   *
   * @return the character at position pos
   */
  public final char yycharat(int pos) {
    return zzBuffer[zzStartRead+pos];
  }


  /**
   * Returns the length of the matched text region.
   */
  public final int yylength() {
    return zzMarkedPos-zzStartRead;
  }


  /**
   * Reports an error that occured while scanning.
   *
   * In a wellformed scanner (no or only correct usage of 
   * yypushback(int) and a match-all fallback rule) this method 
   * will only be called with things that "Can't Possibly Happen".
   * If this method is called, something is seriously wrong
   * (e.g. a JFlex bug producing a faulty scanner etc.).
   *
   * Usual syntax/scanner level error handling should be done
   * in error fallback rules.
   *
   * @param   errorCode  the code of the errormessage to display
   */
  private void zzScanError(int errorCode) {
    String message;
    try {
      message = ZZ_ERROR_MSG[errorCode];
    }
    catch (ArrayIndexOutOfBoundsException e) {
      message = ZZ_ERROR_MSG[ZZ_UNKNOWN_ERROR];
    }

    throw new Error(message);
  } 


  /**
   * Pushes the specified amount of characters back into the input stream.
   *
   * They will be read again by then next call of the scanning method
   *
   * @param number  the number of characters to be read again.
   *                This number must not be greater than yylength()!
   */
  public void yypushback(int number)  {
    if ( number > yylength() )
      zzScanError(ZZ_PUSHBACK_2BIG);

    zzMarkedPos -= number;
  }


  /**
   * Resumes scanning until the next regular expression is matched,
   * the end of input is encountered or an I/O-Error occurs.
   *
   * @return      the next token
   * @exception   java.io.IOException  if any I/O-Error occurs
   */
  public int yylex() throws java.io.IOException {
    int zzInput;
    int zzAction;

    // cached fields:
    int zzCurrentPosL;
    int zzMarkedPosL;
    int zzEndReadL = zzEndRead;
    char [] zzBufferL = zzBuffer;
    char [] zzCMapL = ZZ_CMAP;

    int [] zzTransL = ZZ_TRANS;
    int [] zzRowMapL = ZZ_ROWMAP;
    int [] zzAttrL = ZZ_ATTRIBUTE;

    while (true) {
      zzMarkedPosL = zzMarkedPos;

      boolean zzR = false;
      for (zzCurrentPosL = zzStartRead; zzCurrentPosL < zzMarkedPosL;
                                                             zzCurrentPosL++) {
        switch (zzBufferL[zzCurrentPosL]) {
        case '\u000B':
        case '\u000C':
        case '\u0085':
        case '\u2028':
        case '\u2029':
          yyline++;
          yycolumn = 0;
          zzR = false;
          break;
        case '\r':
          yyline++;
          yycolumn = 0;
          zzR = true;
          break;
        case '\n':
          if (zzR)
            zzR = false;
          else {
            yyline++;
            yycolumn = 0;
          }
          break;
        default:
          zzR = false;
          yycolumn++;
        }
      }

      if (zzR) {
        // peek one character ahead if it is \n (if we have counted one line too much)
        boolean zzPeek;
        if (zzMarkedPosL < zzEndReadL)
          zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        else if (zzAtEOF)
          zzPeek = false;
        else {
          boolean eof = zzRefill();
          zzEndReadL = zzEndRead;
          zzMarkedPosL = zzMarkedPos;
          zzBufferL = zzBuffer;
          if (eof) 
            zzPeek = false;
          else 
            zzPeek = zzBufferL[zzMarkedPosL] == '\n';
        }
        if (zzPeek) yyline--;
      }
      zzAction = -1;

      zzCurrentPosL = zzCurrentPos = zzStartRead = zzMarkedPosL;
  
      zzState = ZZ_LEXSTATE[zzLexicalState];


      zzForAction: {
        while (true) {
    
          if (zzCurrentPosL < zzEndReadL)
            zzInput = zzBufferL[zzCurrentPosL++];
          else if (zzAtEOF) {
            zzInput = YYEOF;
            break zzForAction;
          }
          else {
            // store back cached positions
            zzCurrentPos  = zzCurrentPosL;
            zzMarkedPos   = zzMarkedPosL;
            boolean eof = zzRefill();
            // get translated positions and possibly new buffer
            zzCurrentPosL  = zzCurrentPos;
            zzMarkedPosL   = zzMarkedPos;
            zzBufferL      = zzBuffer;
            zzEndReadL     = zzEndRead;
            if (eof) {
              zzInput = YYEOF;
              break zzForAction;
            }
            else {
              zzInput = zzBufferL[zzCurrentPosL++];
            }
          }
          int zzNext = zzTransL[ zzRowMapL[zzState] + zzCMapL[zzInput] ];
          if (zzNext == -1) break zzForAction;
          zzState = zzNext;

          int zzAttributes = zzAttrL[zzState];
          if ( (zzAttributes & 1) == 1 ) {
            zzAction = zzState;
            zzMarkedPosL = zzCurrentPosL;
            if ( (zzAttributes & 8) == 8 ) break zzForAction;
          }

        }
      }

      // store back cached position
      zzMarkedPos = zzMarkedPosL;

      switch (zzAction < 0 ? zzAction : ZZ_ACTION[zzAction]) {
        case 47: 
          { return Parser.T_ISVOID;
          }
        case 49: break;
        case 35: 
          { return Parser.T_NOT;
          }
        case 50: break;
        case 7: 
          { return Parser.T_EQUAL;
          }
        case 51: break;
        case 43: 
          { return Parser.T_POOL;
          }
        case 52: break;
        case 48: 
          { return Parser.T_INHERITS;
          }
        case 53: break;
        case 30: 
          { return Parser.T_RIGHTARROW;
          }
        case 54: break;
        case 34: 
          { return Parser.T_NEW;
          }
        case 55: break;
        case 28: 
          { return Parser.T_LEFTARROW;
          }
        case 56: break;
        case 40: 
          { return Parser.T_ESAC;
          }
        case 57: break;
        case 33: 
          { return Parser.T_LET;
          }
        case 58: break;
        case 5: 
          { return Parser.T_LESS;
          }
        case 59: break;
        case 25: 
          { return Parser.T_IF;
          }
        case 60: break;
        case 32: 
          { return Parser.T_LDOCUMENTATIONCOMMENT;
          }
        case 61: break;
        case 13: 
          { return Parser.T_NEG;
          }
        case 62: break;
        case 22: 
          { // ignoramos el espacio en blanco
          }
        case 63: break;
        case 4: 
          { yylval = Integer.parseInt(yytext());
                return Parser.INT;
          }
        case 64: break;
        case 46: 
          { return Parser.T_FALSE;
          }
        case 65: break;
        case 1: 
          { System.out.printf("Unknown character [%s]\n", yytext());
                       return Parser.T_UNKNOWN;
          }
        case 66: break;
        case 9: 
          { return Parser.T_MULTIPLY;
          }
        case 67: break;
        case 29: 
          { return Parser.T_LESSEQ;
          }
        case 68: break;
        case 20: 
          { return Parser.T_COMMA;
          }
        case 69: break;
        case 11: 
          { return Parser.T_LKEY;
          }
        case 70: break;
        case 3: 
          { yylval = yytext();
                return Parser.TYPE;
          }
        case 71: break;
        case 19: 
          { return Parser.T_SEMICOLON;
          }
        case 72: break;
        case 18: 
          { return Parser.T_COLON;
          }
        case 73: break;
        case 10: 
          { return Parser.T_DIVIDE;
          }
        case 74: break;
        case 14: 
          { return Parser.T_LPAREN;
          }
        case 75: break;
        case 2: 
          { yylval = yytext();
                return Parser.ID;
          }
        case 76: break;
        case 41: 
          { return Parser.T_THEN;
          }
        case 77: break;
        case 36: 
          { // ignoramos el comentario
          }
        case 78: break;
        case 15: 
          { return Parser.T_RPAREN;
          }
        case 79: break;
        case 27: 
          { return Parser.T_OF;
          }
        case 80: break;
        case 39: 
          { return Parser.T_ELSE;
          }
        case 81: break;
        case 37: 
          { return Parser.T_CASE;
          }
        case 82: break;
        case 24: 
          { return Parser.T_FI;
          }
        case 83: break;
        case 26: 
          { return Parser.T_IN;
          }
        case 84: break;
        case 45: 
          { return Parser.T_CLASS;
          }
        case 85: break;
        case 31: 
          { return Parser.T_RDOCUMENTATIONCOMMENT;
          }
        case 86: break;
        case 23: 
          { yylval = yytext();
                return Parser.STRING;
          }
        case 87: break;
        case 44: 
          { return Parser.T_WHEN;
          }
        case 88: break;
        case 42: 
          { return Parser.T_TRUE;
          }
        case 89: break;
        case 12: 
          { return Parser.T_RKEY;
          }
        case 90: break;
        case 6: 
          { return Parser.T_MINUS;
          }
        case 91: break;
        case 38: 
          { return Parser.T_LOOP;
          }
        case 92: break;
        case 21: 
          { return Parser.T_DOT;
          }
        case 93: break;
        case 8: 
          { return Parser.T_PLUS;
          }
        case 94: break;
        case 17: 
          { return Parser.T_RBRACE;
          }
        case 95: break;
        case 16: 
          { return Parser.T_LBRACE;
          }
        case 96: break;
        default: 
          if (zzInput == YYEOF && zzStartRead == zzCurrentPos) {
            zzAtEOF = true;
              {
                return Parser.EOF;
              }
          } 
          else {
            zzScanError(ZZ_NO_MATCH);
          }
      }
    }
  }


}
