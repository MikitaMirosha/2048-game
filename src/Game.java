package com.mirosha.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

// KeyListener - ��������� ������� ����������
// Runnable - ��� ������
public class Game extends JPanel implements KeyListener, Runnable { 

	private static final long serialVersionUID = 1L; // ������������� Game
	public static final int WIDTH = 700;
	public static final int HEIGHT = 800;
	public static final Font main = new Font("Arial", Font.PLAIN, 16);
	
	private GameField gameField;
	private Thread gameThread;
	private boolean trackRunningThread; // ����������� ��������� ������ (run or stop)
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB); // ��� ��������� 
	
	public Game() { // ����������� ����
		setFocusable(true); // ��������� ������� � �����
		setPreferredSize(new Dimension(WIDTH, HEIGHT)); // ��������� ������ �� ������� �������
		addKeyListener(this); // ����������, ����� �� ������ ��� �������� ��������� �����
		// ���������� ���� � �������� ������������ ��������� ������
		gameField = new GameField(WIDTH / 2 - GameField.FIELD_WIDTH / 2, HEIGHT - GameField.FIELD_HEIGHT - 25); 
	}
	
	// update() � render() ���������� 60 ��� � ������� (60 fps)
	private void update() {
		gameField.update(); 
		Keyboard.updateKey(); 
	} 
	
	private void render() {
		// ��� �� ������ ��������
		// image - ����������� ��������, ��� ����������� � ������, ��� ����������� ���, ��� �� ������ �� ������
		Graphics2D virtualImage = (Graphics2D) image.getGraphics(); // ��������� ��������� �����������
		virtualImage.setColor(Color.black); 
		virtualImage.fillRect(0, 0, WIDTH, HEIGHT); // ������ ���
		gameField.render(virtualImage); // ��������� �������� ����
	    virtualImage.dispose(); // ���������� �������, ���������� ������������ ����
		Graphics2D frameImage = (Graphics2D) getGraphics(); // ��������� ���������� ����������� � JPanel
		frameImage.drawImage(image, 0, 0, null);
		frameImage.dispose();
	}
	
	@Override // run() ����������, ����� ���������� ������ ������
	public void run() {
		int fps = 0, updates = 0;
		long timerFPS = System.currentTimeMillis();
		double nanoSecPerUpdate = 1000000000.0 / 60; // ����������� ������� ���������� � ����������� ����� ������������
		
		// ��������� ���������� � ������������
		double then = System.nanoTime();
		double unprocessed = 0; // ����������� ������� ���������� �� ������ ������� �� ������, ���� ��������� �������
		
		while(trackRunningThread) { // ���� ���� ������������
			boolean shouldRender = false;
			double newTime = System.nanoTime();
			unprocessed += (newTime - then) / nanoSecPerUpdate; // ������� ������� ����� ������� ����������
			then = newTime;
			
		// ������� ����������
		while(unprocessed >= 1) {
			updates++;
			update();
			unprocessed--;
			shouldRender = true;
		}
		
		// ������ 
		if(shouldRender) { // ��������
			fps++;
			render();
			shouldRender = false;
		}
		else {
			try {
				Thread.sleep(1); // �������� �����, ���� �� ��������
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		}
		
		// ������ FPS 
		if(System.currentTimeMillis() - timerFPS > 1000) {
			fps = 0;
			updates = 0;
			timerFPS += 1000;
		}
	}
	
	// ������������� ��� ����, ����� ���������, ��� ���� ����� ��� ������
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
	
	@Override // ���������� ��� ����� invalid
	public void keyTyped(KeyEvent e) {
		
	}
}
