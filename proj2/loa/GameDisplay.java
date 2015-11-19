package loa;

import ucb.gui.Pad;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.imageio.ImageIO;

import java.io.InputStream;
import java.io.IOException;

/**
 * A widget that displays a Pinball playfield.
 *
 * @author P. N. Hilfinger
 */
class GameDisplay extends Pad {

    /** Color of display field. */
    private static final Color BACKGROUND_COLOR = Color.white;

    /** Window size. */
    static final int BOARD_WIDTH = 1100, BOARD_HEIGHT = 645;

    /** Game board size. */
    static final int BOARD_SIZE = 640;

    /** Displayed dimensions of a piece image. */
    static final int PIECE_SIZE = 80;

    /** Displayed dimensions of a instruction image. */
    static final int INS_WIDTH = 391, INS_HEIGHT = 626;

    /** Displayed location of a free face. */
    static final int FF_X = 655, FF_Y = 70;

    /** Displayed dimensions of Freeface. */
    static final int FF_WIDTH = 50, FF_HEIGHT = 50;

    /** Direction of free face. */
    private static String faceDir = "freefaceL.png";

    /** Set direction of free face to left. */
    static void setFaceDirL() {
        faceDir = "freefaceL.png";
    }

    /** Set direction of free face to right. */
    static void setFaceDirR() {
        faceDir = "freefaceR.png";
    }

    /** A graphical representation of GAME. */
    public GameDisplay(Game game) {
        _game = game;
        setPreferredSize(BOARD_WIDTH, BOARD_HEIGHT);
    }

    /** Return an Image read from the resource named NAME. */
    private Image getImage(String name) {
        InputStream in =
                getClass().getResourceAsStream("/loa/resources/" + name);
        try {
            return ImageIO.read(in);
        } catch (IOException excp) {
            return null;
        }
    }

    /** Return an Image of Piece.
     * @param p Piece*/
    private Image getPieceImage(Piece p) {
        return getImage("playing-cards/" + p + ".png");
    }

    /** Draw Piece at X, Y on G.
     * @param g Graphic
     * @param p Piece
     * @param x x
     * @param y y */
    private void paintPiece(Graphics2D g, Piece p, int x, int y) {
        if (p == Piece.BP || p == Piece.WP) {
            g.drawImage(getPieceImage(p), x, y,
                    PIECE_SIZE, PIECE_SIZE, null);
        }
    }

    /** Draw Pieces.
    * @param b Board
    * @param g
    *      Graphic2D object
    */
    void drawPieces(Graphics2D g, Board b) {
        for (int c = 1; c <= 8; c++) {
            for (int r = 1; r <= 8; r++) {
                Piece p = b.get(c, r);
                if (p != Piece.EMP) {
                    paintPiece(g, p, (c - 1) * PIECE_SIZE,
                            (8 - r) * PIECE_SIZE);

                }
            }
        }
    }

    @Override
    public synchronized void paintComponent(Graphics2D g) {

        g.setColor(BACKGROUND_COLOR);
        Rectangle b = g.getClipBounds();
        g.fillRect(0, 0, b.width, b.height);
        g.drawImage(getImage("board.png"), 0, 0, BOARD_SIZE, BOARD_SIZE, null);
        g.drawImage(getImage("ins.png"), BOARD_SIZE + 10, 10,
                INS_WIDTH, INS_HEIGHT, null);
        g.drawImage(getImage(faceDir), FF_X,  FF_Y,
                FF_HEIGHT, FF_WIDTH, null);
        drawPieces(g, _game.getBoard());
    }

    /** Get the _cheat.
     * @return Boolean */
    Boolean getCheat() {
        return _cheat;
    }

    /** Set the _cheat.
     * @param s state */
    void setCheat(Boolean s) {
        _cheat = s;
    }

    /** Game I am displaying. */
    private final Game _game;

    /** Cheat off = 0 or cheat on = 1. */
    private boolean _cheat = false;

}
