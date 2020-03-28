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

public class Game extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {
	// Runnable - для потока
	// KeyListener - обработка событий клавиатуры
	// MouseListener - обработка событий мыши
	// MouseMotionListener - обработка событий движения мыши
	
	private Thread gameThread; // поток игры
	private boolean trackRunningThread; // отслеживает состояние потока (run/stop)
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	private static final long serialVersionUID = 1L;  // сериализовать класс Game
	
	public static final int WIDTH = GameField.FIELDW + 40; // установка размера главного окна
	public static final int HEIGHT = 610;
	public static final Font main = new Font("Arial", Font.PLAIN, 30); // основной стиль шрифта
	private Screen screen;
	
	public Game() { // конструктор игры
		
		setFocusable(true); // для ввода с клавиатуры
		setPreferredSize(new Dimension(WIDTH, HEIGHT)); // установка фрейма по нужному размеру
		addKeyListener(this); // для уведомления при изменении состояния ключа для клавиатуры и мыши
		addMouseListener(this);
		addMouseMotionListener(this);
		
		screen = Screen.getInstance(); // задаем параметры каждой панели для отрисовки графики
		screen.addPanel("Menu", new PanelMenu());
		screen.addPanel("Play", new PanelGame());
		screen.addPanel("Leaderboards", new PanelScores());
		screen.setPanel("Menu"); // устанавливаем главное окно в начальное состояние меню 
	}
	
	private void mainRender() { // рендерим фон окон
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.black);  // цвет главного фона
		graphics.fillRect(0, 0, WIDTH, HEIGHT); // заполняем прямоугольник пикселями внутри фигуры
		screen.render(graphics); // рендер фона
		graphics.dispose(); // освобождаем ресурсы 

		Graphics2D graph = (Graphics2D) getGraphics();
		graph.drawImage(image, 0, 0, null); // отрисовываем графику
		graph.dispose(); // освобождаем ресурсы 
	}

	private void updateKeyboard() { // обновляем события клавиатуры
		screen.updateScreen();
		Keyboard.updateKeys();
	}

	@Override
	public void run() { // вызов run при работе потока
		int FPS = 0, updates = 0;
		long timerFPS = System.currentTimeMillis();
		double nanoSecPerUpdate = 1000000000.0 / 60; // отслеживаем сколько наносекунд в промежутках между обновлениями

		// последнее обновление времени в наносекундах
		double lastTime = System.nanoTime();
		double unprocessed = 0;

		while (trackRunningThread) {

			boolean mustRender = false;
			double newTime = System.nanoTime();
			unprocessed += (newTime - lastTime) / nanoSecPerUpdate;  // считает сколько нужно сделать обновлений
			lastTime = newTime;

			while (unprocessed >= 1) { // очередь обновления

				// обновление
				updates++;
				updateKeyboard();
				unprocessed--;
				mustRender = true;
			}

			if (mustRender) { // рендерим
				FPS++;
				mainRender();
				mustRender = false;
			}
			else {
				try {
					Thread.sleep(1); // усыпляем поток, если не рендерим
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			// таймер FPS
			if (System.currentTimeMillis() - timerFPS > 1000) {
				// вывод FPS за обновление
				System.out.printf("%d fps %d updates", FPS, updates);
				System.out.println("");
				FPS = 0; // обнуляем 
				updates = 0;
				timerFPS += 1000;
			}
		}
	}

	// убеждаемся, что весь метод был вызван;
	// синхронизируем начало и завершение потока
	public synchronized void start() {
		if (trackRunningThread) return;
		trackRunningThread = true;
		gameThread = new Thread(this, "game");
		gameThread.start();
	}

	public synchronized void stop() {
		if (!trackRunningThread) return;
		trackRunningThread = false;
		System.exit(0); // выходим из программмы, если поток остановлен
	}

	// отчеты о действиях/состояних мыши и клавиатуры: 
	@Override
	public void keyTyped(KeyEvent event) {}

	@Override
	public void keyPressed(KeyEvent event) { Keyboard.isPressed(event); }

	@Override
	public void keyReleased(KeyEvent event) { Keyboard.isReleased(event); }

	@Override
	public void mouseClicked(MouseEvent event) {}

	@Override
	public void mousePressed(MouseEvent event) { screen.mousePressed(event); }

	@Override
	public void mouseReleased(MouseEvent event) { screen.mouseReleased(event); }

	@Override
	public void mouseEntered(MouseEvent event) {}

	@Override
	public void mouseExited(MouseEvent event) {}

	@Override
	public void mouseDragged(MouseEvent event) { screen.mouseDragged(event); }

	@Override
	public void mouseMoved(MouseEvent event) { screen.mouseMoved(event); }
}
