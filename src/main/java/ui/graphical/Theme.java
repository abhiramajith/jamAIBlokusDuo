package ui.graphical;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.ray3k.stripe.FreeTypeSkin;

import static ui.graphical.HelloGame.musicVol;

//Class for setting current theme for the game
public class Theme {

    String titleFont;
    String normalFont;
    String bgImage;
    Skin skin;
    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;
    TiledMapTileLayer tileLayer;
    Texture background;
    MapLayer objectLayer;
    Music music;

    public Theme(String skinName, String tiledMapName, String bgmName, String titleFont, String normalFont, String bgImage) {
        this.skin = new FreeTypeSkin(Gdx.files.internal(skinName));
        this.tiledMap = new TmxMapLoader().load(tiledMapName);
        this.tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Tile Layer");
        this.objectLayer = tiledMap.getLayers().get("Object Layer");
        this.music = Gdx.audio.newMusic(Gdx.files.internal(bgmName));
        this.titleFont = titleFont;
        this.normalFont = normalFont;
        this.bgImage = bgImage;

        music.setVolume(musicVol);
        music.setLooping(true);
        music.play();
    }

    public void updateTheme(String skinName, String tiledMapName, String bgmName, String titleFont, String normalFont, String bgImage) {
        dispose(); //dispose resources from previous themes

        //update theme with new resources
        this.skin = new FreeTypeSkin(Gdx.files.internal(skinName));
        this.tiledMap = new TmxMapLoader().load(tiledMapName);
        this.tileLayer = (TiledMapTileLayer) tiledMap.getLayers().get("Tile Layer");
        this.objectLayer = tiledMap.getLayers().get("Object Layer");
        music.dispose();
        this.music = Gdx.audio.newMusic(Gdx.files.internal(bgmName));
        this.titleFont = titleFont;
        this.normalFont = normalFont;
        this.bgImage = bgImage;

        music.setVolume(musicVol);
        music.setLooping(true);
        music.play();
    }

    public void dispose() {
        if (skin != null) skin.dispose();
        if (background != null) background.dispose();
        if (tiledMap != null) tiledMap.dispose();
    }

}

