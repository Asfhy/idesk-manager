package es.asfhy.idesk.manager.fxui.controls;

import java.io.File;
import java.util.Optional;

import es.asfhy.idesk.manager.ManagerMain;
import es.asfhy.idesk.manager.objects.DesktopIcon;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class DeskIconPane extends BorderPane {
	private Tab					parentTab;
	private final DesktopIcon	ico;
	private final TitledPane	viewPane;
	private final ImageView		view;
	private final FileChooser	chooser;
	
	public DeskIconPane(Tab parnet, String fileName) {
		this(parnet, DesktopIcon.create(fileName));
	}
	
	public DeskIconPane(Tab parnet, DesktopIcon icon) {
		super();
		parentTab = parnet;
		ico = icon;
		chooser = new FileChooser();
		chooser.setTitle(ManagerMain.bundle.getString("deskpane.icon.chooserTitle"));
		chooser.getExtensionFilters().clear();
		chooser.getExtensionFilters().add(new ExtensionFilter(ManagerMain.bundle.getString("deskpane.icon.chooserFilter"), "*.png", "*.gif", "*.jpg", "*.jpeg"));
		viewPane = new TitledPane(ManagerMain.bundle.getString("deskpane.icon.title"), view = new ImageView());
		viewPane.setCollapsible(false);
		setCenter(viewPane);
		viewPane.prefHeightProperty().bind(heightProperty());
		setAlignment(viewPane, Pos.TOP_CENTER);
		viewPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent evt) {
				if (evt.getButton() == MouseButton.PRIMARY && evt.getClickCount() == 2) {
					File out = chooser.showOpenDialog(getScene().getWindow());
					if (out != null) {
						ico.setIcon(out.getAbsolutePath());
						updateImage();
					}
				}
			}
		});
		setRight(createNumbersPanel());
		setTop(createTextsPanel());
		setBottom(createButtonsPanel());
		updateImage();
	}
	
	private GridPane createButtonsPanel() {
		GridPane out = new GridPane();
		out.setPadding(new Insets(10));
		//
		ColumnConstraints cc = new ColumnConstraints();
		cc.setFillWidth(true);
		cc.setHalignment(HPos.CENTER);
		cc.setHgrow(Priority.ALWAYS);
		cc.setPercentWidth(50d);
		//
		out.getColumnConstraints().clear();
		out.getColumnConstraints().addAll(cc, cc);
		//
		Button save = new Button(ManagerMain.bundle.getString("deskpane.save.text"), new ImageView(new Image(getClass().getResourceAsStream("/es/asfhy/idesk/manager/rsrc/save.png"), ManagerMain.iconSize, ManagerMain.iconSize, true, true)));
		save.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				try {
					ico.save();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		save.prefWidthProperty().bind(out.widthProperty());
		out.add(save, 0, 0);
		//
		Button delete = new Button(ManagerMain.bundle.getString("deskpane.del.text"), new ImageView(new Image(getClass().getResourceAsStream("/es/asfhy/idesk/manager/rsrc/delete.png"), ManagerMain.iconSize, ManagerMain.iconSize, true, true)));
		delete.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.CONFIRMATION, ManagerMain.bundle.getString("deskpane.del.question"), ButtonType.YES, ButtonType.NO);
				Optional<ButtonType> result = alert.showAndWait();
				if (result != null && result.isPresent() && result.get() == ButtonType.YES) {
					boolean done = false;
					try {
						done = ico.delete();
					} catch (Exception e) {
						e.printStackTrace();
						done = false;
					}
					if (done) {
						parentTab.getTabPane().getTabs().remove(parentTab);
						parentTab = null;
					}
				}
			}
		});
		delete.prefWidthProperty().bind(out.widthProperty());
		out.add(delete, 1, 0);
		//
		return out;
	}
	
	private GridPane createTextsPanel() {
		GridPane out = new GridPane();
		out.setPadding(new Insets(10));
		//
		ColumnConstraints cc1 = new ColumnConstraints();
		cc1.setFillWidth(true);
		cc1.setHgrow(Priority.NEVER);
		cc1.setPercentWidth(25);
		//
		ColumnConstraints cc2 = new ColumnConstraints();
		cc2.setFillWidth(true);
		cc2.setHgrow(Priority.ALWAYS);
		cc2.setPercentWidth(75);
		//
		out.getColumnConstraints().add(cc1);
		out.getColumnConstraints().add(cc2);
		//
		out.add(new Label(ManagerMain.bundle.getString("deskpane.icon.caption")), 0, 0);
		//
		TextField caption = new TextField(ico.getCaption());
		caption.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String old_val, String new_val) {
				ico.setCaption(new_val);
				if (new_val == null || new_val.trim().length() == 0)
					parentTab.setText("[-]");
				else
					parentTab.setText(new_val);
			}
		});
		out.add(caption, 1, 0);
		//
		out.add(new Label(ManagerMain.bundle.getString("deskpane.icon.tooltip")), 0, 1);
		//
		TextField tooltipCaption = new TextField(ico.getTooltipCaption());
		tooltipCaption.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> val, String old_val, String new_val) {
				ico.setTooltipCaption(new_val);
			}
		});
		out.add(tooltipCaption, 1, 1);
		//
		out.add(new Label(ManagerMain.bundle.getString("deskpane.icon.command0")), 0, 2);
		//
		TextField cmd0 = new TextField();
		if (ico.getCommands() != null && ico.getCommands().length >= 1)
			cmd0.setText(ico.getCommands()[0]);
		cmd0.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> val, String old_val, String new_val) {
				if (ico.getCommands() == null || ico.getCommands().length < 1)
					ico.setCommands(new String[2]);
				ico.getCommands()[0] = new_val;
			}
		});
		out.add(cmd0, 1, 2);
		//
		out.add(new Label(ManagerMain.bundle.getString("deskpane.icon.command1")), 0, 3);
		//
		TextField cmd1 = new TextField();
		if (ico.getCommands() != null && ico.getCommands().length >= 2)
			cmd1.setText(ico.getCommands()[1]);
		cmd1.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> val, String old_val, String new_val) {
				if (ico.getCommands() == null || ico.getCommands().length < 1)
					ico.setCommands(new String[2]);
				else if (ico.getCommands().length < 2) {
					String cmds[] = new String[2];
					cmds[0] = ico.getCommands()[0];
					ico.setCommands(cmds);
				}
				ico.getCommands()[1] = new_val;
			}
		});
		out.add(cmd1, 1, 3);
		//
		return out;
	}
	
	private GridPane createNumbersPanel() {
		GridPane out = new GridPane();
		out.setPadding(new Insets(10));
		//
		out.add(new Label(ManagerMain.bundle.getString("deskpane.icon.x")), 0, 0);
		//
		Spinner<Integer> spX = new Spinner<Integer>(-1, 2048, ico.getX() != null ? ico.getX() : 0);
		spX.setEditable(true);
		spX.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				if (spX.getValue().compareTo(0) >= 0)
					ico.setX(spX.getValue());
				else
					ico.setX(null);
			}
		});
		out.add(spX, 1, 0);
		//
		out.add(new Label(ManagerMain.bundle.getString("deskpane.icon.y")), 0, 1);
		//
		Spinner<Integer> spY = new Spinner<Integer>(-1, 2048, ico.getY() != null ? ico.getY() : 0);
		spY.setEditable(true);
		spY.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				if (spY.getValue().compareTo(0) >= 0)
					ico.setY(spY.getValue());
				else
					ico.setY(null);
			}
		});
		out.add(spY, 1, 1);
		//
		out.add(new Label(ManagerMain.bundle.getString("deskpane.icon.width")), 0, 2);
		//
		Spinner<Integer> spW = new Spinner<Integer>(-1, 2048, ico.getWidth() != null ? ico.getWidth() : 0);
		spW.setEditable(true);
		spW.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				if (spW.getValue().compareTo(0) >= 0)
					ico.setWidth(spW.getValue());
				else
					ico.setWidth(null);
				updateImage();
			}
		});
		out.add(spW, 1, 2);
		//
		out.add(new Label(ManagerMain.bundle.getString("deskpane.icon.height")), 0, 3);
		//
		Spinner<Integer> spH = new Spinner<Integer>(-1, 2048, ico.getHeight() != null ? ico.getHeight() : 0);
		spH.setEditable(true);
		spH.valueProperty().addListener(new ChangeListener<Integer>() {
			@Override
			public void changed(ObservableValue<? extends Integer> arg0, Integer arg1, Integer arg2) {
				if (spH.getValue().compareTo(0) >= 0)
					ico.setHeight(spH.getValue());
				else
					ico.setHeight(null);
				updateImage();
			}
		});
		out.add(spH, 1, 3);
		//
		return out;
	}
	
	private synchronized void updateImage() {
		Image im = null;
		double maxW = viewPane.getWidth() - 5;
		double maxH = viewPane.getHeight() - 5;
		if (ico.getIcon() != null) {
			String url = String.format("file://%s", ico.getIcon());
			if (ico.getWidth() != null && ico.getHeight() != null && ico.getWidth().compareTo(0) > 0 && ico.getHeight().compareTo(0) > 0) {
				im = new Image(url, ico.getWidth().doubleValue(), ico.getHeight().doubleValue(), false, true);
			} else {
				Image aux = new Image(url);
				im = new Image(url, Math.min(maxW, aux.getWidth()), Math.min(maxH, aux.getHeight()), true, true);
				aux = null;
			}
		}
		view.setImage(im);
	}
	
	public synchronized DesktopIcon getIcon() {
		return ico;
	}
}
