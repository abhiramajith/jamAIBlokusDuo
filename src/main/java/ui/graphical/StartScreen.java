package ui.graphical;

/*
     Team: jamAI
     Student #: 20344393, 20364441, 20483142
 */

import aurelienribon.tweenengine.Timeline;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.rafaskoberg.gdx.typinglabel.TypingLabel;
import ui.graphical.Tween.ActorAccessor;

import java.util.Objects;

public class StartScreen extends ScreenAdapter {


    Theme theme;
    HelloGame helloGame;

    Stage stage;
    Skin skin;
    Table table, table2, table3;
    Label warning1, warning2;
    boolean noNameWarning = false;
    boolean sameNameWarning = false;
    TweenManager tweenManager;
    private TypingLabel titlep1Label, titlep2Label2;
    private String titlep1, titlep2;
    TextField p1TextField, p2TextField;
    Label p1Label, p2Label;
    TextButton start;
    Image player1, player2;


    public StartScreen(HelloGame helloGame, Theme theme) {
        this.helloGame = helloGame;
        this.stage = helloGame.stage;
        this.skin = helloGame.theme.skin;
        this.skin.getFont("font").getData().setScale(1.1f, 1.1f);
        this.theme = theme;

        table = new Table();
        table2 = new Table();
        table3 = new Table();
        table.setFillParent(true);
        table2.setFillParent(true);
        table3.setFillParent(true);
        table.setBackground(skin.getDrawable("bg-tile-ten-2"));
        table2.align(Align.bottomLeft);
        table3.align(Align.topRight);

        titlep1 = "{SLIDE}{EASE}BLOKUS";
        titlep2 = "{EASE=1;1;1} {WAIT=1}DUO";
        titlep1Label = new TypingLabel(titlep1, skin, theme.titleFont, Color.WHITE);
        titlep2Label2 = new TypingLabel(titlep2, skin, theme.titleFont, Color.WHITE);
        table.add(titlep1Label).align(Align.center);
        table.getCell(titlep1Label).spaceBottom(20).colspan(3);
        table.row();
        table.add(titlep2Label2).align(Align.center).colspan(3);
        table.getCell(titlep2Label2);
        table.row();

        p1Label = new Label("Enter name of Player 1:", skin, theme.normalFont, Color.WHITE);
        table.add(p1Label).align(Align.left).pad(10);
        p1TextField = new TextField(null, skin);
        p1TextField.setMessageText("Player 1 Name");
        table.add(p1TextField).align(Align.left);
        player1 = new Image(theme.skin.getRegion("p1Square"));
        table.add(player1).pad(30);
        table.row();


        p2Label = new Label("Enter name of Player 2:", skin, theme.normalFont, Color.WHITE);
        table.add(p2Label).align(Align.left).pad(10);
        p2TextField = new TextField(null, skin);
        p2TextField.setMessageText("Player 2 Name");
        table.add(p2TextField).align(Align.left);
        player2 = new Image(theme.skin.getRegion("p2Square"));
        table.add(player2).pad(30);
        table.row();

        start = new TextButton("START", skin);
        table.add(start).align(Align.center).colspan(3).pad(10);
        start.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {

                String name = p1TextField.getText();
                String name2 = p2TextField.getText();
                if (name.isEmpty() || name2.isEmpty()) {
                    name = p1TextField.getText();
                    name2 = p2TextField.getText();
                }

                if (!noNameWarning && (name.isEmpty() || name2.isEmpty())) {
                    giveNoNameWarning();
                }

                if (!sameNameWarning && (Objects.equals(name, name2)) && !(name.isEmpty() || name2.isEmpty())) {
                    giveSameNameWarning();
                }

                // send name to game control thread
                if (!name.isEmpty() && !name2.isEmpty() && !Objects.equals(name, name2)) {
                    helloGame.uiStream.println(name);
                    helloGame.uiStream.println(name2);
                    helloGame.playSound(helloGame.isSoundMuted);
                    helloGame.activatePlayScreen();
                }
            }
        });


        ImageButton settingsButton = new ImageButton(skin.getDrawable("cog"));
        table2.add(settingsButton);
        settingsButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                helloGame.settingsBox();
            }
        });

        Label skinText = new Label("Select a skin: ", skin, theme.normalFont, Color.WHITE);
        table3.add(skinText);
        SelectBox<String> skinSelect = new SelectBox<String>(skin);
        String[] skins = new String[]{"OG", "Shine", "Serenity"};
        skinSelect.setItems(skins);
        switch (theme.titleFont) { //set selected skin based on current title, or else will default after every update
            case "Title1" -> skinSelect.setSelected("OG");
            case "Title2" -> skinSelect.setSelected("Shine");
            case "Title3" -> skinSelect.setSelected("Serenity");
        }
        table3.add(skinSelect);

        skinSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {

                //Switch skin based on user selection
                switch (skinSelect.getSelected()) {

                    case "OG":
                        theme.updateTheme("Skins/skin1.json", "Tilesets/play1.tmx", helloGame.bgmPath1, "Title1", "sansD", "Images/bg.png");
                        helloGame.startScreen = new StartScreen(helloGame, theme);
                        helloGame.playScreen = new PlayScreen(helloGame, theme);
                        helloGame.activateStartScreen();
                        break;
                    case "Shine":
                        theme.updateTheme("Skins/skin2.json", "Tilesets/play2.tmx", helloGame.bgmPath2, "Title2", "sansD", "Images/bg2.png");
                        helloGame.startScreen = new StartScreen(helloGame, theme);
                        helloGame.playScreen = new PlayScreen(helloGame, theme);
                        helloGame.activateStartScreen();
                        break;
                    case "Serenity":
                        theme.updateTheme("Skins/skin3.json", "Tilesets/play3.tmx", helloGame.bgmPath3, "Title3", "sansD", "Images/bg3.png");
                        helloGame.startScreen = new StartScreen(helloGame, theme);
                        helloGame.playScreen = new PlayScreen(helloGame, theme);
                        helloGame.activateStartScreen();
                        break;

                }
            }
        });

        fadeIn();

    }

    public void giveNoNameWarning() {
        if (sameNameWarning) {
            warning2.remove();
            sameNameWarning = false;
        }
        table.row();
        warning1 = new Label("Please enter two names!      ", skin, "sansD", Color.RED);
        table.add(warning1).align(Align.left).colspan(3);
        noNameWarning = true;
    }

    public void giveSameNameWarning() {
        if (noNameWarning) {
            warning1.remove();
            noNameWarning = false;
        }
        table.row();
        warning2 = new Label("Please enter different names!", skin, "sansD", Color.RED);
        table.add(warning2).align(Align.left).colspan(3);
        sameNameWarning = true;
    }

    public void show() {
        super.show();
        stage.addActor(table);
        stage.addActor(table2);
        stage.addActor(table3);
    }

    public void render(float delta) {
        super.render(delta);
        tweenManager.update(delta);
        ScreenUtils.clear(Color.PURPLE);
        stage.act(delta);
        stage.draw();
    }

    public void hide() {
        super.hide();
        stage.clear();
    }


    public void resize(int width, int height) {
        super.resize(width, height);
        stage.getViewport().update(width, height, true);
    }

    public void dispose() {
        super.dispose();
        stage.dispose();
        skin.dispose();


    }

    public void fadeIn() {
        //Setting alphas to 0 to fade in
        start.setColor(start.getColor().r, start.getColor().g, start.getColor().b, 0);
        p1Label.setColor(p1Label.getColor().r, p1Label.getColor().g, p1Label.getColor().b, 0);
        p2Label.setColor(p2Label.getColor().r, p2Label.getColor().g, p2Label.getColor().b, 0);
        player1.setColor(player1.getColor().r, player1.getColor().g, player1.getColor().b, 0);
        player2.setColor(player2.getColor().r, player2.getColor().g, player2.getColor().b, 0);
        p1TextField.setColor(p1TextField.getColor().r, p1TextField.getColor().g, p1TextField.getColor().b, 0);
        p2TextField.setColor(p2TextField.getColor().r, p2TextField.getColor().g, p2TextField.getColor().b, 0);
        //Animating table
        tweenManager = new TweenManager();
        Tween.registerAccessor(Actor.class, new ActorAccessor());

        //Screen fade in

        Timeline.createParallel().beginParallel()
                .pushPause(2)
                .push(Tween.to(p1Label, ActorAccessor.ALPHA, 1f).target(1))
                .push(Tween.to(p2Label, ActorAccessor.ALPHA, 1f).target(1))
                .push(Tween.to(player1, ActorAccessor.ALPHA, 1f).target(1))
                .push(Tween.to(player2, ActorAccessor.ALPHA, 1f).target(1))
                .push(Tween.to(start, ActorAccessor.ALPHA, 1f).target(1))
                .push(Tween.to(p1TextField, ActorAccessor.ALPHA, 1f).target(1))
                .push(Tween.to(p2TextField, ActorAccessor.ALPHA, 1f).target(1))
                .end().start(tweenManager);
    }
}