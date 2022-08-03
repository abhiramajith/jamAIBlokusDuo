package ui.text;

/*
     Team: jamAI
     Student #: 20344393, 20364441, 20483142
 */

import model.GameBoard;
import model.GamePiece;
import model.Player;
import ui.UI;

import java.util.Objects;
import java.util.Scanner;

public class TextUI implements UI{
    Scanner scanner = new Scanner(System.in);

    GameBoard gameBoard;
    char [][] board;
    public Player [] players;
    private int playerTurn;
//    char [] chars = {'\u2488', '\u2488', '\u2488', '\u2488', '\u2488', '\u2488', '\u2488', '\u2488', '\u2488', '\u2488'
//            , '\u2488', '\u2488', '\u2488', '\u2494'};

    public TextUI(GameBoard gameboard, Player [] players) {
        this.gameBoard = gameboard;
        this.board = this.gameBoard.getBoard();
        this.players = players;
    }

    public void setBoard(int i, int j, char symbol){
        board[i][j] = symbol;
    }

    public char[][] getBoard(){
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
    public void undoLastPiece() {
    }

    @Override
    public void placePiece() {

    }

    @Override
    public void updateString(String message) {

    }

    @Override
    public void announceWinner() {
        int[] playerScore = {players[0].getPlayerScore(),players[1].getPlayerScore()};

        for(int i=0;i<2;i++){
            System.out.print("\nPlayer " + (i+1) + ": " + playerScore[i] + " pts");
        }

        if(playerScore[0]>playerScore[1]) {
            System.out.println("Player 1 wins");
        }
        else {
            System.out.println("Player 2 wins");
        }
        System.out.println("GAME OVER");

    }

    @Override
    public void pieceDownSound() {

    }

    @Override
    public void unlockScreen() {

    }

    public void printBoard(){
        System.out.println();
        for(int i = board.length-1; i>=0; i--){
            //aligning the print due to the misalignment from the double digits
            if (i < 10) {
                System.out.print(" ");
            }
            System.out.print(i);
            for(int j = 0; j < board[i].length; j++){
                System.out.print(" " + board[i][j]);
            }
            System.out.println();
        }
//        System.out.print("  ");

        System.out.print("   0 1 2 3 4 5 6 7 8 9 10111213");

    }

    public String generatePlayer(int playerNumber) {
        String playerName;

        System.out.println("\nEnter the name of Player " + playerNumber + ": ");
        playerName = scanner.nextLine().trim();
        while (Objects.equals(playerName , "")){
            System.out.println("Play name cannot be blank!\n");
            System.out.println("Enter the name of Player " + playerNumber + ": ");
            playerName = scanner.nextLine().trim();
        }
        return playerName;
    }

    public String pickPiece(Player player){

        System.out.println("\n" + player.getPlayerName() + "'s turn (" + player.getPlayerSymbol() + "): ");
        System.out.println(player.getPlayerPieces());
        System.out.println("Pick a piece to place on the board from your set: ");

        String playerPiece = scanner.next();

        while (!player.getPlayerPieces().contains(playerPiece.toUpperCase())){

            System.out.println("\nInvalid piece does not belong to your set\n");
            System.out.println("Pick a piece to place on the board from your set: ");
            playerPiece = scanner.next();

        }

        player.removePiece(playerPiece);
        return playerPiece;

    }

    public int[] manipulatePiece(int[] piece, char symbol){


        previewPiece(piece,symbol);
        System.out.println("Enter 'r' to rotate, 'f' to flip, or 'p' to place the gamepiece: ");

        String option = scanner.next();

        while (!Objects.equals(option.toLowerCase(), "p")){

            if (Objects.equals(option.toLowerCase(), "r")){

                GamePiece.rotatePiece(piece,  'R');

            }
            else if(Objects.equals(option.toLowerCase(), "f")){
                GamePiece.flip(piece);
            }
            previewPiece(piece,symbol);
            System.out.println("Enter 'r' to rotate, 'f' to flip, or 'p' to place the gamepiece: ");
            option = scanner.next();
        }

        return piece;

    }

    public static void previewPiece(int[] piece, char symbol) {

        //Create a board to place a chosen piece and rotate it
        char[][] pieceBoard = new char[10][10];

        //Using existing method to place piece in centre of created board
        GamePiece.placePiece(piece, 5,5,pieceBoard, symbol);

        //Hold minimum and maximum values for the rows (I) and columns (J) that contain the placed piece
        int minJ = pieceBoard.length;
        int minI = pieceBoard.length;
        int maxJ = 0;
        int maxI = 0;

        for (int i = pieceBoard.length - 1; i >= 0; i--) {
            for (int j = 0; j < pieceBoard[i].length; j++) {

                //Finding the maximum bounds of the piece
                if (pieceBoard[i][j] == symbol){
                    if( j < minJ){
                        minJ = j;
                    }
                    if ( i < minI){
                        minI = i;
                    }
                    if (j > maxJ){
                        maxJ = j;

                    }
                    if( i > maxI){
                        maxI = i;
                    }
                }
            }
        }

        //Board must be printed bottom to top for correct display
        System.out.println();
        for (int i = maxI ; i >= minI; i--) {
            for (int j = minJ; j <= maxJ; j++) {

                if (pieceBoard[i][j] == symbol) {
                    System.out.print(pieceBoard[i][j] + " ");
                }
                else {
                    System.out.print("  ");
                }
            }
            System.out.println();
        }

    }

    public int [] getCoordinates() {
        int [] coordinates = new int[2];

        System.out.println("Enter x coordinate");
        coordinates[1] = Integer.parseInt(scanner.next());
        System.out.println("Enter y coordinate");
        coordinates[0] = Integer.parseInt(scanner.next());

        return coordinates;
    }

    public void alertInvalidMove() {
        System.out.println("Invalid move");
    }

    @Override
    public void displayGreeting(String greeting) {

    }
}

