package mirosha.game;

import java.awt.event.KeyEvent;

/**
 * ����� ��� ������ � �����������
 * @author Mirosha
 * @version 1.0
 */
public class Keyboard { 

	/** ���� ������� �������*/
	public static boolean[] pressedKey = new boolean[50];
	
	/** ���� ���������� ������� �������*/
	public static boolean[] previousKey = new boolean[50];
	
	/**
     * ��������� ���������� ������� ������
     */
	public static void updateKeys() { 
		for(int i = 0; i < 4; i++) {
			if(i == 0) previousKey[KeyEvent.VK_LEFT] = pressedKey[KeyEvent.VK_LEFT];
			if(i == 1) previousKey[KeyEvent.VK_RIGHT] = pressedKey[KeyEvent.VK_RIGHT];
			if(i == 2) previousKey[KeyEvent.VK_UP] = pressedKey[KeyEvent.VK_UP];
			if(i == 3) previousKey[KeyEvent.VK_DOWN] = pressedKey[KeyEvent.VK_DOWN];
		}
	}
	
	/**
     * ��������� ��������� ����� ��� ������� �������
     */
	public static void isPressed(KeyEvent key) { 
		pressedKey[key.getKeyCode()] = true;
	}
	
	/**
     * ��������� ��������� ����� ��� ���������� �������
     */
	public static void isReleased(KeyEvent e) {
		pressedKey[e.getKeyCode()] = false;
	}
}
