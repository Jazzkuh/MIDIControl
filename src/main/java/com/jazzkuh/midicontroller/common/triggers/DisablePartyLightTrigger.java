package com.jazzkuh.midicontroller.common.triggers;

import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiTriggerAction;
import lombok.SneakyThrows;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

public class DisablePartyLightTrigger extends MidiTriggerAction {
	@Override
	@SneakyThrows
	public void process(MidiResult midiResult) {
		sendRequest("http://localhost:8888/dance-to-spotify/abort");
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				new FixAllLightTrigger().process(midiResult);
				System.out.println("Party light disabled");
			}
		}, 5);
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
