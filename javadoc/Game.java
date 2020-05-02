package mirosha.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import mirosha.gui.PanelScores;
import mirosha.gui.PanelMenu;
import mirosha.gui.PanelGame;
import mirosha.gui.Screen;

/**
 * Класс для создания игры
 * @author Mirosha
 * @version 1.0
 */
public class Game extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {
	
	/* Runnable - для потока
	 * KeyListener - обработка событий клавиатуры
	 * MouseListener - обработка событий мыши
	 * MouseMotionListener - обработка событий движения мыши */
	
	/** Поле поток игры*/
	private Thread gameThread; 
	
	/** Поле для отслеживания состояния потока (run/stop)*/
	private boolean trackRunningThread; 
	
	/** Поле для создания изображения*/
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	
	/** Поле для того, чтобы сериализовать класс Game*/
	private static final long serialVersionUID = 1L;  
	
	/** Поле ширина главного окна*/
	public static final int WIDTH = GameField.FIELDW + 40; 
	
	/** Поле высота главного окна*/
	public static final int HEIGHT = 610;
	
	/** Поле основной стиль шрифта*/
	public static final Font main = new Font("Century Gothic", Font.PLAIN, 30); 
	
	/** Поле экран*/
	private Screen screen;
	
	/** 
     * Конструктор - создание нового объекта игры
     */
	public Game() { 
		
		setFocusable(true); // для ввода с клавиатуры
		setPreferredSize(new Dimension(WIDTH, HEIGHT)); // установка фрейма по нужному размеру
		addKeyListener(this); // для уведомления при изменении состояния ключа для клавиатуры и мыши
		addMouseListener(this);
		addMouseMotionListener(this);
		
		screen = Screen.getInstance(); // задание параметров каждой панели для отрисовки графики
		screen.addPanel("Menu", new PanelMenu());
		screen.addPanel("Play", new PanelGame());
		screen.addPanel("Leaderboards", new PanelScores());
		screen.setPanel("Menu"); // установка главного окна в начальное состояние меню 
	}
	
	/**
     * Процедура рендер фона окон
     */
	private void mainRender() { 
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(new Color(0, 38, 77));	// цвет главного фона
		graphics.fillRect(0, 0, WIDTH, HEIGHT); 
		screen.render(graphics); 
		graphics.dispose(); 

		Graphics2D graph = (Graphics2D) getGraphics();
		graph.drawImage(image, 0, 0, null); 
		graph.dispose(); 
	}

	/**
     * Процедура обновление событий клавиатуры
     */
	private void updateKeyboard() { 
		screen.updateScreen();
		Keyboard.updateKeys();
	}

	/**
     * Процедура вызов работы потока
     */
	@Override
	public void run() { 
		int FPS = 0, updates = 0;
		long timerFPS = System.currentTimeMillis();
		double nanoSecPerUpdate = 1000000000.0 / 60; // подсчет наносекунд в промежутках между обновлениями

		// последнее обновление времени в наносекундах
		double lastTime = System.nanoTime();
		double unprocessed = 0;

		while (trackRunningThread) {

			boolean mustRender = false;
			double newTime = System.nanoTime();
			unprocessed += (newTime - lastTime) / nanoSecPerUpdate;  // подсчет сколько нужно сделать обновлений
			lastTime = newTime;

			while (unprocessed >= 1) { // очередь обновления

				// обновление
				updates++;
				updateKeyboard();
				unprocessed--;
				mustRender = true;
			}

			if (mustRender) { // рендер
				FPS++;
				mainRender();
				mustRender = false;
			}
			else {
				try {
					Thread.sleep(1); // усыпление потока, если не рендерим
				} catch (Throwable ex) {
					 System.out.println("Произошла ошибка: " + ex.getMessage()); 
				}
			}

			// таймер FPS
			if (System.currentTimeMillis() - timerFPS > 1000) {
				// вывод FPS за обновление
				System.out.printf("%d FPS %d обновления", FPS, updates);
				System.out.println("");
				FPS = 0; // обнуление 
				updates = 0;
				timerFPS += 1000;
			}
		}
	}

	/**
     * Процедура синхронизация начала потока
     */
	public synchronized void start() {
		if (trackRunningThread) return;
		trackRunningThread = true;
		gameThread = new Thread(this, "game");
		gameThread.start();
	}

	/**
     * Процедура синхронизация завершения потока
     */
	public synchronized void stop() {
		if (!trackRunningThread) return;
		trackRunningThread = false;
		System.exit(0); // выход из программмы, если поток остановлен
	}

	/**
     * Процедура состояние при нажатии клавиши
     * @param event - событие клавиатуры
     */ 
	@Override
	public void keyTyped(KeyEvent event) {}

	/**
     * Процедура состояние при нажатии клавиши
     * @param event - событие клавиатуры
     */ 
	@Override
	public void keyPressed(KeyEvent event) { Keyboard.isPressed(event); }

	/**
     * Процедура состояние при отпускании клавиши
     * @param event - событие клавиатуры
     */ 
	@Override
	public void keyReleased(KeyEvent event) { Keyboard.isReleased(event); }

	/**
     * Процедура состояние при нажатии мыши
     * @param event - событие мыши
     */ 
	@Override
	public void mouseClicked(MouseEvent event) {}

	/**
     * Процедура состояние при нажатии мыши
     * @param event - событие мыши
     */ 
	@Override
	public void mousePressed(MouseEvent event) { screen.mousePressed(event); }

	/**
     * Процедура состояние при отпускании мыши
     * @param event - событие мыши
     */ 
	@Override
	public void mouseReleased(MouseEvent event) { screen.mouseReleased(event); }

	/**
     * Процедура состояние при нажатии мыши на входе
     * @param event - событие мыши
     */ 
	@Override
	public void mouseEntered(MouseEvent event) {}

	/**
     * Процедура состояние при нажатии мыши на выходе
     * @param event - событие мыши
     */ 
	@Override
	public void mouseExited(MouseEvent event) {}

	/**
     * Процедура состояние при захвате мышью 
     * @param event - событие мыши
     */ 
	@Override
	public void mouseDragged(MouseEvent event) { screen.mouseDragged(event); }

	/**
     * Процедура состояние при движении мыши
     * @param event - событие мыши
     */ 
	@Override
	public void mouseMoved(MouseEvent event) { screen.mouseMoved(event); }
}
