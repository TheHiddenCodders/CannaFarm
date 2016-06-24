package com.mygdx.scene.map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.scene.inventory.Inventory;
import com.mygdx.scene.item.Item;
import com.mygdx.scene.resources.GameItems;
import com.mygdx.scene.resources.GameTilesets;
import com.mygdx.scene.tileset.Tileset;

public class ItemMap extends Map implements Serializable {
	/** All items of the map order by display */
	private Array<Array<Item>> items;
	
	/** All values read in the JSON file to create items. */
	private Array<Array<String>> itemsSave;
	
	private Inventory inventory;
	
	
	// Construction

	/**
	 * Constructor. Do nothing, use for {@link Serializable}.
	 */
	public ItemMap() {
		super();
	}
	
	
	// Edition management.
	
	/**
	 * Create the array containing items and initiate it with null items.
	 */
	private void createItems() {
		items.clear();
		
		// Create arrays
		for (int i = 0; i < (size.x - 1) + (size.y - 1) + 1; i++) {
			items.add(new Array<Item>());
			
			for (int j = 0; j < (size.x - 1) + (size.y - 1) + 1; j++) {
				// Create null items
				items.get(i).add(null);
			}
		}
	}
	
	/**
	 * Create map's items according to values read in the JSON file.
	 * @param gameItems a reference to all game items.
	 * @param itemTileset a reference to the tileset to set on items.
	 */
	private void initFromFile(GameItems gameItems, Tileset itemTileset, final Inventory inventory) {
		// Create items
		this.items = new Array<Array<Item>>();
		createItems();
		
		// Load items
		for (int i = 0; i < this.size.x; i++) {
			for (int j = 0; j < this.size.y; j++) {
				if (!this.itemsSave.get(i).get(j).equals("null")) {
					Item itemToAdd = Item.loadItemFromJSON(gameItems.getItemPath(this.itemsSave.get(i).get(j)), itemTileset);
					addItem(itemToAdd, i, j);
				}
			}
		}
		
		// Add the reference to the inventory
		this.inventory = inventory;
		
		addListener(new InputListener() {
			//private boolean onMap;
			
			public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
				//onMap = true;
				System.out.println("enter on map");
				if (inventory.isItemDrag()) {
					touchDown(event, x, y, pointer, 0);
				}
			}
			
			public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
				//onMap = false;
			}
			
			public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
				System.out.println("Touch up.");
				
				if (inventory.isItemDrag() /*&& onMap*/) {
					System.out.println("Place item on map here.");
				}
			}
		});
	}
	
	
	// Item methods
	
	/**
	 * Add an item to the map at the specified position.
	 * @param itemToAdd a reference to the item to add on the map.
	 * @param positionX the horizontal position of the item.
	 * @param positionY the vertical position of the item.
	 */
	public void addItem(Item itemToAdd, int positionX, int positionY) {
		int itemCount = 0;
		
		int screenPositionX = ((int)size.y - 1 - positionY) - ((int)size.x - 1 - positionX) + ((int)size.x - 1);
		int screenPositionY = ((int)size.x - 1 - positionX) + ((int)size.y - 1 - positionY);
		
		// Take in count the item tile size.
		// x positive : x++, y--
		// y positive : x--, y--
		
		// If the place is already taken
		if (items.get(screenPositionX).get(screenPositionY) != null) {
			removeItem(positionX, positionY);
		}
		
		// Add the item in the array and place it on the stage.
		items.get(screenPositionX).set(screenPositionY, itemToAdd);
		items.get(screenPositionX).get(screenPositionY).setPosition((positionX - positionY) * getTileSize().x / 2 , (positionX + positionY) * getTileSize().y / 2);
		
		
		addActor(items.get(screenPositionX).get(screenPositionY));
		
		// Browse all items to set the Z index
		for (int i = 0; i < (size.x - 1) + (size.y - 1) + 1; i++) {
			for (int j = 0; j < (size.x - 1) + (size.y - 1) + 1; j++) {
				if (items.get(j).get(i) != null) {
					items.get(j).get(i).setZIndex((int) (size.x*size.y + itemCount));
					itemCount++;
				}
			}
		}
	}
	
	/**
	 * rRemove the item at the specified position, if it exist.
	 * @param positionX the item's horizontal position.
	 * @param positionY the item's vertical position.
	 */
	public void removeItem(int positionX, int positionY) {
		// Get the screen position of the item.
		int screenPositionX = ((int)size.x - 1 - positionX) + ((int)size.y - 1 - positionY);
		int screenPositionY = ((int)size.x - 1 - positionX) - ((int)size.y - 1 - positionY) + ((int)size.x - 1);
		
		// If the item exist, delete it.
		if (items.get(screenPositionX).get(screenPositionY) != null) {
			items.get(screenPositionX).get(screenPositionY).dispose();
			removeActor(items.get(screenPositionX).get(screenPositionY));
			
			items.get(positionX).set(positionY, null);
		}
	}
	
	
	// JSON methods

	@Override
	public void write(Json json) {
		super.write(json);
		
		// TODO : create a json file from a MapItem.
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		// Initialize Map attribute
		super.read(json, jsonData);
		
		// Move to items of the map.
		JsonValue currentline = jsonData.child.next.next.child;
		this.itemsSave = new Array<Array<String>>();
		
		// Store read data in itemsSave.
		for (int i = 0; i < this.size.x; i++) {
			String[] lineValues = currentline.asStringArray();
			this.itemsSave.add(new Array<String>());
			
			for (int j = 0; j < this.size.y; j++) {
				this.itemsSave.get(i).add(lineValues[j]);
			}
			
			currentline = currentline.next;
		}
	}
	
	
	// Factory

	/**
	 * Create a map containing item according to the specified JSON.
	 * @param jsonPath the path to the JSON file.
	 * @param tilesets a reference to all game's items.
	 * @param gameItems a reference to all game's tilesets.
	 * @return the created map.
	 */
	public static ItemMap loadFromJSON(String jsonPath, GameTilesets tilesets, GameItems gameItems, final Inventory inventory) {
		Json json = new Json();
		
		// Load the file.
		FileHandle file = new FileHandle(jsonPath);
		
		// Extract Map from JSON file.
		ItemMap createdMap = json.fromJson(ItemMap.class, file);
		
		// Apply tileset on tiles.
		createdMap.setTileset(tilesets.getTileset("tile"));
		
		// Create items according to read data.
		createdMap.initFromFile(gameItems, tilesets.getTileset("item"), inventory);
		
		
		// return the map.
		return createdMap;
	}
} 
