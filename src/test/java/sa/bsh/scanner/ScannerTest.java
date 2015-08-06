package sa.bsh.scanner;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ScannerTest {
    @Test
    public void testEmptySource() throws Exception {
        Scanner scanner1 = new Scanner("");
        Scanner scanner2 = new Scanner("        ");

        Token.Type type = Token.Type.EOF;

        assertEquals(type, scanner1.scan().getType());
        assertEquals(type, scanner2.scan().getType());
    }

    @Test
    public void testEndOfInputCase1() throws Exception {
        Scanner scanner = new Scanner("        a");

        Token tok1 = scanner.scan();
        Token tok2 = scanner.scan();
        Token tok3 = scanner.scan();
        Token tok4 = scanner.scan();

        assertEquals(new Token(Token.Type.IDENTIFIER, 1, 9, "a"), tok1);
        assertEquals(new Token(Token.Type.EOF, 1, 10), tok2);
        assertEquals(new Token(Token.Type.EOF, 1, 10), tok3);
        assertEquals(new Token(Token.Type.EOF, 1, 10), tok4);
    }

    @Test
    public void testEndOfInputCase2() throws Exception {
        Scanner scanner = new Scanner("\r\n a\n");

        Token tok1 = scanner.scan();
        Token tok2 = scanner.scan();
        Token tok3 = scanner.scan();
        Token tok4 = scanner.scan();
        Token tok5 = scanner.scan();
        Token tok6 = scanner.scan();

        assertEquals(new Token(Token.Type.NEWLINE, 1, 1), tok1);
        assertEquals(new Token(Token.Type.IDENTIFIER, 2, 2, "a"), tok2);
        assertEquals(new Token(Token.Type.NEWLINE, 2, 3), tok3);
        assertEquals(new Token(Token.Type.EOF, 3, 1), tok4);
        assertEquals(new Token(Token.Type.EOF, 3, 1), tok5);
        assertEquals(new Token(Token.Type.EOF, 3, 1), tok6);
    }

    @Test
    public void testIdentifiers() throws Exception {
        Scanner scanner = new Scanner("ABCD\n  varX");

        Token tok1 = scanner.scan();
        Token tok2 = scanner.scan();
        Token tok3 = scanner.scan();
        Token tok4 = scanner.scan();
        Token tok5 = scanner.scan();

        assertEquals(new Token(Token.Type.IDENTIFIER, 1, 1, "ABCD"), tok1);
        assertEquals(new Token(Token.Type.NEWLINE, 1, 5), tok2);
        assertEquals(new Token(Token.Type.IDENTIFIER, 2, 3, "varX"), tok3);
        assertEquals(new Token(Token.Type.EOF, 2, 7), tok4);
        assertEquals(new Token(Token.Type.EOF, 2, 7), tok5);
    }

    @Test
    public void testReservedWords() throws Exception {
        Scanner scanner = new Scanner("elseif else true false null");

        Token tok1 = scanner.scan();
        Token tok2 = scanner.scan();
        Token tok3 = scanner.scan();
        Token tok4 = scanner.scan();
        Token tok5 = scanner.scan();
        Token tok6 = scanner.scan();

        assertEquals(new Token(Token.Type.ELSEIF, 1, 1), tok1);
        assertEquals(new Token(Token.Type.ELSE, 1, 8), tok2);
        assertEquals(new Token(Token.Type.TRUE, 1, 13), tok3);
        assertEquals(new Token(Token.Type.FALSE, 1, 18), tok4);
        assertEquals(new Token(Token.Type.NULL, 1, 24), tok5);
        assertEquals(new Token(Token.Type.EOF, 1, 28), tok6);
    }

    @Test
    public void testNewlineCase1() throws Exception {
        Scanner scanner = new Scanner("\n \r\n  \n   \n");

        Token tok1 = scanner.scan();
        Token tok2 = scanner.scan();
        Token tok3 = scanner.scan();
        Token tok4 = scanner.scan();
        Token tok5 = scanner.scan();

        assertEquals(new Token(Token.Type.NEWLINE, 1, 1), tok1);
        assertEquals(new Token(Token.Type.NEWLINE, 2, 2), tok2);
        assertEquals(new Token(Token.Type.NEWLINE, 3, 3), tok3);
        assertEquals(new Token(Token.Type.NEWLINE, 4, 4), tok4);
        assertEquals(new Token(Token.Type.EOF, 5, 1), tok5);
    }

    @Test
    public void testNewlineCase2() throws Exception {
        Scanner scanner = new Scanner("\n\r\n\r    var");

        Token tok1 = scanner.scan();
        Token tok2 = scanner.scan();
        Token tok3 = scanner.scan();
        Token tok4 = scanner.scan();
        Token tok5 = scanner.scan();

        assertEquals(new Token(Token.Type.NEWLINE, 1, 1), tok1);
        assertEquals(new Token(Token.Type.NEWLINE, 2, 1), tok2);
        assertEquals(new Token(Token.Type.NEWLINE, 3, 1), tok3);
        assertEquals(new Token(Token.Type.VAR, 4, 5), tok4);
        assertEquals(new Token(Token.Type.EOF, 4, 8), tok5);
    }
}