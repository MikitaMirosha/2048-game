package mirosha.game;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

public class DisplayObject { // класс для отображения нужных графических объектов

	public static int getObjectWidth(String text, Font font, Graphics2D graphics) { // ширина объекта
		graphics.setFont(font); // устанавливаем фон
		Rectangle2D bounds = graphics.getFontMetrics().getStringBounds(text, graphics); // создаем прямоугольник в Graphics2D
		return (int) bounds.getWidth(); // возврат ширины
	}

	public static int getObjectHeight(String text, Font font, Graphics2D graphics) { // высота объекта
		graphics.setFont(font); // устанавливаем фон
		if(text.length() == 0) return 0; // возврат 0, если нулевая длина текста
		TextLayout textLayout = new TextLayout(text, font, graphics.getFontRenderContext()); // рисуем текст в Graphics2D
		return (int) textLayout.getBounds().getHeight(); // возврат текстовых данных согласно высоте
	}
}
