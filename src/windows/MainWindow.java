package windows;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.TexturePaint;

import javax.swing.*;

import main.Engine;
import main.GameThread;
import aux.SpriteCache;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements Engine, MouseListener{

	private SpriteCache spriteCache;
	private BufferStrategy strategy;

	public JPanel menuPanel;
	public JPanel gamePanel;
	public JButton b1Duck;
	public JButton b2Ducks;
	private JButton bExit;

	public MainWindow(String title){
		super(title);
		setVisible(true);
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		setBounds(0,0,Engine.WIDTH,Engine.HEIGHT);

		spriteCache = new SpriteCache();
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		startMenu();
	}
	public void startMenu(){
		//Pintado del background
		Graphics2D g = (Graphics2D)getStrategy().getDrawGraphics();
		BufferedImage background = spriteCache.getSprite("mainmenu.gif");

		g.setPaint(new TexturePaint(background, new Rectangle(0,0,Engine.WIDTH, Engine.HEIGHT)));
		g.fillRect(0, 0, getWidth(), getHeight());
		getStrategy().show();
		
		//Panel que va a ocupar el tama–o del JFrame
		menuPanel = new JPanel();
		menuPanel.setLayout(new BorderLayout());
		menuPanel.setPreferredSize(new Dimension(Engine.WIDTH,Engine.HEIGHT));

		//Panel contenedor que va a ir en el centro del BorderLayout
		JPanel centerPanel = new JPanel();
		centerPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));

		//Pongo el panel en la ventana
		getContentPane().add(menuPanel);
		//A–ado a el panel del menu con BL el panel central que es un FL
		menuPanel.add(centerPanel, BorderLayout.CENTER);

		//Botones que ocupan el centro
		b1Duck = new JButton("1 Duck");
		b1Duck.addMouseListener(this);
		
		b2Ducks = new JButton("2 Ducks");
		b2Ducks.addMouseListener(this);
		
		bExit = new JButton("Quit");
		bExit.addMouseListener(this);

		//A–ado los botones al contenedor que hay en el centro de el BorderLayout
		centerPanel.add(b1Duck);
		centerPanel.add(b2Ducks);
		centerPanel.add(bExit);
	}

	public SpriteCache getSpriteCache(){
		return spriteCache;
	}
	public BufferStrategy getStrategy(){
		return strategy;
	}

	public static void main(String[] args) {
		//Barra de menœ nativa en OSX
		String SO=System.getProperty("os.name");
		if(SO.startsWith("Mac")){
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Duck Hunt!");
		}
		//Creaci—n y configuraci—n de la ventana principal
		MainWindow mainWindow=new MainWindow("DuckHunt!");
		mainWindow.setVisible(true);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		Object source = e.getSource();
		if(source == this.b2Ducks){
			//NEW GAME
			GameThread gameThread = new GameThread(this, 2);
			Thread t = new Thread(gameThread);
			t.start();
		}else if(source == this.b1Duck){
			//NEW GAME
			GameThread gameThread = new GameThread(this, 1);
			Thread t = new Thread(gameThread);
			t.start();	
		}else if(source == this.bExit){
			//EXIT GAME
			dispose();
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println(e.toString());
	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub

	}
}