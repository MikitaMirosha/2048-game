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
	
	private int x; // x, y - позиции где рендерить на экране
	private int y;
	private int score = 0;
	private int highScore = 0;
	private Font scoreFont;
	private SetAudio audio;  
	private final int initialCubesSpawn = 2; // кол-во начальных спавнов плитки
	private Cube[][] field; // отслеживает где все плитки на доске и размещает в массиве
	private boolean isDead; // ! ДОДЕЛАТЬ
	private boolean isWin; // ! ДОДЕЛАТЬ
	private BufferedImage gameField; // для фона игрового поля
	private BufferedImage finalField; // все содержимое поля: фон, плитки и т.д.
	
	private static int DISTANCE = 10; // расстояние между плитками
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
		this.x = x; // координаты отрисовки
		this.y = y;
		field = new Cube[ROWS][COLS];
		gameField = new BufferedImage(FIELD_WIDTH, FIELD_HEIGHT, BufferedImage.TYPE_INT_RGB);
		finalField = new BufferedImage(FIELD_WIDTH, FIELD_HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		loadHighScore();  
		makeFieldImage();
		start(); // здесь спаунятся кубики
		
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
		
		for(int row = 0; row < ROWS; row++) { // отрисовка кубиков в массиве
			for(int col = 0; col < COLS; col++) {
				int x = DISTANCE + DISTANCE * col + Cube.WIDTH * col; // позиции отрисовки
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
			if(current == null) { // спауним новые кубики
				int value = random.nextInt(10) < 9 ? 2 : 4; // ищет рандомное число от 0 до 9 (90% - спаун для 2, 10% - для 4)
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
	
	public void render(Graphics2D finalFieldImage) { // графика для final field 
		Graphics2D image2D = (Graphics2D)finalField.getGraphics();
		image2D.drawImage(gameField, 0, 0, null);
		
		for(int row = 0; row < ROWS; row++) {
			for(int col = 0; col < COLS; col++) {
				Cube current = field[row][col];
				if(current == null) continue; // в этом случае не реднерим
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
				resetPos(current, row, col); // сброс позиции
				if(current.getValue() == 2048) { 
					isWin = true;  
				}
			}
		}
	}
	
	private void resetPos(Cube current, int row, int col) {
		if(current == null) return; // не можем двигать
		
		int x = getCubeX(col);
		int y = getCubeY(row);
		// пока кубики двигаются, они получают координаты x, y
		int distX = current.getX() - x;
		int distY = current.getY() - y;
		
		// math.abs - получаем модуль числа
		// плитка перемещается в любую точку в 28 пикселей за 1 update
		// и если там только 10 пикселей для перемещения,
		// то вместе перемещения 20 пикселей, оно перемещает на 10
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
		if(current == null) return false; // нет возможности двигать
		boolean move = true; 
		int newCol = col;
		int newRow = row;
		 
		while(move) {
			newCol += horDirection;
			newRow += verDirection;
			if(checkBounds(direct, newRow, newCol)) break; // не можем передвигать, выходим из цикла
			if(field[newRow][newCol] == null) { // можно передвигать
				field[newRow][newCol] = current; // здесь берем плитку и двигаем на нужное место
				field[newRow - verDirection][newCol - horDirection] = null;
				field[newRow][newCol].setSlideTo(new Spot(newRow, newCol));
				moveAbility = true;
			}
			// можно передвинуть и комбинировать
			else if(field[newRow][newCol].getValue() == current.getValue() && field[newRow][newCol].combineAbility()) { 
				field[newRow][newCol].setCombineAbility(false); // больше нельзя комбинировать 
				field[newRow][newCol].setValue(field[newRow][newCol].getValue() * 2); // комбинируем умножая на 2
				moveAbility = true;
				field[newRow - verDirection][newCol - horDirection] = null;
				field[newRow][newCol].setSlideTo(new Spot(newRow, newCol));
            	field[newRow][newCol].setCombineAnimation(true);
				score += field[newRow][newCol].getValue(); // ведем счет 
			}
			else {
				move = false; // на всякий случай (fix)
			}
		}
		return moveAbility;
	}
	
	// проверяет в каком мы направлении передвигаем кубики
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

	// move - предугадывает двигать кубики и/или комбинировать
	private void moveCubes(CubesDirection direct) {
		boolean moveAbility = false; // есть возможность передвинуть на свободное место или нет
		int horDirection = 0;
		int verDirection = 0;
		
		if(direct == CubesDirection.LEFT) {
			horDirection = -1;
			for(int row = 0; row < ROWS; row++) {
				for(int col = 0; col < COLS; col++) {
					if(!moveAbility) { // false, если последний кубик можно двигать; true, если последний кубик нельзя двигать
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
			System.out.println(direct + "invalid direction"); // доделать
		}
		
		for(int row = 0; row < ROWS; row++) {
			for(int col = 0; col < COLS; col++) {
				Cube current = field[row][col];
				if(current == null) continue;
				current.setCombineAbility(true);
			}
		}
		
		if(moveAbility) {
			audio.play("move", 0); // 0 - играет 1 раз
			randomSpawn();
			checkIsDead();
		}
	}
	
	private void checkIsDead() {
		for(int row = 0; row < ROWS; row++) {
			for(int col = 0; col < COLS; col++) {
				if(field[row][col] == null) return; // имеем open space 
				if(checkNearCubes(row, col, field[row][col])) { // чекаем кубики рядом на возможность их комбинирования
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
