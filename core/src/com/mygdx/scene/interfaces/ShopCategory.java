package com.mygdx.scene.interfaces;

import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.mygdx.scene.item.InventoryItem;
import com.mygdx.scene.resources.GameTilesets;
import com.mygdx.scene.tileset.Tileset;

public class ShopCategory extends Group {
	private Array<ShopSlot> slots;
	
	private ButtonStyle normalStyle, selectedStyle;
	
	private Table mainTable;
	
	//private GameItems items;
	
	public ShopCategory(GameTilesets gameTilesets) {
		this.slots = new Array<ShopSlot>();
		//this.items = items;
		
		// Create the main table.
		this.mainTable = new Table();
		this.mainTable.setFillParent(true);
		addActor(this.mainTable);
		
		// Create slots textures.
		Tileset gameTileset = gameTilesets.getTileset("slot");
		Rectangle buttonBounds;
		
		buttonBounds = gameTileset.getTileBound(0);
		TextureRegion upRegion = new TextureRegion(gameTileset.getTexture(), (int)buttonBounds.x, (int)buttonBounds.y, (int)buttonBounds.width, (int)buttonBounds.height);
		NinePatch upPatch = new NinePatch(upRegion, 10, 10, 10, 10);
	    
		buttonBounds = gameTileset.getTileBound(1);
		TextureRegion overRegion = new TextureRegion(gameTileset.getTexture(), (int)buttonBounds.x, (int)buttonBounds.y, (int)buttonBounds.width, (int)buttonBounds.height);
		NinePatch overPatch = new NinePatch(overRegion, 10, 10, 10, 10);
	    
		buttonBounds = gameTileset.getTileBound(2);
		TextureRegion downRegion = new TextureRegion(gameTileset.getTexture(), (int)buttonBounds.x, (int)buttonBounds.y, (int)buttonBounds.width, (int)buttonBounds.height);
		NinePatch downPatch = new NinePatch(downRegion, 10, 10, 10, 10);
		
		buttonBounds = gameTileset.getTileBound(3);
		TextureRegion selectedRegion = new TextureRegion(gameTileset.getTexture(), (int)buttonBounds.x, (int)buttonBounds.y, (int)buttonBounds.width, (int)buttonBounds.height);
		NinePatch selectedPatch = new NinePatch(selectedRegion, 10, 10, 10, 10);
		
		// Create slot's style and Apply texture on it.
		this.normalStyle = new TextButtonStyle();
		this.normalStyle.up = new NinePatchDrawable(upPatch);
		this.normalStyle.over = new NinePatchDrawable(overPatch);
		this.normalStyle.down = new NinePatchDrawable(downPatch);
		
		this.selectedStyle = new TextButtonStyle();
		this.selectedStyle.up = new NinePatchDrawable(selectedPatch);
		this.selectedStyle.over = new NinePatchDrawable(overPatch);
		this.selectedStyle.down = new NinePatchDrawable(selectedPatch);
	}
	
	
	// Items methods
	
	public void addItem(InventoryItem item, int price) {
		this.slots.add(new ShopSlot(this.normalStyle, this.selectedStyle) );
		this.slots.get(this.slots.size - 1).setItem(item, price);
		
		this.mainTable.add(this.slots.get(this.slots.size - 1)).width(200).height(70);
		this.mainTable.row();
		
		setHeight(70*this.slots.size);
	}
}
