package coolc.symtable;

import java.util.LinkedHashMap;
import java.util.ArrayList;
import java.lang.IllegalArgumentException;

public abstract class Scope {
    
    protected LinkedHashMap<String, Symbol> symbols;
    ArrayList<Scope> subscopes;

    public LinkedHashMap<String, Symbol> getSymbols()
    {
        return this.symbols;
    }

    protected Scope() {

        symbols = new LinkedHashMap<String, Symbol>();
        subscopes = new ArrayList<Scope>();
    }
    
    public abstract Symbol find(String id);
    public abstract void add(String id, Symbol s);

    protected void put(String id, Symbol s) throws IllegalArgumentException {
        if( this.symbols.containsKey(id) ) {
            throw new IllegalArgumentException("symbol " + id + " already on scope");
        }
        else {
            s.scope = this;
            symbols.put(id, s);
        }

    }

    public Symbol get(String id) {
        return symbols.get(id);
    }



}