package es.asfhy.idesk.manager;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JLabel;

import es.asfhy.idesk.manager.fxui.ManagerMainFX;
import es.asfhy.idesk.manager.objects.MainConfigFile;
import javafx.application.Application;

public class ManagerMain {
	public static final File			ideskrc		= new File(System.getProperty("user.home"), ".ideskrc");
	public static final File			ideskFolder	= new File(System.getProperty("user.home"), ".idesktop");
	public static final double			iconSize	= 1.75 * new Canvas().getFontMetrics(new JLabel().getFont()).getHeight();
	public static final String			families[]	= GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();
	public static final Integer			sizes[]		= new Integer[750];
	public static final ResourceBundle	bundle;
	static {
		ResourceBundle aux = null;
		try {
			aux = ResourceBundle.getBundle("es.asfhy.idesk.manager.rsrc.langs.Strings");
		} catch (Exception e) {
			aux = ResourceBundle.getBundle("es.asfhy.idesk.manager.rsrc.langs.Strings", new Locale("en", "US"));
		}
		bundle = aux;
		for (int x = 0; x < sizes.length; x++)
			sizes[x] = x + 4;
	}
	
	private static final String format(java.awt.Color color) {
		return String.format("rgba(%d, %d, %d, %d)", color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha());
	}
	
	public static final javafx.scene.paint.Color cast(java.awt.Color color) {
		final double div = 255d;
		double r = color.getRed() / div;
		double g = color.getGreen() / div;
		double b = color.getBlue() / div;
		double o = color.getAlpha() / div;
		return new javafx.scene.paint.Color(r, g, b, o);
	}
	
	public static final java.awt.Color cast(javafx.scene.paint.Color color) {
		final double mult = 255d;
		int r = (int) Math.floor(mult * color.getRed());
		int g = (int) Math.floor(mult * color.getGreen());
		int b = (int) Math.floor(mult * color.getBlue());
		int a = (int) Math.floor(mult * color.getOpacity());
		return new java.awt.Color(r, g, b, a);
	}
	
	public static List<File> getDesktopIcons() {
		ArrayList<File> out = null;
		if (!ideskFolder.exists())
			ideskFolder.mkdirs();
		if (ideskFolder.exists() && ideskFolder.canRead()) {
			out = new ArrayList<File>();
			File files[] = ideskFolder.listFiles();
			for (File f : files) {
				if (f != null && f.getName() != null && f.getName().toLowerCase().endsWith(".lnk"))
					out.add(f);
			}
		}
		return out;
	}
	
	public static final String getFontStyle(Color fore, Color back, String family, double size, boolean bold) {
		//
		String fg = format(fore);
		String bg = format(back);
		//
		StringBuilder style = new StringBuilder();
		style.append("-fx-font-family: '").append(family).append("'; ");
		style.append("-fx-font-size: ").append(size).append("pt; ");
		style.append("-fx-text-fill: ").append(fg).append("; ");
		style.append("-fx-font-weight: ");
		if (!bold)
			style.append("normal");
		else
			style.append("bold");
		style.append("; ");
		style.append("-fx-border-style: solid; ");
		style.append("-fx-border-width: 1pt; ");
		style.append("-fx-border-color: ").append(fg).append("; ");
		style.append("-fx-background-color: ").append(bg).append("; ");
		return style.toString();
	}
	
	public static void main(String args[]) {
		if (!ideskrc.exists())
			try {
				new MainConfigFile().save();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		Locale.setDefault(bundle.getLocale());
		Application.launch(ManagerMainFX.class, args);
	}
}
