// Remove all comments that begin with //, and replace appropriately.
// Feel free to modify ANYTHING in this file.
package loa;

import static loa.Piece.BP;
import static loa.Piece.WP;

import java.util.ArrayList;

/** An automated Player.
 *  @author Tim Chan*/
class MachinePlayer extends Player {

    /** A MachinePlayer that plays the SIDE pieces in GAME. */
    MachinePlayer(Piece side, Game game) {
        super(side, game);
        // FILL IN
    }

    @Override
    Move makeMove() {
    	Move mv = bestMove();
    	
    	return mv;
    }
    
    Move bestMove() {
    	Move best = null;
    	boolean init = true;
    	int bestness = 0;
    	Board board = getBoard();
    	Piece turn = board.turn();
    	if(turn == WP) {
    		for(Move m : getBoard()){ 
            	board.makeMove(m);
            	int current = eval(board);
            	if(init) {
            		bestness = current;
            		init = !init;
            	}
            	if(current >= bestness) {
            		best = m;
            		bestness = current;
            	}
            	board.retract();
            }	
    	}
    	if(turn == BP) {
    		for(Move m : getBoard()){ 
            	board.makeMove(m);
            	int current = eval(board);
            	if(init) {
            		bestness = current;
            		init = !init;
            	}
            	if(current <= bestness) {
            		best = m;
            		bestness = current;
            	}
            	board.retract();
            }	
    	}
        
    	if(best == null) {
    		System.out.println("null move");
    		for(Move m : getBoard()){ 
    			best = m;
    		}
    	}
        return best;
    }
    

    /** Returns an evaluation of a board, higher is in favour of WP. 
     * Find the average centre location C from all Side's clusters' own average location L_k,
     * with cluster size S_k:
     * bpSum = sum((||C - L_k||)^2 / S_k)	Notice, the bigger the no. the more spread
     * wpSum = sum((||C - L_k||)^2 / S_k)	so we do bpSum - wpSum
     * bpSum - wpSum
     *   */
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
		
		for(int i = 0; i < wpNoOfCluster; i++) {
			int[] cluster = wpCluster.get(i);
			wpC[i] = cluster[0];
			wpR[i] = cluster[1];
			wpS[i] = cluster[2];
		}
		
		for(int i = 0; i < bpNoOfCluster; i++) {
			int[] cluster = bpCluster.get(i);
			bpC[i] = cluster[0];
			bpR[i] = cluster[1];
			bpS[i] = cluster[2];
		}
		
    	bpSum = pSum(bpC, bpR, bpS);
    	wpSum = pSum(wpC, wpR, wpS);
    	return bpSum - wpSum;
    }
    
    
    /** pSum = sum((||C - L_k||)^2 / S_k)*/
    public static int pSum(int[] c, int[] r, int[] s) {
    	int size = c.length;
    	double result = 0;
    	int[] centreLoc = new int[]{0, 0};
    	for(int i = 0; i < size; i++) {
    		centreLoc[0] += c[i];
    		centreLoc[1] += r[i];
    	}
    	centreLoc[0] = (int)Math.floor(centreLoc[0] / size );
    	centreLoc[1] = (int)Math.floor(centreLoc[1] / size );
    	int Cc = centreLoc[0];
    	int Cr = centreLoc[1];
    	for(int i = 0; i < size; i++) {
    		result += distanceSquared(Cc, Cr, c[i], r[i]) / s[i];
    	}
    	return (int) Math.floor(result * 100) + 1;	//+1 in case anything weird happen.
    }
    
    /** Distance squared between 2pts. */
    public static double distanceSquared(int c0, int r0, int c1, int r1) {
    	return (c0 - c1) * (c0 - c1) + (r0 - r1) * (r0 - r1);
    }
}
