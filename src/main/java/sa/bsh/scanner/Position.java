package sa.bsh.scanner;

/**
 * Token position information, line number and column.
 */
public class Position {
    private final int line;
    private final int column;

    /**
     * Create a new object representing the position information in the file.
     * @param line The line number, starting from one.
     * @param column The column number, starting from one.
     */
    public Position(int line, int column) {
        assert line >= 1: "Line number must be >= 1";
        assert column >= 1: "Column number must be >= 1";

        this.line = line;
        this.column = column;
    }

    /**
     * Get token line number, starting from one.
     * @return Line number.
     */
    public int getLine() {
        return line;
    }

    /**
     * Get token column number, starting from one.
     * @return Column number.
     */
    public int getColumn() {
        return column;
    }

    /**
     * Check if two tokens have the same line number and column number.
     * @param o The object to be tested for equality.
     * @return {@code true} if the two objects are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Position))
            return false;
        if (this == o)
            return true;

        Position pos = (Position) o;
        return line == pos.line && column == pos.column;
    }

    /**
     * Compute the hash code of a given object.
     * @return Hash code of the object.
     */
    @Override
    public int hashCode() {
        int result = line;
        result = 31 * result + column;
        return result;
    }

    @Override
    public String toString() {
        return String.format("Position(%d:%d)", line, column);
    }
}
