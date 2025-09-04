package io.github.joshuacgunn.core;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import io.github.joshuacgunn.core.screens.MainMenuScreen;

public class Main extends Game {
    public SpriteBatch batch;
    public AssetManager assets;
    public BitmapFont titleFont;

    public void create() {
        batch = new SpriteBatch();
        assets = new AssetManager();

        assets.load("uiskin.json", Skin.class);
        assets.load("mainmenu.png", Texture.class);
        assets.load("gray.jpg", Texture.class);
        assets.load("border.png", Texture.class);
        assets.load("border.jpg", Texture.class);
        assets.load("ninepatches1.png", Texture.class);

        assets.finishLoading();

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("Ac437_IBM_BIOS.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = 24;
        param.borderColor = Color.BLACK;
        param.borderWidth = 2;
        param.shadowColor = Color.BLACK;
        param.shadowOffsetX = 2;
        param.shadowOffsetY = 2;
        titleFont = generator.generateFont(param);
        generator.dispose();

        setScreen(new MainMenuScreen(this));
    }

    public void render() {
        super.render();
    }

    public void dispose() {
        super.dispose();
        batch.dispose();
        assets.dispose();
    }
}

