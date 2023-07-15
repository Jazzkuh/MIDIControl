package com.jazzkuh.midicontroller.common.triggers;

import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiTriggerAction;
import lombok.SneakyThrows;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class DisablePartyLightTrigger extends MidiTriggerAction {
	@Override
	@SneakyThrows
	public void process(MidiResult midiResult) {
		sendRequest("http://localhost:8888/dance-to-spotify/abort");
		new FixAllLightTrigger().process(midiResult);
		new RegularLightTrigger().process(midiResult);
	}

	@SneakyThrows
	public static void sendRequest(String apiUrl) {
		URL url = new URL(apiUrl);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");

		System.out.println(connection.getResponseCode() + " " + connection.getResponseMessage()); // THis is optional

		connection.disconnect();
	}
}
