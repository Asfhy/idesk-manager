package es.asfhy.idesk.manager.rsrc.langs;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Strings {
	private static final ResourceBundle	english	= ResourceBundle.getBundle(Strings.class.getCanonicalName(), new Locale("en"));
	private static final ResourceBundle	main;
	static {
		ResourceBundle rb = null;
		try {
			rb = ResourceBundle.getBundle(Strings.class.getCanonicalName());
		} catch (Exception e) {
			rb = english;
		}
		main = rb;
	}
	
	public static Locale getLocale() {
		Locale out = new Locale("en", "US");
		if (main != null)
			out = main.getLocale();
		else if (english != null)
			out = main.getLocale();
		return out;
	}
	
	public static String getString(String key, String def) {
		String out = null;
		if (main != null) {
			try {
				out = main.getString(key);
			} catch (MissingResourceException e) {
				out = null;
			}
		}
		if (out == null) {
			try {
				out = english.getString(key);
			} catch (MissingResourceException e2) {
				e2.printStackTrace();
				out = def;
			}
		}
		return out;
	}
}
