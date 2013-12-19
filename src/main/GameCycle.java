package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import windows.MainWindow;
import characters.Duck;
import characters.Entity;

public class GameCycle implements Runnable, MouseListener, KeyListener, MouseMotionListener {
	private final static int WEAPONBULLETS = 2;
	private int ducksForGame;
	private int stageNumber;
	private int calibre;
	private int remainBullets;
	private int deadDucks;
	private int fallingDucks;
	private long usedTime;
	private long stageTime;
	private long stageStartTime;
	private long challengeTime;
	
	private boolean gameOver;
	
	private boolean stopTimer;
	private ArrayList<Shootable> entities;
	
	private BufferedImage background;
	private BufferStrategy strategy;
	
	private MainWindow mainWindow;
	private Graphics2D g;
		
	public GameCycle(MainWindow mainWindow, int ducks) {
		this.mainWindow = mainWindow;
		ducksForGame = ducks;
	}
	
	private void initWorld() {
		//Configura los par�metros de inicio de el juego
		stageNumber = 1;
		challengeTime = 8000;
		gameOver = false;
		calibre = 30;
		remainBullets = WEAPONBULLETS;
		this.mainWindow.setScore(0);
		nextStage();
	}

	private void updateWorld() {
		//Actualiza todos los objetos de el juego
		if (stageTime < challengeTime && deadDucks < ducksForGame ){
			
			//Gestión de los patos que se muestran en pantalla
			for (int i = 0; i<entities.size(); i++){				
				Shootable duck = entities.get(i);
				
				if(duck.getHitted() == false){
					//Pato vivo
					duck.act();
				}else if(duck.isAtFloor() == false){
					//Pato cayendo						
					duck.fall();
				} else if(duck.isAtFloor() == true){
					if(ducksForGame == 2) {
						entities.remove(duck);
						if (deadDucks == 0){
							deadDucks = 1;
						} else if (deadDucks == 1) {
							deadDucks = 2;
						}
					} else {
						deadDucks = 1;						
					}
				}
				
				if (fallingDucks == ducksForGame)
					stopTimer = true;
				else
					stopTimer = false;
			}
		} else if(stageTime < challengeTime && deadDucks == ducksForGame) {
			//Han muerto todos los patos, siguiente nivel
			nextStage();
		} else {
			//Se ha acabado el tiempo
			gameOver = true;
		}
	}

	private void paintWorld() {
		//Pintado de todo lo que est� ocurriendo actualmente
		g = (Graphics2D)strategy.getDrawGraphics();
		background = mainWindow.getSpriteCache().getSprite("background.gif");
		
		g.setPaint(new TexturePaint(background, new Rectangle(0,0,Engine.WIDTH, Engine.HEIGHT)));
		g.fillRect(0, 0, mainWindow.getWidth(), mainWindow.getHeight());
		
		for(int i = 0; i<entities.size();i++){
			Entity d = (Entity)entities.get(i);
			d.paint(g);
		}
		
		paintHUD();
		
		strategy.show();
	}
	
	private void paintHUD() {
		g = (Graphics2D)strategy.getDrawGraphics();
		g.setColor(Color.white);
		
		//Pintado de los puntos
		g.drawString("Score", 630, 540);
		g.drawString(String.valueOf(this.mainWindow.getScore()), 630, 560);
		
		//Pintado de los FPS
		if(usedTime > 0){
			long stageSecondsTime = 0;
			if (!stopTimer) {
				stageSecondsTime = stageTime/1000;
			}
			g.drawString(String.valueOf(1000/usedTime)+" fps.", 80, 500);
			
			if(remainBullets > 0) {
				//Tiene balas
				g.drawString(String.valueOf( remainBullets ) + " bullets.", 80, 550 );
			} else {
				//Recargando
				g.setColor(Color.red);
				g.drawString("RELOADING", 80, 550);
				
				g.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 45));
				g.drawString("SPACE to RELOAD", 190, 270);
				//Restaurar pincel
				g = (Graphics2D)strategy.getDrawGraphics();
			}
			g.setColor(Color.white);

			g.drawString(String.valueOf((8-stageSecondsTime))+" seconds remain.", 400, 550);
			g.drawString("Level: "+String.valueOf(stageNumber-1), 250, 550);
		}else{
			g.drawString("--- fps", 0, Engine.HEIGHT-50);
		}
		
		strategy.show();
	}
	
	private void paintShoot() {
		g = (Graphics2D)strategy.getDrawGraphics();
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, mainWindow.getWidth(), mainWindow.getHeight());
		strategy.show();				
	}

	private void nextStage(){
		System.out.println("New level!");
		this.mainWindow.playSound("nextlevelsong.m4a");
		deadDucks = 0;
		fallingDucks = 0;
		stopTimer = false;
		stageStartTime = System.currentTimeMillis();
		//Crea nuevos patos en el juego (Siguiente nivel)
		entities = new ArrayList<Shootable>();
		
		for (int i = 0; i<ducksForGame; i++){
			boolean willBeBonus = Math.random() < 0.5;
			Duck d;
			
			if (willBeBonus && stageNumber % 2 == 0){
				d = new Duck(mainWindow, true);
				int duckBonusVelocity = stageNumber;
				duckBonusVelocity = duckBonusVelocity + 2;
				
				d.setVx(duckBonusVelocity * ((int)Math.random()*3-1));
				d.setVy(duckBonusVelocity * ((int)Math.random()*3-1));
			}else{
				d = new Duck(mainWindow, false);
				d.setVx(stageNumber * ((int)Math.random()*3-1));
				d.setVy(stageNumber * ((int)Math.random()*3-1));
			}
			d.setX((int)(Math.random()*Engine.WIDTH));
			d.setY(i*20);
			
			d.setArea(new Rectangle(30, 30));
			entities.add(d);
		}
		
		//Comprobar si es multiplo de 5 el nivel, en ese caso aumentar calibre del arma
		if(stageNumber%5 == 0){
			calibre = calibre+2;
			System.out.println("Felicidades, te hemos puesto un arma de calibre: "+calibre);
		}
		
		stageNumber++;
	}
	
	private void gameOver() {
		gameOver = true;
		
		if(mainWindow.isVisible())
			this.mainWindow.playSound("gameoversong.m4a");
		
		System.out.println("Game Over");
		mainWindow.viewScores();
		mainWindow.getGameThread().interrupt();
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			// e1.printStackTrace();
		}
		// Cambio de panel
		mainWindow.launchScores();
	}
	
	private void increaseScore(boolean bonus) {
		if (this.mainWindow.getScore() == 0) {
			if (bonus) {
				this.mainWindow.setScore(500);
			} else {
				this.mainWindow.setScore(100);
			}
		} else {
			if (bonus) {
				this.mainWindow.setScore(this.mainWindow.getScore()+500);
			} else {
				this.mainWindow.setScore(this.mainWindow.getScore()+100);
			}
		}
	}
	
	private void reloadWeapon() {
		this.mainWindow.playSound("reloadsong.m4a");
		remainBullets = WEAPONBULLETS;
		System.out.println("Arma recargada.");
	}
	
	@Override
	public void run() {
		this.mainWindow.getGameCanvas().setVisible(true);
		this.mainWindow.getGameCanvas().addMouseListener(this);
		this.mainWindow.getGameCanvas().addMouseMotionListener(this);
		this.mainWindow.getGameCanvas().addKeyListener(this);

		this.mainWindow.getGameCanvas().createBufferStrategy(2);
		strategy = this.mainWindow.getGameCanvas().getBufferStrategy();
		
		this.mainWindow.getGameCanvas().requestFocus();
		
		// Bucle principal de el juego
		usedTime = 1000;
		initWorld();

		while(this.mainWindow.getGameCanvas().isVisible() && this.mainWindow.isVisible() && !gameOver){
			long startTime = System.currentTimeMillis();
			
			updateWorld();
			paintWorld();
			
			if (!stopTimer) {
				stageTime = System.currentTimeMillis() - stageStartTime;				
				usedTime = System.currentTimeMillis() - startTime;				
			}
			
			try{
				Thread.sleep(Engine.SPEED);
			}catch(InterruptedException e){ }
		}
		
		//Pintado de el cartel y los puntos
		gameOver();
	}

	@Override
	public void mouseClicked(final MouseEvent e) {
		Object source = e.getSource();

		if(source == this.mainWindow.getGameCanvas() && !gameOver){
			Thread t = new Thread( new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					fireAction(e);	
				}
			});
			t.start();
		}
	}

	private void fireAction(MouseEvent e) {
		if(remainBullets > 0) {
			//FIRE
			this.mainWindow.playSound("firesong.m4a");
			remainBullets--;
			//Tengo balas
			paintShoot();
			for(int i = 0; i<entities.size();i++){
				Entity d = (Entity)entities.get(i);
				Rectangle shoot = new Rectangle(e.getX()-15, e.getY()-15, calibre, calibre);
				
				if( d.getArea().intersects(shoot) && !((Shootable) d).getHitted() ){
					//Disparo acertado
					fallingDucks = fallingDucks + 1;
					this.mainWindow.playSound("duckfallingsong.m4a");
					System.out.println("Pato "+(i+1)+" dice: AY!!Que da�o!");
					this.mainWindow.playSound("duckshootsong.m4a");
					((Shootable) d).shooted();
					((Shootable) d).setHitted(true);

					if(((Shootable) d).getBonus()){
						//Pato bonus
						increaseScore(true);
					}else{
						//Pato normal
						increaseScore(false);
					}
					//Restar una bala
				}else{
					//Disparo fallido
					System.out.println("Pato "+(i+1)+" dice: jajajaj que malo eres!");
				}
			}
		} else {
			//Toca recargar
			System.out.println("Maldita sea, tengo que recargar el arma.");
		}
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		// Tecla ESC
		System.out.println(arg0.toString());
		if(arg0.getKeyCode() == 27){
			System.out.println("Has pulsado la tecla escape");
			gameOver = true;
		}
		if(arg0.getKeyCode() == 80){
			// Tecla p
		}
		if(arg0.getKeyCode() == 32){
			// Tecla p
			System.out.println("Has pulsado la tecla espacio");
			reloadWeapon();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void mouseDragged(MouseEvent e) {
		// TODO Auto-generated method stub
		System.out.println(e.toString());
	}

	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

}
