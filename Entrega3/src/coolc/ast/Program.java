package coolc.ast;

import java.util.ArrayList;

import coolc.parser.Position;

public class Program extends ArrayList<ClassDef> {
	Position p;
    public Program(ClassDef c, Position p) {
    	this.p = p;
        this.add(c);
    }
	public Position getP() {
		return p;
	}
    
    
    
}
