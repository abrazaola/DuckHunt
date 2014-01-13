package characters;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import main.Engine;
import aux.SpriteCache;

/**
 * Clase que representa una entidad que interactúa con el jugador en el juego
 * @author aitor
 *
 */
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
	
	/**
	 * Constructor
	 * @param engine Objeto que implemente la interfaz Engine sobre el que va a representarse
	 * la entidad
	 */
	public Entity(Engine engine) {
		this.engine = engine;
		spriteCache = engine.getSpriteCache();
	}
	
	/**
	 * Cada entidad sabe pintarse a sí misma mediante este método
	 * @param g Graphics2D que se va a usar para el pintado
	 */
	public void paint(Graphics2D g) {
		g.drawImage(spriteCache.getSprite(spriteNames[currentFrame]),x,y,engine);
		//System.out.println(currentFrame);
	}
	
	/**
	 * Posición en el eje X
	 * @return
	 */
	public int getX() {
		return x;
	}
	
	/**
	 * Establece posición en el eje X
	 * @param i
	 */
	public void setX(int i) {
		x = i;
	}
	
	/**
	 * Posición en el eje Y
	 * @return
	 */
	public int getY() {
		return y;
	}
	
	/**
	 * Establece posición en el eje Y
	 * @param i
	 */
	public void setY(int i) {
		y = i;
	}
	
	/**
	 * Se obtiene el área que ocupa la entidad
	 * @return
	 */
	public Rectangle getArea() {
		return area;
	}
	
	/**
	 * Establece el área que ocupa la entidad
	 * @param r
	 */
	public void setArea(Rectangle r) {
		area = r;
	}
	
	/**
	 * Se obtiene el intervalo de ms que pasan cada vez que se actualiza el estado de la entidad
	 * @return
	 */
	public int getFrameSpeed(){
		return frameSpeed;  
	}
	/**
	 * Se establece el intervalo de ms que pasan cada vez que se actualiza el estado de la entidad
	 * @param i ms de refresco
	 */
	public void setFrameSpeed(int i) {
		frameSpeed = i; 
	}
	
	/**
	 * Carga los nombre de los sprites que pertenecen a la entidad
	 * @param names Array de nombres de los ficheros de sprites que usa la entidad
	 */
	public void setSpriteNames(String[] names) {
		spriteNames = names;
		height = 0;
		width = 0;
		for(int i=0; i<names.length; i++){
			BufferedImage image = spriteCache.getSprite(spriteNames[i]);
			height = Math.max(height, image.getHeight());
			width = Math.max(width, image.getWidth());
		}
	}
	
	/**
	 * Se obtiene la altura de la entidad
	 * @return
	 */
	public int getHeight() {
		return height;
	}
	
	/**
	 * Se obtiene la anchura de la entidad
	 * @return
	 */
	public int getWidth() {
		return width;
	}
	
	/**
	 * Se establece la altura
	 * @param i Altura
	 */
	public void setHeight(int i) {
		height = i;
	}
	
	/**
	 * Se establece la anchura
	 * @param i Anchura
	 */
	public void setWidth(int i) {
		width = i;
	}

	/**
	 * Comienza la animación de movimiento
	 */
	public void act(){
		t++;
		if(t % frameSpeed == 0) {
			t = 0;
			currentFrame = (currentFrame + 1)%spriteNames.length;
		}
	}
	
	/**
	 * Comienza la caída de la entidad al suelo
	 */
	public abstract void fall();	
}
