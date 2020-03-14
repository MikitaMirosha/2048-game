package com.mirosha.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.util.Random;
import javax.sound.sampled.Clip;

public class GameField {
	public static final int ROWS = 4;
	public static final int COLS = 4;
	
	private int x; // x, y - ������� ��� ��������� �� ������
	private int y;
	private int score = 0;
	private int highScore = 0;
	private Font scoreFont;
	private SetAudio audio;  
	private final int initialCubesSpawn = 2; // ���-�� ��������� ������� ������
	private Cube[][] field; // ����������� ��� ��� ������ �� ����� � ��������� � �������
	private boolean isDead; // ! ��������
	private boolean isWin; // ! ��������
	private BufferedImage gameField; // ��� ���� �������� ����
	private BufferedImage finalField; // ��� ���������� ����: ���, ������ � �.�.
	
	private static int DISTANCE = 10; // ���������� ����� ��������
	public static int FIELD_WIDTH = (COLS + 1) * DISTANCE + COLS * Cube.WIDTH;
	public static int FIELD_HEIGHT = (ROWS + 1) * DISTANCE + ROWS * Cube.HEIGHT;
	
	private String saveDataPath;  
	private String fileName = "Data"; 
	
	public GameField(int x, int y) {
		try {
			saveDataPath = GameField.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		scoreFont = Game.main.deriveFont(24f);
		this.x = x; // ���������� ���������
		this.y = y;
		field = new Cube[ROWS][COLS];
		gameField = new BufferedImage(FIELD_WIDTH, FIELD_HEIGHT, BufferedImage.TYPE_INT_RGB);
		finalField = new BufferedImage(FIELD_WIDTH, FIELD_HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		loadHighScore();  
		makeFieldImage();
		start(); // ����� ��������� ������
		
		audio = SetAudio.getSample();
		audio.loadSound("main.mp3", "main"); 
		audio.loadSound("move.wav", "move");
		audio.controlVolume("main", -20);
		audio.controlVolume("move", -45);
		audio.play("main", Clip.LOOP_CONTINUOUSLY);
	}
	
	private void saveData() {
		try {
			File file = new File(saveDataPath, fileName);
			FileWriter output = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(output);
			writer.write("" + 0);
			writer.newLine();
			writer.write("" + Integer.MAX_VALUE);
			writer.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void loadHighScore() {
		try {
			File file = new File(saveDataPath, fileName);
			if(!file.isFile()) {
				saveData();
			}
			
			BufferedReader bufferReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			highScore = Integer.parseInt(bufferReader.readLine());
			bufferReader.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void setHighScore() {
		FileWriter output = null;
		try {
			File file = new File(saveDataPath, fileName);
			output = new FileWriter(file);
			BufferedWriter writer = new BufferedWriter(output);
			writer.write("" + highScore);
		    writer.newLine();
			writer.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private void makeFieldImage() {
		Graphics2D fieldImage = (Graphics2D) gameField.getGraphics();
		fieldImage.setColor(Color.darkGray);
		fieldImage.fillRect(0, 0, FIELD_WIDTH, FIELD_HEIGHT);
		fieldImage.setColor(Color.lightGray);
		
		for(int row = 0; row < ROWS; row++) { // ��������� ������� � �������
			for(int col = 0; col < COLS; col++) {
				int x = DISTANCE + DISTANCE * col + Cube.WIDTH * col; // ������� ���������
				int y = DISTANCE + DISTANCE * row + Cube.HEIGHT * row;
				fieldImage.fillRoundRect(x, y, Cube.WIDTH, Cube.HEIGHT, Cube.ARC_WIDTH, Cube.ARC_HEIGHT);
			}
		}
	}
	
	private void start() {
		for(int i = 0; i < initialCubesSpawn; i++) {
			randomSpawn();
		}
	}
	
	private void randomSpawn() {
		Random random = new Random();
		boolean disabled = true;
		
		while(disabled) {
			int pos = random.nextInt(ROWS * COLS);
			int row = pos / ROWS;
			int col = pos % COLS;
			Cube current = field[row][col];
			if(current == null) { // ������� ����� ������
				int value = random.nextInt(10) < 9 ? 2 : 4; // ���� ��������� ����� �� 0 �� 9 (90% - ����� ��� 2, 10% - ��� 4)
				Cube cube = new Cube(value, getCubeX(col), getCubeY(row));
				field[row][col] = cube;
				disabled = false;
			}
		}
	}
	
	public int getCubeX(int col) {
		return DISTANCE + col * Cube.WIDTH + col * DISTANCE;
	}
	
	public int getCubeY(int row) { 
		return DISTANCE + row * Cube.HEIGHT + row * DISTANCE;
	}
	
	public void render(Graphics2D finalFieldImage) { // ������� ��� final field 
		Graphics2D image2D = (Graphics2D)finalField.getGraphics();
		image2D.drawImage(gameField, 0, 0, null);
		
		for(int row = 0; row < ROWS; row++) {
			for(int col = 0; col < COLS; col++) {
				Cube current = field[row][col];
				if(current == null) continue; // � ���� ������ �� ��������
				current.render(image2D);
			}
		}
		
		finalFieldImage.drawImage(finalField, x, y, null);
		image2D.dispose();
		finalFieldImage.setColor(Color.lightGray);
		finalFieldImage.setFont(scoreFont);
		finalFieldImage.drawString("Score: " + score, 30, 40);
		finalFieldImage.setColor(Color.red);
		finalFieldImage.drawString("Best: " + highScore, Game.WIDTH - DrawBar.getBarWidth("Best: " + highScore, scoreFont, finalFieldImage) - 20, 40);
	}
	
	public void update() {
		checkKey(); 
		
		if(score >= highScore)
			highScore = score;
		
		for(int row = 0; row < ROWS; row++) {
			for(int col = 0; col < COLS; col++) {
				Cube current = field[row][col];
				if(current == null) continue;
				current.update();
				resetPos(current, row, col); // ����� �������
				if(current.getValue() == 2048) { 
					isWin = true;  
				}
			}
		}
	}
	
	private void resetPos(Cube current, int row, int col) {
		if(current == null) return; // �� ����� �������
		
		int x = getCubeX(col);
		int y = getCubeY(row);
		// ���� ������ ���������, ��� �������� ���������� x, y
		int distX = current.getX() - x;
		int distY = current.getY() - y;
		
		// math.abs - �������� ������ �����
		// ������ ������������ � ����� ����� � 28 �������� �� 1 update
		// � ���� ��� ������ 10 �������� ��� �����������,
		// �� ������ ����������� 20 ��������, ��� ���������� �� 10
		if(Math.abs(distX) < Cube.SLIDING_SPEED) {
			current.setX(current.getX() - distX);
		}
		
		if(Math.abs(distY) < Cube.SLIDING_SPEED) {
			current.setY(current.getY() - distY);
		}
		
		if(distX < 0) {
			current.setX(current.getX() + Cube.SLIDING_SPEED);
		}
		
		if(distY < 0) {
			current.setY(current.getY() + Cube.SLIDING_SPEED);
		}
		
		if (distX > 0) {
			current.setX(current.getX() - Cube.SLIDING_SPEED);
		}
		
		if(distY > 0) {
			current.setY(current.getY() - Cube.SLIDING_SPEED);
		}
	}
	
	private boolean moveCubes(int row, int col, int horDirection, int verDirection, CubesDirection direct) {
		boolean moveAbility = false;
		
		Cube current = field[row][col];
		if(current == null) return false; // ��� ����������� �������
		boolean move = true; 
		int newCol = col;
		int newRow = row;
		 
		while(move) {
			newCol += horDirection;
			newRow += verDirection;
			if(checkBounds(direct, newRow, newCol)) break; // �� ����� �����������, ������� �� �����
			if(field[newRow][newCol] == null) { // ����� �����������
				field[newRow][newCol] = current; // ����� ����� ������ � ������� �� ������ �����
				field[newRow - verDirection][newCol - horDirection] = null;
				field[newRow][newCol].setSlideTo(new Spot(newRow, newCol));
				moveAbility = true;
			}
			// ����� ����������� � �������������
			else if(field[newRow][newCol].getValue() == current.getValue() && field[newRow][newCol].combineAbility()) { 
				field[newRow][newCol].setCombineAbility(false); // ������ ������ ������������� 
				field[newRow][newCol].setValue(field[newRow][newCol].getValue() * 2); // ����������� ������� �� 2
				moveAbility = true;
				field[newRow - verDirection][newCol - horDirection] = null;
				field[newRow][newCol].setSlideTo(new Spot(newRow, newCol));
            	field[newRow][newCol].setCombineAnimation(true);
				score += field[newRow][newCol].getValue(); // ����� ���� 
			}
			else {
				move = false; // �� ������ ������ (fix)
			}
		}
		return moveAbility;
	}
	
	// ��������� � ����� �� ����������� ����������� ������
	private boolean checkBounds(CubesDirection direct, int row, int col) {
		if(direct == CubesDirection.LEFT) {
			return col < 0;
		}
		else if(direct == CubesDirection.RIGHT) {
			return col > COLS - 1;
		}
		else if(direct == CubesDirection.UP) { 
			return row < 0;
		}
		else if(direct == CubesDirection.DOWN) {
			return row > ROWS - 1;
		}
		return false;
	}

	// move - ������������� ������� ������ �/��� �������������
	private void moveCubes(CubesDirection direct) {
		boolean moveAbility = false; // ���� ����������� ����������� �� ��������� ����� ��� ���
		int horDirection = 0;
		int verDirection = 0;
		
		if(direct == CubesDirection.LEFT) {
			horDirection = -1;
			for(int row = 0; row < ROWS; row++) {
				for(int col = 0; col < COLS; col++) {
					if(!moveAbility) { // false, ���� ��������� ����� ����� �������; true, ���� ��������� ����� ������ �������
						moveAbility = moveCubes(row, col, horDirection, verDirection, direct); 
					}
					else moveCubes(row, col, horDirection, verDirection, direct); 
				}
			}
		}
		
		else if(direct == CubesDirection.RIGHT) {
			horDirection = 1;
			for(int row = 0; row < ROWS; row++) {
				for(int col = COLS - 1; col >= 0; col--) {
					if(!moveAbility) {
						moveAbility = moveCubes(row, col, horDirection, verDirection, direct);
					}
					else moveCubes(row, col, horDirection, verDirection, direct);
				}
			}
		}
		
		else if(direct == CubesDirection.UP) {
			verDirection = -1;
			for(int row = 0; row < ROWS; row++) {
				for(int col = 0; col < COLS; col++) {
					if(!moveAbility) {
						moveAbility = moveCubes(row, col, horDirection, verDirection, direct);
					}
					else moveCubes(row, col, horDirection, verDirection, direct);
				}
			}
		}
		
		else if(direct == CubesDirection.DOWN) {
			verDirection = 1;
			for(int row = ROWS - 1; row >= 0; row--) {
				for(int col = 0; col < COLS; col++) {
					if(!moveAbility) {
						moveAbility = moveCubes(row, col, horDirection, verDirection, direct);
					}
					else moveCubes(row, col, horDirection, verDirection, direct);
				}
			}
		}
		
		else {
			System.out.println(direct + "invalid direction"); // ��������
		}
		
		for(int row = 0; row < ROWS; row++) {
			for(int col = 0; col < COLS; col++) {
				Cube current = field[row][col];
				if(current == null) continue;
				current.setCombineAbility(true);
			}
		}
		
		if(moveAbility) {
			audio.play("move", 0); // 0 - ������ 1 ���
			randomSpawn();
			checkIsDead();
		}
	}
	
	private void checkIsDead() {
		for(int row = 0; row < ROWS; row++) {
			for(int col = 0; col < COLS; col++) {
				if(field[row][col] == null) return; // ����� open space 
				if(checkNearCubes(row, col, field[row][col])) { // ������ ������ ����� �� ����������� �� ��������������
					return;
				}
			}
		}
		
		isDead = true;
		if(score >= highScore) highScore = score;
		setHighScore();
		//------------------
		if(isDead)
		{
			System.out.println("DEAD!!!");
		}
		//------------------
	}
	
	private boolean checkNearCubes(int row, int col, Cube current) {
		if(row > 0) {
			Cube check = field[row - 1][col];
			if(check == null) return true;
			if(current.getValue() == check.getValue()) return true;
		}
		if(row < ROWS - 1) {
			Cube check = field[row + 1][col];
			if(check == null) return true;
			if(current.getValue() == check.getValue()) return true;
		}
		if(col > 0) {
			Cube check = field[row][col - 1];
			if(check == null) return true;
			if(current.getValue() == check.getValue()) return true;
		}
		if(col < COLS - 1) {
			Cube check = field[row][col + 1];
			if(check == null) return true;
			if(current.getValue() == check.getValue()) return true;
		}
		return false;
	}
	
	private void checkKey() {
		if(Keyboard.typed(KeyEvent.VK_LEFT)) {
			moveCubes(CubesDirection.LEFT);
		}
		if(Keyboard.typed(KeyEvent.VK_RIGHT)) {
			moveCubes(CubesDirection.RIGHT);
		}
		if(Keyboard.typed(KeyEvent.VK_UP)) {
			moveCubes(CubesDirection.UP);
		} 
		if(Keyboard.typed(KeyEvent.VK_DOWN)) {
			moveCubes(CubesDirection.DOWN);
		}
	}
}
