package ui.graphical;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import model.GameBoard;

public class GraphicalGameboard {

    float boardX;
    float boardY;
    float boardWidth;
    float boardHeight;
    float cellWidth;
    float cellHeight;
    TextureRegion p1Square;
    TextureRegion p2Square;
    GameBoard board;

    public GraphicalGameboard(float boardX, float boardY, float boardWidth, float boardHeight,
                              TextureRegion p1Square, TextureRegion p2Square, GameBoard board) {
        this.boardX = boardX;
        this.boardY = boardY;
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        this.p1Square = p1Square;
        this.p2Square = p2Square;
        this.board = board;
        cellHeight = boardHeight / GameBoard.HEIGHT;
        cellWidth = boardWidth / GameBoard.WIDTH;
    }


    //Gets the bottom left coordinates of the board object
    public float getOriginX() {
        return boardX;
    }

    public float getOriginY() {
        return boardY;
    }


    //Gets the column (0-13) on the board base on x coordinate on screen
    public int getBoardColumn(float x) {
        int result = -1;
        if ((boardX < x) && ((boardX + boardWidth) > x)) {
            result = (int) ((x - boardX) / cellWidth);
        }
        return result;
    }

    //Gets the column (0-13) on the board base on y coordinate on screen
    public int getBoardRow(float y) {
        int result = -1;
        if ((boardY < y) && ((boardY + boardHeight) > y)) {
            result = (int) ((y - boardY) / cellHeight);
        }
        return result;
    }

    //returns what square on the board is clicked
    public boolean isHit(float x, float y) {
        return (getBoardColumn(x) != -1) && (getBoardRow(y) != -1);
    }
}
