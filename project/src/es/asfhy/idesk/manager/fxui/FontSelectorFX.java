package es.asfhy.idesk.manager.fxui;

import java.awt.Color;

import es.asfhy.idesk.manager.ManagerMain;
import es.asfhy.idesk.manager.fxui.utils.FontConfig;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

public class FontSelectorFX extends Dialog<FontConfig> {
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
		setDialogPane(new DialogPane() {
			{
				prefWidthProperty().bind(FontSelectorFX.this.widthProperty());
				preview.prefWidthProperty().bind(widthProperty());
				ComboBox<String> fntName = new ComboBox<String>(FXCollections.observableArrayList(ManagerMain.families));
				fntName.setValue(fontName);
				fntName.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						config.setFontName(fntName.getValue());
						updatePreview();
					}
				});
				ComboBox<Integer> fntSize = new ComboBox<Integer>(FXCollections.observableArrayList(ManagerMain.sizes));
				fntSize.setValue(fontSize);
				fntSize.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent arg0) {
						config.setFontSize(fntSize.getValue());
						updatePreview();
					}
				});
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
				Button ok = new Button(ManagerMain.bundle.getString("fontsel.accept"));
				ok.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent evt) {
						setResult(config);
						close();
					}
				});
				Button cancel = new Button(ManagerMain.bundle.getString("fontsel.cancel"));
				cancel.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent evt) {
						setResult(originalCfg);
						close();
					}
				});
				VBox panel = new VBox(3, preview, new Label(ManagerMain.bundle.getString("fontsel.family")), fntName, new Label(ManagerMain.bundle.getString("fontsel.size")), fntSize, new Label(ManagerMain.bundle.getString("fontsel.fore")), fgColPick, new Label(ManagerMain.bundle.getString("fontsel.back")), bgColPick, ok, cancel);
				panel.layout();
				for (Node n : panel.getChildren())
					if (Region.class.isInstance(n))
						Region.class.cast(n).setMinWidth(700);
				getChildren().add(panel);
				// setPrefHeight(350);
				prefHeightProperty().bind(panel.heightProperty());
				//
				layout();
				requestLayout();
			}
		});
		updatePreview();
	}
	
	public FontSelectorFX setTile(String title) {
		super.setTitle(title);
		return this;
	}
}
