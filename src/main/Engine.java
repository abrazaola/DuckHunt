package main;

import java.awt.image.ImageObserver;

import aux.SpriteCache;

/**
 * Interfaz que implementan los Frames sobre los que se va a realizar un juego
 * @author aitor
 *
 */
public interface Engine extends ImageObserver{
	public static final int WIDTH = 800;
	public static final int HEIGHT = 600;
	public static final int SPEED = 10;
	/**
	 * Obtienes el objeto caché de imágenes
	 * @return
	 */
	public SpriteCache getSpriteCache();
}
