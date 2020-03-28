package mirosha.game;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

public class DisplayObject { // ����� ��� ����������� ������ ����������� ��������

	public static int getObjectWidth(String text, Font font, Graphics2D graphics) {  // ������ �������
		graphics.setFont(font);  // ������������� ���
		Rectangle2D bounds = graphics.getFontMetrics().getStringBounds(text, graphics);  // ������� ������������� � Graphics2D
		return (int) bounds.getWidth(); // ������� ������
	}

	public static int getObjectHeight(String text, Font font, Graphics2D graphics) {  // ������ �������
		graphics.setFont(font);  // ������������� ���
		if(text.length() == 0) return 0; // ������� 0, ���� ������� ����� ������
		TextLayout textLayout = new TextLayout(text, font, graphics.getFontRenderContext()); // ������ ����� � Graphics2D
		return (int) textLayout.getBounds().getHeight(); // ������� ��������� ������ �������� ������
	}
}
