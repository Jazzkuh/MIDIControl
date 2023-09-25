package com.jazzkuh.midicontroller.common.midi.triggers.music;

import com.jazzkuh.midicontroller.MidiController;
import com.jazzkuh.midicontroller.common.midi.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.midi.triggers.abstraction.MidiTriggerAction;
import com.jazzkuh.midicontroller.common.utils.music.MusicEngine;
import lombok.SneakyThrows;

public class MusicPreviousTrigger extends MidiTriggerAction {
	@Override
	@SneakyThrows
	public void process(MidiResult midiResult) {
		MusicEngine musicEngine = MidiController.getInstance().getMusicEngine();
		musicEngine.previous();
	}
}
