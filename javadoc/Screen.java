package mirosha.gui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.HashMap;

/**
 * ����� ��� ��������� � ��������� ������ ������� ��� ������ ����
 * @author Mirosha
 * @version 1.0
 */
public class Screen { 

	/** ���� �����*/
	private static Screen screen;
	
	/** ���� ��� �������� ������ � hash map*/
	private HashMap<String, PanelButton> panels; 
	
	/** ���� ��� ������������� �������� ������*/
	private String currentPanel = ""; 

	/** 
     * ����������� - �������� ������ ������� ������������ ������ � ���� hash map
     */
	private Screen() { 
		panels = new HashMap<String, PanelButton>();
	}

	/** ����� ���������� ������ ���������� (Singleton)*/
	public static Screen getInstance() {
		if (screen == null) {
			screen = new Screen();
		}
		return screen;
	}

	/**
     * ��������� ���������� ������ � ������
     * @param name - �������� ������
     * @param panelButton - ������ ������
     */
	public void addPanel(String name, PanelButton panelButton) { 
		panels.put(name, panelButton);
	}

	/**
     * ��������� ��������� ������
     * @param name - �������� ������� ������
     */
	public void setPanel(String name) { 
		currentPanel = name;
	}
	
	/**
     * ��������� ���������� ������ ������
     */
	public void updateScreen() { 
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).updatePanel();
		}
	}

	/**
     * ��������� ������ ������ ������
     * @param graphics - ������� ������� ������ ������
     */
	public void render(Graphics2D graphics) { 
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).renderPanel(graphics);
		}
	}

	/**
     * ��������� ��������� �������� ������ ��� ��������/��������� ���� 
     * @param event - ������� ������� �� ������
     */
	public void mouseMoved(MouseEvent event) { 
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).mouseMoved(event);
		}
	}
	
	/**
     * ��������� ��������� �������� ������ ��� ������� ����
     * @param event - ������� ������� �� ������
     */
	public void mouseDragged(MouseEvent event) { 
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).mouseDragged(event);
		}
	}
	
	/**
     * ��������� ��������� �������� ������ ��� ������� ���� 
     * @param event - ������� ������� �� ������
     */
	public void mousePressed(MouseEvent event) { 
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).mousePressed(event);
		}
	}

	/**
     * ��������� ��������� �������� ������ ��� ���������� ����  
     * @param event - ������� ������� �� ������
     */
	public void mouseReleased(MouseEvent event) { 
		if (panels.get(currentPanel) != null) {
			panels.get(currentPanel).mouseReleased(event);
		}
	}
}
