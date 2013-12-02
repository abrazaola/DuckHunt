package main;

public interface Shootable {
	public boolean hitted = false;
	
	public boolean getHitted();
	public void setHitted(boolean h);
	public boolean isAtFloor();
	public void shooted();
	public void act();
	public void fall();
	public boolean getBonus();
}
