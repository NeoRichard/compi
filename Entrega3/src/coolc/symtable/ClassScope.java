package coolc.symtable;

class ClassScope extends Scope {
    
	String parent;

    public Symbol find(String id) {
        return this.get(id);
    }

    public void add(String id, Symbol s) {
        this.put(id, s);
    }

	public ClassScope(String parent) {
		this.parent = parent;
	}
	public String getParent(){
		return parent;
	}


}