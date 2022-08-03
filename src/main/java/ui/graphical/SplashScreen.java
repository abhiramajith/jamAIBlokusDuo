package ui.graphical;


/*
     Team: jamAI
     Student #: 20344393, 20364441, 20483142
 */

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import org.lwjgl.opengl.GL20;
import ui.graphical.Tween.SpriteAccessor;

import static java.lang.Math.round;


public class SplashScreen implements Screen {

    private Stage stage;
    private Skin skin;
    private HelloGame helloGame;
    private Sprite backgroundSprite;
    private SpriteBatch batch;
    private TweenManager tweenManager;
    private String backgroundString, bg1 = "Images/bg.png", bg1alt = "Images/bg1alt.png";


    public SplashScreen(HelloGame helloGame) {
        this.helloGame = helloGame;
        this.stage = helloGame.stage;
        this.skin = helloGame.theme.skin;

    }


    public void show() {
        batch = new SpriteBatch();
        tweenManager = new TweenManager();
        Table table = new Table();
        table.setFillParent(true);

        //allows sprite alpha to be accessed
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());

        //typinglabel methods for label animations
        String jamAItext = "{COLOR=WHITE}{HANG=1;0.8} {WAIT=1.5}jam{WAIT}AI ";

        TypingLabel splashLabel = new TypingLabel(jamAItext, skin, "karmaW", Color.WHITE);

        //randomly pick between drawn backgrounds
        int randNum = (int) round(Math.random());
        if (randNum == 1){
            backgroundString = bg1;
        }else{
            backgroundString = bg1alt;
        }
        Texture background = new Texture(backgroundString);
        backgroundSprite = new Sprite(background);

        //setting alpha to 0 to fade in
        backgroundSprite.setAlpha(0);

        //using Tween engine to fade background in and out
        Tween.to(backgroundSprite, SpriteAccessor.ALPHA, 2).target(1).repeatYoyo(1,2).setCallback(new TweenCallback() {
            @Override
            public void onEvent(int type, BaseTween<?> source) {
                helloGame.activateMenuScreen();
            }
        }).start(tweenManager);

        stage.addActor(table);
        table.add(splashLabel);

    }

    public void hide() {

    }

    public void render(float delta) {
        Gdx.gl.glClearColor(0,0,0,1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);
        batch.begin();
        batch.setColor(backgroundSprite.getColor());
        backgroundSprite.draw(batch);
        batch.end();

        stage.act(delta);
        stage.draw();

    }

    public void resize(int width, int height) {

        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    public void dispose() {
        stage.dispose();
        batch.dispose();
        backgroundSprite.getTexture().dispose();
    }


}
