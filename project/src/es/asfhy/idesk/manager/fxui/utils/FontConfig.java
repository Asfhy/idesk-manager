package es.asfhy.idesk.manager.fxui.utils;

import java.awt.Color;

public class FontConfig {
	private String	fontName;
	private double	fontSize;
	private Color	foreColor;
	private Color	backColor;
	
	public synchronized String getFontName() {
		return fontName;
	}
	
	public synchronized void setFontName(String fontName) {
		this.fontName = fontName;
	}
	
	public synchronized double getFontSize() {
		return fontSize;
	}
	
	public synchronized void setFontSize(double fontSize) {
		this.fontSize = fontSize;
	}
	
	public synchronized Color getForeColor() {
		return foreColor;
	}
	
	public synchronized void setForeColor(Color foreColor) {
		this.foreColor = foreColor;
	}
	
	public synchronized Color getBackColor() {
		return backColor;
	}
	
	public synchronized void setBackColor(Color backColor) {
		this.backColor = backColor;
	}
}
