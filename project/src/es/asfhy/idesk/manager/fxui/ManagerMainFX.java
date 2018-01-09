package es.asfhy.idesk.manager.fxui;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import es.asfhy.idesk.manager.ManagerMain;
import es.asfhy.idesk.manager.enums.BackgroundMode;
import es.asfhy.idesk.manager.enums.CursorOver;
import es.asfhy.idesk.manager.enums.FillStyle;
import es.asfhy.idesk.manager.enums.Placement;
import es.asfhy.idesk.manager.enums.SnapOrigin;
import es.asfhy.idesk.manager.fxui.controls.DeskIconPane;
import es.asfhy.idesk.manager.fxui.controls.PreviewPane;
import es.asfhy.idesk.manager.fxui.utils.FontConfig;
import es.asfhy.idesk.manager.objects.DesktopIcon;
import es.asfhy.idesk.manager.objects.MainConfigFile;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TitledPane;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ManagerMainFX extends Application {
	public MainConfigFile					cfg;
	public final LinkedList<DesktopIcon>	icons			= new LinkedList<DesktopIcon>();
	private Label							deskFntCol		= new Label(ManagerMain.bundle.getString("main.desktop.preview"));
	private Label							toolFntCol		= new Label(ManagerMain.bundle.getString("main.tooltip.preview"));
	private final TabPane					tabs			= new TabPane();
	public PreviewPane						preview;
	private final DirectoryChooser			sourceChooser	= new DirectoryChooser();
	private final FileChooser				fileChooser		= new FileChooser();
	
	private synchronized void updateDeskFntCol() {
		String style = ManagerMain.getFontStyle(cfg.getConfigTable().getFontColor(), cfg.getConfigTable().getBackgroundColor(), cfg.getConfigTable().getFontName(), cfg.getConfigTable().getFontSize(), cfg.getConfigTable().isFontBold());
		deskFntCol.setStyle(style.toString());
	}
	
	private synchronized void updateToolFntCol() {
		String style = ManagerMain.getFontStyle(cfg.getConfigTable().getTooltipForeColor(), cfg.getConfigTable().getTooltipBackColor(), cfg.getConfigTable().getTooltipFontName(), cfg.getConfigTable().getTooltipFontSize(), false);
		toolFntCol.setStyle(style);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		//
		cfg = new MainConfigFile();
		//
		List<File> files = ManagerMain.getDesktopIcons();
		if (files != null && files.size() > 0)
			for (File f : files)
				icons.add(new DesktopIcon(f.getName()));
		//
		BorderPane root = new BorderPane();
		//
		Scene scene = new Scene(root, 1024, 768);
		//
		preview = new PreviewPane(this);
		Tab tab0 = new Tab(ManagerMain.bundle.getString("main.tabs.preview"), preview);
		tab0.setClosable(false);
		root.widthProperty().addListener(preview.updateSize);
		root.heightProperty().addListener(preview.updateSize);
		tabs.getTabs().add(tab0);
		//
		Tab tab1 = new Tab(ManagerMain.bundle.getString("main.tabs.config"), createDeskPanel());
		tab1.setClosable(false);
		tabs.getTabs().add(tab1);
		//
		Tab tab2 = new Tab(ManagerMain.bundle.getString("main.tabs.actions"));
		tab2.setClosable(false);
		tabs.getTabs().add(tab2);
		//
		for (DesktopIcon di : icons) {
			Tab t = new Tab(di.getCaption());
			t.setContent(new DeskIconPane(this, t, di));
			t.setClosable(false);
			tabs.getTabs().add(t);
		}
		root.setCenter(tabs);
		root.setTop(createToolbar());
		//
		updateDeskFntCol();
		updateToolFntCol();
		//
		stage.setTitle(ManagerMain.bundle.getString("main.title"));
		//
		stage.setScene(scene);
		stage.show();
		stage.requestFocus();
	}
	
	private Node createDeskPanel() {
		//
		VBox deskPane = new VBox(3, createTooltipsPanel(), createDesktopPanel(), createSnapgridPanel(), createShadowPanel(), createBakcgroundPanel());
		deskPane.setPadding(new Insets(10));
		deskFntCol.prefWidthProperty().bind(deskPane.widthProperty());
		toolFntCol.prefWidthProperty().bind(deskPane.widthProperty());
		//
		return deskPane;
	}
	
	private Node createSnapgridPanel() {
		VBox content = new VBox(3);
		content.setFillWidth(true);
		TitledPane out = new TitledPane(ManagerMain.bundle.getString("main.groups.grid"), content);
		out.setExpanded(false);
		//
		CheckBox gridEnabled = new CheckBox(ManagerMain.bundle.getString("main.grid.iconSnap"));
		gridEnabled.setSelected(cfg.getConfigTable().isIconSnap());
		gridEnabled.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				cfg.getConfigTable().setIconSnap(gridEnabled.isSelected());
			}
		});
		content.getChildren().add(gridEnabled);
		//
		ComboBox<SnapOrigin> snapOrigin = new ComboBox<SnapOrigin>(FXCollections.observableArrayList(SnapOrigin.values()));
		snapOrigin.setValue(cfg.getConfigTable().getSnapOrigin());
		snapOrigin.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				cfg.getConfigTable().setSnapOrigin(snapOrigin.getValue());
			}
		});
		content.getChildren().add(hbox(new Label(ManagerMain.bundle.getString("main.grid.snapSource")), snapOrigin));
		//
		Spinner<Integer> snapWidth = new Spinner<Integer>(1, 1000, cfg.getConfigTable().getSnapWidth());
		snapWidth.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer new_val) {
				cfg.getConfigTable().setSnapWidth(new_val);
			}
		});
		content.getChildren().add(hbox(new Label(ManagerMain.bundle.getString("main.grid.snapWidth")), snapWidth));
		//
		Spinner<Integer> snapHeight = new Spinner<Integer>(1, 1000, cfg.getConfigTable().getSnapHeight());
		snapHeight.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer new_val) {
				cfg.getConfigTable().setSnapHeight(new_val);
			}
		});
		content.getChildren().add(hbox(new Label(ManagerMain.bundle.getString("main.grid.snapHeight")), snapHeight));
		//
		CheckBox gridShadow = new CheckBox(ManagerMain.bundle.getString("main.grid.snapShadow"));
		gridShadow.setSelected(cfg.getConfigTable().isSnapShadow());
		gridShadow.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				cfg.getConfigTable().setSnapShadow(gridShadow.isSelected());
			}
		});
		content.getChildren().add(gridShadow);
		//
		Spinner<Integer> snapShadowTrans = new Spinner<Integer>(0, 255, cfg.getConfigTable().getSnapShadowTrans());
		snapShadowTrans.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer new_val) {
				cfg.getConfigTable().setSnapShadowTrans(new_val);
			}
		});
		content.getChildren().add(hbox(new Label(ManagerMain.bundle.getString("main.grid.snapShadowTrans")), snapShadowTrans));
		//
		return out;
	}
	
	private Node createDesktopPanel() {
		VBox content = new VBox(3);
		TitledPane out = new TitledPane(ManagerMain.bundle.getString("main.groups.desktop"), content);
		out.setExpanded(false);
		//
		CheckBox lock = new CheckBox(ManagerMain.bundle.getString("main.desktop.locked"));
		lock.setSelected(cfg.getConfigTable().isLocked());
		lock.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				cfg.getConfigTable().setLocked(lock.isSelected());
			}
		});
		content.getChildren().add(lock);
		//
		ComboBox<CursorOver> cursorOver = new ComboBox<CursorOver>(FXCollections.observableArrayList(CursorOver.values()));
		cursorOver.setValue(cfg.getConfigTable().getCursorOver());
		cursorOver.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				cfg.getConfigTable().setCursorOver(cursorOver.getValue());
			}
		});
		content.getChildren().add(hbox(new Label(ManagerMain.bundle.getString("main.desktop.cursor")), cursorOver));
		//
		ComboBox<FillStyle> fillStyle = new ComboBox<FillStyle>(FXCollections.observableArrayList(FillStyle.values()));
		fillStyle.setValue(cfg.getConfigTable().getFillStyle());
		fillStyle.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				cfg.getConfigTable().setFillStyle(fillStyle.getValue());
			}
		});
		content.getChildren().add(hbox(new Label(ManagerMain.bundle.getString("main.desktop.fill")), fillStyle));
		//
		Spinner<Integer> delay = new Spinner<Integer>(50, 1000, cfg.getConfigTable().getClickDelay());
		delay.valueProperty().addListener(new ChangeListener<Integer>() {
			public void changed(ObservableValue<? extends Integer> val, Integer old_val, Integer new_val) {
				cfg.getConfigTable().setClickDelay(new_val);
			}
		});
		content.getChildren().add(hbox(new Label(ManagerMain.bundle.getString("main.desktop.delay")), delay));
		//
		CheckBox captionOnHover = new CheckBox(ManagerMain.bundle.getString("main.desktop.captionOnHover"));
		captionOnHover.setSelected(cfg.getConfigTable().isCaptionOnHover());
		captionOnHover.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				cfg.getConfigTable().setCaptionOnHover(captionOnHover.isSelected());
				preview.updateView();
			}
		});
		content.getChildren().add(captionOnHover);
		//
		ComboBox<Placement> captionPlacement = new ComboBox<Placement>(FXCollections.observableArrayList(Placement.values()));
		captionPlacement.setValue(cfg.getConfigTable().getCaptionPlacement());
		captionPlacement.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				cfg.getConfigTable().setCaptionPlacement(captionPlacement.getValue());
				preview.updateView();
			}
		});
		content.getChildren().add(hbox(new Label(ManagerMain.bundle.getString("main.desktop.captionPos")), captionPlacement));
		//
		Spinner<Integer> transparency = new Spinner<Integer>(0, 255, cfg.getConfigTable().getTransparency());
		transparency.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> val, Integer old_val, Integer new_val) {
				cfg.getConfigTable().setTransparency(new_val);
				preview.updateView();
			}
		});
		content.getChildren().add(hbox(new Label(ManagerMain.bundle.getString("main.desktop.transparency")), transparency));
		//
		CheckBox bold = new CheckBox(ManagerMain.bundle.getString("main.desktop.bold"));
		bold.setSelected(cfg.getConfigTable().isFontBold());
		bold.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				cfg.getConfigTable().setFontBold(bold.isSelected());
				updateDeskFntCol();
				preview.updateView();
			}
		});
		content.getChildren().add(bold);
		//
		deskFntCol.setTooltip(new Tooltip(ManagerMain.bundle.getString("main.desktop.preview.tooltip")));
		deskFntCol.getTooltip().setFont(Font.getDefault());
		deskFntCol.setPadding(new Insets(5));
		deskFntCol.setAlignment(Pos.CENTER);
		deskFntCol.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent evt) {
				if (evt.getButton() == MouseButton.PRIMARY && evt.getClickCount() == 1) {
					Optional<FontConfig> result = new FontSelectorFX(ManagerMain.bundle.getString("fontsel.caption.desktop"), cfg.getConfigTable().getFontName(), cfg.getConfigTable().isFontBold(), cfg.getConfigTable().getFontSize(), cfg.getConfigTable().getFontColor(), cfg.getConfigTable().getBackgroundColor()).showAndWait();
					if (result != null && result.isPresent()) {
						FontConfig val = result.get();
						cfg.getConfigTable().setFontName(val.getFontName());
						cfg.getConfigTable().setFontSize((int) Math.round(val.getFontSize()));
						cfg.getConfigTable().setBackgroundColor(val.getBackColor());
						cfg.getConfigTable().setFontColor(val.getForeColor());
						updateDeskFntCol();
						preview.updateView();
					}
				}
			}
		});
		content.getChildren().add(deskFntCol);
		//
		return out;
	}
	
	private Node createTooltipsPanel() {
		VBox content = new VBox(3);
		TitledPane out = new TitledPane(ManagerMain.bundle.getString("main.groups.tooltip"), content);
		out.setExpanded(false);
		//
		CheckBox tooltips = new CheckBox(ManagerMain.bundle.getString("main.tooltip.enabled"));
		tooltips.setSelected(cfg.getConfigTable().isTooltipCaptionOnHover());
		tooltips.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				cfg.getConfigTable().setTooltipCaptionOnHover(tooltips.isSelected());
			}
		});
		content.getChildren().add(tooltips);
		//
		ComboBox<Placement> tooltipsPlacement = new ComboBox<Placement>(FXCollections.observableArrayList(Placement.values()));
		tooltipsPlacement.setValue(cfg.getConfigTable().getTooltipCaptionPlacement());
		tooltipsPlacement.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				cfg.getConfigTable().setTooltipCaptionPlacement(tooltipsPlacement.getValue());
			}
		});
		content.getChildren().add(hbox(new Label(ManagerMain.bundle.getString("main.tooltip.captionPos")), tooltipsPlacement));
		//
		toolFntCol.setTooltip(new Tooltip(ManagerMain.bundle.getString("main.tooltip.preview.tooltip")));
		toolFntCol.getTooltip().setFont(Font.getDefault());
		toolFntCol.setPadding(new Insets(5));
		toolFntCol.setAlignment(Pos.CENTER);
		toolFntCol.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent evt) {
				if (evt.getButton() == MouseButton.PRIMARY && evt.getClickCount() == 1) {
					Optional<FontConfig> result = new FontSelectorFX(ManagerMain.bundle.getString("fontsel.caption.tooltip"), cfg.getConfigTable().getTooltipFontName(), false, cfg.getConfigTable().getTooltipFontSize(), cfg.getConfigTable().getTooltipForeColor(), cfg.getConfigTable().getTooltipBackColor()).showAndWait();
					if (result != null && result.isPresent()) {
						FontConfig val = result.get();
						cfg.getConfigTable().setTooltipFontName(val.getFontName());
						cfg.getConfigTable().setTooltipFontSize((int) Math.round(val.getFontSize()));
						cfg.getConfigTable().setTooltipBackColor(val.getBackColor());
						cfg.getConfigTable().setTooltipForeColor(val.getForeColor());
						updateToolFntCol();
					}
				}
			}
		});
		content.getChildren().add(toolFntCol);
		//
		return out;
	}
	
	private Node createToolbar() {
		ToolBar tb = new ToolBar();
		//
		Button save = new Button();
		save.setTooltip(new Tooltip(ManagerMain.bundle.getString("main.toolbar.save")));
		save.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/es/asfhy/idesk/manager/rsrc/save.png"), ManagerMain.iconSize, ManagerMain.iconSize, true, true)));
		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				try {
					cfg.save();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		tb.getItems().add(save);
		//
		tb.getItems().add(new Separator(Orientation.VERTICAL));
		//
		Button newDesktopIcon = new Button();
		newDesktopIcon.setTooltip(new Tooltip(ManagerMain.bundle.getString("main.toolbar.newIcon")));
		newDesktopIcon.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/es/asfhy/idesk/manager/rsrc/add.png"), ManagerMain.iconSize, ManagerMain.iconSize, true, true)));
		newDesktopIcon.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent evt) {
				TextInputDialog tiDiag = new TextInputDialog(String.format("desktop-icon-%d.lnk", System.currentTimeMillis()));
				tiDiag.setTitle(ManagerMain.bundle.getString("main.toolbar.newIcon.dialogCaption"));
				tiDiag.setHeaderText(ManagerMain.bundle.getString("main.toolbar.newIcon.dialogHeader"));
				Optional<String> result = tiDiag.showAndWait();
				if (result != null && result.isPresent()) {
					StringBuilder fn = new StringBuilder(result.get());
					if (!fn.toString().toLowerCase().endsWith(".lnk"))
						fn.append(".lnk");
					Tab tab = new Tab(fn.toString());
					tab.setClosable(false);
					DeskIconPane dip = new DeskIconPane(ManagerMainFX.this, tab, fn.toString());
					tab.setContent(dip);
					icons.add(dip.getIcon());
					tabs.getTabs().add(tab);
					tabs.getSelectionModel().select(tab);
				}
			}
		});
		tb.getItems().add(newDesktopIcon);
		//
		return tb;
	}
	
	private Node createShadowPanel() {
		VBox content = new VBox(3);
		TitledPane out = new TitledPane(ManagerMain.bundle.getString("main.groups.shadow"), content);
		out.setExpanded(false);
		//
		content.prefWidthProperty().bind(out.widthProperty());
		//
		CheckBox shadowEnabled = new CheckBox(ManagerMain.bundle.getString("main.shadow.enabled"));
		shadowEnabled.setSelected(cfg.getConfigTable().isUseShadow());
		shadowEnabled.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				cfg.getConfigTable().setUseShadow(shadowEnabled.isSelected());
				preview.updateView();
			}
		});
		content.getChildren().add(shadowEnabled);
		//
		ColorPicker shadowPick = new ColorPicker(ManagerMain.cast(cfg.getConfigTable().getShadowColor()));
		shadowPick.getStyleClass().add("button");
		shadowPick.setStyle("-fx-color-rect-width: 1.5em; -fx-color-rect-height: 1.5em;-fx-min-height: 2.25em;");
		shadowPick.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				cfg.getConfigTable().setShadowColor(ManagerMain.cast(shadowPick.getValue()));
				preview.updateView();
			}
		});
		content.getChildren().add(hbox(new Label(ManagerMain.bundle.getString("main.shadow.color")), shadowPick));
		//
		Spinner<Integer> shadowX = new Spinner<Integer>(-1024, 1024, cfg.getConfigTable().getShadowX());
		shadowX.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> val, Integer old_val, Integer new_val) {
				cfg.getConfigTable().setShadowX(new_val);
				preview.updateView();
			}
		});
		content.getChildren().add(hbox(new Label(ManagerMain.bundle.getString("main.shadow.x")), shadowX));
		//
		Spinner<Integer> shadowY = new Spinner<Integer>(-1024, 1024, cfg.getConfigTable().getShadowY());
		shadowY.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> val, Integer old_val, Integer new_val) {
				cfg.getConfigTable().setShadowY(new_val);
				preview.updateView();
			}
		});
		content.getChildren().add(hbox(new Label(ManagerMain.bundle.getString("main.shadow.y")), shadowY));
		//
		return out;
	}
	
	private Node createBakcgroundPanel() {
		VBox content = new VBox(3);
		TitledPane out = new TitledPane(ManagerMain.bundle.getString("main.groups.background"), content);
		out.setExpanded(false);
		//
		String aux = cfg.getConfigTable().getBackgroundSource();
		if (aux != null && aux.trim().length() > 0) {
			File f = new File(aux.trim());
			if (f.exists() && f.isDirectory()) {
				sourceChooser.setInitialDirectory(f);
			} else if (f.exists() && f.isFile()) {
				sourceChooser.setInitialDirectory(f.getParentFile());
			}
		}
		sourceChooser.setTitle(ManagerMain.bundle.getString("main.background.source.title"));
		//
		aux = cfg.getConfigTable().getBackgroundFile();
		if (aux != null && aux.trim().length() > 0) {
			File f = new File(aux.trim());
			if (f.exists() && f.isFile()) {
				fileChooser.setInitialFileName(f.getName());
				fileChooser.setInitialDirectory(f.getParentFile());
			}
		}
		fileChooser.setTitle(ManagerMain.bundle.getString("main.background.file.title"));
		//
		Spinner<Integer> delay = new Spinner<Integer>(0, 10000, cfg.getConfigTable().getBackgroundDelay());
		delay.setEditable(true);
		delay.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> val, Integer old_val, Integer new_val) {
				cfg.getConfigTable().setBackgroundDelay(new_val);
				preview.updateView();
			}
		});
		content.getChildren().add(hbox(new Label(ManagerMain.bundle.getString("main.background.delay")), delay));
		//
		TextField source = new TextField(cfg.getConfigTable().getBackgroundSource());
		source.setEditable(false);
		source.setTooltip(new Tooltip(ManagerMain.bundle.getString("main.background.source.tooltip")));
		source.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.getButton() == MouseButton.PRIMARY && arg0.getClickCount() == 2) {
					File aux = sourceChooser.showDialog(deskFntCol.getScene().getWindow());
					if (aux != null) {
						cfg.getConfigTable().setBackgroundSource(aux.getAbsolutePath());
						source.setText(aux.getAbsolutePath());
						preview.updateView();
					}
				}
			}
		});
		content.getChildren().add(hbox(new Label(ManagerMain.bundle.getString("main.background.source")), source));
		//
		TextField file = new TextField(cfg.getConfigTable().getBackgroundFile());
		file.setEditable(false);
		file.setTooltip(new Tooltip(ManagerMain.bundle.getString("main.background.file.tooltip")));
		file.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				if (arg0.getButton() == MouseButton.PRIMARY && arg0.getClickCount() == 2) {
					File aux = fileChooser.showOpenDialog(deskFntCol.getScene().getWindow());
					if (aux != null) {
						cfg.getConfigTable().setBackgroundFile(aux.getAbsolutePath());
						file.setText(aux.getAbsolutePath());
						preview.updateView();
					}
				}
			}
		});
		content.getChildren().add(hbox(new Label(ManagerMain.bundle.getString("main.background.file")), file));
		//
		ComboBox<BackgroundMode> mode = new ComboBox<BackgroundMode>(FXCollections.observableArrayList(BackgroundMode.values()));
		mode.setValue(cfg.getConfigTable().getBackgroundMode());
		mode.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent arg0) {
				cfg.getConfigTable().setBackgroundMode(mode.getValue());
				preview.updateView();
			}
		});
		content.getChildren().add(hbox(new Label(ManagerMain.bundle.getString("main.background.mode")), mode));
		//
		return out;
	}
	
	private HBox hbox(Label left, Control right) {
		HBox out = new HBox(5, left, right);
		out.setFillHeight(true);
		out.setAlignment(Pos.CENTER_LEFT);
		//
		HBox.setHgrow(left, Priority.NEVER);
		HBox.setHgrow(right, Priority.ALWAYS);
		//
		left.setMinWidth(Label.USE_COMPUTED_SIZE);
		left.setPrefWidth(Label.USE_COMPUTED_SIZE);
		left.setMaxWidth(Label.USE_COMPUTED_SIZE);
		//
		right.setMinWidth(Control.USE_COMPUTED_SIZE);
		right.setPrefWidth(Control.USE_COMPUTED_SIZE);
		right.setMaxWidth(Double.POSITIVE_INFINITY);
		//
		return out;
	}
	
}
