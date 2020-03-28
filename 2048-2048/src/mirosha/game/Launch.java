package mirosha.game;

import javax.swing.JFrame;

public class Launch {  // ����� ����� �����
	
	public static void main(String[] args) {
		Game game = new Game(); // ������� ����������� ����
		JFrame window = new JFrame("2048 Java Game by Nikita Mirosha"); // ����� ������� ������ ����
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // �������� �� �������
		window.setResizable(true); // ��������� �������� ������ ����
		window.add(game); // ��������� ����� ������� game
		window.pack(); // ������������� ����������� ������ ���� ��� ����������
		window.setLocationRelativeTo(null); // null ���������� gui �� ������
		window.setVisible(true); // ������������� ��������� �������
		game.start(); // ��������� ��������� �����
	}
}