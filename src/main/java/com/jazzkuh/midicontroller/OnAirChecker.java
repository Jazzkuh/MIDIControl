package com.jazzkuh.midicontroller;

import com.jazzkuh.midicontroller.common.triggers.OnAirLightTrigger;
import com.jazzkuh.midicontroller.common.triggers.RegularLightTrigger;
import lombok.SneakyThrows;

import java.util.TimerTask;

public class OnAirChecker extends TimerTask {
	@Override
	@SneakyThrows
	public void run() {
		byte[] buffer = new byte[1024];

		int bytesRead = MidiController.getInstance().getLine().read(buffer, 0, buffer.length);

		// Calculate the average amplitude of the audio samples
		long sum = 0;
		for (int i = 0; i < bytesRead; i += 2) {
			int sample = (buffer[i + 1] << 8) | buffer[i];
			sum += Math.abs(sample);
		}

		// Calculate the average amplitude level
		double average = sum / (bytesRead / 2.0);
		if (MidiController.getInstance().getOnAir() == null) {
			MidiController.getInstance().setOnAir(average >= 10);
		}

		if (average >= 10 && !MidiController.getInstance().getOnAir()) {
			MidiController.getInstance().setOnAir(true);
			new OnAirLightTrigger().process(null);
			return;
		}

		if (average < 10 && MidiController.getInstance().getOnAir()) {
			MidiController.getInstance().setOnAir(false);
			new RegularLightTrigger().process(null);
		}
	}
}
