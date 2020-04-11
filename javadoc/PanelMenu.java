package mirosha.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mirosha.game.DisplayObject;
import mirosha.game.Game;

/**
 * Класс для работы с панелью главного меню
 * @author Mirosha
 * @version 1.0
 */
public class PanelMenu extends PanelButton { 

	/** Поле строка 2048*/
	private String header = "2048"; 
	
	/** Поле ширина кнопки*/
	private int buttonW = 220;
	
	/** Поле размер шрифта строки 2048*/
	private Font headerFont = Game.main.deriveFont(100f); 
	
	/** 
     * Конструктор - создание нового объекта панель главного меню
     */
	public PanelMenu() { 
		super(); 
		Button play = new Button(Game.WIDTH / 2 - buttonW / 2, 220, buttonW, 60); 		// создание кнопки ИГРА
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Screen.getInstance().setPanel("Play");
			}
		});
		play.setText("ИГРА"); 
		addButton(play); 
		
		Button scores = new Button(Game.WIDTH / 2 - buttonW / 2, 310, buttonW, 60); 	// создание кнопки СЧЕТ
		scores.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Screen.getInstance().setPanel("Leaderboards");
			}
		});
		scores.setText("СЧЕТ");
		addButton(scores);
		
		Button quitButton = new Button(Game.WIDTH / 2 - buttonW / 2, 400, buttonW, 60);  // создание кнопки ВЫХОД
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		quitButton.setText("ВЫХОД");
		addButton(quitButton); 
	}

	/**
     * Процедура рендер панели главного меню
     * @param graphics - графика содержимого панели
     */
	@Override
	public void renderPanel(Graphics2D graphics) { 
		super.renderPanel(graphics);
		graphics.setFont(headerFont);
		graphics.setColor(Color.white);
		graphics.drawString(header, Game.WIDTH / 2 - DisplayObject.getObjectWidth(header, headerFont, graphics) / 2, 150);
	}
}
