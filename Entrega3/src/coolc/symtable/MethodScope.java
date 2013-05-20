package coolc.symtable;

import java.util.Hashtable;

class MethodScope extends Scope {
    
    ClassScope parent;
    Hashtable<String, Symbol> parameters;

    public ClassScope getClassScope() {
        return parent;
    }

    public MethodScope(ClassScope parent) {
        this.parameters = new Hashtable<String, Symbol>();
        this.parent = parent;
    }

    @Override
    public Symbol find(String id) {
        Symbol s = parameters.get(id);
        if ( s == null ) {
            s = this.get(id);
        }

        if ( s == null ) {
            s = parent.get(id);
        }
        return s;
    }

    public void add(String id, Symbol s) {
        this.put(id, s);        
    }

    public void addParameter(String id, Symbol s)
    {
        this.parameters.put(id, s);
    }


}