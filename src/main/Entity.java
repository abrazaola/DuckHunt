package main;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import aux.SpriteCache;

public abstract class Entity{
	protected int x, y;
	protected int width, height;
	protected Rectangle area;
	protected String[] spriteNames;
	protected int currentFrame;
	
	protected int frameSpeed;
	protected int t;
	
	protected Engine engine;
	protected SpriteCache spriteCache;
	
	public Entity(Engine engine){
		this.engine = engine;
		spriteCache = engine.getSpriteCache();
	}
	
	public void paint(Graphics2D g){
		g.drawImage(spriteCache.getSprite(spriteNames[currentFrame]),x,y,engine);
		//System.out.println(currentFrame);
	}
	
	public int getX(){
		return x;
	}
	
	public void setX(int i){
		x = i;
	}
	
	public int getY(){
		return y;
	}
	
	public void setY(int i){
		y = i;
	}
	
	public Rectangle getArea(){
		return area;
	}
	
	public void setArea(Rectangle r){
		area = r;
	}
	
	public int getFrameSpeed(){
		return frameSpeed;  
	}
	public void setFrameSpeed(int i){
		frameSpeed = i; 
	}
	
	public void setSpriteNames(String[] names){
		spriteNames = names;
		height = 0;
		width = 0;
		for(int i=0; i<names.length; i++){
			BufferedImage image = spriteCache.getSprite(spriteNames[i]);
			height = Math.max(height, image.getHeight());
			width = Math.max(width, image.getWidth());
		}
	}
	
	public int getHeight(){
		return height;
	}
	
	public int getWidth(){
		return width;
	}
	
	public void setHeight(int i){
		height = i;
	}
	
	public void setWidth(int i){
		width = i;
	}
	
	public void act(){
		t++;
		if(t % frameSpeed == 0){
			t = 0;
			currentFrame = (currentFrame + 1)%spriteNames.length;
		}
	}
	
	public abstract void fall();	
}
