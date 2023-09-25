package com.jazzkuh.midicontroller.common.midi;

import com.jazzkuh.midicontroller.MidiController;
import com.jazzkuh.midicontroller.common.MidiTriggerRegistry;
import com.jazzkuh.midicontroller.common.midi.triggers.OnAirLightTrigger;
import com.jazzkuh.midicontroller.common.midi.triggers.RegularLightTrigger;
import com.jazzkuh.midicontroller.common.midi.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.midi.triggers.abstraction.MidiTriggerAction;
import com.jazzkuh.midicontroller.common.midi.triggers.music.MusicSkipStartTrigger;
import com.jazzkuh.midicontroller.common.utils.music.MusicEngine;
import lombok.SneakyThrows;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public class MidiInputReceiver implements Receiver {
	public String name;
	private boolean skipped;

	public MidiInputReceiver(String name) {
		this.name = name;
	}

	@Override
	@SneakyThrows
	public void send(MidiMessage msg, long timeStamp) {
		byte[] byteArray = msg.getMessage();
		byte channel = byteArray[0];
		byte noteValue = byteArray[1];
		byte pressedValue = byteArray[2];

		MidiResult midiResult = new MidiResult(noteValue, pressedValue);
		System.out.println("Channel: " + channel + " Note: " + noteValue + " Pressed: " + pressedValue);

		if (channel == -80 && noteValue == 20 && pressedValue >= 1 && pressedValue <= 6) {
			MidiController.getInstance().setPreviousButton(pressedValue);
			System.out.println("Previous button: " + pressedValue);
		}

		if (channel == -80 && noteValue == 20) {
			System.out.println(MidiController.getInstance().getPreviousButton() == 6);
			if (pressedValue == 6 || MidiController.getInstance().getPreviousButton() == 6) {
				new MusicSkipStartTrigger().process(null);
				System.out.println("Skip/Start button: " + pressedValue);
			}
		}

		if (channel == -80 && noteValue == 15) {
			if (pressedValue >= 1 && !MidiController.getInstance().getOnAir()) {
				MidiController.getInstance().setOnAir(true);
				new OnAirLightTrigger().process(null);
				return;
			}

			if (pressedValue < 1 && MidiController.getInstance().getOnAir()) {
				MidiController.getInstance().setOnAir(false);
				new RegularLightTrigger().process(null);
			}
		}

		MusicEngine musicEngine = MidiController.getInstance().getMusicEngine();
		if (channel == -75 && noteValue == 15) {
			if (pressedValue >= 1 && skipped) {
				skipped = false;
				if (MidiController.getInstance().getShouldSkipOnStart()) {
					musicEngine.next();
					if (!musicEngine.isPlaying() && musicEngine.getProvider() == MusicEngine.MusicEngineProvider.APPLE_MUSIC) {
						musicEngine.playPause();
					}
				} else {
					if (!musicEngine.isPlaying()) {
						musicEngine.playPause();
					}
				}
			}

			if (pressedValue < 1 && !skipped) {
				skipped = true;
				if (musicEngine.isPlaying()) {
					musicEngine.playPause();
				}
			}
		}

		MidiTriggerAction midiTriggerAction = MidiTriggerRegistry.getAction(midiResult);

		if (midiTriggerAction == null) return;
		midiTriggerAction.process(midiResult);
		System.out.println("Triggered " + midiTriggerAction.getClass().getSimpleName());
	}

	@Override
	public void close() {}
}