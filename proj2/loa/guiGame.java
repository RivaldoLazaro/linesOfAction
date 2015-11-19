package loa;

/** Represents one game of Lines of Action.
 *  @author  Tim Chan*/
class GuiGame extends Game {

    @Override
    public void play() {
        _board = new Board();
        this.manualCommand("white");
        this.manualCommand("black");
        this.setPlaying(true);
        _display = new LoaGUI("LOA", this);
    }

    /** The official game board. */
    private Board _board;

    /** The display. */
    private LoaGUI _display;
}
