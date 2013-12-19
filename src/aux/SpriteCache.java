package aux;

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;

public class SpriteCache {
	private HashMap<String, BufferedImage> sprites;
	
	public SpriteCache(){
		sprites = new HashMap<String, BufferedImage>();
	}
	
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
	
	public BufferedImage getSprite(String nombre){
		BufferedImage img=(BufferedImage)sprites.get(nombre);
		
		if(img==null){
			img=loadImage("res/"+nombre);
			sprites.put(nombre, img);
		}
		return img;
	}
}
