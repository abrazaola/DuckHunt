package aux;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

/**
 * Clase encargada de mantener una caché de las imagenes usadas en el juego
 * @author aitor
 *
 */
public class SpriteCache {
	private HashMap<String, BufferedImage> sprites;
	
	/**
	 * Constructor
	 */
	public SpriteCache(){
		sprites = new HashMap<String, BufferedImage>();
	}
	
	/**
	 * Método de carga de imágen
	 * @param nombre Nombre del fichero que contiene la imágen
	 * @return
	 */
	private BufferedImage loadImage(String nombre){
		URL url=null;
		try {
			url = getClass().getClassLoader().getResource(nombre);
			return ImageIO.read(url);
		}catch (Exception e) {
			System.out.println("No se pudo cargar la imagen " + nombre +" de "+url);
			System.out.println("El error fue : "+e.getClass().getName()+" "+e.getMessage());
			System.exit(0);
			return null;
		}
	}
	
	/**
	 * Método público desde el que se solicita una imagen
	 * @param nombre Fichero imagen
	 * @return Imágen requerida
	 */
	public BufferedImage getSprite(String nombre){
		BufferedImage img=(BufferedImage)sprites.get(nombre);
		
		if(img==null){
			img=loadImage("res/"+nombre);
			sprites.put(nombre, img);
		}
		return img;
	}
}
