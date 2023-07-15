package com.jazzkuh.midicontroller.common.triggers;

import com.jazzkuh.midicontroller.MidiController;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiTriggerAction;
import com.jazzkuh.midicontroller.common.utils.lighting.PhilipsWizLightController;
import com.jazzkuh.midicontroller.common.utils.lighting.bulb.Bulb;
import com.jazzkuh.midicontroller.common.utils.lighting.bulb.BulbRegistry;
import lombok.SneakyThrows;

public class OnAirLightTrigger extends MidiTriggerAction {
	@Override
	@SneakyThrows
	public void process(MidiResult midiResult) {
		MidiController.getInstance().setMicrophoneOnAirTime(System.currentTimeMillis());
		for (Bulb bulb : BulbRegistry.getBulbsByGroup("studio")) {
			PhilipsWizLightController.setRGBColor(bulb, 255, 0, 0, 100);
		}
	}
}
