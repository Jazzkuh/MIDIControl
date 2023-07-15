package com.jazzkuh.midicontroller.common.triggers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jazzkuh.midicontroller.MidiController;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiTriggerAction;
import com.jazzkuh.midicontroller.common.utils.lighting.PhilipsWizLightController;
import com.jazzkuh.midicontroller.common.utils.lighting.bulb.Bulb;
import com.jazzkuh.midicontroller.common.utils.lighting.bulb.BulbRegistry;
import lombok.SneakyThrows;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class FixAllLightTrigger extends MidiTriggerAction {
	@Override
	@SneakyThrows
	public void process(MidiResult midiResult) {
		for (Bulb bulb : BulbRegistry.getBulbsByGroups("warm_white")) {
			PhilipsWizLightController.setColorTemperature(bulb, 60);
		}
	}
}
