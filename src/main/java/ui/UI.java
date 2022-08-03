package ui;
/*
     Team: jamAI
     Student #: 20344393, 20364441, 20483142
 */

import model.GameBoard;
import model.Player;

public interface UI {

    void displayGreeting(String greeting);

    void printBoard();

    String generatePlayer(int i);

    String pickPiece(Player player);

    int[] manipulatePiece(int[] piece, char playerSymbol);

    int[] getCoordinates();

    void alertInvalidMove();

    void setBoard(int i, int j, char symbol);

    char[][] getBoard();

    Player[] getPlayers();

    GameBoard board = new GameBoard();

    void setPlayerTurn(int playerTurn);

    char getPlayerTurn();

    void undoLastPiece();

    void placePiece();

    void updateString(String message);

    void announceWinner();

    void pieceDownSound();

    void unlockScreen();
}
