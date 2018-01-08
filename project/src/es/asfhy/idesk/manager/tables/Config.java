package es.asfhy.idesk.manager.tables;

import java.awt.Color;
import java.util.Scanner;

import es.asfhy.idesk.manager.enums.BackgroundMode;
import es.asfhy.idesk.manager.enums.CursorOver;
import es.asfhy.idesk.manager.enums.FillStyle;
import es.asfhy.idesk.manager.enums.Placement;
import es.asfhy.idesk.manager.enums.SnapOrigin;

public class Config {
	private static String color(Color col) {
		return String.format("#%02X%02X%02X", col.getRed(), col.getGreen(), col.getBlue());
	}
	
	private static Color color(String col) {
		int r = Integer.parseInt(col.substring(1, 3), 16);
		int g = Integer.parseInt(col.substring(3, 5), 16);
		int b = Integer.parseInt(col.substring(5, 7), 16);
		return new Color(r, g, b);
	}
	
	private static boolean bool(String bool) {
		return Boolean.valueOf(bool);
	}
	
	public static int _int(String in) {
		return Integer.parseInt(in);
	}
	
	private static Placement placement(String pl) {
		Placement out = Placement.Bottom;
		try {
			out = Placement.valueOf(pl);
		} catch (Exception e) {
		}
		return out;
	}
	
	private static CursorOver cursor(String cu) {
		CursorOver out = CursorOver.arrow;
		try {
			out = CursorOver.valueOf(cu);
		} catch (Exception e) {
			
		}
		return out;
	}
	
	private SnapOrigin snap(String sn) {
		SnapOrigin out = SnapOrigin.TopLeft;
		try {
			out = SnapOrigin.valueOf(sn);
		} catch (Exception e) {
			
		}
		return out;
	}
	
	private FillStyle style(String bm) {
		FillStyle out = FillStyle.DEFAULT;
		try {
			out = FillStyle.valueOf(bm);
		} catch (Exception e) {
			
		}
		return out;
	}
	
	private BackgroundMode mode(String bm) {
		BackgroundMode out = BackgroundMode.Fit;
		try {
			out = BackgroundMode.valueOf(bm);
		} catch (Exception e) {
			
		}
		return out;
	}
	
	// ¿Bloqueado el movimiento de iconos?
	private boolean			locked					= false;
	// Para Detectar Doble Click:
	private int				clickDelay				= 250;
	// Rejilla del Escritorio:
	private boolean			iconSnap				= false;
	private int				snapWidth				= 50;
	private int				snapHeight				= 64;
	private SnapOrigin		snapOrigin				= SnapOrigin.DEFAULT;
	private boolean			snapShadow				= true;
	private int				snapShadowTrans			= 128;
	// Iconos del Escritorio:
	private String			fontName				= "Arial";
	private boolean			fontBold				= true;
	private int				fontSize				= 16;
	private Color			fontColor				= Color.black;
	private CursorOver		cursorOver				= CursorOver.DEFAULT;
	private int				transparency			= 0;
	private boolean			captionOnHover			= false;
	private Placement		captionPlacement		= Placement.Bottom;
	// Sombra de los Iconos:
	private boolean			useShadow				= true;
	private int				shadowX					= 1;
	private int				shadowY					= 1;
	private Color			shadowColor				= Color.black;
	// Estilo del Icono cuando se le Pulsa:
	private FillStyle		fillStyle				= FillStyle.DEFAULT;
	// Tooltips:
	private String			tooltipFontName			= "Arial";
	private int				tooltipFontSize			= 16;
	private Color			tooltipForeColor		= Color.BLACK;
	private Color			tooltipBackColor		= Color.WHITE;
	private boolean			tooltipCaptionOnHover	= true;
	private Placement		tooltipCaptionPlacement	= Placement.Right;
	// Background:
	private String			backgroundFile			= null;
	private String			backgroundSource		= null;
	private int				backgroundDelay			= 0;
	private Color			backgroundColor			= Color.white;
	private BackgroundMode	backgroundMode			= BackgroundMode.Stretch;
	
	public synchronized String getTableString() {
		StringBuilder out = new StringBuilder("table Config\n");
		out.append("\tLocked: ").append(locked).append("\n");
		out.append("\tClickDelay: ").append(clickDelay).append("\n");
		out.append("\tIconSnap: ").append(iconSnap).append("\n");
		out.append("\tSnapWidth: ").append(snapWidth).append("\n");
		out.append("\tSnapHeight: ").append(snapHeight).append("\n");
		out.append("\tSnapOrigin: ").append(snapOrigin == null ? SnapOrigin.TopLeft : snapOrigin).append("\n");
		out.append("\tSnapShadow: ").append(snapShadow).append("\n");
		out.append("\tSnapShadowTrans: ").append(Math.max(0, Math.min(255, snapShadowTrans))).append("\n");
		out.append("\tFontName: ").append(fontName == null || fontName.trim().length() == 0 ? "sans" : fontName).append("\n");
		out.append("\tBold: ").append(fontBold).append("\n");
		out.append("\tFontSize: ").append(Math.max(5, Math.min(512, fontSize))).append("\n");
		out.append("\tFontColor: ").append(color(fontColor == null ? Color.white : fontColor)).append("\n");
		out.append("\tCursorOver: ").append(cursorOver == null ? CursorOver.arrow : cursorOver).append("\n");
		out.append("\tTransparency: ").append(Math.max(0, Math.min(255, transparency))).append("\n");
		out.append("\tCaptionOnHover: ").append(captionOnHover).append("\n");
		out.append("\tCaptionPlacement: ").append(captionPlacement == null ? Placement.Bottom : captionPlacement).append("\n");
		out.append("\tShadow: ").append(useShadow).append("\n");
		out.append("\tShadowColor: ").append(color(shadowColor == null ? Color.black : shadowColor)).append("\n");
		out.append("\tShadowX: ").append(shadowX).append("\n");
		out.append("\tShadowY: ").append(shadowY).append("\n");
		out.append("\tFillStyle: ").append(fillStyle == null ? FillStyle.None : fillStyle).append("\n");
		out.append("\tToolTip.FontName: ").append(tooltipFontName == null || tooltipFontName.trim().length() == 0 ? "sans" : tooltipFontName).append("\n");
		out.append("\tToolTip.FontSize: ").append(Math.max(5, Math.min(512, tooltipFontSize))).append("\n");
		out.append("\tToolTip.CaptionOnHover: ").append(tooltipCaptionOnHover).append("\n");
		out.append("\tToolTip.CaptionPlacement: ").append(tooltipCaptionPlacement == null ? Placement.Right : tooltipCaptionPlacement).append("\n");
		out.append("\tToolTip.ForeColor: ").append(color(tooltipForeColor == null ? Color.black : tooltipForeColor)).append("\n");
		out.append("\tToolTip.BackColor: ").append(color(tooltipBackColor == null ? Color.white : tooltipBackColor)).append("\n");
		if (backgroundFile != null && backgroundFile.trim().length() != 0)
			out.append("\tBackground.File: ").append(backgroundFile).append("\n");
		if (backgroundSource != null && backgroundSource.trim().length() != 0)
			out.append("\tBackground.Source: ").append(backgroundSource).append("\n");
		out.append("\tBackground.Delay: ").append(Math.max(0, Math.min(526000, backgroundDelay))).append("\n");
		out.append("\tBackground.Color: ").append(color(backgroundColor == null ? Color.white : backgroundColor)).append("\n");
		out.append("\tBackground.Mode: ").append(backgroundMode == null ? BackgroundMode.Fit : backgroundMode).append("\n");
		return out.append("end").toString();
	}
	
	public synchronized void addProperties(Scanner scan) {
		String line = null;
		String parts[] = null;
		do {
			line = scan.nextLine();
			if (line == null)
				continue;
			line = line.trim();
			parts = line.split(":", 2);
			if (parts.length == 2 && parts[1] != null && parts[1].trim().length() != 0) {
				String propName = parts[0].trim();
				String propValue = parts[1].trim();
				if (propName.equalsIgnoreCase("Locked"))
					locked = bool(propValue);
				else if (propName.equalsIgnoreCase("ClickDelay"))
					clickDelay = _int(propValue);
				else if (propName.equalsIgnoreCase("IconSnap"))
					iconSnap = bool(propValue);
				else if (propName.equalsIgnoreCase("SnapWidth"))
					snapWidth = _int(propValue);
				else if (propName.equalsIgnoreCase("SnapHeight"))
					snapHeight = _int(propValue);
				else if (propName.equalsIgnoreCase("SnapOrigin"))
					snapOrigin = snap(propValue);
				else if (propName.equalsIgnoreCase("SnapShadow"))
					snapShadow = bool(propValue);
				else if (propName.equalsIgnoreCase("SnapShadowTrans"))
					snapShadowTrans = _int(propValue);
				else if (propName.equalsIgnoreCase("FontName"))
					fontName = propValue;
				else if (propName.equalsIgnoreCase("Bold"))
					fontBold = bool(propValue);
				else if (propName.equalsIgnoreCase("FontSize"))
					fontSize = _int(propValue);
				else if (propName.equalsIgnoreCase("FontColor"))
					fontColor = color(propValue);
				else if (propName.equalsIgnoreCase("CursorOver"))
					cursorOver = cursor(propValue);
				else if (propName.equalsIgnoreCase("Transparency"))
					transparency = _int(propValue);
				else if (propName.equalsIgnoreCase("CaptionOnHover"))
					captionOnHover = bool(propValue);
				else if (propName.equalsIgnoreCase("CaptionPlacement"))
					captionPlacement = placement(propValue);
				else if (propName.equalsIgnoreCase("Shadow"))
					useShadow = bool(propValue);
				else if (propName.equalsIgnoreCase("ShadowX"))
					shadowX = _int(propValue);
				else if (propName.equalsIgnoreCase("ShadowY"))
					shadowY = _int(propValue);
				else if (propName.equalsIgnoreCase("ShadowColor"))
					shadowColor = color(propValue);
				else if (propName.equalsIgnoreCase("FillStyle"))
					fillStyle = style(propValue);
				else if (propName.equalsIgnoreCase("ToolTip.FontName"))
					tooltipFontName = propValue;
				else if (propName.equalsIgnoreCase("ToolTip.FontSize"))
					tooltipFontSize = _int(propValue);
				else if (propName.equalsIgnoreCase("ToolTip.ForeColor"))
					tooltipForeColor = color(propValue);
				else if (propName.equalsIgnoreCase("ToolTip.BackColor"))
					tooltipBackColor = color(propValue);
				else if (propName.equalsIgnoreCase("ToolTip.CaptionOnHover"))
					tooltipCaptionOnHover = bool(propValue);
				else if (propName.equalsIgnoreCase("ToolTip.CaptionPlacement"))
					tooltipCaptionPlacement = placement(propValue);
				else if (propName.equalsIgnoreCase("Background.File"))
					backgroundFile = propValue;
				else if (propName.equalsIgnoreCase("Background.Source"))
					backgroundSource = propValue;
				else if (propName.equalsIgnoreCase("Background.Delay"))
					backgroundDelay = _int(propValue);
				else if (propName.equalsIgnoreCase("Background.Color"))
					backgroundColor = color(propValue);
				else if (propName.equalsIgnoreCase("Background.Mode"))
					backgroundMode = mode(propValue);
			}
		} while (scan.hasNextLine() && !line.trim().startsWith("end"));
	}
	
	public synchronized boolean isLocked() {
		return locked;
	}
	
	public synchronized void setLocked(boolean locked) {
		this.locked = locked;
	}
	
	public synchronized int getClickDelay() {
		return clickDelay;
	}
	
	public synchronized void setClickDelay(int clickDelay) {
		this.clickDelay = clickDelay;
	}
	
	public synchronized boolean isIconSnap() {
		return iconSnap;
	}
	
	public synchronized void setIconSnap(boolean iconSnap) {
		this.iconSnap = iconSnap;
	}
	
	public synchronized int getSnapWidth() {
		return snapWidth;
	}
	
	public synchronized void setSnapWidth(int snapWidth) {
		this.snapWidth = snapWidth;
	}
	
	public synchronized int getSnapHeight() {
		return snapHeight;
	}
	
	public synchronized void setSnapHeight(int snapHeight) {
		this.snapHeight = snapHeight;
	}
	
	public synchronized SnapOrigin getSnapOrigin() {
		return snapOrigin;
	}
	
	public synchronized void setSnapOrigin(SnapOrigin snapOrigin) {
		this.snapOrigin = snapOrigin;
	}
	
	public synchronized boolean isSnapShadow() {
		return snapShadow;
	}
	
	public synchronized void setSnapShadow(boolean snapShadow) {
		this.snapShadow = snapShadow;
	}
	
	public synchronized int getSnapShadowTrans() {
		return snapShadowTrans;
	}
	
	public synchronized void setSnapShadowTrans(int snapShadowTrans) {
		this.snapShadowTrans = snapShadowTrans;
	}
	
	public synchronized String getFontName() {
		return fontName;
	}
	
	public synchronized void setFontName(String fontName) {
		this.fontName = fontName;
	}
	
	public synchronized boolean isFontBold() {
		return fontBold;
	}
	
	public synchronized void setFontBold(boolean fontBold) {
		this.fontBold = fontBold;
	}
	
	public synchronized int getFontSize() {
		return fontSize;
	}
	
	public synchronized void setFontSize(int fontSize) {
		this.fontSize = fontSize;
	}
	
	public synchronized Color getFontColor() {
		return fontColor;
	}
	
	public synchronized void setFontColor(Color fontColor) {
		this.fontColor = fontColor;
	}
	
	public synchronized CursorOver getCursorOver() {
		return cursorOver;
	}
	
	public synchronized void setCursorOver(CursorOver cursorOver) {
		this.cursorOver = cursorOver;
	}
	
	public synchronized int getTransparency() {
		return transparency;
	}
	
	public synchronized void setTransparency(int transparency) {
		this.transparency = transparency;
	}
	
	public synchronized boolean isCaptionOnHover() {
		return captionOnHover;
	}
	
	public synchronized void setCaptionOnHover(boolean captionOnHover) {
		this.captionOnHover = captionOnHover;
	}
	
	public synchronized Placement getCaptionPlacement() {
		return captionPlacement;
	}
	
	public synchronized void setCaptionPlacement(Placement captionPlacement) {
		this.captionPlacement = captionPlacement;
	}
	
	public synchronized boolean isUseShadow() {
		return useShadow;
	}
	
	public synchronized void setUseShadow(boolean useShadow) {
		this.useShadow = useShadow;
	}
	
	public synchronized int getShadowX() {
		return shadowX;
	}
	
	public synchronized void setShadowX(int shadowX) {
		this.shadowX = shadowX;
	}
	
	public synchronized int getShadowY() {
		return shadowY;
	}
	
	public synchronized void setShadowY(int shadowY) {
		this.shadowY = shadowY;
	}
	
	public synchronized Color getShadowColor() {
		return shadowColor;
	}
	
	public synchronized void setShadowColor(Color shadowColor) {
		this.shadowColor = shadowColor;
	}
	
	public synchronized FillStyle getFillStyle() {
		return fillStyle;
	}
	
	public synchronized void setFillStyle(FillStyle fillStyle) {
		this.fillStyle = fillStyle;
	}
	
	public synchronized String getTooltipFontName() {
		return tooltipFontName;
	}
	
	public synchronized void setTooltipFontName(String tooltipFontName) {
		this.tooltipFontName = tooltipFontName;
	}
	
	public synchronized int getTooltipFontSize() {
		return tooltipFontSize;
	}
	
	public synchronized void setTooltipFontSize(int tooltipFontSize) {
		this.tooltipFontSize = tooltipFontSize;
	}
	
	public synchronized Color getTooltipForeColor() {
		return tooltipForeColor;
	}
	
	public synchronized void setTooltipForeColor(Color tooltipForeColor) {
		this.tooltipForeColor = tooltipForeColor;
	}
	
	public synchronized Color getTooltipBackColor() {
		return tooltipBackColor;
	}
	
	public synchronized void setTooltipBackColor(Color tooltipBackColor) {
		this.tooltipBackColor = tooltipBackColor;
	}
	
	public synchronized boolean isTooltipCaptionOnHover() {
		return tooltipCaptionOnHover;
	}
	
	public synchronized void setTooltipCaptionOnHover(boolean tooltipCaptionOnHover) {
		this.tooltipCaptionOnHover = tooltipCaptionOnHover;
	}
	
	public synchronized Placement getTooltipCaptionPlacement() {
		return tooltipCaptionPlacement;
	}
	
	public synchronized void setTooltipCaptionPlacement(Placement tooltipCaptionPlacement) {
		this.tooltipCaptionPlacement = tooltipCaptionPlacement;
	}
	
	public synchronized String getBackgroundFile() {
		return backgroundFile;
	}
	
	public synchronized void setBackgroundFile(String backgroundFile) {
		this.backgroundFile = backgroundFile;
	}
	
	public synchronized String getBackgroundSource() {
		return backgroundSource;
	}
	
	public synchronized void setBackgroundSource(String backgroundSource) {
		this.backgroundSource = backgroundSource;
	}
	
	public synchronized int getBackgroundDelay() {
		return backgroundDelay;
	}
	
	public synchronized void setBackgroundDelay(int backgroundDelay) {
		this.backgroundDelay = backgroundDelay;
	}
	
	public synchronized Color getBackgroundColor() {
		return backgroundColor;
	}
	
	public synchronized void setBackgroundColor(Color backgroundColor) {
		this.backgroundColor = backgroundColor;
	}
	
	public synchronized BackgroundMode getBackgroundMode() {
		return backgroundMode;
	}
	
	public synchronized void setBackgroundMode(BackgroundMode backgroundMode) {
		this.backgroundMode = backgroundMode;
	}
}
