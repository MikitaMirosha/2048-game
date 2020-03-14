package com.mirosha.game;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;

public class DrawBar {
	private DrawBar() { }
	// для оцентровки на экране
	public static int getBarWidth(String bar, Font font, Graphics2D graphics) { 
		graphics.setFont(font);
		Rectangle2D rectangleBounds = graphics.getFontMetrics().getStringBounds(bar, graphics);
		return(int)rectangleBounds.getWidth();
	}
	
	public static int getBarHeight(String bar, Font font, Graphics2D graphics) {
		graphics.setFont(font);
		if(bar.length() == 0) return 0;
		TextLayout layout = new TextLayout(bar, font, graphics.getFontRenderContext());
		return (int)layout.getBounds().getHeight(); 
	}
}
