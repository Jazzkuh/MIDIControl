package com.jazzkuh.midicontroller.common.triggers.spotify;

import com.jazzkuh.midicontroller.MidiController;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiTriggerAction;
import lombok.SneakyThrows;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.miscellaneous.CurrentlyPlayingContext;
import se.michaelthelin.spotify.requests.data.player.GetInformationAboutUsersCurrentPlaybackRequest;
import se.michaelthelin.spotify.requests.data.player.PauseUsersPlaybackRequest;
import se.michaelthelin.spotify.requests.data.player.SkipUsersPlaybackToNextTrackRequest;
import se.michaelthelin.spotify.requests.data.player.StartResumeUsersPlaybackRequest;

public class SpotifySkipStartTrigger extends MidiTriggerAction {
	@Override
	@SneakyThrows
	public void process(MidiResult midiResult) {
		SpotifyApi spotifyApi = MidiController.getSpotifyApi();
		GetInformationAboutUsersCurrentPlaybackRequest currentPlaybackRequest = spotifyApi.getInformationAboutUsersCurrentPlayback().build();
		CurrentlyPlayingContext currentlyPlayingContext = currentPlaybackRequest.execute();

		if (currentlyPlayingContext.getIs_playing()) {
			PauseUsersPlaybackRequest pauseUsersPlaybackRequest = spotifyApi.pauseUsersPlayback().build();
			pauseUsersPlaybackRequest.execute();
		} else if (MidiController.getInstance().getShouldSkipOnStart()) {
			SkipUsersPlaybackToNextTrackRequest skipUsersPlaybackToNextTrackRequest = spotifyApi.skipUsersPlaybackToNextTrack().build();
			skipUsersPlaybackToNextTrackRequest.execute();
		} else {
			if (!currentlyPlayingContext.getIs_playing()) {
				StartResumeUsersPlaybackRequest startResumeUsersPlaybackRequest = spotifyApi.startResumeUsersPlayback().build();
				startResumeUsersPlaybackRequest.execute();
			}
		}
	}
}
