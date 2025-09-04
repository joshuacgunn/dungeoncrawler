package io.github.joshuacgunn.core.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import io.github.joshuacgunn.core.Main;

import java.io.File;

public class MainMenuScreen implements Screen {
    final Main game;
    private final Stage stage;
    private Texture background;

    private TextButton loadGameBtn;
    private TextButton newGameBtn;
    private TextButton quitGameBtn;

    // track ownership so dispose() is safe
    private final boolean ownsResources = true;

    public MainMenuScreen(final Main game) {
        this.game = game;
        // Keep constructor lightweight: just record references and create stage
        stage = new Stage(new ScreenViewport());
    }

    private void initAssets() {
        background = game.assets.get("mainmenu.png", Texture.class);
    }

    private void buildUI() {
        Label.LabelStyle lblStyle = new Label.LabelStyle(game.titleFont, Color.WHITE);
        Label splash = new Label("D U N G E O N C R A W L E R", lblStyle);

        TextButton.TextButtonStyle btnStyle = new TextButton.TextButtonStyle();
        btnStyle.font = game.titleFont;
        btnStyle.fontColor = Color.WHITE;

        loadGameBtn = new TextButton("Load Game", btnStyle);
        newGameBtn = new TextButton("New Game", btnStyle);
        quitGameBtn = new TextButton("Quit Game", btnStyle);

        Table center = new Table();
        center.setFillParent(true);
        center.center();
        center.defaults().pad(8).width(240);

        // show load if saves exist
        if (new File("saves/player_save.json").exists() || new File("backups/saves/").exists()) {
            center.add(loadGameBtn).row();
        }
        center.add(newGameBtn).row();
        center.add(quitGameBtn).row();

        // top text
        Table top = new Table();
        top.setFillParent(true);
        top.top();
        top.add(splash).expandX().padTop(24);

        stage.addActor(center);
        stage.addActor(top);

        // force layout, enable transforms and set origins so scale actions work
        center.layout();
        if (loadGameBtn != null) {
            loadGameBtn.setTransform(true);
            loadGameBtn.setOrigin(loadGameBtn.getWidth() / 2f, loadGameBtn.getHeight() / 2f);
        }
        newGameBtn.setTransform(true);
        newGameBtn.setOrigin(newGameBtn.getWidth() / 2f, newGameBtn.getHeight() / 2f);
        quitGameBtn.setTransform(true);
        quitGameBtn.setOrigin(quitGameBtn.getWidth() / 2f, quitGameBtn.getHeight() / 2f);
    }

    private void attachListeners() {
        if (loadGameBtn != null) {
            loadGameBtn.addListener(new ClickListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    loadGameBtn.addAction(Actions.scaleTo(1.1f, 1.1f, 0.08f));
                }
                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    loadGameBtn.addAction(Actions.scaleTo(1f, 1f, 0.08f));
                }
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    game.setScreen(new GameScreen(game));
                }
            });
        }

        newGameBtn.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                newGameBtn.addAction(Actions.scaleTo(1.1f, 1.1f, 0.08f));
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                newGameBtn.addAction(Actions.scaleTo(1f, 1f, 0.08f));
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.setScreen(new NewCharacterScreen(game));
            }
        });
        quitGameBtn.addListener(new ClickListener() {
            @Override
            public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                quitGameBtn.addAction(Actions.scaleTo(1.1f, 1.1f, 0.08f));
            }
            @Override
            public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                quitGameBtn.addAction(Actions.scaleTo(1f, 1f, 0.08f));
            }
            @Override
            public void clicked(InputEvent event, float x, float y) {
                game.dispose();
                System.exit(0);
            }
        });
    }

    @Override
    public void show() {
        // load assets and UI when the screen is shown
        initAssets();
        buildUI();
        attachListeners();
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(Color.BLACK);
        stage.getViewport().apply();

        this.game.batch.setProjectionMatrix(stage.getCamera().combined);
        this.game.batch.begin();
        this.game.batch.draw(background, 0, 0, stage.getViewport().getWorldWidth(), stage.getViewport().getWorldHeight());
        this.game.batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        stage.dispose();
        if (ownsResources) {
            if (game.titleFont != null) game.titleFont.dispose();
            if (game.batch != null) this.game.batch.dispose();
            if (background != null) background.dispose();
        }
    }
}
