import model.GameBoard;
import model.GamePiece;
import model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameBoardTest {

    private final static int BOARD_SIZE = 14;
    private final static char PLAYER_ONE_SYMBOL = '\u25A1';
    private final static char PLAYER_TWO_SYMBOL = '\u25A0';

    @Test
    void cornerIsTouching() {
        GameBoard gameBoard = new GameBoard();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                gameBoard.getBoard()[i][j] = '.';
            }
        }

        gameBoard.setBoard(2,2, PLAYER_ONE_SYMBOL);

//        for (int i = 0; i < BOARD_SIZE; i++) {
//            for (int j = 0; j < BOARD_SIZE; j++) {
//                System.out.print(gameBoard.getBoard()[i][j] + " ");
//            }
//            System.out.println();
//        }

        assertTrue(gameBoard.cornerIsTouching(PLAYER_ONE_SYMBOL,gameBoard.getBoard(),3,3));
        assertTrue(gameBoard.cornerIsTouching(PLAYER_ONE_SYMBOL,gameBoard.getBoard(),1,1));
        assertTrue(gameBoard.cornerIsTouching(PLAYER_ONE_SYMBOL,gameBoard.getBoard(),1,3));
        assertTrue(gameBoard.cornerIsTouching(PLAYER_ONE_SYMBOL,gameBoard.getBoard(),3,1));
        assertFalse(gameBoard.cornerIsTouching(PLAYER_ONE_SYMBOL,gameBoard.getBoard(),0,0));
    }

    @Test
    void edgeIsTouching() {
        GameBoard gameBoard = new GameBoard();

        gameBoard.setBoard(4,9, PLAYER_TWO_SYMBOL);
        gameBoard.setBoard(9,4, PLAYER_ONE_SYMBOL);

        for (int i = BOARD_SIZE-1; i >= 0; i--) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(gameBoard.getBoard()[i][j] + " ");
            }
            System.out.println();
        }

        assertTrue(gameBoard.edgeIsTouching(PLAYER_ONE_SYMBOL,gameBoard.getBoard(),4,8));
        assertFalse(gameBoard.edgeIsTouching(PLAYER_ONE_SYMBOL,gameBoard.getBoard(),1,1));
        assertFalse(gameBoard.edgeIsTouching(PLAYER_TWO_SYMBOL,gameBoard.getBoard(),4,8));
    }

    @Test
    void positionIsInBounds() {
        GameBoard gameBoard = new GameBoard();

        //In bounds, expect True
        assertTrue(gameBoard.positionInBounds(gameBoard.getBoard(), 1,1));
        assertTrue(gameBoard.positionInBounds(gameBoard.getBoard(), 5, 5));

        //Out of bounds, expect False
        assertFalse(gameBoard.positionInBounds(gameBoard.getBoard(), -1,1));
        assertFalse(gameBoard.positionInBounds(gameBoard.getBoard(), 19,0));
    }

    @Test
    void isOverlapping() {
        GameBoard gameBoard = new GameBoard();

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                gameBoard.getBoard()[i][j] = '.';
            }
        }

        gameBoard.getBoard()[2][2] = '*';

        assertFalse(gameBoard.isOverlapping(gameBoard.getBoard(), 3,3));
        assertTrue(gameBoard.isOverlapping(gameBoard.getBoard(), 2,2));

        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if(i != 2 || j != 2) {
                    assertFalse(gameBoard.isOverlapping(gameBoard.getBoard(), j, i));
                }
            }
        }
    }

    @Test
    void isValidMove() {
        GameBoard gameBoard = new GameBoard();

        gameBoard.setBoard(9,4,PLAYER_ONE_SYMBOL);
        gameBoard.setBoard(4,9,PLAYER_TWO_SYMBOL);


        assertTrue(gameBoard.isValidMove("L5", GamePiece.getShape("L5"), PLAYER_ONE_SYMBOL, gameBoard.getBoard(), 5,7));
        GamePiece.placePiece(GamePiece.getShape("L5"),5,7,gameBoard.getBoard(), PLAYER_ONE_SYMBOL);
        assertTrue(gameBoard.isValidMove("V3", GamePiece.getShape("V3"), PLAYER_ONE_SYMBOL,gameBoard.getBoard(),4,5));
        GamePiece.placePiece(GamePiece.getShape("V3"),4,5,gameBoard.getBoard(), PLAYER_ONE_SYMBOL);
        assertTrue(gameBoard.isValidMove("X", GamePiece.getShape("X"), PLAYER_ONE_SYMBOL,gameBoard.getBoard(),7,4));
        GamePiece.placePiece(GamePiece.getShape("X"),7,4,gameBoard.getBoard(), PLAYER_ONE_SYMBOL);
        assertTrue(gameBoard.isValidMove("I1", GamePiece.getShape("I1"), PLAYER_ONE_SYMBOL,gameBoard.getBoard(),9,5));
        GamePiece.placePiece(GamePiece.getShape("I1"),9,5,gameBoard.getBoard(), PLAYER_ONE_SYMBOL);
        assertTrue(gameBoard.isValidMove("I2", GamePiece.getShape("I2"), PLAYER_ONE_SYMBOL,gameBoard.getBoard(), 3, 7));
        GamePiece.placePiece(GamePiece.getShape("I2"),3,7,gameBoard.getBoard(),PLAYER_ONE_SYMBOL);
        assertTrue(gameBoard.isValidMove("I2", GamePiece.getShape("I2"), PLAYER_TWO_SYMBOL,gameBoard.getBoard(), 8, 5));
        GamePiece.placePiece(GamePiece.getShape("I2"),8,5,gameBoard.getBoard(),PLAYER_TWO_SYMBOL);
        assertTrue(gameBoard.isValidMove("I5", GamePiece.getShape("I5"), PLAYER_TWO_SYMBOL, gameBoard.getBoard(), 10,5));
        GamePiece.placePiece(GamePiece.getShape("I5"), 10,5, gameBoard.getBoard(), PLAYER_TWO_SYMBOL);
        assertTrue(gameBoard.isValidMove("X", GamePiece.getShape("X"), PLAYER_TWO_SYMBOL, gameBoard.getBoard(), 8,2));
        GamePiece.placePiece(GamePiece.getShape("X"), 8,2, gameBoard.getBoard(), PLAYER_TWO_SYMBOL);
        assertTrue(gameBoard.isValidMove("V3", GamePiece.getShape("V3"), PLAYER_ONE_SYMBOL, gameBoard.getBoard(), 10,3));
        GamePiece.placePiece(GamePiece.getShape("V3"), 10,3, gameBoard.getBoard(), PLAYER_ONE_SYMBOL);

        assertFalse(gameBoard.isValidMove("X",GamePiece.getShape("X"), PLAYER_ONE_SYMBOL, gameBoard.getBoard(), 9,7));
        assertFalse(gameBoard.isValidMove("X",GamePiece.getShape("X"), PLAYER_ONE_SYMBOL, gameBoard.getBoard(), 20,4));
        assertFalse(gameBoard.isValidMove("I4",GamePiece.getShape("X"), PLAYER_ONE_SYMBOL, gameBoard.getBoard(), 10,5));
        assertFalse(gameBoard.isValidMove("I1",GamePiece.getShape("I1"), PLAYER_ONE_SYMBOL, gameBoard.getBoard(), 9,6));



        for (int i = BOARD_SIZE - 1; i >= 0; i--) {
            System.out.print(i + " ");
            if (i<10) {
                System.out.print(" ");
            }
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(gameBoard.getBoard()[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("   0 1 2 3 4 5 6 7 8 9 10111213");
        System.out.println();

    }

    @Test
    void isValidStartingMove() {
        GameBoard gameBoard = new GameBoard();

        gameBoard.setBoard(9,4,PLAYER_ONE_SYMBOL);
        gameBoard.setBoard(4,9,PLAYER_TWO_SYMBOL);

        for (int i = BOARD_SIZE - 1; i >= 0; i--) {
            System.out.print(i + " ");
            if (i<10) {
                System.out.print(" ");
            }
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(gameBoard.getBoard()[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("   0 1 2 3 4 5 6 7 8 9 10111213");
        System.out.println();

        assertTrue(gameBoard.isValidStartMove(GamePiece.getShape("I5"),PLAYER_ONE_SYMBOL, gameBoard.getBoard(), 4,5));
        GamePiece.placePiece(GamePiece.getShape("I5"),4,5, gameBoard.getBoard(),PLAYER_ONE_SYMBOL);
        assertFalse(gameBoard.isValidStartMove(GamePiece.getShape("I5"),PLAYER_ONE_SYMBOL, gameBoard.getBoard(), 3,5));

        for (int i = BOARD_SIZE - 1; i >= 0; i--) {
            System.out.print(i + " ");
            if (i<10) {
                System.out.print(" ");
            }
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(gameBoard.getBoard()[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println("   0 1 2 3 4 5 6 7 8 9 10111213");
        System.out.println();
    }

    @Test
    void validMoveLeft() {
        GameBoard gameBoard = new GameBoard();
        char [][] goodBoard = new char[5][5];
        char [][] badBoard = new char[5][5];
        goodBoard[1][1] = '\u25A0';
        Player player = new Player('\u25A0');
        assertTrue(gameBoard.validMoveLeft(goodBoard,player,false));
        assertFalse(gameBoard.validMoveLeft(badBoard, player, false));
    }
}