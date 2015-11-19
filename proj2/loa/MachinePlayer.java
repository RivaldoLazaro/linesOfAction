package loa;

import static loa.Piece.BP;
import static loa.Piece.EMP;
import static loa.Piece.WP;

import java.util.ArrayList;

/**
 * An automated Player.
 *
 * @author Tim Chan
 */
class MachinePlayer extends Player {

    /** Dummy really bad move. */
    static final int WON_GAME = Integer.MAX_VALUE;

    /** Dummy really bad move. */
    static final int LOST_GAME = Integer.MIN_VALUE;

    /** Chance. */
    static final int CHANCE = 50;

    /** Cut off. */
    static final int CUT_OFF = 1000;

    /** Percentage. */
    static final double PERC = 0.9;

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
    }

    /**
     * Pick a random move.
     * @param b board
     * @return move
     */
    Move pickMove(Board b) {
        ArrayList<Move> mov = new ArrayList<Move>();
        for (Move mv : b) {
            mov.add(mv);
        }
        int i = getGame().randInt(mov.size());
        return mov.get(i);
    }

    @Override
    Move makeMove() {

        Move mv = guessBestMove(getBoard().turn(), getBoard());

        if (getBoard().turn() == WP) {
            if (getGame().randInt(CHANCE) < (PERC * CHANCE)) {
                mv = findBestMove(getBoard().turn(), getBoard(), 2, CUT_OFF);
            } else {
                mv = pickMove(getBoard());
            }
        } else {
            if (getGame().randInt(CHANCE) < (PERC * CHANCE)) {
                mv = findBestMove(getBoard().turn(), getBoard(), 2, -CUT_OFF);
            } else {
                mv = pickMove(getBoard());
            }
        }
        return mv;
    }

    /** Return the last move.
     * @param p piece
     * @param board board
     * @param depth depth
     * @param cutoff cutoff
     * @return Move
     * */
    Move findBestMove(Piece p, Board board, int depth, int cutoff) {
        if (board.gameOverP() == p) {
            return board.lastMove();
        }
        if (depth == 0) {
            return guessBestMove(p, board);
        }
        Move v = new Move(1, 1, 1, 1, BP, EMP);
        v.setValue(LOST_GAME);
        Move bestSoFar = v;

        for (Move mv : board) {
            board.makeMove(mv);

            Move response = findBestMove(board.turn(), board,
                    depth - 1, -bestSoFar.value());

            if (-response.value(board) > bestSoFar.value()) {
                bestSoFar = mv;
                bestSoFar.setValue(-response.value());
                board.retract();
                if (bestSoFar.value() >= cutoff) {
                    break;
                }
            } else {
                board.retract();
            }
        }
        return bestSoFar;
    }

    /** Guess best move.
     *
     * @param p piece
     * @param b board
     * @return Move
     */
    Move guessBestMove(Piece p, Board b) {
        Move best = null;
        boolean init = true;
        int bestness = 0;
        Board board = b;
        assert (b.turn() == p);
        if (p == WP) {
            for (Move m : board) {
                board.makeMove(m);
                int current = eval(board);
                if (init) {
                    bestness = current;
                    init = !init;
                }
                if (current >= bestness) {
                    best = m;
                    bestness = current;
                }
                board.retract();
            }
        }
        if (p == BP) {
            for (Move m : board) {
                board.makeMove(m);
                int current = eval(board);
                if (init) {
                    bestness = current;
                    init = !init;
                }
                if (current <= bestness) {
                    best = m;
                    bestness = current;
                }
                board.retract();
            }
        }

        if (best == null) {
            System.out.println("null move");
            for (Move m : getBoard()) {
                best = m;
            }
        }
        return best;
    }

    /**
     * Returns an evaluation of a board, higher is in favour of WP. Find the
     * average centre location C from all Side's clusters' own average location
     * L_k, with cluster size S_k: bpSum = sum((||C - L_k||)^2 / S_k) Notice,
     * the bigger the no. the more spread wpSum = sum((||C - L_k||)^2 / S_k) so
     * we do bpSum - wpSum bpSum - wpSum
     * @param board board
     */
    public static int eval(Board board) {

        ArrayList<int[]> wpCluster = board.piecesDiscontiguity(WP);
        ArrayList<int[]> bpCluster = board.piecesDiscontiguity(BP);
        int bpSum = 0;
        int wpSum = 0;
        int wpNoOfCluster = wpCluster.size();
        int bpNoOfCluster = bpCluster.size();
        int[] wpC = new int[wpNoOfCluster];
        int[] wpR = new int[wpNoOfCluster];
        int[] wpS = new int[wpNoOfCluster];
        int[] bpC = new int[bpNoOfCluster];
        int[] bpR = new int[bpNoOfCluster];
        int[] bpS = new int[bpNoOfCluster];

        for (int i = 0; i < wpNoOfCluster; i++) {
            int[] cluster = wpCluster.get(i);
            wpC[i] = cluster[0];
            wpR[i] = cluster[1];
            wpS[i] = cluster[2];
        }

        for (int i = 0; i < bpNoOfCluster; i++) {
            int[] cluster = bpCluster.get(i);
            bpC[i] = cluster[0];
            bpR[i] = cluster[1];
            bpS[i] = cluster[2];
        }

        bpSum = pSum(bpC, bpR, bpS);
        wpSum = pSum(wpC, wpR, wpS);
        return bpSum - wpSum;
    }

    /**pSum = sum((||C - L_k||)^2 / S_k).
     *
     * @param c col
     * @param r row
     * @param s size
     * @return
     */
    public static int pSum(int[] c, int[] r, int[] s) {
        int size = c.length;
        if (size == 0) {
            return WON_GAME;
        }
        double result = 0;
        int[] centreLoc = new int[] { 0, 0 };
        for (int i = 0; i < size; i++) {
            centreLoc[0] += c[i];
            centreLoc[1] += r[i];
        }
        centreLoc[0] = (int) Math.floor(centreLoc[0] / size);
        centreLoc[1] = (int) Math.floor(centreLoc[1] / size);
        int cc = centreLoc[0];
        int cr = centreLoc[1];
        for (int i = 0; i < size; i++) {
            result += distanceSquared(cc, cr, c[i], r[i]) / s[i];
        }
        return (int) Math.floor(result * 100) + 1;
    }

    /** Distance squared between 2pts.
     *
     * @param c0 col
     * @param r0 row
     * @param c1 col
     * @param r1 row
     * @return double
     */
    public static double distanceSquared(int c0, int r0, int c1, int r1) {
        return (c0 - c1) * (c0 - c1) + (r0 - r1) * (r0 - r1);
    }
}
