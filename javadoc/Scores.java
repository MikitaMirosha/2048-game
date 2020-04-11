package mirosha.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

/**
 * Класс для работы со счетом
 * @author Mirosha
 * @version 1.0
 */
public class Scores { 

	/** Поле текущий счет*/
	private int currentScore;
	
	/** Поле лучший текущий счет*/
	private int currentTopScore;
	
	/** Поле игровое поле 4х4*/
	private int[] field = new int[16];

	/** Поле путь к файлу*/
	private String path; 
	
	/** Поле временный файл*/
	private String temp; 
	
	/** Поле игровое поле*/
	private GameField gameField;

	/** Поле новая игра*/
	private boolean newGame;

	/** 
     * Конструктор - создание нового объекта счет игры
     * @param gameField - игровое поле
     */
	public Scores(GameField gameField) {
		try {
			path = new File("").getAbsolutePath();	// абсолютное значение файла
		} catch (Throwable ex) {
			System.out.println("Произошла ошибка при работе с файлом");
			ex.getMessage();
		}
		temp = "TEMP.tmp";	// создание временного файла

		this.gameField = gameField;
	}

	/**
     * Процедура сброс данных при новой игре
     */
	public void reset() { 
		File file = new File(path, temp);
		if (file.isFile()) {
			file.delete();
		}
		newGame = true;
		currentScore = 0;
	}

	/**
     * Процедура создание файла при новой игре
     */
	private void createFile() { 
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
		} catch (Throwable ex) {
			System.out.println("Произошла ошибка при работе с файлом");
			ex.getMessage();
		}
	}

	/**
     * Процедура загрузка счета игры
     */
	public void loadGame() { 
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
		} catch (Throwable ex) {
			System.out.println("Произошла ошибка при работе с файлом");
			ex.getMessage();
		}
	}
	
	/**
     * Процедура сохранение счета игры
     */
	public void saveGame() { 
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
		} catch (Throwable ex) {
			System.out.println("Произошла ошибка при работе с файлом");
			ex.getMessage();
		}
	}

	/**
     * Функция получение значения поля {@link #currentTopScore}
     * @return возвращает лучший текущий счет
     */
	public int getCurrentTopScore() {
		return currentTopScore;
	}

	/**
     * Процедура установки лучшего текущего счета
     * @param currentTopScore - лучший текущий счет
     */
	public void setCurrentTopScore(int currentTopScore) {
		this.currentTopScore = currentTopScore;
	}
	
	/**
     * Функция получение значения поля {@link #currentScore}
     * @return возвращает текущий счет
     */
	public int getCurrentScore() {
		return currentScore;
	}

	/**
     * Процедура установки текущего счета
     * @param currentScore - текущий счет
     */
	public void setCurrentScore(int currentScore) {
		this.currentScore = currentScore;
	}
	
	/**
     * Функция установки значения флага поля {@link #newGame}
     * @return возвращает флаг новой игры
     */
	public boolean newGame() {
		return newGame;
	}

	/**
     * Функция получение значения поля {@link #field}
     * @return возвращает значение field
     */
	public int[] getField() {
		return field;
	}
}