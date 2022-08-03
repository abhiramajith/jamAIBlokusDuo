package ui.graphical;

/*
     Team: jamAI
     Student #: 20344393, 20364441, 20483142
 */

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.FitViewport;
import ui.UI;

import java.io.PrintStream;

public class HelloGame extends Game {

    public static final int WIDTH = 1280;
    public static final int HEIGHT = 768;

    Thread gameControlThread;
    PrintStream uiStream;
    GraphicalGamepiece graphicalGamepiece;
    GraphicalGameboard graphicalGameboard;

    OrthographicCamera camera;
    Stage stage;
    Theme theme;


    Sound buttonSound, pieceSound, invalidMoveSound, winSound;

    String bgmPath1 = "Music/Guitar-Gentle.mp3";
    String bgmPath2 = "Music/zenzen yoyuu.mp3";
    String bgmPath3 = "Music/The-way.mp3";

    public static float musicVol = 0.3f, soundVol = 0.5f, soundVol2 = 0.5f;
    private final float minValue = 0;
    private final float maxValue = 1f;
    private final boolean isVertical = false;
    private final float musicSliderWidth = 5f, soundSliderWidth = 5f;
    private float musicSliderPercentage = 0.3f, soundSliderPercentage = 0.5f;
    long buttonSoundID, pieceSoundID, invalidMoveID, winSoundID;
    boolean isMusicMuted = false, isSoundMuted;

    StartScreen startScreen;
    PlayScreen playScreen;
    SplashScreen splashScreen;
    MenuScreen menuScreen;

    SpriteBatch batch;

    GUI gui;

    MyEventHandler eventHandler;

    public boolean gameEnded = false;
    boolean isPlayScreen = false;

    public HelloGame(Thread gameControlThread, UI ui) {
        gui = (GUI) ui;
        this.gameControlThread = gameControlThread;

        // establishing communication between GameControl thread and HelloGame (libGDX thread)
        // control -> game
        gui.setGame(this); // for posting runnables into libGDX game loop
        // game -> control
        this.uiStream = new PrintStream(gui.getPipedOutputStream());  // for sending text to control
    }

    public void create() {

        camera = new OrthographicCamera();
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.update();
        stage = new Stage(new FitViewport(WIDTH, HEIGHT, camera));

        theme = new Theme("Skins/skin1.json", "Tilesets/play1.tmx", bgmPath1, "Title1", "sansD", "Images/bg.png");

        Gdx.graphics.setTitle("Blokus Duo");

        buttonSound = Gdx.audio.newSound(Gdx.files.internal("Music/buttonSound.wav"));
        pieceSound = Gdx.audio.newSound(Gdx.files.internal("Music/piecePlace.wav"));
        invalidMoveSound = Gdx.audio.newSound(Gdx.files.internal("Music/invalidPiece.wav"));
        winSound = Gdx.audio.newSound(Gdx.files.internal("Music/winNoise.wav"));

        batch = new SpriteBatch();

        startScreen = new StartScreen(this, theme);
        playScreen = new PlayScreen(this, theme);
        splashScreen = new SplashScreen(this);
        menuScreen = new MenuScreen(this);
        activateSplashScreen();

    }

    public void activateStartScreen() {
        Gdx.input.setInputProcessor(stage);
        setScreen(startScreen);
    }

    public void activatePlayScreen() {
        isPlayScreen = true;
        eventHandler = new MyEventHandler(this);
        setScreen(playScreen);
    }

    public void activateSplashScreen() {
        setScreen(splashScreen);
    }

    public void activateMenuScreen() {
        setScreen(menuScreen);
    }

    public void postRunnable(Runnable r) {
        Gdx.app.postRunnable(r);
    }

    public void playSound(boolean isMuted) {

        if (!isMuted) {
            buttonSoundID = buttonSound.play(soundVol2);
            buttonSound.setLooping(buttonSoundID, false);
        } else {
            return;
        }
    }

    public void playPieceDownSound(boolean isMuted) {

        if (!isMuted) {
            pieceSoundID = pieceSound.play(soundVol2);
            pieceSound.setLooping(pieceSoundID, false);
        } else {
            return;
        }
    }

    public void playInvalidMoveSound(boolean isMuted) {

        if (!isMuted) {
            invalidMoveID = invalidMoveSound.play(soundVol2);
            invalidMoveSound.setLooping(invalidMoveID, false);
        } else {
            return;
        }
    }

    public void playWinSound(boolean isMuted) {

        if (!isMuted) {
            winSoundID = winSound.play(soundVol2);
            winSound.setLooping(winSoundID, false);
        } else {
            return;
        }
    }

    void showDialog(String text) {
        Gdx.input.setInputProcessor(stage);
        Dialog dialog = new Dialog("Attention", theme.skin, "default") {
            public void result(Object obj) {
                playSound(isSoundMuted);
                if (gameEnded){
                    Gdx.input.setInputProcessor(stage);
                }else {
                    Gdx.input.setInputProcessor(eventHandler);
                }

            }
        };
        Label label = new Label(text, theme.skin, theme.normalFont, Color.WHITE);
        dialog.text(label);
        dialog.button("OK", 1);

        dialog.getContentTable().pad(10);
        dialog.getButtonTable().pad(10);
        dialog.show(stage);
    }

    @Override
    public void dispose() {
        super.dispose();
        if (startScreen != null) startScreen.dispose();
        if (playScreen != null) playScreen.dispose();
        if (splashScreen != null) splashScreen.dispose();
        if (menuScreen != null) menuScreen.dispose();
        if (stage != null) stage.dispose();
        if (batch != null) batch.dispose();
        if (theme != null) theme.dispose();
        System.exit(0);
    }

    public void setGreeting(String greeting) {
        showDialog(greeting);
    }

    public Camera getCamera() {
        return camera;
    }


    void undoLast() {
        eventHandler.undoLast();
    }


    @SuppressWarnings("unchecked")
    void pieceToTop(GraphicalGamepiece piece) {
        int currentPlayerTurn = gui.getPlayerTurn() - '0';
        playScreen.gamepieces[currentPlayerTurn].remove(piece);
        playScreen.gamepieces[currentPlayerTurn].add(piece);
    }

    void showMessage(String text) {
        playScreen.showMessage(text);
    }


    public void settingsBox() { //settings menu for music adjustment
        Slider volSlider, soundSlider;
        float stepSize = 0.1f;
        Dialog settingsBox = new Dialog("Settings", theme.skin, "default") {
            public void result(Object obj) {
                playSound(isSoundMuted);
                if (isPlayScreen) {
                    Gdx.input.setInputProcessor(eventHandler);
                }
            }
        };

        volSlider = new Slider(minValue, maxValue, stepSize, isVertical, theme.skin);
        Label muteMusicLabel = new Label("Music", theme.skin, theme.normalFont, Color.WHITE);
        settingsBox.getContentTable().add(muteMusicLabel);
        CheckBox muteMusicButton = new CheckBox("", theme.skin);
        settingsBox.getContentTable().add(muteMusicButton);
        muteMusicButton.setChecked(isMusicMuted);
        muteMusicButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                musicVol = 0.3f - musicVol;
                theme.music.setVolume(musicVol);
                isMusicMuted = !isMusicMuted;

                if (musicVol == 0) {
                    volSlider.setValue(0);
                } else {
                    volSlider.setValue(0.3f);
                }

            }
        });

        settingsBox.getContentTable().row();

        soundSlider = new Slider(minValue, maxValue, stepSize, isVertical, theme.skin);

        Label muteSFXLabel = new Label("SFX", theme.skin, theme.normalFont, Color.WHITE);
        settingsBox.getContentTable().add(muteSFXLabel);
        CheckBox muteSFXButton = new CheckBox("", theme.skin);
        settingsBox.getContentTable().add(muteSFXButton);
        muteSFXButton.setChecked(isSoundMuted);
        muteSFXButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                soundVol = 0.5f - soundVol;
                soundVol2 = soundVol;
                isSoundMuted = !isSoundMuted;

                if (soundVol == 0) {
                    soundSlider.setValue(0);
                } else {
                    soundSlider.setValue(0.5f);
                }
            }
        });

        settingsBox.getContentTable().row();

        Label volLabel = new Label("Music", theme.skin, theme.normalFont, Color.WHITE);

        volSlider.setWidth(musicSliderWidth);
        volSlider.setVisualPercent(musicSliderPercentage);
        settingsBox.getContentTable().add(volLabel);
        settingsBox.getContentTable().add(volSlider);
        volSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                musicSliderPercentage = volSlider.getVisualPercent();
                theme.music.setVolume(musicSliderPercentage);

                if (musicSliderPercentage == 0) {
                    muteMusicButton.setChecked(true);
                } else if (musicSliderPercentage > 0) {
                    muteMusicButton.setChecked(false);
                }

            }
        });

        settingsBox.getContentTable().row();

        Label sfxLabel = new Label("SFX", theme.skin, theme.normalFont, Color.WHITE);


        soundSlider.setWidth(soundSliderWidth);
        soundSlider.setVisualPercent(soundSliderPercentage);
        settingsBox.getContentTable().add(sfxLabel);
        settingsBox.getContentTable().add(soundSlider);
        soundSlider.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                soundSliderPercentage = soundSlider.getVisualPercent();

                if (soundSliderPercentage == 0) {
                    muteSFXButton.setChecked(true);
                } else if (soundSliderPercentage > 0) {
                    muteSFXButton.setChecked(false);
                }

            }
        });
        soundSlider.addListener(new ClickListener() {
            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
                soundVol2 = soundSliderPercentage;
            }

            ;
        });


        settingsBox.button("OK", 1);

        settingsBox.getContentTable().pad(10);
        settingsBox.getButtonTable().pad(10);
        settingsBox.show(stage);
    }
}
