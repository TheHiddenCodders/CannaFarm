package com.mygdx.scene.interfaces;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.mygdx.scene.item.InventoryItem;

public class ShopSlot extends Button {
	/** A reference to the item store in the slot. */
	private InventoryItem item;
	
	private int price;
	
	/** Tell if the slot is selected are not. */
	private boolean selected;
	
	
	/** Styles to use on the slot.*/
	private ButtonStyle normalStyle, selectedStyle;
	
	/** Style apply on title */
	private LabelStyle nameStyle;
	
	/** The title */
	private Label nameLabel, priceLabel;
	
	private Table mainTable;
	private Cell<InventoryItem> itemCell;
	
	// Construction
	
	public ShopSlot(ButtonStyle normalStyle, ButtonStyle selectedStyle) {
		super(normalStyle);
		
		// Memorize styles
		this.normalStyle = normalStyle;
		this.selectedStyle = selectedStyle;
		
		BitmapFont nameFont = new BitmapFont(new FileHandle("fonts/arial.fnt"));
		this.nameStyle = new LabelStyle();
		this.nameStyle.font = nameFont;
		
		this.nameLabel = new Label("", this.nameStyle);
		this.nameLabel.setFontScale(0.5f);
		
		this.priceLabel = new Label("0 CG", this.nameStyle);
		this.priceLabel.setFontScale(0.4f);
		
		// Initialize item. 
		this.item = null;
		this.selected = false;
		this.price = 0;
		
		// Create the main table.
		this.mainTable = new Table();
		this.mainTable.setFillParent(true);
		addActor(this.mainTable);
		
		this.itemCell = this.mainTable.add(this.item).width(32).height(32);
		this.mainTable.add(this.nameLabel).expandX();
		this.mainTable.row();
		this.mainTable.add(this.priceLabel).colspan(2).right().expandY();
		
		// Add a listener to manage slot's selection
		addListener(new ActorGestureListener() {
			public void tap(InputEvent event, float x, float y, int count, int button) {
				if (isSelected()) {
					deselect();
				}
				else {
					select();
				}
		    }
		});
	}
	
	
	// Selection methods
	
	/**
	 * Select the slot applying the correct style.
	 */
	public void select() {
		this.selected = true;
		setStyle(selectedStyle);
	}
	
	/**
	 * Deselect the slot applying the correct style.
	 */
	public void deselect() {
		this.selected = false;
		setStyle(normalStyle);
	}
	
	/**
	 * @return true if the slot is selected, false otherwise.
	 */
	public boolean isSelected() {
		return this.selected;
	}
	
	
	// Items methods
	
	public void setItem(InventoryItem item, int price) {
		if (this.item != null) {
			removeActor(this.item);
		}
		
		this.price = price;
		this.item = item;
		this.nameLabel.setText(item.getTitle());
		this.priceLabel.setText(String.valueOf(this.price) + " CG");
		
		
		this.itemCell.setActor(this.item).width(32).height(32).pad(10);
	}
}
