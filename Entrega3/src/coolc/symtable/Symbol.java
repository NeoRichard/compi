package coolc.symtable;

public class Symbol {

    String id;
    Visibility visibility;
//    Type type;
    String type;
      Scope scope;

    public Symbol(String id, Visibility v, String t, Scope s) {
        this.id = id;
        this.visibility = v;
        this.type = t;
        this.scope = s;   
    }

    public Symbol(String id, String t, Scope s) {
        this.id = id;
        this.visibility = Visibility.PUBLIC;
        this.type = t;
        this.scope = s;   
    }

}
