package mirosha.gui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import mirosha.game.SetAudio;
import mirosha.game.DisplayObject;
import mirosha.game.Game;

public class Button { // класс для создания кнопок
	
	private Color standart;
	private Color hover;
	private Color pressed;
	private String text = "";
	private SetAudio audio;
	private State current = State.RELEASED; // стандартное состояние: отпущено
	private Rectangle rectangleButton; // оболочка кнопки
	private ArrayList<ActionListener> actionListeners; // хранение действий с кнопками в массиве
	private Font font = Game.main.deriveFont(25f); // шрифт текста на кнопке
	
	public Button(int x, int y, int width, int height) { // конструктор кнопки
		
		rectangleButton = new Rectangle(x, y, width, height); // создание оболочки кнопки
		actionListeners = new ArrayList<ActionListener>(); // создание действий с кнопками
		
		standart = new Color(191, 11, 11); // стандартный темно-серый цвет
		hover = new Color(220, 39, 39); // более серый цвет при наведении
		pressed = new Color(229, 65, 65); // еще более серый цвет при нажатии
		
		// работа аудио при нажатии на кнопку
		audio = SetAudio.getInstance();
		audio.loadAudio("select.wav", "select");
	}
	
	public void renderButton(Graphics2D graphics) { // рендер кнопки
		if(current == State.RELEASED) { // рендер при отпускании
			graphics.setColor(standart);
			graphics.fill(rectangleButton);
		}
		else if(current == State.PRESSED) { // рендер при нажатии
			graphics.setColor(pressed);
			graphics.fill(rectangleButton);
		}
		else { // рендер при наведении
			graphics.setColor(hover);
			graphics.fill(rectangleButton);
		}
		graphics.setColor(Color.white);
		graphics.setFont(font); // координата отрисовки текста на кнопке по центру:
		graphics.drawString(text, rectangleButton.x + rectangleButton.width / 2  - DisplayObject.getObjectWidth(text, font, graphics) / 2, rectangleButton.y + rectangleButton.height / 2  + DisplayObject.getObjectHeight(text, font, graphics) / 2);
	}
	
	public void update() {}
	
	public void addActionListener(ActionListener listener) { // добавляем действие с кнопкой
		actionListeners.add(listener);
	}
	
	public void mousePressed(MouseEvent event) { // состояние при нажатии мыши
		if(rectangleButton.contains(event.getPoint())) {
			current = State.PRESSED;
		}
	}

	public void mouseReleased(MouseEvent event) { // состояние при отпускании мыши
		if(rectangleButton.contains(event.getPoint())) {
			for(ActionListener i : actionListeners) {
				i.actionPerformed(null);
			}
			audio.playAudio("select", 0);
		}
		current = State.RELEASED;
	}

	public void mouseDragged(MouseEvent event) { // состояние при захвате мыши (нажато/отпущено)
		if(rectangleButton.contains(event.getPoint())) {
			current = State.PRESSED;
		}
		else {
			current = State.RELEASED;
		}
	}

	public void mouseMoved(MouseEvent event) {  // состояние при движении/наведении мыши
		if(rectangleButton.contains(event.getPoint())) {
			current = State.HOVER;
		}
		else {
			current = State.RELEASED;
		}
	}
	
	// состояние: наведено, отпущено, нажато
	private enum State { HOVER, RELEASED, PRESSED }
	
	// геттеры координат и размеров
	public int getX() { return rectangleButton.x; }
	
	public int getY() { return rectangleButton.y; }
	
	public int getWidth() { return rectangleButton.width; }
	
	public int getHeight() { return rectangleButton.height; }
	
	// сеттер текста
	public void setText(String text) { this.text = text; }
}
