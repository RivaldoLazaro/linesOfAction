// Remove all comments that begin with //, and replace appropriately.
// Feel free to modify ANYTHING in this file.
package loa;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Formatter;
import java.util.NoSuchElementException;
import java.util.Random;

import java.util.regex.Pattern;

import static loa.Piece.*;
import static loa.Board.M;
import static loa.Direction.*;

/**
 * Represents the state of a game of Lines of Action.
 * 
 * @author Tim Chan
 */
class Board implements Iterable<Move> {

    /** Size of a board. */
    static final int M = 8;

    /** Pattern describing a valid square designator (cr). */
    static final Pattern ROW_COL = Pattern.compile("^[a-h][1-8]$");

    /**
     * A Board whose initial contents are taken from INITIALCONTENTS and in
     * which the player playing TURN is to move. The resulting Board has
     * get(col, row) == INITIALCONTENTS[row-1][col-1] Assumes that PLAYER is not
     * null and INITIALCONTENTS is MxM.
     *
     * CAUTION: The natural written notation for arrays initializers puts the
     * BOTTOM row of INITIALCONTENTS at the top.
     */
    Board(Piece[][] initialContents, Piece turn) {
        initialize(initialContents, turn);
    }

    /** A new board in the standard initial position. */
    Board() {
        clear();
    }

    /**
     * A Board whose initial contents and state are copied from BOARD.
     */
    Board(Board board) {
        copyFrom(board);
    }

    /** Set my state to CONTENTS with SIDE to move. */
    void initialize(Piece[][] contents, Piece side) {
        _moves.clear();

        // TK:
        _board = new Piece[M][M];
        // TK.

        for (int r = 1; r <= M; r += 1) {
            for (int c = 1; c <= M; c += 1) {
                set(c, r, contents[r - 1][c - 1]); // TK
            }
        }
        // recount();
        makeZobristTable();
        _turn = side;
    }

    /** Build Zobrist table from _zobristSeed. */
    private void makeZobristTable() {
        Random r = new Random(_zobristSeed);
        for (int i = 0; i < M; i++) {
            for (int j = 0; j < M; j++) {
                for (int k = 0; k < 2; k++) {
                    _zobristTable[i][j][k] = r.nextInt();
                }
            }
        }
        _turnHash[0] = r.nextInt();
        _turnHash[1] = r.nextInt();
    }

    /** Set me to the initial configuration. */
    void clear() {
        initialize(INITIAL_PIECES, BP);
    }

    /** Set me to the desired configuration. */
    void clear(Piece[][] b, Piece p) {
        initialize(b, p);
    }

    /** Set my state to a copy of BOARD. */
    void copyFrom(Board board) {
        if (board == this) {
            return;
        }
        _moves.clear();
        _moves.addAll(board._moves);
        _turn = board._turn;
        // TK:
        for (int r = 1; r <= M; r += 1) {
            for (int c = 1; c <= M; c += 1) {
                set(c, r, board.get(c, r));
            }
        }
        // TK.
    }

    /**
     * Return the contents of column C, row R, where 1 <= C,R <= 8, where column
     * 1 corresponds to column 'a' in the standard notation.
     */
    Piece get(int c, int r) {
        return _board[r - 1][c - 1]; // TK.

    }

    /**
     * Return the contents of the square SQ. SQ must be the standard printed
     * designation of a square (having the form cr, where c is a letter from a-h
     * and r is a digit from 1-8).
     */
    Piece get(String sq) {
        return get(col(sq), row(sq));
    }

    /**
     * Return the column number (a value in the range 1-8) for SQ. SQ is as for
     * {@link get(String)}.
     */
    static int col(String sq) {
        if (!ROW_COL.matcher(sq).matches()) {
            throw new IllegalArgumentException("bad square designator");
        }
        return sq.charAt(0) - 'a' + 1;
    }

    /**
     * Return the row number (a value in the range 1-8) for SQ. SQ is as for
     * {@link get(String)}.
     */
    static int row(String sq) {
        if (!ROW_COL.matcher(sq).matches()) {
            throw new IllegalArgumentException("bad square designator");
        }
        return sq.charAt(1) - '0';
    }

    /**
     * Set the square at column C, row R to V, and make NEXT the next side to
     * move, if it is not null.
     */
    void set(int c, int r, Piece v, Piece next) {

        // TK:
        if (!inBound(c, r)) {
            throw new IllegalArgumentException("Error: invalid location to set: ");
        }
        _board[r - 1][c - 1] = v;
        // TK.

        if (next != null) {
            _turn = next;
        }
    }

    /** Set the square at column C, row R to V. */
    void set(int c, int r, Piece v) {
        set(c, r, v, null);
    }

    /** Assuming isLegal(MOVE), make MOVE. */
    void makeMove(Move move) {
        assert isLegal(move);
        _moves.add(move);
        Piece replaced = move.replacedPiece();
        int c0 = move.getCol0(), c1 = move.getCol1();
        int r0 = move.getRow0(), r1 = move.getRow1();
        if (replaced != EMP) {
            set(c1, r1, EMP);
        }
        set(c1, r1, move.movedPiece());
        set(c0, r0, EMP);
        _turn = _turn.opposite();
    }

    /**
     * Retract (unmake) one move, returning to the state immediately before that
     * move. Requires that movesMade () > 0.
     */
    void retract() {
        assert movesMade() > 0;
        Move move = _moves.remove(_moves.size() - 1);
        Piece replaced = move.replacedPiece();
        int c0 = move.getCol0(), c1 = move.getCol1();
        int r0 = move.getRow0(), r1 = move.getRow1();
        Piece movedPiece = move.movedPiece();
        set(c1, r1, replaced);
        set(c0, r0, movedPiece);
        _turn = _turn.opposite();
    }

    /** Return the Piece representing who is next to move. */
    Piece turn() {
        return _turn;
    }

    /** Return true iff MOVE is legal for the player currently on move. */
    boolean isLegal(Move move) {
        return move != null && !blocked(move) && move.movedPiece() == turn()
                && move.length() == pieceCountAlong(move);
    }

    /** Return a sequence of all legal moves from this position. */
    Iterator<Move> legalMoves() {
        return new MoveIterator();
    }

    @Override
    public Iterator<Move> iterator() {
        return legalMoves();
    }

    /**
     * Return true if there is at least one legal move for the player on move.
     */
    public boolean isLegalMove() {
        return iterator().hasNext();
    }

    /** Return true iff either player has all his pieces continguous. */
    boolean gameOver() {
        return piecesContiguous(BP) || piecesContiguous(WP);
    }

    /** Return the Piece iff a player has all his pieces continguous. */
    Piece gameOverP() {
        if (piecesContiguous(BP) && this.turn() == WP) {
            return BP;
        } else if (piecesContiguous(WP) && this.turn() == BP) {
            return WP;
        } else {
            return EMP;
        }
    }

    /** Return a 8x8 array if side present, make 1 else 0. */
    private int[][] mapOne(Piece side, int[] loc) {
        int[][] sidePieceOnly = new int[M][M];
        for (int c = 1; c <= M; c++) {
            for (int r = 1; r <= M; r++) {
                if (get(c, r) == side) {
                    if (loc != null && loc[0] == 0 && loc[1] == 0) {
                        loc[0] = c;
                        loc[1] = r;
                    }
                    sidePieceOnly[r - 1][c - 1] = 1;
                } else {
                    sidePieceOnly[r - 1][c - 1] = 0;
                }
            }
        }
        return sidePieceOnly;
    }

    private int[] nextChunk(int[][] onesMap) {
        int[] loc = new int[2];
        for (int c = 1; c <= M; c++) {
            for (int r = 1; r <= M; r++) {
                if (onesMap[r - 1][c - 1] == 1) {
                    loc[0] = c;
                    loc[1] = r;
                    return loc;
                }
            }
        }
        return null;
    }

    /** return chunk average location : [avg_c, avg_r, size]. and mark it off */
    private int[] chunkCentre(int[][] onesMap, int c, int r) {
        int[] result = new int[] { 0, 0, 0 };
        ArrayList<int[]> cr = new ArrayList<int[]>();
        markOff(onesMap, c, r, cr);
        for (int[] a : cr) {
            result[0] += a[0];
            result[1] += a[1];
            result[2]++;
        }
        result[0] = (int) Math.floor(result[0] / result[2]);
        result[1] = (int) Math.floor(result[1] / result[2]);
        return result;
    }

    private static void markOff(int[][] i, int c, int r, ArrayList<int[]> cr) {
        i[r - 1][c - 1] = 0;
        if (cr != null) {
            cr.add(new int[] { c, r });
        }
        for (Direction dir : Direction.values()) {
            if (inBound(c + dir.dc, r + dir.dr) && i[r + dir.dr - 1][c + dir.dc - 1] == 1) {
                markOff(i, c + dir.dc, r + dir.dr, cr);
            }
        }
    }

    /**
     * Return an arrayList of average location of each chunk and size of a chunk
     * {{c0, c0, s0}, {c1, r1, s1}, ...}
     */
    ArrayList<int[]> piecesDiscontiguity(Piece side) {
        ArrayList<int[]> result = new ArrayList<int[]>();
        int[][] sidePieceOnly = this.mapOne(side, null);
        int[] loc;
        while (true) {
            loc = nextChunk(sidePieceOnly);
            if (loc == null) {
                break;
            }
            result.add(this.chunkCentre(sidePieceOnly, loc[0], loc[1]));
        }
        return result;
    }

    /** Return true iff SIDE's pieces are continguous. */
    boolean piecesContiguous(Piece side) {
        int[][] sidePieceOnly = new int[M][M];
        int[] loc = new int[2];
        sidePieceOnly = this.mapOne(side, loc);
        int c0 = loc[0];
        int r0 = loc[1];
        markOff(sidePieceOnly, c0, r0);

        if (nextChunk(sidePieceOnly) != null) {
            return false;
        }
        return true;
    }

    private static void markOff(int[][] i, int c, int r) {
        markOff(i, c, r, null);
    }

    private static boolean inBound(int c, int r) {
        return 1 <= c && c <= M && 1 <= r && r <= M;
    }

    /**
     * Return the total number of moves that have been made (and not retracted).
     * Each valid call to makeMove with a normal move increases this number by
     * 1.
     */
    int movesMade() {
        return _moves.size();
    }

    /** Return true iff two Boards have the same _board layout and _turn. */
    @Override
    public boolean equals(Object obj) {
        Board b = (Board) obj;
        // TK:
        if (this.turn() != b.turn()) {
            return false;
        }
        for (int r = 1; r <= M; r += 1) {
            for (int c = 1; c <= M; c += 1) {
                if (this.get(c, r) != b.get(c, r)) {
                    return false;
                }
            }
        }
        return true;
        // TK.
    }

    @Override
    public int hashCode() {
        // TK:
        int result = 0;
        for (int c = 1; c <= M; c++) {
            for (int r = 1; r <= M; r++) {
                Piece p = get(c, r);
                if (p == WP) {
                    result = result ^ _zobristTable[r - 1][c - 1][0];
                } else if (p == BP) {
                    result = result ^ _zobristTable[r - 1][c - 1][1];
                }
            }
        }
        if (turn() == WP) {
            result = result ^ _turnHash[0];
        } else if (turn() == BP) {
            result = result ^ _turnHash[1];
        }
        return result;
        // TK.
    }

    @Override
    public String toString() {
        Formatter out = new Formatter();
        out.format("===%n");
        for (int r = M; r >= 1; r -= 1) {
            out.format("    ");
            for (int c = 1; c <= M; c += 1) {
                out.format("%s ", get(c, r).abbrev());
            }
            out.format("%n");
        }
        out.format("Next move: %s%n===", turn().fullName());
        return out.toString();
    }

    // FUNCTION MIGHT NEEDED!!! NOT FINISHED.
    /**
     * Recount the Number of pieces. in every direction at [r - 1][c - 1][-, |,
     * \, /]
     */
    void recount() {
        for (int c = 1; c <= M; c++) {
            for (int r = 1; r <= M; r++) {
                _piecesCount[r - 1][c - 1][0] = rowCount(r);
                _piecesCount[r - 1][c - 1][1] = colCount(c);
            }
        }
    }

    /** Recount the Number of pieces at col C: 1 <= C <= M. */
    int colCount(int c) {
        int result = 0;
        for (int r = 0; r < M; r++) {
            if (_board[r][c - 1] != EMP) {
                result++;
            }
        }
        return result;
    }

    /** Recount the Number of pieces at row R: 1 <= R <= M. */
    int rowCount(int r) {
        int result = 0;
        for (int c = 0; c < M; c++) {
            if (_board[r - 1][c] != EMP) {
                result++;
            }
        }
        return result;
    }

    /**
     * Recount the Number of pieces along major diagonal (+ve slope) C: 1 <= C
     * <= M; R: 1 <= R <= M.
     */
    int majDiaCount(int c, int r) {
        int c2 = c;
        int r2 = r;
        int result = 0;
        while (c <= M && r <= M) {
            if (_board[r - 1][c - 1] != EMP) {
                result++;
            }
            c++;
            r++; // _baord is upside down from real world.
        }
        if (c2 >= 1 && r2 >= 1) {
            r2--;
            c2--;
        }
        while (c2 >= 1 && r2 >= 1) {
            if (_board[r2 - 1][c2 - 1] != EMP) {
                result++;
            }
            c2--;
            r2--; // _baord is upside down from real world.
        }
        return result;
    }

    /**
     * Recount the Number of pieces along minor diagonal (-ve slope) C: 1 <= C
     * <= M; R: 1 <= R <= M.
     */
    int minDiaCount(int c, int r) {
        int c2 = c;
        int r2 = r;
        int result = 0;
        while (c >= 1 && r <= M) {
            if (_board[r - 1][c - 1] != EMP) {
                result++;
            }
            c--;
            r++; // _baord is upside down from real world.
        }
        if (c2 >= 1 && r2 >= 1) {
            r2--;
            c2++;
        }
        while (c2 <= M && r2 >= 1) {
            if (_board[r2 - 1][c2 - 1] != EMP) {
                result++;
            }
            c2++;
            r2--; // _baord is upside down from real world.
        }
        return result;
    }

    /** Return the number of pieces in the line of action indicated by MOVE. */
    int pieceCountAlong(Move move) {
        // TK:
        return pieceCountAlong(move.getCol0(), move.getRow0(), move.dir());
        // TK.
    }

    /**
     * Return the number of pieces in the line of action in direction DIR and
     * containing the square at column C and row R.
     */
    int pieceCountAlong(int c, int r, Direction dir) {
        // TK:
        switch (dir) {
        case N:
        case S:
            return colCount(c);
        case E:
        case W:
            return rowCount(r);
        case NE:
        case SW:
            return majDiaCount(c, r);
        case NW:
        case SE:
            return minDiaCount(c, r);
        default:
            return 0;
        }
        // TK.
    }

    /**
     * Return true iff MOVE is blocked by an opposing piece or by a friendly
     * piece on the target square.
     */
    boolean blocked(Move move) {
        // TK:
        int ci = move.getCol0();
        int ri = move.getRow0();
        int cf = move.getCol1();
        int rf = move.getRow1();

        if (this.get(cf, rf) == move.movedPiece()) {
            return true;
        }

        while (ci != cf || ri != rf) {
            if (this.get(ci, ri) == move.movedPiece().opposite()) {
                return true;
            }
            for (Direction dir : Direction.values()) {
                if (move.dir() == dir) {
                    ri += dir.dr;
                    ci += dir.dc;
                }
            }
        }
        // TK.
        return false;
    }

    /** The standard initial configuration for Lines of Action. */
    static final Piece[][] INITIAL_PIECES = { { EMP, BP, BP, BP, BP, BP, BP, EMP },
            { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
            { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
            { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP }, { WP, EMP, EMP, EMP, EMP, EMP, EMP, WP },
            { EMP, BP, BP, BP, BP, BP, BP, EMP } };

    /** Grid for testing winning cases */
    static final Piece[][] WIN_BOARD = { { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP }, { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP }, { EMP, EMP, EMP, EMP, WP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP }, { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, BP, EMP, EMP, WP, BP, EMP, EMP } };

    /** Grid for testing tie cases */
    static final Piece[][] TIE_BOARD = { { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP }, { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP }, { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, EMP, EMP, WP, EMP, EMP, EMP }, { EMP, EMP, EMP, EMP, EMP, EMP, EMP, EMP },
            { EMP, EMP, BP, EMP, BP, WP, EMP, EMP } };

    Move lastMove() {
        return _moves.get(_moves.size() - 1);
    }

    /** List of all unretracted moves on this board, in order. */
    private final ArrayList<Move> _moves = new ArrayList<>();
    /** Current side on move. */
    private Piece _turn;
    /** Board of a current game. */
    private Piece[][] _board = new Piece[M][M];
    /** Seed for Zobrist Keys. */
    private final int _zobristSeed = 820329520;
    /** Random key for Zobrist table. [r - 1][c - 1][WP, BP] */
    private final int[][][] _zobristTable = new int[M][M][2];
    /** Random key for generating hash code (turn) */
    private final int[] _turnHash = new int[2];
    /** Number of pieces in every direction at [r - 1][c - 1][-, |, \, /] */
    private int[][][] _piecesCount = new int[8][8][4];

    /** An iterator returning the legal moves from the current board. */
    private class MoveIterator implements Iterator<Move> {
        /** Current piece under consideration. */
        private int _c, _r;
        /** Next direction of current piece to return. */
        private Direction _dir;
        /** Next move. */
        private Move _move;

        /** A new move iterator for turn(). */
        MoveIterator() {
            _c = 1;
            _r = 1;
            _dir = N;
            incr();
        }

        @Override
        public boolean hasNext() {
            return _move != null;
        }

        @Override
        public Move next() {
            if (_move == null) {
                throw new NoSuchElementException("no legal move");
            }

            Move move = _move;
            incr();
            return move;
        }

        @Override
        public void remove() {
        }

        /** Advance to the next legal move. */
        private void incr() {
            Board b = Board.this;
            Move m;
            while (_c <= M) {
                while (_r <= M) {
                    while (_dir != null) {
                        m = Move.create(_c, _r, pieceCountAlong(_c, _r, _dir), _dir, b);
                        if (isLegal(m)) {
                            _move = m;
                            _dir = _dir.succ();
                            return;
                        }
                        _dir = _dir.succ();
                    }
                    _dir = N;
                    _r++;
                }
                _r = 1;
                _c++;
            }
            _move = null;
        }
    }

}
