package mirosha.game;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.sound.sampled.Clip;

public class GameField { // класс для работы с игровым полем

	public static final int ROWS = 4; // поле 4х4
	public static final int COLS = 4;
	private static int DISTANCE = 10; // расстояние между кубиками
	public static int FIELDW = (COLS + 1) * DISTANCE + COLS * Cube.WIDTH;
	public static int FIELDH = (ROWS + 1) * DISTANCE + ROWS * Cube.HEIGHT;

	private int x; // позиции x, y для рендера на экране
	private int y;
	private boolean won;
	private boolean dead;
	private Cube[][] field; // массив для размещения кубиков на доске
	private BufferedImage gameField; // для  фона игрового поля
	private final int initialCubesSpawn = 2; // количество начальных спаунов кубиков
	
	public static final int LEFT = 0; // ключи направлений
	public static final int RIGHT = 1;
	public static final int UP = 2;
	public static final int DOWN = 3;

	private Scores scores;
	private Leaders leaders;
	private SetAudio audio;
	private int saveCount = 0;

	public GameField(int x, int y) { // конструктор игрового поля
		this.x = x;
		this.y = y;
		field = new Cube[ROWS][COLS];
		gameField = new BufferedImage(FIELDW, FIELDH, BufferedImage.TYPE_INT_RGB);
		createFieldImage();

		// установка аудио
		audio = SetAudio.getInstance();
		audio.loadAudio("move.wav", "move"); // музыка при передвижении кубиков
		audio.loadAudio("main.mp3", "background"); // фоновая музыка
		audio.adjustVolume("background", -10); // уровень громкости
		audio.playAudio("background", Clip.LOOP_CONTINUOUSLY); // бесконечный цикл фоновой музыки

		// установка списка лучших результатов игры
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
			// не вызываем setDead т.к. не хотим ничего сохранять 
			dead = checkDead();
			// не вызываем setWon т.к. не хотим сохранять время 
			won = checkWon();
		}
	}

	public void resetData() { // сброс данных при новой игре
		field = new Cube[ROWS][COLS];
		start();
		scores.saveGame();
		dead = false;
		won = false;
		saveCount = 0;
	}

	private void start() { // при начале игры спауним 2 рандомных кубика
		for (int i = 0; i < initialCubesSpawn; i++) {
			spawnRandomCubes();
		}
	}

	private void spawn(int row, int col, int value) { // спаун кубиков в массиве
		field[row][col] = new Cube(value, getCubeX(col), getCubeY(row));
	}

	private void createFieldImage() { // создание изображений на игровом поле
		Graphics2D graphics = (Graphics2D) gameField.getGraphics();
		graphics.setColor(Color.darkGray); // фон подложки поля под кубики
		graphics.fillRect(0, 0, FIELDW, FIELDH);
		graphics.setColor(Color.lightGray); // фон подложки под пустые ячейки

		// отрисовка кубиков в массиве:
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				int x = DISTANCE + DISTANCE * col + Cube.WIDTH * col; // позиции отрисовки
				int y = DISTANCE + DISTANCE * row + Cube.HEIGHT * row;
				graphics.fillRoundRect(x, y, Cube.WIDTH, Cube.HEIGHT, Cube.ARCW, Cube.ARCH); // отрисовка кубиков
			}
		}
	}

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

	public void renderFinal(Graphics2D graphics) { // рендерим графику конечного изображения на игровом поле
		BufferedImage finalBoard = new BufferedImage(FIELDW, FIELDH, BufferedImage.TYPE_INT_ARGB);
		Graphics2D graph = (Graphics2D) finalBoard.getGraphics();
		graph.setColor(new Color(0, 0, 0, 0)); // прозрачный цвет
		graph.fillRect(0, 0, FIELDW, FIELDH); // заполняем прямогульник
		graph.drawImage(gameField, 0, 0, null); // отрисовываем поле

		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				Cube currentCube = field[row][col];
				if (currentCube == null) continue;
				currentCube.renderCubes(graph);
			}
		}

		graphics.drawImage(finalBoard, x, y, null); // отрисовка конечного изображения
		graph.dispose(); // освобождаем ресурсы
	}

	private void resetPos(Cube cube, int row, int col) { // сброс позиции
		if (cube == null) return; // не можем двигать

		int x = getCubeX(col);
		int y = getCubeY(row);

		// пока кубики двигаются, они получают координаты x, y
		int distX = cube.getX() - x;
		int distY = cube.getY() - y;

		// math.abs - получаем модуль числа
		// кубик перемещается в любую точку в 28 пикселей за 1 обновление
		// и если там только 10 пикселей для перемещения,
		// то вместе перемещения 20 пикселей, оно перемещает на 10
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

	// проверяем в каком направлении мы передвигаем кубики
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

	private boolean moveCubes(int row, int col, int horDir, int verDir, int dir) {
		boolean moveAbility = false; // есть возможность передвинуть на свободное место или нет
		Cube currentCube = field[row][col]; // установка текущего кубика в массиве
		if (currentCube == null) return false;
		boolean move = true; // флаг передвижения
		int newCol = col;
		int newRow = row;
		while (move) {
			newCol += horDir;
			newRow += verDir;
			if (boundCheck(dir, newRow, newCol)) break;
			if (field[newRow][newCol] == null) { // если кубик передвигается на новое место без комбинирования с другим
				field[newRow][newCol] = currentCube; // готовим установку текущего кубика на новое место
				moveAbility = true; // есть возможность передвинуть на свободное место
				field[newRow - verDir][newCol - horDir] = null; // освобождаем место откуда сдвинули
				field[newRow][newCol].setSlide(new Spot(newRow, newCol)); // передвигаем на новое место
			} // если кубик двигается на место другого кубика с одинаковым значением, то надо их комбинировать
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
	
	// moveCubesDir - предугадывает двигать кубики и/или комбинировать
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
				currentCube.setUniteAbility(true); // объединяем
			}
		}

		if (moveAbility) { // если можно двигать, то
			audio.playAudio("move", 0); // работает звук передвижения
			spawnRandomCubes(); // спауним новый кубик
			setDead(checkDead()); // проверка на проигрыш
		}
	}
	
	// проверяем на выигрыш (если набрали 2048)
	private boolean checkWon() {
		for (int row = 0; row < ROWS; row++) {
			for (int col = 0; col < COLS; col++) {
				if(field[row][col] == null) continue;
				if(field[row][col].getValue() >= 2048) return true;
			}
		}
		return false;
	}

	// проверяем на проигрыш
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

	// проверяем ближайшие кубики к текущему кубику по row/col
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

	private void spawnRandomCubes() { // спауним новые кубики
		Random randomCubes = new Random();
		boolean disabled = true;

		while (disabled) {
			int position = randomCubes.nextInt(16);
			int row = position / ROWS;
			int col = position % COLS;
			Cube currentCube = field[row][col];
			if (currentCube == null) { // спаун
				int value = randomCubes.nextInt(10) < 9 ? 2 : 4; // ищем рандомное число от 0 до 9 (90% - спаун для 2, 10% - для 4)
				Cube cube = new Cube(value, getCubeX(col), getCubeY(row));
				field[row][col] = cube;
				disabled = false;
			}
		}
	}

	private void checkKeyboard() { // проверка ключей клавиатуры и установка направлений кубиков
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

	public int getNewBiggestValue() { // установка нового начения кубика
		int cubeValue = 2; // минимальное начальное значение кубика
		for(int row = 0; row < ROWS; row++) {
			for(int col = 0; col < COLS; col++) {
				if(field[row][col] == null) continue;
				if(field[row][col].getValue() > cubeValue) 
					cubeValue = field[row][col].getValue(); // установка следующего значения
			}
		}
		return cubeValue;
	}
	
	// геттеры и сеттеры позиций x, y
	public int getX() { return x; }

	public void setX(int x) { this.x = x; }

	public int getY() { return y; }

	public void setY(int y) { this.y = y; }
	
	// геттер и сеттер расположения кубика на поле
	public Cube[][] getField() { return field; }
	
	public void setField(Cube[][] field) { this.field = field; }
	
	public boolean getWon() { return won; } // флаг выигрыша (2048 и более)

	public void setWon(boolean won) { // установка выигрыша
		if(!this.won && won && !dead){ 
			leaders.saveScores(); // сохранение счета
		}
		this.won = won;
	}
	
	public boolean getDead() { return dead; }// флаг смерти

	public void setDead(boolean dead) { // установка смерти
		if(!this.dead && dead) { // если смерть, то устанавливаем 
			leaders.addCube(getNewBiggestValue()); // маскимальное значение кубика
			leaders.addScore(scores.getCurrentScore()); // счет игры
			leaders.saveScores(); // сохранение счета
		}
		this.dead = dead;
	}
	
	// геттер расположения кубика по Х
	public int getCubeX(int col) {
		return DISTANCE + col * Cube.WIDTH + col * DISTANCE;
	}

	// геттер расположения кубика по Y
	public int getCubeY(int row) {
		return DISTANCE + row * Cube.HEIGHT + row * DISTANCE;
	}
	
	 // геттер счета
	public Scores getScores(){ return scores; }
}
