package coolc.ast;

public class Case extends Node {

    private String _id;
    public String getId() {
        return _id;
    }

    private String _type;
    public String getType() {
        return _type;
    }

    private Expr _value;
    public Expr getValue() {
        return _value;
    }


    public Case(String id, String type, Expr value) {
        _id = id;
        _type = type;
        _value = value;
    }
}
