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

public class Button { // ����� ��� �������� ������
	
	private Color standart;
	private Color hover;
	private Color pressed;
	private String text = "";
	private SetAudio audio;
	private State current = State.RELEASED; // ����������� ���������: ��������
	private Rectangle rectangleButton; // �������� ������
	private ArrayList<ActionListener> actionListeners; // �������� �������� � �������� � �������
	private Font font = Game.main.deriveFont(25f); // ����� ������ �� ������
	
	public Button(int x, int y, int width, int height) { // ����������� ������
		
		rectangleButton = new Rectangle(x, y, width, height); // �������� �������� ������
		actionListeners = new ArrayList<ActionListener>(); // �������� �������� � ��������
		
		standart = new Color(191, 11, 11); // ����������� �����-����� ����
		hover = new Color(220, 39, 39); // ����� ����� ���� ��� ���������
		pressed = new Color(229, 65, 65); // ��� ����� ����� ���� ��� �������
		
		// ������ ����� ��� ������� �� ������
		audio = SetAudio.getInstance();
		audio.loadAudio("select.wav", "select");
	}
	
	public void renderButton(Graphics2D graphics) { // ������ ������
		if(current == State.RELEASED) { // ������ ��� ����������
			graphics.setColor(standart);
			graphics.fill(rectangleButton);
		}
		else if(current == State.PRESSED) { // ������ ��� �������
			graphics.setColor(pressed);
			graphics.fill(rectangleButton);
		}
		else { // ������ ��� ���������
			graphics.setColor(hover);
			graphics.fill(rectangleButton);
		}
		graphics.setColor(Color.white);
		graphics.setFont(font); // ���������� ��������� ������ �� ������ �� ������:
		graphics.drawString(text, rectangleButton.x + rectangleButton.width / 2  - DisplayObject.getObjectWidth(text, font, graphics) / 2, rectangleButton.y + rectangleButton.height / 2  + DisplayObject.getObjectHeight(text, font, graphics) / 2);
	}
	
	public void update() {}
	
	public void addActionListener(ActionListener listener) { // ��������� �������� � �������
		actionListeners.add(listener);
	}
	
	public void mousePressed(MouseEvent event) { // ��������� ��� ������� ����
		if(rectangleButton.contains(event.getPoint())) {
			current = State.PRESSED;
		}
	}

	public void mouseReleased(MouseEvent event) { // ��������� ��� ���������� ����
		if(rectangleButton.contains(event.getPoint())) {
			for(ActionListener i : actionListeners) {
				i.actionPerformed(null);
			}
			audio.playAudio("select", 0);
		}
		current = State.RELEASED;
	}

	public void mouseDragged(MouseEvent event) { // ��������� ��� ������� ���� (������/��������)
		if(rectangleButton.contains(event.getPoint())) {
			current = State.PRESSED;
		}
		else {
			current = State.RELEASED;
		}
	}

	public void mouseMoved(MouseEvent event) {  // ��������� ��� ��������/��������� ����
		if(rectangleButton.contains(event.getPoint())) {
			current = State.HOVER;
		}
		else {
			current = State.RELEASED;
		}
	}
	
	// ���������: ��������, ��������, ������
	private enum State { HOVER, RELEASED, PRESSED }
	
	// ������� ��������� � ��������
	public int getX() { return rectangleButton.x; }
	
	public int getY() { return rectangleButton.y; }
	
	public int getWidth() { return rectangleButton.width; }
	
	public int getHeight() { return rectangleButton.height; }
	
	// ������ ������
	public void setText(String text) { this.text = text; }
}
