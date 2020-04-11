package mirosha.gui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.HashMap;

/**
 * Класс для обработки и активации нужных панелей при кликах мыши
 * @author Mirosha
 * @version 1.0
 */
public class Screen { 

	/** Поле экран*/
	private static Screen screen;
	
	/** Поле для хранения кнопок в hash map*/
	private HashMap<String, PanelButton> panels; 
	
	/** Поле для идентификации активной панели*/
	private String currentPanel = ""; 

	/** 
     * Конструктор - создание нового объекта определенной панели в виде hash map
     */
	private Screen() { 
		panels = new HashMap<String, PanelButton>();
	}

	/** Метод добавление нового экземпляра (Singleton)*/
	public static Screen getInstance() {
		if (screen == null) {
			screen = new Screen();
		}
		return screen;
	}

	/**
     * Процедура добавление кнопки в панель
     * @param name - название кнопки
     * @param panelButton - панель кнопки
     */
	public void addPanel(String name, PanelButton panelButton) { 
		panels.put(name, panelButton);
	}

	/**
     * Процедура установка панели
     * @param name - название текущей панели
     */
	public void setPanel(String name) { 
		currentPanel = name;
	}
	
	/**
     * Процедура обновление панели экрана
     */
	public void updateScreen() { 
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).updatePanel();
		}
	}

	/**
     * Процедура рендер панели экрана
     * @param graphics - графика текущей панели экрана
     */
	public void render(Graphics2D graphics) { 
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).renderPanel(graphics);
		}
	}

	/**
     * Процедура поведение активной панели при движении/наведении мыши 
     * @param event - текущее событие на панели
     */
	public void mouseMoved(MouseEvent event) { 
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).mouseMoved(event);
		}
	}
	
	/**
     * Процедура поведение активной панели при захвате мыши
     * @param event - текущее событие на панели
     */
	public void mouseDragged(MouseEvent event) { 
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).mouseDragged(event);
		}
	}
	
	/**
     * Процедура поведение активной панели при нажатии мыши 
     * @param event - текущее событие на панели
     */
	public void mousePressed(MouseEvent event) { 
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).mousePressed(event);
		}
	}

	/**
     * Процедура поведение активной панели при отпускании мыши  
     * @param event - текущее событие на панели
     */
	public void mouseReleased(MouseEvent event) { 
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).mouseReleased(event);
		}
	}
}
