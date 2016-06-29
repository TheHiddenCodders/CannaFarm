package com.mygdx.scene.interfaces;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.mygdx.scene.item.InventoryItem;
import com.mygdx.scene.resources.GameItems;
import com.mygdx.scene.resources.GameTilesets;
import com.mygdx.scene.tileset.Tileset;

public class Shop extends Group {
	/** All game's tilesets */
	private GameTilesets gameTilesets;
	
	/** All game items */
	private GameItems gameItems;
	
	
	/** Style apply on title */
	private LabelStyle titleStyle;
	
	/** Styles apply on slots */ 
	private ButtonStyle normalStyle, selectedStyle;
	
	
	/** The title */
	private Label title;
	
	private Table mainTable;
	
	
	private ShopCategory categories;
	
	
	// Conxtruction
	
	public Shop(GameTilesets gameTilesets, GameItems gameItems) {
		super();
		
		// Memorize reference on game resources.
		this.gameTilesets = gameTilesets;
		this.gameItems = gameItems;
		
		setSize(390, 313);
		createStyle();
		createUI();
		
		// add listener
	}
	
	
	private void createStyle() {
		// Create font texture for title.
		BitmapFont titleFont = new BitmapFont(new FileHandle("fonts/arial.fnt"));
				
		// Create title style.
		this.titleStyle = new LabelStyle();
		this.titleStyle.font = titleFont;
		
		Tileset gameTileset = this.gameTilesets.getTileset("slot");
		Rectangle buttonBounds;
	   
		// Create slots textures.
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
	
	private void createUI() {
		// Create the background image
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(0.32f, 0.36f, 0.36f, 0.8f);
		pixmap.fill();
		
		Image background = new Image(new Texture(pixmap));
		background.setSize(getWidth(), getHeight());
		addActor(background);
		
		// Create title
		this.title = new Label("MAGASIN", this.titleStyle);
		this.title.setHeight(33);
		
		// Create the main table.
		this.mainTable = new Table();
		this.mainTable.setFillParent(true);
		addActor(this.mainTable);
		
		// Add title to the UI.
		this.mainTable.add(title).height(title.getHeight() + 10).top();
		
		this.mainTable.row();
		
		// TEST
		this.categories = new ShopCategory(gameTilesets);
		this.categories.addItem(InventoryItem.loadItemFromJSON(this.gameItems.getItemPath("pot"), gameTilesets.getTileset("inventoryItem")), 10);
		this.categories.addItem(InventoryItem.loadItemFromJSON(this.gameItems.getItemPath("sofa"), gameTilesets.getTileset("inventoryItem")), 100);
		this.categories.addItem(InventoryItem.loadItemFromJSON(this.gameItems.getItemPath("pot"), gameTilesets.getTileset("inventoryItem")), 1000);
		
		// Change height according to number of item inside category
		this.mainTable.add(this.categories).height(this.categories.getHeight());
		// END TEST
	}
}
