package loa;

import static loa.Direction.E;
import static loa.Direction.N;
import static loa.Direction.NE;
import static loa.Direction.NOWHERE;
import static loa.Direction.NW;
import static loa.Direction.S;
import static loa.Direction.SE;
import static loa.Direction.SW;
import static loa.Direction.W;
import static loa.Piece.BP;
import static loa.Piece.EMP;
import static loa.Piece.WP;
import static org.junit.Assert.*;

import org.junit.Test;

public class MachinePlayerTest {

    @Test
    public void testEval() {

        Piece[][] p1 = { { EMP, BP, BP, BP, WP, BP, BP, EMP },
                { WP, EMP, EMP, WP, EMP, WP, EMP, WP }, { WP, EMP, WP, EMP, WP, EMP, EMP, WP },
                { WP, WP, EMP, EMP, WP, EMP, EMP, WP }, { WP, EMP, WP, EMP, WP, EMP, EMP, WP },
                { WP, WP, EMP, EMP, EMP, WP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, WP, WP },
                { EMP, BP, BP, BP, BP, BP, BP, EMP } };
        Board b = new Board(p1, BP);
        System.out.println(b);
        for (Move m : b) {
            b.makeMove(m);
            System.out.println(MachinePlayer.eval(b));
            b.retract();
        }

    }

    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MachinePlayerTest.class));
    }

}
