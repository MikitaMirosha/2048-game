package mirosha.game;

import javax.swing.JFrame;

/**
 * ����� ����� ����� � ����������
 * @author Mirosha
 * @version 1.0
 */
public class Launch {  
	
	public static void main(String[] args) {
		
		/** 
	     * ����������� - �������� ������ ������� ����
	     */
		Game game = new Game(); 
		
		JFrame window = new JFrame("2048 Java Game by Nikita Mirosha"); 
		
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // �������� �� �������
		window.setResizable(true); // ��������� ��������� ������� ����
		window.add(game); // ���������� ������ ������� game
		window.pack(); // �������������� ��������� ������� ���� ��� ����������
		window.setLocationRelativeTo(null); // ��������� GUI �� ������
		window.setVisible(true); // ��������� ��������� �������
		
		game.start(); // ������ ���������� ������
	}
}