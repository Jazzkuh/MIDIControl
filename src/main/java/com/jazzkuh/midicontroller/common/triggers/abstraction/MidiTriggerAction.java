package com.jazzkuh.midicontroller.common.triggers.abstraction;

import lombok.Getter;

public abstract class MidiTriggerAction implements MidiTrigger {

    public abstract void process(MidiResult midiResult);
}