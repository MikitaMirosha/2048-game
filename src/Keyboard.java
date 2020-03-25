package mirosha.game;

import java.awt.event.KeyEvent;

public class Keyboard { // класс для работы клавиатуры

	public static boolean[] pressedKey = new boolean[50]; // массив для нажатых клавиш
	public static boolean[] previousKey= new boolean[50]; // массив для предыдущих клавиш
	
	public static void updateKeys() { // обновляет нажатия клавиш
		for(int i = 0; i < 4; i++) { 
			if(i == 0) previousKey[KeyEvent.VK_LEFT] = pressedKey[KeyEvent.VK_LEFT];
			if(i == 1) previousKey[KeyEvent.VK_RIGHT] = pressedKey[KeyEvent.VK_RIGHT];
			if(i == 2) previousKey[KeyEvent.VK_UP] = pressedKey[KeyEvent.VK_UP];
			if(i == 3) previousKey[KeyEvent.VK_DOWN] = pressedKey[KeyEvent.VK_DOWN];
		}
	}
	
	public static void isPressed(KeyEvent key) { // устанавливаем флаг при нажатии клавиши
		pressedKey[key.getKeyCode()] = true;
	}
	
	public static void isReleased(KeyEvent key) { // устанавливаем флаг при отпускании клавиши
		pressedKey[key.getKeyCode()] = false;
	}
}
