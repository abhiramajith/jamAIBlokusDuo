package model;
/*
     Team: jamAI
     Student #: 20344393, 20364441, 20483142
 */

public class GamePiece {

    private final static int GAME_PIECE_COUNT = 21;

    public final static int ORDINAL_DIRECTIONS = 8;
    private final static int ROTATE_RIGHT_START_POINT = 6;
    private final static int ROTATE_LEFT_START_POINT = 2;

    private static final String[] pieceType = {"I1", "I2", "I3", "I4", "I5", "V3", "L4", "Z4", "O4", "L5", "T5", "V5", "N", "Z5", "T4", "P", "W", "U", "F", "X", "Y"};
    private static final boolean[] sourceIsEdge = {true, true, true, true, true, true, true, true, true, true, false, true,
            true, false, false, true, true, false, false, false, false};

    //Ordinal directions from N, clockwise (y,x) format
    public static final int[][] directions = new int[][]{{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};


    //                                         N NE  E SE  S SW  W  NW
    private static int[] i1Length = new int[]{0, 0, 0, 0, 0, 0, 0, 0};
    private static int[] i2Length = new int[]{1, 0, 0, 0, 0, 0, 0, 0};
    private static int[] i3Length = new int[]{2, 0, 0, 0, 0, 0, 0, 0};
    private static int[] i4Length = new int[]{3, 0, 0, 0, 0, 0, 0, 0};
    private static int[] i5Length = new int[]{4, 0, 0, 0, 0, 0, 0, 0};
    private static int[] v3Length = new int[]{1, 0, 1, 0, 0, 0, 0, 0};
    private static int[] l4Length = new int[]{2, 0, 1, 0, 0, 0, 0, 0};
    private static int[] z4Length = new int[]{1, 1, 0, 0, 0, 0, 1, 0};
    private static int[] o4Length = new int[]{1, 1, 1, 0, 0, 0, 0, 0};
    private static int[] l5Length = new int[]{1, 0, 3, 0, 0, 0, 0, 0};
    private static int[] t5Length = new int[]{2, 0, 1, 0, 0, 0, 1, 0};
    private static int[] v5Length = new int[]{2, 0, 2, 0, 0, 0, 0, 0};
    private static int[] nLength = new int[]{0, 0, 2, 0, 1, 1, 0, 0};
    private static int[] z5Length = new int[]{0, 1, 1, 0, 0, 1, 1, 0};
    private static int[] t4Length = new int[]{1, 0, 1, 0, 0, 0, 1, 0};
    private static int[] pLength = new int[]{0, 0, 1, 1, 2, 0, 0, 0};
    private static int[] wLength = new int[]{1, 1, 0, 0, 0, 1, 1, 0};
    private static int[] uLength = new int[]{1, 1, 0, 1, 1, 0, 0, 0};
    private static int[] fLength = new int[]{1, 1, 0, 0, 1, 0, 1, 0};
    private static int[] xLength = new int[]{1, 0, 1, 0, 1, 0, 1, 0};
    private static int[] yLength = new int[]{1, 0, 2, 0, 0, 0, 1, 0};


    private static int[][] pieceShapes = {i1Length, i2Length, i3Length, i4Length, i5Length, v3Length, l4Length, z4Length, o4Length,
            l5Length, t5Length, v5Length, nLength, z5Length, t4Length, pLength, wLength, uLength, fLength, xLength, yLength};


    public static int[] getShape(String ID) {
        for (int i = 0; i < GAME_PIECE_COUNT; i++) {
            if (ID.equalsIgnoreCase(pieceType[i])) {
                return pieceShapes[i];
            }
        }
        throw new IllegalArgumentException(ID + "is not a valid game piece ID");
    }



    public static int getSize(String ID){
        int[] pieceSize = {1,2,3,4,5,3,4,4,4,5,5,5,5,5,4,5,5,5,5,5,5};
        int count = 0;

        for(int i=0;i<GAME_PIECE_COUNT;i++){
            if(ID.equalsIgnoreCase(pieceType[i])){
                count = i;
            }
        }
        return pieceSize[count];
    }

    public static boolean getSourceIsEnd(String ID) {
        for (int i = 0; i < GAME_PIECE_COUNT; i++) {
            if (ID.equalsIgnoreCase(pieceType[i])) {
                return sourceIsEdge[i];
            }
        }
        throw new IllegalArgumentException(ID + "is not a valid game piece ID");
    }

    public static String[] getPieceType() {
        return pieceType;
    }

    public static int[][] getDirections() {
        return directions;
    }

    public static int getOrdinalDirections() {
        return ORDINAL_DIRECTIONS;
    }

    public static void placePiece(int[] piece, int xCoord, int yCoord, char[][] board, char symbol) {

        board[yCoord][xCoord] = symbol;

        for (int i = 0; i < ORDINAL_DIRECTIONS; i++) {
            //incremented integer that is added to the given origin point
            int xModifier = 0;
            int yModifier = 0;
            for (int j = 0; j < piece[i]; j++) {
                xModifier += directions[i][1];
                yModifier += directions[i][0];
                board[yCoord + yModifier][xCoord + xModifier] = symbol;
            }
        }
    }

    public static void rotatePiece(int[] piece, char direction) {

        //Rotated piece will be copied into new array
        int[] pieceCopy = piece.clone();

        //j is used to iterate through piece[] for in place rotating
        int j = 0;

        //Rotate right
        if (direction == 'R') {
            for (int i = 0; i < piece.length; i++) {

                // Rotate piece by starting at index 6 and circularly iterating through original array
                // Modulo 8 starts index count from 0 after index 7 is passed
                piece[j] = pieceCopy[(i + ROTATE_RIGHT_START_POINT) % ORDINAL_DIRECTIONS];

                j++;
            }
        }

        //Rotate left
        else if (direction == 'L') {
            for (int i = 0; i < piece.length; i++) {

                //To rotate left 2 is the starting index
                piece[j] = pieceCopy[(i + ROTATE_LEFT_START_POINT) % 8];
                j++;
            }
        }


    }

    public static void flip(int[] piece) {
        //temporary array that stores the index of the elements for the array to be flipped
        int[] arr_index = new int[]{1, 7, 2, 6, 3, 5};
        int temp_arr_ind_1 = 0;
        int temp_arr_ind_2 = 1;

        for (int i = 0; i < 3; i++) {
            int temp = piece[arr_index[temp_arr_ind_1]];
            piece[arr_index[temp_arr_ind_1]] = piece[arr_index[temp_arr_ind_2]];
            piece[arr_index[temp_arr_ind_2]] = temp;

            temp_arr_ind_1 += 2;
            temp_arr_ind_2 += 2;
        }

    }

}



