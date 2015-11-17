package loa;

import static loa.Direction.*;
import static loa.Piece.BP;
import static loa.Piece.EMP;
import static loa.Piece.WP;
import static org.junit.Assert.*;

import org.junit.Test;

public class MoveTest {

    @Test
    public void testDir() {

        Move v = new Move(1, 2, 3, 4, WP, EMP);
        assertEquals(NE, v.dir());

        v = new Move(3, 3, 3, 4, WP, EMP);
        assertEquals(N, v.dir());

        v = new Move(3, 3, 4, 4, WP, EMP);
        assertEquals(NE, v.dir());

        v = new Move(3, 3, 4, 3, WP, EMP);
        assertEquals(E, v.dir());

        v = new Move(3, 3, 4, 2, WP, EMP);
        assertEquals(SE, v.dir());

        v = new Move(3, 3, 3, 2, WP, EMP);
        assertEquals(S, v.dir());

        v = new Move(3, 3, 2, 2, WP, EMP);
        assertEquals(SW, v.dir());

        v = new Move(3, 3, 2, 3, WP, EMP);
        assertEquals(W, v.dir());

        v = new Move(3, 3, 2, 4, WP, EMP);
        assertEquals(NW, v.dir());

        v = new Move(3, 3, 3, 3, WP, EMP);
        assertEquals(NOWHERE, v.dir());
    }

    @Test
    public void testValue() {
        Piece[][] p = { { WP, BP, BP, WP, BP, BP, BP, WP }, { EMP, WP, EMP, WP, EMP, EMP, WP, EMP },
                { EMP, EMP, WP, WP, EMP, WP, EMP, EMP }, { WP, WP, WP, WP, WP, WP, WP, WP },
                { EMP, EMP, EMP, WP, WP, EMP, EMP, EMP }, { EMP, EMP, WP, WP, EMP, WP, EMP, EMP },
                { EMP, WP, EMP, WP, EMP, EMP, WP, EMP }, { WP, EMP, EMP, WP, EMP, EMP, EMP, WP } };
        Board b = new Board(p, BP);
        System.out.println(b);
        for (Move mv : b) {
            System.out.println(mv);
        }

    }

    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MoveTest.class));
    }

}
