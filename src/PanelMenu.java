package mirosha.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import mirosha.game.DisplayObject;
import mirosha.game.Game;

public class PanelMenu extends PanelButton { // класс содержит главное меню

	private String header = "2048"; 
	private int buttonW = 220;
	private Font headerFont = Game.main.deriveFont(100f); // размер шрифта 2048
	
	public PanelMenu() { // конструктор панели меню
		super(); 
		Button play = new Button(Game.WIDTH / 2 - buttonW / 2, 220, buttonW, 60); // создаем кнопку ИГРА
		play.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Screen.getInstance().setPanel("Play");
			}
		});
		play.setText("ИГРА"); 
		addButton(play); // добавляем кнопку
		
		Button scores = new Button(Game.WIDTH / 2 - buttonW / 2, 310, buttonW, 60); // создаем кнопку СЧЕТ
		scores.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				Screen.getInstance().setPanel("Leaderboards");
			}
		});
		scores.setText("СЧЕТ");
		addButton(scores); // добавляем кнопку
		
		Button quitButton = new Button(Game.WIDTH / 2 - buttonW / 2, 400, buttonW, 60);  // создаем кнопку ВЫХОД
		quitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				System.exit(0);
			}
		});
		quitButton.setText("ВЫХОД");
		addButton(quitButton); // добавляем кнопку
	}

	@Override
	public void renderPanel(Graphics2D graphics) { // рендерим содержимое (кнопки, заглавие)
		super.renderPanel(graphics);
		graphics.setFont(headerFont);
		graphics.setColor(Color.white);
		graphics.drawString(header, Game.WIDTH / 2 - DisplayObject.getObjectWidth(header, headerFont, graphics) / 2, 150);
	}
}
