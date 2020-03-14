package com.mirosha.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

public class Cube {
	public static final int WIDTH = 150;
	public static final int HEIGHT = 150;
	public static final int ARC_WIDTH = 25;
	public static final int ARC_HEIGHT = 25;
	public static final int SLIDING_SPEED = 30;
	
	private int x; 
	private int y;
	private int value;
	private Color bg;
	private Color text;
	private Font font;
	private Spot slide;
	private BufferedImage cubeImage;
	private boolean newSpawnAnimation = true; 
	private double scaleNewSpawn = 0.1; 
	private BufferedImage startImage;
	private boolean combineAnimation = false; 
	private double scaleCombine = 1.2; // 120% размера увеличивается при комбинировании
	private BufferedImage combineImage;
	private boolean combineAbility = true; // отслеживает скомбинированы ли уже кубики или нет
	
	public Cube(int value, int x, int y) { // конструктор кубика
		this.value = value;
		this.x = x;
		this.y = y;
		slide = new Spot(x, y);
		cubeImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB); 
		startImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB); 
		combineImage = new BufferedImage(WIDTH * 2, HEIGHT * 2, BufferedImage.TYPE_INT_ARGB);
		drawImage();
	}
	
	private void drawImage() {
		Graphics2D graphics = (Graphics2D)cubeImage.getGraphics();
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
		graphics.setColor(bg); // обновление цвета для разных кубиков
		graphics.fillRoundRect(0, 0, WIDTH, HEIGHT, ARC_WIDTH, ARC_HEIGHT);
		graphics.setColor(text); // цифра на кубике 
		font = Game.main.deriveFont(36f); 
		graphics.setFont(font);
		
		// отцентровка цифр на кубиках:
		int drawX = WIDTH / 2 - DrawBar.getBarWidth("" + value, font, graphics) / 2; 
		int drawY = HEIGHT / 2 + DrawBar.getBarHeight("" + value, font, graphics) / 2; 
		graphics.drawString("" +  value, drawX, drawY); 
		graphics.dispose(); // освободить ресурсы, занимаемые компонентами окна
	}
	
	public void update() {
		if(newSpawnAnimation) {
			AffineTransform transform = new AffineTransform(); // для scaling
			transform.translate(WIDTH / 2 - scaleNewSpawn * WIDTH / 2, HEIGHT / 2 - scaleNewSpawn * HEIGHT / 2); // идет в середину и трансформирует 
			transform.scale(scaleNewSpawn, scaleNewSpawn);
			Graphics2D graphics = (Graphics2D)startImage.getGraphics();
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC); // как мы трансформируем
			graphics.setColor(new Color(0, 0, 0, 0)); // чистим
			graphics.fillRect(0, 0, WIDTH, HEIGHT);
			graphics.drawImage(cubeImage, transform, null);
			scaleNewSpawn += 0.1 ; // становится больше на 10%
			graphics.dispose();
			if(scaleNewSpawn >= 1)
				newSpawnAnimation = false;
		}
		else if(combineAnimation) {
			AffineTransform transform = new AffineTransform();
			transform.translate(WIDTH / 2 - scaleCombine * WIDTH / 2, HEIGHT / 2 - scaleCombine * HEIGHT / 2);
			transform.scale(scaleCombine, scaleCombine);
			Graphics2D graphics = (Graphics2D)combineImage.getGraphics();
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			graphics.setColor(new Color(0, 0, 0, 0));
			graphics.fillRect(0, 0, WIDTH, HEIGHT);
			graphics.drawImage(cubeImage, transform, null);
			scaleCombine -= 0.05;
			graphics.dispose();
			if(scaleCombine <= 1)
				combineAnimation = false;
		}
	}
	
	public void render(Graphics2D renderGraphics) {
		if(newSpawnAnimation) {
			renderGraphics.drawImage(startImage, x, y, null);
		}
		else if(combineAnimation) { // рисуем от центра
			renderGraphics.drawImage(combineImage, (int)(x + WIDTH / 2 - scaleCombine * WIDTH / 2), 
					                  (int)(y + HEIGHT / 2 - scaleCombine * HEIGHT / 2), null);
		}
		else {
			renderGraphics.drawImage(cubeImage, x, y, null);	
		}
	}
	
	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}
	
	public int getValue() {
		return value;
	}
	
	public void setValue(int value) {
		this.value = value; // когда установили новое значение 
		drawImage(); // перерисовываем плитку
	}

	public boolean combineAbility() {
		return combineAbility;
	}

	public void setCombineAbility(boolean combineAbility) {
		this.combineAbility = combineAbility;
	}

	public Spot getSlideTo() {
		return slide;
	}

	public void setSlideTo(Spot slideTo) {
		this.slide = slideTo;
	}

	public boolean getCombineAnimation() {
		return combineAnimation;
	}

	public void setCombineAnimation(boolean combineAnimation) {
		this.combineAnimation = combineAnimation;
		if(combineAnimation) scaleCombine = 1.2;
	}
}
