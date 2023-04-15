package com.jazzkuh.midicontroller;

import lombok.SneakyThrows;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.Transmitter;
import java.util.Arrays;
import java.util.List;

public class MidiLoader {
	@SneakyThrows
	public static boolean init() {
		MidiDevice.Info[] devices = MidiSystem.getMidiDeviceInfo();
		if (devices.length == 0) {
			System.out.println("No MIDI devices found");
			return false;
		}

		List<MidiDevice.Info> infoList = Arrays.stream(devices).filter(device -> device.getName().contains("RODECaster")).toList();
		if (infoList.isEmpty()) {
			System.out.println("No RODECaster found");
			return false;
		}

		MidiDevice.Info info = infoList.get(1);
		MidiDevice device = MidiSystem.getMidiDevice(info);
		device.open();

		Transmitter transmitter = device.getTransmitter();
		transmitter.setReceiver(new MidiInputReceiver("RODECaster Pro II"));

		System.out.println("Listening to MIDI messages...");
		return true;
	}
}
