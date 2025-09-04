package io.github.joshuacgunn.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.BaseDrawable;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.joshuacgunn.core.Main;
import io.github.joshuacgunn.core.entity.Player;

public class NewCharacterScreen implements Screen {
    final Main game;
    private final Stage stage;
    private Texture background;
    private TextField charNameField;
    private NinePatch patch;
    private TextButton.TextButtonStyle style;

    private String playerName;
    private Player.PlayerClass playerClass;

    public NewCharacterScreen(Main game) {
        this.game = game;

        stage = new Stage(new ScreenViewport());
    }

    private void InitAssets() {
        background = game.assets.get("gray.jpg", Texture.class);
        patch = new NinePatch(game.assets.get("ninepatches1.png", Texture.class), 12, 12, 12, 12);
    }

    private void BuildUI() {
        Table root = new Table();
        root.setFillParent(true);
        stage.addActor(root);

        Label.LabelStyle lblStyle = new Label.LabelStyle(game.titleFont, Color.WHITE);
        Drawable rounded = new NinePatchDrawable(patch);

        Label top = new Label("Character Creation", lblStyle);
        Label mid1 = new Label("Class Selection", lblStyle);
        Label mid2 = new Label("Middle Box 2", lblStyle);
        Label bottom = new Label("Bottom box", lblStyle);

        Table topBox = new Table();
        topBox.setBackground(rounded);
        topBox.add(top);

        Table midBox1 = new Table();
        midBox1.setBackground(rounded);
        midBox1.add(mid1).expandX().expandY().top().padTop(10);

        Table midBox2 = new Table();
        midBox2.setBackground(rounded);
        midBox2.add(mid2).expandX().expandY().top().padTop(10);

        Table bottomBox = new Table();
        bottomBox.setBackground(rounded);
        bottomBox.add(bottom);


        // Top box (smaller)
        root.add(topBox).expandX().fillX().height(80).pad(10);
        root.row();

        // Middle box 1 (bigger)
        root.add(midBox1).expandX().fillX().height(160).pad(10);
        root.row();

        // Middle box 2 (bigger)
        root.add(midBox2).expandX().fillX().height(160).pad(10);
        root.row();

        // Bottom box (smaller)
        root.add(bottomBox).expandX().fillX().height(80).pad(10);
    }

    private void AttachListeners() {
    }

    @Override
    public void show() {
        InitAssets();
        BuildUI();
        AttachListeners();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.getViewport().apply();

        game.batch.setProjectionMatrix(stage.getCamera().combined);
        game.batch.begin();
        game.batch.draw(background, 0, 0, stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        game.batch.end();
        game.batch.begin();
        game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
