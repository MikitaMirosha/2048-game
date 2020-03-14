package com.mirosha.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

// KeyListener - обработка событий клавиатуры
// Runnable - для потока
public class Game extends JPanel implements KeyListener, Runnable { 

	private static final long serialVersionUID = 1L; // сериализовать Game
	public static final int WIDTH = 700;
	public static final int HEIGHT = 800;
	public static final Font main = new Font("Arial", Font.PLAIN, 16);
	
	private GameField gameField;
	private Thread gameThread;
	private boolean trackRunningThread; // отслеживает состояние потока (run or stop)
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB); // для отрисовки 
	
	public Game() { // конструктор игры
		setFocusable(true); // позволяет вводить с клавы
		setPreferredSize(new Dimension(WIDTH, HEIGHT)); // установка фрейма по нужному размеру
		addKeyListener(this); // уведомляет, когда мы каждый раз изменяем состояние ключа
		// размещения поля с кубиками относительно основного экрана
		gameField = new GameField(WIDTH / 2 - GameField.FIELD_WIDTH / 2, HEIGHT - GameField.FIELD_HEIGHT - 25); 
	}
	
	// update() и render() вызываются 60 раз в секунду (60 fps)
	private void update() {
		gameField.update(); 
		Keyboard.updateKey(); 
	} 
	
	private void render() {
		// как мы рисуем картинку
		// image - виртуальная картинка, кот размещается в памяти, кот отслеживает все, что мы рисуем на экране
		Graphics2D virtualImage = (Graphics2D) image.getGraphics(); // отрисовка реального изображения
		virtualImage.setColor(Color.black); 
		virtualImage.fillRect(0, 0, WIDTH, HEIGHT); // черный фон
		gameField.render(virtualImage); // отрисовка игрового поля
	    virtualImage.dispose(); // освободить ресурсы, занимаемые компонентами окна
		Graphics2D frameImage = (Graphics2D) getGraphics(); // отрисовка финального изображения в JPanel
		frameImage.drawImage(image, 0, 0, null);
		frameImage.dispose();
	}
	
	@Override // run() вызывается, когда начинается работа потока
	public void run() {
		int fps = 0, updates = 0;
		long timerFPS = System.currentTimeMillis();
		double nanoSecPerUpdate = 1000000000.0 / 60; // отслеживает сколько наносекунд в промежутках между обновлениями
		
		// последнее обновление в наносекундах
		double then = System.nanoTime();
		double unprocessed = 0; // отслеживает сколько обновлений мы должны сделать на случай, если рендеринг отстает
		
		while(trackRunningThread) { // пока игра продолжается
			boolean shouldRender = false;
			double newTime = System.nanoTime();
			unprocessed += (newTime - then) / nanoSecPerUpdate; // считает сколько нужно сделать обновлений
			then = newTime;
			
		// очередь обновления
		while(unprocessed >= 1) {
			updates++;
			update();
			unprocessed--;
			shouldRender = true;
		}
		
		// рендер 
		if(shouldRender) { // рендерим
			fps++;
			render();
			shouldRender = false;
		}
		else {
			try {
				Thread.sleep(1); // усыпляем поток, если не рендерим
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		}
		
		// таймер FPS 
		if(System.currentTimeMillis() - timerFPS > 1000) {
			fps = 0;
			updates = 0;
			timerFPS += 1000;
		}
	}
	
	// синхронизация для того, чтобы убедиться, что весь метод был вызван
	public synchronized void start() {
		if(trackRunningThread) return;
		trackRunningThread = true;
		gameThread = new Thread(this, "game");
		gameThread.start();
	}
	
	public synchronized void stop() {
		if(!trackRunningThread) return;
		trackRunningThread = false;
		System.exit(0);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		Keyboard.keyPressed(e);
	}

	@Override
	public void keyReleased(KeyEvent e) {
		Keyboard.keyReleased(e);
	}
	
	@Override // возвращает код ключа invalid
	public void keyTyped(KeyEvent e) {
		
	}
}
