package mirosha.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;

/**
 * ����� ��� ������ � ������ 
 * @author Mirosha
 * @version 1.0
 */
public class Cube { 

	/** ���� ���������� x*/
	private int x;
	
	/** ���� ���������� y*/
	private int y;
	
	/** ���� �������� �� ����*/
	private int value;
	
	/** ���� ����� �� ����*/
	private Font font; 
	
	/** ���� ���� ����*/
	private Color bg; 
	
	/** ���� ���� ������*/
	private Color text; 
	
	/** ���� ����������� �������� (row/col)*/
	private Spot slide;
	
	/** ���� ��������� ����*/
	private BufferedImage cubeImage; 
	
	/** ���� ����������� ����� ���� �� ������*/
	public static final int ARCW = 15; 
	
	/** ���� ����������� ����� ���� �� ������*/
	public static final int ARCH = 15; 
	
	/** ���� �������� ������������ �����*/
	public static final int SPEED = 35; 
	
	/** ���� ������ �������� ����*/
	public static final int WIDTH = 120; 
	
	/** ���� ������ �������� ����*/
	public static final int HEIGHT = 120; 

	/** ���� �������� ������ ������*/
	private boolean newSpawnAnimation = true; 
	
	/** ���� ������� �������� ������ ������ (10%)*/
	private double scaleNewSpawn = 0.1; 
	
	/** ���� ��������� ���������� �����������*/
	private BufferedImage startImage; 
	
	/** ���� �������� ����������� �����*/
	private boolean uniteAnimation = false; 
	
	/** ���� ���������� �������� �� 120% ��� ����������� ����� */
	private double scaleUnited = 1.2; 
	
	/** ���� ��������� ����������� ����� */
	private BufferedImage unitedImage; 
	
	/** ���� ���� ����������� ����������� ����� */
	private boolean uniteAbility = true; 

	/** 
     * ����������� - �������� ������ ������� ����
     * @param value - �������� �� ����
     * @param x - ���������� x
     * @param y - ���������� y
     */
	public Cube(int value, int x, int y) { 
		this.x = x;
		this.y = y;
		this.value = value;
		slide = new Spot(x, y); // ��������� ��������� ����������� �� rows/cols
		cubeImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		startImage = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_ARGB);
		unitedImage = new BufferedImage(WIDTH * 2, HEIGHT * 2, BufferedImage.TYPE_INT_ARGB);
		drawCubes(); // ��������� ��������� �����
	}
	
	/**
     * ��������� ������� ����� ���� � ������ ��� ���� �� ��������
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
		graphics.setColor(new Color(0, 0, 0, 0)); // ���������� ���� (��� ����������� �����)
		graphics.fillRect(0, 0, WIDTH, HEIGHT); 
		graphics.setColor(bg); 
		graphics.fillRoundRect(0, 0, WIDTH, HEIGHT, ARCW, ARCH); // ��������� ������������ ����
		graphics.setColor(text);

		if (value <= 64) { 
			font = Game.main.deriveFont(30f); 
			graphics.setFont(font);
		}
		else { 
			font = Game.main; 
			graphics.setFont(font);
		}

		// ��������� ��������� �� ������ ��� ��������� �������� �� ����:
		int drawX = WIDTH / 2 - DisplayObject.getObjectWidth("" + value, font, graphics) / 2;
		int drawY = HEIGHT / 2 + DisplayObject.getObjectHeight("" + value, font, graphics) / 2;
		graphics.drawString("" + value, drawX, drawY); // ������ �������� � ������ �� ����
		graphics.dispose(); 
	}

	/**
     * ��������� ���������� �������� ����
     */
	public void updateCubeAnimation() {
		if (newSpawnAnimation) { // ���� ����� �����
			AffineTransform affineTransform = new AffineTransform(); // �������� ������������� ��� �������� ��������
			affineTransform.translate(WIDTH / 2 - scaleNewSpawn * WIDTH / 2, HEIGHT / 2 - scaleNewSpawn * HEIGHT / 2);
			affineTransform.scale(scaleNewSpawn, scaleNewSpawn); // ���������������
			Graphics2D graphics = (Graphics2D) startImage.getGraphics(); // Graphics2D ��� ���������� �����������
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC); // �����������
			graphics.setColor(new Color(0, 0, 0, 0)); // ������� ����
			graphics.fillRect(0, 0, WIDTH, HEIGHT); // �������� ���� ���� ��� ���������
			graphics.drawImage(cubeImage, affineTransform, null); // ��������� ����������� ����
			scaleNewSpawn += 0.1; // ���������� ������ �� 10%
			graphics.dispose(); // ������������ ��������
			if(scaleNewSpawn >= 1) newSpawnAnimation = false;  // ������ �������� �������, ���� ��� ���� >= ������
		}
		else if(uniteAnimation) { // ���� ���� ���������� ����
			AffineTransform affineTransform = new AffineTransform(); // �������� ������������� ��� �������� ��������
			affineTransform.translate(WIDTH / 2 - scaleUnited * WIDTH / 2, HEIGHT / 2 - scaleUnited * HEIGHT / 2);
			affineTransform.scale(scaleUnited, scaleUnited); // ���������������
			Graphics2D graphics = (Graphics2D) unitedImage.getGraphics(); // Graphics2D ��� ����������� �����
			graphics.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC); // �����������
			graphics.setColor(new Color(0, 0, 0, 0)); // ������� ����
			graphics.fillRect(0, 0, WIDTH, HEIGHT); // �������� ���� ���� ��� ���������
			graphics.drawImage(cubeImage, affineTransform, null); // ��������� ����������� ����
			scaleUnited -= 0.08; // ���������� �������� ���� ��� �����������
			graphics.dispose(); // ������������ ��������
			if(scaleUnited <= 1) uniteAnimation = false; // ������ �������� �����������, ���� ��� ������������
		}
	}
	
	/**
     * ��������� ������ �����
     * @param graphics - ������� ��� ��������� ����
     */
	public void renderCubes(Graphics2D graphics) { 
		if(newSpawnAnimation) { // ��� ���������� ����������� ����
			graphics.drawImage(startImage, x, y, null);
		}
		else if(uniteAnimation) { // ��� �������� ����������� �����
			graphics.drawImage(unitedImage, (int)(x + WIDTH / 2 - scaleUnited * WIDTH / 2), 
								   (int)(y + HEIGHT / 2 - scaleUnited * HEIGHT / 2), null);
		}
		else { // ��� ����������� ����� �� �������
			graphics.drawImage(cubeImage, x, y, null);
		}
	}
	
	/**
     * ������� ��������� �������� ���� {@link #x}
     * @return ���������� ���������� x
     */
	public int getX() { return x;}

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
     * ������� ��������� �������� ���� {@link #value}
     * @return ���������� �������� �� ����
     */
	public int getValue() { return value; }

	/**
     * ��������� ��������� �������� �� ����
     * @param value - �������� �� ����
     */
	public void setValue(int value) {
		this.value = value;
		drawCubes();
	}

	/**
     * ������� ��������� �������� ���� {@link #slide}
     * @return ���������� �������� ����������� ��������
     */
	public Spot getSlide() { return slide; }

	/**
     * ��������� ��������� �������� ����������� ��������
     * @param slide - �������� ����������� ��������
     */
	public void setSlide(Spot slide) { this.slide = slide; }
	
	/**
     * ������� ��������� �������� ���� {@link #uniteAbility}
     * @return ���������� �������� ����� ����������� �����������
     */
	public boolean uniteAbility() { return uniteAbility; }

	/**
     * ��������� ��������� �������� ����� ����������� �����������
     * @param uniteAbility - �������� ����� ����������� �����������
     */
	public void setUniteAbility(boolean uniteAbility) {
		this.uniteAbility = uniteAbility;
	}
	
	/**
     * ������� ��������� �������� ���� {@link #uniteAnimation}
     * @return ���������� �������� ����� ��� �������� �����������
     */
	public boolean uniteAnimation() { return uniteAnimation; }
	
	/**
     * ��������� ��������� �������� ����� ��� �������� �����������
     * @param uniteAnimation - �������� ����� ��� �������� �����������
     */
	public void setUniteAnimation(boolean uniteAnimation) {
		this.uniteAnimation = uniteAnimation;
		if(uniteAnimation) scaleUnited = 1.2;
	}
}
