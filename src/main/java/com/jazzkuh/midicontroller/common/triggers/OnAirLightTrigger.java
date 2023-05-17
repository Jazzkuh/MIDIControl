package com.jazzkuh.midicontroller.common.triggers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiTriggerAction;
import lombok.SneakyThrows;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class OnAirLightTrigger extends MidiTriggerAction {
	@Override
	@SneakyThrows
	public void process(MidiResult midiResult) {
		JsonObject jsonObject = new JsonParser().parse("{\n" +
				"\"type\": \"studio-scene\",\n" +
				"\"params\": {\n" +
					"\"value\": \"On Air\"\n" +
				"}\n" +
				"}").getAsJsonObject();

		sendRequest("http://localhost:39231/api/send?token=lumia892089382", jsonObject);
	}

	@SneakyThrows
	public static void sendRequest(String apiurl, JsonObject jsonObject){
		URL url = new URL(apiurl);

		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("POST");
		connection.setDoOutput(true);
		connection.setRequestProperty("Content-Type","application/json");
		connection.setRequestProperty("Accept", "application/json");

		byte[] out = jsonObject.toString().getBytes(StandardCharsets.UTF_8);
		OutputStream stream = connection.getOutputStream();
		stream.write(out);
		System.out.println(connection.getResponseCode() + " " + connection.getResponseMessage()); // THis is optional
		connection.disconnect();
	}
}
