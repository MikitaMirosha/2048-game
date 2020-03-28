package mirosha.gui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.HashMap;

// класс отвечает за работу кликов на экране 
// и за активацию определенных панелей для обновления и рендера
public class Screen { 

	private static Screen screen;
	private HashMap<String, PanelButton> panels; // в hash map храним кнопки
	private String currentPanel = ""; // для идентификации какую из панелей использовать

	private Screen() { // в конструкторе класса содержится панель с кнопками в виде hash map
		panels = new HashMap<String, PanelButton>();
	}

	public static Screen getInstance() { // добавляем экземпляр, если его нет
		if (screen == null) {
			screen = new Screen();
		}
		return screen;
	}

	public void addPanel(String name, PanelButton panelButton) { // добавляем в панель кнопки
		panels.put(name, panelButton);
	}

	public void setPanel(String name) { // сеттер для установки панели
		currentPanel = name;
	}
	
	public void updateScreen() { // обновляем панели экрана
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).updatePanel();
		}
	}

	public void render(Graphics2D graphics) { // рендерим панели экрана
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).renderPanel(graphics);
		}
	}

	public void mouseMoved(MouseEvent event) { // поведение активной панели при движении мыши (наведении)
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).mouseMoved(event);
		}
	}
	
	public void mouseDragged(MouseEvent event) { // поведение активной панели при захвате мыши
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).mouseDragged(event);
		}
	}
	
	public void mousePressed(MouseEvent event) { // поведение активной панели при нажатии мыши 
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).mousePressed(event);
		}
	}

	public void mouseReleased(MouseEvent event) { // поведение активной панели при отпускании мыши 
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).mouseReleased(event);
		}
	}
}
