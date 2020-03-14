package com.mirosha.game;

import javax.swing.JFrame;

public class Launch {
	public static void main(String[] args) {
		Game game = new Game();
		JFrame mainWindow = new JFrame("2048 Java Game by Nikita Mirosha");
		mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainWindow.setResizable(false);
		mainWindow.add(game); // ��������� ����� ������� game
		mainWindow.pack(); // ������������� ����������� ������ ���� ��� ����������
		mainWindow.setLocationRelativeTo(null); // null ���������� gui �� ������
		mainWindow.setVisible(true); 
		game.start(); // ��������� ��������� �����
	}
}

