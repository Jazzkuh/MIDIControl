package com.jazzkuh.midicontroller.common.midi.triggers;

import com.jazzkuh.midicontroller.MidiController;
import com.jazzkuh.midicontroller.common.midi.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.midi.triggers.abstraction.MidiTriggerAction;
import lombok.SneakyThrows;

import java.util.Timer;
import java.util.TimerTask;

public class DisablePartyLightTrigger extends MidiTriggerAction {
	@Override
	@SneakyThrows
	public void process(MidiResult midiResult) {
		sendRequest("http://localhost:8888/dance-to-spotify/abort");
		MidiController.getInstance().setPartyMode(false);
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				new FixAllLightTrigger().process(midiResult);
				System.out.println("Party light disabled");
			}
		}, 1);
	}
}
