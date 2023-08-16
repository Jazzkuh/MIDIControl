package com.jazzkuh.midicontroller;

import com.jazzkuh.midicontroller.common.utils.AnimationTimerExt;
import com.jazzkuh.midicontroller.common.utils.panes.DecibelMeterPane;

import javax.sound.sampled.TargetDataLine;

public class AirMeterTask extends AnimationTimerExt {
	private static final double MAX_AMPLITUDE = 32767; // Maximum amplitude for 16-bit audio
	private TargetDataLine targetDataLine;
	private DecibelMeterPane decibelMeterPane;

	public AirMeterTask(TargetDataLine targetDataLine, DecibelMeterPane decibelMeterPane) {
		super(100);
		this.targetDataLine = targetDataLine;
		this.decibelMeterPane = decibelMeterPane;
	}

	@Override
	public void handle() {
		byte[] buffer = new byte[1024];

		int bytesRead = targetDataLine.read(buffer, 0, buffer.length);

		// Calculate the average amplitude of the audio samples
		long sum = 0;
		for (int i = 0; i < bytesRead; i += 2) {
			int sample = (buffer[i + 1] << 8) | buffer[i];
			sum += Math.abs(sample);
		}

		// Calculate the average amplitude level
		double averageAmplitude = sum / (bytesRead / 2.0);
		double decibelValue = mapToDecibel(averageAmplitude, 0, MAX_AMPLITUDE, 0, 100);
		if (decibelValue <= 2) {
			decibelValue = 0;
		}

		decibelMeterPane.updateMeter(decibelValue);
	}

	private double mapToDecibel(double value, double inputMin, double inputMax, double outputMin, double outputMax) {
		double normalizedValue = (value - inputMin) / (inputMax - inputMin);
		double mappedValue = normalizedValue * (outputMax - outputMin) + outputMin;
		return Math.min(outputMax, Math.max(outputMin, mappedValue));
	}
}
