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
 * Класс для работы с игровой панелью (Game Over)
 * @author Mirosha
 * @version 1.0
 */
public class PanelGame extends PanelButton { 

	/** Поле игровое поле*/
	private GameField field;
	
	/** Поле очки*/
	private Scores scores;
	
	/** Поле шрифт очков*/
	private Font scoreFont;
	
	/** Поле результат (счет) игры*/
	private BufferedImage gameResults; 

	/** Поле шрифт на кнопке окончания игры*/
	private Font fontGameOver;
	
	/** Поле кнопка возврата в меню*/
	private Button backToMenu; 
	
	/** Поле кнопка попробовать снова*/
	private Button playAgain; 
	
	/** Поле кнопка сделать скриншот*/
	private Button screenShot;
	
	/** Поле флаг скриншота*/
	private boolean screenshot; 
	
	/** Поле отображение кнопки*/
	private boolean represent; 
	
	/** Поле эффект затухания фона*/
	private int fadeEffect = 0; 
	
	/** Поле расстояние между кнопками*/
	private int buttonDistance = 30; 
	
	/** Поле высота кнопки*/
	private int buttonH = 50;
	
	/** Поле ширина мелкой кнопки*/
	private int littleButtonW = 170; 
	
	/** Поле ширина кнопки возврата в меню*/
	private int bigButtonW = littleButtonW * 2 + buttonDistance; 
	
	/** 
     * Конструктор - создание нового объекта игровая панель (Game Over)
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

		playAgain.setText("ИГРА"); 
		screenShot.setText("СКРИНШОТ");
		backToMenu.setText("МЕНЮ");

		backToMenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Screen.getInstance().setPanel("Menu"); 
			}
		});
		
		playAgain.addActionListener(new ActionListener() { 
			public void actionPerformed(ActionEvent event) {
				field.getScores().reset(); 						// сбрасывание счета при запуске новой игры
				field.resetData();
				fadeEffect = 0;
				
				removeButton(playAgain);
				removeButton(screenShot);
				removeButton(backToMenu);
				represent = false; 								// запрет отображения кнопок
			}
		});
		
		screenShot.addActionListener(new ActionListener() {  
			public void actionPerformed(ActionEvent event) {
				screenshot = true; 								// создание скриншота
			}
		});
	}
	
	/**
     * Процедура отрисовка на поле текста КОНЕЦ ИГРЫ
     * @param graphics - графика при окончании игры
     */
	public void printGameOver(Graphics2D graphics) { 
		graphics.setColor(new Color(222, 222, 222, fadeEffect));
		graphics.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		graphics.setColor(new Color(191, 11, 11));
		graphics.drawString("КОНЕЦ ИГРЫ", Game.WIDTH / 2 - DisplayObject.getObjectWidth("КОНЕЦ ИГРЫ", fontGameOver, graphics) / 2, 250);
	}

	/**
     * Процедура отрисовка на экране счета игры
     * @param graphics - графика счета игры
     */
	private void printScores(Graphics2D graphics) { 
		Graphics2D graph = (Graphics2D) gameResults.getGraphics();
		graph.setColor(new Color(0, 38, 77)); 										// цвет верхней панели
		graph.fillRect(0, 0, gameResults.getWidth(), gameResults.getHeight());
		graph.setColor(Color.white); 												// цвет текущего счета
		graph.setFont(scoreFont);
		graph.drawString("" + scores.getCurrentScore(), 30, 40);
		graph.setColor(new Color(191, 11, 11)); 									// цвет лучшего счета
		graph.drawString("       " + scores.getCurrentTopScore(), Game.WIDTH - DisplayObject.getObjectWidth("Best: " + scores.getCurrentTopScore(), scoreFont, graph) - 20, 40);
		graph.dispose(); 
		graphics.drawImage(gameResults, 0, 0, null); 
	}

	/**
     * Процедура рендера игровой панели
     * @param graphics - графика содержимого панели
     */
	@Override
	public void renderPanel(Graphics2D graphics) { 
		printScores(graphics);
		field.renderFinal(graphics);
		if (screenshot) { 
			BufferedImage buffImage = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_RGB);
			Graphics2D graph = (Graphics2D) buffImage.getGraphics();
			graph.setColor(new Color(0, 38, 77));
			graph.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			printScores(graph);
			field.renderFinal(graph);
			try { 							// сохранение на рабочий стол png-изображения скриншота
				ImageIO.write(buffImage, "png", new File(System.getProperty("user.home") + "//Desktop", "screenshot" + System.nanoTime() + ".png"));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			screenshot = false;
		}
		if (field.getDead()) { 				// в случае проигрыша вывод кнопок и панели конца игры
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
     * Процедура обновление экрана при эффекте затухания 
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
