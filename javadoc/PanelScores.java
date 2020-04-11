package mirosha.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import mirosha.game.DisplayObject;
import mirosha.game.Game;
import mirosha.game.Leaders;

/**
 * Класс для работы с панелью таблицы результатов игры
 * @author Mirosha
 * @version 1.0
 */
public class PanelScores extends PanelButton { 
	
	/** Поле очки*/
	private Leaders scores;
	
	/** Поле ширина кнопки*/
	private int buttonW = 100;
	
	/** Поле высота кнопки*/
	private int buttonH = 50;
	
	/** Поле координата Y кнопки*/
	private int buttonY = 120;
	
	/** Поле координата X очков*/
	private int scoresX = 130;
	
	/** Поле координата Y очков*/
	private int scoresY = buttonY + buttonH + 90;
	
	/** Поле расстояние между кнопками*/
	private int buttonDistance = 20;
	
	/** Поле ширина кнопки возврата в меню*/
	private int backButtonW = 220; 
	
	/** Поле строка СЧЕТ*/
	private String header = "СЧЕТ";
	
	/** Поле шрифт СЧЕТ*/
	private Font headerFont = Game.main.deriveFont(45f);
	
	/** Поле шрифт очков в таблице*/
	private Font scoreFont = Game.main.deriveFont(32f);
	
	/** Поле текущее состояние очки/кубы (очки)*/
	private State currentState = State.SCORE;
	
	/** Поле перечесление очки/кубы*/
	private enum State { SCORE, CUBE }
	
	/** 
     * Конструктор - создание нового объекта панель таблицы результатов игры
     */
	public PanelScores() { 
		super();  
		scores = Leaders.getInstance();
		scores.loadScores();

		// создание и отрисовка кнопки КУБЫ
		Button cube = new Button(Game.WIDTH / 2 - buttonW / 2, buttonY, buttonW, buttonH);
		cube.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				currentState = State.CUBE;
			}
		});
		cube.setText("КУБЫ");
		addButton(cube);
		
		// создание и отрисовка кнопки ОЧКИ
		Button score = new Button(Game.WIDTH / 2 - buttonW / 2 - cube.getWidth() - buttonDistance, buttonY, buttonW, buttonH);
		score.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				currentState = State.SCORE;
			}
		});
		score.setText("ОЧКИ");
		addButton(score);
		
		// создание и отрисовка кнопки НАЗАД
		Button back = new Button(Game.WIDTH / 2 - backButtonW / 2, 500, backButtonW, 60);
		back.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Screen.getInstance().setPanel("Menu"); 				// выход в главное меню
			}
		});
		back.setText("НАЗАД");
		addButton(back);
	}
	
	/**
     * Функция конвертация числа в строку 
     * @param listNum - число
     * @return возвращает строку после конвертации
     */
	private ArrayList<String> convertToStrings(ArrayList<? extends Number> listNum) {
		ArrayList<String> finalString = new ArrayList<String>();
		for(Number num : listNum){
			finalString.add(num.toString());
		}
		return finalString;
	}
	
	/**
     * Процедура отрисовка счета игры
     * @param graphics - печать таблицы со счетом
     */
	private void printScores(Graphics2D graphics) { 
		ArrayList<String> strings = new ArrayList<String>();
		if(currentState == State.SCORE) {
			strings = convertToStrings(scores.getTopScores());
		}
		else if(currentState == State.CUBE) {
			strings = convertToStrings(scores.getTopCubes());
		}
		graphics.setColor(Color.white);
		graphics.setFont(scoreFont);
		
		for(int i = 0; i < strings.size(); i++) { 		// нумерация данных в таблице
			String str = (i + 1) + ". " + strings.get(i);
			graphics.drawString(str, scoresX, scoresY + i * 40);
		}
	}
	
	/**
     * Процедура рендер панели счета игры
     * @param graphics - графика панели счета игры
     */
	@Override
	public void renderPanel(Graphics2D graphics) { 
		super.renderPanel(graphics);
		graphics.setColor(Color.black);
		graphics.drawString(header, Game.WIDTH / 2 - DisplayObject.getObjectWidth(header, headerFont, graphics) / 2, DisplayObject.getObjectHeight(header, headerFont, graphics) + 40);
		printScores(graphics);
	}
	
	@Override
	public void updatePanel() {}
}
