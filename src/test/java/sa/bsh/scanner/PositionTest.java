package sa.bsh.scanner;

import org.junit.Test;

public class PositionTest {
    @Test(expected = AssertionError.class)
    public void testPositionCase1() throws Exception {
        new Position(0, 45);
    }

    @Test(expected = AssertionError.class)
    public void testPositionCase2() throws Exception {
        new Position(34, 0);
    }
}