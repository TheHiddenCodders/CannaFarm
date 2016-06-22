package com.mygdx.scene.inventory;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Button.ButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Array;
import com.mygdx.scene.item.InventoryItem;
import com.mygdx.scene.resources.GameItems;
import com.mygdx.scene.resources.GameTilesets;
import com.mygdx.scene.tileset.Tileset;

public class Inventory extends Group {
	/** All game's tilesets */
	private GameTilesets gameTilesets;
	
	/** All game items */
	private GameItems gameItems;
	
	
	/** The number of slot to display */
	private int slotNumberX, slotNumberY;
	
	/** The slot's size to display */
	private int slotSizeX, slotSizeY;
	
	/** The title size */
	private int titleHeight;
	
	
	/** Styles apply on slots */ 
	private ButtonStyle normalStyle, selectedStyle;
	
	/** Style apply on title */
	private LabelStyle titleStyle;
	
	
	/** UI tables. */
	private Table mainTable, slotTable;
	
	/** The title */
	private Label title;
	
	/** All slots of the inventory */
	private Array<Array<Slot>> slots;
	
	
	// Construction
	
	/**
	 * Constructor.
	 * @param gameTilesets
	 * @param gameItems
	 */
	public Inventory(GameTilesets gameTilesets, GameItems gameItems) {
		super();
		
		// Memorize reference on game resources.
		this.gameTilesets = gameTilesets;
		this.gameItems = gameItems;
		
		// Define interface attributes.
		this.slotNumberX = 9;
		this.slotNumberY = 6;
		
		this.slotSizeX = 40;
		this.slotSizeY = 40;
		
		this.titleHeight = 33;
		
		this.slots = new Array<Array<Slot>>();
		
		// Create the components.
		setSize(this.slotSizeX*this.slotNumberX + 30, this.slotSizeY*this.slotNumberY + this.titleHeight + 10 + 30);
		createStyle();
		createUI();
		
		// Add a listener to manage the drag of the interface and items.
		addListener(new InventoryListener(this));
		
		slots.get(0).get(0).addItem(InventoryItem.loadItemFromJSON(this.gameItems.getItemPath("pot"), gameTilesets.getTileset("inventoryItem")));
		slots.get(1).get(0).addItem(InventoryItem.loadItemFromJSON(this.gameItems.getItemPath("sofa"), gameTilesets.getTileset("inventoryItem")));
		slots.get(0).get(1).addItem(InventoryItem.loadItemFromJSON(this.gameItems.getItemPath("pot"), gameTilesets.getTileset("inventoryItem")));
	}
	
	/**
	 * Create all style use in this interface
	 */
	private void createStyle() {
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
	
		// Create font texture for title.
		BitmapFont titleFont = new BitmapFont(new FileHandle("fonts/arial.fnt"));
		
		// Create slot's style and Apply texture on it.
		this.normalStyle = new TextButtonStyle();
		this.normalStyle.up = new NinePatchDrawable(upPatch);
		this.normalStyle.over = new NinePatchDrawable(overPatch);
		this.normalStyle.down = new NinePatchDrawable(downPatch);
		
		this.selectedStyle = new TextButtonStyle();
		this.selectedStyle.up = new NinePatchDrawable(selectedPatch);
		this.selectedStyle.over = new NinePatchDrawable(overPatch);
		this.selectedStyle.down = new NinePatchDrawable(selectedPatch);
		
		// Create title style.
		this.titleStyle = new LabelStyle();
		this.titleStyle.font = titleFont;
	}
	
	/**
	 * Create all UI components.
	 */
	private void createUI() {
		// Create the background image
		Pixmap pixmap = new Pixmap(1, 1, Format.RGBA8888);
		pixmap.setColor(0.32f, 0.36f, 0.36f, 1.f);
		pixmap.fill();
		
		Image background = new Image(new Texture(pixmap));
		background.setSize(getWidth(), getHeight());
		addActor(background);
				
		// Create title
		this.title = new Label("INVENTAIRE", this.titleStyle);
		this.title.setHeight(33);
		
		// Create the main table.
		this.mainTable = new Table();
		this.mainTable.setFillParent(true);
		addActor(this.mainTable);
		
		// Add title to the UI.
		this.mainTable.add(title).height(title.getHeight() + 10);
		
		// Create slot table.
		this.mainTable.row();
		this.slotTable = new Table();
		this.mainTable.add(this.slotTable);
		
		// Create and add slots to the UI.
		for (int i = 0; i < this.slotNumberY; i++) {
			this.slots.add(new Array<Slot>());
			
			for (int j = 0; j < this.slotNumberX; j++) {
				this.slots.get(i).add(new Slot(this.normalStyle, this.selectedStyle));
				this.slotTable.add(this.slots.get(i).get(j)).width(this.slotSizeX).height(this.slotSizeY);
			}
			
			this.slotTable.row();
		}
	}
	
	
	// Slots methods
	
	/**
	 * @param positionX horizontal cursor position.
	 * @param positionY vertical cursor position.
	 * @return the slot pointed by cursor. Null if any.
	 */
	public Slot getSlotFromCursor(float positionX, float positionY) {
		// Compute the pointed slot position.
		int slotPositionX = (int) ((positionX - 15) / this.slotSizeX);
		int slotPositionY = (this.slotNumberY - 1) - (int) ((positionY - 15) / this.slotSizeY);
		
		// Check if the cursor point a slot.
		if (slotPositionX >= 0 && slotPositionX < this.slotNumberX) {
			if (slotPositionY >= 0 && slotPositionY < this.slotNumberY) {
				return slots.get(slotPositionY).get(slotPositionX);
			}
		}
		
		return null;
	}
	
	/**
	 * @param positionX horizontal slot position.
	 * @param positionY vertical slot position.
	 * @return the slot in specified position.
	 */
	public Slot getSlotFromPosition(int positionX, int positionY) {
		// check if a slot exist to the specified position.
		if (positionX >= 0 && positionX < this.slotNumberX) {
			if (positionY >= 0 && positionY < this.slotNumberY) {
				return slots.get(positionY).get(positionX);
			}
		}
		
		return null;
	}
	
	/**
	 * @return the horizontal number of slots.
	 */
	public int getSlotNumberX() {
		return this.slotNumberX;
	}
	
	/**
	 * @return the vertical number of slots.
	 */
	public int getSlotNumberY() {
		return this.slotNumberY;
	}
}
