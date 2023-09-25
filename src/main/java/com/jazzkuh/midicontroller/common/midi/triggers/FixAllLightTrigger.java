package com.jazzkuh.midicontroller.common.midi.triggers;

import com.jazzkuh.midicontroller.common.midi.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.midi.triggers.abstraction.MidiTriggerAction;
import com.jazzkuh.midicontroller.common.utils.lighting.PhilipsWizLightController;
import com.jazzkuh.midicontroller.common.utils.lighting.bulb.Bulb;
import com.jazzkuh.midicontroller.common.utils.lighting.bulb.BulbRegistry;
import lombok.SneakyThrows;

public class FixAllLightTrigger extends MidiTriggerAction {
	@Override
	@SneakyThrows
	public void process(MidiResult midiResult) {
		for (Bulb bulb : BulbRegistry.getBulbsByGroups("warm_white")) {
			PhilipsWizLightController.setColorTemperature(bulb, 60);
		}
		for (Bulb bulb : BulbRegistry.getBulbsByGroups("studio", "magenta")) {
			PhilipsWizLightController.setRGBColor(bulb, 255, 0, 93, 100);
		}

		for (Bulb bulb : BulbRegistry.getBulbsByGroups("studio", "indigo")) {
			PhilipsWizLightController.setRGBColor(bulb, 111, 0, 255, 100);
		}
	}
}
