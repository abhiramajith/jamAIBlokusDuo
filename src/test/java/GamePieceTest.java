

import model.GamePiece;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class GamePieceTest {

    @Test
    void pieceIsRotating() {


        int[] w = GamePiece.getShape("W");
        int[] wRotatedRight1 = {1, 0, 1, 1, 0, 0, 0, 1};
        int[] wRotatedRight2 = {0, 1, 1, 0, 1, 1, 0, 0};
        int[] wRotatedRight3 = {0, 0, 0, 1, 1, 0, 1, 1};

        GamePiece.rotatePiece(w, 'R');

        assertEquals(Arrays.toString(wRotatedRight1), Arrays.toString(w));

        GamePiece.rotatePiece(w, 'R');
        assertEquals(Arrays.toString(wRotatedRight2), Arrays.toString(w));

        GamePiece.rotatePiece(w,  'R');
        assertEquals(Arrays.toString(wRotatedRight3), Arrays.toString(w));

        GamePiece.rotatePiece(w,  'R');
        assertEquals(Arrays.toString(w), Arrays.toString(w));




    }

    @Test
    void flipTest(){
        int[] prac = new int[]{1,2,3,4,5,6,7,8};
        GamePiece.flip(prac);
        assertEquals(Arrays.toString(prac), "[1, 8, 7, 6, 5, 4, 3, 2]");
    }

}