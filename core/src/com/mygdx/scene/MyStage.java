package com.mygdx.scene;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.scene.inventory.Inventory;
import com.mygdx.scene.map.ItemMap;
import com.mygdx.scene.resources.GameItems;
import com.mygdx.scene.resources.GameTilesets;

public class MyStage extends Stage {

	private GameTilesets gameTilesets;
	private GameItems itemsPaths;
	private ItemMap map;
	private Inventory inventory;
	

	public MyStage(Viewport viewport) {
		super(viewport);
		
		// Load resources
		gameTilesets = GameTilesets.loadFromJSON("save/tilesets/tilesets.json");
		itemsPaths = GameItems.loadFromJSON("save/items/items.json");
		
		// Create a map
		map = ItemMap.loadFromJSON("save/map/itemMap.json", gameTilesets, itemsPaths);
		map.setPosition(400, 100);
		addActor(map);
		
		// Create inventory
		inventory = new Inventory(gameTilesets, itemsPaths);
		inventory.setVisible(false);
		//inventory.setPosition(100, 100);
		//inventory.setSize(200, 200);
		addActor(inventory);
		
		//InventoryItem item = InventoryItem.loadItemFromJSON(itemsPaths.getItemPath("pot"), gameTilesets.getTileset("inventoryItem"));
		//addActor(item);
		
		addListener(new InputListener() {
			public boolean keyDown(InputEvent event, int keycode) {
				if (keycode == Keys.I) {
					if (inventory.isVisible()) {
						inventory.setVisible(false);
					}
					else {
						inventory.setVisible(true);
					}
				}
				return true;
			}
		});
	}

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public void draw() {
		super.draw();
	}
}
