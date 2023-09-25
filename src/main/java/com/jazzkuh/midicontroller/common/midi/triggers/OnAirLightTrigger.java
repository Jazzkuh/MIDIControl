package com.jazzkuh.midicontroller.common.midi.triggers;

import com.jazzkuh.midicontroller.MidiController;
import com.jazzkuh.midicontroller.common.midi.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.midi.triggers.abstraction.MidiTriggerAction;
import com.jazzkuh.midicontroller.common.utils.lighting.PhilipsWizLightController;
import com.jazzkuh.midicontroller.common.utils.lighting.bulb.Bulb;
import com.jazzkuh.midicontroller.common.utils.lighting.bulb.BulbRegistry;
import lombok.SneakyThrows;

import java.util.Timer;
import java.util.TimerTask;

public class OnAirLightTrigger extends MidiTriggerAction {
	@Override
	@SneakyThrows
	public void process(MidiResult midiResult) {
		MidiController.getInstance().setMicrophoneOnAirTime(System.currentTimeMillis());
		if (MidiController.getInstance().getPartyMode()) {
			sendRequest("http://localhost:8888/dance-to-spotify/abort");
			new Timer().schedule(new TimerTask() {
				@Override
				public void run() {
					for (Bulb bulb : BulbRegistry.getBulbsByGroups("led_strip")) {
						PhilipsWizLightController.setRGBColor(bulb, 255, 0, 0, 100);
					}
				}
			}, 1);
			return;
		}
		for (Bulb bulb : BulbRegistry.getBulbsByGroup("studio")) {
			PhilipsWizLightController.setRGBColor(bulb, 255, 0, 0, 100);
		}
	}
}
