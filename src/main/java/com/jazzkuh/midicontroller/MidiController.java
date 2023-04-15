package com.jazzkuh.midicontroller;

import com.jazzkuh.midicontroller.common.configuration.DefaultConfiguration;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.credentials.AuthorizationCodeCredentials;
import se.michaelthelin.spotify.requests.authorization.authorization_code.AuthorizationCodeRefreshRequest;

import java.awt.*;
import java.net.URL;
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

	@SneakyThrows
	public MidiController() {
		defaultConfiguration = new DefaultConfiguration();
		defaultConfiguration.saveConfiguration();

		if (!MidiLoader.init()) {
			System.out.println("Failed to initialize MIDI");
			return;
		}

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

	public static void main(String[] args) {
		setInstance(new MidiController());
		MidiControllerApplication.startApp(args);
	}
}