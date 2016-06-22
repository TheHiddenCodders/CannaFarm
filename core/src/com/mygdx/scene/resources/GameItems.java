package com.mygdx.scene.resources;

import java.util.HashMap;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Json.Serializable;
import com.badlogic.gdx.utils.JsonValue;

public class GameItems implements Serializable {
	/** The map storing all item's JSON file paths. */
	private HashMap<String, String> itemsPaths;
	
	
	// Construction
	
	/**
	 * Constructor. Create the map, use for {@link Serializable}.
	 */
	public GameItems() {
		itemsPaths = new HashMap<String, String>();
	}
	
	
	// Information returning
	
	/**
	 * @param name the wanted item's name 
	 * @return the path to the JSON file of the specified item.
	 */
	public String getItemPath(String name) {
		return itemsPaths.get(name);
	}
	
	
	// JSON methods
	
	@Override
	public void write(Json json) {
		for (String key : itemsPaths.keySet()) {
			json.writeValue(key, itemsPaths.get(key));
		}
	}
	
	@Override
	public void read(Json json, JsonValue jsonData) {
		jsonData = jsonData.child;
		System.out.println("loading items:");
		
		while (jsonData != null) {
			System.out.println(jsonData.name + " at " + jsonData.asString());
			itemsPaths.put(jsonData.name, jsonData.asString());
			
			jsonData = jsonData.next;
		} 
	}
	
	
	// Factory
	
	/**
	 * Create a game item according to the specified JSON.
	 * @param jsonPath the path to the JSON file.
	 * @return the created game item.
	 */
	public static GameItems loadFromJSON(String jsonPath) {
		Json json = new Json();
		
		// Load the file.
		FileHandle file = new FileHandle(jsonPath);
		
		// Extract the GameItem from JSON file.
		GameItems createdItems = json.fromJson(GameItems.class, file);
		
		// return it.
		return createdItems;
	}
}
