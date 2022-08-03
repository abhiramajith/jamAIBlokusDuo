package ui.graphical;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.Vector3;

public class MyEventHandler extends InputAdapter {

    TiledMapTileLayer tileLayer;
    HelloGame game;
    GraphicalGamepiece selectedPiece, lastSelectedPiece;
    GraphicalGameboard graphicalGameboard;
    String manipulationSequence;
    GUI gui;

    public MyEventHandler(HelloGame game) {
        this.game = game;
        this.selectedPiece = null;
        this.lastSelectedPiece = null;
        this.graphicalGameboard = game.playScreen.graphicalGameboard;
        this.tileLayer = game.theme.tileLayer;
        this.manipulationSequence = "";
        this.gui = game.gui;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        manipulationSequence = "";                  //reset
        boolean eventHandled = false;

        int currentPlayerTurn = gui.getPlayerTurn() - '0';

        if (button == Input.Buttons.LEFT) {
            Vector3 coord = unprojectScreenCoordinates(Gdx.input.getX(), Gdx.input.getY());

            //Looking for click on a gamepiece
            for (int i = 0; i < game.playScreen.getGamepieces()[currentPlayerTurn].size(); i++) {
                GraphicalGamepiece gamepiece = (GraphicalGamepiece) game.playScreen.getGamepieces()[currentPlayerTurn].get(i);
                if (gamepiece.isHit(coord.x, coord.y) && !gamepiece.isPlaced() &&
                        gui.getPlayerTurn() == gamepiece.getPieceName().charAt(0)) {
                    game.pieceToTop(gamepiece);
                    selectedPiece = gamepiece;

                    eventHandled = true;
                }
            }

            //Looking for click on settings and help menu
            if (game.stage.hit(coord.x, coord.y, false) != null) {

                if (game.stage.hit(coord.x, coord.y, false).equals(game.playScreen.settingsButton.getImage())) {
                    Gdx.input.setInputProcessor(game.stage);
                    game.settingsBox();
                } else if (game.stage.hit(coord.x, coord.y, false).equals(game.playScreen.helpButton.getImage())) {
                    Gdx.input.setInputProcessor(game.stage);
                    game.playScreen.helpBox();
                }
            }
        }

        return eventHandled;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        boolean eventHandled = false;
        boolean squareFound = false;
        Vector3 coord = unprojectScreenCoordinates(Gdx.input.getX(), Gdx.input.getY());

        int boardColumn = graphicalGameboard.getBoardColumn(coord.x);
        int boardRow = graphicalGameboard.getBoardRow(coord.y);

        //place piece if valid move
        if (graphicalGameboard.isHit(coord.x, coord.y) && selectedPiece != null && selectedPiece.getPieceName().charAt(0) == gui.getPlayerTurn()) {
            game.uiStream.println(selectedPiece.getPieceName().substring(1) + " " + manipulationSequence + "p " +
                    boardColumn + " " + boardRow);
            selectedPiece.setPosition(graphicalGameboard.getOriginX() + (boardColumn * graphicalGameboard.cellWidth) + 16,
                    graphicalGameboard.getOriginY() + (boardRow * graphicalGameboard.cellHeight) + 16);
            Gdx.input.setInputProcessor(null);
            squareFound = true;
            selectedPiece.setPlaced(true);
            lastSelectedPiece = selectedPiece;
            game.playScreen.firstPiecePlayed = true;
            selectedPiece = null;
            eventHandled = true;
        }

        if (!squareFound && selectedPiece != null) {                      //if piece is placed off board
            selectedPiece.setPosition(selectedPiece.getOriginalX(), selectedPiece.getOriginalY());
            selectedPiece.setGamepiece(selectedPiece.getOriginalOrientation().clone());
            selectedPiece = null;
            eventHandled = true;
        }
        return eventHandled;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        boolean eventHandled = false;
        if (null != selectedPiece) {
            Vector3 coord = unprojectScreenCoordinates(screenX, screenY);
            selectedPiece.setPosition(coord.x, coord.y);
            eventHandled = true;
        }
        return eventHandled;
    }

    Vector3 unprojectScreenCoordinates(int x, int y) {
        Vector3 screenCoordinates = new Vector3(x, y, 0);
        Vector3 worldCoordinates = game.getCamera().unproject(screenCoordinates);
        return worldCoordinates;
    }

    @Override
    public boolean keyDown(int keycode) {
        boolean eventHandled = false;
        if (null != selectedPiece) {
            switch (keycode) {
                case Input.Keys.F:
                    selectedPiece.flipAlongY();
                    manipulationSequence += "f ";
                    eventHandled = true;
                    break;
                case Input.Keys.R:
                    selectedPiece.rotateRight();
                    manipulationSequence += "r ";
                    eventHandled = true;
                    break;
            }
        }
        return eventHandled;
    }


    public void undoLast() {
        lastSelectedPiece.setPosition(lastSelectedPiece.getOriginalX(), lastSelectedPiece.getOriginalY());
        lastSelectedPiece.setGamepiece(lastSelectedPiece.getOriginalOrientation().clone());
        lastSelectedPiece.setPlaced(false);
    }

}
