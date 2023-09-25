package com.jazzkuh.midicontroller;

import com.jazzkuh.midicontroller.common.configuration.DefaultConfiguration;
import com.jazzkuh.midicontroller.common.midi.MidiLoader;
import com.jazzkuh.midicontroller.common.midi.triggers.RegularLightTrigger;
import com.jazzkuh.midicontroller.common.utils.music.MusicEngine;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class MidiController {
	private static @Getter @Setter(AccessLevel.PRIVATE) MidiController instance;
	private static final @Getter ExecutorService executorService = Executors.newFixedThreadPool(10);
	private final @Getter Logger logger = Logger.getLogger("MidiControl");
	private final @Getter DefaultConfiguration defaultConfiguration;

	private @Getter MusicEngine musicEngine;
	private @Getter @Setter Boolean onAir = false;
	private @Getter @Setter Boolean shouldSkipOnStart = true;
	private @Getter @Setter byte previousButton = 0;
	private @Getter @Setter Long microphoneOnAirTime = null;
	private @Getter @Setter Boolean partyMode = false;

	@SneakyThrows
	public MidiController() {
		defaultConfiguration = new DefaultConfiguration();
		defaultConfiguration.saveConfiguration();

		if (!MidiLoader.init()) {
			System.out.println("Failed to initialize MIDI");
			return;
		}

		this.musicEngine = new MusicEngine(MusicEngine.MusicEngineProvider.SPOTIFY);
	}

	@SneakyThrows
	public static void main(String[] args) {
		setInstance(new MidiController());

		try {
			new RegularLightTrigger().process(null);
		} catch (Exception e) {
			e.printStackTrace();
		}

		MidiControllerApplication.startApp(args);
	}
}