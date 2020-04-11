package mirosha.gui;

import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

/**
 * ����� ��� ������ � GUI ��������
 * @author Mirosha
 * @version 1.0
 */
public class PanelButton { 

	/** ���� �������� ������ �� ������ {@link #Button}*/
	private ArrayList<Button> buttons;
	
	/** 
     * ����������� - �������� ������ ������� ������
     * � �������� � ������� ��� ����������� �������
     */
	public PanelButton() {
		buttons = new ArrayList<Button>();
	}
	
	/**
     * ��������� ���������� ������ 
     * @param button - ������
     */
	public void addButton(Button button) {
		buttons.add(button);
	}
	
	/**
     * ��������� �������� ������ 
     * @param button - ������
     */
	public void removeButton(Button button) { 
		buttons.remove(button);
	}
	
	/**
     * ��������� ���������� ������
     */
	public void updatePanel() { 
		for(Button button : buttons) {
			button.update();
		}
	}
	
	/**
     * ��������� ������ ������
     * @param graphics -  �������� ������ ������ ��� �������
     */
	public void renderPanel(Graphics2D graphics) { 
		for(Button button : buttons) {
			button.renderButton(graphics);
		}
	}
	
	/**
     * ��������� ��������� ������ ��� ��������/��������� ����
     * @param event - ��������� ������
     */
	public void mouseMoved(MouseEvent event) { 
		for(Button button : buttons) {
			button.mouseMoved(event);
		}
	}
	
	/**
     * ��������� ��������� ������ ��� ������� �����
     * @param event - ��������� ������
     */
	public void mouseDragged(MouseEvent event) { 
		for(Button button : buttons) {
			button.mouseDragged(event);
		}
	}
	
	/**
     * ��������� ��������� ������ ��� ������� ����
     * @param event - ��������� ������
     */
	public void mousePressed(MouseEvent event) { 
		for(Button button : buttons) {
			button.mousePressed(event);
		}
	}
	
	/**
     * ��������� ��������� ������ ��� ���������� ����
     * @param event - ��������� ������
     */
	public void mouseReleased(MouseEvent event) { 
		for(Button button : buttons) {
			button.mouseReleased(event);
		}
	}
}
