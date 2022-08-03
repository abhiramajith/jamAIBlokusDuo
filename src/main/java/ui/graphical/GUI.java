package ui.graphical;

/*
     Team: jamAI
     Student #: 20344393, 20364441, 20483142
 */

import com.badlogic.gdx.Gdx;
import model.GameBoard;
import model.GamePiece;
import model.Player;
import ui.UI;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.Objects;
import java.util.Scanner;

public class GUI implements UI {

    private PipedOutputStream pipedOutputStream;
    private PipedInputStream pipedInputStream;
    private HelloGame helloGame;
    private Scanner scanner;
    GameBoard gameBoard;
    char[][] board;
    public Player[] players;
    private int playerTurn;


    public GUI(GameBoard gameBoard, Player[] players) throws IOException {
        pipedOutputStream = new PipedOutputStream();
        pipedInputStream = new PipedInputStream(pipedOutputStream);
        scanner = new Scanner(pipedInputStream);
        this.gameBoard = gameBoard;
        this.board = this.gameBoard.getBoard();
        this.players = players;
    }

    void setGame(HelloGame helloGame) {
        this.helloGame = helloGame;
    }

    public PipedOutputStream getPipedOutputStream() {
        return pipedOutputStream;
    }

    public void displayGreeting(String greeting) {
        helloGame.postRunnable(new Runnable() {
            @Override
            public void run() {
                helloGame.setGreeting(greeting);
            }
        });
    }


    public void undoLastPiece() {
        helloGame.postRunnable(new Runnable() {
            @Override
            public void run() {
                helloGame.undoLast();
            }
        });
    }

    public void pieceDownSound() {
        helloGame.postRunnable(new Runnable() {
            @Override
            public void run() {
                helloGame.playPieceDownSound(helloGame.isSoundMuted);
            }
        });
    }

    @Override
    public void unlockScreen() {
        helloGame.postRunnable(new Runnable() {
            @Override
            public void run() {
                Gdx.input.setInputProcessor(helloGame.eventHandler);
            }
        });
    }

    @Override
    public void alertInvalidMove() {
        helloGame.postRunnable(new Runnable() {
            @Override
            public void run() {
                helloGame.playInvalidMoveSound(helloGame.isSoundMuted);
            }
        });
    }

    @Override
    public void placePiece() {
    }

    public void setBoard(int i, int j, char symbol) {
        board[i][j] = symbol;
    }

    public char[][] getBoard() {
        return board;
    }

    public Player[] getPlayers() {
        return players;
    }

    @Override
    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

    @Override
    public char getPlayerTurn() {
        if (playerTurn == 0) {
            return '0';
        } else {
            return '1';
        }
    }

    @Override
    public void printBoard() {
    }

    @Override
    public String generatePlayer(int playerNumber) {

        String playerName;
        playerName = scanner.nextLine().trim();

        return playerName;
    }

    @Override
    public String pickPiece(Player player) {
        //gets player piece from pipedstream
        String playerPiece = scanner.next();

        while (!player.getPlayerPieces().contains(playerPiece.toUpperCase())) {
            playerPiece = scanner.next();
        }

        player.removePiece(playerPiece);
        return playerPiece;

    }

    @Override
    public int[] manipulatePiece(int[] piece, char playerSymbol) {


        previewPiece(piece, playerSymbol);

        String option = scanner.next();

        while (!Objects.equals(option.toLowerCase(), "p")) {

            if (Objects.equals(option.toLowerCase(), "r")) {

                GamePiece.rotatePiece(piece, 'R');

            } else if (Objects.equals(option.toLowerCase(), "f")) {
                GamePiece.flip(piece);
            }
            previewPiece(piece, playerSymbol);

            option = scanner.next();
        }

        return piece;
    }

    @Override
    public int[] getCoordinates() {

        int[] coordinates = new int[2];

        coordinates[1] = Integer.parseInt(scanner.next());

        coordinates[0] = Integer.parseInt(scanner.next());

        return coordinates;
    }

    public static void previewPiece(int[] piece, char symbol) {

        //Create a board to place a chosen piece and rotate it
        char[][] pieceBoard = new char[10][10];

        //Using existing method to place piece in centre of created board
        GamePiece.placePiece(piece, 5, 5, pieceBoard, symbol);

        //Hold minimum and maximum values for the rows (I) and columns (J) that contain the placed piece
        int minJ = pieceBoard.length;
        int minI = pieceBoard.length;
        int maxJ = 0;
        int maxI = 0;

        for (int i = pieceBoard.length - 1; i >= 0; i--) {
            for (int j = 0; j < pieceBoard[i].length; j++) {

                //Finding the maximum bounds of the piece
                if (pieceBoard[i][j] == symbol) {
                    if (j < minJ) {
                        minJ = j;
                    }
                    if (i < minI) {
                        minI = i;
                    }
                    if (j > maxJ) {
                        maxJ = j;

                    }
                    if (i > maxI) {
                        maxI = i;
                    }
                }
            }
        }

    }

    public void updateString(String message) {
        helloGame.postRunnable(new Runnable() {
            @Override
            public void run() {
                helloGame.showMessage(message);
            }
        });
    }

    @Override
    public void announceWinner() {
        helloGame.gameEnded = true;
        int[] playerScore = {players[0].getPlayerScore(), players[1].getPlayerScore()};
        String message = "";

        if (playerScore[0] > playerScore[1])
            message = "Player: " + players[0].getPlayerName() + " is the winner!" + "(" + playerScore[0] + " : " + playerScore[1] + ")";
        else
            message = "Player: " + players[1].getPlayerName() + " is the winner!" + "(" + playerScore[0] + " : " + playerScore[1] + ")";

        String finalMessage = message;
        helloGame.postRunnable(new Runnable() {
            @Override
            public void run() {
                helloGame.showDialog(finalMessage);
                helloGame.playWinSound(helloGame.isSoundMuted);
            }
        });
    }
}

