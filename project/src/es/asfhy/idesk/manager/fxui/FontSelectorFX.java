package es.asfhy.idesk.manager.fxui;

import java.awt.Color;

import es.asfhy.idesk.manager.ManagerMain;
import es.asfhy.idesk.manager.fxui.utils.FontConfig;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class FontSelectorFX extends Dialog<FontConfig> {
	private static HBox hbox(String lbl, Control control) {
		Label l = new Label(lbl);
		l.setPrefWidth(Label.USE_COMPUTED_SIZE);
		l.setMinWidth(Label.USE_COMPUTED_SIZE);
		l.setMaxWidth(Label.USE_COMPUTED_SIZE);
		HBox.setHgrow(l, Priority.NEVER);
		//
		control.setMinWidth(Control.USE_COMPUTED_SIZE);
		control.setPrefWidth(Control.USE_COMPUTED_SIZE);
		control.setMaxWidth(Double.POSITIVE_INFINITY);
		HBox.setHgrow(control, Priority.ALWAYS);
		//
		HBox out = new HBox(3, l, control);
		out.setAlignment(Pos.CENTER_LEFT);
		return out;
	}
	
	private final FontConfig	config;
	private final FontConfig	originalCfg;
	private final boolean		bold;
	private final Label			preview	= new Label(ManagerMain.bundle.getString("fontsel.preview"));
	
	private synchronized void updatePreview() {
		preview.setStyle(ManagerMain.getFontStyle(config.getForeColor(), config.getBackColor(), config.getFontName(), config.getFontSize(), bold));
		preview.getParent().requestLayout();
		preview.getParent().layout();
		preview.getScene().getWindow().sizeToScene();
	}
	
	public FontSelectorFX(String caption, String fontName, boolean bold, Integer fontSize, Color fgColor, Color bgColor) {
		super();
		//
		setTile(caption);
		setResizable(false);
		setResultConverter(new Callback<ButtonType, FontConfig>() {
			@Override
			public FontConfig call(ButtonType type) {
				FontConfig out = originalCfg;
				if (type != null && type.getButtonData() == ButtonData.OK_DONE)
					out = config;
				return out;
			}
		});
		//
		preview.setAlignment(Pos.CENTER);
		preview.setPadding(new Insets(5));
		//
		this.bold = bold;
		//
		originalCfg = new FontConfig();
		originalCfg.setFontName(fontName);
		originalCfg.setFontSize(fontSize);
		originalCfg.setForeColor(fgColor);
		originalCfg.setBackColor(bgColor);
		//
		config = new FontConfig();
		config.setFontName(fontName);
		config.setFontSize(fontSize);
		config.setForeColor(fgColor);
		config.setBackColor(bgColor);
		//
		VBox rows = new VBox(3);
		rows.setFillWidth(true);
		rows.setMinWidth(700);
		rows.setPrefWidth(700);
		rows.setMaxWidth(700);
		//
		VBox head = new VBox();
		head.setFillWidth(true);
		head.setMinWidth(700);
		head.setPrefWidth(700);
		head.setMaxWidth(700);
		//
		preview.prefWidthProperty().bind(rows.widthProperty());
		//
		ComboBox<String> fntName = new ComboBox<String>(FXCollections.observableArrayList(ManagerMain.families));
		fntName.setValue(fontName);
		fntName.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				config.setFontName(fntName.getValue());
				updatePreview();
			}
		});
		//
		ComboBox<Integer> fntSize = new ComboBox<Integer>(FXCollections.observableArrayList(ManagerMain.sizes));
		fntSize.setValue(fontSize);
		fntSize.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				config.setFontSize(fntSize.getValue());
				updatePreview();
			}
		});
		//
		ColorPicker fgColPick = new ColorPicker(ManagerMain.cast(fgColor));
		fgColPick.getStyleClass().add("button");
		fgColPick.setStyle("-fx-color-rect-width: 1.5em; -fx-color-rect-height: 1.5em;-fx-min-height: 2.25em;");
		fgColPick.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				config.setForeColor(ManagerMain.cast(fgColPick.getValue()));
				updatePreview();
			}
		});
		//
		ColorPicker bgColPick = new ColorPicker(ManagerMain.cast(bgColor));
		bgColPick.getStyleClass().add("button");
		bgColPick.setStyle("-fx-color-rect-width: 1.5em; -fx-color-rect-height: 1.5em;-fx-min-height: 2.25em;");
		bgColPick.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				config.setBackColor(ManagerMain.cast(bgColPick.getValue()));
				updatePreview();
			}
		});
		//
		Button ok = new Button(ManagerMain.bundle.getString("fontsel.accept"));
		ok.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				setResult(config);
				close();
			}
		});
		ok.setMaxWidth(Double.POSITIVE_INFINITY);
		HBox.setHgrow(ok, Priority.ALWAYS);
		//
		Button cancel = new Button(ManagerMain.bundle.getString("fontsel.cancel"));
		cancel.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				setResult(originalCfg);
				close();
			}
		});
		cancel.setMaxWidth(Double.POSITIVE_INFINITY);
		HBox.setHgrow(cancel, Priority.ALWAYS);
		//
		head.getChildren().add(preview);
		//
		rows.getChildren().addAll(hbox(ManagerMain.bundle.getString("fontsel.family"), fntName), hbox(ManagerMain.bundle.getString("fontsel.size"), fntSize));
		rows.getChildren().addAll(hbox(ManagerMain.bundle.getString("fontsel.fore"), fgColPick), hbox(ManagerMain.bundle.getString("fontsel.back"), bgColPick));
		rows.getChildren().addAll(new Separator(Orientation.HORIZONTAL), ok, cancel);
		//
		getDialogPane().setHeader(head);
		getDialogPane().setContent(rows);
		//
		rows.requestLayout();
		getDialogPane().autosize();
		updatePreview();
	}
	
	public FontSelectorFX setTile(String title) {
		super.setTitle(title);
		return this;
	}
}
