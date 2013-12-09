package windows;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.*;

import main.Engine;
import main.GameCycle;
import aux.SpriteCache;

@SuppressWarnings("serial")
public class MainWindow extends JFrame implements Engine, MouseListener, KeyListener{

	private SpriteCache spriteCache;
	private BufferStrategy strategy;
	private GameCycle gameCycle;
	private Thread gameThread;

	public JPanel menuPanel;
	public JPanel gamePanel;
	public JButton b1Duck;
	public JButton b2Ducks;

	public MainWindow(String title){
		super(title);
		setVisible(true);
		createBufferStrategy(2);
		strategy = getBufferStrategy();
		
		setBounds(300,100,Engine.WIDTH,Engine.HEIGHT);

		spriteCache = new SpriteCache();
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		startMenu();
	}
	public void startMenu(){
		JLabel backgroundLabel = new JLabel();
		backgroundLabel.setBounds(0, 0, Engine.WIDTH, Engine.HEIGHT);
		BufferedImage backgroundImage = spriteCache.getSprite("mainmenu.gif");
		backgroundLabel.setIcon(new ImageIcon(backgroundImage));
		
		//Botones que ocupan el centro
		b1Duck = new JButton("1 Duck");
		b1Duck.addMouseListener(this);
		b1Duck.setBounds(340, 270, 100, 25);
		
		b2Ducks = new JButton("2 Ducks");
		b2Ducks.addMouseListener(this);
		b2Ducks.setBounds(340, 293, 100, 25);
		
		menuPanel = new JPanel();
		menuPanel.setLayout(null);
		
		//A–ado los botones al contenedor que hay en el centro de el BorderLayout
		menuPanel.add(b1Duck);
		menuPanel.add(b2Ducks);
		
		menuPanel.add(backgroundLabel);
		
		getContentPane().add(menuPanel);
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
			gameCycle = new GameCycle(this, 2);
			gameThread = new Thread(gameCycle);
			getContentPane().remove(menuPanel);
			gameThread.start();
		}else if(source == this.b1Duck){
			//NEW GAME
			gameCycle = new GameCycle(this, 1);
			gameThread = new Thread(gameCycle);
			getContentPane().remove(menuPanel);
			gameThread.start();	
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
	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == 27){
			System.out.println("Has pulsado la tecla escape");
			gameThread.interrupt();
		}
	}
	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}
}