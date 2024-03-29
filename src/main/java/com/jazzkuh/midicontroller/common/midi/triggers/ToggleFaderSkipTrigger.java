package com.jazzkuh.midicontroller.common.midi.triggers;

import com.jazzkuh.midicontroller.MidiController;
import com.jazzkuh.midicontroller.common.midi.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.midi.triggers.abstraction.MidiTriggerAction;
import lombok.SneakyThrows;

public class ToggleFaderSkipTrigger extends MidiTriggerAction {
	@Override
	@SneakyThrows
	public void process(MidiResult midiResult) {
		MidiController.getInstance().setShouldSkipOnStart(!MidiController.getInstance().getShouldSkipOnStart());
	}
}
