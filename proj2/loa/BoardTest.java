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

import java.util.ArrayList;

import org.junit.Test;

public class BoardTest {

    @Test
    public void testInitialize() {

        Board b = new Board();
        assertEquals(true, b.equals(new Board()));

        Board b2 = new Board(Board.INITIAL_PIECES, WP);
        assertEquals(false, b2.equals(new Board()));

        Board b3 = new Board(Board.INITIAL_PIECES, BP);
        assertEquals(true, b3.equals(new Board()));

        Board b4 = new Board(Board.INITIAL_PIECES, BP);
        Board b5 = new Board(b4);
        assertEquals(true, b4.equals(b5));
    }

    @Test
    public void testEquals() {

        Piece[][] p1 = { { EMP, BP, BP, BP, BP, BP, BP, EMP },
                { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
                { WP, EMP, EMP, BP, EMP, EMP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
                { WP, BP, EMP, EMP, EMP, BP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
                { EMP, BP, BP, BP, BP, BP, BP, EMP } };

        Piece[][] p2 = { { EMP, BP, BP, BP, BP, BP, BP, EMP },
                { WP, WP, EMP, EMP, EMP, EMP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
                { WP, EMP, EMP, BP, EMP, EMP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
                { WP, BP, EMP, EMP, EMP, BP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
                { EMP, BP, BP, BP, BP, BP, BP, EMP } };

        Piece[][] p3 = { { EMP, BP, BP, BP, BP, BP, BP, EMP },
                { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP }, { WP, EMP, WP, EMP, EMP, EMP, EMP, WP },
                { WP, EMP, EMP, BP, EMP, EMP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
                { WP, BP, EMP, EMP, EMP, BP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
                { EMP, BP, BP, BP, BP, BP, BP, EMP } };

        Board a = new Board(p1, WP);
        Board b = new Board(p2, WP);
        Board c = new Board(p3, WP);

        Board a2 = new Board(p1, BP);
        Board b2 = new Board(p2, BP);
        Board c2 = new Board(p3, BP);

        assertEquals(false, a.equals(b));
        assertEquals(false, b.equals(c));
        assertEquals(true, a.equals(a));
        assertEquals(true, b.equals(b));
        assertEquals(true, c.equals(c));

        assertEquals(false, a2.equals(a));
        assertEquals(false, b2.equals(b));
        assertEquals(false, c2.equals(c));
        assertEquals(true, a2.equals(a2));
        assertEquals(true, b2.equals(b2));
        assertEquals(true, c2.equals(c2));
    }

    @Test
    public void testSet() {

        Piece[][] p1 = { { EMP, BP, BP, BP, BP, BP, BP, EMP },
                { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
                { WP, EMP, EMP, BP, EMP, EMP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
                { WP, BP, EMP, EMP, EMP, BP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
                { EMP, BP, BP, BP, BP, BP, BP, EMP } };

        Piece[][] p2 = { { EMP, BP, BP, BP, BP, BP, BP, EMP },
                { WP, WP, EMP, EMP, EMP, EMP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
                { WP, EMP, EMP, BP, EMP, EMP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
                { WP, BP, EMP, EMP, EMP, BP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
                { EMP, BP, BP, BP, BP, BP, BP, EMP } };

        Piece[][] p3 = { { EMP, BP, BP, BP, BP, BP, BP, EMP },
                { WP, EMP, EMP, EMP, WP, EMP, EMP, WP }, { WP, EMP, WP, EMP, EMP, EMP, EMP, WP },
                { WP, EMP, EMP, BP, EMP, EMP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
                { WP, BP, EMP, EMP, EMP, BP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
                { EMP, BP, BP, BP, BP, BP, BP, EMP } };

        Board a = new Board(p1, WP);

        Board b = new Board(p2, WP);
        Board bt = new Board(a);
        bt.set(2, 2, WP);

        Board c = new Board(p3, WP);
        Board ct = new Board(b);
        ct.set(2, 2, EMP);
        ct.set(3, 3, WP);
        ct.set(5, 2, WP);

        Board b2 = new Board(p2, BP);
        Board b2t = new Board(a);
        b2t.set(2, 2, WP, BP);
        Board c2 = new Board(p3, BP);
        Board c2t = new Board(b);
        c2t.set(2, 2, EMP);
        c2t.set(5, 2, WP);
        c2t.set(3, 3, WP, BP);

        assertEquals(false, a.equals(bt));
        assertEquals(false, bt.equals(ct));

        assertEquals(true, b.equals(bt));
        assertEquals(true, c.equals(ct));

        assertEquals(true, b2.equals(b2t));
        assertEquals(true, c2.equals(c2t));

        assertEquals(false, bt.equals(b2t));
        assertEquals(false, ct.equals(c2t));
    }

    @Test
    public void testColCount() {

        Board b = new Board();
        int[] colCount = new int[] { 6, 2, 2, 2, 2, 2, 2, 6 };
        for (int c = 1; c <= Board.M; c++) {
            assertEquals(colCount[c - 1], b.colCount(c));
        }

        Board c = new Board(b);
        c.set(1, 5, BP);
        c.set(2, 2, WP);
        c.set(3, 6, BP);
        c.set(4, 1, EMP);
        c.set(5, 3, WP);
        c.set(6, 8, EMP);
        c.set(7, 4, BP);
        c.set(8, 7, EMP);
        int[] colCount2 = new int[] { 6, 3, 3, 1, 3, 1, 3, 5 };
        for (int col = 1; col <= Board.M; col++) {
            assertEquals(colCount2[col - 1], c.colCount(col));
        }
    }

    @Test
    public void testRowCount() {

        Board b = new Board();
        int[] rowCount = new int[] { 6, 2, 2, 2, 2, 2, 2, 6 };
        for (int c = 1; c <= Board.M; c++) {
            assertEquals(rowCount[c - 1], b.rowCount(c));
        }

        Board c = new Board(b);
        c.set(1, 5, BP);
        c.set(2, 2, WP);
        c.set(3, 6, BP);
        c.set(4, 1, EMP);
        c.set(5, 3, WP);
        c.set(6, 8, EMP);
        c.set(7, 4, BP);
        c.set(8, 7, EMP);
        int[] rowCount2 = new int[] { 5, 3, 3, 3, 2, 3, 1, 5 };
        for (int row = 1; row <= Board.M; row++) {
            assertEquals(rowCount2[row - 1], c.rowCount(row));
        }
    }

    @Test
    public void testMajDiaCount() {

        Board b = new Board();
        int[][] majDiaCount = new int[][] { { 0, 2, 2, 2, 2, 2, 2, 0 }, // notice
                                                                        // it's
                                                                        // upside
                                                                        // down
                { 2, 0, 2, 2, 2, 2, 2, 2 }, { 2, 2, 0, 2, 2, 2, 2, 2 }, { 2, 2, 2, 0, 2, 2, 2, 2 },
                { 2, 2, 2, 2, 0, 2, 2, 2 }, { 2, 2, 2, 2, 2, 0, 2, 2 }, { 2, 2, 2, 2, 2, 2, 0, 2 },
                { 0, 2, 2, 2, 2, 2, 2, 0 } };
        for (int c = 1; c <= Board.M; c++) {
            for (int r = 1; r <= Board.M; r++) {
                assertEquals(majDiaCount[r - 1][c - 1], b.majDiaCount(c, r));
            }
        }

        Board c = new Board(b);
        c.set(1, 5, BP);
        c.set(2, 2, WP);
        c.set(3, 6, BP);
        c.set(4, 1, EMP);
        c.set(5, 3, WP);
        c.set(6, 8, EMP);
        c.set(7, 4, BP);
        c.set(8, 7, EMP);
        int[][] majDiaCount2 = new int[][] { { 1, 1, 3, 2, 2, 2, 2, 0 }, // notice
                                                                         // it's
                                                                         // upside
                                                                         // down
                { 2, 1, 1, 3, 2, 2, 2, 2 }, { 1, 2, 1, 1, 3, 2, 2, 2 }, { 3, 1, 2, 1, 1, 3, 2, 2 },
                { 2, 3, 1, 2, 1, 1, 3, 2 }, { 2, 2, 3, 1, 2, 1, 1, 3 }, { 2, 2, 2, 3, 1, 2, 1, 1 },
                { 0, 2, 2, 2, 3, 1, 2, 1 } };
        for (int c2 = 1; c2 <= Board.M; c2++) {
            for (int r = 1; r <= Board.M; r++) {
                assertEquals(majDiaCount2[r - 1][c2 - 1], c.majDiaCount(c2, r));
            }
        }
    }

    @Test
    public void testMinDiaCount() {

        Board b = new Board();
        int[][] minDiaCount = new int[][] { { 0, 2, 2, 2, 2, 2, 2, 0 }, // notice
                                                                        // it's
                                                                        // upside
                                                                        // down
                { 2, 2, 2, 2, 2, 2, 0, 2 }, { 2, 2, 2, 2, 2, 0, 2, 2 }, { 2, 2, 2, 2, 0, 2, 2, 2 },
                { 2, 2, 2, 0, 2, 2, 2, 2 }, { 2, 2, 0, 2, 2, 2, 2, 2 }, { 2, 0, 2, 2, 2, 2, 2, 2 },
                { 0, 2, 2, 2, 2, 2, 2, 0 } };
        for (int c = 1; c <= Board.M; c++) {
            for (int r = 1; r <= Board.M; r++) {
                assertEquals(minDiaCount[r - 1][c - 1], b.minDiaCount(c, r));
            }
        }

        Board c = new Board(b);
        c.set(1, 5, BP);
        c.set(2, 2, WP);
        c.set(3, 6, BP);
        c.set(4, 1, EMP);
        c.set(5, 3, WP);
        c.set(6, 8, EMP);
        c.set(7, 4, BP);
        c.set(8, 7, EMP);
        int[][] minDiaCount2 = new int[][] { { 0, 2, 3, 1, 2, 2, 3, 1 }, // notice
                                                                         // it's
                                                                         // upside
                                                                         // down
                { 2, 3, 1, 2, 2, 3, 1, 2 }, { 3, 1, 2, 2, 3, 1, 2, 3 }, { 1, 2, 2, 3, 1, 2, 3, 2 },
                { 2, 2, 3, 1, 2, 3, 2, 2 }, { 2, 3, 1, 2, 3, 2, 2, 1 }, { 3, 1, 2, 3, 2, 2, 1, 1 },
                { 1, 2, 3, 2, 2, 1, 1, 0 } };
        for (int c2 = 1; c2 <= Board.M; c2++) {
            for (int r = 1; r <= Board.M; r++) {
                assertEquals(minDiaCount2[r - 1][c2 - 1], c.minDiaCount(c2, r));
            }
        }
    }

    @Test
    public void testPieceCountAlong() {

        Board b1 = new Board();
        Move v = new Move(4, 1, 4, 3, BP, EMP);
        assertEquals(2, b1.pieceCountAlong(v));
        Move v2 = new Move(1, 2, 3, 2, BP, EMP);
        assertEquals(2, b1.pieceCountAlong(v2));

        Piece[][] p = { { WP, BP, BP, BP, WP, BP, BP, WP }, { WP, EMP, EMP, EMP, WP, EMP, EMP, WP },
                { WP, WP, WP, EMP, EMP, EMP, EMP, WP }, { WP, WP, WP, BP, EMP, EMP, EMP, BP },
                { WP, EMP, WP, EMP, BP, EMP, EMP, BP }, { BP, BP, BP, EMP, EMP, BP, EMP, WP },
                { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP }, { BP, BP, BP, WP, BP, BP, WP, WP } };

        Board b2 = new Board(p, BP);
        v = new Move(1, 2, 3, 4, BP, EMP);
        assertEquals(4, b2.pieceCountAlong(v));
        v = new Move(2, 7, 4, 5, BP, EMP);
        assertEquals(3, b2.pieceCountAlong(v));
        v = new Move(8, 8, 1, 1, BP, EMP);
        assertEquals(6, b2.pieceCountAlong(v));
        v = new Move(4, 6, 7, 3, BP, EMP);
        assertEquals(3, b2.pieceCountAlong(v));
        v = new Move(5, 2, 8, 5, BP, EMP);
        assertEquals(3, b2.pieceCountAlong(v));
        v = new Move(1, 4, 5, 8, BP, EMP);
        assertEquals(3, b2.pieceCountAlong(v));
        v = new Move(3, 8, 7, 4, BP, EMP);
        assertEquals(2, b2.pieceCountAlong(v));
        v = new Move(5, 4, 6, 3, BP, EMP);
        assertEquals(3, b2.pieceCountAlong(v));
        v = new Move(4, 3, 2, 1, BP, EMP);
        assertEquals(2, b2.pieceCountAlong(v));
    }

    @Test
    public void testBlocked() {

        Piece[][] p = { { WP, BP, BP, BP, WP, BP, BP, WP }, { WP, EMP, EMP, EMP, WP, EMP, EMP, WP },
                { WP, WP, WP, EMP, EMP, EMP, EMP, WP }, { WP, WP, WP, BP, EMP, EMP, EMP, BP },
                { WP, EMP, WP, EMP, BP, EMP, EMP, BP }, { BP, BP, BP, EMP, EMP, BP, EMP, WP },
                { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP }, { BP, BP, BP, WP, BP, BP, WP, WP } };

        Board b = new Board(p, BP);

        Move v = Move.create(4, 4, 7, 7, b);
        assertEquals(false, b.blocked(v));

        v = Move.create(1, 2, 4, 5, b);
        assertEquals(false, b.blocked(v));

        v = Move.create(8, 8, 2, 2, b);
        assertEquals(true, b.blocked(v));

        v = Move.create(3, 6, 6, 3, b);
        assertEquals(false, b.blocked(v));

        v = Move.create(5, 2, 8, 5, b);
        assertEquals(false, b.blocked(v));

        v = Move.create(1, 4, 4, 7, b);
        assertEquals(true, b.blocked(v));

        v = Move.create(3, 8, 5, 6, b);
        assertEquals(false, b.blocked(v));
    }

    @Test
    public void testHashCode() {

        Piece[][] p = { { WP, BP, BP, BP, WP, BP, BP, WP }, { WP, EMP, EMP, EMP, WP, EMP, EMP, WP },
                { WP, WP, WP, EMP, EMP, EMP, EMP, WP }, { WP, WP, WP, BP, EMP, EMP, EMP, BP },
                { WP, EMP, WP, EMP, BP, EMP, EMP, BP }, { BP, BP, BP, EMP, EMP, BP, EMP, WP },
                { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP }, { BP, BP, BP, WP, BP, BP, WP, WP } };

        Board b = new Board(p, BP);
        assertEquals(-1701092111, b.hashCode());

        b.set(3, 5, BP);
        assertEquals(1558637313, b.hashCode());

        p[4][2] = BP;
        Board b2 = new Board(p, BP);
        assertEquals(1558637313, b2.hashCode());

        b.set(3, 5, WP);
        assertEquals(false, 1558637313 == b.hashCode());
        assertEquals(-1701092111, b.hashCode());

        b2.set(3, 5, WP, WP);
        assertEquals(false, -1701092111 == b2.hashCode());
        assertEquals(-1656007091, b2.hashCode());

        b.set(3, 5, WP, WP);
        assertEquals(b.hashCode(), b2.hashCode());

        b2.set(3, 5, EMP, WP);
        assertEquals(false, b.hashCode() == b2.hashCode());
        assertEquals(-1785467449, b2.hashCode());

        Piece[][] p1 = { { WP, BP, WP, BP, WP, BP, BP, EMP },
                { EMP, EMP, EMP, EMP, WP, EMP, EMP, WP }, { WP, WP, WP, EMP, EMP, EMP, EMP, WP },
                { WP, WP, WP, BP, EMP, EMP, EMP, WP }, { WP, EMP, WP, EMP, BP, EMP, EMP, BP },
                { BP, WP, BP, EMP, EMP, BP, EMP, WP }, { WP, EMP, EMP, WP, EMP, EMP, EMP, EMP },
                { EMP, BP, BP, WP, WP, BP, WP, WP } };

        Piece[][] p2 = { { WP, BP, WP, BP, WP, BP, BP, EMP },
                { EMP, EMP, EMP, EMP, WP, EMP, EMP, WP }, { WP, WP, WP, EMP, EMP, EMP, EMP, WP },
                { WP, WP, WP, BP, EMP, EMP, EMP, WP }, { WP, EMP, WP, EMP, BP, EMP, EMP, BP },
                { BP, WP, BP, EMP, EMP, BP, EMP, WP }, { WP, EMP, EMP, WP, EMP, EMP, EMP, EMP },
                { EMP, BP, BP, WP, WP, BP, WP, WP } };

        Board a1 = new Board(p1, WP);
        Board a2 = new Board(p2, WP);
        Board a3 = new Board(p2, BP);

        assertEquals(a1.hashCode(), a2.hashCode());
        assertEquals(false, a3.hashCode() == a2.hashCode());
    }

    @Test
    public void testPiecesContiguous() {

        Piece[][] p = { { WP, BP, BP, WP, BP, BP, BP, WP }, { EMP, WP, EMP, WP, EMP, EMP, WP, EMP },
                { EMP, EMP, WP, WP, EMP, WP, EMP, EMP }, { WP, WP, WP, WP, WP, WP, WP, WP },
                { EMP, EMP, EMP, WP, WP, EMP, EMP, EMP }, { EMP, EMP, WP, WP, EMP, WP, EMP, EMP },
                { EMP, WP, EMP, WP, EMP, EMP, WP, EMP }, { WP, EMP, EMP, WP, EMP, EMP, EMP, WP } };
        Board b = new Board(p, BP);
        assertEquals(true, b.piecesContiguous(WP));
        assertEquals(false, b.piecesContiguous(BP));

        Piece[][] p2 = { { WP, BP, BP, WP, BP, BP, BP, WP },
                { EMP, WP, EMP, WP, EMP, EMP, WP, EMP }, { EMP, EMP, WP, BP, EMP, WP, EMP, EMP },
                { WP, WP, BP, EMP, WP, WP, WP, WP }, { EMP, EMP, EMP, WP, WP, EMP, EMP, EMP },
                { EMP, EMP, WP, WP, EMP, WP, EMP, EMP }, { EMP, WP, EMP, WP, EMP, EMP, WP, EMP },
                { WP, EMP, EMP, WP, EMP, EMP, EMP, WP } };
        Board b2 = new Board(p2, BP);
        assertEquals(false, b2.piecesContiguous(WP));
        assertEquals(false, b2.piecesContiguous(BP));

        Piece[][] p3 = { { EMP, BP, BP, BP, WP, BP, BP, EMP },
                { WP, EMP, EMP, WP, EMP, WP, EMP, WP }, { WP, EMP, WP, EMP, WP, EMP, EMP, WP },
                { WP, WP, EMP, EMP, WP, EMP, EMP, WP }, { WP, EMP, WP, EMP, WP, EMP, EMP, WP },
                { WP, WP, EMP, EMP, EMP, WP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, WP, WP },
                { EMP, BP, BP, BP, BP, BP, BP, EMP } };
        Board b3 = new Board(p3, WP);
        assertEquals(true, b3.piecesContiguous(WP));
        b3.set(7, 7, BP);
        assertEquals(false, b3.piecesContiguous(WP));
    }

    @Test
    public void testIsLegal() {

        Piece[][] p = { { WP, BP, BP, BP, WP, BP, BP, WP }, { WP, EMP, EMP, EMP, WP, EMP, EMP, WP },
                { WP, WP, WP, EMP, EMP, EMP, EMP, WP }, { WP, WP, WP, BP, EMP, EMP, EMP, BP },
                { WP, EMP, WP, EMP, BP, EMP, EMP, BP }, { BP, BP, BP, EMP, EMP, BP, EMP, WP },
                { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP }, { BP, BP, BP, WP, BP, BP, WP, WP } };

        Board b = new Board(p, BP);
        assertEquals(false, b.isLegal(Move.create("a3-e3", b)));
        assertEquals(true, b.isLegal(Move.create("h4-c4", b)));
        assertEquals(true, b.isLegal(Move.create("a8-d5", b)));
        assertEquals(false, b.isLegal(Move.create("h1-d4", b)));
        assertEquals(false, b.isLegal(Move.create("a1-b4", b)));
        b = new Board(p, WP);
        assertEquals(true, b.isLegal(Move.create("a3-e3", b)));
        assertEquals(true, b.isLegal(Move.create("e1-e5", b)));
    }

    @Test
    public void testIncr() {

        Piece[][] p = { { WP, BP, BP, BP, WP, BP, BP, WP }, { WP, EMP, EMP, EMP, WP, EMP, EMP, WP },
                { WP, WP, WP, EMP, EMP, EMP, EMP, WP }, { WP, WP, WP, BP, EMP, EMP, EMP, BP },
                { WP, EMP, WP, EMP, BP, EMP, EMP, BP }, { BP, BP, BP, EMP, EMP, BP, EMP, WP },
                { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP }, { BP, BP, BP, WP, BP, BP, WP, WP } };
        Board b = new Board(p, BP);
        int i = 0;
        for (Move m : b) {
            i++;
        }
        assertEquals(25, i);

        Board b2 = new Board();
        int j = 0;
        for (Move m : b2) {
            j++;
        }
        assertEquals(36, j);
    }

    @Test
    public void testPiecesDiscontiguity() {

        Piece[][] p = { { WP, BP, BP, WP, BP, BP, BP, WP }, { EMP, WP, EMP, WP, EMP, EMP, WP, EMP },
                { EMP, EMP, WP, WP, EMP, WP, EMP, EMP }, { WP, WP, WP, WP, WP, WP, WP, WP },
                { EMP, EMP, EMP, WP, WP, EMP, EMP, EMP }, { EMP, EMP, WP, WP, EMP, WP, EMP, EMP },
                { EMP, WP, EMP, WP, EMP, EMP, WP, EMP }, { WP, EMP, EMP, WP, EMP, EMP, EMP, WP } };
        Board b = new Board(p, BP);
        ArrayList<int[]> crsL = b.piecesDiscontiguity(WP);
        ArrayList<int[]> crsLb = b.piecesDiscontiguity(BP);
        assertEquals(1, crsL.size());
        assertEquals(2, crsLb.size());

        Piece[][] p2 = { { WP, BP, BP, WP, BP, BP, BP, WP },
                { EMP, WP, EMP, WP, EMP, EMP, WP, EMP }, { EMP, EMP, WP, BP, EMP, WP, EMP, EMP },
                { WP, WP, BP, EMP, WP, WP, WP, WP }, { EMP, EMP, EMP, WP, WP, EMP, EMP, EMP },
                { EMP, EMP, WP, WP, EMP, WP, EMP, EMP }, { EMP, WP, EMP, WP, EMP, EMP, WP, EMP },
                { WP, EMP, EMP, WP, EMP, EMP, EMP, WP } };
        Board b2 = new Board(p2, BP);
        ArrayList<int[]> crsL2 = b2.piecesDiscontiguity(WP);
        ArrayList<int[]> crsL2b = b2.piecesDiscontiguity(BP);
        assertEquals(2, crsL2.size());
        assertEquals(3, crsL2b.size());

        Piece[][] p3 = { { EMP, BP, BP, BP, WP, BP, BP, EMP },
                { WP, EMP, EMP, WP, EMP, WP, EMP, WP }, { WP, EMP, WP, EMP, WP, EMP, EMP, WP },
                { WP, WP, EMP, EMP, WP, EMP, EMP, WP }, { WP, EMP, WP, EMP, WP, EMP, EMP, WP },
                { WP, WP, EMP, EMP, EMP, WP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, WP, WP },
                { EMP, BP, BP, BP, BP, BP, BP, EMP } };
        Board b3 = new Board(p3, WP);
        ArrayList<int[]> crsL3 = b3.piecesDiscontiguity(WP);
        ArrayList<int[]> crsL3b = b3.piecesDiscontiguity(BP);
        assertEquals(1, crsL3.size());
        assertEquals(3, crsL3b.size());

        Piece[][] p4 = { { EMP, BP, BP, BP, WP, BP, BP, EMP },
                { WP, EMP, EMP, WP, EMP, WP, EMP, WP }, { WP, EMP, WP, EMP, WP, EMP, EMP, WP },
                { WP, WP, EMP, EMP, WP, EMP, EMP, WP }, { WP, EMP, WP, EMP, WP, EMP, EMP, WP },
                { WP, WP, EMP, EMP, EMP, WP, EMP, WP }, { WP, EMP, EMP, WP, EMP, EMP, WP, WP },
                { EMP, BP, BP, BP, BP, BP, BP, EMP } };
        Board b4 = new Board(p4, WP);
        System.out.println(b4);
        ArrayList<int[]> crsL4 = b4.piecesDiscontiguity(WP);
        ArrayList<int[]> crsL4b = b4.piecesDiscontiguity(BP);
        System.out.println("WP: " + crsL4.size());
        System.out.println("BP: " + crsL4b.size());
        for (int[] i : crsL4) {
            System.out.println("c: " + i[0] + " r: " + i[1] + " size: " + i[2]);

        }
    }

    public static void main(String... args) {
        System.exit(ucb.junit.textui.runClasses(BoardTest.class));
    }
}
