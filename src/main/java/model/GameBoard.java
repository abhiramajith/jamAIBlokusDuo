package model;
/*
     Team: jamAI
     Student #: 20344393, 20364441, 20483142
 */

import java.util.ArrayList;

public class GameBoard {

    public final static int STARTING_POINT_1X = 4;
    public final static int STARTING_POINT_1Y = 9;
    public final static int STARTING_POINT_2X = 9;
    public final static int STARTING_POINT_2Y = 4;
    public final static char EMPTY_POSITION = '.';


    char[][] board = new char[14][14];
    public final static int WIDTH = 14;
    public final static int HEIGHT = 14;

    public GameBoard() {
        //initialising the board
        for (int i = 0; i < board.length; i++) {
            for (int k = 0; k < board[i].length; k++)
                //setting the starting points for the players
                if ((i == STARTING_POINT_1X && k == STARTING_POINT_1Y)
                        || (i == STARTING_POINT_2X && k == STARTING_POINT_2Y))
                    board[i][k] = '*';
                else
                    board[i][k] = EMPTY_POSITION;
        }
    }

    //modifier
    public void setBoard(int i, int j, char symbol){
        board[i][j] = symbol;
    }

    //accessor
    public char[][] getBoard(){
        return board;
    }

    //Checks if a symbol on a square is touching a square with the same symbol in the N, E, S, W directions
    public boolean cornerIsTouching(char playerSymbol, char [][] board, int xCoord, int yCoord) {

        for (int i = 1; i < GamePiece.getOrdinalDirections(); i+=2) {

            if (positionInBounds(board, xCoord + GamePiece.getDirections()[i][1] ,
                    yCoord + GamePiece.getDirections()[i][0])) {
                if (board[yCoord + GamePiece.getDirections()[i][0]][xCoord + GamePiece.getDirections()[i][1]] ==
                        playerSymbol) {
                    return true;
                }
            }
        }
        return false;
    }

    //Checks if a symbol on a square is touching a square with the same symbol in the NE, SE, SW, NW directions
    public boolean edgeIsTouching(char playerSymbol, char [][] board, int xCoord, int yCoord) {

        for (int i = 0; i < GamePiece.getOrdinalDirections(); i+=2) {

            if (positionInBounds(board,xCoord + GamePiece.getDirections()[i][1], yCoord + GamePiece.getDirections()[i][0])) {
                if (board[yCoord + GamePiece.getDirections()[i][0]][xCoord + GamePiece.getDirections()[i][1]] == playerSymbol) {
                    return true;
                }
            }
        }
        return false;
    }

    //Checks if a given x,y position is within the bounds of the board
    public boolean positionInBounds(char [][] board, int xCoord, int yCoord) {
        return  xCoord < board[0].length && xCoord >= 0 && yCoord < board.length && yCoord >= 0;
    }

    //Checks if a given x,y position is already occupied
    public boolean isOverlapping(char [][] board, int xCoord, int yCoord) {
        return !(board[yCoord][xCoord] == EMPTY_POSITION);
    }

    //Checks if a given piece can be placed on the board according to the game rules
    public boolean isValidMove(String pieceID, int [] pieceOrientation, char playerSymbol, char [][]board,
                               int xCoord, int yCoord ) {

        boolean validMove = false;

        //Initial check on the source square
        if (!positionInBounds(board, xCoord, yCoord) || isOverlapping(board,xCoord,yCoord) ||
                edgeIsTouching(playerSymbol, board, xCoord, yCoord)) {
            return false;
        }

        //Edge case for when the source square needs to be checked to fulfill the touching corner game rule
        else if (GamePiece.getSourceIsEnd(pieceID)) {
            validMove = !edgeIsTouching(playerSymbol, board, xCoord, yCoord) &&
            cornerIsTouching(playerSymbol, board, xCoord, yCoord);
        }


        for (int i = 0; i < GamePiece.getOrdinalDirections(); i++) {
            int xModifier = 0;
            int yModifier = 0;

            //Go through each piece and run checks on all the squares
            for (int j = 0; j < pieceOrientation[i]; j++) {
                xModifier += GamePiece.getDirections()[i][1];
                yModifier += GamePiece.getDirections()[i][0];

                //Check for rule violation
                if (!positionInBounds(board, xCoord + xModifier, yCoord + yModifier) ||
                        isOverlapping(board, xCoord + xModifier, yCoord + yModifier) ||
                        edgeIsTouching(playerSymbol, board, xCoord + xModifier, yCoord + yModifier)) {
                    return false;
                }

                //Edge case for "end" squares that need to fulfill the touching corners game rules
                else {
                    if (j == pieceOrientation[i] - 1 && cornerIsTouching(playerSymbol, board, xCoord + xModifier, yCoord + yModifier)) {
                        validMove = true;
                    }
                }
            }
        }

        return validMove;
    }

    //Checks if a given piece can be placed as a player's first move
    public boolean isValidStartMove(int [] pieceOrientation, char playerSymbol, char [][]board,
                               int xCoord, int yCoord ) {

        boolean validMove;

        //Initial check on the source square
        if (!positionInBounds(board, xCoord, yCoord)) {
            return false;
        } else {
            validMove = board[yCoord][xCoord] == playerSymbol;
        }

        //Check if any of the other squares overlaps the starting point = valid start move
        for (int i = 0; i < GamePiece.getOrdinalDirections(); i++) {
            int xModifier = 0;
            int yModifier = 0;
            for (int j = 0; j < pieceOrientation[i]; j++) {
                xModifier += GamePiece.getDirections()[i][1];
                yModifier += GamePiece.getDirections()[i][0];

                //Check for rule violation
                if (!positionInBounds(board, xCoord + xModifier, yCoord + yModifier)) {
                    return false;
                } else {
                    if (board[yCoord+yModifier][xCoord+xModifier] == playerSymbol) {
                        return true;
                    }
                }
            }
        }

        return validMove;
    }

    //Checks if a given player can still play a piece on the given board
    public boolean validMoveLeft (char[][] board, Player player, boolean flag){

        boolean foundValidMove = false;
        ArrayList<String> foundValidMoves = new ArrayList<>();

        //If player has no pieces left there are no valid moves
        if (player.getPlayerPieces().size() == 0){
            System.out.println("No more pieces left: " + player.getPlayerName());
            return false;
        }

        //First for loop goes through all the player's remaining pieces
        for (int i = 0; i < player.getPlayerPieces().size(); i++) {

            String pieceID = player.getPlayerPieces().get(i);   //Gets String of current piece in loop

            //These 2 for loops are for iterating through the board
            for (int j = board.length - 1; j >= 0 ; j--) {
                for (int k = 0; k < board[j].length; k++) {

                    StringBuilder currentSequence = new StringBuilder(pieceID);
                    int[] piece = GamePiece.getShape(pieceID).clone();  //Gets array of current piece

                    // For each position on the board we check each piece the player has and all of its orientations
                    //First check if every rotation is valid
                    for (int l = 0; l < 4; l++) {
                        if (board[j][k] == EMPTY_POSITION && isValidMove(pieceID, piece, player.getPlayerSymbol(), board, k, j) && player.getHasMadeFirstMove()) {
                            foundValidMove = true;
                            foundValidMoves.add(currentSequence + " p " + k + " " + j);

                        } else if (!player.getHasMadeFirstMove() && isValidStartMove(piece, player.getPlayerSymbol(), board, k, j)) {
                            foundValidMove = true;
                            foundValidMoves.add(currentSequence + " p " + k + " " + j);
                        }
                        GamePiece.rotatePiece(piece, 'R');
                        currentSequence.append(" r");
                    }

                    //Then check if every rotation when the piece is flipped is valid
                    piece = GamePiece.getShape(pieceID).clone();
                    GamePiece.flip(piece);
                    currentSequence = new StringBuilder(pieceID + " f");
                    for (int l = 0; l < 4; l++) {

                        if (board[j][k] == EMPTY_POSITION && isValidMove(pieceID, piece, player.getPlayerSymbol(), board, k, j) && player.getHasMadeFirstMove()) {
                            foundValidMove = true;
                            foundValidMoves.add(currentSequence + " p " + k + " " + j);
                        } else if (!player.getHasMadeFirstMove() && isValidStartMove(piece, player.getPlayerSymbol(), board, k, j)) {
                            foundValidMove = true;
                            foundValidMoves.add(currentSequence + " p " + k + " " + j);
                        }
                        GamePiece.rotatePiece(piece, 'R');
                        currentSequence.append(" r");

                    }
                }
            }
        }

        if (flag) { //Shows all playable moves in a turn to console
            System.out.println("Player: " + player.getPlayerName() + "(" + player.getPlayerSymbol() + ") "
                    + foundValidMoves + " " + foundValidMoves.size());
        }

        return foundValidMove;
    }
}
