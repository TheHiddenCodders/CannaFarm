package com.mygdx.scene.resources;

import java.util.HashMap;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;
import com.mygdx.scene.tileset.Tileset;

public class GameTilesets implements Serializable {
	/** The map storing all games's tilesets. */
	private HashMap<String, Tileset> tilesets;
	
	/** The map storing all tileset's paths. */
	private HashMap<String, String> tilesetsPaths;
	
	
	// Construction 
	
	/**
	 * Constructor. Create maps, use for {@link Serializable}.
	 */
	public GameTilesets() {
		tilesets = new HashMap<String, Tileset>();
		tilesetsPaths = new HashMap<String, String>();
	}
	
	
	// Information returning
	
	/**
	 * @param name the wanted tileset's name.
	 * @return a reference to the specified tileset.
	 */
	public Tileset getTileset(String name) {
		return tilesets.get(name);
	}
	
	
	// JSON methods

	@Override
	public void write(Json json) {
		for (String key : tilesets.keySet()) {
			json.writeValue(key, tilesetsPaths.get(key));
		}
	}

	@Override
	public void read(Json json, JsonValue jsonData) {
		jsonData = jsonData.child;
		System.out.println("loading tilesetes:");
		
		while (jsonData != null) {
			System.out.println(jsonData.name + " at " + jsonData.asString());
			tilesetsPaths.put(jsonData.name, jsonData.asString());
			tilesets.put(jsonData.name, Tileset.loadTilesetFromJSON(tilesetsPaths.get(jsonData.name)));
			
			jsonData = jsonData.next;
		} 
	}
	
	
	// Factory
	
	/**
	 * Create an game tileset according to the specified JSON.
	 * @param jsonPath the path to the JSON file.
	 * @return the created game tileset.
	 */
	public static GameTilesets loadFromJSON(String jsonPath) {
		Json json = new Json();
		
		// Load the file.
		FileHandle file = new FileHandle(jsonPath);
		
		// Extract GameTilesets from JSON file.
		GameTilesets createdTilesets = json.fromJson(GameTilesets.class, file);
		
		// return it.
		return createdTilesets;
	}
}
