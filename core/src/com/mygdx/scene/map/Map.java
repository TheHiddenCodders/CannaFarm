package com.mygdx.scene.map;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.scene.tileset.Tileset;

/**
 * Represent a tile map.
 * 
 * @author Kurond
 *
 */
public class Map extends Group implements Serializable {
	/** All tiles of the map. */
	private Array<Array<Tile>> tiles;

	/** The map size, in term of tiles. */
	protected Vector2 size;
	
	/** A reference to the tileset use for this {@link Map} */
	protected Tileset tileset;
	
	/** The size of the tile's map in pixel. */
	private Vector2 tileSize;
	
	protected Vector2 pointedTile;

	
	// Construction

	/**
	 * Constructor. Do nothing, use for {@link Serializable}.
	 */
	public Map() {
	}

	
	// Edition management.
	
	/**
	 * Create map's tile.
	 * @param sizeX the horizontal number of tile.
	 * @param sizeY the vertical number of tile.
	 * @param id the default id to use.
	 */
	private void createTiles(int sizeX, int sizeY, int id) {
		this.size = new Vector2(sizeX, sizeY);
		this.tiles.clear();
		
		this.pointedTile = new Vector2();
		
		// Browse map row.
		for (int i = 0; i < this.size.x; i++) {
			// Create map row.
			tiles.add(new Array<Tile>());
			
			// Browse tiles of the row.
			for (int j = 0; j < this.size.y; j++) {
				final int positionX = i, positionY = j;
				
				// Create the tiles and add it as actor.
				tiles.get(i).add(new Tile(id/*, tileset*/));
				tiles.get(i).get(j).addListener(new InputListener() {
					public boolean mouseMoved(InputEvent event, float x, float y) {
						pointedTile.set(positionX, positionY);
						return true;
					}
				});
				addActor(tiles.get(i).get(j));
			}
		}
	}
	
	/**
	 * Recompute the map structure according to the tile size.
	 */
	protected void computeMapStructure() {
		// Browse all tiles of the map.
		for (int i = 0; i < this.size.x; i++) {
			for (int j = 0; j < this.size.y; j++) {
				// Set tile's position and size.
				tiles.get(i).get(j).setPosition((i - j) * tileSize.x / 2 , (i + j) * tileSize.y / 2);
				tiles.get(i).get(j).setSize(tileSize.x, tileSize.y);
			}
		}
	}
	
	/**
	 * Change the id of the specified tile
	 * @param positionX the horizontal tile's position.
	 * @param positionY the vertical tile's position.
	 * @param id the new tile's id.
	 */
	public void setTileID(int positionX, int positionY, int id) {
		tiles.get(positionX).get(positionY).setID(id);
	}
	
	
	// Tileset methods.

	/**
	 * Set the map's {@link Tileset}. Reconstruct the map.
	 * @param tileset
	 */
	public void setTileset(Tileset tileset) {
		this.tileset = tileset;
		//createTiles((int) size.x, (int) size.y, 0);
		
		// Apply the tileset on all map's tiles
		for (Array<Tile> tilesRow : tiles) {
			for (Tile tile : tilesRow) {
				tile.setTileset(tileset);
			}
		}
		
		// Re-scale the map
		setScale(this.tileSize.x/tileset.getTileSize().x, this.tileSize.y/tileset.getTileSize().y);
		
		this.tileSize.x = tileset.getTileBound(0).width;
		this.tileSize.y = tileset.getTileBound(0).height;
		
		computeMapStructure();
	}
	
	
	// Tile size methods.
	
	public Vector2 getTileSize() {
		return this.tileSize;
	}
	
	/**
	 * Set the displayed tile size of all map's tile.
	 * @param tileSize the new tile size.
	 */
	public void setTileSize(Vector2 tileSize) {
		// Re-scale the map
		setScale(tileSize.x/tileset.getTileSize().x, tileSize.y/tileset.getTileSize().y);
		
		this.tileSize.x = tileset.getTileBound(0).width;
		this.tileSize.y = tileset.getTileBound(0).height;
		
		computeMapStructure();
	}
	
	
	// Size methods
	
	/**
	 * @return the map's size.
	 */
	public Vector2 getMapSize() {
		return this.size;
	}
	

	// Relative transformations.
	
	/**
	 * Move the map of a specified vector.
	 * @param translateX
	 * @param translateY
	 */
	public void translate(float translateX, float translateY) {
		setPosition(getX() + translateX, getY() + translateY);
	}
	
	/**
	 * Move the map of a specified vector.
	 * @param translate
	 */
	public void translate(Vector2 translate) {
		translate(translate.x, translate.y);
	}

	/**
	 * Scale the map of the specified vector.
	 * @param scaleX
	 * @param scaleY
	 */
	public void scale(float scaleX, float scaleY) {
		setScale(getScaleX() * scaleX, getScaleY() * scaleY);
	}
	
	/**
	 * Scale the map of the specified vector.
	 * @param scale
	 */
	public void scale(Vector2 scale) {
		scale(scale.x, scale.y);
	}

	/**
	 * Rotate the map of the specified angle.
	 * @param angle
	 */
	public void rotate(float angle) {
		setRotation(getRotation() + angle);
	}
	
	
	// JSON methods

	@Override
	public void write(Json json) {
		// Write in the JSON file the tileset JSON file path, the map's size and tiles size.
		json.writeValue("size", this.size);
		json.writeValue("tileSize", this.tileSize);
		
		// Then, write all IDs.
		json.writeArrayStart("content");
		for (int i = 0; i < this.size.x; i++) {
			json.writeArrayStart();
			for (int j = 0; j < this.size.y; j++) {
				json.writeValue(tiles.get(i).get(j).getId());
			}
			json.writeArrayEnd();
		}
		json.writeArrayEnd();
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		// Instantiate the tile array.
		this.tiles = new Array<Array<Tile>>();
		
		// Create size and tile size.
		this.size = new Vector2(jsonData.child.child.asInt(), jsonData.child.child.next.asInt());
		this.tileSize = new Vector2(jsonData.child.next.child.asFloat(), jsonData.child.next.child.next.asFloat());
		
		// Create tiles according to size read.
		createTiles((int) this.size.x, (int) this.size.y, 0);
		
		// Move to the content of the map.
		JsonValue currentline = jsonData.child.next.next;
		
		// If this content exist,
		if (currentline != null && currentline.name.equals("content"))
		{
			// Then, browse all values to set IDs.
			currentline = currentline.child;
			for (int i = 0; i < this.size.x; i++) {
				int[] lineValues = currentline.asIntArray();
				
				for (int j = 0; j < this.size.y; j++) {
					setTileID(i, j, lineValues[j]);
				}
				currentline = currentline.next;
			}
		}
	}
	
	
	// Factory
	
	/**
	 * Create a map according to the specified JSON.
	 * @param jsonPath the path to the JSON file.
	 * @return the created map.
	 */
	public static Map loadMapFromJSON(String jsonPath, Tileset tileTileset) {
		Json json = new Json();
		
		// Load the file.
		FileHandle file = new FileHandle(jsonPath);
		
		// Extract Map from JSON file.
		Map createdMap = json.fromJson(Map.class, file);
		
		// Apply the tileset on the map.
		createdMap.setTileset(tileTileset);
		
		// return the map.
		return createdMap;
	}
}
