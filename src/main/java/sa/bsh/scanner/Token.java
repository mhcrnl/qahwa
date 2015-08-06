package sa.bsh.scanner;

/**
 * Token type that holds information about a token and its related helper methods.
 *
 * @author Barakat
 */
public class Token {
    private final Type type;
    private final String attr;
    private final Position position;

    /*
     * Token list. The order of tokens is important. When modifying this enum, the following assumptions
     * must hold true.
     *
     * For any token t:
     *  * t in Number set if INTEGER <= t <= DOUBLE
     *  * t in Reserved set if VAR <= t <= END
     *
     *  Also, when setting a predefine attribute like EOF, their attributes are messages, keep letters in
     *  lower case form.
     */
    public enum Type {
        // End of input, e.g. file
        EOF("end of input"),
        // Symbols
        COLON(":"), NEWLINE("new line"),
        // Identifier
        IDENTIFIER,
        // Literals
        STRING, CHAR, LONG, INTEGER, FLOAT, DOUBLE,
        // Operators
        ADD("+"), SUB("-"), MUL("*"), DIV("/"), IADD("+="), ISUB("-="), IMUL("*="), IDIV("/="), ASS("="), EQL("=="),
        NEQ("!="), LT("<"), LTE("<="), GT(">"), GTE(">="), BNOT("~"), BAND("&"), BOR("|"), LAND("and"), LOR("or"),
        LNOT("not"),
        // Reserved words
        VAR("var"), IF("if"), ELSEIF("elseif"), ELSE("else"), WHILE("while"), TRUE("true"), FALSE("false"),
        NULL("null"), END("end"),
        // Illegal
        ILLEGAL;

        private final String attr;

        // Set the attribute to a predefined one, mostly symbols and reserved words.
        Type(String attr) {
            this.attr = attr;
        }

        // Set the attribute to null, for tokens that must have an attribute.
        Type() {
            this.attr = null;
        }

        /**
         * Get the predefined attribute for the given token.
         * @return Predefined attribute string representation.
         */
        public String getAttr() {
            assert hasPredefinedAttr() : "The given token type has not predefined attribute. Check hasPredefinedAttr()";
            return attr;
        }

        /**
         * Check if the current token type has a predefined attribute (e.g. reserved words, symbols .etc).
         * @return {@code true} If the token type has a predefine attribute.
         */
        public boolean hasPredefinedAttr() {
            return attr != null;
        }
    }

    /**
     * Create a token with type, position, and attribute information.
     * @param type The type of the token.
     * @param position The current position of the token.
     * @param attr The token attribute of the token.
     * @see sa.bsh.scanner.Token.Type
     */
    public Token(Type type, Position position, String attr) {
        // For tokens that have no attributes, e.g. VAR, we are expecting attr to be null.
        if (type.hasPredefinedAttr())
            assert attr == null : "The given token is not expected to have an attribute";
        // For tokens that have attributes, e.g. STRING, we are expecting attr to be set to that attribute.
        else
            assert attr != null : "The given token is expected to have an attribute";

        this.type = type;
        this.position = position;
        this.attr = attr;
    }

    public Token(Type type, int line, int column, String attr) {
        this(type, new Position(line, column), attr);
    }

    /**
     * Create a token with type and position. Attribute is set to null.
     * @param type The type of the token.
     * @param position The current position of the token.
     * @see sa.bsh.scanner.Token.Type
     */
    public Token(Type type, Position position) {
        this(type, position, null);
    }

    public Token(Type type, int line, int column) {
        this(type, new Position(line, column));
    }

    /**
     * Create a new copy of a given {@link sa.bsh.scanner.Token}.
     * @param token The token object to be copyied.
     */
    public Token(Token token) {
        this(token.type, token.position, token.attr);
    }

    /**
     * Get the token type.
     * @return The token type.
     * @see sa.bsh.scanner.Token.Type
     */
    public Type getType() {
        return type;
    }

    /**
     * Get the token lexeme.
     * @return String representing the token attribute.
     */
    public String getAttr() {
        if (type.hasPredefinedAttr())
            return type.getAttr();
        return attr;
    }

    /**
     * Get the token position in the input stream, i.e. line number and column.
     * @return The position of the token.
     * @see sa.bsh.scanner.Token.Type
     */
    public Position getPosition() {
        return position;
    }

    /**
     * Get token line number.
     * @return Line number.
     */
    public int getLine() {
        return position.getLine();
    }

    /**
     * Get token column number.
     * @return Column number
     */
    public int getColumn() {
        return position.getColumn();
    }

    /**
     * Check if the given two tokens are equal.
     * @param o The other object to check for equality
     * @return {@code true} If object {@code o} is equal to the current object.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Token token = (Token) o;

        if (type != token.type)
            return false;
        if (!type.hasPredefinedAttr() && !attr.equals(token.attr)) {
            return false;
        }

        return position.equals(token.position);

    }

    /**
     * Compute the hash of the current object.
     * @return The result hash code.
     */
    @Override
    public int hashCode() {
        int result = type.hashCode();
        result = 31 * result + (attr != null ? attr.hashCode() : 0);
        result = 31 * result + position.hashCode();
        return result;
    }

    /**
     * Convert token to string.
     * @return String recreation of the current object in the form {@code Token(type, attribute, @line:column)} or
     * {@code Token(type, @line:column)} if token has no attributes.
     */
    @Override
    public String toString() {
        if (type.hasPredefinedAttr())
            return String.format("Token(%s, @%d:%d)", type.getAttr(), position.getLine(), position.getColumn());
        return String.format("Token(%s=%s, @%d:%d)", type, attr, position.getLine(), position.getColumn());
    }

    /**
     * Check if the token type is the same of the given token.
     * @param type The type of the token to be checked.
     * @return {@code true} If the token if of the same type.
     * @see sa.bsh.scanner.Token.Type
     */
    public boolean isToken(Type type) {
        return this.type == type;
    }

    /**
     * Check if the token is the end of input.
     * @return {@code true} if the token is the end of input.
     */
    public boolean isEndOfInput() {
        return type == Type.EOF;
    }

    public boolean isNumber() {
        return type.compareTo(Type.INTEGER) >= 0 && type.compareTo(Type.DOUBLE) <= 0;
    }

    /**
     * Check if the token is a reserved word.
     * @return {@code true} if the token is a reserved word.
     */
    public boolean isReservedWord() {
        return type.compareTo(Type.VAR) >= 0 && type.compareTo(Type.END) <= 0;
    }

    /**
     * Check if the token is a legal token.
     * @return {@code true} if the token is a legal token.
     */
    public boolean isIllegal() {
        return type == Type.ILLEGAL;
    }
}
