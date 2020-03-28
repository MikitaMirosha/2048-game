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

public class PanelGame extends PanelButton { // класс содержит игровую панель при проигрыше и кнопки

	private GameField field;
	private Scores scores;
	private Font scoreFont;
	private BufferedImage gameResults; // для результатов игры (счет)

	// при проигрыше всплывают кнопки:
	private Font fontGameOver;
	private Button backToMenu; // кнопка в меню
	private Button playAgain; // кнопка попробовать снова
	private Button screenShot; // кнопка сделать скриншот
	private boolean screenshot; // флаг скриншота
	private boolean represent; // для отображения кнопки
	private int fadeEffect = 0; // для эффекта затухания
	private int buttonDistance = 30; // расстояние между кнопками
	private int buttonH = 50; // высота кнопки
	private int littleButtonW = 170; // ширина мелкой кнопки
	private int bigButtonW = littleButtonW * 2 + buttonDistance; // ширина кнопки возврата в меню
	
	public PanelGame() {
		scoreFont = Game.main.deriveFont(24f); // устанавливаем шрифт
		fontGameOver = Game.main.deriveFont(70f);
		field = new GameField(Game.WIDTH / 2 - GameField.FIELDW / 2, Game.HEIGHT - GameField.FIELDH - 20);
		scores = field.getScores(); // устанавливаем счет
		gameResults = new BufferedImage(Game.WIDTH, 200, BufferedImage.TYPE_INT_RGB);

		// создаем кнопки и задаем их размещение
		backToMenu = new Button(Game.WIDTH / 2 - bigButtonW / 2, 450, bigButtonW, buttonH);
		playAgain = new Button(backToMenu.getX(), backToMenu.getY() - buttonDistance - buttonH, littleButtonW, buttonH);
		screenShot = new Button(playAgain.getX() + playAgain.getWidth() + buttonDistance, playAgain.getY(), littleButtonW, buttonH);

		// текст на кнопках
		playAgain.setText("ИГРА"); 
		screenShot.setText("СКРИНШОТ");
		backToMenu.setText("МЕНЮ");

		backToMenu.addActionListener(new ActionListener() { // поведение при нажатии возврата в меню
			public void actionPerformed(ActionEvent event) {
				Screen.getInstance().setPanel("Menu"); // попадаем на экран панели меню
			}
		});
		
		playAgain.addActionListener(new ActionListener() { // поведение при нажатии новой игры
			public void actionPerformed(ActionEvent event) {
				field.getScores().reset(); // сбрасываем счет при запуске новой игры
				field.resetData();
				fadeEffect = 0;
				// удаляем кнопки с экрана
				removeButton(playAgain);
				removeButton(screenShot);
				removeButton(backToMenu);
				represent = false; // запрет отображения кнопок
			}
		});
		
		screenShot.addActionListener(new ActionListener() {  // поведение при нажатии скриншота
			public void actionPerformed(ActionEvent event) {
				screenshot = true; // делаем скриншот
			}
		});
	}
	
	public void printGameOver(Graphics2D graphics) { // отрисовка КОНЕЦ ИГРЫ
		graphics.setColor(new Color(222, 222, 222, fadeEffect));
		graphics.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
		graphics.setColor(new Color(191, 11, 11));
		graphics.drawString("КОНЕЦ ИГРЫ", Game.WIDTH / 2 - DisplayObject.getObjectWidth("КОНЕЦ ИГРЫ", fontGameOver, graphics) / 2, 250);
	}

	private void printScores(Graphics2D graphics) { // отрисовка на экране счета игры
		Graphics2D graph = (Graphics2D) gameResults.getGraphics();
		graph.setColor(Color.black); // цвет верхней панели
		graph.fillRect(0, 0, gameResults.getWidth(), gameResults.getHeight());
		graph.setColor(Color.white); // цвет текущего счета
		graph.setFont(scoreFont);
		graph.drawString("" + scores.getCurrentScore(), 30, 40);
		graph.setColor(new Color(191, 11, 11)); // цвет лучшего счета
		graph.drawString("       " + scores.getCurrentTopScore(), Game.WIDTH - DisplayObject.getObjectWidth("Best: " + scores.getCurrentTopScore(), scoreFont, graph) - 20, 40);
		graph.dispose(); 
		graphics.drawImage(gameResults, 0, 0, null); 
	}

	@Override
	public void renderPanel(Graphics2D graphics) { // рендерим содержимое
		printScores(graphics);
		field.renderFinal(graphics);
		if (screenshot) { // в случае скриншота
			BufferedImage buffImage = new BufferedImage(Game.WIDTH, Game.HEIGHT, BufferedImage.TYPE_INT_RGB);
			Graphics2D graph = (Graphics2D) buffImage.getGraphics();
			graph.setColor(Color.white);
			graph.fillRect(0, 0, Game.WIDTH, Game.HEIGHT);
			printScores(graph);
			field.renderFinal(graph);
			try { // сохраняем на рабочий стол png скриншот
				ImageIO.write(buffImage, "png", new File(System.getProperty("user.home") + "\\Desktop", "screenshot" + System.nanoTime() + ".png"));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			screenshot = false;
		}
		if (field.getDead()) { // в случае проигрыша выводим кнопки и конец игры
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
	
	@Override
	public void updatePanel() { // обновляется экран, работает эффект затухания поля игры
		field.updateField();
		if (field.getDead()) {
			fadeEffect++;
			if (fadeEffect > 200) 
				fadeEffect = 200;
		}
	}
}
