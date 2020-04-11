package mirosha.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.sound.sampled.Clip;

/**
 * Класс для работы с игровым полем
 * @author Mirosha
 * @version 1.0
 */
public class GameField { 

	/** Поле строк*/
	public static final int ROWS = 4; 
	
	/** Поле столбцов*/
	public static final int COLS = 4;
	
	/** Поле расстояние между кубиками*/
	private static int DISTANCE = 10; 
	
	/** Поле ширина игрового поля*/
	public static int FIELDW = (COLS + 1) * DISTANCE + COLS * Cube.WIDTH;
	
	/** Поле высота игрового поля*/
	public static int FIELDH = (ROWS + 1) * DISTANCE + ROWS * Cube.HEIGHT;

	/** Поле координата x*/
	private int x; 
	
	/** Поле координата y*/
	private int y;
	
	/** Поле флаг выигрыша*/
	private boolean won;
	
	/** Поле флаг проигрыша*/
	private boolean dead;
	
	/** Поле массив для размещения кубов на поле*/
	private Cube[][] field; 
	
	/** Поле для фона игрового поля*/
	private BufferedImage gameField; 
	
	/** Поле количество начальных спаунов кубов*/
	private final int initialCubesSpawn = 2; 
	
	/** Поле ключ направления влево*/
	public static final int LEFT = 0; 
	
	/** Поле ключ направления вправо*/
	public static final int RIGHT = 1;
	
	/** Поле ключ направления вверх*/
	public static final int UP = 2;
	
	/** Поле ключ направления вниз*/
	public static final int DOWN = 3;

	/** Поле счет игры*/
	private Scores scores;
	
	/** Поле лучшие результаты игры*/
	private Leaders leaders;
	
	/** Поле установки аудио в игре*/
	private SetAudio audio;
	
	/** Поле счетчик сохранения*/
	private int saveCount = 0;

	/** 
     * Конструктор - создание нового объекта игровое поле
     * @param x - координата x
     * @param y - координата y
     */
	public GameField(int x, int y) { 
		this.x = x;
		this.y = y;
		field = new Cube[ROWS][COLS];
		gameField = new BufferedImage(FIELDW, FIELDH, BufferedImage.TYPE_INT_RGB);
		createFieldImage();

		// установка аудио:
		audio = SetAudio.getInstance();
		audio.loadAudio("move.wav", "move"); 
		audio.loadAudio("main.mp3", "background"); 
		audio.adjustVolume("background", -10); 
		audio.playAudio("background", Clip.LOOP_CONTINUOUSLY); 

		// установка списка лучших результатов игры:
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
     * Процедура сброс данных при новой игре
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
     * Процедура спаун 2 рандомных куба в начале игры
     */
	private void start() { 
		for (int i = 0; i < initialCubesSpawn; i++) {
			spawnRandomCubes();
		}
	}

	/**
     * Процедура спаун кубов в массиве
     * @param row - строки
     * @param col - столбцы
     * @param value - взначение
     */
	private void spawn(int row, int col, int value) { 
		field[row][col] = new Cube(value, getCubeX(col), getCubeY(row));
	}

	/**
     * Процедура создание изображения на игровом поле
     */
	private void createFieldImage() { 
		Graphics2D graphics = (Graphics2D) gameField.getGraphics();
		graphics.setColor(Color.darkGray); // фон подложки поля под кубы
		graphics.fillRect(0, 0, FIELDW, FIELDH);
		graphics.setColor(Color.lightGray); // фон подложки под пустые ячейки

		// отрисовка кубов в массиве:
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				int x = DISTANCE + DISTANCE * col + Cube.WIDTH * col; 
				int y = DISTANCE + DISTANCE * row + Cube.HEIGHT * row;
				graphics.fillRoundRect(x, y, Cube.WIDTH, Cube.HEIGHT, Cube.ARCW, Cube.ARCH); 
			}
		}
	}

	/**
     * Процедура обновление игрового поля
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
     * Процедура рендер конечного изображения на игровом поле
     * @param graphics - графика изображения на игровом поле
     */
	public void renderFinal(Graphics2D graphics) { 
		BufferedImage finalBoard = new BufferedImage(FIELDW, FIELDH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graph = (Graphics2D) finalBoard.getGraphics();
		graph.setColor(new Color(0, 0, 0, 0));	// прозрачный цвет
		graph.fillRect(0, 0, FIELDW, FIELDH); 
		graph.drawImage(gameField, 0, 0, null); 

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				Cube currentCube = field[row][col];
				if (currentCube == null) continue;
				currentCube.renderCubes(graph);
			}
		}

		graphics.drawImage(finalBoard, x, y, null); // отрисовка конечного изображения
		graph.dispose(); 
	}

	/**
     * Процедура сброс позиции
     * @param cube - куб
     * @param row - строка
     * @param col - столбец
     */
	private void resetPos(Cube cube, int row, int col) { 
		if (cube == null) return; // запрещено движение

		int x = getCubeX(col);
		int y = getCubeY(row);

		// получение координат кубов при движении 
		int distX = cube.getX() - x;
		int distY = cube.getY() - y;

		// Math.abs - получение модуля числа
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
     * Функция проверка направления передвижения кубов
     * @param dir - направление
     * @param row - строка
     * @param col - столбец
     * @return возвращает флаг проверки направления
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
     * Функция передвижение кубов 
     * @param row - строка
     * @param col - столбец
     * @param horDir - горизонтальное направление
     * @param verDir - вертикальное направление
     * @param dir - направление 
     * @return возвращает разрешение на передвижение
     */
	private boolean moveCubes(int row, int col, int horDir, int verDir, int dir) {
		boolean moveAbility = false; // нет возможности передвинуть на свободное место
		Cube currentCube = field[row][col]; // установка текущего куба 
		if (currentCube == null) return false;
		boolean move = true; // флаг передвижения
		int newCol = col;
		int newRow = row;
		while (move) {
			newCol += horDir;
			newRow += verDir;
			if (boundCheck(dir, newRow, newCol)) break;
			if (field[newRow][newCol] == null) { // если куб передвигается на новое место без комбинирования с другим
				field[newRow][newCol] = currentCube; // готовим установку текущего куба на новое место
				moveAbility = true; // есть возможность передвинуть на свободное место
				field[newRow - verDir][newCol - horDir] = null; // освобождаем место откуда сдвинули
				field[newRow][newCol].setSlide(new Spot(newRow, newCol)); // передвигаем на новое место
			} // если куб двигается на место другого куба с одинаковым значением, то надо их комбинировать
			else if (field[newRow][newCol].getValue() == currentCube.getValue() && field[newRow][newCol].uniteAbility()) {
				field[newRow][newCol].setUniteAbility(false); // больше нельзя объединять
				field[newRow][newCol].setValue(field[newRow][newCol].getValue() * 2); // увеличиваем результат в 2 раза
				moveAbility = true; // есть возможность передвинуть на свободное место
				field[newRow - verDir][newCol - horDir] = null; // освобождаем место откуда сдвинули
				field[newRow][newCol].setSlide(new Spot(newRow, newCol)); // передвигаем на новое место
				field[newRow][newCol].setUniteAnimation(true); // анимируем
				scores.setCurrentScore(scores.getCurrentScore() + field[newRow][newCol].getValue()); // устанавливаем счет
			}
			else {
				move = false;
			}
		}
		return moveAbility;
	}
	
	/**
     * Процедура предугадывание движение/комбинация куба
     * @param dir - направление куба
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
				currentCube.setUniteAbility(true); // объединение
			}
		}

		if (moveAbility) { 				// если можно двигать, то
			audio.playAudio("move", 0); // работает звук передвижения
			spawnRandomCubes(); 		// спаун нового куба
			setDead(checkDead()); 		// проверка на проигрыш
		}
	}
	
	/**
     * Функция проверка на выигрыш (если набрали 2048)
     * @return возвращает флаг выигрыша
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
     * Функция проверка на проигрыш 
     * @return возвращает флаг проигрыша
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
     * Функция проверка ближайших кубов к текущему кубу
     * @param row - строка
     * @param col - столбец
     * @param cube - куб
     * @return возвращает флаг проверки
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
     * Процедура спаун новых кубов
     */
	private void spawnRandomCubes() { 
		Random randomCubes = new Random();
		boolean disabled = true;

		while (disabled) {
			int position = randomCubes.nextInt(16);
			int row = position / ROWS;
			int col = position % COLS;
			Cube currentCube = field[row][col];
			if (currentCube == null) { // спаун
				int value = randomCubes.nextInt(10) < 9 ? 2 : 4; // поиск рандомного числа от 0 до 9 (90% - спаун для 2, 10% - для 4)
				Cube cube = new Cube(value, getCubeX(col), getCubeY(row));
				field[row][col] = cube;
				disabled = false;
			}
		}
	}

	/**
     * Процедура проверка ключей клавиатуры и установка направлений кубов
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
     * Функция установка нового значения куба
     * @return возвращает значение куба
     */
	public int getNewBiggestValue() { 
		int cubeValue = 2; 				// минимальное начальное значение куба
		for(int row = 0; row < ROWS; row++) {
			for(int col = 0; col < COLS; col++) {
				if(field[row][col] == null) continue;
				if(field[row][col].getValue() > cubeValue) 
					cubeValue = field[row][col].getValue(); // установка следующего значения
			}
		}
		return cubeValue;
	}
	
	/**
     * Функция получения значения поля {@link #x}
     * @return возвращает координату x
     */
	public int getX() { return x; }

	/**
     * Процедура установки координаты x
     * @param x - координатa x
     */
	public void setX(int x) { this.x = x; }

	/**
     * Функция получения значения поля {@link #y}
     * @return возвращает координату y
     */
	public int getY() { return y; }

	/**
     * Процедура установки координаты y
     * @param y - координатa y
     */
	public void setY(int y) { this.y = y; }
	
	/**
     * Функция получения значения поля {@link #field}
     * @return возвращает расположение куба на поле 
     */
	public Cube[][] getField() { return field; }
	
	/**
     * Процедура установки расположения куба на поле
     * @param field - координата куба на поле
     */
	public void setField(Cube[][] field) { this.field = field; }
	
	/**
     * Функция получения значения поля {@link #won}
     * @return возвращает флаг выигрыша (2048 и более)
     */
	public boolean getWon() { return won; } 

	/**
     * Процедура установки флага выигрыша
     * @param won - флаг выигрыша
     */
	public void setWon(boolean won) { 
		if(!this.won && won && !dead){ 
			leaders.saveScores(); // сохранение счета
		}
		this.won = won;
	}
	
	/**
     * Функция получения значения поля {@link #dead}
     * @return возвращает флаг проигрыша
     */
	public boolean getDead() { return dead; }

	/**
     * Процедура установки флага проигрыша
     * @param dead - флаг проигрыша
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
     * Функция получения значения поля {@link #col}
     * @param col - столбец
     * @return возвращает координату по столбцу
     */
	public int getCubeX(int col) {
		return DISTANCE + col * Cube.WIDTH + col * DISTANCE;
	}

	/**
     * Функция получения значения поля {@link #row}
     * @param row - строка
     * @return возвращает координату по строке
     */
	public int getCubeY(int row) {
		return DISTANCE + row * Cube.HEIGHT + row * DISTANCE;
	}
	
	/**
     * Функция получения значения поля {@link #scores}
     * @return возвращает счет игры
     */
	public Scores getScores(){ return scores; }
}
