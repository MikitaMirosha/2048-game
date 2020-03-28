package mirosha.gui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PanelButton { // класс для работы с кнопками

	// храним здесь кнопки из класса Button
	private ArrayList<Button> buttons;
	
	// в конструкторе будем хранить все кнопки в массиве
	// далее будем их обновлять и рендерить
	public PanelButton() {
		buttons = new ArrayList<Button>();
	}
	
	public void addButton(Button button) { // добавляем кнопки
		buttons.add(button);
	}
	
	public void removeButton(Button button) { // удаляем кнопки
		buttons.remove(button);
	}
	
	public void updatePanel() { // обновляем панель
		for(Button button : buttons) {
			button.update();
		}
	}
	
	public void renderPanel(Graphics2D graphics) { // рендерим панель
		for(Button button : buttons) {
			button.renderButton(graphics);
		}
	}
	
	public void mouseMoved(MouseEvent event) { // поведение кнопки при движении мыши (наведении)
		for(Button button : buttons) {
			button.mouseMoved(event);
		}
	}
	
	public void mouseDragged(MouseEvent event) { // поведение кнопки при захвате
		for(Button button : buttons) {
			button.mouseDragged(event);
		}
	}
	
	public void mousePressed(MouseEvent event) { // поведение кнопки при нажатии
		for(Button button : buttons) {
			button.mousePressed(event);
		}
	}
	
	public void mouseReleased(MouseEvent event) { // поведение кнопки при отпускании
		for(Button button : buttons) {
			button.mouseReleased(event);
		}
	}
}
