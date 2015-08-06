package sa.bsh.scanner;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

public class TokenTest {
    static final Position position = new Position(1, 1);

    @Test(expected = AssertionError.class)
    public void testAttributesValidationCase1() throws Exception {
        new Token(Token.Type.DOUBLE, position);
    }

    @Test(expected = AssertionError.class)
    public void testAttributesValidationCase2() throws Exception {
        new Token(Token.Type.ADD, position, "+");
    }

    @Test
    public void testGetType() throws Exception {
        Token tok1 = new Token(Token.Type.DOUBLE, position, "3.1");
        Token tok2 = new Token(Token.Type.ILLEGAL, position, "{");

        assertEquals(tok1.getType(), Token.Type.DOUBLE);
        assertEquals(tok2.getType(), Token.Type.ILLEGAL);
    }

    @Test
    public void testGetValue() throws Exception {
        Token tok1 = new Token(Token.Type.EOF, position);
        Token tok2 = new Token(Token.Type.DOUBLE, position, "3.25");

        assertEquals(tok1.getAttr(), "end of input");
        assertEquals(tok2.getAttr(), "3.25");
    }

    @Test
    public void testGetPosition() throws Exception {
        Token tok = new Token(Token.Type.INTEGER, position, "13");

        assertEquals(tok.getPosition(), position);
    }

    @Test
    public void testEquals() throws Exception {
        Token tok1 = new Token(Token.Type.INTEGER, position, "13");
        Token tok2 = new Token(tok1);

        Token tok3 = new Token(Token.Type.EOF, position);
        Token tok4 = new Token(tok3);

        Token tok5 = new Token(Token.Type.INTEGER, position, "31");

        assertEquals(tok1, tok2);
        assertEquals(tok3, tok4);
        assertNotEquals(tok1, tok5);
    }

    @Test
    public void testHashCode() throws Exception {
        Map<Token, String> map = new HashMap<>();

        map.put(new Token(Token.Type.DOUBLE, position, "3.4"), "Test");

        assertEquals(map.get(new Token(Token.Type.DOUBLE, position, "3.4")), "Test");
    }

    @Test
    public void testToString() throws Exception {
        Token tok1 = new Token(Token.Type.INTEGER, position, "13");
        Token tok2 = new Token(Token.Type.VAR, position);

        assertEquals(tok1.toString(), "Token(INTEGER=13, @1:1)");
        assertEquals(tok2.toString(), "Token(var, @1:1)");
    }

    @Test
    public void testIsToken() throws Exception {
        Token tok = new Token(Token.Type.DOUBLE, position, "11.53");

        assertTrue(tok.isToken(Token.Type.DOUBLE));
    }

    @Test
    public void testIsEndOfInput() throws Exception {
        Token tok1 = new Token(Token.Type.EOF, position);
        Token tok2 = new Token(Token.Type.ILLEGAL, position, "{");

        assertTrue(tok1.isEndOfInput());
        assertFalse(tok2.isEndOfInput());
    }

    @Test
    public void testIsNumber() throws Exception {
        Token tok1 = new Token(Token.Type.INTEGER, position, "6");
        Token tok2 = new Token(Token.Type.DOUBLE, position, "3.4");
        Token tok3 = new Token(Token.Type.STRING, position, "Test");

        assertTrue(tok1.isNumber());
        assertTrue(tok2.isNumber());
        assertFalse(tok3.isNumber());
    }

    @Test
    public void testIsReservedWord() throws Exception {
        Token tok1 = new Token(Token.Type.VAR, position);
        Token tok2 = new Token(Token.Type.ELSEIF, position);
        Token tok3 = new Token(Token.Type.END, position);
        Token tok4 = new Token(Token.Type.EOF, position);
        Token tok5 = new Token(Token.Type.STRING, position, "Test");

        assertTrue(tok1.isReservedWord());
        assertTrue(tok2.isReservedWord());
        assertTrue(tok3.isReservedWord());
        assertFalse(tok4.isReservedWord());
        assertFalse(tok5.isReservedWord());
    }

    @Test
    public void testIsIllegal() throws Exception {
        Token tok1 = new Token(Token.Type.ILLEGAL, position, "{");
        Token tok2 = new Token(Token.Type.EOF, position);

        assertTrue(tok1.isIllegal());
        assertFalse(tok2.isIllegal());
    }
}