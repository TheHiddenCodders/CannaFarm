package com.mygdx.scene.map;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.mygdx.scene.tileset.Tileset;

public class Tile extends Actor {
	/** A reference to the tileset. */
	Tileset tileset;
	
	/** The {@link TextureRegion} allowing to display the tile. */
	TextureRegion tile;
	
	/** The tile's id. */
	int id;
	
	
	// Construction.
	
	/**
	 * Create a tile with a specified id.
	 * @param id the tile's id to set.
	 */
	public Tile (int id) {
		this.tile = new TextureRegion();
		this.id = id;
	}

	
	// Edition management.
	
	/**
	 * Change the tile bounds according to the stored id.
	 */
	private void computeTileRegion() {
		Rectangle tileRectangle = tileset.getTileBound(this.id);
		
		// Change the region of the texture region according to the tileset.
		tile.setRegionX((int) tileRectangle.x);
		tile.setRegionY((int) tileRectangle.y);
		tile.setRegionWidth((int) tileRectangle.width);
		tile.setRegionHeight((int) tileRectangle.height);
		
		setOriginX(tileRectangle.width / 2);
	}
	
	
	// Tileset methods
	
	/**
	 * Set the tile's tileset and recompute the region to display it properly.
	 * @param tileset
	 */
	public void setTileset(Tileset tileset) {
		this.tileset = tileset;
		this.tile.setTexture(this.tileset.getTexture());
		computeTileRegion();
	}
	
	
	// Id methods.
	
	/**
	 * @return the tile's id.
	 */
	public int getId() {
		return this.id;
	}
	
	/**
	 * The tile id to set.
	 * @param id to set.
	 */
	public void setID(int id) {
		this.id = id;
		idChanged();
	}
	
	/**
	 * Call when the id change.
	 */
	protected void idChanged() {
		computeTileRegion();
	}
	
	
	// Dispose
	
	public void dispose() {
		this.tileset.dispose();
	}
	
	
	// Transformation overriding
	
	@Override
	public void setPosition(float x, float y) {
		super.setPosition(x - getOriginX(), y - getOriginY());
	}
	
	@Override
	public void setOrigin(float originX, float originY) {
		float relativeOriginX = getOriginX() - originX;
		float relativeOriginY = getOriginY() - originY;
		
		super.setOrigin(originX, originY);
		super.setPosition(getX() - relativeOriginX, getY() - relativeOriginY);
	}
	
	@Override
	public void setOriginX(float originX) {
		setOrigin(originX, getOriginY());
	}
	
	@Override
	public void setOriginY(float originY) {
		setOrigin(getOriginX(), originY);
	}

	
	// Actor inherit methods.
	
	@Override
	public Actor hit (float x, float y, boolean touchable) {
		
		if (touchable && getTouchable() != Touchable.enabled) return null;
		
		// Check if cursor is in the bottom-left corner
		if (x < getWidth() / 2 && y < getHeight() / 2) {
			// Line eq : y = (-0.5)x + height/2
			// Check if cursor above the line
			return y > x*-0.5f+getHeight()/2  ? this : null;
		}
		// Check if cursor is in the upper-left corner  
		else if (x < getWidth() / 2 && y > getHeight() / 2) {
			// Line eq : y = (0.5)x + height/2
			// Check if cursor under the line
			return y < x*0.5f+getHeight()/2  ? this : null;
		}
		// Check if the cursor is in the bottom-right corner
		else if (x > getWidth() / 2 && y < getHeight() / 2) {
			// Line eq : y = (0.5)x - height/2
			// Check if cursor above the line
			return y > x*0.5f-getHeight()/2  ? this : null;
		}
		
		// Else the cursor is in the upper-right corner
		// Line eq : y = (-0.5)x + height*3/2
		// Check if cursor under the line
		return y < x*-0.5f+getHeight()*3/2  ? this : null;
	}
	
	@Override
	public void act(float delta) {
	}
	
	@Override
	public void draw(Batch batch, float parentAlpha) {
		batch.draw(tile, getX(), getY(), getOriginX(), getOriginY(),
				getWidth(), getHeight(), getScaleX(), getScaleY(),
				getRotation());
	}
}
