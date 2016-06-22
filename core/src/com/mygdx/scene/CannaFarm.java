package com.mygdx.scene;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class CannaFarm extends ApplicationAdapter {
	private Stage stage;

	@Override
	public void create () {
	    stage = new MyStage(new StretchViewport(800, 480));
		//stage = new Inventory(new StretchViewport(800, 480), Tileset.loadTilesetFromJSON("save/tilesets/buttonTileset.json"));
	    Gdx.input.setInputProcessor(stage);
	}

	public void resize (int width, int height) {
	    stage.getViewport().update(width, height, true);
	}
	
	@Override
	public void render () {
	    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
	    
	    stage.act(0);
	    stage.draw();
	}

	public void dispose() {
	    stage.dispose();
	}
}
