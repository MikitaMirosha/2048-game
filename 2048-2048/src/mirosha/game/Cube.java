package mirosha.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Cube { // класс для работы с кубиками 

	// координаты
	private int x;
	private int y;
	private int value; // значение на кубике
	private Font font; // сам фон
	private Color bg; // цвет фона
	private Color text; // цвет текста
	private Spot slide; // куда двигать (row/col)
	private BufferedImage cubeImage; // отрисовка кубика
	
	public static final int ARCW = 15; // закругление углов квадрата по ширине
	public static final int ARCH = 15; // закругление углов квадрата по высоте
	public static final int SPEED = 35; // скорость передвижения кубиков
	public static final int WIDTH = 120; // ширина игрового поля
	public static final int HEIGHT = 120; // высота игрового поля

	private boolean newSpawnAnimation = true; // анимация нового спауна
	private double scaleNewSpawn = 0.1; // масштаб анимации нового спауна (10%)
	private BufferedImage startImage; // отрисовка начального изображения
	
	private boolean uniteAnimation = false; // анимация объединения кубиков
	private double scaleUnited = 1.2; // при объединении кубиков увеличивается масштаб на 120%
	private BufferedImage unitedImage; // отрисовка объединения кубиков
	private boolean uniteAbility = true; // флаг возможности объединения

	public Cube(int value, int x, int y) { // конструктор кубика
		this.x = x;
		this.y = y;
		this.value = value;
		slide = new Spot(x, y); // активируем координаты перемещения по rows/cols
		cubeImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		startImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		unitedImage = new BufferedImage(WIDTH * 2, HEIGHT * 2, BufferedImage.TYPE_INT_ARGB);
		drawCubes(); // отрисовка финальных кубиков
	}
	
	// задаем цвет фона и текста для кубика по каждому значению
	private void drawCubes() { 
		Graphics2D graphics = (Graphics2D) cubeImage.getGraphics(); // Graphics2D для отрисовки кубика
		
		if(value == 2) {
			bg = new Color(0xe9e9e9);
			text = new Color(0x000000);
		}
		else if (value == 4) {
			bg = new Color(0xe6daab);
			text = new Color(0x000000);
		}
		else if (value == 8) {
			bg = new Color(0xf79d3d);
			text = new Color(0xffffff);
		}
		else if (value == 16) {
			bg = new Color(0xf28007);
			text = new Color(0xffffff);
		}
		else if (value == 32) {
			bg = new Color(0xf55e3b);
			text = new Color(0xffffff);
		}
		else if (value == 64) {
			bg = new Color(0xff0000);
			text = new Color(0xffffff);
		}
		else if (value == 128) {
			bg = new Color(0xe9de84);
			text = new Color(0xffffff);
		}
		else if (value == 256) {
			bg = new Color(0xf6e873);
			text = new Color(0xffffff);
		}
		else if (value == 512) {
			bg = new Color(0xf5e455);
			text = new Color(0xffffff);
		}
		else if (value == 1024) {
			bg = new Color(0xf7e12c);
			text = new Color(0xffffff);
		}
		else if (value == 2048) {
			bg = new Color(0xffe400);
			text = new Color(0xffffff);
		}
		else if (value == 4096) {
			bg = new Color(0xaa779a); 
			text = new Color(0xffffff);
		}
		else if (value == 8192) {
			bg = new Color(0x910a60); 
			text = new Color(0xffffff);
		}
		else if (value == 16384) {
			bg = new Color(0x0b7394); 
			text = new Color(0xffffff);
		}
		else if (value == 32768) {
			bg = new Color(0xff3f70); 
			text = new Color(0xffffff);
		}
		else if (value == 65536) {
			bg = new Color(0x009d7e); 
			text = new Color(0xffffff);
		}
		else {
			bg = Color.black;
			text = Color.white;
		}
		graphics.setColor(new Color(0, 0, 0, 0)); // прозрачный цвет (для закруглений углов)
		graphics.fillRect(0, 0, WIDTH, HEIGHT); // заполняем прямоугольник пикселями внутри фигуры
		graphics.setColor(bg); // устанавливаем цвет фона
		graphics.fillRoundRect(0, 0, WIDTH, HEIGHT, ARCW, ARCH); // учитываем закругленные углы
		graphics.setColor(text);// устанавливаем цвет текста

		if (value <= 64) { // если значение на кубике <= 64
			font = Game.main.deriveFont(30f); // то устанавливаем размер шрифта 30
			graphics.setFont(font);
		}
		else { // если 128 и выше
			font = Game.main; // то шрифт прежний
			graphics.setFont(font);
		}

		// установка координат по центру для отрисовки значения на кубике
		int drawX = WIDTH / 2 - DisplayObject.getObjectWidth("" + value, font, graphics) / 2;
		int drawY = HEIGHT / 2 + DisplayObject.getObjectHeight("" + value, font, graphics) / 2;
		graphics.drawString("" + value, drawX, drawY); // запись значения в строку на кубике
		graphics.dispose(); // освобождаем ресурсы, занимаемые компонентами окна
	}

	public void updateCubeAnimation() {
		if (newSpawnAnimation) { // если новый спаун
			AffineTransform affineTransform = new AffineTransform(); // аффинная трансформация для анимации масштаба
			affineTransform.translate(WIDTH / 2 - scaleNewSpawn * WIDTH / 2, HEIGHT / 2 - scaleNewSpawn * HEIGHT / 2);
			affineTransform.scale(scaleNewSpawn, scaleNewSpawn); // масштабируем
			Graphics2D graphics = (Graphics2D) startImage.getGraphics(); // Graphics2D для начального изображения
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC); // сглаживание
			graphics.setColor(new Color(0, 0, 0, 0)); // очищаем поле
			graphics.fillRect(0, 0, WIDTH, HEIGHT); // создаем поле кубика для отрисовки
			graphics.drawImage(cubeImage, affineTransform, null); // отрисовываем изображение кубика
			scaleNewSpawn += 0.1; // становится больше на 10%
			graphics.dispose(); // освобождаем ресурсы, занимаемые компонентами окна
			if(scaleNewSpawn >= 1) newSpawnAnimation = false;  // запрещаем анимацию спаунов, если уже есть >= спауна
		}
		else if(uniteAnimation) { // если надо объединить кубики
			AffineTransform affineTransform = new AffineTransform(); // аффинная трансформация для анимации масштаба
			affineTransform.translate(WIDTH / 2 - scaleUnited * WIDTH / 2, HEIGHT / 2 - scaleUnited * HEIGHT / 2);
			affineTransform.scale(scaleUnited, scaleUnited); // масштабируем
			Graphics2D graphics = (Graphics2D) unitedImage.getGraphics(); // Graphics2D для объединения кубиков
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC); // сглаживание
			graphics.setColor(new Color(0, 0, 0, 0)); // очищаем поле
			graphics.fillRect(0, 0, WIDTH, HEIGHT); // создаем поле кубика для отрисовки
			graphics.drawImage(cubeImage, affineTransform, null); // отрисовываем изображение кубика
			scaleUnited -= 0.08; // уменьшение масштаба кубика при объединении
			graphics.dispose(); // освобождаем ресурсы, занимаемые компонентами окна
			if(scaleUnited <= 1) uniteAnimation = false; // запрещаем анимацию объединения, если уже объединились
		}
	}
	
	public void renderCubes(Graphics2D graphics) { // рендерим графику кубиков
		if(newSpawnAnimation) { // для начального изображения кубика
			graphics.drawImage(startImage, x, y, null);
		}
		else if(uniteAnimation) { // для анимации объединения кубиков
			graphics.drawImage(unitedImage, (int)(x + WIDTH / 2 - scaleUnited * WIDTH / 2), 
								   (int)(y + HEIGHT / 2 - scaleUnited * HEIGHT / 2), null);
		}
		else { // для изображения кубиков по дефолту
			graphics.drawImage(cubeImage, x, y, null);
		}
	}
	
	// геттеры и сеттеры для координат x, y
	public int getX() { return x;}

	public void setX(int x) { this.x = x; }

	public int getY() { return y; }

	public void setY(int y) { this.y = y; }

	// геттер и сеттер для значений величин на кубике
	public int getValue() { return value; }

	public void setValue(int value) {
		this.value = value;
		drawCubes();
	}

	// геттер и сеттер для перемещения по rows/cols
	public Spot getSlide() { return slide; }

	public void setSlide(Spot slide) { this.slide = slide; }
	
	// геттер и сеттер для возможности объединения
	public boolean uniteAbility() { return uniteAbility; }

	public void setUniteAbility(boolean uniteAbility) {
		this.uniteAbility = uniteAbility;
	}
	
	// геттер и сеттер для анимации объединения
	public boolean uniteAnimation() { return uniteAnimation; }
	
	public void setUniteAnimation(boolean uniteAnimation) {
		this.uniteAnimation = uniteAnimation;
		if(uniteAnimation) scaleUnited = 1.2;
	}
}
