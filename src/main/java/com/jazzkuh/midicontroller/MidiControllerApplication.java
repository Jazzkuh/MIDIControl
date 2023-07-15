package com.jazzkuh.midicontroller;

import com.jazzkuh.midicontroller.common.utils.panes.DecibelMeterPane;
import de.jangassen.MenuToolkit;
import de.jangassen.model.AppearanceMode;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;

import javax.sound.sampled.TargetDataLine;
import java.awt.*;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.CompletableFuture;

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

		BorderPane borderPane = new BorderPane();
		StackPane stackPane = new StackPane();
		borderPane.setCenter(stackPane);
		Scene scene = new Scene(borderPane, 640, 480);

		Image offAir = new Image(MidiController.class.getClassLoader().getResource("offair.png").toString());
		Image onAir = new Image(MidiController.class.getClassLoader().getResource("onair.png").toString());
		ImageView onAirImage = new ImageView(offAir);
		onAirImage.setFitWidth(64);
		onAirImage.setFitHeight(64);
		onAirImage.setTranslateY(-150);
		onAirImage.setTranslateX(20);

		HBox hbxImg = new HBox();
		hbxImg.setAlignment(Pos.CENTER_LEFT);
		hbxImg.getChildren().add(onAirImage);

		String formattedTime = new java.text.SimpleDateFormat("HH:mm:ss").format(new Date());
		TextField time = getTextField(formattedTime, -150, 70, Pos.CENTER_LEFT, "textfield", "-fx-font-size: 60px;");

		TextField nextHourTextField = getTextField("Next hour:", -80, Pos.CENTER_LEFT, "textfield");
		TextField nextHour = getTextField("00:00:00", -20, Pos.CENTER_LEFT, "textfield2");

		TextField micOpenTextField = getTextField("Mic open:", 40, Pos.CENTER_LEFT, "textfield");
		TextField micOpenText = getTextField("00:00:00", 100, Pos.CENTER_LEFT, "textfield2");

		Image logo = new Image(MidiController.class.getClassLoader().getResource("logo-transparent.png").toString());
		ImageView logoImage = new ImageView(logo);
		logoImage.setFitWidth(500);
		logoImage.setFitHeight(500);
		logoImage.setTranslateX(-20);

		HBox logoHBox = new HBox();
		logoHBox.setAlignment(Pos.CENTER_RIGHT);
		logoHBox.getChildren().add(logoImage);

		Timer timer = new Timer();
		timer.schedule(new java.util.TimerTask() {
			@Override
			public void run() {
				CompletableFuture.runAsync(() -> {
					try {
						SimpleDateFormat dateFormat = new java.text.SimpleDateFormat("HH:mm:ss");
						Date now = new Date();

						String formattedTime = new SimpleDateFormat("HH:mm:ss").format(now);
						time.setText(formattedTime);

						int minutes = now.getMinutes();
						int seconds = now.getSeconds();

						long msUntilNextHour = Duration.ofHours(1).minus(Duration.ofMinutes(minutes)).minus(Duration.ofSeconds(seconds)).toMillis();
						dateFormat.setTimeZone(TimeZone.getTimeZone("Etc/GMT+0"));

						nextHour.setText(dateFormat.format(new Date(msUntilNextHour)));

						if (MidiController.getInstance().getMicrophoneOnAirTime() != null) {
							long elapsedMillis = System.currentTimeMillis() - MidiController.getInstance().getMicrophoneOnAirTime();
							Date elapsed = new Date(elapsedMillis);

							dateFormat.setTimeZone(TimeZone.getTimeZone("Etc/GMT+0"));

							micOpenText.setText(dateFormat.format(elapsed));
							onAirImage.setImage(onAir);
						} else {
							micOpenText.setText("00:00:00");
							onAirImage.setImage(offAir);
						}
					} catch (Exception ignored) {
					}
				});
			}
		}, 1000, 1000);

		DecibelMeterPane mainMeter = new DecibelMeterPane(70, 300, "Main Output");

		Map<TargetDataLine, DecibelMeterPane> decibelMeterPaneMap = new HashMap<>();
		decibelMeterPaneMap.put(MidiController.getInstance().getMainLine(), mainMeter);

		for (Map.Entry<TargetDataLine, DecibelMeterPane> entry : decibelMeterPaneMap.entrySet()) {
			TargetDataLine line = entry.getKey();
			DecibelMeterPane decibelMeterPane = entry.getValue();

			//CompletableFuture.runAsync(() -> new Timer().scheduleAtFixedRate(new OutputLevelDetector(line, decibelMeterPane), 0, 100));
		}


		HBox decibelMeterHBox = new HBox();
		decibelMeterHBox.setAlignment(Pos.BOTTOM_RIGHT);
		decibelMeterHBox.setTranslateX(-40);
		decibelMeterHBox.setTranslateY(-50);
		decibelMeterHBox.getChildren().addAll(mainMeter);

		stackPane.getChildren().addAll(
				nextHour,
				nextHourTextField,
				micOpenText,
				micOpenTextField,
				time,
				hbxImg,
				logoHBox,
				decibelMeterHBox
		);

		borderPane.setBackground(new Background(new BackgroundFill(Color.BLACK, CornerRadii.EMPTY, Insets.EMPTY)));

		stage.setScene(scene);
		stage.show();
	}

	public static void startApp(String[] args) {
		Toolkit defaultToolkit = Toolkit.getDefaultToolkit();
		URL imageResource = MidiController.class.getClassLoader().getResource("app.png");
		java.awt.Image image = defaultToolkit.getImage(imageResource);
		Taskbar.getTaskbar().setIconImage(image);

		launch(args);
	}

	private TextField getTextField(String text, int y, int x, Pos alignment, String styleClass, String style) {
		javafx.scene.control.TextField textField = new javafx.scene.control.TextField(text);
		textField.getStylesheets().add(MidiController.class.getClassLoader().getResource("style.css").toString());
		textField.getStyleClass().add(styleClass);

		textField.setAlignment(alignment);
		textField.setBorder(null);
		textField.setBackground(null);
		textField.setStyle(style);
		textField.setDisable(true);
		textField.setTranslateY(y);
		textField.setTranslateX(x);

		return textField;
	}

	private TextField getTextField(String text, int y, Pos alignment, String styleClass) {
		return getTextField(text, y, 0, alignment, styleClass, "-fx-font-size: 44px;");
	}
}
