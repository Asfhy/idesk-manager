package es.asfhy.idesk.manager.fxui.controls;

import java.io.File;
import java.io.FileInputStream;

import es.asfhy.idesk.manager.ManagerMain;
import es.asfhy.idesk.manager.fxui.ManagerMainFX;
import es.asfhy.idesk.manager.objects.DesktopIcon;
import es.asfhy.idesk.manager.tables.Config;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class PreviewPane extends BorderPane {
	private final ManagerMainFX	parent;
	private final Config		cfg;
	private final Canvas		canvas;
	
	public PreviewPane(ManagerMainFX main) {
		super();
		setCenter(canvas = new Canvas(1920 / 2, 1200 / 2));
		parent = main;
		cfg = parent.cfg.getConfigTable();
		updateView();
	}
	
	private synchronized void updateView() {
		Image img = null;
		if (cfg.getBackgroundSource() != null && cfg.getBackgroundSource().trim().length() != 0 && cfg.getBackgroundDelay() > 0) {
			File src = new File(cfg.getBackgroundSource());
			if (src.exists() && src.isDirectory() && src.canRead()) {
				File files[] = src.listFiles();
				for (File f : files) {
					String fn = f.getName().toLowerCase();
					if (fn.endsWith(".jpg") || fn.endsWith(".jpeg") || fn.endsWith(".png") || fn.endsWith(".gif")) {
						try {
							img = new Image(new FileInputStream(f));
							break;
						} catch (Exception e) {
						}
					}
				}
			}
		} else if (cfg.getBackgroundFile() != null && cfg.getBackgroundFile().trim().length() != 0) {
			File f = new File(cfg.getBackgroundFile().trim());
			String fn = f.getName().toLowerCase();
			if (fn.endsWith(".jpg") || fn.endsWith(".jpeg") || fn.endsWith(".png") || fn.endsWith(".gif")) {
				try {
					img = new Image(new FileInputStream(f));
				} catch (Exception e) {
				}
			}
		}
		if (img != null) {
			GraphicsContext g = canvas.getGraphicsContext2D();
			g.clearRect(-5, -5, canvas.getWidth() + 10, canvas.getHeight() + 10);
			double x = 0, y = 0, w = canvas.getWidth(), h = canvas.getHeight();
			switch (cfg.getBackgroundMode()) {
				case Center:
					w = img.getWidth() / 2;
					h = img.getHeight() / 2;
					x = (canvas.getWidth() - w) / 2d;
					y = (canvas.getHeight() - h) / 2d;
					break;
				case Fit:
					double aux = Math.min(canvas.getWidth() / img.getWidth(), canvas.getHeight() / img.getHeight());
					w = img.getWidth() * aux;
					h = img.getHeight() * aux;
					x = (canvas.getWidth() - w) / 2d;
					y = (canvas.getHeight() - h) / 2d;
					break;
				case Mirror:
					break;
				case Scale:
					break;
				case Stretch:
					// Nothing to do, current values are for Stretch Mode, as it the default value.
					break;
				default:
					// Nothing to do, current values are for Stretch Mode, as it the default value.
					break;
			}
			g.drawImage(img, x, y, w, h);
			//
			for (DesktopIcon di : parent.icons) {
				Image ico = null;
				try {
					// Dibujar el Icono:
					ico = new Image("file://" + di.getIcon());
					x = di.getX() == null ? 0 : di.getX() / 2d;
					y = di.getY() == null ? 0 : di.getY() / 2d;
					w = di.getWidth() == null ? ico.getWidth() / 2d : di.getWidth() / 2d;
					h = di.getHeight() == null ? ico.getHeight() / 2d : di.getHeight() / 2d;
					g.drawImage(ico, x, y, w, h);
					// Dibujar el Titlo del Icono, si por defecto se muestran:
					if (!cfg.isCaptionOnHover()) {
						// Creamos la fuente:
						Font fnt = Font.font(cfg.getFontName(), cfg.isFontBold() ? FontWeight.BOLD : FontWeight.NORMAL, cfg.getFontSize());
						// Calculamos el tamaño del texto:
						
						// Usamos la configuración para calcular la posición:
						switch (cfg.getCaptionPlacement()) {
							case Bottom:
								break;
							case Left:
								break;
							case Right:
								break;
							case Top:
								break;
							default:
								break;
						}
						//
						g.setFont(fnt);
						g.setFill(ManagerMain.cast(cfg.getFontColor()));
						g.fillText(di.getCaption(), x, y);
					}
				} catch (Exception e) {
				}
			}
			// TODO: Utilizar la imagen cargada para configurar una vista previa del escritorio.
			g.setStroke(Color.BLACK);
			g.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
		}
	}
}
