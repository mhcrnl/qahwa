package sa.bsh.scanner;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.Arrays;

/**
 * Scanner scans the input for tokens and performs lexical analysis.
 */
public class Scanner {
    private final Reader reader;
    // Keep this list of reserved word sorted in ascending order to look them up using binary search.
    private final String[] reserved = {"elseif", "end", "if", "var", "while"};
    private int ch;
    private int lineNumber;
    private int columnNumber;
    private boolean seenEOF;

    /**
     * Create a new scanner from a given reader object.
     * @param reader The source reader object.
     * @throws IOException
     */
    public Scanner(Reader reader) throws IOException {
        this.reader = reader;
        this.lineNumber = 1;
        this.columnNumber = 1;
        this.seenEOF = false;

        // Do not call next() as it alters column
        this.ch = reader.read();
    }

    /**
     * Create a new scanner from a string containing the source. A string reader will be created to read the source.
     * @param source The source code.
     * @throws IOException
     */
    public Scanner(String source) throws IOException {
        this(new StringReader(source));
    }

    /**
     * Scan the source and return the next token.
     * @return The next token.
     * @throws IOException
     */
    public Token scan() throws IOException {
        do {
            if (ch == -1) {
                return new Token(Token.Type.EOF, new Position(lineNumber, columnNumber));
            }

            // Remember the current position.
            Position position = markPosition();

            // Begin with identifiers (and reserved words) as they are the most used type of tokens.
            if (Character.isJavaIdentifierStart(ch)) {
                // Construct the identifier attribute.
                StringBuilder builder = new StringBuilder();
                do {
                    builder.append((char) ch);
                    next();
                } while (Character.isJavaIdentifierPart((char) ch));

                // Check first for reserved words
                int i = Arrays.binarySearch(reserved, builder.toString());
                if (i >= 0) {
                    Token.Type type = Token.Type.valueOf(reserved[i].toUpperCase());
                    return new Token(type, position);
                }

                // It was an identifier.
                return new Token(Token.Type.IDENTIFIER, position, builder.toString());
            }

            // Handle newline, in the forms \n and \r\n.
            else if (ch == '\n' || ch == '\r') {
                int prev = ch;
                next();
                if (prev == '\r' && ch == '\n') {
                    next();
                }
                // Increment the line number and reset the column.
                lineNumber++;
                columnNumber = 1;
                return new Token(Token.Type.NEWLINE, position);
            }

            // Skip whitespaces (\n, \r should already been handled).
            else if (Character.isWhitespace(ch)) {
                next();
            }

            else {
                // Illegal character is found.
                return new Token(Token.Type.ILLEGAL, position, Character.toString((char) ch));
            }
        } while (true);
    }


    // Put the next character in the input in ch
    private void next() throws IOException {
        ch = reader.read();
        if (ch != -1)
            columnNumber++;
        else {
            // Increment the column number only on the fist time EOF is met. This allows us to return EOF
            // token every time scan() is called when EOF is already reached.
            if (!seenEOF) {
                columnNumber++;
                seenEOF = true;
            }
        }
    }

    // Mark the current potion in the source input.
    private Position markPosition() {
        return new Position(lineNumber, columnNumber);
    }
}