package mirosha.game;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

/**
 * ����� ��� ����������� ������ ����������� ��������
 * @author Mirosha
 * @version 1.0
 */
public class DisplayObject { 

	/**
     * ������� ��������� �������� ������ �������
     * @param text - ����� �������
     * @param font - ����� �������
     * @param graphics - ������� �������
     * @return ���������� ������ �������
     */
	public static int getObjectWidth(String text, Font font, Graphics2D graphics) {  
		graphics.setFont(font);  // �������� �������������� Graphics2D: 
		Rectangle2D bounds = graphics.getFontMetrics().getStringBounds(text, graphics);  
		return (int) bounds.getWidth(); 
	}

	/**
     * ������� ��������� �������� ������ �������
     * @param text - ����� �������
     * @param font - ����� �������
     * @param graphics - ������� �������
     * @return ���������� ��������� ������ �������� ������
     */
	public static int getObjectHeight(String text, Font font, Graphics2D graphics) {  
		graphics.setFont(font);  
		if(text.length() == 0) return 0; // ������� 0, ���� ������� ����� ������
		TextLayout textLayout = new TextLayout(text, font, graphics.getFontRenderContext()); // ��������� ������
		return (int) textLayout.getBounds().getHeight(); 
	}
}
