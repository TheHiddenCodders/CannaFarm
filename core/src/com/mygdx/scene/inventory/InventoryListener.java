package com.mygdx.scene.inventory;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.mygdx.scene.item.InventoryItem;

public class InventoryListener extends ActorGestureListener {
	/** A reference to the item to drag. Null if any. */
	private InventoryItem dragItem = null;
	
	/** A reference to the slot which the item coming from. Null if any. */
	private Slot initialSlot = null;
	
	
	/** A reference to the inventory. */
	private final Inventory inventory;
	
	
	// Construction
	
	/**
	 * Constructor.
	 * @param inventory a reference to the inventory creating this listener.
	 */
	public InventoryListener(final Inventory inventory) {
		this.inventory = inventory;
	}
	
	
	// Inherit methods.
	
	public void tap(InputEvent event, float x, float y, int count, int button) {
		// Deselect all slot except one which is pointed.
        for (int i = 0; i < this.inventory.getSlotNumberX(); i++) {
			for (int j = 0; j < this.inventory.getSlotNumberY(); j++) {
				if (!this.inventory.getSlotFromPosition(i, j).equals(getTouchDownTarget()))
					this.inventory.getSlotFromPosition(i, j).deselect();
			}
        }
    }
	
	public void touchDown(InputEvent event, float x, float y, int pointer, int button)  {
		// Memorize the pointed item and slot.
		if (getTouchDownTarget() instanceof InventoryItem) {
			// Get the pointed item ands slot
			this.initialSlot = this.inventory.getSlotFromCursor(x, y);
			this.dragItem = this.initialSlot.getItem();
			
			// Put the item as inventory actor.
			this.inventory.addActor(this.dragItem);
			this.dragItem.setPosition(x, y);
		}
	}
	
	public void touchUp(InputEvent event, float x, float y, int pointer, int button) {
		// Check if an item was drag
		if (this.initialSlot != null && this.dragItem != null) {
			// Get the pointed slot
			Slot newSlot = this.inventory.getSlotFromCursor(x, y);
			
			this.inventory.removeActor(this.dragItem);
			
			// Check if the cursor is on an empty slot.
			if (newSlot != null && newSlot.isEmpty()) {
				newSlot.addItem(this.dragItem, this.initialSlot.getQuantity());
				this.initialSlot.removeItem();
			}
			// Else, check if the cursor is on a new slot with the same item 
			else if (newSlot != null && newSlot != this.initialSlot && newSlot.getItem().equals(this.dragItem)) {
				newSlot.addItem(this.dragItem, this.initialSlot.getQuantity());
				this.initialSlot.removeItem();
			}
			// Else, replace the item properly in the slot.
			else {
				this.initialSlot.replaceItem();
			}
			
			this.initialSlot = null;
			this.dragItem = null;
		}
	}
	
	public void pan(InputEvent event, float x, float y, float deltaX, float deltaY) {
		// Check if an item is drag
		if (getTouchDownTarget() instanceof InventoryItem) {
				this.dragItem.setPosition(x, y);
		}
		// Check if a the dragged actor is not a slot.
		else if (!(getTouchDownTarget() instanceof Slot)) {
			this.inventory.setPosition(this.inventory.getX() + deltaX, this.inventory.getY() + deltaY);
		}
	}
	
	
	// drag methods.
	
	/**
	 * @return true if an item is dragged, false otherwise.
	 */
	public boolean isItemDrag() {
		if (this.dragItem != null && this.initialSlot != null) {
			return true;
		}
		
		return false;
	}
}
