package com.gmail.robmadeyou.draw;

import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.TextureLoader;

import com.gmail.robmadeyou.ABLayer;
import com.gmail.robmadeyou.Abereth;
import com.gmail.robmadeyou.effects.ABColor;
import com.gmail.robmadeyou.effects.ABTextureLoader;
import com.gmail.robmadeyou.gui.ABGui;

public abstract class ABDrawable {
	
	private Vector2f coord;
	private Vector2f dimension;
	private int layer;
	private int texture;
	private ABColor color;
	private float opacity;
	private boolean useTranslate;
	private long id;
	boolean inverts;
	public ABDrawable(Vector2f coord, Vector2f dimension){
		this.coord = coord;
		this.dimension = dimension;
		this.layer = ABLayer.GUILayer();
		this.opacity = 1f;
		this.color = ABColor.White;
		this.texture = -1;
		this.useTranslate = false;
		this.id = Abereth.id;
		this.inverts = false;
		Abereth.id++;
	}
	
	/*
	 * Getters
	 */
	
	public float getDrawX(){
		return coord.x;
	}
	public float getDrawY(){
		return coord.y;
	}
	public float getDrawWidth(){
		return dimension.x;
	}
	public float getDrawHeight(){
		return dimension.y;
	}
	public int getLayer(){
		return layer;
	}
	public int getTexture(){
		return texture;
	}
	public Image getTextureAsImage(){
		Image image = null;
		try{
			image = new Image(ABTextureLoader.TextureInfo.get(getTexture()).getLocation());
		}catch(Exception e){}
		return image;
	}
	public ABColor getColor(){
		return color;
	}
	public float getOpacity(){
		return opacity;
	}
	public boolean getUseTranslate(){
		return useTranslate;
	}
	public long getID(){
		return id;
	}
	public boolean getInverts(){
		return inverts;
	}
	
	/*
	 * Setters
	 */
	
	public void setDrawX(float x){
		this.coord.x = x;
	}
	public void setDrawY(float y){
		this.coord.y = y;
	}
	public void setDrawWidth(float width){
		this.dimension.x = width;
	}
	public void setDrawHeight(float height){
		this.dimension.y = height;
	}
	public void setDrawLayer(int layer){
		this.layer = layer;
	}
	public void setTexture(int texture){
		this.texture = texture;
	}
	public void setColor(ABColor color){
		this.color = color;
	}
	public void setOpacity(float opacity){
		this.opacity = opacity;
	}
	public void setUseTranslate(boolean args){
		this.useTranslate = args;
	}
	public void setInverts(boolean args){
		this.inverts = args;
	}

	
	/*
	 * Casuals
	 */
	
	protected void updateDrawValues(Vector2f coord, Vector2f dimension){
		this.coord = coord;
		this.dimension = dimension;
	}
	
	
	public abstract void draw();
}