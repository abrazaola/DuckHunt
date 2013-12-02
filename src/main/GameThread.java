package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import windows.MainWindow;
import characters.Duck;

public class GameThread implements Runnable, MouseListener {
	public long usedTime;
	public long stageTime;
	public long stageStartTime;
	public long challengeTime;
	
	public int deadDucks;
	
	public boolean gameOver;
	
	private ArrayList<Shootable> entities;
	
	private int ducksForGame;
	private int stageNumber;
	
	private int score;
	
	private BufferedImage background;
	
	private MainWindow mainWindow;

	public GameThread(MainWindow mainWindow, int ducks){
		this.mainWindow = mainWindow;
		ducksForGame = ducks;
	}
	
	public void initWorld(){
		//Configura los par‡metros de inicio de el juego
		stageNumber = 1;
		challengeTime = 8000;
		gameOver = false;
		
		nextStage();
	}
	
	public void game(){
		mainWindow.gamePanel = new JPanel();
		mainWindow.gamePanel.setLayout(null);
		mainWindow.gamePanel.setPreferredSize(new Dimension(Engine.WIDTH,Engine.HEIGHT));
		//Pongo el panel en la ventana
		mainWindow.getContentPane().add(mainWindow.gamePanel);
		
		//Configura la ventana y controla el bucle de el juego
		mainWindow.addMouseListener(this);
		
		//Bucle principal de el juego
		usedTime = 1000;
		initWorld();
		
		while(mainWindow.gamePanel.isVisible() && !gameOver){
			long startTime = System.currentTimeMillis();
			
			updateWorld();
			paintWorld();
			
			stageTime = System.currentTimeMillis() - stageStartTime;
			usedTime = System.currentTimeMillis() - startTime;
			try{
				Thread.sleep(Engine.SPEED);
			}catch(InterruptedException e){}
		}
		//Pintado de el cartel y los puntos
		paintGameOver();
	}

	public void updateWorld(){
		//Actualiza todos los objetos de el juego
		if (stageTime < challengeTime && deadDucks < ducksForGame){
			//Gesti—n de los patos que se muestran en pantalla
			for (int i = 0; i<entities.size(); i++){				
				Shootable duck = entities.get(i);
				
				if(duck.getHitted() == false){
					//Pato vivo
					duck.act();
				}else if(duck.isAtFloor() == false){
					//Pato cayendo						
					duck.fall();
				}
			}
		} else if(stageTime < challengeTime && deadDucks == ducksForGame) {
			//Han muerto todos los patos, siguiente nivel
			nextStage();
		} else {
			//Se ha acabado el tiempo
			gameOver();
		}
	}

	public void paintWorld(){
		//Pintado de todo lo que est‡ ocurriendo actualmente
		Graphics2D g = (Graphics2D)mainWindow.getStrategy().getDrawGraphics();
		background = mainWindow.getSpriteCache().getSprite("background.gif");
		
		g.setPaint(new TexturePaint(background, new Rectangle(0,0,Engine.WIDTH, Engine.HEIGHT)));
		g.fillRect(0, 0, mainWindow.getWidth(), mainWindow.getHeight());
		
		for(int i = 0; i<entities.size();i++){
			Entity d = (Entity)entities.get(i);
			d.paint(g);
		}
		g.setColor(Color.white);
		//Pintado de los puntos
		g.drawString("Score", Engine.WIDTH-110, Engine.HEIGHT-45);
		g.drawString(String.valueOf(score), Engine.WIDTH-110, Engine.HEIGHT-30);
		//Pintado de los FPS
		if(usedTime > 0){
			long stageSecondsTime = stageTime/1000;
			g.drawString(String.valueOf(1000/usedTime)+" fps", 0, Engine.HEIGHT-50);
			g.drawString(String.valueOf(stageSecondsTime)+" stage time.", 0, Engine.HEIGHT-30);
		}else{
			g.drawString("--- fps", 0, Engine.HEIGHT-50);
		}
		
		mainWindow.getStrategy().show();
	}

	public void nextStage(){
		System.out.println("Next level!");
		deadDucks = 0;
		stageStartTime = System.currentTimeMillis();
		//Crea nuevos patos en el juego (Siguiente nivel)
		entities = new ArrayList<Shootable>();
		for (int i = 0; i<ducksForGame; i++){
			boolean willBeBonus = Math.random() < 0.5;
			Duck d;
			
			if (willBeBonus){
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
		
		stageNumber++;
	}
	
	public void gameOver(){
		gameOver = true;
		System.out.println("Game Over");
	}
	
	public void paintGameOver(){
		//Pintado de el Game Over
		Graphics2D g = (Graphics2D)mainWindow.getStrategy().getDrawGraphics();
		background = mainWindow.getSpriteCache().getSprite("background.gif");
		
		g.setPaint(new TexturePaint(background, new Rectangle(0,0,Engine.WIDTH, Engine.HEIGHT)));
		g.fillRect(0, 0, mainWindow.getWidth(), mainWindow.getHeight());
		
		g.setColor(Color.white);
		g.setFont(new Font("Impact Regular",Font.BOLD, 40));
		//Pintado de el cartel central
		g.drawString("GAME OVER", Engine.WIDTH/2 - 120, Engine.HEIGHT/2 - 90);
		g.drawString("Score: "+String.valueOf(score), Engine.WIDTH/2 - 80, Engine.HEIGHT/2 );
				
		mainWindow.getStrategy().show();
	}
	
	public void increaseScore(boolean bonus){
		if (score == 0){
			if (bonus){
				score = 500;
			}else{
				score = 100;
			}
		}else{
			if (bonus){
				score += 500;
			}else{
				score += 100;
			}
		}
	}
	
	@Override
	public void run() {
		game();
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		//FIRE
		for(int i = 0; i<entities.size();i++){
			Entity d = (Entity)entities.get(i);
			Rectangle shoot = new Rectangle(e.getX()+15, e.getY()+15, 30, 30);
			
			if( d.getArea().intersects(shoot) && !((Shootable) d).getHitted() ){
				System.out.println("AY!!Que da–o!");
				((Shootable) d).shooted();
				((Shootable) d).setHitted(true);
				deadDucks++;
				
				if(((Shootable) d).getBonus()){
					//Pato bonus
					increaseScore(true);
				}else{
					//Pato normal
					increaseScore(false);
				}
			}else{
				System.out.println("jajajaj que malo eres!");
			}
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

}
