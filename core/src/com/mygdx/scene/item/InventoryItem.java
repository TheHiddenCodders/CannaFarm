package com.mygdx.scene.item;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.scene.tileset.Tileset;

public class InventoryItem extends Actor implements Serializable{
	/** A reference to the tileset use for this {@link InventoryItem} */
	private Tileset tileset;
	
	/** The {@link TextureRegion} allowing to display the item. */
	private TextureRegion item;
	
	/** The item's id. */
	private int id;
	
	/** The path to the JSON file. */
	private String jsonPath;
	
	
	// Construction
	
	/**
	 * Constructor. Do nothing, use for {@link Serializable}.
	 */
	public InventoryItem() {
		
	}
	
	
	// Edition management.
	
	/**
	 * Change the item bounds according to the stored id.
	 */
	private void computeItemRegion() {
		Rectangle tileRectangle = tileset.getTileBound(this.id);
		
		// Change the region of the texture according to the tileset.
		item.setRegionX((int) tileRectangle.x);
		item.setRegionY((int) tileRectangle.y);
		item.setRegionWidth((int) tileRectangle.width);
		item.setRegionHeight((int) tileRectangle.height);
		
		setSize(tileRectangle.width, tileRectangle.height);
	}
	
	
	// Tileset methods
	
	/**
	 * Set the item's tileset and recompute the region to display it properly.
	 * @param itemTileset
	 */
	public void setTileset(Tileset itemTileset) {
		this.tileset = itemTileset;
		this.item = new TextureRegion(this.tileset.getTexture());
		
		computeItemRegion();
	}
	
	
	// JSON file methods
	
	/**
	 * The JSON use to set this item.
	 * @param jsonPath the path to set.
	 */
	public void setJsonPath(String jsonPath) {
		this.jsonPath = jsonPath;
	}
	
	/**
	 * @return the JSON use to set this item.
	 */
	public String getJsonPath() {
		return this.jsonPath;
	}
	
	
	// ID methods
	
	/**
	 * The item id to set.
	 * @param id to set.
	 */
	public void setID(int id) {
		this.id = id;
		computeItemRegion();
	}
	
	/**
	 * @return the item's id.
	 */
	public int getId() {
		return this.id;
	}
	
	
	// Actors inherit methods

	@Override
	public void act(float delta) {
	}

	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(item, getX(), getY(), getOriginX(), getOriginY(),
				getWidth(), getHeight(), getScaleX(), getScaleY(),
				getRotation());
	}
	
	
	// JSON methods

	@Override
	public void write(Json json) {
		/*json.writeValue("spriteSize", this.spriteSize);
		json.writeValue("id", this.id);
		json.writeValue("tileSize", this.tileSize);
		json.writeValue("origin", new Vector2(this.getOriginX(), this.getOriginY()));*/
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		this.id = jsonData.child.next.asInt();
	}
	
	
	// Equals
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof InventoryItem){
			InventoryItem itemToCompare = (InventoryItem) obj;
			
			if (itemToCompare.getId() == this.getId()) {
				return true;
			}
		}
		
		return false;
	}
		
	
	// Factory
	
	/**
	 * Create an item according to the specified JSON.
	 * @param jsonPath the path to the JSON file.
	 * @param itemTileset the tileset to use for this item.
	 * @return the created item.
	 */
	public static InventoryItem loadItemFromJSON(String jsonPath, Tileset itemTileset) {
		Json json = new Json();
		
		// Load the file.
		FileHandle file = new FileHandle(jsonPath);
		
		// Extract Item from JSON file.
		InventoryItem createdItem = json.fromJson(InventoryItem.class, file);
		createdItem.setTileset(itemTileset);
		createdItem.setJsonPath(jsonPath);
		
		// return it.
		return createdItem;
	}
}
