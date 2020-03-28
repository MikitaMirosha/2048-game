package mirosha.game;

import java.awt.event.KeyEvent;

public class Keyboard { // класс для работы с клавиатурой

	public static boolean[] pressedKey = new boolean[50];
	public static boolean[] previousKey = new boolean[50];
	
	public static void updateKeys() {  // обновляет нажатия клавиш
		for(int i = 0; i < 4; i++) {
			if(i == 0) previousKey[KeyEvent.VK_LEFT] = pressedKey[KeyEvent.VK_LEFT];
			if(i == 1) previousKey[KeyEvent.VK_RIGHT] = pressedKey[KeyEvent.VK_RIGHT];
			if(i == 2) previousKey[KeyEvent.VK_UP] = pressedKey[KeyEvent.VK_UP];
			if(i == 3) previousKey[KeyEvent.VK_DOWN] = pressedKey[KeyEvent.VK_DOWN];
		}
	}
	
	public static void isPressed(KeyEvent key) {  // устанавливаем флаг при нажатии клавиши
		pressedKey[key.getKeyCode()] = true;
	}
	
	public static void isReleased(KeyEvent e) { // устанавливаем флаг при отпускании клавиши
		pressedKey[e.getKeyCode()] = false;
	}
}
