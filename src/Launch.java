package mirosha.game;

import javax.swing.JFrame;

public class Launch { // класс точки входа

	public static void main(String[] args) {
		Game game = new Game(); // создаем конструктор игры
		JFrame window = new JFrame("2048 Java Game by Nikita Mirosha"); // текст верхней панели окна
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // закрытие на крестик
		window.setResizable(false); // запрещено изменять размер окна
		window.add(game); // заполняем фрейм классом game
		window.pack(); // автоматически настраивает размер окна под содержимое
		window.setLocationRelativeTo(null); // null центрирует gui на экране
		window.setVisible(true); // устаннавливаем видимость графики
		game.start(); // запускаем начальный поток
	}
}

