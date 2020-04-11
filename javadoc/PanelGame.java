package mirosha.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

import mirosha.game.DisplayObject;
import mirosha.game.Game;
import mirosha.game.GameField;
import mirosha.game.Scores;

/**
 * ����� ��� ������ � ������� ������� (Game Over)
 * @author Mirosha
 * @version 1.0
 */
public class PanelGame extends PanelButton { 

	/** ���� ������� ����*/
	private GameField field;
	
	/** ���� ����*/
	private Scores scores;
	
	/** ���� ����� �����*/
	private Font scoreFont;
	
	/** ���� ��������� (����) ����*/
	private BufferedImage gameResults; 

	/** ���� ����� �� ������ ��������� ����*/
	private Font fontGameOver;
	
	/** ���� ������ �������� � ����*/
	private Button backToMenu; 
	
	/** ���� ������ ����������� �����*/
	private Button playAgain; 
	
	/** ���� ������ ������� ��������*/
	private Button screenShot;
	
	/** ���� ���� ���������*/
	private boolean screenshot; 
	
	/** ���� ����������� ������*/
	private boolean represent; 
	
	/** ���� ������ ��������� ����*/
	private int fadeEffect = 0; 
	
	/** ���� ���������� ����� ��������*/
	private int buttonDistance = 30; 
	
	/** ���� ������ ������*/
	private int buttonH = 50;
	
	/** ���� ������ ������ ������*/
	private int littleButtonW = 170; 
	
	/** ���� ������ ������ �������� � ����*/
	private int bigButtonW = littleButtonW * 2 + buttonDistance; 
	
	/** 
     * ����������� - �������� ������ ������� ������� ������ (Game Over)
     */
	public PanelGame() {
		scoreFont = Game.main.deriveFont(24f); 
		fontGameOver = Game.main.deriveFont(70f);
		field = new GameField(Game.WIDTH / 2 - GameField.FIELDW / 2, Game.HEIGHT - GameField.FIELDH - 20);
		scores = field.getScores(); 
		gameResults = new BufferedImage(Game.WIDTH, 200, BufferedImage.TYPE_INT_RGB);

		backToMenu = new Button(Game.WIDTH / 2 - bigButtonW / 2, 450, bigButtonW, buttonH);
		playAgain = new Button(backToMenu.getX(), backToMenu.getY() - buttonDistance - buttonH, littleButtonW, buttonH);
		screenShot = new Button(playAgain.getX() + playAgain.getWidth() + buttonDistance, playAgain.getY(), littleButtonW, buttonH);

		playAgain.setText("����"); 
		screenShot.setText("��������");
		backToMenu.setText("����");

		backToMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Screen.getInstance().setPanel("Menu"); 
			}
		});
		
		playAgain.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent event) {
				field.getScores().reset(); 						// ����������� ����� ��� ������� ����� ����
				field.resetData();
				fadeEffect = 0;
				
				removeButton(playAgain);
				removeButton(screenShot);
				removeButton(backToMenu);
				represent = false; 								// ������ ����������� ������
			}
		});
		
		screenShot.addActionListener(new ActionListener() {  
			public void actionPerformed(ActionEvent event) {
				screenshot = true; 								// �������� ���������
			}
		});
	}
	
	/**
     * ��������� ��������� �� ���� ������ ����� ����
     * @param graphics - ������� ��� ��������� ����
     */
	public void printGameOver(Graphics2D graphics) { 
		graphics.setColor(new Color(222, 222, 222, fadeEffect));
		graphics.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		graphics.setColor(new Color(191, 11, 11));
		graphics.drawString("����� ����", Game.WIDTH / 2 - DisplayObject.getObjectWidth("����� ����", fontGameOver, graphics) / 2, 250);
	}

	/**
     * ��������� ��������� �� ������ ����� ����
     * @param graphics - ������� ����� ����
     */
	private void printScores(Graphics2D graphics) { 
		Graphics2D graph = (Graphics2D) gameResults.getGraphics();
		graph.setColor(Color.black); 												// ���� ������� ������
		graph.fillRect(0, 0, gameResults.getWidth(), gameResults.getHeight());
		graph.setColor(Color.white); 												// ���� �������� �����
		graph.setFont(scoreFont);
		graph.drawString("" + scores.getCurrentScore(), 30, 40);
		graph.setColor(new Color(191, 11, 11)); 									// ���� ������� �����
		graph.drawString("       " + scores.getCurrentTopScore(), Game.WIDTH - DisplayObject.getObjectWidth("Best: " + scores.getCurrentTopScore(), scoreFont, graph) - 20, 40);
		graph.dispose(); 
		graphics.drawImage(gameResults, 0, 0, null); 
	}

	/**
     * ��������� ������� ������� ������
     * @param graphics - ������� ����������� ������
     */
	@Override
	public void renderPanel(Graphics2D graphics) { 
		printScores(graphics);
		field.renderFinal(graphics);
		if (screenshot) { 
			BufferedImage buffImage = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_RGB);
			Graphics2D graph = (Graphics2D) buffImage.getGraphics();
			graph.setColor(Color.white);
			graph.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			printScores(graph);
			field.renderFinal(graph);
			try { 							// ���������� �� ������� ���� png-����������� ���������
				ImageIO.write(buffImage, "png", new File(System.getProperty("user.home") + "\\Desktop", "screenshot" + System.nanoTime() + ".png"));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			screenshot = false;
		}
		if (field.getDead()) { 				// � ������ ��������� ����� ������ � ������ ����� ����
			if (!represent) { 
				represent = true;
				addButton(backToMenu);
				addButton(screenShot);
				addButton(playAgain);
			}
			printGameOver(graphics);
		}
		super.renderPanel(graphics);
	}
	
	/**
     * ��������� ���������� ������ ��� ������� ��������� 
     */
	@Override
	public void updatePanel() { 
		field.updateField();
		if (field.getDead()) {
			fadeEffect++;
			if (fadeEffect > 200) 
				fadeEffect = 200;
		}
	}
}