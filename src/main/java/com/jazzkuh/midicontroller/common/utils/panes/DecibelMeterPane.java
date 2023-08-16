package com.jazzkuh.midicontroller.common.utils.panes;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;

public class DecibelMeterPane extends StackPane {
    private static final int MAX_DB = 60; // Maximum decibel value

    private SpectrumBar meterBarLeft;
    private SpectrumBar meterBarRight;
    private Label decibelLabel;
    private Label secondaryLabel;

    public DecibelMeterPane(double width, double height, String secondaryLabelText) {
        setPrefSize(width, height);
        setMaxSize(width, height);
        setMinSize(width, height);

        double meterWidth = (width - 6) / 2 - 6; // Divide the width equally for dual meters with a gap of 6 units

        meterBarLeft = createSpectrumBar(meterWidth, height - 48);
        meterBarRight = createSpectrumBar(meterWidth, height - 48);
        decibelLabel = createDecibelLabel();
        secondaryLabel = createSecondaryLabel(secondaryLabelText);

        HBox meterBox = new HBox(6, meterBarLeft, meterBarRight);
        meterBox.setAlignment(Pos.BOTTOM_CENTER);

        StackPane.setAlignment(meterBox, Pos.CENTER);
        StackPane.setAlignment(decibelLabel, Pos.BOTTOM_CENTER);
        StackPane.setAlignment(secondaryLabel, Pos.BOTTOM_CENTER);

        getChildren().addAll(meterBox, decibelLabel, secondaryLabel);
    }

    private SpectrumBar createSpectrumBar(double width, double height) {
        SpectrumBar spectrumBar = new SpectrumBar(MAX_DB, 50); // Adjust the parameters as needed
        spectrumBar.setPrefSize(width, height);
        spectrumBar.setTranslateY(-20); // Adjust the translation to raise the bars higher
        return spectrumBar;
    }

    private Label createDecibelLabel() {
        Label label = new Label("0 dB");
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 14px;");
        label.setTranslateY(13);
        return label;
    }

    private Label createSecondaryLabel(String text) {
        Label label = new Label(text);
        label.setTextFill(Color.WHITE);
        label.setStyle("-fx-font-size: 12px;");
        label.setTranslateY(-3);
        return label;
    }

    public void updateMeter(double decibelValue) {
        meterBarLeft.setValue(decibelValue);
        meterBarRight.setValue(decibelValue);

        decibelLabel.setText(String.format("%.1f dB", decibelValue));
    }
}