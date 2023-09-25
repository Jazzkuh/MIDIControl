package com.jazzkuh.midicontroller.common;

import com.jazzkuh.midicontroller.common.triggers.DisablePartyLightTrigger;
import com.jazzkuh.midicontroller.common.triggers.EnablePartyLightTrigger;
import com.jazzkuh.midicontroller.common.triggers.ToggleFaderSkipTrigger;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiTriggerAction;
import com.jazzkuh.midicontroller.common.triggers.applemusic.AppleMusicPauseTrigger;
import com.jazzkuh.midicontroller.common.triggers.applemusic.AppleMusicPreviousTrigger;
import com.jazzkuh.midicontroller.common.triggers.applemusic.AppleMusicSkipTrigger;

import java.util.HashMap;
import java.util.Map;

public class MidiTriggerRegistry {
	private static Map<MidiResult, Class<? extends MidiTriggerAction>> triggers = new HashMap<>();

	static {
		triggers.put(new MidiResult((byte) 7, (byte) 0), DisablePartyLightTrigger.class);
		triggers.put(new MidiResult((byte) 7, (byte) 127), EnablePartyLightTrigger.class);
		triggers.put(new MidiResult((byte) 8, (byte) 127), AppleMusicPauseTrigger.class);
		triggers.put(new MidiResult((byte) 9, (byte) 127), AppleMusicSkipTrigger.class);
		triggers.put(new MidiResult((byte) 10, (byte) 127), AppleMusicPreviousTrigger.class);
		triggers.put(new MidiResult((byte) 11, (byte) 127), ToggleFaderSkipTrigger.class);
	}

	public static void registerAction(MidiResult midiResult, Class<? extends MidiTriggerAction> triggerClass) {
		triggers.put(midiResult, triggerClass);
	}

	public static MidiTriggerAction getAction(MidiResult midiResult) {
		Class<? extends MidiTriggerAction> triggerClass = triggers.keySet().stream().filter(midiResult1 -> equals(midiResult1, midiResult)).map(triggers::get).findFirst().orElse(null);
		if (triggerClass == null) return null;

		try {
			return triggerClass.getConstructor().newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static boolean equals(MidiResult midiResult, MidiResult midiResult2) {
		return midiResult.noteValue() == midiResult2.noteValue() && midiResult.pressedValue() == midiResult2.pressedValue();
	}
}
