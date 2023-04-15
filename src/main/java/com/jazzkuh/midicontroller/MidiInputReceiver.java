package com.jazzkuh.midicontroller;

import com.jazzkuh.midicontroller.common.MidiTriggerRegistry;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiResult;
import com.jazzkuh.midicontroller.common.triggers.abstraction.MidiTriggerAction;

import javax.sound.midi.MidiMessage;
import javax.sound.midi.Receiver;

public class MidiInputReceiver implements Receiver {
	public String name;
	public MidiInputReceiver(String name) {
		this.name = name;
	}
	public void send(MidiMessage msg, long timeStamp) {
		byte[] byteArray = msg.getMessage();
		byte noteValue = byteArray[1];
		byte pressedValue = byteArray[2];
		MidiResult midiResult = new MidiResult(noteValue, pressedValue);
		System.out.println(midiResult);
		MidiTriggerAction midiTriggerAction = MidiTriggerRegistry.getAction(midiResult);

		if (midiTriggerAction == null) return;
		midiTriggerAction.process(midiResult);
		System.out.println("Triggered " + midiTriggerAction.getClass().getSimpleName());
	}
	public void close() {}
}