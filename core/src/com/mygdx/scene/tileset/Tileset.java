package com.mygdx.scene.tileset;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class Tileset implements Serializable {
	/** The tileset texture. */
	private Texture texture;
	
	/** The texture file path. */
	private String texturePath;
	
	/** The tileset size (in tile). */
	private Vector2 tileNumber;
	
	
	// Construction.
	
	/**
	 * Default constructor. Do nothing, use for {@link Serializable}.
	 */
	public Tileset() {
	}
	
	/**
	 * Constructor. Load the specified texture and store the tileset size.
	 * @param texturePath the path to the texture.
	 * @param tileNumber the tileset size (in tile).
	 */
	public Tileset(String texturePath, Vector2 tileNumber) {
		this.texturePath = texturePath;
		this.texture = new Texture(texturePath);
		this.tileNumber = tileNumber;
	}
	
	/**
	 * Constructor. Load the texture specified texture and store the tileset size.
	 * @param texturePath the path to the texture.
	 * @param tileNumberX the horizontal tile number in the tileset.
	 * @param tileNumberY the vertical tils number in the tileset.
	 */
	public Tileset(String texturePath, int tileNumberX, int tileNumberY) {
		this(texturePath, new Vector2(tileNumberX, tileNumberY));
	}
	
	
	// Texture gesture.
	
	/**
	 * @return the tileset texture.
	 */
	public Texture getTexture() {
		return this.texture;
	}
	
	
	// Tileset size methods.
	
	/**
	 * @return the tileset size (in tile).
	 */
	public Vector2 getTileNumber() {
		return this.tileNumber;
	}
	
	/**
	 * @return the size of a single tile in this tileset.
	 */
	public Vector2 getTileSize() {
		return (new Vector2()).set(texture.getWidth(), texture.getHeight()).scl(1/tileNumber.x, 1/tileNumber.y);
	}
	
	/**
	 * @param id the tile id wanted.
	 * @return a {@link Rectangle} defining the region of the specified id.
	 */
	public Rectangle getTileBound(int id) {
		// Create the rectangle to return
		Rectangle textureRegion = new Rectangle();
		
		// Compute the tile size in the texture.
		int tileSizeX = (int) (this.texture.getWidth() / this.tileNumber.x);
		int tileSizeY = (int) (this.texture.getHeight() / this.tileNumber.y);
		
		// Fill the rectangle in.
		textureRegion.x = (int) (id % tileNumber.x) * (int) tileSizeX;
		textureRegion.y = (int) (id / tileNumber.x) * (int) tileSizeY;
		textureRegion.width = (int) tileSizeX;
		textureRegion.height = (int) tileSizeY;
		
		// Return it.
		return textureRegion;
	}
	
	
	// Dispose
	
	public void dispose() {
		this.texture.dispose();
	}
	
	
	// JSON methods
	
	@Override
	public void write(Json json) {
		// Write in the JSON file only the texture file path and the number of tile of this tileset.
		json.writeValue("texturePath", this.texturePath);
		json.writeValue("tileNumber", this.tileNumber);
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		// Get the texture file path and load it.
		this.texturePath = jsonData.child.asString();
		this.texture = new Texture(texturePath);
		
		// Set the tile number.
		this.tileNumber = new Vector2(jsonData.child.next.child.asFloat(), jsonData.child.next.child.next.asFloat());
	}
	
	
	// Factory
	
	/**
	 * Create a tileset according to the specified JSON.
	 * @param jsonPath the path to the JSON file.
	 * @return the created tileset.
	 */
	public static Tileset loadTilesetFromJSON(String jsonPath) {
		Json json = new Json();
		
		// Load the file.
		FileHandle file = new FileHandle(jsonPath);
		
		// Extract Tileset from JSON file.
		Tileset createdTileset = json.fromJson(Tileset.class, file);
		
		// return the tileset.
		return createdTileset;
	}
}
