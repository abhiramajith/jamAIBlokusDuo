package ui.graphical;

import aurelienribon.tweenengine.*;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import ui.graphical.Tween.ActorAccessor;

public class MenuScreen implements Screen {

    private HelloGame helloGame;
    private Stage stage;
    private Skin skin;
    private TextButton playButton, exitbutton;
    private TypingLabel title1Label, title2Label;
    private TweenManager tweenManager;
    private Table table, table2;

    public MenuScreen(HelloGame helloGame) {
        this.helloGame = helloGame;
        this.stage = helloGame.stage;
        this.skin = helloGame.theme.skin;

    }

    @Override
    public void show() {

        table = new Table();
        table2 = new Table();

        table.setBackground(skin.getDrawable("bg-tile-ten-2"));
        table.setFillParent(true);

        table2.setFillParent(true);
        table2.align(Align.bottomLeft);

        //main title screen
        String title1 = "{SLIDE}BLOKUS";
        String title2 = "{EASE=1;1;1} {WAIT=1}{RAINBOW}DUO";

        title1Label = new TypingLabel(title1, skin, "Title", Color.WHITE);
        title2Label = new TypingLabel(title2, skin, "Title", Color.WHITE);

        table.add(title1Label).align(Align.center);
        table.getCell(title1Label).spaceBottom(50);

        table.row();

        table.add(title2Label).align(Align.center);
        table.getCell(title2Label).spaceBottom(50);

        ImageButton settingsButton = new ImageButton(skin.getDrawable("cog"));
        table2.add(settingsButton);
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                helloGame.settingsBox();
            }
        });

        table.row();

        playButton = new TextButton("PLAY", skin);
        table.add(playButton).align(Align.center).colspan(3).pad(10);
        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                helloGame.playSound(helloGame.isSoundMuted);

                //fade out menuscreen
                Timeline.createParallel().beginParallel()
                        .pushPause(2)
                        .push(Tween.to(title1Label, ActorAccessor.ALPHA, 1f).target(0))
                        .push(Tween.to(title2Label, ActorAccessor.ALPHA, 1f).target(0))
                        .push(Tween.to(playButton, ActorAccessor.ALPHA, 1f).target(0))
                        .push(Tween.to(exitbutton, ActorAccessor.ALPHA, 1f).target(0))
                        .end().setCallback(new TweenCallback() {
                            @Override
                            public void onEvent(int type, BaseTween<?> source) {
                                helloGame.activateStartScreen();
                                hide();
                            }
                        }).start(tweenManager);
            }
        });

        table.row();

        exitbutton = new TextButton("EXIT", skin);
        table.add(exitbutton).align(Align.center).colspan(3).pad(10);
        exitbutton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                helloGame.playSound(helloGame.isSoundMuted);
                Gdx.app.exit();
            }
        });

        //setting alphas to 0
        playButton.setColor(exitbutton.getColor().r, exitbutton.getColor().g, exitbutton.getColor().b, 0);
        exitbutton.setColor(exitbutton.getColor().r, exitbutton.getColor().g, exitbutton.getColor().b, 0);

        //Animating buttons
        tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new ActorAccessor());

        //Button fade in sequentially

        Timeline.createSequence().beginSequence()
                .pushPause(2)
                .push(Tween.to(playButton, ActorAccessor.ALPHA, 1f).target(1))
                .push(Tween.to(exitbutton, ActorAccessor.ALPHA, 0.5f).target(1))
                .end().setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        //Only allow user to click once effect have loaded in
                        Gdx.input.setInputProcessor(stage);
                    }
                }).start(tweenManager);


        stage.addActor(table);
        stage.addActor(table2);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.PURPLE);

        tweenManager.update(delta);

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        table.clear();
        table2.clear();
    }

    @Override
    public void dispose() {
        skin.dispose();
        stage.dispose();

    }
}
