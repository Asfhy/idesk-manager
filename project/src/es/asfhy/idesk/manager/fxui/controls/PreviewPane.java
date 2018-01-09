package es.asfhy.idesk.manager.fxui.controls;

import java.io.File;

import es.asfhy.idesk.manager.ManagerMain;
import es.asfhy.idesk.manager.fxui.ManagerMainFX;
import es.asfhy.idesk.manager.objects.DesktopIcon;
import es.asfhy.idesk.manager.tables.Config;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.TextAlignment;
import javafx.stage.Screen;

public class PreviewPane extends BorderPane {
	private final ManagerMainFX			parent;
	private final Config				cfg;
	private final Canvas				canvas;
	public final ChangeListener<Number>	updateSize	= new ChangeListener<Number>() {
														@Override
														public void changed(ObservableValue<? extends Number> arg0, Number arg1, Number arg2) {
															Rectangle2D rec = Screen.getPrimary().getVisualBounds();
															Bounds b = getBoundsInLocal();
															if (getParent() != null)
																b = getParent().getBoundsInLocal();
															double mul = Math.min(b.getWidth() / rec.getWidth(), b.getHeight() / rec.getHeight());
															canvas.setWidth(mul * rec.getWidth());
															canvas.setHeight(mul * rec.getHeight());
															updateView();
														}
													};
	
	public PreviewPane(ManagerMainFX main) {
		super();
		canvas = new Canvas();
		canvas.setCache(false);
		parent = main;
		cfg = parent.cfg.getConfigTable();
		setCenter(canvas);
		widthProperty().addListener(updateSize);
		heightProperty().addListener(updateSize);
		updateSize.changed(null, null, null);
	}
	
	public synchronized void updateView() {
		Rectangle2D rec = Screen.getPrimary().getVisualBounds();
		double div = Math.max(rec.getWidth() / getWidth(), rec.getHeight() / getHeight());
		if (div == Double.POSITIVE_INFINITY)
			return;
		Image img = null;
		if (cfg.getBackgroundSource() != null && cfg.getBackgroundSource().trim().length() != 0 && cfg.getBackgroundDelay() > 0) {
			File src = new File(cfg.getBackgroundSource());
			if (src.exists() && src.isDirectory() && src.canRead()) {
				File files[] = src.listFiles();
				for (File f : files) {
					String fn = f.getName().toLowerCase();
					if (fn.endsWith(".jpg") || fn.endsWith(".jpeg") || fn.endsWith(".png") || fn.endsWith(".gif")) {
						try {
							img = new Image(f.getAbsoluteFile().toURI().toString());
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
					img = new Image(f.getAbsoluteFile().toURI().toString());
				} catch (Exception e) {
				}
			}
		}
		GraphicsContext g = canvas.getGraphicsContext2D();
		g.clearRect(-5, -5, canvas.getWidth() + 10, canvas.getHeight() + 10);
		g.setFill(ManagerMain.cast(cfg.getBackgroundColor()));
		g.fillRect(-5, -5, canvas.getWidth() + 10, canvas.getHeight() + 10);
		double x = 0, y = 0, w = canvas.getWidth(), h = canvas.getHeight();
		if (img != null) {
			switch (cfg.getBackgroundMode()) {
				case Center:
					w = img.getWidth() / div;
					h = img.getHeight() / div;
					x = (canvas.getWidth() - w) / 2d;
					y = (canvas.getHeight() - h) / 2d;
					g.drawImage(img, x, y, w, h);
					break;
				case Fit:
					double aux = Math.min(canvas.getWidth() / img.getWidth(), canvas.getHeight() / img.getHeight());
					w = img.getWidth() * aux;
					h = img.getHeight() * aux;
					x = (canvas.getWidth() - w) / 2d;
					y = (canvas.getHeight() - h) / 2d;
					g.drawImage(img, x, y, w, h);
					break;
				case Mirror:
					w = img.getWidth() / div;
					h = img.getHeight() / div;
					int imw = (int) w * 2;
					int imh = (int) h * 2;
					Canvas cnv = new Canvas(imw, imh);
					GraphicsContext g2 = cnv.getGraphicsContext2D();
					g2.drawImage(img, 0, 0, w, h);
					g2.drawImage(img, cnv.getWidth(), 0, -w, h);
					g2.drawImage(img, 0, cnv.getHeight(), w, -h);
					g2.drawImage(img, cnv.getWidth(), cnv.getHeight(), -w, -h);
					ImagePattern ip1 = new ImagePattern(cnv.snapshot(null, null), 0, 0, imw, imh, false);
					g.setFill(ip1);
					g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
					break;
				case Scale:
					g.drawImage(img, x, y, w, h);
					break;
				case Stretch:
				default:
					w = img.getWidth() / div;
					h = img.getHeight() / div;
					ImagePattern ip = new ImagePattern(img, 0, 0, w, h, false);
					g.setFill(ip);
					g.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
					break;
			}
		}
		//
		for (DesktopIcon di : parent.icons) {
			Image ico = null;
			try {
				// Draw Desktop Icon Image:
				ico = new Image("file://" + di.getIcon());
				x = di.getX() == null ? 0 : di.getX() / div;
				y = di.getY() == null ? 0 : di.getY() / div;
				w = di.getWidth() == null ? ico.getWidth() / div : di.getWidth() / div;
				h = di.getHeight() == null ? ico.getHeight() / div : di.getHeight() / div;
				g.save();
				g.setGlobalAlpha(1d - (cfg.getTransparency() / 255d));
				g.drawImage(ico, x, y, w, h);
				g.restore();
				// Draw Desktop Icon Name, if it's enabled.
				if (!cfg.isCaptionOnHover()) {
					// Create the Font Object:
					Font fnt = Font.font(cfg.getFontName(), cfg.isFontBold() ? FontWeight.BOLD : FontWeight.NORMAL, (Screen.getPrimary().getDpi() / 72d) * (cfg.getFontSize() / div));
					// Use the Location Setting to Find text Position and Alignment:
					double tx = 0, ty = 0;
					TextAlignment ta = null;
					VPos tb = null;
					switch (cfg.getCaptionPlacement()) {
						case Bottom:
							tx = (x + w / 2d);
							ty = y + h;
							ta = TextAlignment.CENTER;
							tb = VPos.TOP;
							break;
						case Left:
							tx = x;
							ty = (y + h / 2d);
							ta = TextAlignment.RIGHT;
							tb = VPos.CENTER;
							break;
						case Right:
							tx = x + w;
							ty = (y + h / 2d);
							ta = TextAlignment.LEFT;
							tb = VPos.CENTER;
							break;
						case Top:
							tx = (x + w / 2d);
							ty = y;
							ta = TextAlignment.CENTER;
							tb = VPos.BOTTOM;
							break;
						default:
							break;
					}
					// Draw the Text where it should be:
					g.setFont(fnt);
					g.setTextBaseline(tb);
					g.setTextAlign(ta);
					if (cfg.isUseShadow()) {
						double x2 = tx + cfg.getShadowX() / div;
						double y2 = ty + cfg.getShadowY() / div;
						g.setFill(ManagerMain.cast(cfg.getShadowColor()));
						g.fillText(di.getCaption(), x2, y2);
					}
					g.setFill(ManagerMain.cast(cfg.getFontColor()));
					g.fillText(di.getCaption(), tx, ty);
				}
			} catch (Exception e) {
			}
		}
		g.setStroke(Color.BLACK);
		g.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());
	}
}
