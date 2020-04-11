package mirosha.game;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

/**
 * Класс для отображения нужных графических объектов
 * @author Mirosha
 * @version 1.0
 */
public class DisplayObject { 

	/**
     * Функция получение значения ширины объекта
     * @param text - текст объекта
     * @param font - шрифт объекта
     * @param graphics - графика объекта
     * @return возвращает ширину объекта
     */
	public static int getObjectWidth(String text, Font font, Graphics2D graphics) {  
		graphics.setFont(font);  // создание прямоугольника Graphics2D: 
		Rectangle2D bounds = graphics.getFontMetrics().getStringBounds(text, graphics);  
		return (int) bounds.getWidth(); 
	}

	/**
     * Функция получение значения высоты объекта
     * @param text - текст объекта
     * @param font - шрифт объекта
     * @param graphics - графика объекта
     * @return возвращает текстовые данные согласно высоте
     */
	public static int getObjectHeight(String text, Font font, Graphics2D graphics) {  
		graphics.setFont(font);  
		if(text.length() == 0) return 0; // возврат 0, если нулевая длина текста
		TextLayout textLayout = new TextLayout(text, font, graphics.getFontRenderContext()); // отрисовка текста
		return (int) textLayout.getBounds().getHeight(); 
	}
}
