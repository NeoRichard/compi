package coolc.symtable;



public class MethodSymbol extends Symbol {

    MethodScope methodScope;

    public MethodSymbol(String id, Visibility v, String t, Scope s, MethodScope methodScope) {
//        public MethodSymbol(String id, Visibility v, Type t, Scope s, MethodScope methodScope) {
        super(id, v, t, s);
        this.methodScope = methodScope;
    }
    public MethodSymbol(String id, String t, Scope s, MethodScope methodScope) {
//      public MethodSymbol(String id, Visibility v, Type t, Scope s, MethodScope methodScope) {
      super(id, Visibility.PUBLIC, t, s);
      this.methodScope = methodScope;
  }

}