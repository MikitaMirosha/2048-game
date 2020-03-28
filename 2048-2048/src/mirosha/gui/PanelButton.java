package mirosha.gui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class PanelButton { // ����� ��� ������ � ��������

	// ������ ����� ������ �� ������ Button
	private ArrayList<Button> buttons;
	
	// � ������������ ����� ������� ��� ������ � �������
	// ����� ����� �� ��������� � ���������
	public PanelButton() {
		buttons = new ArrayList<Button>();
	}
	
	public void addButton(Button button) { // ��������� ������
		buttons.add(button);
	}
	
	public void removeButton(Button button) { // ������� ������
		buttons.remove(button);
	}
	
	public void updatePanel() { // ��������� ������
		for(Button button : buttons) {
			button.update();
		}
	}
	
	public void renderPanel(Graphics2D graphics) { // �������� ������
		for(Button button : buttons) {
			button.renderButton(graphics);
		}
	}
	
	public void mouseMoved(MouseEvent event) { // ��������� ������ ��� �������� ���� (���������)
		for(Button button : buttons) {
			button.mouseMoved(event);
		}
	}
	
	public void mouseDragged(MouseEvent event) { // ��������� ������ ��� �������
		for(Button button : buttons) {
			button.mouseDragged(event);
		}
	}
	
	public void mousePressed(MouseEvent event) { // ��������� ������ ��� �������
		for(Button button : buttons) {
			button.mousePressed(event);
		}
	}
	
	public void mouseReleased(MouseEvent event) { // ��������� ������ ��� ����������
		for(Button button : buttons) {
			button.mouseReleased(event);
		}
	}
}
