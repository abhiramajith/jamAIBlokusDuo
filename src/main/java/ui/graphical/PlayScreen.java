package ui.graphical;

/*
     Team: jamAI
     Student #: 20344393, 20364441, 20483142
 */

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import model.GameBoard;
import model.GamePiece;

import java.util.ArrayList;

public class PlayScreen extends ScreenAdapter {

    private static final int PLAYER_PIECES = 21;
    private static final float FADE_IN_PIECES_DURATION = 1.2f;
    private static final float P1_FADE_START = 1f;
    private static final float P1_FADE_END = P1_FADE_START + FADE_IN_PIECES_DURATION;
    private static final float P2_FADE_START = P1_FADE_END;
    private static final float P2_FADE_END = P2_FADE_START + FADE_IN_PIECES_DURATION;
    private static final float GLOW_START_TIME = P2_FADE_END;
    private static final float GLOW_DURATION = 1.2f;

    HelloGame helloGame;
    Stage stage;
    Skin skin;
    Theme theme;

    Table table, table2;
    SpriteBatch batch, p1Batch, p2Batch, highlightBatch;
    ImageButton settingsButton, helpButton;
    Texture background;
    Sprite backgroundSprite;

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    OrthographicCamera camera;
    TiledMapRenderer mapRenderer;
    GraphicalGamepiece graphicalGamepiece;

    ArrayList<GraphicalGamepiece> player1Pieces;
    ArrayList<GraphicalGamepiece> player2Pieces;
    ArrayList[] gamepieces;
    GraphicalGameboard graphicalGameboard;

    String currentPlayer;
    BitmapFont currentPlayerTextFont;
    float currentPlayerTextX;
    float currentPlayerTextY;
    float currentPlayerTextWidth;

    long startTime;
    boolean firstPiecePlayed = false;
    int elapsedTime;
    float timerX;
    float timerY;
    float timerWidth;
    BitmapFont timerFont;
    String timer = "00:00";


    MapObjects mapObjects;
    TextureRegion p1Square, p2Square;
    TextureRegion[] playerSquare;

    float highlightAlpha = 0;
    static int currentPlayerTurn = 0;
    boolean increasing = true;
    float startDelta = 0;

    public PlayScreen(HelloGame helloGame, Theme theme) {
        this.helloGame = helloGame;
        this.stage = helloGame.stage;
        this.skin = helloGame.theme.skin;
        this.background = helloGame.theme.background;
        this.tiledMap = helloGame.theme.tiledMap;
        this.tiledMapRenderer = helloGame.theme.tiledMapRenderer;
        this.camera = helloGame.camera;
        this.graphicalGamepiece = helloGame.graphicalGamepiece;
        this.graphicalGameboard = helloGame.graphicalGameboard;
        this.theme = theme;

        Texture background = new Texture(theme.bgImage);
        backgroundSprite = new Sprite(background);
        batch = new SpriteBatch();
        p1Batch = new SpriteBatch();
        p2Batch = new SpriteBatch();
        //hide game pieces at start
        p1Batch.setColor(1.0f, 1.0f, 1.0f, 0f);
        p2Batch.setColor(1.0f, 1.0f, 1.0f, 0f);
        highlightBatch = new SpriteBatch();

        //table for title
        table = new Table();
        table.setFillParent(true);
        table.align(Align.top);
        String titlep1 = "{WAIT} {SLIDE}{EASE}BLOKUS";
        String titlep2 = "{WAIT} {FADE}{EASE=1;1;1} {WAIT=1}DUO";
        TypingLabel typingLabel = new TypingLabel(titlep1, skin, theme.titleFont, Color.WHITE);
        TypingLabel typingLabel2 = new TypingLabel(titlep2, skin, theme.titleFont, Color.WHITE);
        table.add(typingLabel).align(Align.center);
        table.getCell(typingLabel).spaceBottom(20).colspan(3);
        table.row();
        table.add(typingLabel2).align(Align.center).colspan(3);

        //retrieving objects from tiledmap
        MapLayer layer = helloGame.theme.tiledMap.getLayers().get("Object Layer");
        MapObjects objects = layer.getObjects();
        MapObject currentPlayerText = objects.get("Current Player");
        currentPlayerTextX = (float) currentPlayerText.getProperties().get("x");
        currentPlayerTextY = (float) currentPlayerText.getProperties().get("y");
        currentPlayerTextWidth = (float) currentPlayerText.getProperties().get("width");
        currentPlayerTextFont = skin.getFont(theme.normalFont);
        currentPlayer = "";

        MapObject timer = objects.get("Timer");
        timerX = (float) timer.getProperties().get("x");
        timerY = (float) timer.getProperties().get("y");
        timerWidth = (float) timer.getProperties().get("width");
        timerFont = skin.getFont(theme.normalFont);
        elapsedTime = 0;

        //table for help and settings
        table2 = new Table();
        table2.setFillParent(true);
        settingsButton = new ImageButton(skin.getDrawable("cog"));
        helpButton = new ImageButton(skin.getDrawable("help"));
        table2.add(settingsButton);
        table2.add(helpButton);
        table2.align(Align.bottom);

        mapObjects = theme.objectLayer.getObjects();

        MapObject boardLocation = mapObjects.get("Board");
        float boardX = (float) boardLocation.getProperties().get("x");
        float boardY = (float) boardLocation.getProperties().get("y");
        float boardHeight = (float) boardLocation.getProperties().get("height");
        float boardWidth = (float) boardLocation.getProperties().get("width");

        mapRenderer = new OrthogonalTiledMapRenderer(theme.tiledMap);

        p1Square = theme.skin.getRegion("p1Square");
        p2Square = theme.skin.getRegion("p2Square");
        playerSquare = new TextureRegion[2];
        playerSquare[1] = theme.skin.getRegion("highlight");

        player1Pieces = new ArrayList<>();
        player2Pieces = new ArrayList<>();

        String pieceName;

        for (int i = 0; i < PLAYER_PIECES * 2; i++) {
            if (i < PLAYER_PIECES) {
                pieceName = "0";
            } else {
                pieceName = "1";
            }

            pieceName += GamePiece.getPieceType()[i % PLAYER_PIECES];
            MapObject mapObject = mapObjects.get(pieceName);
            float gamepieceX = (float) mapObject.getProperties().get("x");
            float gamepieceY = (float) mapObject.getProperties().get("y");
            if (pieceName.charAt(0) == '0') {
                playerSquare[0] = p1Square;
            } else {
                playerSquare[0] = p2Square;
            }
            graphicalGamepiece = new GraphicalGamepiece(GamePiece.getShape(GamePiece.getPieceType()[i % 21]).clone(),
                    pieceName, playerSquare, gamepieceX, gamepieceY);

            if (i < PLAYER_PIECES) {
                player1Pieces.add(graphicalGamepiece);
            } else {
                player2Pieces.add(graphicalGamepiece);
            }
        }

        gamepieces = new ArrayList[]{player1Pieces, player2Pieces};

        graphicalGameboard = new GraphicalGameboard(boardX, boardY, boardWidth, boardHeight, p1Square, p2Square, new GameBoard());

    }

    public void show() {
        super.show();
        stage.addActor(table);
        stage.addActor(table2);
    }

    public void hide() {
        super.hide();
        stage.clear();
    }
    public void render(float delta) {
        super.render(delta);
        ScreenUtils.clear(Color.PURPLE);

        startDelta += delta;

        //reset highlight alpha if player change detected
        if (currentPlayerTurn != (helloGame.gui.getPlayerTurn() - '0')) {
            highlightAlpha = 0;
            increasing = true;
            currentPlayerTurn = helloGame.gui.getPlayerTurn() - '0';
        }

        //get start time once a piece has been placed
        if (!firstPiecePlayed) {
            startTime = System.currentTimeMillis();
        }

        //only update timer if a second has passed
        if (elapsedTime != (System.currentTimeMillis() - startTime) / 1000 && !helloGame.gameEnded) {
            elapsedTime = (int) ((System.currentTimeMillis() - startTime) / 1000);
            timer = String.format("%02d", elapsedTime / 60) + " " + String.format("%02d", elapsedTime % 60);
        }

        ScreenUtils.clear(1.0f, 1.0f, 1.0f, 1.0f);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();

        batch.begin();
        batch.setColor(backgroundSprite.getColor());
        backgroundSprite.draw(batch);
        batch.end();

        // first let's draw the tiled Map
        mapRenderer.setView(camera);
        mapRenderer.render();

        batch.begin();
        // Next we draw the gamepiece and the banner
        currentPlayerTextFont.draw(batch, currentPlayer, currentPlayerTextX, currentPlayerTextY, currentPlayerTextWidth,
                Align.center, true);
        timerFont.draw(batch, timer, timerX, timerY, timerWidth,
                Align.center, true);
        batch.end();

        int playerTurn = helloGame.gui.getPlayerTurn() - '0';

        highlightBatch.enableBlending();

        highlightBatch.begin();
        for (int i = 0; i < PLAYER_PIECES; i++) {
            GraphicalGamepiece piece = (GraphicalGamepiece) gamepieces[playerTurn].get(i);
            if (!piece.isPlaced()) {
                highlightBatch.setColor(1.0f, 1.0f, 1.0f, highlightAlpha);
                if (startDelta > GLOW_START_TIME) {
                    piece.drawHighlight(highlightBatch);
                }
            }

        }
        highlightBatch.end();

        //start changing highlight alpha after all game pieces are faded in
        if (startDelta > GLOW_START_TIME) {
            if (highlightAlpha >= 1) {
                increasing = false;
            }

            if (highlightAlpha <= 0) {
                increasing = true;
            }

            if (increasing) {
                highlightAlpha += delta*(1/GLOW_DURATION);
            } else {
                highlightAlpha -= delta*(1/GLOW_DURATION);
            }
        }



        //change alpha of pieces based on frames rendered
        if (startDelta > P1_FADE_START && startDelta < P1_FADE_END) {
            p1Batch.setColor(1.0f, 1.0f, 1.0f, ((startDelta-P1_FADE_START)/FADE_IN_PIECES_DURATION));
        } else if (startDelta > P2_FADE_START && startDelta < P2_FADE_END) {
            p2Batch.setColor(1.0f, 1.0f, 1.0f, ((startDelta-P2_FADE_START)/FADE_IN_PIECES_DURATION));
        } else if (startDelta > GLOW_START_TIME) {
            p1Batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            p2Batch.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        }


        p1Batch.begin();
        for (int j = 0; j < gamepieces[0].size(); j++) {
            ((GraphicalGamepiece) gamepieces[0].get(j)).draw(p1Batch);
        }
        p1Batch.end();

        p2Batch.begin();
        for (int j = 0; j < gamepieces[1].size(); j++) {
            ((GraphicalGamepiece) gamepieces[1].get(j)).draw(p2Batch);
        }
        p2Batch.end();

        //draw selected piece with solid highlight
        batch.begin();
        if (helloGame.eventHandler.selectedPiece != null) {
            helloGame.eventHandler.selectedPiece.drawHighlight(batch);
            helloGame.eventHandler.selectedPiece.draw(batch);
        }
        batch.end();

        stage.act(delta);
        stage.draw();

    }

    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        super.dispose();
        batch.dispose();
        System.exit(0);
    }

    public void showMessage(String text) {
        currentPlayer = text;
    }

    public void helpBox() { //help menu in playscreen

        Dialog helpBox = new Dialog("How to Play", theme.skin, "default") {
            public void result(Object obj) {
                helloGame.playSound(helloGame.isSoundMuted);
                if (helloGame.isPlayScreen) {
                    Gdx.input.setInputProcessor(helloGame.eventHandler);
                }
            }
        };

        helpBox.text("Instructions:\n" +
                ">    Each player has 21 pieces to be placed on the board. \n" +
                ">    Players take turns placing their chosen piece on the board.\n" +
                ">    Each piece must touch at least one other piece of the same colour but only at the corner.\n" +
                ">    No flat edges of the same colour pieces can touch.\n" +
                ">    No restrictions on the contact of two different coloured pieces.\n" +
                ">    A game piece cannot be moved again during subsequent turns once placed.\n" +
                "\n" +
                "SCORING\n" +
                ">    Each piece not played at the end of the game counts as a minus (unit square of the individual piece).\n" +
                ">    Players earn +15 points if all 21 of their pieces have been placed on the board and an additional 5 \n" +
                "     if the last piece placed on the board was the smallest piece.", new Label.LabelStyle(theme.skin.getFont(theme.normalFont), Color.WHITE));

        helpBox.button("OK", 1);

        helpBox.getContentTable().pad(10);
        helpBox.getButtonTable().pad(10);
        helpBox.show(stage);
    }

    public ArrayList[] getGamepieces() {
        return gamepieces;
    }
}
