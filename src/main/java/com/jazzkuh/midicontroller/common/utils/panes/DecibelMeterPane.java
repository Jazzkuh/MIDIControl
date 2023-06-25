package com.jazzkuh.midicontroller.common.utils.panes;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class DecibelMeterPane extends StackPane {

    private static final double MAX_DB = 100; // Maximum decibel value

    private Rectangle meterFillLeft;
    private Rectangle meterFillRight;
    private Label decibelLabel;
    private Label secondaryLabel;

    public DecibelMeterPane(double width, double height, String secondaryLabelText) {
        setPrefSize(width, height);
        setMaxSize(width, height);
        setMinSize(width, height);

        double meterWidth = (width - 6) / 2 - 6; // Divide the width equally for dual meters with a gap of 6 units

        meterFillLeft = createMeterFill(meterWidth, height - 48);
        meterFillRight = createMeterFill(meterWidth, height - 48);
        decibelLabel = createDecibelLabel();
        secondaryLabel = createSecondaryLabel(secondaryLabelText);

        HBox meterBox = new HBox(6, meterFillLeft, meterFillRight);
        meterBox.setAlignment(Pos.BOTTOM_CENTER);

        StackPane.setAlignment(meterBox, Pos.CENTER);
        StackPane.setAlignment(decibelLabel, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(secondaryLabel, Pos.BOTTOM_CENTER);

        getChildren().addAll(meterBox, decibelLabel, secondaryLabel);
    }

    private Rectangle createMeterFill(double width, double height) {
        Rectangle fill = new Rectangle();
        fill.setWidth(width);
        fill.setHeight(0);
        fill.setFill(Color.GREEN);
        fill.setTranslateY(-18); // Adjust the translation to center the meter
        return fill;
    }

    private Label createDecibelLabel() {
        Label label = new Label("0 dB");
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 14px;");
        label.setTranslateY(10);
        return label;
    }

    private Label createSecondaryLabel(String text) {
        Label label = new Label(text);
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 12px;");
        label.setTranslateY(-5);
        return label;
    }

    public void updateMeter(double decibelValue) {
        double fillHeight = decibelValue / MAX_DB * (getHeight() - 60);

        Platform.runLater(() -> {
            meterFillLeft.setHeight(fillHeight);
            meterFillRight.setHeight(fillHeight);

            decibelLabel.setText(String.format("%.1f dB", decibelValue));

            if (decibelValue >= 80) {
                meterFillLeft.setFill(Color.RED);
                meterFillRight.setFill(Color.RED);
            } else if (decibelValue >= 60) {
                meterFillLeft.setFill(Color.YELLOW);
                meterFillRight.setFill(Color.YELLOW);
            } else {
                meterFillLeft.setFill(Color.GREEN);
                meterFillRight.setFill(Color.GREEN);
            }
        });
    }
}
