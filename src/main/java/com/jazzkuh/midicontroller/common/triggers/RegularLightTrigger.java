package com.jazzkuh.midicontroller.common.triggers;

import com.jazzkuh.midicontroller.MidiController;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiTriggerAction;
import com.jazzkuh.midicontroller.common.utils.lighting.PhilipsWizLightController;
import com.jazzkuh.midicontroller.common.utils.lighting.bulb.Bulb;
import com.jazzkuh.midicontroller.common.utils.lighting.bulb.BulbRegistry;
import lombok.SneakyThrows;

public class RegularLightTrigger extends MidiTriggerAction {
	@Override
	@SneakyThrows
	public void process(MidiResult midiResult) {
		MidiController.getInstance().setMicrophoneOnAirTime(null);
		for (Bulb bulb : BulbRegistry.getBulbsByGroups("studio", "green")) {
			PhilipsWizLightController.setRGBColor(bulb, 24, 255, 0, 100);
		}

		for (Bulb bulb : BulbRegistry.getBulbsByGroups("studio", "purple")) {
			PhilipsWizLightController.setRGBColor(bulb, 188, 0, 255, 100);
		}
	}
}
