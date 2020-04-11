package mirosha.gui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * Класс для работы с GUI кнопками
 * @author Mirosha
 * @version 1.0
 */
public class PanelButton { 

	/** Поле хранение кнопок из класса {@link #Button}*/
	private ArrayList<Button> buttons;
	
	/** 
     * Конструктор - создание нового объекта кнопки
     * и хранение в массиве для дальнейшего рендера
     */
	public PanelButton() {
		buttons = new ArrayList<Button>();
	}
	
	/**
     * Процедура добавление кнопки 
     * @param button - кнопка
     */
	public void addButton(Button button) {
		buttons.add(button);
	}
	
	/**
     * Процедура удаление кнопки 
     * @param button - кнопка
     */
	public void removeButton(Button button) { 
		buttons.remove(button);
	}
	
	/**
     * Процедура обновление панели
     */
	public void updatePanel() { 
		for(Button button : buttons) {
			button.update();
		}
	}
	
	/**
     * Процедура рендер панели
     * @param graphics -  создание фигуры панели для рендера
     */
	public void renderPanel(Graphics2D graphics) { 
		for(Button button : buttons) {
			button.renderButton(graphics);
		}
	}
	
	/**
     * Процедура состояние кнопки при движении/наведении мыши
     * @param event - состояние кнопки
     */
	public void mouseMoved(MouseEvent event) { 
		for(Button button : buttons) {
			button.mouseMoved(event);
		}
	}
	
	/**
     * Процедура состояние кнопки при захвате мышью
     * @param event - состояние кнопки
     */
	public void mouseDragged(MouseEvent event) { 
		for(Button button : buttons) {
			button.mouseDragged(event);
		}
	}
	
	/**
     * Процедура состояние кнопки при нажатии мыши
     * @param event - состояние кнопки
     */
	public void mousePressed(MouseEvent event) { 
		for(Button button : buttons) {
			button.mousePressed(event);
		}
	}
	
	/**
     * Процедура состояние кнопки при отпускании мыши
     * @param event - состояние кнопки
     */
	public void mouseReleased(MouseEvent event) { 
		for(Button button : buttons) {
			button.mouseReleased(event);
		}
	}
}
