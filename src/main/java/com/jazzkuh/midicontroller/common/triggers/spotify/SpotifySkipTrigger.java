package com.jazzkuh.midicontroller.common.triggers.spotify;

import com.jazzkuh.midicontroller.MidiController;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiTriggerAction;
import lombok.SneakyThrows;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.requests.data.player.SkipUsersPlaybackToNextTrackRequest;

public class SpotifySkipTrigger extends MidiTriggerAction {
	@Override
	@SneakyThrows
	public void process(MidiResult midiResult) {
		SpotifyApi spotifyApi = MidiController.getSpotifyApi();
		SkipUsersPlaybackToNextTrackRequest skipUsersPlaybackToNextTrackRequest = spotifyApi.skipUsersPlaybackToNextTrack().build();
		skipUsersPlaybackToNextTrackRequest.execute();
	}
}
