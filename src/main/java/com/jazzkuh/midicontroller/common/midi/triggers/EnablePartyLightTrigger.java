package com.jazzkuh.midicontroller.common.midi.triggers;

import com.jazzkuh.midicontroller.MidiController;
import com.jazzkuh.midicontroller.common.midi.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.midi.triggers.abstraction.MidiTriggerAction;
import lombok.SneakyThrows;

public class EnablePartyLightTrigger extends MidiTriggerAction {
	@Override
	@SneakyThrows
	public void process(MidiResult midiResult) {
		sendRequest("http://localhost:8888/dance-to-spotify?mode=party");
		MidiController.getInstance().setPartyMode(true);
	}
}
