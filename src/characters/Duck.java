package characters;

import main.Engine;
import main.Shootable;

/**
 * Clase descendiente de Entity que representa un pato en el juego
 * @author aitor
 *
 */
public class Duck extends Entity implements Shootable{
	protected int vx;
	protected int vy;
	
	public boolean hitted;
	public boolean inFloor;
	public boolean isBonus;
	
	/**
	 * Constructor
	 * @param engine Frame con interfaz engine
	 * @param bonus Indica si el pato contendrá bonificación extra
	 */
	public Duck(Engine engine, boolean bonus) {
		super(engine);
		isBonus = bonus;
		if (isBonus) {
			setSpriteNames(new String[]{"bonusduckupright0.gif","bonusduckupright1.gif", "bonusduckupright2.gif"});
		} else {
			setSpriteNames(new String[]{"duckupright0.gif","duckupright1.gif", "duckupright2.gif"});			
		}
		setFrameSpeed(35);
		hitted = false;
		inFloor = false;
	}
	
	/* (non-Javadoc)
	 * @see characters.Entity#act()
	 */
	public void act() {
		super.act();
		// Investigar como hacer que puedan ir horizontalmente tambi�n
		getArea().setLocation(x, y);
		x+=vx;
		if(x<0 || x>Engine.WIDTH){
			// Cambio de sentido
			vx = -vx;
			if (vx > 0) {
				System.out.println("Hacia la derecha");
			} else {
				System.out.println("Hacia la izquierda");
			}
		}
		y+=vy;
		if(y<0 || y>Engine.HEIGHT-190){
			// Cambio de sentido
			vy = -vy;
			if (vy > 0) {
				System.out.println("Hacia abajo");
			} else {
				System.out.println("Hacia arriba");
			}
			
		}
	}
	
	/**
	 * Detiene el movimiento del pato
	 */
	public void stop() {
		vx = 0;
		vy = 0;
	}
	
	/**
	 * Obtiene la velocidad con la que se mueve en el eje X
	 * @return
	 */
	public int getVx() {
		return vx;
	}
	
	/**
	 * Establece la velocidad con la que se mueve en el eje X
	 * @param i
	 */
	public void setVx(int i) {
		vx = i;
	}
	/**
	 * Obtiene la velocidad con la que se mueve en el eje Y
	 * @return
	 */
	public int getVy(){
		return vy;
	}
	
	/**
	 * Establece la velocidad con la que se mueve en el eje Y
	 * @param i
	 */
	public void setVy(int i) {
		vy = i;
	}
	/* (non-Javadoc)
	 * @see main.Shootable#getHitted()
	 */
	public boolean getHitted() {
		return hitted;
	}
	/* (non-Javadoc)
	 * @see main.Shootable#setHitted(boolean)
	 */
	public void setHitted(boolean h) {
		hitted = h;
	}
	
	/**
	 * Establece si el pato es bonus o no
	 * @param b
	 */
	public void setBonus(boolean b) {
		isBonus = b;
	}
	
	/* (non-Javadoc)
	 * @see main.Shootable#getBonus()
	 */
	public boolean getBonus() {
		return isBonus;
	}
	
	/* (non-Javadoc)
	 * @see main.Shootable#isAtFloor()
	 */
	public boolean isAtFloor() {
		return inFloor;
	}

	/* (non-Javadoc)
	 * @see characters.Entity#fall()
	 */
	public void fall() {
		System.out.println("Estoy cayendo"+getY()+" "+(Engine.HEIGHT-190));
		if (getY()<(Engine.HEIGHT-170)) {
			//Cayendo...
			if (isBonus) {
				setSpriteNames(new String[]{"bonusduckfall1.gif","bonusduckfall2.gif", "bonusduckfall2.gif"});
			} else {
				setSpriteNames(new String[]{"duckfall1.gif","duckfall2.gif", "duckfall2.gif"});				
			}
			System.out.println("Sigo cayendo");
			y+=3;			
		} else {
			//Toca el suelo
			System.out.println("Lelgue al suelo");
			vy = 0;
			inFloor = true;
		}
	}

	/* (non-Javadoc)
	 * @see main.Shootable#shooted()
	 */
	public void shooted() {
		vx = 0;
		vy = 0;
		if (isBonus) {
			setSpriteNames(new String[]{"bonusduckshot.gif","bonusduckshot.gif", "bonusduckshot.gif" });
		} else {
			setSpriteNames(new String[]{"duckshot.gif","duckshot.gif", "duckshot.gif" });			
		}
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
