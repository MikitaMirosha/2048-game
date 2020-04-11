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

/**
 * Класс для создания GUI кнопок 
 * @author Mirosha
 * @version 1.0
 */
public class Button { 
	
	/** Поле цвет кнопки при бездействии*/
	private Color standart;
	
	/** Поле цвет кнопки при наведении*/
	private Color hover;
	
	/** Поле цвет кнопки при нажатии*/
	private Color pressed;
	
	/** Поле текст на кнопке*/
	private String text = "";
	
	/** Поле звук при нажатии на кнопку*/
	private SetAudio audio;
	
	/** Поле стандартное состояние кнопки при бездействии*/
	private State current = State.RELEASED; 
	
	/** Поле оболочка кнопки*/
	private Rectangle rectangleButton;
	
	/** Поле хранение действий с кнопками*/
	private ArrayList<ActionListener> actionListeners; 
	
	/** Поле шрифт текста на кнопке*/
	private Font font = Game.main.deriveFont(25f); 
	
	/** 
     * Конструктор - создание нового объекта (кнопка) с определенными значениями
     * @param x - координата x
     * @param y - координата y
     * @param width - ширина кнопки
     * @param height - высота кнопки
     */
	public Button(int x, int y, int width, int height) { 
		
		rectangleButton = new Rectangle(x, y, width, height); 		// оболочка кнопки
		actionListeners = new ArrayList<ActionListener>(); 		    // хранение действий	
		
		standart = new Color(191, 11, 11); 		
		hover = new Color(220, 39, 39); 		
		pressed = new Color(229, 65, 65);
		
		audio = SetAudio.getInstance();
		audio.loadAudio("select.wav", "select");
	}
	
	/**
     * Процедура рендер кнопки 
     * @param graphics - создание фигуры кнопки для рендера
     */
	public void renderButton(Graphics2D graphics) { 
		if(current == State.RELEASED) { 			// рендер при отпускании
			graphics.setColor(standart);
			graphics.fill(rectangleButton);
		}
		else if(current == State.PRESSED) {			// рендер при нажатии
			graphics.setColor(pressed);
			graphics.fill(rectangleButton);
		}
		else { 										// рендер при наведении
			graphics.setColor(hover);
			graphics.fill(rectangleButton);
		}
		graphics.setColor(Color.white);
		graphics.setFont(font); 
		graphics.drawString(text, rectangleButton.x + rectangleButton.width / 2  - DisplayObject.getObjectWidth(text, font, graphics) / 2, rectangleButton.y + rectangleButton.height / 2  + DisplayObject.getObjectHeight(text, font, graphics) / 2);
	}
	
	/**
     * Процедура обновление кнопки 
     */
	public void update() {}
	
	/**
     * Процедура добавление действия с кнопкой
     * @param listener - прием событий с кнопкой
     */
	public void addActionListener(ActionListener listener) { 
		actionListeners.add(listener);
	}
	
	/**
     * Процедура состояние кнопки при нажатии мыши
     * @param event - состояние кнопки
     */
	public void mousePressed(MouseEvent event) { 
		if(rectangleButton.contains(event.getPoint())) {
			current = State.PRESSED;
		}
	}

	/**
     * Процедура состояние кнопки при отпускании мыши
     * @param event - состояние кнопки
     */
	public void mouseReleased(MouseEvent event) {
		if(rectangleButton.contains(event.getPoint())) {
			for(ActionListener i : actionListeners) {
				i.actionPerformed(null);
			}
			audio.playAudio("select", 0);
		}
		current = State.RELEASED;
	}

	/**
     * Процедура состояние кнопки при захвате мышью
     * @param event - состояние кнопки
     */
	public void mouseDragged(MouseEvent event) { 
		if(rectangleButton.contains(event.getPoint())) {
			current = State.PRESSED;
		}
		else {
			current = State.RELEASED;
		}
	}

	/**
     * Процедура состояние кнопки при движении/наведении мыши
     * @param event - состояние кнопки
     */
	public void mouseMoved(MouseEvent event) {  
		if(rectangleButton.contains(event.getPoint())) {
			current = State.HOVER;
		}
		else {
			current = State.RELEASED;
		}
	}
	
	/**
     * Перечесление состояний кнопки при действиях мыши
     * состояния: наведено, отпущено, нажато
     */
	private enum State { HOVER, RELEASED, PRESSED }
	
	/**
     * Функция получения значения поля {@link #rectangleButton.x}
     * @return возвращает координату Х кнопки
     */
	public int getX() { return rectangleButton.x; }
	
	/**
     * Функция получения значения поля {@link #rectangleButton.y}
     * @return возвращает координату Y кнопки
     */
	public int getY() { return rectangleButton.y; }
	
	/**
     * Функция получения значения поля {@link #rectangleButton.width}
     * @return возвращает ширину кнопки
     */
	public int getWidth() { return rectangleButton.width; }
	
	/**
     * Функция получения значения поля {@link #rectangleButton.height}
     * @return возвращает высоту кнопки
     */
	public int getHeight() { return rectangleButton.height; }
	
	/**
     * Процедура установки текста на кнопке
     * @param text - текст кнопки
     */
	public void setText(String text) { this.text = text; }
}
