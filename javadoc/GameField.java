package mirosha.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.sound.sampled.Clip;

/**
 * ����� ��� ������ � ������� �����
 * @author Mirosha
 * @version 1.0
 */
public class GameField { 

	/** ���� �����*/
	public static final int ROWS = 4; 
	
	/** ���� ��������*/
	public static final int COLS = 4;
	
	/** ���� ���������� ����� ��������*/
	private static int DISTANCE = 10; 
	
	/** ���� ������ �������� ����*/
	public static int FIELDW = (COLS + 1) * DISTANCE + COLS * Cube.WIDTH;
	
	/** ���� ������ �������� ����*/
	public static int FIELDH = (ROWS + 1) * DISTANCE + ROWS * Cube.HEIGHT;

	/** ���� ���������� x*/
	private int x; 
	
	/** ���� ���������� y*/
	private int y;
	
	/** ���� ���� ��������*/
	private boolean won;
	
	/** ���� ���� ���������*/
	private boolean dead;
	
	/** ���� ������ ��� ���������� ����� �� ����*/
	private Cube[][] field; 
	
	/** ���� ��� ���� �������� ����*/
	private BufferedImage gameField; 
	
	/** ���� ���������� ��������� ������� �����*/
	private final int initialCubesSpawn = 2; 
	
	/** ���� ���� ����������� �����*/
	public static final int LEFT = 0; 
	
	/** ���� ���� ����������� ������*/
	public static final int RIGHT = 1;
	
	/** ���� ���� ����������� �����*/
	public static final int UP = 2;
	
	/** ���� ���� ����������� ����*/
	public static final int DOWN = 3;

	/** ���� ���� ����*/
	private Scores scores;
	
	/** ���� ������ ���������� ����*/
	private Leaders leaders;
	
	/** ���� ��������� ����� � ����*/
	private SetAudio audio;
	
	/** ���� ������� ����������*/
	private int saveCount = 0;

	/** 
     * ����������� - �������� ������ ������� ������� ����
     * @param x - ���������� x
     * @param y - ���������� y
     */
	public GameField(int x, int y) { 
		this.x = x;
		this.y = y;
		field = new Cube[ROWS][COLS];
		gameField = new BufferedImage(FIELDW, FIELDH, BufferedImage.TYPE_INT_RGB);
		createFieldImage();

		// ��������� �����:
		audio = SetAudio.getInstance();
		audio.loadAudio("move.wav", "move"); 
		audio.loadAudio("main.mp3", "background"); 
		audio.adjustVolume("background", -10); 
		audio.playAudio("background", Clip.LOOP_CONTINUOUSLY); 

		// ��������� ������ ������ ����������� ����:
		leaders = Leaders.getInstance();
		leaders.loadScores();
		scores = new Scores(this);
		scores.loadGame();
		scores.setCurrentTopScore(leaders.getHighScore());
		
		if(scores.newGame()) {
			start();
			scores.saveGame();
		}
		else {
			for(int i = 0; i < scores.getField().length; i++) {
				if(scores.getField()[i] == 0) continue;
				spawn(i / ROWS, i % COLS, scores.getField()[i]);
			}
			dead = checkDead();
			won = checkWon();
		}
	}

	/**
     * ��������� ����� ������ ��� ����� ����
     */
	public void resetData() { 
		field = new Cube[ROWS][COLS];
		start();
		scores.saveGame();
		dead = false;
		won = false;
		saveCount = 0;
	}

	/**
     * ��������� ����� 2 ��������� ���� � ������ ����
     */
	private void start() { 
		for (int i = 0; i < initialCubesSpawn; i++) {
			spawnRandomCubes();
		}
	}

	/**
     * ��������� ����� ����� � �������
     * @param row - ������
     * @param col - �������
     * @param value - ���������
     */
	private void spawn(int row, int col, int value) { 
		field[row][col] = new Cube(value, getCubeX(col), getCubeY(row));
	}

	/**
     * ��������� �������� ����������� �� ������� ����
     */
	private void createFieldImage() { 
		Graphics2D graphics = (Graphics2D) gameField.getGraphics();
		graphics.setColor(Color.darkGray); // ��� �������� ���� ��� ����
		graphics.fillRect(0, 0, FIELDW, FIELDH);
		graphics.setColor(Color.lightGray); // ��� �������� ��� ������ ������

		// ��������� ����� � �������:
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				int x = DISTANCE + DISTANCE * col + Cube.WIDTH * col; 
				int y = DISTANCE + DISTANCE * row + Cube.HEIGHT * row;
				graphics.fillRoundRect(x, y, Cube.WIDTH, Cube.HEIGHT, Cube.ARCW, Cube.ARCH); 
			}
		}
	}

	/**
     * ��������� ���������� �������� ����
     */
	public void updateField() {
		saveCount++;
		if (saveCount >= 120) {
			saveCount = 0;
			scores.saveGame();
		}
	
		checkKeyboard();

		if (scores.getCurrentScore() > scores.getCurrentTopScore()) {
			scores.setCurrentTopScore(scores.getCurrentScore());
		}

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				Cube currentCubes = field[row][col];
				if (currentCubes == null) continue;
				currentCubes.updateCubeAnimation();
				resetPos(currentCubes, row, col);
				if (currentCubes.getValue() == 2048) {
					setWon(true);
				}
			}
		}
	}

	/**
     * ��������� ������ ��������� ����������� �� ������� ����
     * @param graphics - ������� ����������� �� ������� ����
     */
	public void renderFinal(Graphics2D graphics) { 
		BufferedImage finalBoard = new BufferedImage(FIELDW, FIELDH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graph = (Graphics2D) finalBoard.getGraphics();
		graph.setColor(new Color(0, 0, 0, 0));	// ���������� ����
		graph.fillRect(0, 0, FIELDW, FIELDH); 
		graph.drawImage(gameField, 0, 0, null); 

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				Cube currentCube = field[row][col];
				if (currentCube == null) continue;
				currentCube.renderCubes(graph);
			}
		}

		graphics.drawImage(finalBoard, x, y, null); // ��������� ��������� �����������
		graph.dispose(); 
	}

	/**
     * ��������� ����� �������
     * @param cube - ���
     * @param row - ������
     * @param col - �������
     */
	private void resetPos(Cube cube, int row, int col) { 
		if (cube == null) return; // ��������� ��������

		int x = getCubeX(col);
		int y = getCubeY(row);

		// ��������� ��������� ����� ��� �������� 
		int distX = cube.getX() - x;
		int distY = cube.getY() - y;

		// Math.abs - ��������� ������ �����
		if (Math.abs(distX) < Cube.SPEED) {
			cube.setX(cube.getX() - distX);
		}

		if (Math.abs(distY) < Cube.SPEED) {
			cube.setY(cube.getY() - distY);
		}

		if (distX < 0) {
			cube.setX(cube.getX() + Cube.SPEED);
		}
		if (distY < 0) {
			cube.setY(cube.getY() + Cube.SPEED);
		}
		if (distX > 0) {
			cube.setX(cube.getX() - Cube.SPEED);
		}
		if (distY > 0) {
			cube.setY(cube.getY() - Cube.SPEED);
		}
	}

	/**
     * ������� �������� ����������� ������������ �����
     * @param dir - �����������
     * @param row - ������
     * @param col - �������
     * @return ���������� ���� �������� �����������
     */
	private boolean boundCheck(int dir, int row, int col) {
		if (dir == LEFT) {
			return col < 0;
		}
		else if (dir == RIGHT) {
			return col > COLS - 1;
		}
		else if (dir == UP) {
			return row < 0;
		}
		else if (dir == DOWN) {
			return row > ROWS - 1;
		}
		return false;
	}

	/**
     * ������� ������������ ����� 
     * @param row - ������
     * @param col - �������
     * @param horDir - �������������� �����������
     * @param verDir - ������������ �����������
     * @param dir - ����������� 
     * @return ���������� ���������� �� ������������
     */
	private boolean moveCubes(int row, int col, int horDir, int verDir, int dir) {
		boolean moveAbility = false; // ��� ����������� ����������� �� ��������� �����
		Cube currentCube = field[row][col]; // ��������� �������� ���� 
		if (currentCube == null) return false;
		boolean move = true; // ���� ������������
		int newCol = col;
		int newRow = row;
		while (move) {
			newCol += horDir;
			newRow += verDir;
			if (boundCheck(dir, newRow, newCol)) break;
			if (field[newRow][newCol] == null) { // ���� ��� ������������� �� ����� ����� ��� �������������� � ������
				field[newRow][newCol] = currentCube; // ������� ��������� �������� ���� �� ����� �����
				moveAbility = true; // ���� ����������� ����������� �� ��������� �����
				field[newRow - verDir][newCol - horDir] = null; // ����������� ����� ������ ��������
				field[newRow][newCol].setSlide(new Spot(newRow, newCol)); // ����������� �� ����� �����
			} // ���� ��� ��������� �� ����� ������� ���� � ���������� ���������, �� ���� �� �������������
			else if (field[newRow][newCol].getValue() == currentCube.getValue() && field[newRow][newCol].uniteAbility()) {
				field[newRow][newCol].setUniteAbility(false); // ������ ������ ����������
				field[newRow][newCol].setValue(field[newRow][newCol].getValue() * 2); // ����������� ��������� � 2 ����
				moveAbility = true; // ���� ����������� ����������� �� ��������� �����
				field[newRow - verDir][newCol - horDir] = null; // ����������� ����� ������ ��������
				field[newRow][newCol].setSlide(new Spot(newRow, newCol)); // ����������� �� ����� �����
				field[newRow][newCol].setUniteAnimation(true); // ���������
				scores.setCurrentScore(scores.getCurrentScore() + field[newRow][newCol].getValue()); // ������������� ����
			}
			else {
				move = false;
			}
		}
		return moveAbility;
	}
	
	/**
     * ��������� �������������� ��������/���������� ����
     * @param dir - ����������� ����
     */
	public void moveCubeDir(int dir) {
		boolean moveAbility = false;
		int horDir = 0;
		int verDir = 0;

		if (dir == LEFT) {
			horDir = -1;
			for (int row = 0; row < ROWS; row++) {
				for (int col = 0; col < COLS; col++) {
					if (!moveAbility)
						moveAbility = moveCubes(row, col, horDir, verDir, dir);
					else moveCubes(row, col, horDir, verDir, dir);
				}
			}
		}
		else if (dir == RIGHT) {
			horDir = 1;
			for (int row = 0; row < ROWS; row++) {
				for (int col = COLS - 1; col >= 0; col--) {
					if (!moveAbility)
						moveAbility = moveCubes(row, col, horDir, verDir, dir);
					else moveCubes(row, col, horDir, verDir, dir);
				}
			}
		}
		else if (dir == UP) {
			verDir = -1;
			for (int row = 0; row < ROWS; row++) {
				for (int col = 0; col < COLS; col++) {
					if (!moveAbility)
						moveAbility = moveCubes(row, col, horDir, verDir, dir);
					else moveCubes(row, col, horDir, verDir, dir);
				}
			}
		}
		else if (dir == DOWN) {
			verDir = 1;
			for (int row = ROWS - 1; row >= 0; row--) {
				for (int col = 0; col < COLS; col++) {
					if (!moveAbility)
						moveAbility = moveCubes(row, col, horDir, verDir, dir);
					else moveCubes(row, col, horDir, verDir, dir);
				}
			}
		}

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				Cube currentCube = field[row][col];
				if (currentCube == null) continue;
				currentCube.setUniteAbility(true); // �����������
			}
		}

		if (moveAbility) { 				// ���� ����� �������, ��
			audio.playAudio("move", 0); // �������� ���� ������������
			spawnRandomCubes(); 		// ����� ������ ����
			setDead(checkDead()); 		// �������� �� ��������
		}
	}
	
	/**
     * ������� �������� �� ������� (���� ������� 2048)
     * @return ���������� ���� ��������
     */
	private boolean checkWon() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				if(field[row][col] == null) continue;
				if(field[row][col].getValue() >= 2048) return true;
			}
		}
		return false;
	}

	/**
     * ������� �������� �� �������� 
     * @return ���������� ���� ���������
     */ 
	private boolean checkDead() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				if (field[row][col] == null) return false;
				boolean uniteAbility = checkNearCubes(row, col, field[row][col]);
				if (uniteAbility) {
					return false;
				}
			}
		}
		return true;
	}

	/**
     * ������� �������� ��������� ����� � �������� ����
     * @param row - ������
     * @param col - �������
     * @param cube - ���
     * @return ���������� ���� ��������
     */
	private boolean checkNearCubes(int row, int col, Cube cube) {
		if (row > 0) {
			Cube checkCube = field[row - 1][col];
			if (checkCube == null) return true;
			if (cube.getValue() == checkCube.getValue()) return true;
		}
		if (row < ROWS - 1) {
			Cube checkCube = field[row + 1][col];
			if (checkCube == null) return true;
			if (cube.getValue() == checkCube.getValue()) return true;
		}
		if (col > 0) {
			Cube checkCube = field[row][col - 1];
			if (checkCube == null) return true;
			if (cube.getValue() == checkCube.getValue()) return true;
		}
		if (col < COLS - 1) {
			Cube checkCube = field[row][col + 1];
			if (checkCube == null) return true;
			if (cube.getValue() == checkCube.getValue()) return true;
		}
		return false;
	}

	/**
     * ��������� ����� ����� �����
     */
	private void spawnRandomCubes() { 
		Random randomCubes = new Random();
		boolean disabled = true;

		while (disabled) {
			int position = randomCubes.nextInt(16);
			int row = position / ROWS;
			int col = position % COLS;
			Cube currentCube = field[row][col];
			if (currentCube == null) { // �����
				int value = randomCubes.nextInt(10) < 9 ? 2 : 4; // ����� ���������� ����� �� 0 �� 9 (90% - ����� ��� 2, 10% - ��� 4)
				Cube cube = new Cube(value, getCubeX(col), getCubeY(row));
				field[row][col] = cube;
				disabled = false;
			}
		}
	}

	/**
     * ��������� �������� ������ ���������� � ��������� ����������� �����
     */
	private void checkKeyboard() { 
		if (!Keyboard.pressedKey[KeyEvent.VK_LEFT] && Keyboard.previousKey[KeyEvent.VK_LEFT]) {
			moveCubeDir(LEFT);
		}
		if (!Keyboard.pressedKey[KeyEvent.VK_RIGHT] && Keyboard.previousKey[KeyEvent.VK_RIGHT]) {
			moveCubeDir(RIGHT);
		}
		if (!Keyboard.pressedKey[KeyEvent.VK_UP] && Keyboard.previousKey[KeyEvent.VK_UP]) {
			moveCubeDir(UP);
		}
		if (!Keyboard.pressedKey[KeyEvent.VK_DOWN] && Keyboard.previousKey[KeyEvent.VK_DOWN]) {
			moveCubeDir(DOWN);
		}
	}

	/**
     * ������� ��������� ������ �������� ����
     * @return ���������� �������� ����
     */
	public int getNewBiggestValue() { 
		int cubeValue = 2; 				// ����������� ��������� �������� ����
		for(int row = 0; row < ROWS; row++) {
			for(int col = 0; col < COLS; col++) {
				if(field[row][col] == null) continue;
				if(field[row][col].getValue() > cubeValue) 
					cubeValue = field[row][col].getValue(); // ��������� ���������� ��������
			}
		}
		return cubeValue;
	}
	
	/**
     * ������� ��������� �������� ���� {@link #x}
     * @return ���������� ���������� x
     */
	public int getX() { return x; }

	/**
     * ��������� ��������� ���������� x
     * @param x - ���������a x
     */
	public void setX(int x) { this.x = x; }

	/**
     * ������� ��������� �������� ���� {@link #y}
     * @return ���������� ���������� y
     */
	public int getY() { return y; }

	/**
     * ��������� ��������� ���������� y
     * @param y - ���������a y
     */
	public void setY(int y) { this.y = y; }
	
	/**
     * ������� ��������� �������� ���� {@link #field}
     * @return ���������� ������������ ���� �� ���� 
     */
	public Cube[][] getField() { return field; }
	
	/**
     * ��������� ��������� ������������ ���� �� ����
     * @param field - ���������� ���� �� ����
     */
	public void setField(Cube[][] field) { this.field = field; }
	
	/**
     * ������� ��������� �������� ���� {@link #won}
     * @return ���������� ���� �������� (2048 � �����)
     */
	public boolean getWon() { return won; } 

	/**
     * ��������� ��������� ����� ��������
     * @param won - ���� ��������
     */
	public void setWon(boolean won) { 
		if(!this.won && won && !dead){ 
			leaders.saveScores(); // ���������� �����
		}
		this.won = won;
	}
	
	/**
     * ������� ��������� �������� ���� {@link #dead}
     * @return ���������� ���� ���������
     */
	public boolean getDead() { return dead; }

	/**
     * ��������� ��������� ����� ���������
     * @param dead - ���� ���������
     */
	public void setDead(boolean dead) { 
		if(!this.dead && dead) { 
			leaders.addCube(getNewBiggestValue()); 
			leaders.addScore(scores.getCurrentScore()); 
			leaders.saveScores(); 
		}
		this.dead = dead;
	}
	
	/**
     * ������� ��������� �������� ���� {@link #col}
     * @param col - �������
     * @return ���������� ���������� �� �������
     */
	public int getCubeX(int col) {
		return DISTANCE + col * Cube.WIDTH + col * DISTANCE;
	}

	/**
     * ������� ��������� �������� ���� {@link #row}
     * @param row - ������
     * @return ���������� ���������� �� ������
     */
	public int getCubeY(int row) {
		return DISTANCE + row * Cube.HEIGHT + row * DISTANCE;
	}
	
	/**
     * ������� ��������� �������� ���� {@link #scores}
     * @return ���������� ���� ����
     */
	public Scores getScores(){ return scores; }
}
