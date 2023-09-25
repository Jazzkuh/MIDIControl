package com.jazzkuh.midicontroller;

import com.jazzkuh.midicontroller.common.MidiTriggerRegistry;
import com.jazzkuh.midicontroller.common.triggers.OnAirLightTrigger;
import com.jazzkuh.midicontroller.common.triggers.RegularLightTrigger;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiTriggerAction;
import com.jazzkuh.midicontroller.common.triggers.applemusic.AppleMusicSkipStartTrigger;
import com.tagtraum.macos.music.Epls;
import lombok.SneakyThrows;
import se.michaelthelin.spotify.SpotifyApi;

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
		System.out.println("Channel: " + channel + " Note: " + noteValue + " Pressed: " + pressedValue);

		if (channel == -80 && noteValue == 20 && pressedValue >= 1 && pressedValue <= 6) {
			MidiController.getInstance().setPreviousButton(pressedValue);
			System.out.println("Previous button: " + pressedValue);
		}

		if (channel == -80 && noteValue == 20) {
			System.out.println(MidiController.getInstance().getPreviousButton() == 6);
			if (pressedValue == 6 || MidiController.getInstance().getPreviousButton() == 6) {
				new AppleMusicSkipStartTrigger().process(null);
				System.out.println("Skip/Start button: " + pressedValue);
			}
		}

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
		com.tagtraum.macos.music.Application appleMusic = com.tagtraum.macos.music.Application.getInstance();
		if (channel == -75 && noteValue == 15) {
			if (pressedValue >= 1 && skipped) {
				skipped = false;
				if (MidiController.getInstance().getShouldSkipOnStart()) {
					/*
					SkipUsersPlaybackToNextTrackRequest skipUsersPlaybackToNextTrackRequest = spotifyApi.skipUsersPlaybackToNextTrack().build();
					skipUsersPlaybackToNextTrackRequest.execute();
					 */
					appleMusic.nextTrack();
					if (appleMusic.getPlayerState() != Epls.PLAYING) {
						appleMusic.playpause();
					}
				} else {
					/*
					GetInformationAboutUsersCurrentPlaybackRequest currentPlaybackRequest = spotifyApi.getInformationAboutUsersCurrentPlayback().build();
					CurrentlyPlayingContext currentlyPlayingContext = currentPlaybackRequest.execute();

					if (!currentlyPlayingContext.getIs_playing()) {
						StartResumeUsersPlaybackRequest startResumeUsersPlaybackRequest = spotifyApi.startResumeUsersPlayback().build();
						startResumeUsersPlaybackRequest.execute();
					}
					 */

					if (appleMusic.getPlayerState() != Epls.PLAYING) {
						appleMusic.playpause();
					}
				}
			}

			if (pressedValue < 1 && !skipped) {
				skipped = true;
				/*
				GetInformationAboutUsersCurrentPlaybackRequest currentPlaybackRequest = spotifyApi.getInformationAboutUsersCurrentPlayback().build();
				CurrentlyPlayingContext currentlyPlayingContext = currentPlaybackRequest.execute();

				if (currentlyPlayingContext.getIs_playing()) {
					PauseUsersPlaybackRequest pauseUsersPlaybackRequest = spotifyApi.pauseUsersPlayback().build();
					pauseUsersPlaybackRequest.execute();
				}
				 */
				if (appleMusic.getPlayerState() == Epls.PLAYING) {
					appleMusic.playpause();
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