package com.jazzkuh.midicontroller;

import com.jazzkuh.midicontroller.common.configuration.DefaultConfiguration;
import com.jazzkuh.midicontroller.common.triggers.RegularLightTrigger;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;

import javax.sound.sampled.*;
import java.util.Arrays;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

public class MidiController {
	private static @Getter @Setter(AccessLevel.PRIVATE) MidiController instance;
	private static final @Getter ExecutorService executorService = Executors.newFixedThreadPool(10);
	private static @Getter SpotifyApi spotifyApi;
	private final @Getter Logger logger = Logger.getLogger("MidiControl");
	private final @Getter DefaultConfiguration defaultConfiguration;
	private @Getter TargetDataLine mainLine;
	private @Getter @Setter Boolean onAir = false;
	private @Getter @Setter Boolean shouldSkipOnStart = true;
	private @Getter @Setter byte previousButton = 0;
	private @Getter @Setter Long microphoneOnAirTime = null;

	@SneakyThrows
	public MidiController() {
		defaultConfiguration = new DefaultConfiguration();
		defaultConfiguration.saveConfiguration();

		if (!MidiLoader.init()) {
			System.out.println("Failed to initialize MIDI");
			return;
		}

		AudioFormat format = new AudioFormat(44100, 16, 1, true, true);
		/*mainLine = getLine("RODECaster Pro II Main Multitrack", format);*/

		spotifyApi = new SpotifyApi.Builder()
				.setClientId(defaultConfiguration.getSpotifyClientId())
				.setClientSecret(defaultConfiguration.getSpotifyClientSecret())
				.setRefreshToken(defaultConfiguration.getSpotifyRefreshToken())
				.build();

		AuthorizationCodeRefreshRequest authorizationCodeRefreshRequest = spotifyApi.authorizationCodeRefresh().build();
		new Timer().scheduleAtFixedRate(new TimerTask() {
			@Override
			@SneakyThrows
			public void run() {
				final AuthorizationCodeCredentials authorizationCodeCredentials = authorizationCodeRefreshRequest.executeAsync().get();
				spotifyApi.setAccessToken(authorizationCodeCredentials.getAccessToken());
				System.out.println("Regenerated new Spotify credentials. (Expires in: " + (authorizationCodeCredentials.getExpiresIn() / 60 / 60) + " hour)");
			}
		}, 0, 3200 * 1000);
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

	@SneakyThrows
	private TargetDataLine getLine(String name, AudioFormat format) {
		Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
		Optional<Mixer.Info> optionalLineInfo = Arrays.stream(mixerInfos).filter(mixerInfo -> mixerInfo.getName().equals(name)).findFirst();
		if (optionalLineInfo.isEmpty()) {
			System.out.println("Microphone line is not supported.");
			System.exit(0);
		}

		DataLine.Info info = new DataLine.Info(TargetDataLine.class, format, AudioSystem.NOT_SPECIFIED);
		Mixer mixer = AudioSystem.getMixer(optionalLineInfo.get());

		if (!mixer.isLineSupported(info)) {
			System.out.println("Selected input device is not supported.");
			System.exit(0);
		}

		TargetDataLine dataLine = (TargetDataLine) mixer.getLine(info);
		dataLine.open(format);
		dataLine.start();
		System.out.println("Microphone line " + name + " is ready.");

		return dataLine;
	}
}