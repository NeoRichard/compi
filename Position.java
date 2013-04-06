package coolc.parser;

public class Position {

    public int line;

    public int column;

    public Position(int line, int column) {
        this.line = line;
        this.column = column;
    }

    public String toString() {
        return "{%s %s, %s}".format(this.getClass().getName(), line, column);
    }
}