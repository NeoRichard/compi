package coolc.symtable;

public class Type {

    public static final Type Int = new Type(SymbolType.INT);
    public static final Type Boolean = new Type(SymbolType.BOOLEAN);
    public static final Type Float = new Type(SymbolType.FLOAT);
    public static final Type String = new Type(SymbolType.STRING);
    public static final Type Void = new Type(SymbolType.VOID);
    public static final Type Error = new Type(SymbolType.ERROR);

    private SymbolType type;
    public SymbolType getType()
    {
        return type;
    }

    private String className;
    public String getClassName()
    {
        return className;
    }

    private boolean array;
    public boolean isArray()
    {
        return array;
    }

    public Type arrayType()
    {
        Type current = new Type(this.type);
        current.className = this.className;
        current.array = true;
        return current;
    }
    public void isArray(boolean b) {
        array = b;
    }

    protected Type(SymbolType basicType) {
        this.type = basicType;
        this.array = false;
    }

    public Type(String className) {
        this.type = SymbolType.CLASS;
        this.className = className;
        this.array = false;
    }

    public String toString() {
        String t = null;

        switch (type) {
            case INT:
                t = "int";
                break;

            case BOOLEAN:
                t = "boolean";
                break;

            case FLOAT:
                t = "float";
                break;

            case STRING:
                t = "String";
                break;

            case VOID:
                t = "void";
                break;
            
            case CLASS:
                t = className;
                break;
                
            case ERROR:
                t = "error";
                break;
        }

        if (this.isArray()) {
            t += "[]";
        }

        return t;
    }

    public boolean isEqual(Type t)
    {
        return ( t == this ||  (t.type == this.type && t.array == this.array && t.className == this.className));
    }
}