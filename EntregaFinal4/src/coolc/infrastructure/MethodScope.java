package coolc.infrastructure;

import coolc.ast.*;
import java.util.*;

public class MethodScope extends Scope {
    
    private String _returnType;
    public String getReturnType() {
        return _returnType;
    }

    private ArrayList<String> _params;
    public List<String> getParams() {
        return _params;
    }

    public boolean hasParamField(String name) {
    	/*
    	System.out.println("name: "+name);
    	System.out.println("_params.contains(name);: "+_params.contains(name));
    	System.out.println("_params.contains(@name);: "+_params.contains("@"+name));
    	System.out.println("_params.contains(%name);: "+_params.contains("%"+name));
*/
    	return _params.contains(name);
    }

    
    public MethodScope(ClassScope parent, String returnType, Node node) {
        super(parent);
        _returnType = returnType;
        _params = new ArrayList<String>();
        setNode(node);
    }

    @Override
    public void addField(String name, Field field){
        super.addField(name, field);
        _params.add(name);
    }


    public String getSignature() {
        StringBuilder signature = new StringBuilder();
    
        for(Map.Entry<String, Field> f : this.listFields()) {
            signature.append(f.getValue().getType()).append(" -> ");
        }
        signature.append(_returnType);

        return signature.toString();
    }


}