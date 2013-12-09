package characters;

import main.Engine;
import main.Shootable;

public class Duck extends Entity implements Shootable{
	protected int vx;
	protected int vy;
	
	public boolean hitted;
	public boolean inFloor;
	public boolean isBonus;
	
	public Duck(Engine engine, boolean bonus) {
		super(engine);
		isBonus = bonus;
		if (isBonus) {
			setSpriteNames(new String[]{"bonusduckright0.gif","bonusduckright1.gif", "bonusduckright2.gif"});
		} else {
			setSpriteNames(new String[]{"duckright0.gif","duckright1.gif", "duckright2.gif"});			
		}
		setFrameSpeed(35);
		hitted = false;
		inFloor = false;
	}
	
	public void act() {
		super.act();
		// Investigar como hacer que puedan ir horizontalmente también
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
		if(y<0 || y>Engine.HEIGHT-170){
			// Cambio de sentido
			vy = -vy;
			if (vy > 0) {
				System.out.println("Hacia abajo");
			} else {
				System.out.println("Hacia arriba");
			}
			
		}
	}
	
	public int getVx() {
		return vx;
	}
	
	public void setVx(int i) {
		vx = i;
	}
	public int getVy(){
		return vy;
	}
	
	public void setVy(int i) {
		vy = i;
	}
	public boolean getHitted() {
		return hitted;
	}
	public void setHitted(boolean h) {
		hitted = h;
	}
	
	public void setBonus(boolean b) {
		isBonus = b;
	}
	
	public boolean getBonus() {
		return isBonus;
	}
	
	public boolean isAtFloor() {
		return inFloor;
	}

	public void fall() {
		System.out.println("Estoy cayendo"+getY()+" "+(Engine.HEIGHT-170));
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
