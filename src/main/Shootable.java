package main;

/**
 * Interfaz que implementan todos los objetos a los que se les puede disparar en el juego
 * @author aitor
 *
 */
public interface Shootable {
	public boolean hitted = false;
	
	/**
	 * Se obtiene si se ha disparado
	 * @return
	 */
	public boolean getHitted();
	/**
	 * Se especifica si se ha disparado sobre el objeto que lo implementa
	 * @param h Estado de el disparo
	 */
	public void setHitted(boolean h);
	/**
	 * Se obtiene si el objeto se encuentra ya en el suelo
	 * @return
	 */
	public boolean isAtFloor();
	/**
	 * Se obtiene si en el instante al que se llama a este método se ha disparado sobre el objeto
	 * que lo implemente
	 */
	public void shooted();
	/**
	 * El objeto que implementa esta interfaz realiza la actividad programada de movimiento en el juego
	 */
	public void act();
	/**
	 * El objeto que implementa esta interfaz comienza a caer al suelo
	 */
	public void fall();
	/**
	 * Forma de saber si el objeto que implementa esta interfaz tiene puntuación adicional
	 * @return
	 */
	public boolean getBonus();
}
