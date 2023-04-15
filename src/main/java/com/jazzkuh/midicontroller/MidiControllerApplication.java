package com.jazzkuh.midicontroller;

import de.jangassen.MenuToolkit;
import de.jangassen.model.AppearanceMode;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.awt.*;
import java.net.URL;

public class MidiControllerApplication extends Application {
	@Override
	public void start(Stage stage) {
		stage.setOnCloseRequest(event -> {
			Platform.exit();
			System.exit(0);
		});

		MenuToolkit menuToolkit = MenuToolkit.toolkit();
		menuToolkit.setAppearanceMode(AppearanceMode.AUTO);

		Image image = new Image(MidiController.class.getClassLoader().getResource("tray_icon.png").toString());
		Menu trayMenu = new Menu("MidiControl", new ImageView(image));

		MenuItem exitItem = new MenuItem("Exit");
		exitItem.setOnAction(event -> {
			Platform.exit();
			System.exit(0);
		});
		trayMenu.getItems().add(exitItem);
		MenuToolkit.toolkit().setTrayMenu(trayMenu);
	}

	public static void startApp(String[] args) {
		Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
		URL imageResource = MidiController.class.getClassLoader().getResource("app.png");
		java.awt.Image image = defaultToolkit.getImage(imageResource);
		Taskbar.getTaskbar().setIconImage(image);

		launch(args);
	}
}
