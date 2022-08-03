package Control;

/*
     Team: jamAI
     Student #: 20344393, 20364441, 20483142
 */

import model.GameBoard;
import model.GamePiece;
import model.Player;
import ui.UI;

public class GameControl implements Runnable {

    public UI ui;
    Player[] players;
    GameBoard board;
    int playerTurn;
    boolean flag;
    boolean announced = false;

    public GameControl(UI ui, int playerTurn, boolean flag) {
        this.ui = ui;
        this.playerTurn = playerTurn;
        this.board = ui.board;
        this.players = ui.getPlayers();
        this.flag = flag;
    }

    @Override
    public void run() {

        ui.getPlayers()[0].setPlayerName(ui.generatePlayer(1));
        players[0].setPlayerName(ui.getPlayers()[0].getPlayerName());
        ui.getPlayers()[1].setPlayerName(ui.generatePlayer(2));
        players[1].setPlayerName(ui.getPlayers()[1].getPlayerName());

        ui.displayGreeting(players[playerTurn].getPlayerName() +
                " goes first!\n" +
                "To play click and drag a piece\n" +
                "to a valid place on the board.\n" +
                "You can press the r key while holding\n" +
                "a piece to rotate or the f key to flip.\n" +
                "GoodLuck!");

        board.setBoard(9, 4, players[0].getPlayerSymbol());
        ui.setBoard(9, 4, players[0].getPlayerSymbol());
        board.setBoard(4, 9, players[1].getPlayerSymbol());
        ui.setBoard(4, 9, players[1].getPlayerSymbol());


        //Game continues while both players still have remaining pieces
        while (!players[0].getPlayerPieces().isEmpty() || !players[1].getPlayerPieces().isEmpty()) {

            int currentPlayer = playerTurn % 2;
            ui.printBoard();

            //Game is ended if it is impossible for either players to play a piece
            if (!board.validMoveLeft(board.getBoard(), players[0], false) && !board.validMoveLeft(board.getBoard(), players[1], false)) {
                break;
            }

            //Pass the player's turn if they cannot place a piece
            if (currentPlayer == 0) {
                if (!(board.validMoveLeft(board.getBoard(), players[0], flag))) {
                    currentPlayer = (++playerTurn) % 2;
                    board.validMoveLeft(board.getBoard(), players[1], flag);

                    if (!announced) {
                        ui.displayGreeting(players[0].getPlayerName() + " has no moves left!");
                        announced = true;
                    }
                }
            } else {
                if (!(board.validMoveLeft(board.getBoard(), players[1], flag))) {
                    currentPlayer = (++playerTurn) % 2;
                    board.validMoveLeft(board.getBoard(), players[0], flag);

                    if (!announced) {
                        ui.displayGreeting(players[1].getPlayerName() + " has no moves left!");
                        announced = true;
                    }
                }
            }

            ui.setPlayerTurn(currentPlayer);
            ui.updateString("Current Player is " + players[currentPlayer].getPlayerName());

            String pieceName = ui.pickPiece(players[currentPlayer]);
            int[] piece = GamePiece.getShape(pieceName).clone();
            piece = ui.manipulatePiece(piece, players[currentPlayer].getPlayerSymbol());
            int[] coordinates = ui.getCoordinates();


            if (!players[currentPlayer].getHasMadeFirstMove()) {
                if (board.isValidStartMove(piece, players[currentPlayer].getPlayerSymbol(), board.getBoard(), coordinates[1], coordinates[0])) {
                    ui.placePiece();
                    ui.pieceDownSound();
                    GamePiece.placePiece(piece, coordinates[1], coordinates[0], board.getBoard(), players[currentPlayer].getPlayerSymbol());
                    GamePiece.placePiece(piece, coordinates[1], coordinates[0], ui.getBoard(), players[currentPlayer].getPlayerSymbol());

                    playerTurn++;
                    players[currentPlayer].madeFirstMove();
                } else {
                    ui.undoLastPiece();
                    ui.alertInvalidMove();
                    players[currentPlayer].addPiece(pieceName);
                }
            } else {
                if (board.isValidMove(pieceName, piece, players[currentPlayer].getPlayerSymbol(), board.getBoard(), coordinates[1], coordinates[0])) {
                    ui.placePiece();
                    ui.pieceDownSound();
                    GamePiece.placePiece(piece, coordinates[1], coordinates[0], board.getBoard(), players[currentPlayer].getPlayerSymbol());
                    GamePiece.placePiece(piece, coordinates[1], coordinates[0], ui.getBoard(), players[currentPlayer].getPlayerSymbol());
                    playerTurn++;
                } else {
                    ui.undoLastPiece();
                    ui.alertInvalidMove();
                    players[currentPlayer].addPiece(pieceName);
                }
            }

            ui.unlockScreen();

        }

        ui.updateString("GAME OVER");

        for (Player player : players) {
            player.updateScore();
        }

        ui.announceWinner();
    }
}
