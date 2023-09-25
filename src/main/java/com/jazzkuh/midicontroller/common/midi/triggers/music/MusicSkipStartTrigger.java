package com.jazzkuh.midicontroller.common.midi.triggers.music;

import com.jazzkuh.midicontroller.MidiController;
import com.jazzkuh.midicontroller.common.midi.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.midi.triggers.abstraction.MidiTriggerAction;
import com.jazzkuh.midicontroller.common.utils.music.MusicEngine;
import lombok.SneakyThrows;

public class MusicSkipStartTrigger extends MidiTriggerAction {
	@Override
	@SneakyThrows
	public void process(MidiResult midiResult) {
		MusicEngine musicEngine = MidiController.getInstance().getMusicEngine();
		if (musicEngine.isPlaying()) {
			musicEngine.playPause();
		} else if (MidiController.getInstance().getShouldSkipOnStart()) {
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
}
