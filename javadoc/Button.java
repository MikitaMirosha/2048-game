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
 * ����� ��� �������� GUI ������ 
 * @author Mirosha
 * @version 1.0
 */
public class Button { 
	
	/** ���� ���� ������ ��� �����������*/
	private Color standart;
	
	/** ���� ���� ������ ��� ���������*/
	private Color hover;
	
	/** ���� ���� ������ ��� �������*/
	private Color pressed;
	
	/** ���� ����� �� ������*/
	private String text = "";
	
	/** ���� ���� ��� ������� �� ������*/
	private SetAudio audio;
	
	/** ���� ����������� ��������� ������ ��� �����������*/
	private State current = State.RELEASED; 
	
	/** ���� �������� ������*/
	private Rectangle rectangleButton;
	
	/** ���� �������� �������� � ��������*/
	private ArrayList<ActionListener> actionListeners; 
	
	/** ���� ����� ������ �� ������*/
	private Font font = Game.main.deriveFont(25f); 
	
	/** 
     * ����������� - �������� ������ ������� (������) � ������������� ����������
     * @param x - ���������� x
     * @param y - ���������� y
     * @param width - ������ ������
     * @param height - ������ ������
     */
	public Button(int x, int y, int width, int height) { 
		
		rectangleButton = new Rectangle(x, y, width, height); 		// �������� ������
		actionListeners = new ArrayList<ActionListener>(); 		    // �������� ��������	
		
		standart = new Color(191, 11, 11); 		
		hover = new Color(220, 39, 39); 		
		pressed = new Color(229, 65, 65);
		
		audio = SetAudio.getInstance();
		audio.loadAudio("select.wav", "select");
	}
	
	/**
     * ��������� ������ ������ 
     * @param graphics - �������� ������ ������ ��� �������
     */
	public void renderButton(Graphics2D graphics) { 
		if(current == State.RELEASED) { 			// ������ ��� ����������
			graphics.setColor(standart);
			graphics.fill(rectangleButton);
		}
		else if(current == State.PRESSED) {			// ������ ��� �������
			graphics.setColor(pressed);
			graphics.fill(rectangleButton);
		}
		else { 										// ������ ��� ���������
			graphics.setColor(hover);
			graphics.fill(rectangleButton);
		}
		graphics.setColor(Color.white);
		graphics.setFont(font); 
		graphics.drawString(text, rectangleButton.x + rectangleButton.width / 2  - DisplayObject.getObjectWidth(text, font, graphics) / 2, rectangleButton.y + rectangleButton.height / 2  + DisplayObject.getObjectHeight(text, font, graphics) / 2);
	}
	
	/**
     * ��������� ���������� ������ 
     */
	public void update() {}
	
	/**
     * ��������� ���������� �������� � �������
     * @param listener - ����� ������� � �������
     */
	public void addActionListener(ActionListener listener) { 
		actionListeners.add(listener);
	}
	
	/**
     * ��������� ��������� ������ ��� ������� ����
     * @param event - ��������� ������
     */
	public void mousePressed(MouseEvent event) { 
		if(rectangleButton.contains(event.getPoint())) {
			current = State.PRESSED;
		}
	}

	/**
     * ��������� ��������� ������ ��� ���������� ����
     * @param event - ��������� ������
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
     * ��������� ��������� ������ ��� ������� �����
     * @param event - ��������� ������
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
     * ��������� ��������� ������ ��� ��������/��������� ����
     * @param event - ��������� ������
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
     * ������������ ��������� ������ ��� ��������� ����
     * ���������: ��������, ��������, ������
     */
	private enum State { HOVER, RELEASED, PRESSED }
	
	/**
     * ������� ��������� �������� ���� {@link #rectangleButton.x}
     * @return ���������� ���������� � ������
     */
	public int getX() { return rectangleButton.x; }
	
	/**
     * ������� ��������� �������� ���� {@link #rectangleButton.y}
     * @return ���������� ���������� Y ������
     */
	public int getY() { return rectangleButton.y; }
	
	/**
     * ������� ��������� �������� ���� {@link #rectangleButton.width}
     * @return ���������� ������ ������
     */
	public int getWidth() { return rectangleButton.width; }
	
	/**
     * ������� ��������� �������� ���� {@link #rectangleButton.height}
     * @return ���������� ������ ������
     */
	public int getHeight() { return rectangleButton.height; }
	
	/**
     * ��������� ��������� ������ �� ������
     * @param text - ����� ������
     */
	public void setText(String text) { this.text = text; }
}
