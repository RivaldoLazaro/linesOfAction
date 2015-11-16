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

		Move v = new Move(1, 2, 3, 4, EMP, EMP);
		assertEquals(NE, v.dir());
		
		v = new Move(3, 3, 3, 4, EMP, EMP);
		assertEquals(N, v.dir());
		
		v = new Move(3, 3, 4, 4, EMP, EMP);
		assertEquals(NE, v.dir());
		
		v = new Move(3, 3, 4, 3, EMP, EMP);
		assertEquals(E, v.dir());
		
		v = new Move(3, 3, 4, 2, EMP, EMP);
		assertEquals(SE, v.dir());
		
		v = new Move(3, 3, 3, 2, EMP, EMP);
		assertEquals(S, v.dir());
		
		v = new Move(3, 3, 2, 2, EMP, EMP);
		assertEquals(SW, v.dir());
		
		v = new Move(3, 3, 2, 3, EMP, EMP);
		assertEquals(W, v.dir());
		
		v = new Move(3, 3, 2, 4, EMP, EMP);
		assertEquals(NW, v.dir());
		
		v = new Move(3, 3, 3, 3, EMP, EMP);
		assertEquals(NOWHERE, v.dir());
		
		
		
		
		
		
		
		
		
		
		
	}
	
	public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(MoveTest.class));
    }

}