package mirosha.gui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.HashMap;

// ����� �������� �� ������ ������ �� ������ 
// � �� ��������� ������������ ������� ��� ���������� � �������
public class Screen { 

	private static Screen screen;
	private HashMap<String, PanelButton> panels; // � hash map ������ ������
	private String currentPanel = ""; // ��� ������������� ����� �� ������� ������������

	private Screen() { // � ������������ ������ ���������� ������ � �������� � ���� hash map
		panels = new HashMap<String, PanelButton>();
	}

	public static Screen getInstance() { // ��������� ���������, ���� ��� ���
		if (screen == null) {
			screen = new Screen();
		}
		return screen;
	}

	public void addPanel(String name, PanelButton panelButton) { // ��������� � ������ ������
		panels.put(name, panelButton);
	}

	public void setPanel(String name) { // ������ ��� ��������� ������
		currentPanel = name;
	}
	
	public void updateScreen() { // ��������� ������ ������
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).updatePanel();
		}
	}

	public void render(Graphics2D graphics) { // �������� ������ ������
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).renderPanel(graphics);
		}
	}

	public void mouseMoved(MouseEvent event) { // ��������� �������� ������ ��� �������� ���� (���������)
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).mouseMoved(event);
		}
	}
	
	public void mouseDragged(MouseEvent event) { // ��������� �������� ������ ��� ������� ����
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).mouseDragged(event);
		}
	}
	
	public void mousePressed(MouseEvent event) { // ��������� �������� ������ ��� ������� ���� 
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).mousePressed(event);
		}
	}

	public void mouseReleased(MouseEvent event) { // ��������� �������� ������ ��� ���������� ���� 
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).mouseReleased(event);
		}
	}
}
