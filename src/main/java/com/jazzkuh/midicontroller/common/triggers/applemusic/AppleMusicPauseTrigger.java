package com.jazzkuh.midicontroller.common.triggers.applemusic;

import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiTriggerAction;
import com.tagtraum.macos.music.Application;
import lombok.SneakyThrows;

public class AppleMusicPauseTrigger extends MidiTriggerAction {
	@Override
	@SneakyThrows
	public void process(MidiResult midiResult) {
		Application appleMusic = Application.getInstance();
		appleMusic.playpause();
	}
}
