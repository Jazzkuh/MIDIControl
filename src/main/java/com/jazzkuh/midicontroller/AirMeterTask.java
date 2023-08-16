package com.jazzkuh.midicontroller;

import com.jazzkuh.midicontroller.common.utils.AnimationTimerExt;
import com.jazzkuh.midicontroller.common.utils.panes.DecibelMeterPane;

import javax.sound.sampled.TargetDataLine;

public class AirMeterTask extends AnimationTimerExt {
	private static final byte[] buffer = new byte[1024]; // Buffer for audio samples
	private static final double MAX_AMPLITUDE = 32767; // Maximum amplitude for 16-bit audio
	private TargetDataLine targetDataLine;
	private DecibelMeterPane decibelMeterPane;

	public AirMeterTask(TargetDataLine targetDataLine, DecibelMeterPane decibelMeterPane) {
		super(150);
		this.targetDataLine = targetDataLine;
		this.decibelMeterPane = decibelMeterPane;
	}

	@Override
	public void handle() {
		int bytesRead = targetDataLine.read(buffer, 0, buffer.length);

		// Calculate the average amplitude of the audio samples
		long sum = 0;
		for (int i = 0; i < bytesRead; i += 2) {
			int sample = (buffer[i + 1] << 8) | buffer[i];
			sum += Math.abs(sample);
		}

		// Calculate the average amplitude level
		double averageAmplitude = sum / (bytesRead / 2.0);
		double decibelValue = mapToDecibel(averageAmplitude);
		if (decibelValue <= 2) {
			decibelValue = 0;
		}

		decibelMeterPane.updateMeter(decibelValue);
	}

	private double mapToDecibel(double value) {
		double normalizedValue = value / AirMeterTask.MAX_AMPLITUDE;
		double mappedValue = normalizedValue * 60D;
		return Math.min(60D, Math.max(0, mappedValue));
	}
}
