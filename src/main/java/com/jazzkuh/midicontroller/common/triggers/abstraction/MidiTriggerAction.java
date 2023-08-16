package com.jazzkuh.midicontroller.common.triggers.abstraction;

import lombok.SneakyThrows;

import java.net.HttpURLConnection;
import java.net.URL;

public abstract class MidiTriggerAction implements MidiTrigger {

    public abstract void process(MidiResult midiResult);

    @SneakyThrows
    public final void sendRequest(String apiUrl) {
        URL url = new URL(apiUrl);

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");

        System.out.println(connection.getResponseCode() + " " + connection.getResponseMessage()); // THis is optional

        connection.disconnect();
    }
}