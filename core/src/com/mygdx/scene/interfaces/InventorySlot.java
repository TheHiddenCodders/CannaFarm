package com.mygdx.scene.interfaces;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.mygdx.scene.item.InventoryItem;

public class InventorySlot extends Button {
	/** A reference to the item store in the slot. */
	private InventoryItem item;
	
	/** The quantity of item store in the slot. */
	private int quantity;
	
	/** Tell if the slot is selected are not. */
	private boolean selected;
	
	/** Styles to use on the slot.*/
	private ButtonStyle normalStyle, selectedStyle;
	
	/** Style apply on title */
	private LabelStyle quantityStyle;
	
	/** The title */
	private Label quantityLabel;
	
	
	// Construction
	
	/**
	 * Constructor. Initialize the slot with styles.
	 * @param normalStyle the style to apply when the slot is not selected.
	 * @param selectedStyle the style to apply when the slot is selected.
	 */
	public InventorySlot(ButtonStyle normalStyle, ButtonStyle selectedStyle) {
		super(normalStyle);
		
		// Memorize styles
		this.normalStyle = normalStyle;
		this.selectedStyle = selectedStyle;
				
		// Create the label to display quantity
		this.quantity = 0;
		
		BitmapFont quantityFont = new BitmapFont(new FileHandle("fonts/arial.fnt"));
		this.quantityStyle = new LabelStyle();
		this.quantityStyle.font = quantityFont;
		
		this.quantityLabel = new Label(String.valueOf(this.quantity), this.quantityStyle);
		this.quantityLabel.setFontScale(0.5f);
		
		// Initialize item. 
		this.item = null;
		this.selected = false;
		
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
	
	
	// Quantity methods
	
	/**
	 * @return the quantity of the stored item.
	 */
	public int getQuantity() {
		return this.quantity;
	}
	
	
	// Item methods.
	
	/**
	 * @return true if the slot is empty, false otherwise.
	 */
	public boolean isEmpty() {
		return quantity==0?true:false;
	}
	
	/**
	 * @return the item stored in the slot. May be null.
	 */
	public InventoryItem getItem() {
		return this.item;
	}
	
	/**
	 * Add the specified item in the slot. 
	 * If the specified item already exist in the slot, increment the quantity. 
	 * Do nothing otherwise.
	 * @param item the item to add in the solt.
	 */
	public void addItem(InventoryItem item, int quantity) {
		// Check if the slot is empty
		if (this.item == null) {
			// Add the item into the slot. Replace it on the screen
			this.item = item;
			this.quantity = quantity;
		}
		// Else, cjheck if the item already exist in the slot.
		else if (this.item.equals(item)) {
			this.quantity += quantity;
		}
		
		replaceItem();
	}
	
	/**
	 * Remove the item from the slot.
	 */
	public void removeItem() {
		removeActor(this.item);
		removeActor(this.quantityLabel);
		this.item = null;
		this.quantity = 0;
	}
	
	/**
	 * Replace the item on the screen.
	 */
	public void replaceItem() {
		removeActor(this.item);
		removeActor(this.quantityLabel);
		
		this.item.setSize(32, 32);
		this.item.setPosition(3, 3);
		
		this.quantityLabel.setText(String.valueOf(this.quantity));
		
		addActor(this.item);
		addActor(this.quantityLabel);
	}
	
	// Button inherit methods.
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		super.draw(batch, parentAlpha);
	}
}
