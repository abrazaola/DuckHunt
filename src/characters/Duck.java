package characters;

import main.Engine;
import main.Entity;
import main.Shootable;

public class Duck extends Entity implements Shootable{
	protected int vx;
	protected int vy;
	
	public boolean hitted;
	public boolean inFloor;
	public boolean isBonus;
	
	public Duck(Engine engine, boolean bonus) {
		super(engine);
		setSpriteNames(new String[]{"duck0.gif","duck1.gif"});
		setFrameSpeed(35);
		hitted = false;
		inFloor = false;
		isBonus = bonus;
	}
	
	public void act(){
		super.act();
		getArea().setLocation(x, y);
		x+=vx;
		if(x<0 || x>Engine.WIDTH){
			vx = -vx;			
		}
		y+=vy;
		if(y<0 || y>Engine.HEIGHT-170){
			vy = -vy;			
		}
	}
	
	public int getVx(){
		return vx;
	}
	
	public void setVx(int i){
		vx = i;
	}
	public int getVy(){
		return vy;
	}
	
	public void setVy(int i){
		vy = i;
	}
	public boolean getHitted(){
		return hitted;
	}
	public void setHitted(boolean h){
		hitted = h;
	}
	
	public void setBonus(boolean b){
		isBonus = b;
	}
	
	public boolean getBonus(){
		return isBonus;
	}
	
	public boolean isAtFloor(){
		return inFloor;
	}

	public void fall() {
		System.out.println("Estoy cayendo"+getY()+" "+(Engine.HEIGHT-170));
		if(getY()<(Engine.HEIGHT-170)){
			//Cayendo...
			setSpriteNames(new String[]{"duckfall1.gif","duckfall2.gif"});
			System.out.println("Sigo cayendo");
			y+=3;			
		}else{
			//Toca el suelo
			System.out.println("Lelgue al suelo");
			vy = 0;
			inFloor = true;
		}
	}

	public void shooted() {
		vx = 0;
		vy = 0;
		setSpriteNames(new String[]{"duckshot.gif","duckshot.gif" });
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
