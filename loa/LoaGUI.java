package loa;

import ucb.gui.TopLevel;
import ucb.gui.LayoutSpec;

import java.awt.event.MouseEvent;

/**
 * A top-level GUI for Canfield solitaire.
 *
 * @author Tim Chan
 */
class LoaGUI extends TopLevel {

    /** Displayed dimensions of a piece image. */
    static final int PIECE_SIZE = 80;

    /** A new window with given TITLE and displaying GAME. */
    LoaGUI(String title, GuiGame game) {
        super(title, true);
        _game = game;

        addMenuButton("Data->Undo", "undo");
        addMenuButton("Data->Toggle Cheat", "toggleCheat");
        addMenuButton("Data->Restart", "restart");
        addMenuButton("Data->Quit", "quit");

        addLabel("Turn: black", "turn", new LayoutSpec("y", 1, "x", 0));
        addLabel("Score: 0", "score", new LayoutSpec("y", 1, "x", 1));
        updateCounts();

        addButton("WP auto", "wpAuto", new LayoutSpec("y", 1, "x", 2));
        addButton("WP manual", "wpManual", new LayoutSpec("y", 1, "x", 3));
        addButton("BP auto", "bpAuto", new LayoutSpec("y", 1, "x", 4));
        addButton("BP manual", "bpManual", new LayoutSpec("y", 1, "x", 5));
        addButton("Pause/Resume", "pause", new LayoutSpec("y", 1, "x", 6));

        _display = new GameDisplay(game);
        add(_display, new LayoutSpec("y", 2, "width", 7));
        _display.setMouseHandler("click", this, "mouseClicked");
        _display.setMouseHandler("release", this, "mouseReleased");
        _display.setMouseHandler("drag", this, "mouseDragged");
        _display.setMouseHandler("press", this, "mousePressed");
        _display.setMouseHandler("move", this, "mouseMoved");

        display(true);
    }

    /**Pause/resume the game.
     *
     * @param dummy
     */
    public void pause(String dummy) {
        _game.setPlaying(!_game.getPlaying());
        updateCounts();
    }

    /**set WP to auto.
    *
    * @param dummy
    */
    public void wpAuto(String dummy) {
        _game.autoCommand("white");
        updateCounts();
    }

    /**set WP to manual.
    *
    * @param dummy
    */
    public void wpManual(String dummy) {
        _game.manualCommand("white");
        updateCounts();
    }

    /**set BP to auto.
    *
    * @param dummy
    */
    public void bpAuto(String dummy) {
        _game.autoCommand("black");
        updateCounts();
    }

    /**set BP to manual.
    *
    * @param dummy
    */
    public void bpManual(String dummy) {
        _game.manualCommand("black");
        updateCounts();
    }

    /** Response to "Quit" menu item.
     * @param dummy */
    public void quit(String dummy) {
        if (showOptions("Really quit?", "Quit?", "question",
                "Yes", "Yes", "No") == 0) {
            System.exit(1);
        }
    }

    /** Display number of points and lines.*/
    void updateCounts() {
        setLabel("turn", "Turn: " + _game.getBoard().turn().fullName());
        setLabel("score", String.format("Score: %1$6s",
                MachinePlayer.eval(_game.getBoard())));
    }


    /** Response to "Undo" menu item.
     * @param dummy*/
    public void undo(String dummy) {
        _game.getBoard().retract();
        updateCounts();
        _display.repaint();
    }


    /** Response to "Toggle Cheat" menu item.
     * @param dummy*/
    public void toggleCheat(String dummy) {
        toggleCheat();
        updateCounts();
        _display.repaint();
    }

    /** Toggle cheating. */
    void toggleCheat() {
        _display.setCheat(!_display.getCheat());
    }

    /** Response to "restart" menu item.
     * @param dummy*/
    public void restart(String dummy) {
        _game.getBoard().clear();
        _game.manualCommand("white");
        _game.manualCommand("black");
        updateCounts();
        _display.repaint();
    }

    /**
     * check if cursor is in the board area.
     * @param x x
     * @param y y
     * @return boolean
     */
    boolean cursorOnBoard(int x, int y) {
        return (x < GameDisplay.BOARD_SIZE) && (y < GameDisplay.BOARD_SIZE);
    }

    /**
     * x co-ordinate to c.
     * @param x x
     * @return int
     */
    public int xToC(int x) {
        return 1 + (int) Math.ceil(x / PIECE_SIZE);
    }

    /**
     * y co-ordinate to r.
     * @param y y
     * @return int
     */
    public int yToR(int y) {
        return 8 - (int) Math.floor(y / PIECE_SIZE);
    }

    /** Return True if the cursor is on a WP at cursorX, cursorY.
     *  @param cursorX
     *      X-coordinate of mouse relative to the frame.
     *  @param cursorY
     *      Y-coordinate of mouse relative to the frame.
     *  @return boolean
     */
    public boolean onWP(int cursorX, int cursorY) {
        int c = xToC(cursorX);
        int r = yToR(cursorY);
        return _game.getBoard().get(c, r) == Piece.WP;
    }

    /** Return True if the cursor is on a BP at cursorX, cursorY.
     *  @param cursorX
     *      X-coordinate of mouse relative to the frame.
     *  @param cursorY
     *      Y-coordinate of mouse relative to the frame.
     *  @return boolean
     */
    public boolean onBP(int cursorX, int cursorY) {
        int c = xToC(cursorX);
        int r = yToR(cursorY);
        return _game.getBoard().get(c, r) == Piece.BP;
    }

    /** Flip the piece at cursorX, cursorY.
     *  @param cursorX
     *      X-coordinate of mouse relative to the frame.
     *  @param cursorY
     *      Y-coordinate of mouse relative to the frame.
     */
    public void switchPieces(int cursorX, int cursorY) {
        if (onBP(cursorX, cursorY)) {
            _game.getBoard().set(xToC(cursorX), yToR(cursorY), Piece.WP);
        } else if (onWP(cursorX, cursorY)) {
            _game.getBoard().set(xToC(cursorX), yToR(cursorY), Piece.BP);
        }
    }

    /** Action in response to mouse-moving event EVENT. */
    public synchronized void mouseMoved(MouseEvent event) {
        pressedX = event.getX();
        if (event.getX() <= GameDisplay.FF_X) {
            GameDisplay.setFaceDirL();
        } else {
            GameDisplay.setFaceDirR();
        }

        if (_game.getBoard().gameOver()) {
            setLabel("score",
                    String.format(_game.getBoard().gameOverP().fullName()
                    + " Won!" + "@ " + MachinePlayer.eval(_game.getBoard())));
            _game.setPlaying(false);
        }

        int turn = _game.getBoard().turn().ordinal();
        if (_game.getPlaying() && _game.getPlayer(turn) instanceof MachinePlayer
                && cursorOnBoard(event.getX(), event.getY())) {
            Move mv = _game.getPlayer(turn).makeMove();
            _game.getBoard().makeMove(mv);
            updateCounts();
        }
        _display.repaint();
    }

    /** Action in response to mouse-clicking event EVENT. */
    public synchronized void mouseClicked(MouseEvent event) {
        if (_display.getCheat()) {
            switchPieces(event.getX(), event.getY());
        }
        updateCounts();
        System.out.println(xToC(event.getX()) + " " + yToR(event.getY()));
        updateCounts();
        _display.repaint();
    }

    /** Location where mouse is clicked. */
    private int pressedX = 0, pressedY = 0;

    /** Called when the mouse has been pressed.
     *
     * @param event
     *      A MouseEvent
     */
    public synchronized void mousePressed(MouseEvent event) {
        pressedX = event.getX();
        pressedY = event.getY();
    }

    /** Action in response to mouse-released event EVENT.
     *
     *
     * */
    public synchronized void mouseReleased(MouseEvent event) {
        int c0 = xToC(pressedX);
        int r0 = yToR(pressedY);
        int c1 = xToC(event.getX());
        int r1 = yToR(event.getY());
        Move mv = Move.create(c0, r0, c1, r1, _game.getBoard());
        if (_game.getBoard().isLegal(mv)
                && _game.getPlayer(_game.getBoard().turn().ordinal())
                instanceof HumanPlayer) {
            _game.getBoard().makeMove(mv);
        }
        updateCounts();
        _display.repaint();
    }

    /** Action in response to mouse-dragging event EVENT. */
    public synchronized void mouseDragged(MouseEvent event) {
    }

    /** The board widget. */
    private final GameDisplay _display;

    /** The game I am consulting. */
    private final Game _game;
}
