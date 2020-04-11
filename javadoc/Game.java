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
 * ����� ��� �������� ����
 * @author Mirosha
 * @version 1.0
 */
public class Game extends JPanel implements Runnable, KeyListener, MouseListener, MouseMotionListener {
	
	/* Runnable - ��� ������
	 * KeyListener - ��������� ������� ����������
	 * MouseListener - ��������� ������� ����
	 * MouseMotionListener - ��������� ������� �������� ���� */
	
	/** ���� ����� ����*/
	private Thread gameThread; 
	
	/** ���� ��� ������������ ��������� ������ (run/stop)*/
	private boolean trackRunningThread; 
	
	/** ���� ��� �������� �����������*/
	private BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	
	/** ���� ��� ����, ����� ������������� ����� Game*/
	private static final long serialVersionUID = 1L;  
	
	/** ���� ������ �������� ����*/
	public static final int WIDTH = GameField.FIELDW + 40; 
	
	/** ���� ������ �������� ����*/
	public static final int HEIGHT = 610;
	
	/** ���� �������� ����� ������*/
	public static final Font main = new Font("Arial", Font.PLAIN, 30); 
	
	/** ���� �����*/
	private Screen screen;
	
	/** 
     * ����������� - �������� ������ ������� ����
     */
	public Game() { 
		
		setFocusable(true); // ��� ����� � ����������
		setPreferredSize(new Dimension(WIDTH, HEIGHT)); // ��������� ������ �� ������� �������
		addKeyListener(this); // ��� ����������� ��� ��������� ��������� ����� ��� ���������� � ����
		addMouseListener(this);
		addMouseMotionListener(this);
		
		screen = Screen.getInstance(); // ������� ���������� ������ ������ ��� ��������� �������
		screen.addPanel("Menu", new PanelMenu());
		screen.addPanel("Play", new PanelGame());
		screen.addPanel("Leaderboards", new PanelScores());
		screen.setPanel("Menu"); // ��������� �������� ���� � ��������� ��������� ���� 
	}
	
	/**
     * ��������� ������ ���� ����
     */
	private void mainRender() { 
		Graphics2D graphics = (Graphics2D) image.getGraphics();
		graphics.setColor(Color.black);	// ���� �������� ����
		graphics.fillRect(0, 0, WIDTH, HEIGHT); 
		screen.render(graphics); 
		graphics.dispose(); 

		Graphics2D graph = (Graphics2D) getGraphics();
		graph.drawImage(image, 0, 0, null); 
		graph.dispose(); 
	}

	/**
     * ��������� ���������� ������� ����������
     */
	private void updateKeyboard() { 
		screen.updateScreen();
		Keyboard.updateKeys();
	}

	/**
     * ��������� ����� ������ ������
     */
	@Override
	public void run() { 
		int FPS = 0, updates = 0;
		long timerFPS = System.currentTimeMillis();
		double nanoSecPerUpdate = 1000000000.0 / 60; // ������� ���������� � ����������� ����� ������������

		// ��������� ���������� ������� � ������������
		double lastTime = System.nanoTime();
		double unprocessed = 0;

		while (trackRunningThread) {

			boolean mustRender = false;
			double newTime = System.nanoTime();
			unprocessed += (newTime - lastTime) / nanoSecPerUpdate;  // ������� ������� ����� ������� ����������
			lastTime = newTime;

			while (unprocessed >= 1) { // ������� ����������

				// ����������
				updates++;
				updateKeyboard();
				unprocessed--;
				mustRender = true;
			}

			if (mustRender) { // ������
				FPS++;
				mainRender();
				mustRender = false;
			}
			else {
				try {
					Thread.sleep(1); // ��������� ������, ���� �� ��������
				} catch (Throwable ex) {
					 System.out.println("��������� ������: " + ex.getMessage()); 
				}
			}

			// ������ FPS
			if (System.currentTimeMillis() - timerFPS > 1000) {
				// ����� FPS �� ����������
				System.out.printf("%d FPS %d ����������", FPS, updates);
				System.out.println("");
				FPS = 0; // ��������� 
				updates = 0;
				timerFPS += 1000;
			}
		}
	}

	/**
     * ��������� ������������� ������ ������
     */
	public synchronized void start() {
		if (trackRunningThread) return;
		trackRunningThread = true;
		gameThread = new Thread(this, "game");
		gameThread.start();
	}

	/**
     * ��������� ������������� ���������� ������
     */
	public synchronized void stop() {
		if (!trackRunningThread) return;
		trackRunningThread = false;
		System.exit(0); // ����� �� ����������, ���� ����� ����������
	}

	/**
     * ��������� ��������� ��� ������� �������
     * @param event - ������� ����������
     */ 
	@Override
	public void keyTyped(KeyEvent event) {}

	/**
     * ��������� ��������� ��� ������� �������
     * @param event - ������� ����������
     */ 
	@Override
	public void keyPressed(KeyEvent event) { Keyboard.isPressed(event); }

	/**
     * ��������� ��������� ��� ���������� �������
     * @param event - ������� ����������
     */ 
	@Override
	public void keyReleased(KeyEvent event) { Keyboard.isReleased(event); }

	/**
     * ��������� ��������� ��� ������� ����
     * @param event - ������� ����
     */ 
	@Override
	public void mouseClicked(MouseEvent event) {}

	/**
     * ��������� ��������� ��� ������� ����
     * @param event - ������� ����
     */ 
	@Override
	public void mousePressed(MouseEvent event) { screen.mousePressed(event); }

	/**
     * ��������� ��������� ��� ���������� ����
     * @param event - ������� ����
     */ 
	@Override
	public void mouseReleased(MouseEvent event) { screen.mouseReleased(event); }

	/**
     * ��������� ��������� ��� ������� ���� �� �����
     * @param event - ������� ����
     */ 
	@Override
	public void mouseEntered(MouseEvent event) {}

	/**
     * ��������� ��������� ��� ������� ���� �� ������
     * @param event - ������� ����
     */ 
	@Override
	public void mouseExited(MouseEvent event) {}

	/**
     * ��������� ��������� ��� ������� ����� 
     * @param event - ������� ����
     */ 
	@Override
	public void mouseDragged(MouseEvent event) { screen.mouseDragged(event); }

	/**
     * ��������� ��������� ��� �������� ����
     * @param event - ������� ����
     */ 
	@Override
	public void mouseMoved(MouseEvent event) { screen.mouseMoved(event); }
}
