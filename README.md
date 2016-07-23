# Lines of Action
![](demo.gif?raw=true)

### 1. Introduction

Lines of Action is a board game invented by Claude Soucie. It is played on a checkerboard with ordinary checkers pieces. The two players take turns, each moving a piece, and possibly capturing an opposing piece. The goal of the game is to get all of one’s pieces into one group of pieces that are adjacent horizontally, vertically, or diagonally.

### 2. AI Algorithm
* min max algorithm
* clustering
* randomness for non-repeatitive game

### 3. Files

Files in this project:

* `ReadMe`:             This file
* `Makefile`:           A makefile (for gmake) that will compile your
			            files and run tests.  You must turn in a Makefile, 'gmake ' must compile all                         your files, and 'gmake check' must perform all your tests.
* `loa`:                    Subdirectory holding files for integration testing:
    * `Makefile`:           A convenience Makefile so that you can issue compilation commands from                               the game directory.
    * `Piece.java`:         An enumerated type describing the kinds of pieces.
    * `Move.java`:          Represents a single move.
    * `Direction.java`:     Represents a direction (north, south, etc.) on a board.
    * `Board.java`:         Represents a game board.  Contains much of the machinery for checking or                             generating possible moves.
    * `HumanPlayer.java`:   A kind of Player that reads moves from the standard input (i.e.,                                     presumably from a human player).
    * `MachinePlayer.java`: A kind of Player that chooses its moves automatically.
    * `Reporter.java`:      A utility class for handling debugging output.
    * `UnitTest.java`:      Class that performs unit testing of the loa package.
* `testing`:                Subdirectory holding files for integration testing:
    * `Makefile`:           Directions for testing.
    * `Piece.java`:         Test cases.  Each one is input to a testing script test-loa.
    * `Move.java`:          Correct output from some test cases, containing
    			            dumps of the board and win messages.

### 4. Detailed explanation
[here]

### 5. Running the Database System
Go into project root directory (you will see folders `loa`, `testing`), remove all old classes:
```sh
$ make clean
```

Compile all java files (A `make` file has been made to achieve this, go inside `make` to see more options):
```sh
$ make
```

Test Lines of Action source integrity (Not exhaustive) (Optional) (The game will be run and the output will be compared against expected output):
```sh
$ make check
```

Now the system is ready to run. Like always, we have CLI and GUI:

CLI:
```sh
$ java loa.Main
```

GUI:
```sh
$ java loa.Main --display
```

### 6. Class Project Site
[here]

[here]: <https://inst.eecs.berkeley.edu/~cs61b/fa15/hw/proj2/proj2.pdf>