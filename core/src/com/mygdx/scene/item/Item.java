package com.mygdx.scene.item;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.scene.tileset.Tileset;

public class Item extends Group implements Serializable {
	/** A reference to the tileset use for this {@link Item} */
	private Tileset tileset;
	
	/** The {@link TextureRegion} allowing to display the item. */
	private TextureRegion item;
	
	/** The size of the item in the map (in tile). */
	private Vector2 tileSize;
	
	/** The exact size of the item. All items don't have the same size. */
	private Vector2 spriteSize;
	
	/** The item's id. */
	private int id;
	
	private String title;
	
	private String description;
	
	/** The path to the JSON file. */
	private String jsonPath;
	
	
	// Construction
	
	/**
	 * Constructor. Do nothing, use for {@link Serializable}.
	 */
	public Item() {
		
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
		item.setRegionWidth((int) this.spriteSize.x);
		item.setRegionHeight((int)this.spriteSize.y);
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
	
	
	// Title and description methods
	
	public String getTitle() {
		return this.title;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	
	// Flip management
	
	/**
	 * Flip the item.
	 */
	public void flip() {
		// Flip the texture.
		if (item.isFlipX())
			item.flip(false, false);
		else
			item.flip(true, false);
		
		// Flip the size.
		float y = this.tileSize.x;
		this.tileSize.x = this.tileSize.y;
		this.tileSize.y = y;
		
		// Flip origin
		setOriginX(item.getRegionWidth() - getOriginX());
	}
	
	
	// Tile size methods
	
	/**
	 * @return the tile size of the item.
	 */
	public Vector2 getTileSize() {
		return this.tileSize;
	}
	
	/**
	 * Set the size of the item (in tile).
	 * @param tileSizeX the hitem's horizontal size.
	 * @param tileSizeY the item's vertical size.
	 */
	public void setTileSize(int tileSizeX, int tileSizeY) {
		this.tileSize.x = tileSizeX;
		this.tileSize.y = tileSizeY;
	}
	
	
	// Transformation overriding
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x - getOriginX(), y - getOriginY());
	}
	
	@Override
	public void setOrigin(float originX, float originY) {
		float relativeOriginX = getOriginX() - originX;
		float relativeOriginY = getOriginY() - originY;
		
		super.setOrigin(originX, originY);
		super.setPosition(getX() - relativeOriginX, getY() - relativeOriginY);
	}
	
	@Override
	public void setOriginX(float originX) {
		setOrigin(originX, getOriginY());
	}
	
	@Override
	public void setOriginY(float originY) {
		setOrigin(getOriginX(), originY);
	}
	
	
	// Dispose
	
	public void dispose() {
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
		json.writeValue("spriteSize", this.spriteSize);
		json.writeValue("id", this.id);
		json.writeValue("tileSize", this.tileSize);
		json.writeValue("origin", new Vector2(this.getOriginX(), this.getOriginY()));
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		this.spriteSize = new Vector2(jsonData.child.child.asInt(), jsonData.child.child.next.asInt());
		this.id = jsonData.child.next.asInt();
		this.title = jsonData.child.next.next.name;
		this.description = jsonData.child.next.next.next.asString();
		this.setSize(spriteSize.x, spriteSize.y);
		
		this.tileSize = new Vector2(jsonData.child.next.next.next.next.child.asInt(), jsonData.child.next.next.next.next.child.next.asInt());
		this.setOrigin(jsonData.child.next.next.next.next.next.child.asFloat(), jsonData.child.next.next.next.next.next.child.next.asFloat());
	}
	
	
	// Factory
	
	/**
	 * Create an item according to the specified JSON.
	 * @param jsonPath the path to the JSON file.
	 * @param itemTileset the tileset to use for this item.
	 * @return the created item.
	 */
	public static Item loadItemFromJSON(String jsonPath, Tileset itemTileset) {
		Json json = new Json();
		
		// Load the file.
		FileHandle file = new FileHandle(jsonPath);
		
		// Extract Item from JSON file.
		Item createdItem = json.fromJson(Item.class, file);
		createdItem.setTileset(itemTileset);
		createdItem.setJsonPath(jsonPath);
		
		// return it.
		return createdItem;
	}
}
