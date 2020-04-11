package mirosha.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * Класс для работы с кубами 
 * @author Mirosha
 * @version 1.0
 */
public class Cube { 

	/** Поле координата x*/
	private int x;
	
	/** Поле координата y*/
	private int y;
	
	/** Поле значение на кубе*/
	private int value;
	
	/** Поле шрифт на кубе*/
	private Font font; 
	
	/** Поле цвет фона*/
	private Color bg; 
	
	/** Поле цвет текста*/
	private Color text; 
	
	/** Поле направление движения (row/col)*/
	private Spot slide;
	
	/** Поле отрисовка куба*/
	private BufferedImage cubeImage; 
	
	/** Поле закругление углов куба по ширине*/
	public static final int ARCW = 15; 
	
	/** Поле закругление углов куба по высоте*/
	public static final int ARCH = 15; 
	
	/** Поле скорость передвижения кубов*/
	public static final int SPEED = 35; 
	
	/** Поле ширина игрового поля*/
	public static final int WIDTH = 120; 
	
	/** Поле высота игрового поля*/
	public static final int HEIGHT = 120; 

	/** Поле анимация нового спауна*/
	private boolean newSpawnAnimation = true; 
	
	/** Поле масштаб анимации нового спауна (10%)*/
	private double scaleNewSpawn = 0.1; 
	
	/** Поле отрисовка начального изображения*/
	private BufferedImage startImage; 
	
	/** Поле анимация объединения кубов*/
	private boolean uniteAnimation = false; 
	
	/** Поле увеличение масштаба на 120% при объединении кубов */
	private double scaleUnited = 1.2; 
	
	/** Поле отрисовка объединения кубов */
	private BufferedImage unitedImage; 
	
	/** Поле флаг возможности объединения кубов */
	private boolean uniteAbility = true; 

	/** 
     * Конструктор - создание нового объекта куба
     * @param value - значение на кубе
     * @param x - координата x
     * @param y - координата y
     */
	public Cube(int value, int x, int y) { 
		this.x = x;
		this.y = y;
		this.value = value;
		slide = new Spot(x, y); // активация координат перемещения по rows/cols
		cubeImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		startImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		unitedImage = new BufferedImage(WIDTH * 2, HEIGHT * 2, BufferedImage.TYPE_INT_ARGB);
		drawCubes(); // отрисовка финальных кубов
	}
	
	/**
     * Процедура задание цвета фона и текста для куба по значению
     */
	private void drawCubes() { 
		Graphics2D graphics = (Graphics2D) cubeImage.getGraphics(); 
		
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
		graphics.fillRect(0, 0, WIDTH, HEIGHT); 
		graphics.setColor(bg); 
		graphics.fillRoundRect(0, 0, WIDTH, HEIGHT, ARCW, ARCH); // учитываем закругленные углы
		graphics.setColor(text);

		if (value <= 64) { 
			font = Game.main.deriveFont(30f); 
			graphics.setFont(font);
		}
		else { 
			font = Game.main; 
			graphics.setFont(font);
		}

		// установка координат по центру для отрисовки значения на кубе:
		int drawX = WIDTH / 2 - DisplayObject.getObjectWidth("" + value, font, graphics) / 2;
		int drawY = HEIGHT / 2 + DisplayObject.getObjectHeight("" + value, font, graphics) / 2;
		graphics.drawString("" + value, drawX, drawY); // запись значения в строку на кубе
		graphics.dispose(); 
	}

	/**
     * Процедура обновление анимации куба
     */
	public void updateCubeAnimation() {
		if (newSpawnAnimation) { // если новый спаун
			AffineTransform affineTransform = new AffineTransform(); // аффинная трансформация для анимации масштаба
			affineTransform.translate(WIDTH / 2 - scaleNewSpawn * WIDTH / 2, HEIGHT / 2 - scaleNewSpawn * HEIGHT / 2);
			affineTransform.scale(scaleNewSpawn, scaleNewSpawn); // масштабирование
			Graphics2D graphics = (Graphics2D) startImage.getGraphics(); // Graphics2D для начального изображения
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC); // сглаживание
			graphics.setColor(new Color(0, 0, 0, 0)); // очистка поля
			graphics.fillRect(0, 0, WIDTH, HEIGHT); // создание поля куба для отрисовки
			graphics.drawImage(cubeImage, affineTransform, null); // отрисовка изображения куба
			scaleNewSpawn += 0.1; // становится больше на 10%
			graphics.dispose(); // освобождение ресурсов
			if(scaleNewSpawn >= 1) newSpawnAnimation = false;  // запрет анимации спаунов, если уже есть >= спауна
		}
		else if(uniteAnimation) { // если надо объединить кубы
			AffineTransform affineTransform = new AffineTransform(); // аффинная трансформация для анимации масштаба
			affineTransform.translate(WIDTH / 2 - scaleUnited * WIDTH / 2, HEIGHT / 2 - scaleUnited * HEIGHT / 2);
			affineTransform.scale(scaleUnited, scaleUnited); // масштабирование
			Graphics2D graphics = (Graphics2D) unitedImage.getGraphics(); // Graphics2D для объединения кубов
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC); // сглаживание
			graphics.setColor(new Color(0, 0, 0, 0)); // очистка поля
			graphics.fillRect(0, 0, WIDTH, HEIGHT); // создание поля куба для отрисовки
			graphics.drawImage(cubeImage, affineTransform, null); // отрисовка изображения куба
			scaleUnited -= 0.08; // уменьшение масштаба куба при объединении
			graphics.dispose(); // освобождение ресурсов
			if(scaleUnited <= 1) uniteAnimation = false; // запрет анимации объединения, если уже объединились
		}
	}
	
	/**
     * Процедура рендер кубов
     * @param graphics - графика для отрисовки куба
     */
	public void renderCubes(Graphics2D graphics) { 
		if(newSpawnAnimation) { // для начального изображения куба
			graphics.drawImage(startImage, x, y, null);
		}
		else if(uniteAnimation) { // для анимации объединения кубов
			graphics.drawImage(unitedImage, (int)(x + WIDTH / 2 - scaleUnited * WIDTH / 2), 
								   (int)(y + HEIGHT / 2 - scaleUnited * HEIGHT / 2), null);
		}
		else { // для изображения кубов по дефолту
			graphics.drawImage(cubeImage, x, y, null);
		}
	}
	
	/**
     * Функция получения значения поля {@link #x}
     * @return возвращает координату x
     */
	public int getX() { return x;}

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
     * Функция получения значения поля {@link #value}
     * @return возвращает значение на кубе
     */
	public int getValue() { return value; }

	/**
     * Процедура установки значения на кубе
     * @param value - значение на кубе
     */
	public void setValue(int value) {
		this.value = value;
		drawCubes();
	}

	/**
     * Функция получения значения поля {@link #slide}
     * @return возвращает значение направления движения
     */
	public Spot getSlide() { return slide; }

	/**
     * Процедура установки значения направления движения
     * @param slide - значение направления движения
     */
	public void setSlide(Spot slide) { this.slide = slide; }
	
	/**
     * Функция получения значения поля {@link #uniteAbility}
     * @return возвращает значение флага возможности объединения
     */
	public boolean uniteAbility() { return uniteAbility; }

	/**
     * Процедура установки значения флага возможности объединения
     * @param uniteAbility - значение флага возможности объединения
     */
	public void setUniteAbility(boolean uniteAbility) {
		this.uniteAbility = uniteAbility;
	}
	
	/**
     * Функция получения значения поля {@link #uniteAnimation}
     * @return возвращает значение флага для анимации объединения
     */
	public boolean uniteAnimation() { return uniteAnimation; }
	
	/**
     * Процедура установки значения флага для анимации объединения
     * @param uniteAnimation - значение флага для анимации объединения
     */
	public void setUniteAnimation(boolean uniteAnimation) {
		this.uniteAnimation = uniteAnimation;
		if(uniteAnimation) scaleUnited = 1.2;
	}
}
