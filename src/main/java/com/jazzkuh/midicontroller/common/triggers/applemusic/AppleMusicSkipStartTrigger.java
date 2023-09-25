package com.jazzkuh.midicontroller.common.triggers.applemusic;

import com.jazzkuh.midicontroller.MidiController;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiTriggerAction;
import com.tagtraum.macos.music.Application;
import com.tagtraum.macos.music.Epls;
import lombok.SneakyThrows;

public class AppleMusicSkipStartTrigger extends MidiTriggerAction {
	@Override
	@SneakyThrows
	public void process(MidiResult midiResult) {
		Application appleMusic = Application.getInstance();
		if (appleMusic.getPlayerState() == Epls.PLAYING) {
			appleMusic.pause();
		} else if (MidiController.getInstance().getShouldSkipOnStart()) {
			appleMusic.nextTrack();
			if (appleMusic.getPlayerState() != Epls.PLAYING) {
				appleMusic.playpause();
			}
		} else {
			if (appleMusic.getPlayerState() != Epls.PLAYING) {
				appleMusic.playpause();
			}
		}
	}
}
