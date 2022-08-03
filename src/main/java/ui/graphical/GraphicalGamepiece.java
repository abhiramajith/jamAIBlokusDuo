package ui.graphical;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import model.GamePiece;

import static model.GamePiece.ORDINAL_DIRECTIONS;
import static model.GamePiece.directions;

public class GraphicalGamepiece {
    private int[] gamepieceOrientation;
    private final int[] originalOrientation;
    private String pieceName;
    private TextureRegion square;
    private TextureRegion highlight;
    private float originX, originY;  // world X Y coordinate of the gamepiece's origin
    private boolean placed;
    private float originalX, originalY;     //original position of gamepieces' origin


    public GraphicalGamepiece(int[] gamepiece, String pieceName, TextureRegion[] square, float originX, float originY) {
        this.gamepieceOrientation = gamepiece.clone();
        this.originalOrientation = gamepiece.clone();
        this.pieceName = pieceName;
        this.square = square[0];
        this.originX = originX;
        this.originY = originY;
        this.originalX = originX + 16;
        this.originalY = originY + 16;
        this.placed = false;
        this.highlight = square[1];
    }

    public void draw(SpriteBatch batch) {
        batch.draw(square,
                originX,
                originY);
        for (int i = 0; i < ORDINAL_DIRECTIONS; i++) {
            //incremented integer that is added to the given origin point
            int xModifier = 0;
            int yModifier = 0;
            for (int j = 0; j < gamepieceOrientation[i]; j++) {
                xModifier += directions[i][1];
                yModifier += directions[i][0];
                batch.draw(square,
                        originX + square.getRegionWidth() * xModifier,
                        originY + square.getRegionHeight() * yModifier);
            }
        }
    }

    public void drawHighlight(SpriteBatch batch) {
        batch.draw(highlight,
                originX - 2,
                originY - 2);
        for (int i = 0; i < ORDINAL_DIRECTIONS; i++) {
            //incremented integer that is added to the given origin point
            int xModifier = 0;
            int yModifier = 0;
            for (int j = 0; j < gamepieceOrientation[i]; j++) {
                xModifier += directions[i][1];
                yModifier += directions[i][0];
                batch.draw(highlight,
                        originX + square.getRegionWidth() * xModifier - 2,
                        originY + square.getRegionHeight() * yModifier - 2);
            }
        }
    }

    public void flipAlongY() {
        GamePiece.flip(gamepieceOrientation);
    }

    public void rotateRight() {
        GamePiece.rotatePiece(gamepieceOrientation, 'R');
    }

    //returns whether the given coordinate is within the piece
    public boolean isHit(float x, float y) {
        boolean isHit = false;
        if (new Rectangle(originX, originY, square.getRegionWidth(), square.getRegionHeight()).contains(x, y)) {
            isHit = true;
        }
        for (int i = 0; i < ORDINAL_DIRECTIONS; i++) {
            //incremented integer that is added to the given origin point
            int xModifier = 0;
            int yModifier = 0;
            for (int j = 0; j < gamepieceOrientation[i]; j++) {
                xModifier += directions[i][1];
                yModifier += directions[i][0];
                Rectangle rectangle = new Rectangle(originX + xModifier * square.getRegionWidth(),
                        originY + yModifier * square.getRegionHeight(),
                        square.getRegionWidth(),
                        square.getRegionHeight());
                if (rectangle.contains(x, y)) {
                    isHit = true;
                }
            }
        }
        return isHit;
    }

    public void setPosition(float x, float y) {
        // set new position of the gamepiece, so that the pointer coordinates
        // are in the middle of the "origin" square ( Location(0,0) ).
        originX = x - square.getRegionWidth() * 0.5f;
        originY = y - square.getRegionHeight() * 0.5f;
    }

    public void setGamepiece(int[] gamepiece) {
        this.gamepieceOrientation = gamepiece;
    }

    public String getPieceName() {
        return pieceName;
    }

    public boolean isPlaced() {
        return placed;
    }

    public void setPlaced(boolean placed) {
        this.placed = placed;
    }

    public int[] getOriginalOrientation() {
        return originalOrientation;
    }

    public float getOriginalX() {
        return originalX;
    }

    public float getOriginalY() {
        return originalY;
    }
}
