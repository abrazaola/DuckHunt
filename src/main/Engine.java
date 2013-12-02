package main;

import java.awt.image.ImageObserver;

import aux.SpriteCache;

public interface Engine extends ImageObserver{
	public static final int WIDTH = 640;
	public static final int HEIGHT = 480;
	public static final int SPEED = 10;
	public SpriteCache getSpriteCache();
}
