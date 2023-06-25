package com.jazzkuh.midicontroller.common.utils.panes;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class SingleDecibelMeterPane extends StackPane {

    private static final double MAX_DB = 100; // Maximum decibel value

    private Rectangle meterFill;
    private Label decibelLabel;

    public SingleDecibelMeterPane(double width, double height) {
        setPrefSize(width, height);
        setMaxSize(width, height);
        setMinSize(width, height);

        meterFill = createMeterFill(width - 12, height - 48);
        decibelLabel = createDecibelLabel();

        VBox meterBox = new VBox(meterFill, decibelLabel);
        meterBox.setAlignment(Pos.BOTTOM_CENTER);
        meterBox.setPadding(new Insets(0, 0, 10, 0));

        StackPane.setAlignment(meterBox, Pos.CENTER);
        getChildren().addAll(meterBox);
    }

    private Rectangle createMeterFill(double width, double height) {
        Rectangle fill = new Rectangle();
        fill.setWidth(width);
        fill.setHeight(0);
        fill.setFill(Color.GREEN);;
        fill.setTranslateY(-6); // Adjust the translation to center the meter
        return fill;
    }

    private Label createDecibelLabel() {
        Label label = new Label("0 dB");
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 14px;");
        return label;
    }

    public void updateMeter(double decibelValue) {
        double fillHeight = decibelValue / MAX_DB * (getHeight() - 60);

        Platform.runLater(() -> {
            meterFill.setHeight(fillHeight);

            decibelLabel.setText(String.format("%.1f dB", decibelValue));

            if (decibelValue >= 80) {
                meterFill.setFill(Color.RED);
            } else if (decibelValue >= 60) {
                meterFill.setFill(Color.YELLOW);
            } else {
                meterFill.setFill(Color.GREEN);
            }
        });
    }
}
