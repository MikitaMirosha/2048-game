package com.mirosha.game;

import javax.swing.JFrame;

public class Launch {
	public static void main(String[] args) {
		Game game = new Game();
		JFrame mainWindow = new JFrame("2048 Java Game by Nikita Mirosha");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setResizable(false);
		mainWindow.add(game); // заполняет фрейм классом game
		mainWindow.pack(); // автоматически настраивает размер окна под содержимое
		mainWindow.setLocationRelativeTo(null); // null центрирует gui на экране
		mainWindow.setVisible(true); 
		game.start(); // запускает начальный поток
	}
}

