package mirosha.game;

import javax.swing.JFrame;

/**
 * Класс точка входа в приложение
 * @author Mirosha
 * @version 1.0
 */
public class Launch {  
	
	public static void main(String[] args) {
		
		/** 
	     * Конструктор - создание нового объекта игра
	     */
		Game game = new Game(); 
		
		JFrame window = new JFrame("2048 Java Game by Nikita Mirosha"); 
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // закрытие на крестик
		window.setResizable(true); // запрещено изменение размера окна
		window.add(game); // заполнение фрейма классом game
		window.pack(); // автоматическая настройка размера окна под содержимое
		window.setLocationRelativeTo(null); // центровка GUI на экране
		window.setVisible(true); // установка видимости графики
		
		game.start(); // запуск начального потока
	}
}