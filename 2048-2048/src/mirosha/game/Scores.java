package mirosha.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class Scores { // класс для работы со счетом

	// текущий счет: 
	private int currentScore;
	private int currentTopScore;
	private int[] field = new int[16];

	// файлы:
	private String path; // путь к файлу
	private String temp; // временный файл
	private GameField gameField;

	private boolean newGame;

	public Scores(GameField gameField) {
		try {
			path = new File("").getAbsolutePath(); // абсолютное значение файла
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		temp = "TEMP.tmp"; // создание временного файла

		this.gameField = gameField;
	}

	public void reset() { // сброс данных при новой игре
		File file = new File(path, temp);
		if (file.isFile()) {
			file.delete();
		}
		newGame = true;
		currentScore = 0;
	}

	private void createFile() { // создаем файл при новой игре
		FileWriter output = null;
		newGame = true;
		try {
			File file = new File(path, temp);
			output = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(output);
			writer.write("" + 0);
			writer.newLine();
			writer.write("" + 0);
			writer.newLine();
			writer.write("" + 0);
			writer.newLine();
			writer.write("" + 0);
			writer.newLine();
			for (int row = 0; row < GameField.ROWS; row++) {
				for (int col = 0; col < GameField.COLS; col++) {
					if(row == GameField.ROWS - 1 && col == GameField.COLS - 1) {
						writer.write("" + 0);
					}
					else {
						writer.write(0 + "-");
					}
				}
			}
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void loadGame() { // загружаем игру
		try {
			File file = new File(path, temp);

			if (!file.isFile()) {
				createFile();
			}

			BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			currentScore = Integer.parseInt(reader.readLine());
			currentTopScore = Integer.parseInt(reader.readLine());

			String[] field = reader.readLine().split("-");
			for (int i = 0; i < field.length; i++) {
				this.field[i] = Integer.parseInt(field[i]);
			}
			reader.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void saveGame() { // сохраняем игру
		FileWriter output = null;
		if (newGame) newGame = false;
		try {
			File file = new File(path, temp);
			output = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(output);
			writer.write("" + currentScore);
			writer.newLine();
			writer.write("" + currentTopScore);
			writer.newLine();
			for (int row = 0; row < GameField.ROWS; row++) {
				for (int col = 0; col < GameField.COLS; col++) {
					this.field[row * GameField.COLS + col] = gameField.getField()[row][col] != null ? gameField.getField()[row][col].getValue() : 0;
					if (row == GameField.ROWS - 1 && col == GameField.COLS - 1)
						writer.write("" + field[row * GameField.COLS + col]);
					else writer.write(field[row * GameField.COLS + col] + "-");
				}
			}
			writer.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	// геттеры и сеттеры результатов игры
	public int getCurrentTopScore() {
		return currentTopScore;
	}

	public void setCurrentTopScore(int currentTopScore) {
		this.currentTopScore = currentTopScore;
	}
	
	public int getCurrentScore() {
		return currentScore;
	}

	public void setCurrentScore(int currentScore) {
		this.currentScore = currentScore;
	}
	
	public boolean newGame() {
		return newGame;
	}

	public int[] getField() {
		return field;
	}
}