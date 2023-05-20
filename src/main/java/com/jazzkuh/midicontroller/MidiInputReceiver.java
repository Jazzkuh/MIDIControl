package com.jazzkuh.midicontroller;

import com.jazzkuh.midicontroller.common.MidiTriggerRegistry;
import com.jazzkuh.midicontroller.common.triggers.OnAirLightTrigger;
import com.jazzkuh.midicontroller.common.triggers.RegularLightTrigger;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiTriggerAction;
import lombok.SneakyThrows;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import se.michaelthelin.spotify.requests.data.player.GetInformationAboutUsersCurrentPlaybackRequest;
import se.michaelthelin.spotify.requests.data.player.PauseUsersPlaybackRequest;
import se.michaelthelin.spotify.requests.data.player.SkipUsersPlaybackToNextTrackRequest;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public class MidiInputReceiver implements Receiver {
	public String name;
	private boolean skipped;

	public MidiInputReceiver(String name) {
		this.name = name;
	}

	@Override
	@SneakyThrows
	public void send(MidiMessage msg, long timeStamp) {
		byte[] byteArray = msg.getMessage();
		byte channel = byteArray[0];
		byte noteValue = byteArray[1];
		byte pressedValue = byteArray[2];

		MidiResult midiResult = new MidiResult(noteValue, pressedValue);
		System.out.println(midiResult);

		if (channel == -80 && noteValue == 15) {
			if (pressedValue >= 1 && !MidiController.getInstance().getOnAir()) {
				MidiController.getInstance().setOnAir(true);
				new OnAirLightTrigger().process(null);
				return;
			}

			if (pressedValue < 1 && MidiController.getInstance().getOnAir()) {
				MidiController.getInstance().setOnAir(false);
				new RegularLightTrigger().process(null);
			}
		}

		SpotifyApi spotifyApi = MidiController.getSpotifyApi();
		if (channel == -78 && noteValue == 15) {
			if (pressedValue >= 1 && skipped) {
				skipped = false;
				SkipUsersPlaybackToNextTrackRequest skipUsersPlaybackToNextTrackRequest = spotifyApi.skipUsersPlaybackToNextTrack().build();
				skipUsersPlaybackToNextTrackRequest.execute();
			}

			if (pressedValue < 1 && !skipped) {
				skipped = true;
				GetInformationAboutUsersCurrentPlaybackRequest currentPlaybackRequest = spotifyApi.getInformationAboutUsersCurrentPlayback().build();
				CurrentlyPlayingContext currentlyPlayingContext = currentPlaybackRequest.execute();

				if (currentlyPlayingContext.getIs_playing()) {
					PauseUsersPlaybackRequest pauseUsersPlaybackRequest = spotifyApi.pauseUsersPlayback().build();
					pauseUsersPlaybackRequest.execute();
				}
			}
		}

		MidiTriggerAction midiTriggerAction = MidiTriggerRegistry.getAction(midiResult);

		if (midiTriggerAction == null) return;
		midiTriggerAction.process(midiResult);
		System.out.println("Triggered " + midiTriggerAction.getClass().getSimpleName());
	}

	@Override
	public void close() {}
}