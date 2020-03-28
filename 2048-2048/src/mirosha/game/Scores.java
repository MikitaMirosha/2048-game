package mirosha.game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

public class Scores { // ����� ��� ������ �� ������

	// ������� ����: 
	private int currentScore;
	private int currentTopScore;
	private int[] field = new int[16];

	// �����:
	private String path; // ���� � �����
	private String temp; // ��������� ����
	private GameField gameField;

	private boolean newGame;

	public Scores(GameField gameField) {
		try {
			path = new File("").getAbsolutePath(); // ���������� �������� �����
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		temp = "TEMP.tmp"; // �������� ���������� �����

		this.gameField = gameField;
	}

	public void reset() { // ����� ������ ��� ����� ����
		File file = new File(path, temp);
		if (file.isFile()) {
			file.delete();
		}
		newGame = true;
		currentScore = 0;
	}

	private void createFile() { // ������� ���� ��� ����� ����
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

	public void loadGame() { // ��������� ����
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
	
	public void saveGame() { // ��������� ����
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

	// ������� � ������� ����������� ����
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