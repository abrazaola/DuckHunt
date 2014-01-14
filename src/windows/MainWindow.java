package windows;

import java.awt.Canvas;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.sql.*;
import java.util.Vector;

import javafx.embed.swing.JFXPanel;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import main.Engine;
import main.GameCycle;
import auxil.SQLiteManager;
import auxil.SpriteCache;

/**
 * Clase que representa la ventana sobre la que se muestra todo el contenido de la aplicación
 * @author aitor
 *
 */
@SuppressWarnings("serial")
public class MainWindow extends JFrame implements Engine, MouseListener{
	
	public final static String GAMEPANEL = "Card with the canvas of the game";
	public final static String MENUPANEL = "Card with the main menu";
	public final static String SCOREPANEL = "Card with the table of scores";
	public final static String LOGOPANEL = "Card with my game development brand of games";
	
	private int exitOption;

	private SpriteCache spriteCache;
	private GameCycle gameCycle;
	private Thread gameThread;
	private SQLiteManager manager;
	
	private long score;
	private String maxScore;
	
	private JFXPanel cards;
	private JPanel menuCard;
	private JPanel gameCard;
	private JPanel scoreCard;
	private JPanel logoCard;
	
	private Canvas gameCanvas;
	
	private JLabel lMaxScore;
	private JButton b1Duck;
	private JButton b2Ducks;
	private JButton bQuit;
	
	private JTextField txPlayerName;
	private JLabel lGameOver;
	private JLabel lPlayerName;
	private JLabel lYourPlayerScore;
	private JLabel lPlayerScore;
	private JButton bSubmitScore;
	private JTable tbScoreTable;
	private DefaultTableModel scoreTableModel;

	/**
	 * Constructor
	 * @param title Título de la ventana creada
	 */
	public MainWindow(String title){
		super(title);
		gameCanvas = new Canvas();		
		// SQLLite
		manager = new SQLiteManager("scores.db");		
		
		setBounds(300,100,Engine.WIDTH,Engine.HEIGHT);
		
		maxScore = "";
		updateMaxScore();
		spriteCache = new SpriteCache();
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setResizable(false);
		
		cards = new JFXPanel();
		cards.setLayout(new CardLayout());
		
		menuCard = new JPanel();
		menuCard.setLayout(null);
		
		gameCard = new JPanel();
		gameCard.setLayout(null);
		gameCard.setPreferredSize(new Dimension(Engine.WIDTH,Engine.HEIGHT));
		
		scoreCard = new JPanel();
		scoreCard.setLayout(new BoxLayout(scoreCard, BoxLayout.PAGE_AXIS));
		scoreCard.setPreferredSize(new Dimension(Engine.WIDTH,Engine.HEIGHT));
		
		logoCard = new JPanel();
		logoCard.setLayout(null);
		logoCard.setPreferredSize(new Dimension(Engine.WIDTH,Engine.HEIGHT));
		
		//Logo
		JLabel backgroundLogoLabel = new JLabel();
		backgroundLogoLabel.setBounds(0, 0, Engine.WIDTH, Engine.HEIGHT);
		BufferedImage backgroundLogoImage = spriteCache.getSprite("kryptonitelogo.gif");
		backgroundLogoLabel.setIcon(new ImageIcon(backgroundLogoImage));
		
		// MEN� PRINCIPAL
		JLabel backgroundLabel = new JLabel();
		backgroundLabel.setBounds(0, 0, Engine.WIDTH, Engine.HEIGHT);
		BufferedImage backgroundImage = spriteCache.getSprite("mainmenu.gif");
		backgroundLabel.setIcon(new ImageIcon(backgroundImage));
		
		logoCard.add(backgroundLogoLabel);
		
		// Componentes de los paneles
		// Menu inicio
		b1Duck = new JButton("1 Duck");
		b1Duck.addMouseListener(this);
		b1Duck.setBounds(375, 338, 100, 25);
		
		b2Ducks = new JButton("2 Ducks");
		b2Ducks.addMouseListener(this);
		b2Ducks.setBounds(375, 370, 100, 25);
		
		bQuit = new JButton("Quit game");
		bQuit.addMouseListener(this);
		bQuit.setBounds(690, 10, 100, 25);
		
		lMaxScore = new JLabel(maxScore);
		lMaxScore.setForeground(Color.white);
		lMaxScore.setFont( new Font(Font.SANS_SERIF, Font.BOLD, 23) );
		lMaxScore.setBounds(490, 465, 60, 20);
		
		// Ventana de puntos
		lGameOver = new JLabel("GAME OVER");
		lGameOver.setFont( new Font(Font.SANS_SERIF, Font.BOLD, 60) );
		lYourPlayerScore = new JLabel("Your score: ");
		lPlayerScore = new JLabel(String.valueOf(score));
		txPlayerName = new JTextField(20);
		lPlayerName = new JLabel("Your name: ");
		
		// CONFIGURACION DE LA TABLA -------
		tbScoreTable = new JTable();
		tbScoreTable.setEnabled(false);
		
		Vector<String> colNames = new Vector<String>();
		colNames.add("Score");
		colNames.add("Name");
		
		scoreTableModel = new DefaultTableModel(colNames, 0);
		tbScoreTable.setModel(scoreTableModel);
		
		tbScoreTable.getColumn("Name").setPreferredWidth(100);
		tbScoreTable.getColumn("Score").setMaxWidth(50);
		
		
		bSubmitScore = new JButton("Submit & Go!");
		bSubmitScore.addMouseListener(this);
		// ----------------------------------
		
		// A�ado los botones al contenedor que hay en el centro de el BorderLayout
		menuCard.add(b1Duck);
		menuCard.add(b2Ducks);
		menuCard.add(bQuit);
		menuCard.add(lMaxScore);
		menuCard.add(backgroundLabel);
		
		// JUEGO
		gameCanvas.setBounds(0, 0, Engine.WIDTH, Engine.HEIGHT);
		gameCard.add(gameCanvas);
		
		// INTRODUCIR PUNTOS
		JPanel tbHeaderPane = new JPanel();
		
		tbHeaderPane.add(lYourPlayerScore);
		tbHeaderPane.add(lPlayerScore);
		tbHeaderPane.add(Box.createRigidArea(new Dimension(10, 0)));
		
		tbHeaderPane.add(lPlayerName);
		tbHeaderPane.add(txPlayerName);
		tbHeaderPane.add(Box.createRigidArea(new Dimension(10, 0)));
		
		tbHeaderPane.add(bSubmitScore);
		
		JScrollPane scrollTable = new JScrollPane(tbScoreTable);
		
		scoreCard.add(lGameOver);
		scoreCard.add(tbHeaderPane);
		scoreCard.add(scrollTable);
		
		// TARJETAS
		cards.add(menuCard, MENUPANEL);
		cards.add(gameCard, GAMEPANEL);
		cards.add(scoreCard, SCOREPANEL);
		cards.add(logoCard, LOGOPANEL);
		
		getContentPane().add(cards);
		launchLogo();
		
		Thread t = new Thread(new Runnable() {

			@Override
			public void run() {
				long currentTime = System.currentTimeMillis();
				boolean nextCard = false;
				while(!nextCard) {
					if ( currentTime - System.currentTimeMillis() < -4000) {
						nextCard = true;
					}
				}
				launchMenu();
			}
			
		});
		t.start();
	}
	
	/**
	 * Muestra la tarjeta en la que se muestra el logo de mi marca de desarrollo
	 */
	public void launchLogo() {
		CardLayout cl = (CardLayout)(cards.getLayout());
		cl.show(cards, LOGOPANEL);
	}
	
	/**
	 * Muestra la tarjeta en la que se encuentra el menú principal
	 */
	public void launchMenu() {
		CardLayout cl = (CardLayout)(cards.getLayout());
		cl.show(cards, MENUPANEL);
		b1Duck.requestFocus();
		
		playSound("mainmenusong.m4a");		
	}
	
	/**
	 * Muestra la tarjeta en la que se encuentra el juego
	 * @param ducks Patos que se desean tener en el juego (1 o 2)
	 */
	public void launchGame(int ducks) {
		// NEW GAME
		gameCycle = new GameCycle(this, ducks);
		gameThread = new Thread(gameCycle);
		
		// Cambio de panel
		CardLayout cl = (CardLayout)(cards.getLayout());
		cl.show(cards, GAMEPANEL);
		
		gameThread.start();
	}
	
	/**
	 * Muestra la tarjeta en la que se encuentra la tabla de puntuaciones
	 */
	public void launchScores() {
		CardLayout cl = (CardLayout)(getCards().getLayout());
		cl.show(getCards(), MainWindow.SCOREPANEL);
		txPlayerName.requestFocus();
	}
	
	/**
	 * Reproduce un audio utilizando el Framework JavaFX en un hilo independiente
	 * @param audio Fichero de audio a reproducir
	 */
	public void playSound(final String audio) {
		Thread t = new Thread(new Runnable(){
			@Override
			public void run() {
				// TODO Auto-generated method stub
				URL url = getClass().getClassLoader().getResource("res/"+audio);
				Media m = new Media(url.toString());
				
				final MediaPlayer player = new MediaPlayer(m);
				player.setOnEndOfMedia(new Runnable(){
					@Override
					public void run() {
						// TODO Auto-generated method stub
						player.stop();
					}
				});
				player.play();
			}
		});
		t.start();	
	}

	/* (non-Javadoc)
	 * @see main.Engine#getSpriteCache()
	 */
	public SpriteCache getSpriteCache(){
		return spriteCache;
	}

	/**
	 * Get para el hilo sobre el que se ejecuta todos los procesos del juego
	 * @return
	 */
	public Thread getGameThread() {
		return gameThread;
	}
	
	/**
	 * Get para el objeto Canvas sobre el que se pinta el contenido de el juego
	 * @return
	 */
	public Canvas getGameCanvas(){
		return gameCanvas;
	}

	/**
	 * Get para el objeto que contiene todos los paneles de tipo tarjeta que maneja la ventana principal
	 * @return
	 */
	public JFXPanel getCards() {
		return cards;
	}
	
	/**
	 * Get para el objeto manager de la BBDD SQLite
	 * @return
	 */
	public SQLiteManager getSQLManager() {
		return manager;
	}
	
	/**
	 * Establece una puntuación
	 * @param s
	 */
	public void setScore(long s) {
		score = s;
	}
	
	/**
	 * Obtienes la puntuación
	 * @return
	 */
	public long getScore() {
		return score;
	}
	
	/**
	 * Actualiza la variable de puntuación máxima en función de los datos de la BBDD
	 */
	public void updateMaxScore() {
		ResultSet results = manager.query("SELECT MAX(score) FROM Game");
		
		try {
			while (results.next()) {
				maxScore = results.getString(1);
			   }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Recupera las puntuaciones de la BBDD y las carga en la tabla de puntos
	 */
	public void viewScores() {
		lPlayerScore.setText(String.valueOf(getScore()));
		scoreTableModel.getDataVector().removeAllElements();
		ResultSet results = manager.query("SELECT * FROM Game ORDER BY score DESC");
		
		try {
			while (results.next()) {
				Object[] myRow = { results.getString(3), results.getString(2) };
				scoreTableModel.addRow(myRow);
			   }
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Método estático desde el que se lanza la ventana
	 * @param args
	 */
	public static void main(String[] args) {
		// Barra de men� nativa en OSX
		String SO=System.getProperty("os.name");
		if(SO.startsWith("Mac")){
			System.setProperty("apple.laf.useScreenMenuBar", "true");
			System.setProperty("com.apple.mrj.application.apple.menu.about.name", "Duck Hunt!");
		}
		
		//Creaci�n y configuraci�n de la ventana principal
		MainWindow mainWindow=new MainWindow("DuckHunt!");
		mainWindow.setVisible(true);
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		Object source = e.getSource();
		if(source == this.b2Ducks){
			launchGame(2);
		}else if(source == this.b1Duck){
			launchGame(1);
		}else if(source == this.bQuit){
			// CLOSE
			exitOption = JOptionPane.showConfirmDialog(this, "Do you really want to exit?", "Confirm exit", JOptionPane.OK_CANCEL_OPTION);
			
			if (exitOption == 0) {
				dispose();
			}
		}else if(source == this.bSubmitScore){
			// SAVE SCORE AND RETURN TO MAIN MENU
			manager.insert( "INSERT INTO Game VALUES("+System.currentTimeMillis()+",' "+txPlayerName.getText()+" ',"+score+")" );
			updateMaxScore();
			txPlayerName.setText("");
			gameThread.interrupt();
			launchMenu();
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
		//System.out.println(e.toString());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println(e.toString());
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		//System.out.println(e.toString());
	}
}